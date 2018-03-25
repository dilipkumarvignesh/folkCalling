package com.iskcon.pfh.folkcalling;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import java.util.ArrayList;
import java.util.HashMap;

public class StatusActivity extends AppCompatActivity {
    private ShareActionProvider mShareActionProvider;
    Button Report,shrReport,shrFile,ChooseProgram,sendWhatsapp;
    private String csvFilename;
    String TeleCaller;
    CheckBox date;
    TextView rep;
    ArrayList<String> selectedPrograms = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Bundle extras = getIntent().getExtras();
        csvFilename=extras.getString("filename");
        Log.d("info","Csvfilename:"+csvFilename);


        sendWhatsapp = (Button)findViewById(R.id.btnWhatsapp);

        sendWhatsapp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                sendWhatsappMessages();
            }
        });
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
    public void sendWhatsappMessages()
    {
        Intent k = new Intent(getApplicationContext(), Whatsapp.class);

        k.putExtra("filename", csvFilename);

        startActivity(k);
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
        TeleCaller = TC.getSelectedItem().toString();
        date = (CheckBox)findViewById(R.id.checkBox);


       // ExcelAccess EA = new ExcelAccess();



            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
//
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            progress.show();
                        }
                    });
                    CallStatusUpdate callManager = new CallStatusUpdate();
                    final Boolean today = date.isChecked();
                    HashMap fR = callManager.finalReport(csvFilename, TeleCaller, selectedPrograms, today);

                    final Object totalPeople = fR.get("NoOfPeople");
                    Log.d("info", "TotalPeople:" + totalPeople);
                    final Object totalCalls = fR.get("NoOfCalls");
                    final Object A1 = fR.get("A1");
                    final Object A2 = fR.get("A2");
                    final Object A3 = fR.get("A3");
                    final Object A4 = fR.get("A4");
                    final Object B = fR.get("B");
                    final Object C = fR.get("C");
                    final Object D = fR.get("D");
                    final Object E = fR.get("E");
                    final Object F = fR.get("F");
                    final Object G = fR.get("G");
                    final Object X = fR.get("X");
                    final Object Y1 = fR.get("Y1");
                    final Object Y2 = fR.get("Y2");
                    final Object Y3 = fR.get("Y3");
                    final Object Z = fR.get("Z");
                    final Object Inactive = fR.get("Inactive");
                    final Object Drop = fR.get("Drop");
                    final Object Active = fR.get("Active");

                    rep = (TextView) findViewById(R.id.report);


                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            String listString = "";

                            for (String s : selectedPrograms) {
                                listString += s + ",";
                            }
                            String dat = "";
                            if (today == true) {
                                dat = "Today";
                            } else
                                dat = "Overall";
                            rep.setText("Selected Programs: " + listString + "\n Date: " + dat + "\nTeleCaller: " + TeleCaller + "\n\n\n" +
                                    "Total People: " + totalPeople.toString() + "\n" +


                                    "Total Calls: " + totalCalls.toString() + "\n\n" +
                                    "A1(Conformation Calls): " + A1.toString() + "\n" +
                                    "A2(Not Interested): " + A2.toString() + "\n" +
                                    "A3(Interested and Not coming): " + A3.toString() + "\n" +
                                    "A4(Tentative): " + A4.toString() + "\n" +
                                    "B(Ringing but not picking): " + B.toString() + "\n" +
                                    "C(Busy): " + C.toString() + "\n" +
                                    "D(Invalid No/Out of Service): " + D.toString() + "\n" +
                                    "E(Switched Off): " + E.toString() + "\n" +
                                    "F(Not Reachable): " + F.toString() + "\n" +
                                    "G(Relocated to Out of Bangalore): " + G.toString() + "\n" +
                                    "X(Age > 30 or Female): " + X.toString() + "\n" +
                                    "Y1(Call After few minutes): " + Y1.toString() + "\n" +
                                    "Y2(Call Later): " + Y2.toString() + "\n" +
                                    "Y3(Call on a particular Date): " + Y3.toString() + "\n" +
                                    "Z(Already Attended): " + Z.toString() + "\n\n" +
                                    "Inactive: " + Inactive.toString() + "\n" +
                                    "Drop: " + Drop.toString() + "\n" +
                                    "Active: " + Active.toString());
                                    progress.dismiss();

                        }


                    });

                }


        };
        new Thread(runnable).start();
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