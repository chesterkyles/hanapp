package com.hanapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    public static final String LoginObject = "Login";

    private EditText email_ad, password;
    private ImageButton is_login;
    private TextView is_sign_up;
    private String input_id;
    private EditText email_, user_, pass_;
    private EditText confirm_;
    String email_str, user_str, pass_str, confirm_str;
    Boolean is_confirm = null;

    Dialog signup_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_ad = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        is_login = (ImageButton) findViewById(R.id.login);
        is_sign_up = (TextView) findViewById(R.id.sign_up_button);

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if(id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        is_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        signup_dialog = new Dialog(this);
        is_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUp();
            }
        });
    }

    public void showSignUp(){
        ImageButton submit;
        ImageButton cancel;

        signup_dialog.setContentView(R.layout.signup);
        submit = (ImageButton) signup_dialog.findViewById(R.id.submit_id);
        cancel = (ImageButton) signup_dialog.findViewById(R.id.cancel_id);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focus_view = null;
                Boolean is_valid_signup = true;

                email_ = (EditText) signup_dialog.findViewById(R.id.email_signup);
                user_ = (EditText) signup_dialog.findViewById(R.id.user_signup);
                pass_ = (EditText) signup_dialog.findViewById(R.id.password_signup);
                confirm_ = (EditText) signup_dialog.findViewById(R.id.confirm_pass_signup);

                email_str = email_.getText().toString();
                user_str = user_.getText().toString();
                pass_str = pass_.getText().toString();
                confirm_str = confirm_.getText().toString();

                is_confirm = false;
                if(pass_str.equals(confirm_str)){
                    is_confirm = true;
                }

                if(TextUtils.isEmpty(email_str)) {
                    email_.setError(getString(R.string.empty_field));
                    focus_view = email_;
                    is_valid_signup = false;
                } else if(!isEmailAdValid(email_str)) {
                    email_.setError(getString(R.string.invalid_username));
                    focus_view = email_;
                    is_valid_signup = false;
                }

                if(TextUtils.isEmpty(user_str)) {
                    user_.setError(getString(R.string.empty_field));
                    focus_view = user_;
                    is_valid_signup = false;
                }

                if(TextUtils.isEmpty(pass_str)) {
                    pass_.setError(getString(R.string.empty_field));
                    focus_view = pass_;
                    is_valid_signup = false;
                } else if(!isPasswordValid(pass_str)) {
                    pass_.setError(getString(R.string.invalid_password));
                    focus_view = pass_;
                    is_valid_signup = false;
                }
                if(!is_confirm){
                    is_valid_signup = false;
                }

                if(!is_valid_signup){
                    focus_view.requestFocus();
                } else {
                    String path = "/sdcard/CSV_Files/";
                    String fileName = "user.csv";
                    File file = new File (path+fileName);
                    try {
                        FileWriter fileWriter = new FileWriter(file, true);
                        input_id = email_str + "," + pass_str + "," + user_str + "," + "0.00,";
                        fileWriter.write(input_id);
                    } catch (IOException ex){

                    }
                }
                signup_dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup_dialog.dismiss();
            }
        });

        signup_dialog.show();
    }

    private void attemptLogin(){

        View focus_view = null;
        email_ad.setError(null);
        password.setError(null);
        boolean is_valid_login = true;
        String success_login = null;

        String email_ad_str = email_ad.getText().toString();
        String password_str = password.getText().toString();

        if(TextUtils.isEmpty(email_ad_str)) {
            email_ad.setError(getString(R.string.empty_field));
            focus_view = email_ad;
            is_valid_login = false;
        } else if(!isEmailAdValid(email_ad_str)) {
            email_ad.setError(getString(R.string.invalid_username));
            focus_view = email_ad;
            is_valid_login = false;
        }

        if(TextUtils.isEmpty(password_str)) {
            password.setError(getString(R.string.empty_field));
            focus_view = password;
            is_valid_login = false;
        } else if(!isPasswordValid(password_str)) {
            password.setError(getString(R.string.invalid_password));
            focus_view = password;
            is_valid_login = false;
        }

        if(!is_valid_login){
            focus_view.requestFocus();
        } else {

            String path = "/sdcard/CSV_Files/";
            String fileName = "user.csv";
            CsvFileInOut csvFile = new CsvFileInOut(path,fileName);
            success_login = csvFile.read(email_ad_str, password_str);

            if(success_login.equals("success")) {
                Intent home_page_intent = new Intent(LoginActivity.this, HomeActivity.class);
                home_page_intent.putExtra(LoginObject, email_ad_str);
                startActivity(home_page_intent);
                finish();
            } else if(success_login.equals("invalid_username")){
                email_ad.setError("Not a valid username");
                email_ad.requestFocus();
            } else if(success_login.equals("invalid_password")) {
                password.setError("Wrong password");
                password.requestFocus();
            } else {
                // Nothing to do.
            }
        }
    }

    private boolean isEmailAdValid (String email_ad) {
        // add another logic please lang po.
        return email_ad.contains("@");
    }

    private boolean isPasswordValid (String password) {
        return password.length() > 5;
    }

}
