package com.iskcon.pfh.folkcalling;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class Operations extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL = 0;
    private static final int FILE_SELECT_CODE = 0;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    EditText txtGoogleId;
    TextView txtStatus,lFileInput;
    String jName,csvFilename;
    Integer Callenabled;
    Button btnDownload,CallStop,CallContinue,UpdateCallStatus,Report;
    ImageView SearchFile;

    JSONArray jA = new JSONArray();
    View vi;
    String GoogleId;
    TextToSpeech t1;

    Button b2;
    int i=0;
    int contact_count=0;
    public static final int REQUEST_CODE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations);
        txtStatus = (TextView) findViewById(R.id.Status);
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
        Callenabled = 1;
        CallStop = (Button) findViewById(R.id.CALLSTOP);
        UpdateCallStatus = (Button)findViewById(R.id.UpdateCallStatus);
        CallContinue = (Button) findViewById(R.id.CALLCONTINUE);
        CallStop.setOnClickListener(this);
        CallContinue.setOnClickListener(this);
        UpdateCallStatus.setOnClickListener(this);
        Report = (Button) findViewById(R.id.REPORT);
        Report.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        Boolean LFileCheck  = extras.getBoolean("LocalFile");
        if (LFileCheck == true)
        {
            csvFilename=extras.getString("Filename");
            String StatusValue=extras.getString("StatusValue");
            String TeleCaller=extras.getString("TeleCaller");
            String DayValue=extras.getString("DayValue");
            String PrValue=extras.getString("PrValue");
            CallStatusUpdate CallData = new CallStatusUpdate();
            jA = CallData.getCallDataStatus(StatusValue,this,csvFilename,TeleCaller,DayValue,PrValue);
            Toast.makeText(getApplicationContext(),
                    jA.length()+" Contacts Downloaded", Toast.LENGTH_LONG).show();
            contact_count = jA.length();
            Log.d("info","DownloadedData:"+jA);
            //int cou = contact_count - i;
            String Status =  "0 Contacts Called " + contact_count+" Contacts Remaining";
            txtStatus.setText(Status);
        }
        else
        {

        }
      //  Log.d("info","DownloadedLink"+link.toString());
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
            case R.id.UpdateCallStatus:
                try {
                    JSONObject objects = jA.getJSONObject(i-1);

                    String jNumber = objects.get("Number").toString();
                    Spinner sta = (Spinner) findViewById(R.id.updateSpinner);
                    EditText comments = (EditText)findViewById(R.id.CallComment);
                    String StatusValue = sta.getSelectedItem().toString();
                    String comm = comments.getText().toString();
                    Log.d("info","StatusValue:"+StatusValue);
                    updateStatus(jNumber,StatusValue,comm);
                    repeatCall();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                break;
            case R.id.REPORT:
                CallStatusUpdate updateCall = new CallStatusUpdate();
                int FinalReport[] = updateCall.getFinalReport();
                Intent k = new Intent(getApplicationContext(),StatusActivity.class);
                k.putExtra("finalReport",FinalReport);

                startActivity(k);
                break;
            case R.id.GET_FILE:
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(i, 15);
                break;

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
                Log.d("info","CalledName:"+jName);
                String jNumber = objects.get("Number").toString();
                String fNumber = jNumber;
                intent.setData(Uri.parse("tel:" + fNumber));
                // String na = name[i];
               // t1.speak("Calling " + jName, TextToSpeech.QUEUE_FLUSH, null);
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
            String Program = "";

            Log.d("info","SpinnerText:"+StatusValue);
            Log.d("info","TCValue:"+TeleCaller);
            Log.d("info","DayValue"+DayValue);
            CallStatusUpdate CallData = new CallStatusUpdate();
            jA = CallData.getCallDataStatus(StatusValue,this,csvFilename,TeleCaller,DayValue,Program);
            Toast.makeText(getApplicationContext(),
                    jA.length()+" Contacts Downloaded", Toast.LENGTH_LONG).show();
            contact_count = jA.length();
            //int cou = contact_count - i;
            String Status =  "0 Contacts Called " + contact_count+" Contacts Remaining";
            txtStatus.setText(Status);

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

    private String getGoogleId(String goog)
    {
        // goog = "https://docs.google.com/spreadsheets/d/1xk8AY8MOWiqwC3qvFEyOVN-wBdMtDW8QtirmcUkocrU/edit#gid=0";
        String[] words=goog.split("/");
        return words[5];
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
    public void updateStatus(String number, String Status,String comm)
    {
        //EditText cFilename = (EditText) findViewById(R.id.LFileInput);
       // String csvFilename = cFilename.getText().toString();
        CallStatusUpdate updateCall = new CallStatusUpdate();
        updateCall.writeStatus(number,Status,comm,this,csvFilename);
        Toast.makeText(getApplicationContext(),"Status Updated",
                Toast.LENGTH_SHORT).show();
    }
}
