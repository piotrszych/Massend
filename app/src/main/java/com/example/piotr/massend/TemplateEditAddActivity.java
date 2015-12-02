package com.example.piotr.massend;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.piotr.massend.utils.Consts;

public class TemplateEditAddActivity extends Activity{

    //TODO save instance state?
    //TODO onPause/onResume handling

    private final String TAG = "TemplateEditAddActivity";

    //controls
    EditText et_main_input;
    TextView tv_title_holder;
    Button button_save_template;
    Button button_edit_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_edit_add);

        //inicjalizacja kontrolek
        initializeControls();

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

    private void initializeControls() {
        et_main_input = (EditText) findViewById(R.id.tea_main_input);
        tv_title_holder = (TextView) findViewById(R.id.tea_tv_editing_title);
        button_save_template = (Button) findViewById(R.id.tea_save_template);
        button_edit_title = (Button) findViewById(R.id.tea_change_title);
    }
}
