//package com.iskcon.pfh.folkcalling;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.TextView;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
///**
// * Created by i308830 on 12/17/17.
// */
//
//public class WhatsAppMessages extends AppCompatActivity {
//
//    public void sendWhatsapp()
//    {
//        if (i<=contact_count && Callenabled == 1)
//        {
//            try{
//                ArrayList<Uri> imageUriArray = new ArrayList<Uri>();
//                String Message = txtMessage.getText().toString();
//                JSONObject objects = jA.getJSONObject(i);
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
////                imageUriArray.add(Uri.parse(fileLocation));
////                imageUriArray.add(uri);
//                sendIntent.setAction(Intent.ACTION_SEND);
//
//                if(lFileInput.getText().toString().isEmpty()&&imageUriArray.isEmpty())
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
//              //  TextView name = (TextView)bubbleView.getChildAt(1);
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
//
//}
