package com.example.android.baskettime;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fabio on 11/11/2015.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private EditText etName;
    private EditText etSurname;
    private EditText etEmail;
    private EditText etPassword;

    private Button buttonRegister;

    private static final String REGISTER_URL = "http://95.85.23.84/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        String urlSuffix = "?name="+name+"&surname="+surname+"&email="+email+"&password="+password;
        class RegisterUser extends AsyncTask<String, Void, String>{

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterActivity.this, "Attendere, prego...",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                }catch(Exception e){
                    return null;
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }
}