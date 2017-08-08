package com.iskcon.pfh.folkcalling;

import android.Manifest;
import android.os.Environment;
import android.util.Log;

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
    public void getData(String Number)
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
            //    Toast.makeText(getBaseContext(), csvLine,Toast.LENGTH_LONG).show();
                if(ids[1] == Number)
                {
                    csvLine=csvLine+",Not Attending";
                    bw.write(csvLine+"\n");
                }
                else{
                    bw.write(csvLine+"\n");
                }

              //  message= txtEd.getText().toString();
              //  message=message+ids[0]+":"+ids[1]+"/n";
                Log.d("Collumn 1 ", "" + ids[0]+ids[1]);
                //txtEd.setText(message);



            }
            reader.close();
            bw.close();
            file.delete();
            file1.renameTo(new File(SD_CARD_PATH, "/Contacts.csv"));
//        bw.close();
//        fw.close();
            //  inputStream = getResources().openRawResource(file);
        }catch (Exception e) {
//            Toast.makeText(getBaseContext(), e.getMessage(),
//                    Toast.LENGTH_SHORT).show();
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
}
