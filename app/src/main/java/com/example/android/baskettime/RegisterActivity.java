package com.example.android.baskettime;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by fabio on 11/11/2015.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName;
    private EditText etSurname;
    private EditText etEmail;
    private EditText etPassword;

    private Button buttonRegister;

    private static final String REGISTER_URL = "http://95.85.23.84/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.name_text2);
        etSurname = (EditText) findViewById(R.id.surname_text2);
        etEmail = (EditText) findViewById(R.id.email_text2);
        etPassword = (EditText) findViewById(R.id.pass_text2);

        buttonRegister = (Button) findViewById(R.id.login_butt2);
    }

    @Override
    public void onClick(View v) {

        if (v == buttonRegister) {
            registerUser();
        }
    }

    private void registerUser() {

        String name = etName.getText().toString().trim().toLowerCase();
        String surname = etSurname.getText().toString().trim().toLowerCase();
        String email = etEmail.getText().toString().trim().toLowerCase();
        String password = etPassword.getText().toString().trim().toLowerCase();

        register(name, surname, email, password);
    }

    private void register(String name, String surname, String email, String password) {
        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;
            RegisterUserActivity ruc = new RegisterUserActivity();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterActivity.this, "Attendere, prego...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("name", params[0]);
                data.put("surname", params[1]);
                data.put("email", params[2]);
                data.put("password", params[3]);

                String result = ruc.sendPostRequest(REGISTER_URL, data);

                return result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name, surname, email, password);
    }
}