package com.example.android.baskettime;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fabio on 17/11/2015.
 */
public class Popup0Activity extends AppCompatActivity {

    RecyclerView recyclerViewPopup;
    LinearLayoutManager linearLayoutManagerPopup;
    List<Quarters> quarterList;

    public TextView teamHome;
    public TextView scoreHome;
    public TextView teamVisitor;
    public TextView scoreVisitor;

    String hometeam;
    String nquarter;
    String visitorteam;
    String homescore;
    String visitorscore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup0);

        SharedPreferences sharedPreferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, MODE_PRIVATE);

        final String sessionId = sharedPreferences.getString(ConfigActivity.SESSION_ID, "");
        final String selectedMatch = this.getIntent().getExtras().get("matchID").toString();
        final String function = "getGames";

        quarterList = new ArrayList<>();

        recyclerViewPopup = (RecyclerView) findViewById(R.id.recyclerViewPopup);
        recyclerViewPopup.setHasFixedSize(true);

        linearLayoutManagerPopup = new LinearLayoutManager(Popup0Activity.this);
        recyclerViewPopup.setLayoutManager(linearLayoutManagerPopup);

        final QuarterAdapter quarterAdapter = new QuarterAdapter(quarterList);
        recyclerViewPopup.setAdapter(quarterAdapter);

        teamHome = (TextView) findViewById(R.id.teamHomePopup);
        scoreHome = (TextView) findViewById(R.id.scoreHomePopup);
        teamVisitor = (TextView) findViewById(R.id.teamVisitorPopup);
        scoreVisitor = (TextView) findViewById(R.id.scoreVisitorPopup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.8));

        if (this.getIntent().getExtras() != null) {

            StringRequest quarterRequest = new StringRequest(Request.Method.POST, ConfigActivity.ENTRY, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {

                        JSONObject jsonObject;
                        JSONArray matchDetails;

                        jsonObject = new JSONObject(response);
                        matchDetails = jsonObject.getJSONArray(ConfigActivity.TAG_SINGLE_MATCH);

                        for (int i = 0; i < matchDetails.length(); i++) {

                            JSONObject quarterDetails = matchDetails.getJSONObject(i);
                            Quarters quarters = new Quarters();

                            hometeam = quarterDetails.getString(ConfigActivity.TAG_CURRENT_TEAM_HOME);
                            visitorteam = quarterDetails.getString(ConfigActivity.TAG_CURRENT_TEAM_VISITOR);
                            homescore = quarterDetails.getString("final_score_home");
                            visitorscore = quarterDetails.getString("final_score_visitor");

                            quarters.nquarter = quarterDetails.getInt("quarter");
                            quarters.scoreHome = quarterDetails.getInt("singleScoreHome");
                            quarters.scoreVisitor = quarterDetails.getInt("singleScoreVisitor");

                            teamHome.setText(hometeam);
                            teamVisitor.setText(visitorteam);
                            scoreHome.setText(homescore + " - ");
                            scoreVisitor.setText(visitorscore);

                            quarterList.add(quarters);
                        }

                        quarterAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    final String function = "getGames";

                    //Aggiungo i parametri alla richiesta
                    params.put(ConfigActivity.KEY_ID_SESSION, sessionId);
                    params.put(ConfigActivity.KEY_MATCH_ID, selectedMatch);
                    params.put("f", function);

                    //Ritorno i paramentri
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(quarterRequest);
        }
    }
}

