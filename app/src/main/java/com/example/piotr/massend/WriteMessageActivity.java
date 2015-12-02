package com.example.piotr.massend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.piotr.massend.utils.Consts;

public class WriteMessageActivity extends Activity {

    private static final String TAG = "WriteMessageActivity";

    //TODO onLongClickListeners dla buttonow z wyjasnieniem!

    //controls
    Button button_send;
    Button button_templates;
    Button button_recipients_choose;
    Button button_recipients_show;
    TextView et_mainInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_message);

        //initialize controls
        initializeControls();
    }

    private void initializeControls() {

        et_mainInput = (EditText) findViewById(R.id.write_message_main_input);
        button_send = (Button) findViewById(R.id.write_message_button_send);
        button_templates = (Button) findViewById(R.id.write_message_button_templates);
        button_recipients_choose = (Button) findViewById(R.id.write_message_button_choose_recipients);
        button_recipients_show = (Button) findViewById(R.id.write_message_button_show_recipients);

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO check constraints and send
            }
        });

        button_templates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WriteMessageActivity.this, TemplatesActivity.class);
                startActivityForResult(intent, Consts.REQUEST_CODE_GET_TEMPLATE);
            }
        });

        button_recipients_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO go to recipients
            }
        });

        button_recipients_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO show dialog with chosen recipients
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Consts.REQUEST_CODE_GET_TEMPLATE) {
            et_mainInput.setText(data.getExtras().getString(Consts.DATA_TEMPLATE_CONTENT));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
