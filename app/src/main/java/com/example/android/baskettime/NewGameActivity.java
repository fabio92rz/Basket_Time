package com.example.android.baskettime;

import android.app.ActivityOptions;
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

    int id_game;

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

        insertButton = (Button) findViewById(R.id.insert_button);
        spinnerHome = (Spinner) findViewById(R.id.spinner_team_home);
        spinnerVisitor = (Spinner) findViewById(R.id.spinner_team_visitor);

        insertButton.setOnClickListener(this);
        spinnerHome.setOnItemSelectedListener(this);
        spinnerVisitor.setOnItemSelectedListener(this);


        getData();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();

        }
        return true;
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(ConfigActivity.GET_TEAMS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            result = j.getJSONArray(ConfigActivity.JSON_ARRAY);
                            getTeams(result);
                        } catch (JSONException e) {
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

    private void getTeams(JSONArray j) {

        for (int i = 0; i < j.length(); i++) {

            try {
                JSONObject json = j.getJSONObject(i);

                teamHome.add(json.getString(ConfigActivity.TAG_HOME_TEAM));
                teamVisitor.add(json.getString(ConfigActivity.TAG_VISITOR_TEAM));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        spinnerVisitor.setAdapter(new ArrayAdapter<String>(NewGameActivity.this, android.R.layout.simple_spinner_dropdown_item, teamVisitor));
        spinnerHome.setAdapter(new ArrayAdapter<String>(NewGameActivity.this, android.R.layout.simple_spinner_dropdown_item, teamHome));
    }


    private int getID(int position) {

        int id = 0;


        try {
            JSONObject json = result.getJSONObject(position);
            id = json.getInt(ConfigActivity.TAG_ID);

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return id;

    }

    private void insertTeams() {

        final String idhome = String.valueOf(getID((int) spinnerHome.getSelectedItemId()));
        final String idvisitor = String.valueOf(getID((int) spinnerVisitor.getSelectedItemId()));
        Log.i("idhome", "getstring" + idhome);
        Log.i("idVisitor", "getstring" + idvisitor);


        class insertTeams extends AsyncTask<Void, Void, String> {


            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Intent livegame = new Intent(NewGameActivity.this, LiveActivity.class);
                startActivity(livegame);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

            }

            @Override
            protected String doInBackground(Void... params) {

                HashMap<String, String> param = new HashMap<>();
                param.put(ConfigActivity.KEY_HOME_TEAM, idhome);
                param.put(ConfigActivity.KEY_VISITOR_TEAM, idvisitor);

                RequestHandler requestHandler = new RequestHandler();
                String res = requestHandler.sendPostRequest(ConfigActivity.INSERT_GAMES, param);

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
