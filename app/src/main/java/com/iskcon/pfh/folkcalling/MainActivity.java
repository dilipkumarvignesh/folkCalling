package com.iskcon.pfh.folkcalling;

import android.Manifest;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL = 0;
    private String[] no = {"+918939325360","+919962701659"};
    private String[] name = {"Dinasharan","Dilip"};
    EditText txtGoogleId;
    TextView txtStatus;
    String jName;
    Button btnDownload;
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

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
        txtGoogleId = (EditText) findViewById(R.id.txtGoogleId);
        txtStatus = (TextView) findViewById(R.id.Status);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("info", "onClick working");

                download_excel();
            }
        });
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                            String toSpeak = ed1.getText().toString();
////                            Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
////                            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
//                repeatCall();
//            }
//        });
        Button fab = (Button) findViewById(R.id.btn1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               callNow();
            }
        });

      //  ed1=(EditText)findViewById(R.id.editText);
//        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.btn2);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                            String toSpeak = ed1.getText().toString();
//                            Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
//                            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
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

//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String toSpeak = "Calling";
//                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
//                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
//            }
//        });

//        Intent intent = new Intent(Intent.ACTION_CALL);
//
////        intent.setData(Uri.parse("tel:" + bundle.getString("mobilePhone")));
//
//        intent.setData(Uri.parse("tel:" + "9663898009"));

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
    private void download_excel() {
        //  DownloadWebpageTask myTask = new DownloadWebpageTask();
        GoogleId = txtGoogleId.getText().toString();
        Log.d("info", "googleId=" + GoogleId);
        Toast.makeText(getApplicationContext(),
                "Downloading Excel. Please wait ...", Toast.LENGTH_LONG).show();
        DownloadWebpageTask dow = new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {

                processJson(object);

            }
        });
        //  GoogleId = ""+GoogleId;
        //GoogleId = ""+"1iuVKzHh2ueSkZ7pAGQBb4CmaqwXHpdd5a3lV89xpdGs";
       // GoogleId = "" + "1xk8AY8MOWiqwC3qvFEyOVN-wBdMtDW8QtirmcUkocrU";
        GoogleId = "1ZVxUrStq3US-Yyi22aojwESTAbYHZfcOKIhmBAYWqew";
        dow.execute("https://spreadsheets.google.com/tq?key="+GoogleId);
        //1iuVKzHh2ueSkZ7pAGQBb4CmaqwXHpdd5a3lV89xpdGs
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
                    repeatCall();
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
            JSONObject objects = jA.getJSONObject(i);
            jName = objects.get("Name").toString();
            String jNumber = objects.get("Number").toString();
            String fNumber = jNumber;
            intent.setData(Uri.parse("tel:" + fNumber));
           // String na = name[i];
            t1.speak("Calling " + jName, TextToSpeech.QUEUE_FLUSH, null);
            intent.setData(Uri.parse("tel:" + jNumber));
            startActivity(intent);
           // startActivityForResult(intent, REQUEST_CODE);
            Toast.makeText(getApplicationContext(),
                    "Calling "+jName, Toast.LENGTH_LONG).show();
            i++;
            int cou = contact_count - i;
            String Status = i+ " Contacts Called " + cou+" Contacts Remaining";
            txtStatus.setText(Status);
            Runnable showDialogRun = new Runnable() {
                public void run(){
                    Intent showDialogIntent = new Intent(MainActivity.this, DisplayMessageActivity.class);
                    showDialogIntent.putExtra("Name", jName);
                    startActivity(showDialogIntent);

                }
            };
            Handler h = new Handler();
            h.postDelayed(showDialogRun, 2000);
        }
        catch (JSONException e){
                            e.printStackTrace();
                        }
//        intent.setData(Uri.parse("tel:" + jNumber));
//        String na = name[i];
//        t1.speak(jName, TextToSpeech.QUEUE_FLUSH, null);


//        String na = name[i];
//         t1.speak(na, TextToSpeech.QUEUE_FLUSH, null);
//
//        startActivityForResult(intent, REQUEST_CODE);
//

//        if(i<=1) {
//            Log.d("info","I value="+i);
//            startActivityForResult(intent, REQUEST_CODE);
//        }
//        else{
//            return;
//        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("info","Inside OnActivityResult");
        Log.d("info","I value="+i);

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
