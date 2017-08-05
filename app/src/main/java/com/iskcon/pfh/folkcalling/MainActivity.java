package com.iskcon.pfh.folkcalling;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL = 0;

    EditText txtGoogleId;
    TextView txtStatus;
    String jName;
    Integer Callenabled;
    Button btnDownload,CallStop,CallContinue;
    JSONObject obj = new JSONObject();
    JSONArray jA = new JSONArray();
    String GoogleId;
    TextToSpeech t1;
    EditText ed1;
    Button b1,b2;
    int i=0;
    int contact_count=0;
    public static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(
                Context.TELEPHONY_SERVICE);
        Callenabled = 1;
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
                    repeatCall();
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
        CallStop = (Button) findViewById(R.id.CALLSTOP);
        CallContinue = (Button) findViewById(R.id.CALLCONTINUE);
        CallStop.setOnClickListener(this);
        CallContinue.setOnClickListener(this);
//        Button fab = (Button) findViewById(R.id.btn1);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               callNow();
//            }
//        });

        b2=(Button)findViewById(R.id.btn2);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                repeatCall();
            }
        });
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE},
                MY_PERMISSIONS_REQUEST_CALL);




    }

    @Override
    public void onClick(View v) {
        Log.d("info","Button clicked:"+v.getId());
        switch(v.getId()) {
            case R.id.CALLCONTINUE:
                Callenabled = 1;
                Log.d("info","CallEnabled:"+"TRUE");
                repeatCall();
                break;
            case R.id.CALLSTOP:
                 Callenabled = 0;
                Log.d("info","CallEnabled:"+"FALSE");
                break;


        }
    }
    private void processJson(JSONObject object) {


        try {
            JSONArray rows = object.getJSONArray("rows");
            Toast.makeText(getApplicationContext(),
                    rows.length()+" Contacts Downloaded", Toast.LENGTH_LONG).show();
            contact_count = rows.length();
           //int cou = contact_count - i;
            String Status =  "0 Contacts Called " + contact_count+" Contacts Remaining";
            txtStatus.setText(Status);

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
                Log.d("info", "Name=" + name);
                Log.d("info", "Number=" + number);
                obj.put("Name", name);
                obj.put("Number", number);
                jA.put(obj);

            }

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
        GoogleId = txtGoogleId.getText().toString();

        Toast.makeText(getApplicationContext(),
                "Downloading Excel. Please wait ...", Toast.LENGTH_LONG).show();
        DownloadWebpageTask dow = new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {

                processJson(object);

            }
        });

        // String final_google_id  = getGoogleId(GoogleId);
        String final_google_id = "1C7XyjLQj0t6waLGlDWsvVdGtM0JWh24RFi0ZiR5L6w0";
        dow.execute("https://spreadsheets.google.com/tq?key="+final_google_id);

    }

    public void callNow()
    {           Log.d("info","inside Call now");
                 ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE},
                MY_PERMISSIONS_REQUEST_CALL);
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Info", "Call now");
                       // repeatCall();
                 //   repeatCall();
//                    b2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                            String toSpeak = ed1.getText().toString();
////                            Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
////                            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
//                             repeatCall();
//                        }
//                    });

//        intent.setData(Uri.parse("tel:" + bundle.getString("mobilePhone")));
//                    for(int i=0;i<3;i++) {
//                       repeatCall();
//                        Log.d("Info","Intent Call");
//
//                    }
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
            if(Callenabled == 1) {
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
                Runnable showDialogRun = new Runnable() {
                    public void run() {
                        Intent showDialogIntent = new Intent(MainActivity.this, DisplayMessageActivity.class);
                        showDialogIntent.putExtra("Name", jName);
                        startActivityForResult(showDialogIntent, 2);

                    }
                };
                Handler h = new Handler();
                h.postDelayed(showDialogRun, 2000);
            }
        }
        catch (JSONException e){
                            e.printStackTrace();
                        }



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("info","Inside OnActivityResult");
        //Log.d("info","I value="+i);

        if (requestCode == 1) {

           // repeatCall();
            Log.d("info","Inside requestCode");
            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                Log.d("info","Inside result Ok");
//                if(i<=contact_count) {
//                   repeatCall();
//                }
//                if (resultCode == RESULT_CANCELED){
//                    Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
//                    // The user picked a contact.
//                }
//                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
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
        }

//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                // this code will be executed after 2 seconds
//                if(i<contact_count){
//                    repeatCall();
//                }
//            }
//        }, 2000);

////        if (requestCode == REQUEST_CODE) {
//
//            Log.d("info","I value inside if="+i);
//             if(i<=1) {
//        //          repeatCall();
////                  i++;
//            i++;
//            Log.d("info","resultcode"+resultCode);
//            if (resultCode == Activity.RESULT_OK) {
//              //  int result = data.getIntExtra("pos");
//                Log.d("info","Call ok");
//                i++;
//                repeatCall();
//                // do something with the result
//
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                // some stuff that will happen if there's no result
//                Log.d("info","Call Cancelled");
//                repeatCall();
//            }
//        }}
    }

