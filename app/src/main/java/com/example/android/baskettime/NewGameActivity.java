package com.example.android.baskettime;

import android.app.ActivityOptions;
import android.app.DownloadManager;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Fabio on 11/01/2016.
 */
public class NewGameActivity extends AppCompatActivity implements View.OnClickListener, Spinner.OnItemSelectedListener {

    Toolbar mtoolbar;
    Button insertButton;

    private Spinner spinnerHome;
    private Spinner spinnerVisitor;
    private Spinner spinnerChampionship;

    public Calendar calendar = Calendar.getInstance();

    private ArrayList<String> teamHome;
    private ArrayList<String> teamVisitor;
    private ArrayList<String> championship;

    final String function = "insertGames";

    int id_game;

    private JSONObject j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgame);
        setTitle("Nuova Partita");

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        teamHome = new ArrayList<String>();
        teamVisitor = new ArrayList<String>();
        championship = new ArrayList<String>();

        insertButton = (Button) findViewById(R.id.insert_button);

        spinnerHome = (Spinner) findViewById(R.id.spinner_team_home);
        spinnerVisitor = (Spinner) findViewById(R.id.spinner_team_visitor);
        spinnerChampionship = (Spinner) findViewById(R.id.champ_spinner);

        insertButton.setOnClickListener(this);
        spinnerHome.setOnItemSelectedListener(this);
        spinnerVisitor.setOnItemSelectedListener(this);
        spinnerChampionship.setOnItemSelectedListener(this);


        getData();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();
                overridePendingTransition(R.anim.push_out_right, R.anim.pull_in_left);

        }
        return true;
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfigActivity.ENTRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            j = new JSONObject(response);
                            getTeams(j);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                SharedPreferences sharedPreferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, MODE_PRIVATE);
                final String function = "getTeams";
                final String sessionId = sharedPreferences.getString(ConfigActivity.SESSION_ID, "");

                //Aggiungo i parametri alla richiesta
                params.put(ConfigActivity.KEY_ID_SESSION, sessionId);
                params.put("f", function);

                //Ritorno i paramentri
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    private void getTeams(JSONObject response) {

        try {

        JSONArray j = response.getJSONObject(ConfigActivity.JSON_ARRAY).getJSONArray(ConfigActivity.TAG_TEAM);

        for (int i = 0; i < j.length(); i++) {


                JSONObject json = j.getJSONObject(i);

                teamHome.add(json.getString(ConfigActivity.TAG_HOME_TEAM));
                teamVisitor.add(json.getString(ConfigActivity.TAG_VISITOR_TEAM));

            }

        j = response.getJSONObject(ConfigActivity.JSON_ARRAY).getJSONArray(ConfigActivity.TAG_CHAMP);

        for (int i = 0; i < j.length(); i++) {


                JSONObject json = j.getJSONObject(i);
                championship.add(json.getString(ConfigActivity.TAG_SPECIFIC_CHAMP));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        spinnerChampionship.setAdapter(new ArrayAdapter<String>(NewGameActivity.this, android.R.layout.simple_spinner_dropdown_item, championship));
        spinnerVisitor.setAdapter(new ArrayAdapter<String>(NewGameActivity.this, android.R.layout.simple_spinner_dropdown_item, teamVisitor));
        spinnerHome.setAdapter(new ArrayAdapter<String>(NewGameActivity.this, android.R.layout.simple_spinner_dropdown_item, teamHome));
    }


    private int getTeamsID(int position) {

        int id = 0;

        try {

            JSONArray json = j.getJSONObject(ConfigActivity.JSON_ARRAY).getJSONArray(ConfigActivity.TAG_TEAM);
            JSONObject jsonObject = json.getJSONObject(position);
            id = jsonObject.getInt(ConfigActivity.TAG_ID);

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return id;
    }

    private int getChampID(int position){

        int id = 0;

        try {

            JSONArray json = j.getJSONObject(ConfigActivity.JSON_ARRAY).getJSONArray(ConfigActivity.TAG_CHAMP);
            JSONObject jsonObject = json.getJSONObject(position);

            id = jsonObject.getInt(ConfigActivity.TAG_ID);

        } catch (JSONException e){

            e.printStackTrace();
        }

        return id;
    }

    private void insertTeams() {

        SharedPreferences sharedPreferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, MODE_PRIVATE);
        final String idhome = String.valueOf(getTeamsID((int) spinnerHome.getSelectedItemId()));
        final String idvisitor = String.valueOf(getTeamsID((int) spinnerVisitor.getSelectedItemId()));
        final String idChamp = String.valueOf(getChampID((int) spinnerChampionship.getSelectedItemId()));
        final String sessionId = sharedPreferences.getString(ConfigActivity.KEY_ID_SESSION, "");
        Log.i("idhome", "getstring" + idhome);
        Log.i("idVisitor", "getstring" + idvisitor);
        Log.d("Championship", "Get ID" + idChamp);

        final int day = calendar.get(Calendar.DATE);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        String date = "";
        String actualMonth = "";
        String actualMinute = "";
        String time = "";

        if (day < 10 ){

            date = "0" + String.valueOf(day);

        }else{

            date = String.valueOf(day);
        }
        if (month + 1 < 10){

            actualMonth = "0" + String.valueOf(month + 1);

        }else {

            actualMonth = String.valueOf(month + 1);
        }

        if (hour <10){

            time = "0" + String.valueOf(hour);

        } else {

            time = String.valueOf(hour);
        }

        if (minute <10){

            actualMinute = "0" + String.valueOf(minute);

        } else {

            actualMinute = String.valueOf(minute);
        }

        final String Time = time + ":" + actualMinute;
        final String Date = date + "/" + actualMonth + "/" + String.valueOf(year);

        class insertTeams extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Intent livegame = new Intent(NewGameActivity.this, LiveActivity.class);
                startActivity(livegame);
                finish();
            }

            @Override
            protected String doInBackground(Void... params) {

                HashMap<String, String> param = new HashMap<>();
                param.put(ConfigActivity.KEY_HOME_TEAM, idhome);
                param.put(ConfigActivity.KEY_VISITOR_TEAM, idvisitor);
                param.put(ConfigActivity.TAG_GAMES_CHAMP, idChamp);
                param.put(ConfigActivity.KEY_ID_SESSION, sessionId);
                param.put("f", function);
                param.put("day", Date);
                param.put("time", Time);

                RequestHandler requestHandler = new RequestHandler();
                String res = requestHandler.sendPostRequest(ConfigActivity.ENTRY, param);

                JSONObject j = null;
                JSONObject gameDetails = null;

                try {

                    j = new JSONObject(res);
                    gameDetails = j.getJSONObject(ConfigActivity.TAG_CURRENT_GAME);
                    id_game = gameDetails.getInt(ConfigActivity.TAG_CURRENT_ID);

                    SharedPreferences sharedPreferences = NewGameActivity.this.getSharedPreferences(ConfigActivity.TAG_ID_GAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putInt(ConfigActivity.ID_GAME, id_game);
                    Log.d("NewGameActivity", "id_game" + id_game);

                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return res;

            }

        }

        insertTeams it = new insertTeams();
        it.execute();

    }

    @Override
    public void onClick(View v) {
        if (v == insertButton) {
            insertTeams();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
