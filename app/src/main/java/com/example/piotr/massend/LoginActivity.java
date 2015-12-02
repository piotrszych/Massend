package com.example.piotr.massend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.piotr.massend.utils.DatabaseDummy;
import com.example.piotr.massend.utils.Consts;

public class LoginActivity extends Activity {

    //controls
    EditText et_login;
    EditText et_password;
    Button button_login;

    DatabaseDummy db;

    private final String SAVEDINSTANCESTATE_LOGIN = "s_login";
    private final String SAVEDINSTANCESTATE_PASSWORD = "s_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //get db
        db = DatabaseDummy.getInstance(getApplicationContext());

        //initialize controls
        initializeControls();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if(!et_login.getText().toString().equals("")) {
            savedInstanceState.putString(SAVEDINSTANCESTATE_LOGIN, et_login.getText().toString());
        }
        if(!et_password.getText().toString().equals("")) {
            savedInstanceState.putString(SAVEDINSTANCESTATE_PASSWORD, et_password.getText().toString());
        }
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(!savedInstanceState.getString(SAVEDINSTANCESTATE_LOGIN, "").equals("")) {
            et_login.setText(savedInstanceState.getString(SAVEDINSTANCESTATE_LOGIN));
        }
        if(!savedInstanceState.getString(SAVEDINSTANCESTATE_PASSWORD, "").equals("")) {
            et_password.setText(savedInstanceState.getString(SAVEDINSTANCESTATE_PASSWORD));
        }
    }

    private void initializeControls() {
        et_login = (EditText) findViewById(R.id.login_et_login);
        et_password = (EditText) findViewById(R.id.login_et_password);
        button_login = (Button) findViewById(R.id.login_button);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkResult = checkLoginPassword();
                if(checkResult == 0) {
                    //go to next activity
                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(checkResult == 1){
                    Toast.makeText(LoginActivity.this, getString(R.string.login_activity_toast_wrong_password), Toast.LENGTH_SHORT).show();
                }
                else if(checkResult == 2){
                    Toast.makeText(LoginActivity.this, getString(R.string.login_activity_toast_wrong_credentials), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int checkLoginPassword() {
        String login = et_login.getText().toString();
        String password = et_password.getText().toString();

        //check internal
        if(login.length() < Consts.MIN_CHARS_LOGIN) {
            String rawMessage = getResources().getString(R.string.login_activity_toast_short_login);
            String message = String.format(rawMessage, Consts.MIN_CHARS_LOGIN);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        else if(password.length() < Consts.MIN_CHARS_PASSWORD) {
            String rawMessage = getResources().getString(R.string.login_activity_toast_short_password);
            String message = String.format(rawMessage, Consts.MIN_CHARS_PASSWORD);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        else {
        //check external
            return db.checkLoginPassword(login, password);
        }
        return 3;
    }
}
