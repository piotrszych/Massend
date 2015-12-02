package com.example.piotr.massend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends Activity {

    private static final String TAG = "MainMenuActivity";

    //controls
    Button button_writeMessage;
    Button button_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //initialize controls
        initializeControls();
    }

    private void initializeControls() {
        button_writeMessage = (Button) findViewById(R.id.main_menu_button_writemessage);
        button_settings = (Button) findViewById(R.id.main_menu_button_settings);

        button_writeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO start activity of writing message
                Intent writeMessageIntent = new Intent(MainMenuActivity.this, WriteMessageActivity.class);
                startActivity(writeMessageIntent);
            }
        });

        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start activity of settings
                Intent settingsIntent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });
    }
}
