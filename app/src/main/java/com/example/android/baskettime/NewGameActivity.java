package com.example.android.baskettime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Fabio on 11/01/2016.
 */
public class NewGameActivity extends AppCompatActivity implements View.OnClickListener, Spinner.OnItemSelectedListener {

    Toolbar mtoolbar;
    Button insertButton;
    private Spinner spinnerHome;
    private Spinner spinnerVisitor;

    private ArrayList<String> teams;

    private JSONArray result;
    private NewGameActivity onItemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgame);
        setTitle("  Nuova Partita");

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        
        teams = new ArrayList<String>();
        
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

                teams.add(json.getString(ConfigActivity.TAG_TEAM));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        spinnerHome.setAdapter(new ArrayAdapter<String>(NewGameActivity.this, android.R.layout.simple_spinner_dropdown_item, teams));
    }

    @Override
    public void onClick(View v) {
        if (v == insertButton){

        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }
}
