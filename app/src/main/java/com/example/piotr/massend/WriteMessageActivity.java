package com.example.piotr.massend;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piotr.massend.utils.Consts;
import com.example.piotr.massend.utils.Contact;

import java.util.ArrayList;

public class WriteMessageActivity extends Activity {

    private static final String TAG = "WriteMessageActivity";

    //TODO onLongClickListeners dla buttonow z wyjasnieniem!

    //controls
    Button button_send;
    Button button_templates;
    Button button_recipients_choose;
    Button button_recipients_show;
    TextView et_mainInput;

    ArrayList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_message);

        //initialize controls
        initializeControls();

        contacts = new ArrayList<>();
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
                if(et_mainInput.getText() == null || et_mainInput.getText().toString().equals("")) {
                    Toast.makeText(WriteMessageActivity.this, R.string.write_message_warning_no_content, Toast.LENGTH_SHORT).show();
                }
                else if(contacts.size() == 0) {
                    Toast.makeText(WriteMessageActivity.this, R.string.write_message_dialog_no_recipients_chosen, Toast.LENGTH_SHORT).show();
                }
                else {
                    //TODO progress dialog to send!
                    Toast.makeText(WriteMessageActivity.this, "Wysłano wiadomości!", Toast.LENGTH_LONG).show();
                    finish();
                }

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
                Intent intent = new Intent(WriteMessageActivity.this, ContactsActivity.class);
                startActivityForResult(intent, Consts.REQUEST_CODE_GET_CONTACTS);
            }
        });

        button_recipients_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WriteMessageActivity.this);
                builder.setTitle(R.string.write_message_dialog_recipients_title);
                if(contacts.size() == 0) {
                    builder.setMessage(R.string.write_message_dialog_no_recipients_chosen);
                }
                else {
                    String[] contactStrings = new String[contacts.size()];
                    for(int i = 0; i < contacts.size(); i++) {
                        contactStrings[i] = contacts.get(i).toString();
                    }
                    builder.setItems(contactStrings, null);
                }
                builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Consts.REQUEST_CODE_GET_TEMPLATE) {
            et_mainInput.setText(data.getExtras().getString(Consts.DATA_TEMPLATE_CONTENT));
        }
        else if(requestCode == Consts.REQUEST_CODE_GET_CONTACTS && resultCode == RESULT_OK) {
            contacts = new ArrayList<>(data.<Contact>getParcelableArrayListExtra(Consts.DATA_CONTACTS));
            Log.d(TAG, contacts.toString());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
