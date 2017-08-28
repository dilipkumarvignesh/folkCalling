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

/**
 * Created by i308830 on 8/8/17.
 */

public class CallStatusUpdate {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    //  CSVWriter writer;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public void getData(String Number, String Status, Context con)
    {
        InputStream inputStream;
        String[] ids;
        FileInputStream iStream;
        BufferedReader reader;
        String message;

        File SD_CARD_PATH = Environment.getExternalStorageDirectory(); //.toString();
        Log.d("info","SDCARDPATH:"+SD_CARD_PATH);
        //File yourFile = new File(SD_CARD_PATH, "/Music/.csv");
        //new File(SD_CARD_PATH + "/Music/" + "Contacts.csv");
        try {
            File file = new File(SD_CARD_PATH, "Contacts.csv");
            File file1 = new File(SD_CARD_PATH, "Contacts1.csv");
            FileInputStream fIn = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fIn));
//        FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(new FileWriter(file1));
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {


                ids = csvLine.split(",");

                if(ids[1].equalsIgnoreCase(Number))
                {

                    csvLine=csvLine+","+Status;
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
            file1.renameTo(new File(SD_CARD_PATH, "Contacts.csv"));
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

        File SD_CARD_PATH = Environment.getExternalStorageDirectory(); //.toString();
        String fname = Filename +".csv";

        try {
            File file = new File(SD_CARD_PATH, fname);
            FileInputStream fIn = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fIn));
            while ((csvLine = reader.readLine()) != null) {
//                Toast.makeText(Con.getApplicationContext(), csvLine,
//                Toast.LENGTH_SHORT).show();
                ids = csvLine.split(",");
                Log.d("info","Rowinfo:"+ids[2]);
                if (ids[2].equalsIgnoreCase(Status))
                {
//                    Toast.makeText(Con.getApplicationContext(), csvLine,
//                        Toast.LENGTH_SHORT).show();
                    JSONObject obj = new JSONObject();
                    obj.put("Name", ids[0]);
                    obj.put("Number", ids[1]);
                    jA.put(obj);
                      Log.d("info","FileInfo:"+ids[1]+","+ids[2]);
                }
            }
        }
        catch (Exception e) {
            Toast.makeText(Con.getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        return jA;
    }
}
