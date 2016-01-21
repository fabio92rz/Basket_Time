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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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
    TextView prova;
    TextView prova2;
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
        prova = (TextView) findViewById(R.id.prova);
        prova2 = (TextView) findViewById(R.id.prova1);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        teamHome = new ArrayList<String>();
        teamVisitor = new ArrayList<String>();

        spinnerHome = (Spinner) findViewById(R.id.spinner_team_home);
        spinnerVisitor = (Spinner) findViewById(R.id.spinner_team_visitor);

        spinnerHome.setOnItemSelectedListener(this);
        spinnerVisitor.setOnItemSelectedListener(this);

        insertButton = (Button) findViewById(R.id.insert_button);

        getData();

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String idhome = String.valueOf(spinnerHome.getSelectedItemId());
                //final String idvisitor = String.valueOf(spinnerVisitor.getId());
                Log.i("idhome", "getstring" + idhome);

                class insertHome extends AsyncTask<Void, Void, String> {

                    @Override
                    protected String doInBackground(Void... params) {

                        HashMap<String, String> param = new HashMap<>();
                        param.put(ConfigActivity.KEY_HOME_TEAM, idhome);
                        //param.put(ConfigActivity.KEY_VISITOR_TEAM, idvisitor);

                        RequestHandler requestHandler = new RequestHandler();
                        String res = requestHandler.sendPostRequest(ConfigActivity.INSERT_HOME, param);

                        return res;
                    }
                }

                insertHome ih = new insertHome();
                ih.execute();

            }
        });
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

    @Override
    public void onClick(View v) {
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        /**Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spinner_team_home) {

            int idhome = getID(position);
            final String idHome = "";
            idHome.valueOf(idhome);

            insertButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertHome(idHome);

                }
            });

        } else if (spinner.getId() == R.id.spinner_team_visitor) {

            int idvisitor = getID(position);
            final String idVisitor = "";
            idVisitor.valueOf(idvisitor);

            insertButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertVisitor(idVisitor);

                }
            });

        }**/
    }


    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void insertVisitor(final String idvisitor) {

        class insertVisitor extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {

                HashMap<String, String> param = new HashMap<>();
                param.put(ConfigActivity.KEY_VISITOR_TEAM, idvisitor);

                RequestHandler requestHandler = new RequestHandler();
                String res = requestHandler.sendPostRequest(ConfigActivity.INSERT_HOME, param);

                return res;
            }
        }

        insertVisitor iv = new insertVisitor();
        iv.execute();
    }

}
