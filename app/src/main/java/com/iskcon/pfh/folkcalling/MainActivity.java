package com.iskcon.pfh.folkcalling;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import net.gotev.speech.Speech;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import static android.R.attr.path;
import static android.R.attr.permission;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL = 0;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int FILE_SELECT_CODE = 0;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CALL_LOG

    };
    ArrayList<String> selectedPrograms = new ArrayList<>();
    EditText txtGoogleId;
    TextView txtStatus,lFileInput;
    String jName;
    Integer Callenabled;
    Button btnDownload;
    ImageView SearchFile;

    JSONArray jA = new JSONArray();
    View vi;
    String GoogleId;
    TextToSpeech t1;

    Button chooseProgram;
    int i=0;
    int contact_count=0;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(
                Context.TELEPHONY_SERVICE);
        vi = this.findViewById(android.R.id.content);
        Speech.init(this,getPackageName());
        Callenabled = 1;
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE


            );

        }

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        ToggleButton A3toggle = (ToggleButton) findViewById(R.id.A3toggleButton);
        ToggleButton Inactivetoggle = (ToggleButton) findViewById(R.id.toggleButton);
        chooseProgram = (Button)findViewById(R.id.ChooseProgram);
        chooseProgram.setOnClickListener(this);

        PhoneStateListener callStateListener = new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber){
                if(state==TelephonyManager.CALL_STATE_RINGING){
                  //  tts.speak(incomingNumber+" calling", TextToSpeech.QUEUE_FLUSH, null);
//                    Toast.makeText(getApplicationContext(),"Phone is Ringing : "+incomingNumber,
//                            Toast.LENGTH_LONG).show();
                }
                if(state==TelephonyManager.CALL_STATE_OFFHOOK){
//                    Toast.makeText(getApplicationContext(),"Phone in a call or call picked",
//                            Toast.LENGTH_LONG).show();
                }
                if(state==TelephonyManager.CALL_STATE_IDLE){
                    //phone is neither ringing nor in a call
//                    Toast.makeText(getApplicationContext(),"Phone Idle",
//                            Toast.LENGTH_LONG).show();

//                    Intent showDialogIntent = new Intent(MainActivity.this, DisplayMessageActivity.class);
//                        showDialogIntent.putExtra("Name", jName);
//                        startActivityForResult(showDialogIntent, 2);
//                    if (i!=0) {
//                        FragmentManager fm = getFragmentManager();
//                        CallStatus cs = new CallStatus();
//                        cs.show(fm, "DialogFragment");
//                        Runnable showDialogRun = new Runnable() {
//                            public void run() {
//                                repeatCall();
//                            }
//                        };
//                        Handler h = new Handler();
//                        h.postDelayed(showDialogRun, 3000);
//                    }
                }
            }
        };

        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
        txtGoogleId = (EditText) findViewById(R.id.txtGoogleId);
        txtStatus = (TextView) findViewById(R.id.Status);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                download_excel();
            }
        });

        lFileInput = (TextView)findViewById(R.id.LFileInput);
        SearchFile = (ImageView)findViewById(R.id.GET_FILE);
        SearchFile.setOnClickListener(this);





        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });

        Speech.init(this,getPackageName());

    }


    @Override
    public void onClick(View v) {
        Log.d("info","Button clicked:"+v.getId());
        switch(v.getId()) {

            case R.id.CALLSTOP:
                 Callenabled = 0;
                Log.d("info","CallEnabled:"+"FALSE");
                break;
            case R.id.UpdateCallStatus:
                try {
                    JSONObject objects = jA.getJSONObject(i-1);

                    String jNumber = objects.get("Number").toString();
                    String Name = objects.get("Name").toString();
                    Spinner sta = (Spinner) findViewById(R.id.updateSpinner);
                    EditText comments = (EditText)findViewById(R.id.CallComment);
                    String StatusValue = sta.getSelectedItem().toString();
                    String comm = comments.getText().toString();
                    Log.d("info","StatusValue:"+StatusValue);
                  //  updateStatus(Name,jNumber,StatusValue,comm);
                    repeatCall();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                break;
            case R.id.REPORT:
                CallStatusUpdate updateCall = new CallStatusUpdate();

                Intent k = new Intent(getApplicationContext(),StatusActivity.class);


                startActivity(k);


            case R.id.GET_FILE:
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(i, 15);
                break;
            case R.id.ChooseProgram:
                selectPrograms();
        }

    }
    private void processJson(JSONObject object) {

        i=0;
        jA= new JSONArray(new ArrayList<String>());
        try {
            JSONArray rows = object.getJSONArray("rows");

            Spinner sta = (Spinner)findViewById(R.id.spinner);
            String SpinnerValue = sta.getSelectedItem().toString();
           //int cou = contact_count - i;


            Log.d("info", "values_of_rows=" + rows);
            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                JSONObject obj = new JSONObject();
                Log.d("info", "Download row=" + row);
                Log.d("info", "Download column=" + columns);
//                int position = columns.getJSONObject(0).getInt("v");
                String name = columns.getJSONObject(0).getString("v");
                String number = columns.getJSONObject(1).getString("f");
                String StatusValue = columns.getJSONObject(2).getString("v");
                Log.d("info", "Name=" + name);
                Log.d("info", "Number=" + number);
                obj.put("Name", name);
                obj.put("Number", number);
                obj.put("Status",StatusValue);
                if (StatusValue.equals(SpinnerValue)) {
                    jA.put(obj);
                }
            }
            Toast.makeText(getApplicationContext(),
                    jA.length()+" Contacts Downloaded", Toast.LENGTH_LONG).show();
            contact_count = jA.length();
            String Status =  "0 Contacts Called " + contact_count+" Contacts Remaining";
            txtStatus.setText(Status);
            Log.d("info", "values=" + jA);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String getGoogleId(String goog)
    {
       // goog = "https://docs.google.com/spreadsheets/d/1xk8AY8MOWiqwC3qvFEyOVN-wBdMtDW8QtirmcUkocrU/edit#gid=0";
        String[] words=goog.split("/");
        return words[5];
    }
    private void download_excel() {
        //  DownloadWebpageTask myTask = new DownloadWebpageTask();
        i=0;
        GoogleId = txtGoogleId.getText().toString();
        EditText cFilename = (EditText) findViewById(R.id.LFileInput);
        String csvFilename = cFilename.getText().toString();
        if (!csvFilename.equals(""))
        {
            Spinner sta = (Spinner)findViewById(R.id.spinner);
            String StatusValue = sta.getSelectedItem().toString();
            Spinner TC = (Spinner)findViewById(R.id.updateTC);
            String TeleCaller = TC.getSelectedItem().toString();
            Spinner Day = (Spinner)findViewById(R.id.updateDay);
            String DayValue = Day.getSelectedItem().toString();
//            Spinner PR = (Spinner)findViewById(R.id.updateProgram);
//            String PrValue = PR.getSelectedItem().toString();

            ToggleButton A1toggle = (ToggleButton) findViewById(R.id.toggleButton);
            Boolean A1SmsStatus = A1toggle.isChecked();
            Log.d("info","A1SmsStatus:"+A1SmsStatus);

            ToggleButton A3toggle = (ToggleButton) findViewById(R.id.A3toggleButton);
            Boolean A3SmsStatus = A3toggle.isChecked();
            Log.d("info","A3SmsStatus:"+A3SmsStatus);


            ToggleButton Inactivetoggle = (ToggleButton) findViewById(R.id.InactivetoggleButton);
            Boolean InactiveSmsStatus = Inactivetoggle.isChecked();
            Log.d("info","InactiveSmsStatus:"+InactiveSmsStatus);

            EditText PrefixMessage = (EditText) findViewById(R.id.SmsPrefix);
            String Pretxt = PrefixMessage.getText().toString();
            EditText A1txtMessage = (EditText) findViewById(R.id.A1txtMessage);
            String A1txt = A1txtMessage.getText().toString();
            EditText A3txtMessage =   (EditText) findViewById(R.id.A3txtMessage);
            String A3txt = A3txtMessage.getText().toString();
            EditText InactivetxtMessage = (EditText) findViewById(R.id.InactivetxtMessage);
            String Inactivetxt = InactivetxtMessage.getText().toString();

            Log.d("info","SpinnerText:"+StatusValue);
            Log.d("info","TCValue:"+TeleCaller);
            Log.d("info","DayValue"+DayValue);

            Intent k = new Intent(getApplicationContext(),Operations.class);
            k.putExtra("LocalFile",true);
            k.putExtra("Filename",csvFilename);
            k.putExtra("StatusValue",StatusValue);
            k.putExtra("TeleCaller",TeleCaller);
            k.putExtra("DayValue",DayValue);
//            k.putExtra("PrValue",PrValue);
            if(selectedPrograms.isEmpty())
            {
                selectedPrograms.add("ALL");
            }
            k.putStringArrayListExtra("PrValues",selectedPrograms);
            k.putExtra("A1SmsStatus",A1SmsStatus);
            k.putExtra("A3SmsStatus",A3SmsStatus);
            k.putExtra("InactiveSmsStatus",InactiveSmsStatus);
            k.putExtra("smsPrefix",Pretxt);
            k.putExtra("A1txt",A1txt);
            k.putExtra("A3txt",A3txt);
            k.putExtra("Inactivetxt",Inactivetxt);

            startActivity(k);


        }
        else if(!GoogleId.equals(""))
        {
            String final_google_id = getGoogleId(GoogleId);

            Toast.makeText(getApplicationContext(),
                    "Downloading Excel. Please wait ...", Toast.LENGTH_LONG).show();
            DownloadWebpageTask dow = new DownloadWebpageTask(new AsyncResult() {
                @Override
                public void onResult(JSONObject object) {
                    //    ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.CALL_PHONE},
                    processJson(object);

                }
            });
           // String final_google_id = "1C7XyjLQj0t6waLGlDWsvVdGtM0JWh24RFi0ZiR5L6w0";
            dow.execute("https://spreadsheets.google.com/tq?key="+final_google_id);
        }



//         Toast.makeText(getApplicationContext(),
//                 jA.length()+" Contacts Downloaded", Toast.LENGTH_LONG).show();


       // String final_google_id  = getGoogleId(GoogleId);


        }



public void callNow()
        {           Log.d("info","inside Call now");
//
//                MY_PERMISSIONS_REQUEST_CALL);
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Info", "Call now");
 
                }
                }
        }
    }

    public void repeatCall()
    {
//        Intent showDialogIntent = new Intent(this, DisplayMessageActivity.class);
//        startActivity(showDialogIntent);

        Log.d("info","Connecting Call");
        Intent intent = new Intent(Intent.ACTION_CALL);
        try {
            if(Callenabled == 1 && i < contact_count) {

                JSONObject objects = jA.getJSONObject(i);
                jName = objects.get("Name").toString();
                String jNumber = objects.get("Number").toString();
                String fNumber = jNumber;
                intent.setData(Uri.parse("tel:" + fNumber));
                // String na = name[i];
                t1.speak("Calling " + jName, TextToSpeech.QUEUE_FLUSH, null);
                intent.setData(Uri.parse("tel:" + jNumber));
                startActivity(intent);

                //startActivityForResult(intent, REQUEST_CODE);

                Toast.makeText(getApplicationContext(),
                        "Calling " + jName, Toast.LENGTH_LONG).show();
                i++;
                int cou = contact_count - i;
                String Status = i + " Contacts Called " + cou + " Contacts Remaining";
                txtStatus.setText(Status);
                TextView nameStatus = (TextView)findViewById(R.id.txtStatus);
                nameStatus.setText("Update Call Status for "+jName);

            }

            //vi.addView(ly1, params1);



        }
        catch (JSONException e){
                            e.printStackTrace();
                        }



    }

