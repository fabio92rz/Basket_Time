package com.example.android.baskettime;

/**
 * Created by fabio on 11/11/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Definisco le Views ed il pulsante
    private EditText eTEmail;
    private EditText eTPassword;
    private Button loginButton;

    //Definisco la variabile Booleana e la setto false
    private boolean loggedIn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //Inizializzo le Views
        eTEmail = (EditText) findViewById(R.id.login_email_text);
        eTPassword = (EditText) findViewById(R.id.login_pass_text);

        //Inizializzo il bottone e setto il ClickListener
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
    protected void onResume() {
        super.onResume();
        //In onResume catturo i valori dalle SharedPreference
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Catturo il valore booleano dalle SharedPreference
        loggedIn = sharedPreferences.getBoolean(ConfigActivity.LOGGEDIN_SHARED_PREF, false);

        //Se diventa vero
        if (loggedIn) {
            //Lo faccio passare dalla LoginActivity alla LiveActivity
            Intent live = new Intent(LoginActivity.this, HistoryActivity.class);
            startActivity(live);
        }
    }

    private void login() {
        //Prendo i valori dalle EditText
        final String email = eTEmail.getText().toString().trim();
        final String password = eTPassword.getText().toString().trim();


        //Inizializzo il popup di caricamento
        final ProgressDialog loading;
        loading = ProgressDialog.show(LoginActivity.this, "Attendere prego...", null, true, true);

        //Creo la string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfigActivity.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Se riceviamo success dal server
                if (response.equalsIgnoreCase(ConfigActivity.LOGIN_SUCCESS)) {

                    //Creo una Shared Preference
                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Creo un editor per conservare i valori di SharedPreference
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    //Aggiungo i valori all'editor
                    editor.putBoolean(ConfigActivity.LOGGEDIN_SHARED_PREF, true);
                    editor.putString(ConfigActivity.EMAIL_SHARED_PREF, email);

                    //Salvo i valori
                    editor.commit();

                    //Tolgo il popup di caricamento
                    loading.dismiss();

                    //Lancio Live Activity
                    Intent live = new Intent(LoginActivity.this, HistoryActivity.class);
                    startActivity(live);
                } else {
                    //Se la risposta del server non è Success stampa un messaggi di errore su toast
                    Toast.makeText(LoginActivity.this, "Username o Password errate", Toast.LENGTH_LONG).show();
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
                params.put(ConfigActivity.KEY_EMAIL, email);
                params.put(ConfigActivity.KEY_PASSWORD, password);

                //Ritorno i paramentri
                return params;
            }
        };

        //Aggiungo le string request alla coda
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            login();
        }
    }

    /**public List readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    public List readMessagesArray(JsonReader reader) throws IOException {

        List messages = new ArrayList();
        reader.beginArray();

        while (reader.hasNext()) {

            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public Message readMessage(JsonReader reader) throws IOException {

        long id = -1;
        String status = null;
        Data data = null;

        reader.beginObject();
        while (reader.hasNext()) {

            String jsonkey = reader.nextName();
            if (jsonkey.equals("status")) {
                status = reader.nextString();

            } else if (jsonkey.equals("data")) {

                data = readData(reader);

            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Message();
    }

    public Data readData(JsonReader reader) throws IOException {
        String username = null;
        String userSurname = null;
        String userEmail = null;
        String userPassword = null;

        reader.beginObject();
        while (reader.hasNext()) {

            String jsonKey = reader.nextName();
            if (jsonKey.equals("name")) {
                username = reader.nextString();

            } else if (jsonKey.equals("surname")) {
                userSurname = reader.nextString();

            } else if (jsonKey.equals("email")) {
                userEmail = reader.nextString();

            } else if (jsonKey.equals("password")) {
                userPassword = reader.nextString();
            } else {

                reader.skipValue();
            }
        }
        reader.endObject();
        return new Data(username, userSurname, userEmail, userPassword);
    }**/
}