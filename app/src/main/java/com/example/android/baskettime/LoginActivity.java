package com.example.android.baskettime;

/**
 * Created by fabio on 11/11/2015.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.TestCase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";
    private static final String LOGIN_URL = "http://95.85.23.84/login.php";

    private EditText eTEmail;
    private EditText eTPassword;

    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        eTEmail = (EditText) findViewById(R.id.email_text);
        eTPassword = (EditText) findViewById(R.id.pass_text);

        loginButton = (Button) findViewById(R.id.login_butt);
        loginButton.setOnClickListener(this);


        TextView signup = (TextView) findViewById(R.id.crea_ora);
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent signup = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(signup);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == loginButton){
            login();
        }
    }

    private void login(){
        String email = eTEmail.getText().toString().trim();
        String password = eTPassword.getText().toString().trim();
        userLogin(email, password);
    }

    private void userLogin(final String email, final String password){
        class LoginUser extends AsyncTask <String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this, "Attendere, prego...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();

                if (s.trim().equalsIgnoreCase("success")){
                    Intent intent = new Intent(LoginActivity.this, LiveActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "Login non effettuato", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params){
                HashMap<String, String> data = new HashMap<>();
                data.put("email", params[0]);
                data.put("password", params[1]);

                RegisterUserActivity rua = new RegisterUserActivity();
                String result = rua.sendPostRequest(LOGIN_URL, data);

                return result;
            }
        }
        LoginUser lu = new LoginUser();
        lu.execute(email, password);
    }
}