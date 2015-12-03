package com.example.piotr.massend;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.piotr.massend.utils.Consts;
import com.example.piotr.massend.utils.DatabaseDummy;
import com.example.piotr.massend.utils.Template;

import java.util.zip.Inflater;

public class TemplateEditAddActivity extends Activity{

    //TODO save instance state?
    //TODO onPause/onResume handling

    private final String TAG = "TemplateEditAddActivity";

    //database
    DatabaseDummy db;

    //controls
    private EditText et_main_input;
    private TextView tv_title_holder;
    private Button button_save_template;
    private Button button_edit_title;

    //if edited
    private boolean b_editedMain = false;
    private boolean b_editedTitle = false;

    //strings
    private String s_newName;
    private String s_newContent;
    private String s_oldName;

    //data to return
    private int template_position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_edit_add);

        db = DatabaseDummy.getInstance(this);

        //inicjalizacja kontrolek
        Bundle extras = getIntent().getExtras();
        initializeControls(extras);
        template_position = extras.getInt(Consts.DATA_TEMPLATE_POSITION, -1);

        int request_code = this.getIntent().getIntExtra(Consts.REQUEST_CODE, -1);
        switch (request_code) {
            case Consts.REQUEST_CODE_ADD_TEMPLATE:      //new template
                //TODO handle new template
                break;
            case Consts.REQUEST_CODE_EDIT_TEMPLATE:     //edit existing template
                //TODO handle existing template
                break;
            default:                                    //something else happens...
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        if(checkIfSave()) {
            //display prompt on save
            AlertDialog.Builder builder = new AlertDialog.Builder(TemplateEditAddActivity.this);
            builder.setTitle(R.string.dialog_warning);
            builder.setMessage(R.string.template_edit_add_dialog_backbutton_message);
            builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        saveResults(s_oldName, s_newName, s_newContent);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        finish();
                    }
                }
            });
            builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.show();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void initializeControls(Bundle bundle) {
        et_main_input = (EditText) findViewById(R.id.tea_main_input);
        tv_title_holder = (TextView) findViewById(R.id.tea_tv_editing_title);
        button_save_template = (Button) findViewById(R.id.tea_save_template);
        button_edit_title = (Button) findViewById(R.id.tea_change_title);

        //initialize if edit
        if(bundle.containsKey(Consts.DATA_TEMPLATE_CONTENT) && bundle.containsKey(Consts.DATA_TEMPLATE_NAME)) {
            s_oldName = bundle.getString(Consts.DATA_TEMPLATE_NAME, null);
            s_newName = s_oldName;
            s_newContent = bundle.getString(Consts.DATA_TEMPLATE_CONTENT, null);

            tv_title_holder.setText(getString(R.string.template_edit_add_edited_template_message, s_oldName));
            et_main_input.setText(s_newContent);
        }
        else {  //initialize if not edit
            s_oldName = null;
            tv_title_holder.setText(R.string.template_edit_add_new_template_message);
        }

        //set listeners
        button_save_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfSave()) {
                    saveResults(s_oldName, s_newName, s_newContent);
                }
                else {
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        });
        button_edit_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTitleDialog editTitleDialog = new EditTitleDialog(TemplateEditAddActivity.this);
                editTitleDialog.show();
            }
        });

        et_main_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nope
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //yup
                b_editedMain = true;
                s_newContent = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //nope
            }
        });
    }

    private boolean checkIfSave() {
        return (b_editedMain || b_editedTitle)
                && s_newContent != null && !s_newContent.equals("")
                && s_newName != null && !s_newName.equals("");
    }

    private void saveResults(@Nullable String oldName, String newName, String newContent) {

        Intent data = new Intent();

        if(oldName == null) {   //new template
            db.insertIntoTemplates(newName, newContent);
        }
        else {  //edited template
            //1 if we're good, 0 if not
            int result = (int) db.editTemplateByName(oldName, newName, newContent);
            data.putExtra(Consts.DATA_TEMPLATE_POSITION, template_position);
        }
        data.putExtra(Consts.DATA_TEMPLATE_CONTENT, newContent);
        data.putExtra(Consts.DATA_TEMPLATE_NAME, newName);
        setResult(RESULT_OK, data);
    }

    private class EditTitleDialog extends AlertDialog {

        public EditTitleDialog(TemplateEditAddActivity context) {
            super(context);

            LayoutInflater inflater = context.getLayoutInflater();
            View rootView = inflater.inflate(R.layout.dialog_tea_edit_title, null);
            setView(rootView);
            final EditText et = (EditText) rootView.findViewById(R.id.et_tea_change_title);
            if(s_oldName != null )
                setTitle(s_oldName);
            else
                setTitle(R.string.template_edit_add_dialog_edit_title_new_template_title);

            setButton(BUTTON_POSITIVE, getString(R.string.dialog_save), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    s_newName = et.getText().toString();
                    tv_title_holder.setText(getString(R.string.template_edit_add_edited_template_message, s_newName));
                    b_editedTitle = true;
                    dismiss();
                }
            });
            setButton(BUTTON_NEGATIVE, getString(R.string.dialog_cancel), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
        }

    }
}
