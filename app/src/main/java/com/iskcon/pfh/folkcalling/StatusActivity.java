package com.iskcon.pfh.folkcalling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class StatusActivity extends AppCompatActivity {
    private ShareActionProvider mShareActionProvider;
    Button Report,shrReport,shrFile,ChooseProgram;
    private String csvFilename;
    ArrayList<String> selectedPrograms = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Bundle extras = getIntent().getExtras();
        csvFilename=extras.getString("filename");
        Log.d("info","Csvfilename:"+csvFilename);
        Report = (Button)findViewById(R.id.btnStatus);
        Report.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                getReport();
            }
        });
        ChooseProgram = (Button)findViewById(R.id.ChooseProgram);
        ChooseProgram.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                selectPrograms();
            }
        });
        shrReport = (Button)findViewById(R.id.shrReport);
        shrReport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                shareReport();
            }
        });
        shrFile = (Button)findViewById(R.id.shrFile);
        shrFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                shareFile();
            }
        });
    }

    public void shareReport()
    {
        TextView rep = (TextView)findViewById(R.id.report);
        String report = rep.getText().toString();
       // String shareBody = "Here is the share content body";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Call Report");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, report);
        startActivity(Intent.createChooser(sharingIntent, "Share Report"));
    }
    public void shareFile()
    {
//        MenuItem item = menu.findItem(R.id.action_share);
//
//        //Fetch and store ShareActionProvider
//        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareIntent(createShareIntent());
    }
    public void getReport()
    {
       // Spinner program = (Spinner)findViewById(R.id.updateProgram);
       // String ProgramValue = program.getSelectedItem().toString();
        Spinner TC = (Spinner)findViewById(R.id.updateTC);
        String TeleCaller = TC.getSelectedItem().toString();
        CheckBox date = (CheckBox)findViewById(R.id.checkBox);
        Boolean today = date.isChecked();

        ExcelAccess EA = new ExcelAccess();
        try {
            HashMap fR = EA.finalReport(csvFilename,TeleCaller,selectedPrograms,today);

         Log.d("info","TodayDate123:"+today);
//        CallStatusUpdate cs = new CallStatusUpdate();
//        HashMap fR = cs.finalReport(csvFilename,TeleCaller,selectedPrograms,today);


        Object totalPeople = fR.get("NoOfPeople");
        Log.d("info","TotalPeople:"+totalPeople);
        Object totalCalls = fR.get("NoOfCalls");
        Object A1 = fR.get("A1");
        Object A2 = fR.get("A2");
        Object A3 = fR.get("A3");
        Object A4 = fR.get("A4");
        Object B = fR.get("B");
        Object C = fR.get("C");
        Object D = fR.get("D");
        Object E = fR.get("E");
        Object F = fR.get("F");
        Object G = fR.get("G");
        Object X = fR.get("X");
        Object Y1 = fR.get("Y1");
        Object Y2 = fR.get("Y2");
        Object Y3 = fR.get("Y3");
        Object Z = fR.get("Z");
        Object Inactive = fR.get("Inactive");
        Object Drop = fR.get("Drop");
        Object Active = fR.get("Active");

        TextView rep = (TextView)findViewById(R.id.report);
        String dat = "";
        if(today == true)
        {
        dat = "Today";
        }
        else
        dat = "Overall";
        String listString = "";

        for (String s : selectedPrograms)
        {
            listString += s + ",";
        }
        rep.setText("Selected Programs: "+listString+ "\n Date: "+dat+"\nTeleCaller: "+TeleCaller+"\n\n\n"+
                "Total People: "+totalPeople.toString()+"\n"+

                    "Total Calls: "+totalCalls.toString()+"\n"+
                    "A1: "+A1.toString()+" A2: "+A2.toString()+ " A3: "+A3.toString()+"\n"+
                    "A4: "+A4.toString()+" B: "+B.toString()+" C: "+C.toString()+" D: "+D.toString()+"\n"+
                    "E: "+E.toString()+" F: "+F.toString()+ " G: "+G.toString()+ " X: "+X.toString()+"\n"+
                    "Y1: "+Y1.toString()+" Y2: "+Y2.toString()+" Y3: "+Y3.toString()+" Z: "+Z.toString()+"\n"+
                    "Inactive: "+Inactive.toString()+" Drop: "+Drop.toString()+"\n"+
                    "Active: "+Active.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
//        mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);
//        setShareIntent(createShareIntent());
        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
    private Intent createShareIntent() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
       // shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_TEXT,
//                "http://stackandroid.com");
//
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        File SD_CARD_PATH = Environment.getExternalStorageDirectory();
        File filename = new File(SD_CARD_PATH,csvFilename);
        Log.d("info","filename:"+filename);
        Uri screenshotUri = Uri.fromFile(filename);
        Log.d("info","fileUri:"+screenshotUri);
        sharingIntent.setType("*/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "Share File"));
        return sharingIntent;
    }


    public void selectPrograms()
    {
        final String[] items = {"PR1","PR2","PR3","PR4","PR5","PR6","PR7","PR8","PR9","PR10","PR11","PR12","PR13","PR14","PR15"};
        selectedPrograms.clear();
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Select Programs")
                .setMultiChoiceItems(items, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {

                                selectedPrograms.add(items[item]);
                            }
                        });

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ListView list = ((android.app.AlertDialog) dialog).getListView();
                        //ListView has boolean array like {1=true, 3=true}, that shows checked items

                        //
                    }
                });
        builder.show();
    }
}