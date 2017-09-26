package com.iskcon.pfh.folkcalling;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.CallLog;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Operations extends AppCompatActivity implements View.OnClickListener {

    EditText txtGoogleId,CallComment;
    TextView txtStatus,lFileInput;
    String jName,csvFilename,SmsPrefix,A1txt,A3txt,Inactivetxt;
    Boolean A1Status,A3Status,InactiveStatus;
    Integer Callenabled;
    Button CallStop,UpdateCallStatus,Report,PickDate,PickTime,PickCalendar;
    ArrayList<String> selectedPrograms = new ArrayList<>();

    JSONArray jA = new JSONArray();
    View vi;
    String GoogleId;
    TextToSpeech t1;

    Button b2;
    int i=0;
    int contact_count=0;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
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
        PickDate= (Button) findViewById(R.id.PickDate);
        PickTime = (Button) findViewById(R.id.TimePick);
        PickCalendar = (Button) findViewById(R.id.CAL);
        final Calendar myCalendar = Calendar.getInstance();
        CallComment = (EditText)findViewById(R.id.CallComment);

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
        CallStop.setOnClickListener(this);

        UpdateCallStatus.setOnClickListener(this);
        Report = (Button) findViewById(R.id.REPORT);
        Report.setOnClickListener(this);
        PickDate.setOnClickListener(this);
        PickCalendar.setOnClickListener(this);
        PickTime.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        Boolean LFileCheck  = extras.getBoolean("LocalFile");
        if (LFileCheck == true)
        {
            csvFilename=extras.getString("Filename");
            String StatusValue=extras.getString("StatusValue");
            String TeleCaller=extras.getString("TeleCaller");
            String DayValue=extras.getString("DayValue");
            String PrValue=extras.getString("PrValue");
            ArrayList<String> selectedPrograms = extras.getStringArrayList("PrValues");
            Log.d("info","SelPrograms:"+selectedPrograms);
            SmsPrefix = extras.getString("smsPrefix");
            A1txt = extras.getString("A1txt");
            A3txt = extras.getString("A3txt");
            Inactivetxt = extras.getString("Inactivetxt");
            A1Status = extras.getBoolean("A1SmsStatus");
            A3Status = extras.getBoolean("A3SmsStatus");
            InactiveStatus = extras.getBoolean("InactiveSmsStatus");
            CallStatusUpdate CallData = new CallStatusUpdate();
            jA = CallData.getCallDataStatus(StatusValue,this,csvFilename,TeleCaller,DayValue,PrValue,selectedPrograms);
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
//            case R.id.CALLCONTINUE:
//                Callenabled = 1;
//                Log.d("info","CallEnabled:"+"TRUE");
//                repeatCall();
//                break;
            case R.id.CALLSTOP:
                if(Callenabled == 0)
                {
                    Callenabled = 1;
                    CallStop.setBackgroundResource(R.mipmap.pause);
                }
                else
                {
                    Callenabled = 0;
                    CallStop.setBackgroundResource(R.mipmap.play);

                }
                Log.d("info","CallEnabled:"+"FALSE");
                break;
            case R.id.UpdateCallStatus:
                try {
                    JSONObject objects = jA.getJSONObject(i-1);
                    Log.d("info","CallRowNo:"+i);
                    String name = objects.get("Name").toString();
                    String jNumber = objects.get("Number").toString();
                    Spinner sta = (Spinner) findViewById(R.id.updateSpinner);
                    EditText comments = (EditText)findViewById(R.id.CallComment);
                    String StatusValue = sta.getSelectedItem().toString();
                    String comm = comments.getText().toString();
                    Log.d("info","StatusValue:"+StatusValue);
                    updateStatus(name,jNumber,StatusValue,comm);
                 //   EditText comments = (EditText)findViewById(R.id.CallComment);
                    comments.setText("");


                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                break;
            case R.id.REPORT:
                CallStatusUpdate updateCall = new CallStatusUpdate();

                Intent k = new Intent(getApplicationContext(),StatusActivity.class);

                k.putExtra("filename",csvFilename);

                startActivity(k);
                break;




            case R.id.GET_FILE:
                Intent z = new Intent(Intent.ACTION_GET_CONTENT);
                z.setType("*/*");
                startActivityForResult(z, 15);
                break;

            case R.id.PickDate:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;

            case R.id.TimePick:
                DialogFragment newTimeFragment = new TimePickerFragment();
                newTimeFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.CAL:
                try {
                    JSONObject objects = jA.getJSONObject(i);
                    Log.d("info","CallRowNo:"+i);
                    String name = objects.get("Name").toString();
                    String jNumber = objects.get("Number").toString();
                    Spinner sta = (Spinner) findViewById(R.id.updateSpinner);
                    EditText comments = (EditText)findViewById(R.id.CallComment);
                    String StatusValue = sta.getSelectedItem().toString();
                    String comm = comments.getText().toString();
                    Log.d("info","StatusValue:"+StatusValue);
                   // updateStatus(name,jNumber,StatusValue,comm);
                    //   EditText comments = (EditText)findViewById(R.id.CallComment);
                    comments.setText("");
                    addRemainder(name,jNumber,comm);

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                break;


//`               new DatePickerDialog(MainActivity.this, date, myCalendar
//                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                break;

        }

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

      //  edittext.setText(sdf.format(myCalendar.getTime()));
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
                t1.speak(jName, TextToSpeech.QUEUE_FLUSH, null);
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

                Runnable showDialogRun = new Runnable() {
                            public void run() {
                                showDetailDialog();
                            }
                        };
                        Handler h = new Handler();
                        h.postDelayed(showDialogRun, 3000);
            }

            //vi.addView(ly1, params1);



        }
        catch (JSONException e){
            e.printStackTrace();
        }



    }
    private void showDetailDialog()
    {
        final Dialog d = new Dialog(this,R.style.CustomDialogTheme);
            d.setContentView(R.layout.custom_dialog);
            d.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            d.show();
        try {
            JSONObject NextCaller = jA.getJSONObject(i-1);
            String PersonName = NextCaller.get("Name").toString();
            TextView PersonNameText = (TextView) d.findViewById(R.id.PersonName);
            PersonNameText.setText(PersonName);
            String ProgramName = NextCaller.get("Program").toString();
            TextView PRnametext = (TextView) d.findViewById(R.id.txtProgramName);
            PRnametext.setText(ProgramName);
            String SourceName = NextCaller.get("Source").toString();
            TextView SourceText = (TextView) d.findViewById(R.id.txtSource);
            SourceText.setText(SourceName);
            String CollegeName = NextCaller.get("College").toString();
            TextView CollegeNameText = (TextView) d.findViewById(R.id.txtCollege);
            CollegeNameText.setText(CollegeName);
            String CampaignedName = NextCaller.get("Campaigned").toString();
            TextView CampaingendNameText = (TextView) d.findViewById(R.id.txtCampaigned);
            CampaingendNameText.setText(CampaignedName);

            String DOR = NextCaller.get("DOR").toString();
            TextView DORtext = (TextView) d.findViewById(R.id.txtDOR);
            DORtext.setText(DOR);
            String DOP = NextCaller.get("DOP").toString();
            TextView DOPText = (TextView) d.findViewById(R.id.txtProgramDate);
            DOPText.setText(DOP);
        }
        catch(JSONException e)
        {

        }
            Button close_btn = (Button) d.findViewById(R.id.dialogButtonOK);
            close_btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    d.dismiss();
                }
            });
    }



    public void updateStatus(String Name,String number, String Status,String comm)
    {
        //EditText cFilename = (EditText) findViewById(R.id.LFileInput);
       // String csvFilename = cFilename.getText().toString();
        CallStatusUpdate updateCall = new CallStatusUpdate();
        String status = updateCall.getStatus(Status);
        if (status.equals("Y3"))
        {
           // addRemainder("Dilip","9663898009","Hello");
        }
        else {
            updateCall.writeStatus(Name,number, Status, comm, this, csvFilename,SmsPrefix,A1txt,A1Status,A3txt,A3Status,Inactivetxt,InactiveStatus);
            Log.d("info","SmsStatus: Inactive Status :"+InactiveStatus+" A3 Status:"+A3Status);
            Toast.makeText(getApplicationContext(), "Status Updated",
                    Toast.LENGTH_SHORT).show();
            getLastOutgoingCallDuration(this);
            repeatCall();
        }


    }

    public void addRemainder(String name,String number,String day)
    {

        Calendar beginTime = Calendar.getInstance();

        String [] time = day.split(" ");
        Log.d("info","TimeValue:"+time[2]);

        Log.d("info","TimeValue2:"+time[5]);
        Log.d("info","inside add Remainder");

        String [] datesplit = time[2].split("/");
        String[] timesplit = time[5].split(":");
        int gday = Integer.parseInt(datesplit[0]);
        int month = Integer.parseInt(datesplit[1])-1;
        int year = Integer.parseInt(datesplit[2]);
        int hour = Integer.parseInt(timesplit[0]);
        int minu = Integer.parseInt(timesplit[1]);

        Log.d("info","sendAlarm:"+gday+":"+month+":"+year+":"+hour+":"+minu);

        Log.d("info","Inside Remainder");
        //Log.d("info","")
        beginTime.set(year,month,gday,hour,minu);

        Calendar endTime = Calendar.getInstance();
        endTime.set(year,month,gday,hour,minu+5);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(Events.TITLE, "Calling Remainder")
                .putExtra(Events.DESCRIPTION, "Call "+name+" Number:"+number)
                .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
        // .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        this.startActivityForResult(intent,200);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 200)
        {
           // repeatCall();
            Toast.makeText(getApplicationContext(),
                    "Remainder Successfully added", Toast.LENGTH_LONG).show();
        }

    }


    public String getLastOutgoingCallDuration(final Context context) {
        String output = null;

        final Uri callog = CallLog.Calls.CONTENT_URI;
        Cursor cursor = null;

        try {
            // Query all the columns of the records that matches "type=2"
            // (outgoing) and orders the results by "date"
            cursor = context.getContentResolver().query(callog, null,
                    CallLog.Calls.TYPE + "=" + CallLog.Calls.OUTGOING_TYPE,
                    null, CallLog.Calls.DATE);
            final int durationCol = cursor
                    .getColumnIndex(CallLog.Calls.DURATION);
            final int CallType = cursor.getColumnIndex(CallLog.Calls.TYPE);
            Log.d("info","CallType:"+ CallType);

            if(CallType == CallLog.Calls.MISSED_TYPE)
            {
                Toast.makeText(getApplicationContext(),
                        "Missed Call", Toast.LENGTH_LONG).show();
                Log.d("info","CallDuration:"+durationCol);
            }
            else if(CallType == CallLog.Calls.OUTGOING_TYPE)
            {
                Toast.makeText(getApplicationContext(),
                        "OutGoing Call "+durationCol, Toast.LENGTH_LONG).show();
            }




            // Retrieve only the last record to get the last outgoing call
            if (cursor.moveToLast()) {
                // Retrieve only the duration column
                output = cursor.getString(durationCol);
            }
        } finally {
            // Close the resources
            if (cursor != null) {
                cursor.close();
            }
        }


        return output;
    }



    public void getSelectedDate(int year, int month, int day)
    {
        String pickedDate = "Date : "+(day)+"/"+(month+1)+"/"+year;
        CallComment.setText(pickedDate);

    }
    public void getSelectedTime(int Hour,int minutes) throws ParseException
    {
         String pickedTime = ""+(Hour)+":"+minutes;
         String commentText = CallComment.getText().toString();
         commentText = commentText + " Time : "+pickedTime;
         CallComment.setText(commentText);

    }
}