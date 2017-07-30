package com.iskcon.pfh.folkcalling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayMessageActivity extends Activity {
    Button Update;
    TextView Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Update = (Button) findViewById(R.id.btnUpdate);
        Name = (TextView)findViewById(R.id.name);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                            String toSpeak = ed1.getText().toString();
//                            Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
//                            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                update();
            }
        });
       String s = getIntent().getStringExtra("Name");
        Name.setText(s);
    }

    public void update()
    {
        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
        Intent resultIntent = new Intent();
        EditText upd = (EditText)findViewById(R.id.UpdatedText);
        String updateText = upd.getText().toString();
    // TODO Add extras or a data URI to this intent as appropriate.
        resultIntent.putExtra("Status", updateText);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }
}
