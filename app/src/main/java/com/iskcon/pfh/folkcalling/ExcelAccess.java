package com.iskcon.pfh.folkcalling;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by i308830 on 11/13/17.
 */

public class ExcelAccess {
    ArrayList<Contact> contacts = new ArrayList<Contact>();
    ArrayList<Contact> whatsappContacts = new ArrayList<Contact>();
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_DOCUMENTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.SYSTEM_ALERT_WINDOW

    };

    public ArrayList<Contact> getWhatsappMessages(Context cont,String Status,String filename) throws FileNotFoundException {


        //Log.d("info","Input Stream:"+is);

        String finalStatus = getStatus(Status);
        Log.d("info","Final Status:"+finalStatus);
        try {
            File SD_CARD_PATH = Environment.getExternalStorageDirectory();
            File file = new File(SD_CARD_PATH, filename);
            FileInputStream fIn = new FileInputStream(file);
            // InputStream is = cr.openInputStream(fIn);
            XSSFWorkbook workbook = new XSSFWorkbook(fIn);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            Log.d("info", "Row Count:" + rowsCount);
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int r = 1; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                Contact con = new Contact();

                Cell NameCell = sheet.getRow(r).getCell(0,row.CREATE_NULL_AS_BLANK);
                //  con.name = NameCell.getStringCellValue().toString();
                con.name = formatter.formatCellValue(NameCell);
                Log.d("info","Row name:"+con.name);

                Cell NumberCell = sheet.getRow(r).getCell(1,row.CREATE_NULL_AS_BLANK);
                //  con.name = NameCell.getStringCellValue().toString();
                con.number = formatter.formatCellValue(NumberCell);
                Log.d("info","Row name:"+con.number);

                Cell CallResponseCell = sheet.getRow(r).getCell(12,row.CREATE_NULL_AS_BLANK);
                //  con.CallResponse = CallResponseCell.getStringCellValue().toString();
                con.CallResponse = formatter.formatCellValue(CallResponseCell);
                Log.d("info","Row Response:"+con.CallResponse);
                if(Status.equals("ALL")&&!(con.number.isEmpty()))
                {
                    whatsappContacts.add(con);
                }
                else if(con.CallResponse.equals(finalStatus))
                {
                    whatsappContacts.add(con);
                }
                else if (Status.equals("Inactive Calls")  ) {
                    if (con.CallResponse.equals("B") || con.CallResponse.equals("C") || con.CallResponse.equals("Y2") || con.CallResponse.equals("E") || con.CallResponse.equals("F") ||
                            con.CallResponse.equals("Y1")) {
                        contacts.add(con);
                    }
                } else if (Status.equals("Tentative")  ) {
                    if (con.CallResponse.equals("A4") || con.CallResponse.equals("Y1") || con.CallResponse.equals("Y2")) {
                        whatsappContacts.add(con);
                    }
                }

                else if (Status.equals("Active")  ) {
                    if (con.CallResponse.equals("A1") || con.CallResponse.equals("A2") || con.CallResponse.equals("A3")|| con.CallResponse.equals("A4")) {
                        whatsappContacts.add(con);
                    }
                }
            }
            fIn.close();
            Log.d("info","whatsappContacts:"+whatsappContacts.size());
        }
        catch (Exception e) {
            /* proper exception handling to be here */
            Log.d("info","Error to File");
            Toast.makeText(cont,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return whatsappContacts;
    }

    public ArrayList<Contact> fileResource(String Status,Context cont,String filename, String TeleCaller, String Day,ArrayList<String> selectedPrograms) throws FileNotFoundException {
        ContentResolver cr = cont.getContentResolver();

        //Log.d("info","Input Stream:"+is);
        Log.d("info","Day of Calling Excel:"+Day);

        contacts.clear();
        try {
            File SD_CARD_PATH = Environment.getExternalStorageDirectory();
            File file = new File(SD_CARD_PATH, filename);
            FileInputStream fIn = new FileInputStream(file);
           // InputStream is = cr.openInputStream(fIn);
            XSSFWorkbook workbook = new XSSFWorkbook(fIn);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            Log.d("info","Row Count:"+rowsCount);
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int r = 1; r<rowsCount-1; r++) {
                Row row = sheet.getRow(r);

                Contact con = new Contact();

                Cell NameCell = sheet.getRow(r).getCell(0,row.CREATE_NULL_AS_BLANK);
              //  con.name = NameCell.getStringCellValue().toString();
                con.name = formatter.formatCellValue(NameCell);
                Log.d("info","Row name:"+con.name);

                Cell NumberCell = sheet.getRow(r).getCell(1,row.CREATE_NULL_AS_BLANK);
                //  con.name = NameCell.getStringCellValue().toString();
                con.number = formatter.formatCellValue(NumberCell);
                Log.d("info","Row name:"+con.number);

                Cell ProgramCell = sheet.getRow(r).getCell(2,row.CREATE_NULL_AS_BLANK);
            //    con.program = ProgramCell.getStringCellValue().toString();
                con.program = formatter.formatCellValue(ProgramCell);
                Log.d("info","Row program:"+con.program);

                Cell SourceCell = sheet.getRow(r).getCell(3,row.CREATE_NULL_AS_BLANK);
               // con.source = SourceCell.getStringCellValue().toString();
                con.source = formatter.formatCellValue(SourceCell);
                Log.d("info","Row source:"+con.source);

                Cell CollCompanyCell = sheet.getRow(r).getCell(4,row.CREATE_NULL_AS_BLANK);
              //  con.ColCompany = CollCompanyCell.getStringCellValue();
                con.ColCompany = formatter.formatCellValue(CollCompanyCell);
                Log.d("info","Row source:"+con.source);

                Cell CampaignedCell = sheet.getRow(r).getCell(5,row.CREATE_NULL_AS_BLANK);
               // con.campaignedBy = CampaignedCell.getStringCellValue().toString();
                con.campaignedBy = formatter.formatCellValue(CampaignedCell);
                Log.d("info","Row campaigned:"+con.campaignedBy);

                Cell DORCell = sheet.getRow(r).getCell(6,row.CREATE_NULL_AS_BLANK);
             //   con.dor = DORCell.getStringCellValue().toString();
                con.dor = formatter.formatCellValue(DORCell);
                Log.d("info","Row dor:"+con.dor);

                Cell DOPCell = sheet.getRow(r).getCell(7,row.CREATE_NULL_AS_BLANK);
               // con.dop = DOPCell.getStringCellValue().toString();
                con.dop = formatter.formatCellValue(DOPCell);
                Log.d("info","Row DOP:"+con.dop);

                Cell DayOfCallingCell = sheet.getRow(r).getCell(8,row.CREATE_NULL_AS_BLANK);
                // con.dop = DOPCell.getStringCellValue().toString();
                con.doc = formatter.formatCellValue(DayOfCallingCell);
                Log.d("info","Row DOC:"+con.doc);

                Cell TimeOfCallingCell = sheet.getRow(r).getCell(9,row.CREATE_NULL_AS_BLANK);
                // con.toc = TimeOfCallingCell.getStringCellValue().toString();
                con.toc = formatter.formatCellValue(TimeOfCallingCell);
                Log.d("info","Row TimeOfCalling:"+con.toc);

                Cell TCCell = sheet.getRow(r).getCell(10,row.CREATE_NULL_AS_BLANK);
                // con.tc = TCCell.getStringCellValue().toString();
                con.tc = formatter.formatCellValue(TCCell);
                Log.d("info","Row TC:"+con.tc);

                Cell PreviousResCell = sheet.getRow(r).getCell(11,row.CREATE_NULL_AS_BLANK);
                // con.tc = TCCell.getStringCellValue().toString();
                con.pResponse = formatter.formatCellValue(PreviousResCell);
                Log.d("info","Previous Res :"+con.pResponse);

                Cell CallResponseCell = sheet.getRow(r).getCell(12,row.CREATE_NULL_AS_BLANK);
                Log.d("info","Contact Response Excel:"+formatter.formatCellValue(CallResponseCell));
                con.CallResponse = formatter.formatCellValue(CallResponseCell);
                Log.d("info","Row Response:"+con.CallResponse);

                Cell NoOfCallsCell = sheet.getRow(r).getCell(17,row.CREATE_NULL_AS_BLANK);
                //  con.CallResponse = CallResponseCell.getStringCellValue().toString();
                con.noc = formatter.formatCellValue(NoOfCallsCell);
                Log.d("info","No of Calls Row Response:"+con.noc);

                Cell DateOfCallingCell = sheet.getRow(r).getCell(15,row.CREATE_NULL_AS_BLANK);
             //   con.date_of_Calling = DateOfCallingCell.getStringCellValue().toString();
                con.date_of_Calling = formatter.formatCellValue(DateOfCallingCell);
                Log.d("info","Row date_of_Calling:"+con.date_of_Calling);

                Cell TotalComments = sheet.getRow(r).getCell(20,row.CREATE_NULL_AS_BLANK);
                //   con.date_of_Calling = DateOfCallingCell.getStringCellValue().toString();
                con.TotalComments = formatter.formatCellValue(TotalComments);
                Log.d("info","Row date_of_Calling:"+con.TotalComments);

                if((Status.equals("ALL")&&!(con.number.isEmpty()) )) {
                    contacts.add(con);
                }
                    else
                {
                    Log.d("info","Con.number:"+con.number);
                    Log.d("info","Con.name:"+con.name);

                if ((selectedPrograms.contains(con.program)||selectedPrograms.contains("ALL"))&&(Day.equals(con.doc)||Day.equals("ALL")))
                {
                    if (Status.equalsIgnoreCase("Fresh Calls")) {
                        if ((con.CallResponse.equals("NA")||(con.CallResponse.isEmpty()&&!(con.number.isEmpty()))) && (con.tc.equals(TeleCaller) || TeleCaller.equals("ALL"))) {
                          contacts.add(con);
                            // sendSms("Hare Krishna <name>. Thank you for your confirmation for attending YFH",ids[0],ids[1]);
                        }
                    } else if (Status.equals("Confirmation Calls") && (con.tc.equals(TeleCaller) || TeleCaller.equals("ALL")) ) {
                        if (con.CallResponse.equals("A1")) {
                            contacts.add(con);
                            Log.d("info","Contact Call Response:"+con.CallResponse);
                            Log.d("info","Contact added:"+con.number);
                        }
                    } else if (Status.equals("Inactive Calls") && (con.tc.equals(TeleCaller) || TeleCaller.equals("ALL")) ) {
                        if (con.CallResponse.equals("B") || con.CallResponse.equals("C") || con.CallResponse.equals("Y2") || con.CallResponse.equals("E") || con.CallResponse.equals("F") ||
                                con.CallResponse.equals("Y1")) {
                            contacts.add(con);
                        }
                    } else if (Status.equals("Tentative") && (con.tc.equals(TeleCaller) || TeleCaller.equals("ALL")) ) {
                        if (con.CallResponse.equals("A4") || con.CallResponse.equals("Y1") || con.CallResponse.equals("Y2")) {
                        contacts.add(con);
                        }
                    } else if (Status.equals("Recall Inactive Numbers") && (con.tc.equals(TeleCaller) || TeleCaller.equals("ALL")) && con.date_of_Calling.equals(getDate())) {
                        Log.d("info", "Inside RecallInactive");

                        if ((con.CallResponse.equals("B") || con.CallResponse.equals("C") || con.CallResponse.equals("Y2") || con.CallResponse.equals("E") || con.CallResponse.equals("F") ||
                                con.CallResponse.equals("Y1")) ) {
                        contacts.add(con);
                        }
                    }
                    con = null;

                }





        }}
        fIn.close();
        }catch (Exception e) {
            /* proper exception handling to be here */
            Log.d("info","Error to File");
            Toast.makeText(cont,"Reading Error. File Read problem"+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return contacts;
    }


    public void onWriteClick(Activity act,Contact contac, String Status, String comm, Context con, String filename, String SmsPrefix, String A1txt, Boolean A1Status, String A3txt, Boolean A3Status, String Inactivetxt, Boolean InactiveStatus) {
        printlnToUser("writing xlsx file");
        File SD_CARD_PATH = Environment.getExternalStorageDirectory();

        try{

            File file = new File(SD_CARD_PATH, filename);
            //File file1 = new File(SD_CARD_PATH, fname2);
            FileInputStream fIn = new FileInputStream(file);

//            ContentResolver cr = con.getContentResolver();
            Log.d("info","Input FileName:" +filename);
           // InputStream is = cr.openInputStream(Uri.parse(filename));

            // InputStream stream = con.getResources().openRawResource(R.raw.mtmw);
            // OutputStream oStream = getResources().openRawResource(R.raw.mtmw);
            // XSSFWorkbook workbook = new XSSFWorkbook(stream);

            XSSFWorkbook workbook = new XSSFWorkbook(fIn);


            Log.d("info","workbook info:"+workbook.toString());
            XSSFSheet sheet = workbook.getSheetAt(0);
            Log.d("info","Sheet info:"+sheet.toString());
            Log.d("info","RowSheetValue:"+sheet.getRow(1));
            Log.d("info","RowSheetValue1:"+sheet.getRow(1).getCell(1));
          //  Log.d("info","RowSheetValue2:"+sheet.getRow(1).getCell(1).getStringCellValue());
            DataFormatter formatter = new DataFormatter();
            int rowsCount = sheet.getPhysicalNumberOfRows();

            for(int i=1;i<rowsCount;i++) {
                Row row = sheet.getRow(i);
                //   Cell called = row.getCell(2,row.CREATE_NULL_AS_BLANK);
                String number = formatter.formatCellValue(sheet.getRow(i).getCell(1));
                String name = formatter.formatCellValue(sheet.getRow(i).getCell(0));

                Log.d("info", "Excel Mum:" + number);
             //   Log.d("info", "Input Num:" + Number);


                // String numberValue=number.getStringCellValue();
                //  Log.d("info","Input Number:"+number.getStringCellValue());
                if (number.equals(contac.number) && name.equals(contac.name)) {
                    Log.d("info", "Excel Name:" + name);
                    Log.d("info", "Input Name:" + contac.name);
                    Log.d("info","ExcelStatus:"+Status);

                    String FinalStatus = getStatus(Status);

                    Log.d("info","ExcelFinalStatus:"+FinalStatus);
                    String CallResponse = "";
                    // csvLine=String.join(",",ids);

                    switch (FinalStatus) {
                        case "A1":
                            CallResponse = "Active";

                            break;
                        case "A2":
                            CallResponse = "Active";

                            break;
                        case "A3":
                            CallResponse = "Active";

                            break;
                        case "A4":
                            CallResponse = "Active";

                            break;
                        case "B":
                            CallResponse = "Inactive";

                            break;
                        case "C":
                            CallResponse = "Inactive";

                            break;
                        case "D":
                            CallResponse = "Drop";

                            break;
                        case "E":
                            CallResponse = "Inactive";

                            break;
                        case "F":
                            CallResponse = "Inactive";

                            break;
                        case "G":
                            CallResponse = "Drop";

                            break;
                        case "X":
                            CallResponse = "Drop";

                            break;
                        case "Y1":
                            CallResponse = "Inactive";

                            break;
                        case "Y2":
                            CallResponse = "Inactive";

                            break;
                        case "Y3":
                            CallResponse = "Inactive";


                            Log.d("info", "Y3 Response Selected");
                            break;
                        case "Z":
                            CallResponse = "Drop";

                            break;


                    }
//                    String TodayDate = getDate();
//                    String currentTime = currentTime();
                    Log.d("info", "Outside switch");
                    if (contac.CallResponse.equals("NA"))   //Check if current call response is there
                    {
                        Log.d("info", "100");
                        contac.CallResponse = getStatus(Status);

                    } else {
                        Log.d("info", "101");
                        contac.pResponse = contac.CallResponse;
                        contac.CallResponse = getStatus(Status);


                    }
                    if (getDate().equals(contac.date_of_Calling)) {
                        Log.d("info","CallUpdateNo's:");
                        int call = Integer.parseInt(contac.noc);
                        Log.d("info","CallUpdateNumberss:"+call);
                        call = call + 1;
                        contac.noc = Integer.toString(call);
                        Log.d("info","CallUpdateNumberssToString:"+call);
                        Log.d("info", "102");

                    } else {
                        Log.d("info", "104");
                        String call = "1";
                        contac.noc = call;
                    }
                    Log.d("info", "105");
                    contac.CallStatus = CallResponse;


                    Log.d("info","Total Comments:"+contac.TotalComments);
                    contac.Comments = comm;

                    if(contac.TotalComments !=null) {
                        contac.TotalComments = contac.TotalComments + contac.Comments;
                    }
                    else
                        contac.TotalComments = contac.Comments;
                    contac.date_of_Calling = getDate();
                    contac.dTime = currentTime();
                    Cell previousResponseCell = row.getCell(11, row.CREATE_NULL_AS_BLANK);
                    Cell CallResponseCell = row.getCell(12, row.CREATE_NULL_AS_BLANK);
                    Cell CallStatusCell = row.getCell(13, row.CREATE_NULL_AS_BLANK);
                    Cell commentsCell = row.getCell(14, row.CREATE_NULL_AS_BLANK);
                    Cell dateCell = row.getCell(15, row.CREATE_NULL_AS_BLANK);
                    Cell timeCell = row.getCell(16, row.CREATE_NULL_AS_BLANK);
                    Cell nocCell = row.getCell(17, row.CREATE_NULL_AS_BLANK);
                    Cell remainderDateCell = row.getCell(18, row.CREATE_NULL_AS_BLANK);
                    Cell remainderTimeCell = row.getCell(19, row.CREATE_NULL_AS_BLANK);
                    Cell TotalCommentsCell = row.getCell(20, row.CREATE_NULL_AS_BLANK);

                    previousResponseCell.setCellValue(contac.pResponse);
                    CallResponseCell.setCellValue(contac.CallResponse);
                    CallStatusCell.setCellValue(contac.CallStatus);
                    commentsCell.setCellValue(contac.Comments);
                    dateCell.setCellValue(contac.date_of_Calling);
                    timeCell.setCellValue(contac.dTime);
                    nocCell.setCellValue(contac.noc);
                    TotalCommentsCell.setCellValue(contac.TotalComments);
                    remainderDateCell.setCellValue(contac.RemainderDay);
                    remainderTimeCell.setCellValue(contac.RemainderTime);

                   break;
                }


            }
            fIn.close();
            FileOutputStream fOut = new FileOutputStream(file);
            workbook.write(fOut);

          //  outputStream.flush();
            fOut.close();
            printlnToUser("sharing file...");
            //  share(outFileName, getApplicationContext());
        } catch (Exception e) {
            final Activity act1 = act;
            final Exception exp = e;
            /* proper exception handling to be here */
            printlnToUser(e.toString());
            act1.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(act1,"error in excel Write:"+exp.toString() , Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    private String getDate() {
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat format1 = new SimpleDateFormat("d/M/yy");
        System.out.println(cal.getTime());
        String todayDate = format1.format(cal.getTime());
        Log.d("info", "TodayDate:" + todayDate);
        return todayDate;
    }
    /**
     * print line to the output TextView
     * @param str
     */
    private void printlnToUser(String str) {
        final String string = str;
//        if (output.length()>8000) {
//            CharSequence fullOutput = output.getText();
//            fullOutput = fullOutput.subSequence(5000,fullOutput.length());
//            output.setText(fullOutput);
//            output.setSelection(fullOutput.length());
//        }
//        output.append(string+"\n");
    }



    public String getStatus(String Status) {
        String[] status = Status.split("[(]");
        return status[0];
    }

    private String currentTime() {


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(cal.getTime());
        Log.d("info", "TodayTime:" + currentTime);
        return currentTime;


    }

    public HashMap finalReport(Context con,String filename, String TeleCaller, ArrayList<String> Program, Boolean Date) throws IOException {

        File SD_CARD_PATH = Environment.getExternalStorageDirectory();
        File file = new File(SD_CARD_PATH, filename);
        FileInputStream fIn = null;
        try {
            fIn = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(con,"File Not found ",Toast.LENGTH_LONG).show();
        }
        // InputStream is = cr.openInputStream(fIn);
        XSSFWorkbook workbook = new XSSFWorkbook(fIn);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowsCount = sheet.getPhysicalNumberOfRows();
        Log.d("info","Row Count:"+rowsCount);
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

        DataFormatter formatter = new DataFormatter();


        HashMap<String, Integer> map = new HashMap<String, Integer>();

        int totalNoOfPeople=0,totalNoOfCalls=0,A1=0,Z=0,active = 0,inActive = 0,drop = 0,
                A2=0,A3=0,A4=0,B=0,C=0,D=0,E=0,F=0,G=0,X=0,Y1=0,Y2=0,Y3=0;

        try {


            for (int r = 1; r<rowsCount; r++) {
                Row row = sheet.getRow(r);
                Cell ProgramCell = sheet.getRow(r).getCell(2,row.CREATE_NULL_AS_BLANK);
                formatter.formatCellValue(ProgramCell);
                String ProgramName = formatter.formatCellValue(ProgramCell);
                Log.d("info","Rows program:"+ProgramName);

                Cell TCCell = sheet.getRow(r).getCell(10,row.CREATE_NULL_AS_BLANK);
                String TCaller =  formatter.formatCellValue(TCCell);
                Log.d("info","Report telecaller:"+TCaller);

                Cell DateCell = sheet.getRow(r).getCell(15,row.CREATE_NULL_AS_BLANK);
                String DateofCall = formatter.formatCellValue(DateCell);
                Log.d("info","Report Date:"+DateofCall);
                Log.d("info","Report Today Date:"+getDate());
                Cell CallResponseCell = sheet.getRow(r).getCell(12,row.CREATE_NULL_AS_BLANK);
                String CallResponse =  formatter.formatCellValue(CallResponseCell);

                Cell NoOfCallsCell = sheet.getRow(r).getCell(17,row.CREATE_NULL_AS_BLANK);
                String NoOfCalls =  formatter.formatCellValue(NoOfCallsCell);

                Cell CallStatusCell = sheet.getRow(r).getCell(13,row.CREATE_NULL_AS_BLANK);
                String CallStatus =  formatter.formatCellValue(CallStatusCell);
                Log.d("info","SelectedPrograms:"+Program);


                if ((TeleCaller.equals(TCaller)||(TeleCaller.equals("ALL")))&&(Program.contains(ProgramName)||Program.isEmpty()))
                {
                    if( ((Date==true) && DateofCall.equals(getDate())) || (Date==false) )
                    {
                     //   Log.d("info","SelectedPeople:"+ids[0]);
                        Log.d("info","Inside Report Conditions");
                        totalNoOfPeople++;
                        totalNoOfCalls = totalNoOfCalls + Integer.parseInt(NoOfCalls);
                        switch (CallResponse) {
                            case "A1":
                                A1++;

                                break;
                            case "A2":
                                A2++;

                                break;
                            case "A3":
                                A3++;
                                break;
                            case "A4":
                                A4++;

                                break;
                            case "B":
                                B++;
                                break;
                            case "C":
                                C++;

                                break;
                            case "D":
                                D++;
                                break;
                            case "E":
                                E++;
                                break;
                            case "F":
                                F++;
                                break;
                            case "G":
                                G++;
                                break;
                            case "X":
                                X++;

                                break;
                            case "Y1":
                                Y1++;

                                break;
                            case "Y2":
                                Y2++;

                                break;
                            case "Y3":
                                Y3++;


                                Log.d("info", "Y3 Response Selected");
                                break;
                            case "Z":
                                Z++;

                                break;


                        }

                        if(CallStatus.equals("Active"))
                        {
                            active++;
                        }
                        if(CallStatus.equals("Inactive"))
                        {
                            inActive++;
                        }
                        if(CallStatus.equals("Drop"))
                        {
                            drop++;
                        }
                    }}
            }

        } catch (Exception e) {
            Toast.makeText(con.getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        Log.d("info","TotalNoOfPeopleCalls:"+totalNoOfPeople);
        map.put("A1",A1);
        map.put("A2",A2);
        map.put("A3",A3);
        map.put("A4",A4);
        map.put("B",B);
        map.put("C",C);
        map.put("D",D);
        map.put("E",E);
        map.put("F",F);
        map.put("G",G);
        map.put("X",X);
        map.put("Y1",Y1);
        map.put("Y2",Y2);
        map.put("Y3",Y3);


        map.put("Z",Z);
        map.put("Inactive",inActive);
        map.put("Drop",drop);
        map.put("Active",active);
        map.put("NoOfPeople",totalNoOfPeople);
        map.put("NoOfCalls",totalNoOfCalls);

        return map;

    }

}
