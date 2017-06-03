package com.iskcon.pfh.folkcalling;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        finish();
    }
}
