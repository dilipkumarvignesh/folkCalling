package com.iskcon.pfh.folkcalling;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class Whatsapp extends AppCompatActivity {
AutoCompleteTextView txtTemplate;
EditText txtMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp);
        txtMessage = (EditText)findViewById(R.id.txtMessage);

        txtTemplate = (AutoCompleteTextView)findViewById(R.id.txttemplate);
        String[] fL = FileUtil.readFileFromInternalStorage(this,"document_urls.txt");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, fL);
        Button btSave = (Button)findViewById(R.id.btnSaveTemplate);
        txtTemplate.setThreshold(1);//will start working from first character
        txtTemplate.setAdapter(adapter);

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

                btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFile();
            }
        });
    }


//    public void sendWhatsapp()
//    {
//        if (i<=contact_count && Callenabled == 1)
//        {
//            try{
//                ArrayList<Uri> imageUriArray = new ArrayList<Uri>();
//                String Message = txtMessage.getText().toString();
//
//                String jName="";
//                String jNumber="";
//                String temp = "";
//                String fileLocation = "";
//                String final_message = Message;
//                int object_count = objects.length();
//                for(int j=0;j<object_count;j++)
//                {
//                    if (j==0)
//                    {
//                        jName = objects.get(""+j).toString();
//
//                    }
//                    else if(j==1)
//                    {
//                        jNumber = objects.get(""+j).toString();
//                        jNumber= "91"+jNumber;
//                        Log.d("info","number"+jNumber);
//
//                    }
////                    else if(j==2)
////                    {
////                        fileLocation = objects.get(""+j).toString();
////                        Log.d("info","FileLocation:"+fileLocation);
////                    }
//                    else {
//                        temp = objects.get("" + j).toString();
////                        if(temp.contains("file") || temp.contains("content")||temp.contains("Content"))
////                        {
////                            imageUriArray.add(Uri.parse(temp));
////                        }
//                        final_message = final_message.replace("<" + j + ">", temp);
//                    }
//                }
////            String jName = objects.get("Name").toString();
////            String jNumber = objects.get("Number").toString();
//                Intent sendIntent = new Intent("android.intent.action.MAIN");
//
////                if(!(uri.toString().equals("")))
////                {
////                    imageUriArray.add(uri);
////                }
//                //  imageUriArray.add(uri);
//
//                //  imageUriArray.add(Uri.parse(fileLocation));
//                imageUriArray.add(uri);
//                sendIntent.setAction(Intent.ACTION_SEND);
//
//                if(lFileInput.getText().toString().isEmpty())
//                {
//                    sendIntent.setType("text/plain");
//                    sendIntent.putExtra(Intent.EXTRA_TEXT, final_message);
//                }
//                else {
//                    //  sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                    sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,imageUriArray);
//                    sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    sendIntent.setType("image/*");
//                }
//                final_message = final_message.replace("<name>", jName);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, final_message);
//
//
//                sendIntent.putExtra("jid", jNumber + "@s.whatsapp.net"); //phone number without "+" prefix
//                sendIntent.setPackage("com.whatsapp");
//                startActivityForResult(sendIntent,1);
//                TextView name = (TextView)bubbleView.getChildAt(1);
//                name.setText(jName);
//                i++;
//                int cou = contact_count - i;
//                String Status = i+ " Messages sent " + cou+" Contacts Remaining";
//                txtStatus.setText(Status);
//            }
//            catch (JSONException e){
//                e.printStackTrace();
//            }}
//
//
//
//    }

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
