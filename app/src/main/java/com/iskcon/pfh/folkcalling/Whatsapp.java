package com.iskcon.pfh.folkcalling;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Timer;

public class Whatsapp extends AppCompatActivity {
AutoCompleteTextView txtTemplate;
EditText txtMessage,imageFile,lFileInput;
ImageView search;
Button btnDownload;
String csvFilename;
Timer timer;
    Uri uri;
int i=0,contact_count=0;
    ArrayList<Contact> contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp);
        txtMessage = (EditText)findViewById(R.id.txtMessage);
        imageFile = (EditText)findViewById(R.id.LFileInput);
        txtTemplate = (AutoCompleteTextView)findViewById(R.id.txttemplate);
        String[] fL = FileUtil.readFileFromInternalStorage(this,"document_urls.txt");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, fL);
        Button btSave = (Button)findViewById(R.id.btnSaveTemplate);
        txtTemplate.setThreshold(0);//will start working from first character
        txtTemplate.setAdapter(adapter);
        lFileInput = (EditText)findViewById(R.id.LFileInput);
        btnDownload = (Button)findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                sendWhatsappMessage();
            }
        });
        search = (ImageView)findViewById(R.id.GET_FILE);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(i, 15);
            }
        });
        Bundle extras = getIntent().getExtras();
        csvFilename=extras.getString("filename");

        txtTemplate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Log.d("info", "Item Selected:" + position + ":" + id);
                String selection = (String) adapter.getItemAtPosition(position);
                String[] outputEle = selection.split("@");
                txtTemplate.setText(outputEle[0]);
                txtMessage.setText(outputEle[1]);


            }
        });
        ArrayList<String> sPrograms = new ArrayList<>();
        sPrograms.add("ALL");
        ExcelAccess EA = new ExcelAccess();
        try {
            ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
// To dismiss the dialog

            contacts = EA.fileResource("ALL", this, csvFilename, "ALL","ALL",sPrograms);
            Toast.makeText(getApplicationContext(),contacts.size()+"Downloaded",Toast.LENGTH_LONG).show();
            addContacts();
            progress.dismiss();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        contact_count = contacts.size();

                btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFile();
            }
        });
    }

    public void sendWhatsappMessage()
    {
        ExcelAccess EA = new ExcelAccess();
        Spinner status = (Spinner)findViewById(R.id.updateSpinner);
        String statusValue = status.getSelectedItem().toString();
        Context co = this;
        try {
            contacts=EA.getWhatsappMessages(this,statusValue,csvFilename);
            contact_count = contacts.size();
            Log.d("info","WhatsappContacts Size:"+contacts.size());
            callAsynchronousTask();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void addContacts()
    {
        Toast.makeText(this,"Inside Write Contacts",Toast.LENGTH_LONG).show();
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
                    ContactHelper.writeContact(this,Name,Number);
                    Log.d("info","Writing Contact");
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    public void callAsynchronousTask() {
//
//        final Handler handler = new Handler();
//        timer = new Timer();
//        TimerTask doAsynchronousTask = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        try {
//                            if (i<contact_count) {
//                                sendWhatsapp();
//                            }
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                        }
//                    }
//                });
//            }
//        };
//        timer.schedule(doAsynchronousTask, 0, 3000); //execute in every 50000 ms

        final Handler handler1 = new Handler();


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
      /* do what you need to do */
                sendWhatsapp();
      /* and here comes the "trick" */
                if(i<=contact_count)
                    handler1.postDelayed(this, 4000);
                else
                    handler1.removeCallbacks(this);
            }
        };

        handler1.postDelayed(runnable, 4000);
    }

//        final Handler handler1 = new Handler();
//
//
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//      /* do what you need to do */
//                sendWhatsapp();
//      /* and here comes the "trick" */
//                handler1.postDelayed(this, 3000);
//            }
//        };
//
//        handler1.postDelayed(runnable, 3000);
    public void sendWhatsapp()
    {
        Log.d("info","I Value:"+i);

               if(i<=contact_count) {

                   ArrayList<Uri> imageUriArray = new ArrayList<Uri>();
                   String Message = txtMessage.getText().toString();
                   Contact contact = contacts.get(i);

                   String final_message = Message;

                   Intent sendIntent = new Intent("android.intent.action.MAIN");

                   sendIntent.setAction(Intent.ACTION_SEND);

                   if (imageFile.getText().toString().isEmpty()) {
                       sendIntent.setType("text/plain");

                   } else {
                       imageUriArray.add(uri);
                       // sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                       sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
                       sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                       sendIntent.setType("image/*");
                   }
                   final_message = final_message.replace("<name>", contact.name);
                   sendIntent.putExtra(Intent.EXTRA_TEXT, final_message);


                   String no = "91" + contact.number;
                   sendIntent.putExtra("jid", no + "@s.whatsapp.net"); //phone number without "+" prefix
                   sendIntent.setPackage("com.whatsapp");
                   startActivityForResult(sendIntent, 1);
                   i++;
               }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("info", "Inside OnActivityResult");
        Log.d("info", "I value=" + i);
        if (requestCode == 1) {


            sendWhatsapp();
        }
        if(requestCode == 15) {
            if (resultCode == RESULT_OK) {
                Log.d("info", "file:" + data.getData());
                uri = data.getData();
                String uriString = uri.toString();
                lFileInput.setText(uriString);
            }}
    }

    public void saveFile()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save File Location");
        EditText  txtMessage = (EditText) findViewById(R.id.txtMessage);
        final String fLocation = txtMessage.getText().toString();
        String message =fLocation +  " \nGive Template Name for Saving the File";
        builder.setMessage(message);


// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        final Context now = this;
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mTemplateName = input.getText().toString();
                String wriFile = mTemplateName + "@"+fLocation;
                FileUtil.writeConfiguration(now,wriFile);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }
}
