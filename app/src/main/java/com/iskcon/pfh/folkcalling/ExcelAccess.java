package com.iskcon.pfh.folkcalling;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by i308830 on 11/13/17.
 */

public class ExcelAccess {
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_DOCUMENTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.SYSTEM_ALERT_WINDOW

    };
    public ArrayList<Contact> fileResource(String filename, Context cont) throws FileNotFoundException {
        ContentResolver cr = cont.getContentResolver();

        //Log.d("info","Input Stream:"+is);
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        try {
            File SD_CARD_PATH = Environment.getExternalStorageDirectory();
            File file = new File(SD_CARD_PATH, filename);
            FileInputStream fIn = new FileInputStream(file);
           // InputStream is = cr.openInputStream(fIn);
            XSSFWorkbook workbook = new XSSFWorkbook(fIn);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int r = 1; r<rowsCount; r++) {
                Row row = sheet.getRow(r);
                String name="",number="",status="";
                int cellsCount = row.getPhysicalNumberOfCells();
                Contact con = new Contact(name,number,status);

                Cell StatusCell = sheet.getRow(r).getCell(2);
                con.Status = StatusCell.getStringCellValue().toString();
                Log.d("info","Status Value:"+con.Status);
//
//                Cell nameCell = sheet.getRow(r).getCell(0);
//                con.name = nameCell.getStringCellValue();
//                Log.d("info","Name Value:"+con.name);
//                Cell numberCell = sheet.getRow(r).getCell(1);
//                con.number = numberCell.getStringCellValue();
//                Log.d("info","Number Value:"+con.number);
//                if ((con.Status).equals("NA")) {
//                    Cell nameCell = sheet.getRow(r).getCell(0);
//                    con.name = nameCell.getStringCellValue();
//
//                    Cell numberCell = sheet.getRow(r).getCell(1);
//                    con.number = numberCell.getStringCellValue().toString();
//                    contacts.add(con);
//
//                    Log.d("info","Contact Name:"+con.name);
//                }
                for (int c = 0; c<cellsCount; c++) {
                    String value = getCellAsString(row, c, formulaEvaluator);

                    String cellInfo = "r:"+r+"; c:"+c+"; v:"+value;
                    if (c==0)
                    {

                        con.name = value;
                    }
                    else if (c==1)
                    {

                        String num = value.replace(".","").replace("E9","");
                        if(num.length() == 9)
                            num = num+'0';
                        //Log.d("info","Numbers Excel:"+num);
                        con.number = num;
                    }
                    else if (c==2)
                    {
                        if(value.equals("NA")){
                            con.Status = value;
                            contacts.add(con);
                        }
                    }

//                  //  printlnToUser(cellInfo);
//                }contacts.add(con);

            }



        }
        fIn.close();
        }catch (Exception e) {
            /* proper exception handling to be here */
            Log.d("info","Error to File");
        }

        return contacts;
    }
    public void onReadClick(Context context) {
        printlnToUser("reading XLSX file from resources");
      //  InputStream stream = context.getResources().openRawResource(R.raw.mtmw);
//        ArrayList<Contact> contacts = new ArrayList<Contact>();
//
//        try {
//            XSSFWorkbook workbook = new XSSFWorkbook(stream);
//            XSSFSheet sheet = workbook.getSheetAt(0);
//            int rowsCount = sheet.getPhysicalNumberOfRows();
//            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
//            for (int r = 1; r<rowsCount; r++) {
//                Row row = sheet.getRow(r);
//                String name = "", number = "", status = "";
//                int cellsCount = row.getPhysicalNumberOfCells();
//                Contact con = new Contact(name, number, status);
//
//                Cell StatusCell = sheet.getRow(r).getCell(2);
//                con.Status = StatusCell.getStringCellValue();
//
//                if (con.Status.equals("NA")) {
//                    Cell nameCell = sheet.getRow(r).getCell(0);
//                    con.name = nameCell.getStringCellValue();
//
//                    Cell numberCell = sheet.getRow(r).getCell(1);
//                    con.number = numberCell.getStringCellValue().toString();
//                    contacts.add(con);
//                }
//            }
////                for (int c = 0; c<cellsCount; c++) {
//                    String value = getCellAsString(row, c, formulaEvaluator);
//
//                    String cellInfo = "r:"+r+"; c:"+c+"; v:"+value;
//                    if (c==0)
//                    {
//
//                        con.name = value;
//                    }
//                    else if (c==1)
//                    {
//                        String num = value.replace(".","").replace("E9","");
//
//
//                        con.number = num;
//                    }
//
//
//                    printlnToUser(cellInfo);
//                }contacts.add(con);





//        } catch (Exception e) {
//            /* proper exception handling to be here */
//            printlnToUser(e.toString());
//        }
    }

    public void onWriteClick(Context cont, String Name, String Number, String filename, String Comments, String stat, int position) {
        printlnToUser("writing xlsx file");
        File SD_CARD_PATH = Environment.getExternalStorageDirectory();

        try{

            File file = new File(SD_CARD_PATH, filename);
            //File file1 = new File(SD_CARD_PATH, fname2);
            FileInputStream fIn = new FileInputStream(file);

            ContentResolver cr = cont.getContentResolver();
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

            for(int i=1;i<rowsCount;i++)
            {
                Row row = sheet.getRow(i);
            Cell called = row.getCell(2,row.CREATE_NULL_AS_BLANK);
            String number = formatter.formatCellValue(sheet.getRow(i).getCell(1));
            Log.d("info","Excel Mum:"+number);
            Log.d("info","Input Num:"+Number);
            Toast.makeText(cont,"ExcelNum:"+number, Toast.LENGTH_LONG);
            Toast.makeText(cont,"InputNum:"+Number, Toast.LENGTH_LONG);


           // String numberValue=number.getStringCellValue();
          //  Log.d("info","Input Number:"+number.getStringCellValue());
            if(number.equals(Number)) {
                called.setCellValue("Called");

                Cell status = row.getCell(3,row.CREATE_NULL_AS_BLANK);
                     Log.d("info","Inside Number Check;");

                status.setCellValue(stat);

                Cell comments = row.getCell(4,row.CREATE_NULL_AS_BLANK);
                //     Log.d("info","Input Number:"+status.getStringCellValue());

                comments.setCellValue(Comments);
            }}
//            Cell comments = sheet.getRow(position).getCell(3);
//            comments.setCellValue(Comments);

//            Uri fileUri = Uri.parse(filename);
//            String outFileName = "filetoshare.xlsx";
//            File filewro = new File(fileUri.getPath());

           // printlnToUser("writing file " + outFileName);
           // File cacheDir = getCacheDir();
           // File outFile = new File(cacheDir, outFileName);
           // File SD_CARD_PATH = Environment.getExternalStorageDirectory();
            File file1 = new File(SD_CARD_PATH, "Output1.xlsx");
           // cont.grantUriPermission(cont.getPackageName(), fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
         //   cr.takePersistableUriPermission(fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //  OutputStream outputStream = new FileOutputStream(outFile.getAbsolutePath());
           // OutputStream outputStream = new FileOutputStream(file1.getAbsolutePath());
          //  OutputStream outputStream =  cr.openOutputStream(Uri.parse(filename));
//            OutputStream outputStream =  cr.openOutputStream(fileUri);
           // OutputStream outputStream = new FileOutputStream(filewro);
//            Log.d("info","filepath:"+file1.getAbsolutePath().toString());
            fIn.close();
            FileOutputStream fOut = new FileOutputStream(file);
            workbook.write(fOut);

          //  outputStream.flush();
            fOut.close();
            printlnToUser("sharing file...");
            //  share(outFileName, getApplicationContext());
        } catch (Exception e) {
            /* proper exception handling to be here */
            printlnToUser(e.toString());
            Toast.makeText(cont,"error in exel:"+e.toString() , Toast.LENGTH_LONG).show();
        }
    }

    protected String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("dd/MM/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = ""+numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            printlnToUser(e.toString());
        }
        return value;
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

    public void share(String fileName, Context context) {
//        Uri fileUri = Uri.parse("content://"+getPackageName()+"/"+fileName);
//        printlnToUser("sending "+fileUri.toString()+" ...");
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
//        shareIntent.setType("application/octet-stream");
      //  startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }

}
