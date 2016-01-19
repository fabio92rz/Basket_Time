package com.example.android.baskettime;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Fabio on 11/01/2016.
 */
public class NewGameActivity extends AppCompatActivity implements View.OnClickListener, Spinner.OnItemSelectedListener {

    Toolbar mtoolbar;
    Button insertButton;
    private Spinner spinnerHome;
    private Spinner spinnerVisitor;

    private ArrayList<String> teamHome;
    private ArrayList<String> teamVisitor;

    private JSONArray result;

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

        spinnerHome = (Spinner) findViewById(R.id.spinner_team_home);
        spinnerVisitor = (Spinner)findViewById(R.id.spinner_team_visitor);

        spinnerHome.setOnItemSelectedListener(this);
        spinnerVisitor.setOnItemSelectedListener(this);

        insertButton = (Button) findViewById(R.id.insert_button);

        getData();
    }

    private void getData(){
        StringRequest stringRequest = new StringRequest(ConfigActivity.GET_TEAMS_URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject j = null;
                try {
                    j = new JSONObject(response);

                    result = j.getJSONArray(ConfigActivity.JSON_ARRAY);
                    getTeams(result);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    private void getTeams(JSONArray j){

        for (int i = 0; i<j.length(); i++){

            try {
                JSONObject json = j.getJSONObject(i);

                teamHome.add(json.getString(ConfigActivity.TAG_HOME_TEAM));
                teamVisitor.add(json.getString(ConfigActivity.TAG_VISITOR_TEAM));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        spinnerVisitor.setAdapter(new ArrayAdapter<String>(NewGameActivity.this, android.R.layout.simple_spinner_dropdown_item, teamVisitor));
        spinnerHome.setAdapter(new ArrayAdapter<String>(NewGameActivity.this, android.R.layout.simple_spinner_dropdown_item, teamHome));
    }

    private String getHomeTeam(int position){
        String teamHome = "";
        try {
            JSONObject json = result.getJSONObject(position);
            teamHome = json.getString(ConfigActivity.TAG_HOME_TEAM);

        } catch (JSONException e){
            e.printStackTrace();
        }

        return teamHome;
    }

    private String getVisitorTeam(int position){
        String teamVisitor = "";
        try {
            JSONObject json = result.getJSONObject(position);
            teamVisitor = json.getString(ConfigActivity.TAG_VISITOR_TEAM);

        } catch (JSONException e){
            e.printStackTrace();
        }

        return teamVisitor;
    }


    @Override
    public void onClick(View v) {
        if (v == insertButton) {
            addGame();
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spinner_team_home) {

            String teamHome = parent.getItemAtPosition(position).toString();
        }



        else if (spinner.getId() == R.id.spinner_team_visitor){

            String teamVisitor = getVisitorTeam(position);

        }
    }



    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void addGame() {

        final String teamHome = spinnerHome.getSelectedItem().toString();
        final String teamVisitor = spinnerVisitor.getSelectedItem().toString();
        final String homeResult = "0";
        final String visitorResult = "0";


        Log.d("Valore teamHome", "teamHome=" + teamHome);

        class addTeamHome extends AsyncTask<Void, Void, String>{

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(NewGameActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(ConfigActivity.KEY_HOME_TEAM, teamHome);
                params.put(ConfigActivity.KEY_HOME_VISITOR, teamVisitor);
                params.put(ConfigActivity.KEY_SCORE_HOME, homeResult);
                params.put(ConfigActivity.KEY_HOME_VISITOR, visitorResult);

                RequestHandler requestHandler = new RequestHandler();
                String res = requestHandler.sendPostRequest(ConfigActivity.INSERT_GAME, params);
                return res;
            }
        }

        addTeamHome at = new addTeamHome();
        at.execute();
    }

}
