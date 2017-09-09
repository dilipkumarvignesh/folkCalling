package com.iskcon.pfh.folkcalling;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.widget.Toast;
import android.telephony.SmsManager;
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
    private Context Con;
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

        File SD_CARD_PATH = Environment.getExternalStorageDirectory();

        String fname = filename;
        String fname2 = "1"+".csv";
        try {
            File file = new File(SD_CARD_PATH, fname);
            File file1 = new File(SD_CARD_PATH, fname2);
            FileInputStream fIn = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fIn));

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
                            CallResponse ="Inactive";
                            finalReport[4]++;
                            break;
                        case "C":
                            CallResponse="Inactive";
                            finalReport[5]++;
                            break;
                        case "D":
                            CallResponse="Drop";
                            finalReport[6]++;
                            break;
                        case "E":
                            CallResponse="Inactive";
                            finalReport[7]++;
                            break;
                        case "F":
                            CallResponse="Inactive";
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
                         //   addRemainder(ids[0],ids[1],comm);
                            Log.d("info","Y3 Response Selected");
                            break;
                        case "Z":
                            CallResponse="Drop";
                            finalReport[12]++;
                            break;


                    }
//                    String TodayDate = getDate();
//                    String currentTime = currentTime();
                    Log.d("info","Outside switch");
                    if(ids[12].equals("NA"))   //Check if current call response is there
                    {
                        Log.d("info","100");
                        ids[12]=getStatus(Status);

                    }
                    else
                    {    Log.d("info","101");
                        ids[11]=ids[12];
                        ids[12]=getStatus(Status);

                    }
                    if(getDate().equals(ids[15]))
                    {  Log.d("info","102");
                        int call =  Integer.parseInt(ids[17]);
                        call=call+1;
                        ids[17]=Integer.toString(call);

                    }
                    else
                    {
                        Log.d("info","104");
                        int call = 1;
                        ids[17]=Integer.toString(call);
                    }
                    Log.d("info","105");
                    ids[13]= CallResponse;
                    ids[14]=comm;
                    ids[15] = getDate();
                    ids[16] = currentTime();
                    csvLine = ids[0]+","+ids[1]+","+ids[2]+","+ids[3]+","+ids[4]+","+ids[5]+
                            ","+ids[6]+","+ids[7]+","+ids[8]+","+ids[9]+","+ids[10]
                            +","+ids[11]+","+ids[12]+","+ids[13]+","+ids[14]+","+ids[15]+","+ids[16]+","+ids[17]+","+ids[18]+","+ids[19];
                   // csvLine=csvLine+","+Status+","+CallResponse+","+comm+","+TodayDate+","+currentTime;

                    bw.write(csvLine+"\n");
                    Log.d("info","After write");
                }
                else{
                    bw.write(csvLine+"\n");
                }


                Log.d("Collumn 1 ", "" + ids[0]+ids[1]);
                //txtEd.setText(message);



            }
            Log.d("info","After Writing 1");
            reader.close();
            bw.close();
            file.delete();
            file1.renameTo(new File(SD_CARD_PATH, fname));

        }catch (Exception e) {
            Toast.makeText(con.getApplicationContext(), e.getMessage(),
                   Toast.LENGTH_SHORT).show();
        }

    }

    public JSONArray getCallDataStatus(String Status,Context Con,String Filename,String TeleCaller, String Day,String Program)
    {
        this.Con = Con;
        InputStream inputStream;
        String[] ids;
        FileInputStream iStream;
        BufferedReader reader;
        String message;
        String csvLine;
        JSONArray jA = new JSONArray();
        Log.d("info","CurrentStatus"+Status);
        File SD_CARD_PATH = Environment.getExternalStorageDirectory(); //.toString();
        String fname = Filename;


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
                Log.d("info","StatusValue:"+Status);
                Log.d("info","ProgramDate:"+Program);

                if(Status.equalsIgnoreCase("Fresh Calls"))
                {
                    if(ids[12].equals("NA")&&(ids[10].equals(TeleCaller)||TeleCaller.equals("ALL"))&&(ids[8].equals(Day)||Day.equals("ALL")))
                    {
                        obj.put("Name", ids[0]);

                        Log.d("info","RowName"+ids[0]);
                        Log.d("info","InfoValues:"+jA);
                        obj.put("Number", ids[1]);
                        obj.put("Program",ids[2]);
                        obj.put("Program",ids[2]);
                        obj.put("Source",ids[3]);
                        obj.put("College",ids[4]);
                        obj.put("Campaigned",ids[5]);
                        obj.put("DOR",ids[6]);
                        obj.put("DOP",ids[7]);

                        jA.put(obj);
                       // sendSms("Hare Krishna <name>. Thank you for your confirmation for attending YFH",ids[0],ids[1]);
                    }
                }
                else if(Status.equals("Confirmation Calls")&&(ids[10].equals(TeleCaller)||TeleCaller.equals("ALL"))&&(ids[8].equals(Day)||Day.equals("ALL"))){
                    if(ids[12].equals("A1"))
                    {
                        obj.put("Name", ids[0]);
                        obj.put("Number", ids[1]);
                        obj.put("Program",ids[2]);
                        obj.put("Source",ids[3]);
                        obj.put("College",ids[4]);
                        obj.put("Campaigned",ids[5]);
                        obj.put("DOR",ids[6]);
                        obj.put("DOP",ids[7]);
                        jA.put(obj);
                    }
                }
                else if(Status.equals("Inactive Calls")&&(ids[10].equals(TeleCaller)||TeleCaller.equals("ALL"))&&(ids[8].equals(Day)||Day.equals("ALL"))){
                    if(ids[12].equals("B")||ids[12].equals("C")||ids[12].equals("Y2")||ids[12].equals("E")||ids[12].equals("F")||
                            ids[12].equals("Y1"))
                    {
                        obj.put("Name", ids[0]);
                        obj.put("Number", ids[1]);
                        obj.put("Program",ids[2]);
                        obj.put("Source",ids[3]);
                        obj.put("College",ids[4]);
                        obj.put("Campaigned",ids[5]);
                        obj.put("DOR",ids[6]);
                        obj.put("DOP",ids[7]);
                        jA.put(obj);
                    }
                }
                else if(Status.equals("Tentative")&&(ids[10].equals(TeleCaller)||TeleCaller.equals("ALL"))&&(ids[8].equals(Day)||Day.equals("ALL"))){
                    if(ids[12].equals("A4") || ids[12].equals("Y1") || ids[12].equals("Y2"))
                    {
                        obj.put("Name", ids[0]);
                        obj.put("Number", ids[1]);
                        obj.put("Program",ids[2]);
                        obj.put("Source",ids[3]);
                        obj.put("College",ids[4]);
                        obj.put("Campaigned",ids[5]);
                        obj.put("DOR",ids[6]);
                        obj.put("DOP",ids[7]);
                        jA.put(obj);
                    }
                }
                else if(Status.equals("Recall Inactive Numbers")&&(ids[10].equals(TeleCaller)||TeleCaller.equals("ALL"))&&(ids[8].equals(Day)||Day.equals("ALL"))){
                  Log.d("info","Inside RecallInactive");
                    Log.d("info","DateValue"+ids[11]);
                    Log.d("info","TodayDate"+getDate());
                    if((ids[12].equals("B")||ids[12].equals("C")||ids[12].equals("Y2")||ids[12].equals("E")||ids[12].equals("F")||
                    ids[12].equals("Y1")) && ids[11].equals(getDate()))
                    {
                        obj.put("Name", ids[0]);
                        obj.put("Number", ids[1]);
                        obj.put("Program",ids[2]);
                        obj.put("Source",ids[3]);
                        obj.put("College",ids[4]);
                        obj.put("Campaigned",ids[5]);
                        obj.put("DOR",ids[6]);
                        obj.put("DOP",ids[7]);
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

        SimpleDateFormat format1 = new SimpleDateFormat("d/M/yy");
        System.out.println(cal.getTime());
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

    public void addRemainder(String name,String number,String day)
    {
        Calendar beginTime = Calendar.getInstance();
        String [] time = day.split(" ");
        int year = Integer.parseInt(time[2]) ;
        int month = Integer.parseInt(time[1])-1;
        int Cday = Integer.parseInt(time[0]);
        int hour = Integer.parseInt(time[3]);

        Log.d("info","Inside Remainder");
        beginTime.set(year,month,Cday,hour,0);

        Calendar endTime = Calendar.getInstance();
        endTime.set(year,month,Cday,hour,5);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(Events.TITLE, "Calling Remainder")
                .putExtra(Events.DESCRIPTION, "Call "+name+" Number:"+number)
                .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
               // .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        this.Con.startActivity(intent);

     }

     public void sendSms(String Message,String name,String number)
     {
         SmsManager smsManager = SmsManager.getDefault();
         String message = Message;
         message = message.replace("<name>", name);
         smsManager.sendTextMessage(number, null, message, null, null);

     }

}
