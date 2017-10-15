package com.iskcon.pfh.folkcalling;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.telephony.SmsManager;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by i308830 on 8/8/17.
 */


public class CallStatusUpdate {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;


    private Context Con;
    //  CSVWriter writer;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void writeStatus(String Name, String Number, String Status, String comm, Context con, String filename, String SmsPrefix, String A1txt, Boolean A1Status, String A3txt, Boolean A3Status, String Inactivetxt, Boolean InactiveStatus) {
        InputStream inputStream;
        String[] ids;
        FileInputStream iStream;
        BufferedReader reader;
        String message;

        File SD_CARD_PATH = Environment.getExternalStorageDirectory();


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
                Log.d("info", "Write Number:" + Number);
                String CallResponse = "";
                if (ids[0].equalsIgnoreCase(Name) && ids[1].equalsIgnoreCase(Number)) {
                    Log.d("info", "Write Number Inside:" + Number);
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
//                    String TodayDate = getDate();
//                    String currentTime = currentTime();
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
                    csvLine = ids[0] + "," + ids[1] + "," + ids[2] + "," + ids[3] + "," + ids[4] + "," + ids[5] +
                            "," + ids[6] + "," + ids[7] + "," + ids[8] + "," + ids[9] + "," + ids[10]
                            + "," + ids[11] + "," + ids[12] + "," + ids[13] + "," + ids[14] + "," + ids[15] + "," + ids[16] + "," + ids[17] + "," + ids[18] + "," + ids[19];
                    // csvLine=csvLine+","+Status+","+CallResponse+","+comm+","+TodayDate+","+currentTime;

                    bw.write(csvLine + "\n");
                    Log.d("info", "After write");
                } else {
                    bw.write(csvLine + "\n");
                }


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

