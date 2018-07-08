package com.iskcon.pfh.folkcalling;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Operations extends AppCompatActivity implements View.OnClickListener, ItemFragment.OnListFragmentInteractionListener {
    ExcelAccess EA;
    CallStatusUpdate callManager;
    EditText CallComment;
    Activity act;
    TextView txtStatus, lFileInput;
    String jName, csvFilename, SmsPrefix, A1txt, A3txt, Inactivetxt, jNumber, SearchName, SearchNumber;
    Boolean A1Status, A3Status, InactiveStatus;
    Integer Callenabled, SearchStatus = 0;
    Button CallStop, UpdateCallStatus, Report, PickDate, PickTime, PickCalendar;
    private ArrayList < String > sPrograms;
    ArrayList < Contact > contacts = new ArrayList < Contact > ();
    ArrayList < Contact > Searchcontacts = new ArrayList < Contact > ();
    AutoCompleteTextView actv;
    TextToSpeech t1;
    String[] numbers;
    Button b2;
    int i = 0, pre_q = 0;
    int contact_count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations);
        EA = new ExcelAccess();
        callManager = new CallStatusUpdate();
        act = getActivity();

        // recyclerView.setAdapter(mAdapter);
        txtStatus = (TextView) findViewById(R.id.Status);
        b2 = (Button) findViewById(R.id.btn2);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                repeatCall();
            }
        });
        PickDate = (Button) findViewById(R.id.PickDate);
        PickTime = (Button) findViewById(R.id.TimePick);
        PickCalendar = (Button) findViewById(R.id.CAL);
        final Calendar myCalendar = Calendar.getInstance();
        CallComment = (EditText) findViewById(R.id.CallComment);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });

        Callenabled = 1;
        CallStop = (Button) findViewById(R.id.CALLSTOP);

        UpdateCallStatus = (Button) findViewById(R.id.UpdateCallStatus);
        CallStop.setOnClickListener(this);

        UpdateCallStatus.setOnClickListener(this);
        Report = (Button) findViewById(R.id.REPORT);
        Report.setOnClickListener(this);
        PickDate.setOnClickListener(this);
        PickCalendar.setOnClickListener(this);
        PickTime.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        Boolean LFileCheck = extras.getBoolean("LocalFile");
        if (LFileCheck == true) {
            csvFilename = extras.getString("Filename");
            final String StatusValue = extras.getString("StatusValue");
            final String TeleCaller = extras.getString("TeleCaller");
            final String DayValue = extras.getString("DayValue");
            //            String PrValue=extras.getString("PrValue");
            final ArrayList < String > selectedPrograms = extras.getStringArrayList("PrValues");
            Log.d("info", "SelPrograms:" + selectedPrograms);
            SmsPrefix = extras.getString("smsPrefix");
            A1txt = extras.getString("A1txt");
            A3txt = extras.getString("A3txt");
            Inactivetxt = extras.getString("Inactivetxt");
            A1Status = extras.getBoolean("A1SmsStatus");
            A3Status = extras.getBoolean("A3SmsStatus");
            InactiveStatus = extras.getBoolean("InactiveSmsStatus");

            final Context co = this;



            try {
                sPrograms = new ArrayList < > ();
                sPrograms.add("ALL");

                final ProgressDialog progress = new ProgressDialog(this);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        progress.setTitle("Loading Contacts");
                        progress.setMessage("Please wait ...");
                        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progress.show();
                            }
                        });
                        //


                        Searchcontacts = callManager.getCallDataStatus1("ALL", co, csvFilename, "ALL", "ALL", sPrograms);
                        Log.d("info", "SearchContacts Size:" + Searchcontacts.size());

                          numbers = new String[Searchcontacts.size()];
                        for (int z = 0; z < Searchcontacts.size(); z++) {
                            Log.d("info", "SearchConta name:" + Searchcontacts.get(z).name);
                            numbers[z] = Searchcontacts.get(z).number + " " + Searchcontacts.get(z).name;
                            Log.d("info", "Z Value:" + z);
                            Log.d("info", "number+name:" + numbers[z]);
                        }

                        // contacts = EA.fileResource(StatusValue, co, csvFilename, TeleCaller,DayValue,selectedPrograms);

                        contacts = callManager.getCallDataStatus1(StatusValue, co, csvFilename, TeleCaller, DayValue, selectedPrograms);
                        Log.d("info", "SearchContacts Size1:" + contacts);
                        contact_count = contacts.size();

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                ArrayAdapter < String > adapter = new ArrayAdapter < String >
                                        (co, android.R.layout.select_dialog_item, numbers);
                                AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.SearchNumber);
                                actv.setThreshold(1); //will start working from first character
                                actv.setAdapter(adapter);

                                actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView <?> adapter, View view, int position, long id) {
                                        Log.d("info", "Item Selected:" + position + ":" + id);
                                        String selection = (String) adapter.getItemAtPosition(position);
                                        String[] outputEle = selection.split(" ", 2);
                                        TextView nameStatus = (TextView) findViewById(R.id.txtStatus);
                                        SearchName = outputEle[1];
                                        Log.d("info", "Search Name:" + SearchName);
                                        SearchNumber = outputEle[0];
                                        nameStatus.setText("Update Call Status for " + SearchName);
                                        SearchStatus = 1;
                                        int pos = -1;

                                        for (int i = 0; i < numbers.length; i++) {
                                            if (numbers[i].equals(selection)) {
                                                pos = i;

                                                Log.d("info", "Number:" + pos);
                                                break;
                                            }
                                        }
                                        pre_q = i;
                                        i = pos;
                                        Log.d("info", "Searched Number position:" + i);
                                    }


                                });
                                progress.dismiss();
                                String Status = "0 Contacts Called " + contact_count + " Contacts Remaining";
                                txtStatus.setText(Status);
                            }
                        });
                        //
                    }
                };

                new Thread(runnable).start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

        }

    }

    public Activity getActivity() {
        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
    @Override
    public void onClick(View v) {
        Log.d("info", "Button clicked:" + v.getId());
        switch (v.getId()) {

            case R.id.CALLSTOP:
                if (Callenabled == 0) {
                    Callenabled = 1;
                    CallStop.setBackgroundResource(R.mipmap.pause);
                } else {
                    Callenabled = 0;
                    CallStop.setBackgroundResource(R.mipmap.play);

                }
                Log.d("info", "CallEnabled:" + "FALSE");
                break;
            case R.id.UpdateCallStatus:
                UpdateCallStatus.setEnabled(false);
                UpdateCallStatus.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UpdateCallStatus.setEnabled(true);
                    }
                }, 5000);
                Spinner sta = (Spinner) findViewById(R.id.updateSpinner);
                EditText comments = (EditText) findViewById(R.id.CallComment);
                String StatusValue = sta.getSelectedItem().toString();
                String comm = comments.getText().toString();
                updateStatus(StatusValue, comm);


                Log.d("info", "StatusValue:" + StatusValue);

                //   EditText comments = (EditText)findViewById(R.id.CallComment);
                comments.setText("");



                break;
            case R.id.REPORT:
                //  CallStatusUpdate updateCall = new CallStatusUpdate();

                Intent k = new Intent(getApplicationContext(), StatusActivity.class);

                k.putExtra("filename", csvFilename);

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

                // JSONObject objects = jA.getJSONObject(i);
                Contact con = contacts.get(i - 1);
                Log.d("info", "CallRowNo:" + i);
                String name = con.name;
                jNumber = con.number;
                sta = (Spinner) findViewById(R.id.updateSpinner);
                comments = (EditText) findViewById(R.id.CallComment);
                StatusValue = sta.getSelectedItem().toString();
                comm = comments.getText().toString();
                Log.d("info", "StatusValue:" + StatusValue);
                String timeSchedule[] = calDayTime(comm);
                con.RemainderDay = timeSchedule[0];
                con.RemainderTime = timeSchedule[1];

                //  updateStatus(name,jNumber,StatusValue,comm);
                addRemainder(name, jNumber, comm);
                //   EditText comments = (EditText)findViewById(R.id.CallComment);
                comments.setText("");



                break;


        }

    }



    public void repeatCall() {
        //        Intent showDialogIntent = new Intent(this, DisplayMessageActivity.class);
        //        startActivity(showDialogIntent);

        Log.d("info", "Connecting Call");
        Intent intent = new Intent(Intent.ACTION_CALL);
        if (SearchStatus == 0) {
            if (Callenabled == 1 && i < contact_count) {

                Contact contact = contacts.get(i);
                jName = contact.name;
                jNumber = contact.number;


                intent.setData(Uri.parse("tel:" + jNumber));
                startActivity(intent);

                //startActivityForResult(intent, REQUEST_CODE);

                Toast.makeText(getApplicationContext(),
                        "Calling " + jName, Toast.LENGTH_LONG).show();


                TextView nameStatus = (TextView) findViewById(R.id.txtStatus);
                nameStatus.setText("Update Call Status for " + jName);

                Runnable showDialogRun = new Runnable() {
                    public void run() {
                        showDetailDialog();


                    }
                };
                Handler h = new Handler();
                h.postDelayed(showDialogRun, 3000);
                i++;
                int cou = contact_count - i;
                String Status = i + " Contacts Called " + cou + " Contacts Remaining";
                txtStatus.setText(Status);

            }
        }
        if (SearchStatus == 1) {
            Contact contact = Searchcontacts.get(i);
            jName = contact.name;
            jNumber = contact.number;
            intent.setData(Uri.parse("tel:" + jNumber));
            startActivity(intent);
            Toast.makeText(getApplicationContext(),
                    "Calling " + jName, Toast.LENGTH_LONG).show();


        }

    }

    private void showDetailDialog() {
        final Dialog d = new Dialog(this, R.style.CustomDialogTheme);
        d.setContentView(R.layout.custom_dialog);
        d.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        if (android.os.Build.VERSION.SDK_INT <= 23){
            d.show();
        } else{
            // do something for phones running an SDK before lollipop
        }


        Contact NextCaller = contacts.get(i - 1);
        //  JSONObject NextCaller = jA.getJSONObject(i - 1);
        // String PersonName = NextCaller.get("Name").toString();
        String PersonName = NextCaller.name;
        TextView PersonNameText = (TextView) d.findViewById(R.id.PersonName);
        PersonNameText.setText(PersonName);
        String ProgramName = NextCaller.program;
        TextView PRnametext = (TextView) d.findViewById(R.id.txtProgramName);
        PRnametext.setText(ProgramName);
        String SourceName = NextCaller.source;
        TextView SourceText = (TextView) d.findViewById(R.id.txtSource);
        SourceText.setText(SourceName);
        String CollegeName = NextCaller.ColCompany;
        TextView CollegeNameText = (TextView) d.findViewById(R.id.txtCollege);
        CollegeNameText.setText(CollegeName);
        String CampaignedName = NextCaller.campaignedBy;
        TextView CampaingendNameText = (TextView) d.findViewById(R.id.txtCampaigned);
        CampaingendNameText.setText(CampaignedName);
        String PComments = NextCaller.TotalComments;
        TextView PCommentsText = (TextView) d.findViewById(R.id.txtComments);
        PCommentsText.setText(PComments);
        String DOR = NextCaller.dor;
        TextView DORtext = (TextView) d.findViewById(R.id.txtDOR);
        DORtext.setText(DOR);
        String DOP = NextCaller.dop;
        TextView DOPText = (TextView) d.findViewById(R.id.txtProgramDate);
        DOPText.setText(DOP);

        Button close_btn = (Button) d.findViewById(R.id.dialogButtonOK);
        close_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }


    public void updateStatus(String Status, String comm) {

        try {
            CallStatusUpdate updateCall = new CallStatusUpdate();
            String status = updateCall.getStatus(Status);
            final String com = comm;
            final String Stat = Status;
            final Context co = this;

            new Thread(new Runnable() {
                public void run() {
                    Contact contac = new Contact();
                    if (SearchStatus == 1) {
                        Log.d("info", "Current Search position" + i);
                        int updateContact = i;


                        contac = Searchcontacts.get(updateContact);

                        Log.d("info", "Search Contact Update Name:" + contac.name);
                    } else if (SearchStatus == 0) {
                        contac = contacts.get(i - 1);

                    }
                    callManager.writeStatus1(act, contac, Stat, com, co, csvFilename, SmsPrefix, A1txt, A1Status, A3txt, A3Status, Inactivetxt, InactiveStatus);


                }

            }).start();


            Log.d("info", "SmsStatus: Inactive Status :" + InactiveStatus + " A3 Status:" + A3Status);
            Toast.makeText(getApplicationContext(), "Status Updated",
                    Toast.LENGTH_SHORT).show();

            Log.d("info", "CallStatusOperations:" + status);
            //  getLastOutgoingCallDuration(this);
            if (SearchStatus == 0) {
                repeatCall();
            } else if (SearchStatus == 1) {
                SearchStatus = 0;
                i = pre_q;
                pre_q = 0;
            }


        } catch (ArrayIndexOutOfBoundsException IOE) {
            Toast.makeText(this, IOE.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String[] calDayTime(String day) {
        Calendar beginTime = Calendar.getInstance();

        String[] time = day.split(" ");
        Log.d("info", "TimeValue:" + time[2]);

        Log.d("info", "TimeValue2:" + time[5]);
        Log.d("info", "inside add Remainder");

        String[] datesplit = time[2].split("/");
        String[] timesplit = time[5].split(":");
        int gday = Integer.parseInt(datesplit[0]);
        int month = Integer.parseInt(datesplit[1]) - 1;
        int year = Integer.parseInt(datesplit[2]);
        int hour = Integer.parseInt(timesplit[0]);
        int minu = Integer.parseInt(timesplit[1]);

        String dayt = gday + "/" + month + "/" + year;
        String timet = hour + ":" + minu;

        String ret[] = {
                dayt,
                timet
        };

        return ret;
    }
    public void addRemainder(String name, String number, String day) {

        Calendar beginTime = Calendar.getInstance();

        String[] time = day.split(" ");
        Log.d("info", "TimeValue:" + time[2]);

        Log.d("info", "TimeValue2:" + time[5]);
        Log.d("info", "inside add Remainder");

        String[] datesplit = time[2].split("/");
        String[] timesplit = time[5].split(":");
        int gday = Integer.parseInt(datesplit[0]);
        int month = Integer.parseInt(datesplit[1]) - 1;
        int year = Integer.parseInt(datesplit[2]);
        int hour = Integer.parseInt(timesplit[0]);
        int minu = Integer.parseInt(timesplit[1]);

        Log.d("info", "sendAlarm:" + gday + ":" + month + ":" + year + ":" + hour + ":" + minu);

        Log.d("info", "Inside Remainder");
        //Log.d("info","")
        beginTime.set(year, month, gday, hour, minu);

        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month, gday, hour, minu + 5);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(Events.TITLE, "Calling Remainder")
                .putExtra(Events.DESCRIPTION, "Call " + name + " Number:" + number)
                .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
        // .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        this.startActivityForResult(intent, 200);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 200) {

            Toast.makeText(getApplicationContext(),
                    "Remainder Successfully added", Toast.LENGTH_LONG).show();
            //   repeatCall();
        }

    }


    //    public String getLastOutgoingCallDuration(final Context context) {
    //        String output = null;
    //
    //        final Uri callog = CallLog.Calls.CONTENT_URI;
    //        Cursor cursor = null;
    //
    //        try {
    //            // Query all the columns of the records that matches "type=2"
    //            // (outgoing) and orders the results by "date"
    //            cursor = context.getContentResolver().query(callog, null,
    //                    CallLog.Calls.TYPE + "=" + CallLog.Calls.OUTGOING_TYPE,
    //                    null, CallLog.Calls.DATE);
    //            final int durationCol = cursor
    //                    .getColumnIndex(CallLog.Calls.DURATION);
    //            final int CallType = cursor.getColumnIndex(CallLog.Calls.TYPE);
    //            Log.d("info", "CallType:" + CallType);
    //
    //            if (CallType == CallLog.Calls.MISSED_TYPE) {
    //                Toast.makeText(getApplicationContext(),
    //                        "Missed Call", Toast.LENGTH_LONG).show();
    //                Log.d("info", "CallDuration:" + durationCol);
    //            } else if (CallType == CallLog.Calls.OUTGOING_TYPE) {
    //                Toast.makeText(getApplicationContext(),
    //                        "OutGoing Call " + durationCol, Toast.LENGTH_LONG).show();
    //            }
    //
    //
    //            // Retrieve only the last record to get the last outgoing call
    //            if (cursor.moveToLast()) {
    //                // Retrieve only the duration column
    //                output = cursor.getString(durationCol);
    //            }
    //        } finally {
    //            // Close the resources
    //            if (cursor != null) {
    //                cursor.close();
    //            }
    //        }
    //
    //
    //        return output;
    //    }


    public void getSelectedDate(int year, int month, int day) {
        String pickedDate = "Date : " + (day) + "/" + (month + 1) + "/" + year;
        CallComment.setText(pickedDate);

    }

    public void getSelectedTime(int Hour, int minutes) throws ParseException {
        String pickedTime = "" + (Hour) + ":" + minutes;
        String commentText = CallComment.getText().toString();
        commentText = commentText + " Time : " + pickedTime;
        CallComment.setText(commentText);

    }


    @Override
    public void onListFragmentInteraction(CallUpdate item) {
        Toast.makeText(getApplicationContext(), "History Fragment",
                Toast.LENGTH_LONG).show();
    }

}