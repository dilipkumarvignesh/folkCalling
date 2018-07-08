package com.iskcon.pfh.folkcalling;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by i308830 on 8/8/17.
 */


public class CallStatusUpdate {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static ArrayList CallList = new ArrayList<CallUpdate>();

    ArrayList<Contact> contacts = new ArrayList<Contact>();
    private Context Con;
    //  CSVWriter writer;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static ArrayList<CallUpdate> CallArrayList() {
        Log.d("info", "CallListStatus1:" + CallList.size());
        return CallList;
    }

    public void writeStatus(Activity act, Contact contac, String Status, String comm, Context con, String filename, String SmsPrefix, String A1txt, Boolean A1Status, String A3txt, Boolean A3Status, String Inactivetxt, Boolean InactiveStatus) {
        String[] ids;
        BufferedReader reader;

        File SD_CARD_PATH = Environment.getExternalStorageDirectory();
        String oldLine = "";
        String fname2 = "1" + ".csv";
        try {
            File file = new File(SD_CARD_PATH, filename);
            File file1 = new File(SD_CARD_PATH, fname2);
            FileInputStream fIn = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fIn));

            BufferedWriter bw = new BufferedWriter(new FileWriter(file1));
            String csvLine;

            while ((csvLine = reader.readLine()) != null) {


                String FinalStatus = "";
                ids = csvLine.split(",");

                //   ids[2]=Status;
                Log.d("info", "Write Number:" + contac.number);
                String CallResponse = "";
                if (ids[0].equalsIgnoreCase(contac.name) && ids[1].equalsIgnoreCase(contac.number)) {
                    Log.d("info", "Write Number Inside:" + contac.number);
                    FinalStatus = getStatus(Status);
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

                    Log.d("info", "Outside switch");
                    if (ids[12].equals("NA"))   //Check if current call response is there
                    {
                        Log.d("info", "100");
                        ids[12] = getStatus(Status);

                    } else {
                        Log.d("info", "101");
                        ids[11] = ids[12];
                        ids[12] = getStatus(Status);

                    }
                    if (getDate().equals(ids[15])) {
                        Log.d("info", "102");
                        int call = Integer.parseInt(ids[17]);
                        call = call + 1;
                        ids[17] = Integer.toString(call);

                    } else {
                        Log.d("info", "104");
                        int call = 1;
                        ids[17] = Integer.toString(call);
                    }
                    Log.d("info", "105");
                    ids[13] = CallResponse;
                    comm = comm.replace(',', '.');
                    ids[14] = comm;
                    ids[15] = getDate();
                    ids[16] = currentTime();
//                    ids[19] = contac.RemainderDay;
//                    ids[20] = contac.RemainderTime;
                    csvLine = ids[0] + "," + ids[1] + "," + ids[2] + "," + ids[3] + "," + ids[4] + "," + ids[5] +
                            "," + ids[6] + "," + ids[7] + "," + ids[8] + "," + ids[9] + "," + ids[10]
                            + "," + ids[11] + "," + ids[12] + "," + ids[13] + "," + ids[14] + "," + ids[15] + "," + ids[16] + "," + ids[17] + "," + ids[18] ; //+ ids[19]+","+ids[20];
                    // csvLine=csvLine+","+Status+","+CallResponse+","+comm+","+TodayDate+","+currentTime;

                    bw.write(csvLine + "\n");
                    Log.d("info", "After write");
                } else {
                    bw.write(csvLine + "\n");
                }
                oldLine = oldLine+csvLine+System.lineSeparator();

                Log.d("Collumn 1 ", "" + ids[0] + ids[1]);
                //txtEd.setText(message);
                if (FinalStatus.equals("A1") && A1Status == true) {
                    String finalMessage = constructMessage(SmsPrefix, ids[0], A1txt);
                    sendSms(finalMessage, ids[1]);
                }

                if (FinalStatus.equals("A4") && A3Status == true) {
                    String finalMessage = constructMessage(SmsPrefix, ids[0], A3txt);
                    sendSms(finalMessage, ids[1]);
                }

                if (CallResponse.equals("Inactive") && InactiveStatus == true) {
                    String finalMessage = constructMessage(SmsPrefix, ids[0], Inactivetxt);
                    sendSms(finalMessage, ids[1]);
                }

            }


            reader.close();
            bw.close();
            file.delete();
            file1.renameTo(new File(SD_CARD_PATH, filename));
            Log.d("info","oldLine:"+oldLine);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error",e.getMessage());
        }

    }

    public void writeStatus1(Activity act, Contact contac, String Status, String comm, Context con, String filename, String SmsPrefix, String A1txt, Boolean A1Status, String A3txt, Boolean A3Status, String Inactivetxt, Boolean InactiveStatus) {
        String[] ids;
        BufferedReader reader;

        File SD_CARD_PATH = Environment.getExternalStorageDirectory();


        try {
            File file = new File(SD_CARD_PATH, filename);
           // File file1 = new File(SD_CARD_PATH, fname2);
         //   FileInputStream fIn = new FileInputStream(file);
            reader = new BufferedReader(new FileReader(file));
            FileWriter writer = null;
         //   BufferedWriter bw = new BufferedWriter(new FileWriter(file1));
            String csvLine;

            String oldLine = "";

            while ((csvLine = reader.readLine()) != null) {


                String FinalStatus = "";
                ids = csvLine.split(",");

                //   ids[2]=Status;
                Log.d("info", "Write Number:" + contac.number);
                String CallResponse = "";
                if (ids[0].equalsIgnoreCase(contac.name) && ids[1].equalsIgnoreCase(contac.number)) {
                    Log.d("info", "Write Number Inside:" + contac.number);
                    FinalStatus = getStatus(Status);
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

                    Log.d("info", "Outside switch");
                    if (ids[12].equals("NA"))   //Check if current call response is there
                    {
                        Log.d("info", "100");
                        ids[12] = getStatus(Status);

                    } else {
                        Log.d("info", "101");
                        ids[11] = ids[12];
                        ids[12] = getStatus(Status);

                    }
                    if (getDate().equals(ids[15])) {
                        Log.d("info", "102");
                        int call = Integer.parseInt(ids[17]);
                        call = call + 1;
                        ids[17] = Integer.toString(call);

                    } else {
                        Log.d("info", "104");
                        int call = 1;
                        ids[17] = Integer.toString(call);
                    }
                    Log.d("info", "105");
                    ids[13] = CallResponse;
                    comm = comm.replace(',', '.');
                    ids[14] = comm;
                    ids[15] = getDate();
                    ids[16] = currentTime();
//                    ids[19] = contac.RemainderDay;
//                    ids[20] = contac.RemainderTime;
                    csvLine = ids[0] + "," + ids[1] + "," + ids[2] + "," + ids[3] + "," + ids[4] + "," + ids[5] +
                            "," + ids[6] + "," + ids[7] + "," + ids[8] + "," + ids[9] + "," + ids[10]
                            + "," + ids[11] + "," + ids[12] + "," + ids[13] + "," + ids[14] + "," + ids[15] + "," + ids[16] + "," + ids[17] + "," + ids[18] ; //+ ids[19]+","+ids[20];
                    // csvLine=csvLine+","+Status+","+CallResponse+","+comm+","+TodayDate+","+currentTime;

                //    bw.write(csvLine + "\n");
                    Log.d("info", "After write");
                } else {
                 //   bw.write(csvLine + "\n");
                }

                oldLine=oldLine+csvLine+System.lineSeparator();

                Log.d("Collumn 1 ", "" + ids[0] + ids[1]);
                //txtEd.setText(message);
                if (FinalStatus.equals("A1") && A1Status == true) {
                    String finalMessage = constructMessage(SmsPrefix, ids[0], A1txt);
                    sendSms(finalMessage, ids[1]);
                }

                if (FinalStatus.equals("A4") && A3Status == true) {
                    String finalMessage = constructMessage(SmsPrefix, ids[0], A3txt);
                    sendSms(finalMessage, ids[1]);
                }

                if (CallResponse.equals("Inactive") && InactiveStatus == true) {
                    String finalMessage = constructMessage(SmsPrefix, ids[0], Inactivetxt);
                    sendSms(finalMessage, ids[1]);
                }

            }

            writer = new FileWriter(file);
            writer.write(oldLine);
            reader.close();
            writer.close();

            Log.d("info","oldLine:"+oldLine);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error",e.getMessage());
        }

    }

    public String constructMessage(String Prefix, String name, String Message) {
        String finalMessage = Prefix + " " + name + "," + Message;
        return finalMessage;
    }



    public ArrayList<Contact> getCallDataStatus1(String Status, Context Con, String Filename, String TeleCaller, String Day, ArrayList<String> selectedPrograms) {
        this.Con = Con;

        String[] ids;

        BufferedReader reader;

        String csvLine;
        ArrayList<Contact> contacts= new ArrayList<Contact>();
        File SD_CARD_PATH = Environment.getExternalStorageDirectory(); //.toString();
        String fname = Filename;

        contacts.clear();
        try {
            File file = new File(SD_CARD_PATH, fname);
            FileInputStream fIn = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fIn));

            while ((csvLine = reader.readLine()) != null) {
//
                Contact con = new Contact();

                ids = csvLine.split(",");
                Log.d("info", "Rowinfo:" + ids[2]);
                Log.d("info", "RowName:" + ids[0]);
                Log.d("info", "StatusValue:" + Status);

                con.name = ids[0];
                con.number = ids[1];
                con.program = ids[2];
                con.source = ids[3];
                con.ColCompany = ids[4];
                con.campaignedBy = ids[5];
                con.dor = ids[6];
                con.dop = ids[7];

                con.toc = ids[8];
                Log.d("info", "Row TimeOfCalling:" + con.toc);


                con.tc = ids[10];
                Log.d("info", "Row TC:" + con.tc);


                // con.tc = TCCell.getStringCellValue().toString();
                con.pResponse = ids[11];


                con.CallResponse = ids[12];
                con.noc = ids[17];
                Log.d("info", "No of Calls Row Response:" + con.noc);


                con.date_of_Calling = ids[15];


                // con.TotalComments = ids[20]
                Log.d("info", "CallListSize:" + CallList.size());
                if ((Status.equals("ALL") && !(con.number.isEmpty()))) {
                    contacts.add(con);
                } else {
                    Log.d("info", "Selected Programs:" + selectedPrograms);


                    if ((selectedPrograms.contains(con.program) || selectedPrograms.contains("ALL")) && (Day.equals(con.doc) || Day.equals("ALL"))) {
                        if (Status.equalsIgnoreCase("Fresh Calls")) {
                            if ((con.CallResponse.equals("NA") || (con.CallResponse.isEmpty() && !(con.number.isEmpty()))) && (con.tc.equals(TeleCaller) || TeleCaller.equals("ALL"))) {
                                contacts.add(con);
                                // sendSms("Hare Krishna <name>. Thank you for your confirmation for attending YFH",ids[0],ids[1]);
                            }
                        } else if (Status.equals("Confirmation Calls") && (con.tc.equals(TeleCaller) || TeleCaller.equals("ALL"))) {
                            if (con.CallResponse.equals("A1")) {
                                contacts.add(con);
                                Log.d("info", "Contact Call Response:" + con.CallResponse);
                                Log.d("info", "Contact added:" + con.number);
                            }
                        } else if (Status.equals("Inactive Calls") && (con.tc.equals(TeleCaller) || TeleCaller.equals("ALL"))) {
                            if (con.CallResponse.equals("B") || con.CallResponse.equals("C") || con.CallResponse.equals("Y2") || con.CallResponse.equals("E") || con.CallResponse.equals("F") ||
                                    con.CallResponse.equals("Y1")) {
                                contacts.add(con);
                            }
                        } else if (Status.equals("Tentative") && (con.tc.equals(TeleCaller) || TeleCaller.equals("ALL"))) {
                            if (con.CallResponse.equals("A4") || con.CallResponse.equals("Y1") || con.CallResponse.equals("Y2")) {
                                contacts.add(con);
                            }
                        } else if (Status.equals("Recall Inactive Numbers") && (con.tc.equals(TeleCaller) || TeleCaller.equals("ALL")) && con.date_of_Calling.equals(getDate())) {
                            Log.d("info", "Inside RecallInactive");

                            if ((con.CallResponse.equals("B") || con.CallResponse.equals("C") || con.CallResponse.equals("Y2") || con.CallResponse.equals("E") || con.CallResponse.equals("F") ||
                                    con.CallResponse.equals("Y1"))) {
                                contacts.add(con);
                            }
                        }


                    }


                }

            }

            fIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("info","Error:"+e.getMessage());
        }
        return contacts;
    }

    private String getDate() {
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat format1 = new SimpleDateFormat("d/M/yy");
        System.out.println(cal.getTime());
        String todayDate = format1.format(cal.getTime());
        Log.d("info", "TodayDate:" + todayDate);
        return todayDate;
    }

    private String currentTime() {


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(cal.getTime());
        Log.d("info", "TodayTime:" + currentTime);
        return currentTime;


    }

    public String getStatus(String Status) {
        String[] status = Status.split("[(]");
        return status[0];
    }


    public void sendSms(String Message, String number) {
        SmsManager smsManager = SmsManager.getDefault();


        smsManager.sendTextMessage(number, null, Message, null, null);

    }

    public void writeNoDelete(String filename,String oldString,String newString)
    {
        File fileToBeModified = new File(filename);

        String oldContent = "";

        BufferedReader reader = null;

        FileWriter writer = null;

        try
        {
            reader = new BufferedReader(new FileReader(fileToBeModified));

            //Reading all the lines of input text file into oldContent

            String line = reader.readLine();

            while (line != null)
            {
                oldContent = oldContent + line + System.lineSeparator();

                line = reader.readLine();
            }

            //Replacing oldString with newString in the oldContent

            String newContent = oldContent.replaceAll(oldString, newString);

            //Rewriting the input text file with newContent

            writer = new FileWriter(fileToBeModified);

            writer.write(newContent);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                //Closing the resources

                reader.close();

                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public HashMap finalReport(String filename, String TeleCaller, ArrayList<String> Program, Boolean Date) {
        File SD_CARD_PATH = Environment.getExternalStorageDirectory();
        String[] ids;
        FileInputStream iStream;
        BufferedReader reader;
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        int totalNoOfPeople = 0, totalNoOfCalls = 0, A1 = 0, Z = 0, active = 0, inActive = 0, drop = 0,
                A2 = 0, A3 = 0, A4 = 0, B = 0, C = 0, D = 0, E = 0, F = 0, G = 0, X = 0, Y1 = 0, Y2 = 0, Y3 = 0;
        String csvLine;
        try {
            File file = new File(SD_CARD_PATH, filename);
            FileInputStream fIn = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fIn));

            while ((csvLine = reader.readLine()) != null) {

                ids = csvLine.split(",");
                Log.d("info", "People:" + ids[0]);
                Log.d("info", "SelectedPrograms:" + Program);
                if ((TeleCaller.equals(ids[10]) || (TeleCaller.equals("ALL"))) && (Program.contains(ids[2]))) {
                    if (((Date == true) && ids[15].equals(getDate())) || (Date == false)) {
                        Log.d("info", "SelectedPeople:" + ids[0]);
                        totalNoOfPeople++;
                        totalNoOfCalls = totalNoOfCalls + Integer.parseInt(ids[17]);
                        switch (ids[12]) {
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

                        if (ids[13].equals("Active")) {
                            active++;
                        }
                        if (ids[13].equals("Inactive")) {
                            inActive++;
                        }
                        if (ids[13].equals("Drop")) {
                            drop++;
                        }
                    }
                }
            }

            fIn.close();
        } catch (Exception e) {
//            Toast.makeText(Con.getApplicationContext(), e.getMessage(),
//                    Toast.LENGTH_SHORT).show();
        }


        Log.d("info", "TotalNoOfPeopleCalls:" + totalNoOfPeople);
        map.put("A1", A1);
        map.put("A2", A2);
        map.put("A3", A3);
        map.put("A4", A4);
        map.put("B", B);
        map.put("C", C);
        map.put("D", D);
        map.put("E", E);
        map.put("F", F);
        map.put("G", G);
        map.put("X", X);
        map.put("Y1", Y1);
        map.put("Y2", Y2);
        map.put("Y3", Y3);


        map.put("Z", Z);
        map.put("Inactive", inActive);
        map.put("Drop", drop);
        map.put("Active", active);
        map.put("NoOfPeople", totalNoOfPeople);
        map.put("NoOfCalls", totalNoOfCalls);

        return map;

    }

    public String convertStatus(String sta) {
        String CallResponse = "";
        switch (sta) {
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
        return (CallResponse);

    }

    public ArrayList<Contact> getWhatsappMessages(String Status, String filename,ArrayList<String> selectedPrograms) throws FileNotFoundException {


        String[] ids;

        BufferedReader reader;

        String csvLine;

        Log.d("info", "CurrentStatus" + Status);
        File SD_CARD_PATH = Environment.getExternalStorageDirectory(); //.toString();
        String fname = filename;
        ArrayList<Contact> whatsappContacts = new ArrayList<Contact>();

        try {
            File file = new File(SD_CARD_PATH, fname);
            FileInputStream fIn = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fIn));

            while ((csvLine = reader.readLine()) != null) {

                Contact con = new Contact();

                ids = csvLine.split(",");
                Log.d("info", "Rowinfo:" + con);
                Log.d("info", "RowName:" + ids[0]);
                Log.d("info", "StatusValue:" + Status);

                con.name = ids[0];
                con.number = ids[1];

                con.CallResponse = ids[12];
                con.program = ids[2];

                if(selectedPrograms.isEmpty())
                {
                    selectedPrograms.add("ALL");
                }

                String finalStatus = getStatus(Status);
                Log.d("info", "Final Status:" + finalStatus);

                    if (Status.equals("ALL") && !(con.number.isEmpty())&&(selectedPrograms.contains(con.program)||selectedPrograms.contains("ALL"))) {
                        whatsappContacts.add(con);
                    } else if (con.CallResponse.equals(finalStatus)&&(selectedPrograms.contains(con.program)||selectedPrograms.contains("ALL"))) {
                        whatsappContacts.add(con);
                    } else if (Status.equals("Inactive Calls")&&(selectedPrograms.contains(con.program)||selectedPrograms.contains("ALL"))) {
                        if (con.CallResponse.equals("B") || con.CallResponse.equals("C") || con.CallResponse.equals("Y2") || con.CallResponse.equals("E") || con.CallResponse.equals("F") ||
                                con.CallResponse.equals("Y1")) {
                            contacts.add(con);
                        }
                    } else if (Status.equals("Tentative")&&(selectedPrograms.contains(con.program)||selectedPrograms.contains("ALL"))) {
                        if (con.CallResponse.equals("A4") || con.CallResponse.equals("Y1") || con.CallResponse.equals("Y2")) {
                            whatsappContacts.add(con);
                        }
                    } else if (Status.equals("Active")&&(selectedPrograms.contains(con.program)||selectedPrograms.contains("ALL"))) {
                        if (con.CallResponse.equals("A1") || con.CallResponse.equals("A2") || con.CallResponse.equals("A3") || con.CallResponse.equals("A4")) {
                            whatsappContacts.add(con);
                        }
                    }
                }
                fIn.close();
                Log.d("info", "whatsappContacts:" + whatsappContacts.size());
            } catch (IOException e1) {
            e1.printStackTrace();
        }

        return whatsappContacts;
        }
    }