            Log.d("info", "After Writing 1");
            reader.close();
            bw.close();
            file.delete();
            file1.renameTo(new File(SD_CARD_PATH, filename));

        } catch (Exception e) {
            Toast.makeText(con.getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

    }

    public String constructMessage(String Prefix, String name, String Message) {
        String finalMessage = Prefix + " " + name + "," + Message;
        return finalMessage;
    }

    public JSONArray getCallDataStatus(String Status, Context Con, String Filename, String TeleCaller, String Day, ArrayList<String> selectedPrograms) {
        this.Con = Con;
        InputStream inputStream;
        String[] ids;
        FileInputStream iStream;
        BufferedReader reader;
        String message;
        String csvLine;
        JSONArray jA = new JSONArray();
        Log.d("info", "CurrentStatus" + Status);
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
                Log.d("info", "Rowinfo:" + ids[2]);
                Log.d("info", "RowName:" + ids[0]);
                Log.d("info", "StatusValue:" + Status);

                if (selectedPrograms.contains(ids[2]))
                {
                    if (Status.equalsIgnoreCase("Fresh Calls")) {
                    if (ids[12].equals("NA") && (ids[10].equals(TeleCaller) || TeleCaller.equals("ALL")) && (ids[8].equals(Day) || Day.equals("ALL"))) {
                        obj.put("Name", ids[0]);

                        Log.d("info", "RowName" + ids[0]);
                        Log.d("info", "InfoValues:" + jA);
                        obj.put("Number", ids[1]);
                        obj.put("Program", ids[2]);
                        obj.put("Program", ids[2]);
                        obj.put("Source", ids[3]);
                        obj.put("College", ids[4]);
                        obj.put("Campaigned", ids[5]);
                        obj.put("DOR", ids[6]);
                        obj.put("DOP", ids[7]);

                        jA.put(obj);
                        // sendSms("Hare Krishna <name>. Thank you for your confirmation for attending YFH",ids[0],ids[1]);
                    }
                } else if (Status.equals("Confirmation Calls") && (ids[10].equals(TeleCaller) || TeleCaller.equals("ALL")) && (ids[8].equals(Day) || Day.equals("ALL"))) {
                    if (ids[12].equals("A1")) {
                        obj.put("Name", ids[0]);
                        obj.put("Number", ids[1]);
                        obj.put("Program", ids[2]);
                        obj.put("Source", ids[3]);
                        obj.put("College", ids[4]);
                        obj.put("Campaigned", ids[5]);
                        obj.put("DOR", ids[6]);
                        obj.put("DOP", ids[7]);
                        jA.put(obj);
                    }
                } else if (Status.equals("Inactive Calls") && (ids[10].equals(TeleCaller) || TeleCaller.equals("ALL")) && (ids[8].equals(Day) || Day.equals("ALL"))) {
                    if (ids[12].equals("B") || ids[12].equals("C") || ids[12].equals("Y2") || ids[12].equals("E") || ids[12].equals("F") ||
                            ids[12].equals("Y1")) {
                        obj.put("Name", ids[0]);
                        obj.put("Number", ids[1]);
                        obj.put("Program", ids[2]);
                        obj.put("Source", ids[3]);
                        obj.put("College", ids[4]);
                        obj.put("Campaigned", ids[5]);
                        obj.put("DOR", ids[6]);
                        obj.put("DOP", ids[7]);
                        jA.put(obj);
                    }
                } else if (Status.equals("Tentative") && (ids[10].equals(TeleCaller) || TeleCaller.equals("ALL")) && (ids[8].equals(Day) || Day.equals("ALL"))) {
                    if (ids[12].equals("A4") || ids[12].equals("Y1") || ids[12].equals("Y2")) {
                        obj.put("Name", ids[0]);
                        obj.put("Number", ids[1]);
                        obj.put("Program", ids[2]);
                        obj.put("Source", ids[3]);
                        obj.put("College", ids[4]);
                        obj.put("Campaigned", ids[5]);
                        obj.put("DOR", ids[6]);
                        obj.put("DOP", ids[7]);
                        jA.put(obj);
                    }
                } else if (Status.equals("Recall Inactive Numbers") && (ids[10].equals(TeleCaller) || TeleCaller.equals("ALL")) && (ids[8].equals(Day) || Day.equals("ALL"))) {
                    Log.d("info", "Inside RecallInactive");
                    Log.d("info", "DateValue" + ids[15]);

                    Log.d("info", "TodayDate" + getDate());
                    if ((ids[12].equals("B") || ids[12].equals("C") || ids[12].equals("Y2") || ids[12].equals("E") || ids[12].equals("F") ||
                            ids[12].equals("Y1")) && ids[15].equals(getDate())) {
                        obj.put("Name", ids[0]);
                        obj.put("Number", ids[1]);
                        obj.put("Program", ids[2]);
                        obj.put("Source", ids[3]);
                        obj.put("College", ids[4]);
                        obj.put("Campaigned", ids[5]);
                        obj.put("DOR", ids[6]);
                        obj.put("DOP", ids[7]);
                        jA.put(obj);
                    }
                }


            }}
            Log.d("info", "Downloaded Info Data:" + jA);
        } catch (Exception e) {
            Toast.makeText(Con.getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        return jA;
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

    public HashMap finalReport(String filename, String TeleCaller, ArrayList<String> Program, Boolean Date) {
        File SD_CARD_PATH = Environment.getExternalStorageDirectory();
        String[] ids;
        FileInputStream iStream;
        BufferedReader reader;
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        int totalNoOfPeople=0,totalNoOfCalls=0,A1=0,Z=0,active = 0,inActive = 0,drop = 0,
                A2=0,A3=0,A4=0,B=0,C=0,D=0,E=0,F=0,G=0,X=0,Y1=0,Y2=0,Y3=0;
        String csvLine;
        try {
            File file = new File(SD_CARD_PATH, filename);
            FileInputStream fIn = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fIn));

            while ((csvLine = reader.readLine()) != null) {

                ids = csvLine.split(",");
                Log.d("info","People:"+ids[0]);
                Log.d("info","SelectedPrograms:"+Program);
                if ((TeleCaller.equals(ids[10])||(TeleCaller.equals("ALL")))&&(Program.contains(ids[2])))
                {
                    if( ((Date==true) && ids[15].equals(getDate())) || (Date==false) )
                    {
                    Log.d("info","SelectedPeople:"+ids[0]);
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

                    if(ids[13].equals("Active"))
                    {
                        active++;
                    }
                    if(ids[13].equals("Inactive"))
                    {
                        inActive++;
                    }
                    if(ids[13].equals("Drop"))
                    {
                        drop++;
                    }
                }}
            }

        } catch (Exception e) {
//            Toast.makeText(Con.getApplicationContext(), e.getMessage(),
//                    Toast.LENGTH_SHORT).show();
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
}