//    public void updateStatus(String Name,String number, String Status,String comm)
//    {
//        EditText cFilename = (EditText) findViewById(R.id.LFileInput);
//        String csvFilename = cFilename.getText().toString();
//        CallStatusUpdate updateCall = new CallStatusUpdate();
//        updateCall.writeStatus(Name,number,Status,comm,this,csvFilename);
//        Toast.makeText(getApplicationContext(),"Status Updated",
//                            Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("info","Inside OnActivityResult");
        //Log.d("info","I value="+i);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

           // repeatCall();
            Log.d("info","Inside requestCode");

            }
            else if  (requestCode == 2){
            if (resultCode == Activity.RESULT_OK) {
                // TODO Extract the data returned from the child Activity.
                String returnValue = data.getStringExtra("Status");
                Log.d("info","UpdatedStatus="+returnValue);
                TextView updText = (TextView)findViewById(R.id.UpdatedStatus1);
                String StatusText = updText.getText().toString();
                StatusText = StatusText + "\n"+ returnValue;
                updText.setText(StatusText);


            }

        }
        if(requestCode == 15) {
            if (resultCode == RESULT_OK) {
                Log.d("info", "file:" + data.getData());
                Uri uri = data.getData();
                String uriString = uri.toString();

                File file = new File(uriString);
                Log.d("info", "Filepath:" + file.getAbsolutePath());
                String path = file.getAbsolutePath();
                String displayName = null;
                if (uriString.startsWith("content")) {
               //     Log.d("info","Inside Content"+getFilePath(file.getAbsolutePath().toString()));


                    lFileInput.setText(getFilePath(uri.getPath(),false));
                    Toast.makeText(this,uri.getPath().toString(), Toast.LENGTH_LONG);
//                    Cursor cursor = null;
//                    try {
//                        final String column = "_data";
//                        final String[] projection = {
//                                column
//                        };
//                        cursor = this.getContentResolver().query(uri,projection, null, null, null);
//
//                        final int index = cursor.getColumnIndexOrThrow(column);
//                        if (cursor != null && cursor.moveToFirst()) {
//                            displayName = cursor.getString(index);
//                            Log.d("info","CursorValue:"+cursor.moveToFirst());
//                            Log.d("info","CursorValue1"+cursor.getString(0));
//                            Log.d("info","CursorValue2"+cursor.getString(1));
//                            Log.d("info","CursorValue3"+cursor.getString(2));
//
//                        }
//
//                    } finally {
//                        cursor.close();
//                    }

                } else if (uriString.startsWith("file://")) {
                    Toast.makeText(this,uriString.toString(), Toast.LENGTH_LONG);
                   lFileInput.setText(getFilePath(data.getData().toString(),true));
                }

                Log.d("info", "Filepath:" + path);
//                Toast.makeText(getApplicationContext(),
//                        data + "Path of chosen File", Toast.LENGTH_LONG).show();


            }
        }

        if (requestCode == FILE_SELECT_CODE){
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("info", "File Uri: " + uri.toString());
                    // Get the path
                    //String path = FileUtils.getPath(this, uri);
                    Log.d("info", "File Path: " + path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }

        }
        super.onActivityResult(requestCode, resultCode, data);

        }



        public String getFilePath(String path,Boolean type)
        {
            if(type == true)
            {
                String[] words = path.split("[0]");
                Toast.makeText(getApplicationContext(),
                        words[1]+ "Path of chosen File", Toast.LENGTH_LONG).show();
                Log.d("info","words:"+words[0]+":::"+words[1]);
                return words[1];
            }
            else {
                Log.d("info", "FIleparts" + path);
                String[] words = path.split(":");
                return words[1];
            }
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

