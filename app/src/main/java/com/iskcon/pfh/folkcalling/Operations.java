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
    String jName,csvFilename;
    Integer Callenabled;
    Button CallStop,UpdateCallStatus,Report,PickDate,PickTime,PickCalendar;


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
//        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel();
//            }
//
//        };


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });


//        PickDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setDateTimeField() ;
//            }
//        });



        Callenabled = 1;
        CallStop = (Button) findViewById(R.id.CALLSTOP);

        UpdateCallStatus = (Button)findViewById(R.id.UpdateCallStatus);
       // CallContinue = (Button) findViewById(R.id.CALLCONTINUE);
        CallStop.setOnClickListener(this);
//        CallContinue.setOnClickListener(this);
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
//    private void setDateTimeField() {
//        Calendar newCalendar = dateSelected;
//        String myFormat = "MM/dd/yy";
//        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
//                   CallComment.setText(sdf.format(dateSelected.getTime()));
//            }
//
//        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//        CallComment.setText(sdf.format(dateSelected.getTime()));
//    }
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
//                    JSONObject NextCaller = jA.getJSONObject(i);
//                    String ProgramName = NextCaller.get("Program").toString();
//                    String SourceName = NextCaller.get("Source").toString();
//                    String CollegeName = NextCaller.get("College").toString();
//                    String CampaignedName = NextCaller.get("Campaigned").toString();
//                    String DOR = NextCaller.get("DOR").toString();
//                    String DOP = NextCaller.get("DOP").toString();
//                    TextView ProgName = (TextView)findViewById(R.id.txtProgramName);
//                    ProgName.setText(ProgramName);
//                    TextView SourName = (TextView)findViewById(R.id.txtSource);
//                    SourName.setText(SourceName);
//                    TextView CollName = (TextView)findViewById(R.id.txtCollege);
//                    CollName.setText(CollegeName);
//                    TextView Camp = (TextView)findViewById(R.id.txtCampaigned);
//                    Camp.setText(CampaignedName);
//                    TextView DORText = (TextView)findViewById(R.id.txtDOR);
//                    DORText.setText(DOR);
//                    TextView DOPName = (TextView)findViewById(R.id.txtProgramDate);
//                    DOPName.setText(DOP);


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
                k.putExtra("filename",csvFilename);

                startActivity(k);
                break;
            case R.id.GET_FILE:
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(i, 15);
                break;

            case R.id.PickDate:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;

            case R.id.TimePick:
                DialogFragment newTimeFragment = new TimePickerFragment();
                newTimeFragment.show(getSupportFragmentManager(), "datePicker");
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
    public void updateStatus(String Name,String number, String Status,String comm)
    {
        //EditText cFilename = (EditText) findViewById(R.id.LFileInput);
       // String csvFilename = cFilename.getText().toString();
        CallStatusUpdate updateCall = new CallStatusUpdate();
        String status = updateCall.getStatus(Status);
        if (status.equals("Y3"))
        {
            addRemainder("Dilip","9663898009","Hello");
        }
        else {
            updateCall.writeStatus(Name,number, Status, comm, this, csvFilename);
            Toast.makeText(getApplicationContext(), "Status Updated",
                    Toast.LENGTH_SHORT).show();
//            getLastOutgoingCallDuration(this);
            repeatCall();
        }


    }

    public void addRemainder(String name,String number,String day)
    {
        Calendar beginTime = Calendar.getInstance();
        day="20 9 2017 7";
        String [] time = day.split(" ");
        Log.d("info","inside add Remainder");
        int year = Integer.parseInt(time[2]) ;
        int month = Integer.parseInt(time[1])-1;
        int Cday = Integer.parseInt(time[0]);
        int hour = Integer.parseInt(time[3]);

        Log.d("info","Inside Remainder");
        //Log.d("info","")
        beginTime.set(year,month,Cday,hour,0);

        Calendar endTime = Calendar.getInstance();
        endTime.set(year,month,Cday,hour,5);
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
            repeatCall();
        }

    }

//    public void getCallLog()
//    {
//        String number = "123456789";
//        String whereClause = CallLog.Calls.NUMBER + " = " + number;
//
//        Cursor c =  getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, CallLog.Calls.NUMBER + " = ? " ,
//                new String[]{number}, CallLog.Calls.DATE + " DESC");
//    }
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
            Log.d("info","CallDuration:"+durationCol);


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
        //selectedDate.setText(day+"."+month+"."+year);
//        Gyear = year;
//        Gmonth = month;
//        Gday = day;
//        Update fragment = (Update)getSupportFragmentManager().findFragmentByTag("update");
//        try {
//            fragment.setDate(year,month,day);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

    }
    public void getSelectedTime(int Hour,int minutes) throws ParseException
    {
         String pickedTime = ""+(Hour)+":"+minutes;
         String commentText = CallComment.getText().toString();
         commentText = commentText + " Time : "+pickedTime;
         CallComment.setText(commentText);
//        Settings fragment = (Settings)getSupportFragmentManager().findFragmentByTag("settings");
//        //try {
//        fragment.SetTime(Hour,minutes);
////        } catch (ParseException e) {
////            e.printStackTrace();
////        }
    }
}