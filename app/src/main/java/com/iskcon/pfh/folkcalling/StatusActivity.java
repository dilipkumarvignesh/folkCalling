package com.iskcon.pfh.folkcalling;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

public class StatusActivity extends AppCompatActivity {
    private ShareActionProvider mShareActionProvider;
    private String csvFilename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Bundle extras = getIntent().getExtras();
        csvFilename=extras.getString("filename");
        Log.d("info","Csvfilename:"+csvFilename);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);
        setShareIntent(createShareIntent());
        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
    private Intent createShareIntent() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
       // shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_TEXT,
//                "http://stackandroid.com");
//
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        File SD_CARD_PATH = Environment.getExternalStorageDirectory();
        File filename = new File(SD_CARD_PATH,csvFilename);
        Log.d("info","filename:"+filename);
        Uri screenshotUri = Uri.fromFile(filename);
        Log.d("info","fileUri:"+screenshotUri);
        sharingIntent.setType("*/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "Share File"));
        return sharingIntent;
    }
}