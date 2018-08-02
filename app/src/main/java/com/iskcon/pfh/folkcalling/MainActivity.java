package com.iskcon.pfh.folkcalling;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;

import static android.R.attr.path;
import static android.R.attr.permission;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL = 0;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int FILE_SELECT_CODE = 0;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
    };
    ArrayList<String> selectedPrograms = new ArrayList<>();
    EditText txtGoogleId;
    TextView txtStatus,lFileInput;
    String jName;
    Integer Callenabled;
    Button btnDownload;
    ImageView SearchFile;
    String csvFilename;
    JSONArray jA = new JSONArray();
    View vi;
    String GoogleId;
    TextToSpeech t1;
    CheckBox whatsapp;
    Button chooseProgram;
    int i=0;
    int contact_count=0;
    ArrayList<Contact> contacts;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("info","FilePath:"+ Environment.getExternalStorageDirectory().getAbsolutePath() );
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(
                Context.TELEPHONY_SERVICE);
        vi = this.findViewById(android.R.id.content);
        Speech.init(this,getPackageName());
        Callenabled = 1;
        ContactHelper.insertContact(getContentResolver(),"Dilip KV1","12345678");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE


            );
           // writeContact("ZABC","1234567890");

        }


        chooseProgram = (Button)findViewById(R.id.ChooseProgram);
        chooseProgram.setOnClickListener(this);

        txtStatus = (TextView) findViewById(R.id.Status);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                download_excel();
            }
        });
        whatsapp = (CheckBox)findViewById(R.id.checkbox_cheese);
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
      //  showContacts();

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

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        else
        {
            Boolean contactExists = ContactHelper.contactExists(this,"12367890");
            if(contactExists)
            {
                Toast.makeText(this,"Contact Exists",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this,"Contact Does not Exist",Toast.LENGTH_LONG).show();
            }
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
//        GoogleId = txtGoogleId.getText().toString();
        EditText cFilename = (EditText) findViewById(R.id.LFileInput);
        csvFilename = cFilename.getText().toString();
        Log.d("info","FIlename:"+csvFilename);
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
        else
        {
            Toast.makeText(this,"Please Select a file",Toast.LENGTH_LONG).show();
        }






        }



    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Info", "Call now");
 
                }
                }
            case PERMISSIONS_REQUEST_READ_CONTACTS:{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

               Boolean contactExists = ContactHelper.contactExists(this,"1234567890");
               if(contactExists)
               {
                   Toast.makeText(this,"Contact Exists",Toast.LENGTH_LONG).show();
               }
               else
               {
                   Toast.makeText(this,"Contact Does not Exist",Toast.LENGTH_LONG).show();
               }
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
//                TextView updText = (TextView)findViewById(R.id.UpdatedStatus1);
//                String StatusText = updText.getText().toString();
//                StatusText = StatusText + "\n"+ returnValue;
//                updText.setText(StatusText);


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

                if (uriString.startsWith("content")) {
               //     Log.d("info","Inside Content"+getFilePath(file.getAbsolutePath().toString()));


                    lFileInput.setText(getFilePath(uri.getPath(),false));
                    csvFilename = lFileInput.getText().toString();
                    Toast.makeText(this,uri.getPath().toString(), Toast.LENGTH_LONG);


                } else if (uriString.startsWith("file://")) {
                    Toast.makeText(this,uriString.toString(), Toast.LENGTH_LONG);
                   lFileInput.setText(getFilePath(data.getData().toString(),true));

                  //  EditText cFilename = (EditText) findViewById(R.id.LFileInput);
                    csvFilename = lFileInput.getText().toString();
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

    public void showAddContacts(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Contacts from the file will be added to your phonebook for enabling sending Whatsapp messages. It can later be deleted once the messages are sent ");
        builder.setTitle("Add Contacts");
// Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getContacts();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
          whatsapp.setChecked(false);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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

public void getContacts() {
    try {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Adding Contacts");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progress.show();
                    }
                });

                CallStatusUpdate callManager = new CallStatusUpdate();

                try {
                    selectedPrograms.add("ALL");
                    contacts = callManager.getWhatsappMessages("ALL", csvFilename,selectedPrograms);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // Toast.makeText(getApplicationContext(), contacts.size() + "Downloaded", Toast.LENGTH_LONG).show();
                addContacts();
                contact_count = contacts.size();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progress.dismiss();
                        // callAsynchronousTask();
                    }
                });
            }
        };
        new Thread(runnable).start();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void addContacts()
    {
        //Toast.makeText(con,"Inside Write Contacts",Toast.LENGTH_LONG).show();
        for(int i=0;i<contacts.size();i++)
        {

            try {

                Contact contact = contacts.get(i);
                String Number = contact.number;
                Log.d("info","ContactNumber:"+Number);
                Boolean ContactExists = ContactHelper.contactExists(this,Number);
                Log.d("info","ContactExists:"+ContactExists);
                if(!ContactExists)
                {
                    String Name = contact.name;
                    ContactHelper.writeContact(getActivity(),Name,Number);
                    Log.d("info","Writing Contact");
                }

            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}