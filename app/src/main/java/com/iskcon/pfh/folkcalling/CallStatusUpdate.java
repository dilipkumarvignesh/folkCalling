package com.iskcon.pfh.folkcalling;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by i308830 on 8/8/17.
 */

public class CallStatusUpdate {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    int finalReport[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int totalCallCount = -1;
    //  CSVWriter writer;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public void writeStatus(String Number, String Status,String comm, Context con,String filename)
    {
        InputStream inputStream;
        String[] ids;
        FileInputStream iStream;
        BufferedReader reader;
        String message;

        File SD_CARD_PATH = Environment.getExternalStorageDirectory(); //.toString();
      //  Log.d("info","SDCARDPATH:"+SD_CARD_PATH,filename);
        //File yourFile = new File(SD_CARD_PATH, "/Music/.csv");
        //new File(SD_CARD_PATH + "/Music/" + "Contacts.csv");
        String fname = filename +".csv";
        String fname2 = filename+"1"+".csv";
        try {
            File file = new File(SD_CARD_PATH, fname);
            File file1 = new File(SD_CARD_PATH, fname2);
            FileInputStream fIn = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fIn));
//        FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(new FileWriter(file1));
            String csvLine;

            while ((csvLine = reader.readLine()) != null) {

                updateTotalCount();
                totalCallCount++;
                ids = csvLine.split(",");

             //   ids[2]=Status;
                Log.d("info","Write Number:"+Number);
                String CallResponse ="";
                if(ids[1].equalsIgnoreCase(Number))
                {
                    Log.d("info","Write Number Inside:"+Number);
                    String FinalStatus = getStatus(Status);
                   // csvLine=String.join(",",ids);

                    switch(FinalStatus){
                        case "A1":
                            CallResponse ="Active";
                            finalReport[0]++;
                            break;
                        case "A2":
                            CallResponse ="Active";
                            finalReport[1]++;
                            break;
                        case "A3":
                            CallResponse ="Active";
                            finalReport[2]++;
                            break;
                        case "A4":
                            CallResponse ="Active";
                            finalReport[3]++;
                            break;
                        case "B":
                            CallResponse ="InActive";
                            finalReport[4]++;
                            break;
                        case "C":
                            CallResponse="InActive";
                            finalReport[5]++;
                            break;
                        case "D":
                            CallResponse="Drop";
                            finalReport[6]++;
                            break;
                        case "E":
                            CallResponse="InActive";
                            finalReport[7]++;
                            break;
                        case "F":
                            CallResponse="InActive";
                            finalReport[8]++;
                            break;
                        case "G":
                            CallResponse="Drop";
                            finalReport[9]++;
                            break;
                        case "X":
                            CallResponse="Drop";
                            finalReport[9]++;
                            break;
                        case "Y1":
                            CallResponse="Inactive";
                            finalReport[10]++;
                            break;
                        case "Y2":
                            CallResponse="Inactive";
                            finalReport[11]++;
                            break;
                        case "Y3":
                            CallResponse="Inactive";
                            finalReport[12]++;
                            break;


                    }
//                    String TodayDate = getDate();
//                    String currentTime = currentTime();
                    if(ids[8].equals("NA"))
                    {
                        ids[8]=getStatus(Status);
                    }
                    else
                    {
                        ids[7]=ids[8];
                        ids[8]=getStatus(Status);
                    }
                    if(getDate().equals(ids[11]))
                    {
                        int call =  Integer.parseInt(ids[13]);
                        call=call+1;
                        ids[13]=Integer.toString(call);
                    }
                    else
                    {
                        int call = Integer.parseInt(ids[13]);
                        call = 1;
                        ids[13]=Integer.toString(call);
                    }
                    ids[9]= CallResponse;
                    ids[10]=comm;
                    ids[11] = getDate();
                    ids[12] = currentTime();
                    csvLine = ids[0]+","+ids[1]+","+ids[2]+","+ids[3]+","+ids[4]+","+ids[5]+
                            ","+ids[6]+","+ids[7]+","+ids[8]+","+ids[9]+","+ids[10]
                            +","+ids[11]+","+ids[12]+","+ids[13];
                   // csvLine=csvLine+","+Status+","+CallResponse+","+comm+","+TodayDate+","+currentTime;

                    bw.write(csvLine+"\n");

                }
                else{
                    bw.write(csvLine+"\n");
                }


                Log.d("Collumn 1 ", "" + ids[0]+ids[1]);
                //txtEd.setText(message);



            }
            reader.close();
            bw.close();
            file.delete();
            file1.renameTo(new File(SD_CARD_PATH, fname));
//        bw.close();
//        fw.close();
            //  inputStream = getResources().openRawResource(file);
        }catch (Exception e) {
            Toast.makeText(con.getApplicationContext(), e.getMessage(),
                   Toast.LENGTH_SHORT).show();
        }
//    try{
//    File file = new File(SD_CARD_PATH, "/Music/Contacts.csv");
//    FileWriter fw = new FileWriter(file.getAbsoluteFile());
//    BufferedWriter bw = new BufferedWriter(fw);
//    bw.write("ABC");
//    bw.close();
//    }catch (Exception e) {
//        Toast.makeText(getBaseContext(), e.getMessage(),
//                Toast.LENGTH_SHORT).show();
//    }
    }

    public JSONArray getCallDataStatus(String Status,Context Con,String Filename)
    {
        InputStream inputStream;
        String[] ids;
        FileInputStream iStream;
        BufferedReader reader;
        String message;
        String csvLine;
        JSONArray jA = new JSONArray();
        Log.d("info","CurrentStatus"+Status);
        File SD_CARD_PATH = Environment.getExternalStorageDirectory(); //.toString();
        String fname = Filename +".csv";


        try {
            File file = new File(SD_CARD_PATH, fname);
            FileInputStream fIn = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fIn));

            while ((csvLine = reader.readLine()) != null) {
//                Toast.makeText(Con.getApplicationContext(), csvLine,
//                Toast.LENGTH_SHORT).show();

                JSONObject obj = new JSONObject();
                ids = csvLine.split(",");
                Log.d("info","Rowinfo:"+ids[2]);
                Log.d("info","RowName:"+ids[0]);


                if (ids[8].equalsIgnoreCase(Status))
                {
//                    Toast.makeText(Con.getApplicationContext(), csvLine,
//                        Toast.LENGTH_SHORT).show();

                    obj.put("Name", ids[0]);
                    obj.put("Number", ids[1]);
                    jA.put(obj);
                      Log.d("info","FileInfo:"+ids[1]+","+ids[2]);

                }
                else if(Status.equalsIgnoreCase("Fresh Calls"))
                {
                    if(ids[8].equals("NA"))
                    {
                        obj.put("Name", ids[0]);
                        Log.d("info","RowName"+ids[0]);
                        Log.d("info","InfoValues:"+jA);
                        obj.put("Number", ids[1]);
                        jA.put(obj);
                    }
                }
                else if(Status.equals("Confirmation Calls")){
                    if(ids[8].equals("A1"))
                    {
                        obj.put("Name", ids[0]);
                        obj.put("Number", ids[1]);
                        jA.put(obj);
                    }
                }
                else if(Status.equals("Inactive Calls")){
                    if(ids[8].equals("B")||ids[8].equals("C")||ids[8].equals("Y2")||ids[8].equals("E")||ids[8].equals("F")||
                            ids[8].equals("Y1"))
                    {
                        obj.put("Name", ids[0]);
                        obj.put("Number", ids[1]);
                        jA.put(obj);
                    }
                }
                else if(Status.equals("Tentative")){
                    if(ids[8].equals("A4") || ids[8].equals("Y1") || ids[8].equals("Y2"))
                    {
                        obj.put("Name", ids[0]);
                        obj.put("Number", ids[1]);
                        jA.put(obj);
                    }
                }
                else if(Status.equals("Recall Inactive Numbers")){
                  if((ids[8].equals("B")||ids[8].equals("C")||ids[8].equals("Y2")||ids[8].equals("E")||ids[8].equals("F")||
                    ids[8].equals("Y1")) && ids[11].equals(getDate()))
                    {
                        obj.put("Name", ids[0]);
                        obj.put("Number", ids[1]);
                        jA.put(obj);
                    }
                }


            }
            Log.d("info","Downloaded Info Data:"+jA);
        }
        catch (Exception e) {
            Toast.makeText(Con.getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        return jA;
    }
    public String getDate()
    {
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println(cal.getTime());
// Output "Wed Sep 26 14:23:28 EST 2012"

        String todayDate = format1.format(cal.getTime());
        Log.d("info","TodayDate:"+todayDate);
        return todayDate;
    }
    public String currentTime(){


            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String currentTime = sdf.format(cal.getTime());
            Log.d("info","TodayTime:"+currentTime);
            return currentTime;


    }

    public String getStatus(String Status)
    {
        String[] status = Status.split("[(]");
        return status[0];
    }
    public int[] getFinalReport ()
    {
        return finalReport;
    }
    public void updateTotalCount()
    {
        totalCallCount++;
    }
}
