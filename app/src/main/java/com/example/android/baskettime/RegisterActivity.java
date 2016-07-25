package com.example.android.baskettime;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.*;

/**
 * Created by fabio on 11/11/2015.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName;
    private EditText etSurname;
    private EditText etEmail;
    private EditText etPassword;

    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.name_text2);
        etSurname = (EditText) findViewById(R.id.surname_text2);
        etEmail = (EditText) findViewById(R.id.email_text2);
        etPassword = (EditText) findViewById(R.id.pass_text2);

        buttonRegister = (Button) findViewById(R.id.login_butt2);
        buttonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == buttonRegister) {
            registerUser();
        }
    }

    private void registerUser() {

        final String function = "register";
        final String name = etName.getText().toString().trim().toLowerCase();
        final String surname = etSurname.getText().toString().trim().toLowerCase();
        final String email = etEmail.getText().toString().trim().toLowerCase();
        final String password = etPassword.getText().toString().trim().toLowerCase();


        StringRequest register = new StringRequest(Request.Method.POST, ConfigActivity.ENTRY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonObject = null;
                String success = "";

                try {

                    jsonObject = new JSONObject(response);
                    JSONObject register = jsonObject.getJSONObject("register");

                    success = register.getString("result");
                    Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Per gestire eventuali errori
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //Aggiungo i parametri alla richiesta
                params.put("name", name);
                params.put("surname", surname);
                params.put("email", email);
                params.put("password", password);
                params.put("f", function);

                //Ritorno i paramentri
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(register);
    }
}