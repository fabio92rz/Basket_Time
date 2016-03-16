package com.example.android.baskettime;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by fabio on 17/11/2015.
 */
public class Popup0Activity extends AppCompatActivity {

    public TextView teamHome;
    public TextView scoreHome;
    public TextView teamVisitor;
    public TextView scoreVisitor;

    String hometeam;
    String visitorteam;
    String homescore;
    String visitorscore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup0);

        teamHome = (TextView) findViewById(R.id.teamHomePopup);
        scoreHome = (TextView) findViewById(R.id.scoreHomePopup);
        teamVisitor = (TextView) findViewById(R.id.teamVisitorPopup);
        scoreVisitor = (TextView) findViewById(R.id.scoreVisitorPopup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .6));

        if (this.getIntent().getExtras() != null) {

            getMatch();
        }
    }

    private void getMatch() {

        final String selectedMatch = this.getIntent().getExtras().get("matchID").toString();
        Log.d("Prova selected match", "match numero =" + selectedMatch);

        class getMatch extends AsyncTask<Void, String, String> {

            @Override
            protected String doInBackground(Void... params) {
                {

                    HashMap<String, String> param = new HashMap<>();
                    param.put(ConfigActivity.KEY_MATCH_ID, selectedMatch);

                    RequestHandler requestHandler = new RequestHandler();
                    String res = requestHandler.sendPostRequest(ConfigActivity.GET_GAME, param);

                    try {

                        JSONObject jsonObject;
                        JSONObject matchDetails;

                        jsonObject = new JSONObject(res);
                        matchDetails = jsonObject.getJSONObject(ConfigActivity.TAG_SINGLE_MATCH);

                        hometeam = matchDetails.getString(ConfigActivity.TAG_CURRENT_TEAM_HOME);
                        visitorteam = matchDetails.getString(ConfigActivity.TAG_CURRENT_TEAM_VISITOR);
                        homescore = matchDetails.getString("singleScoreHome");
                        visitorscore = matchDetails.getString("singleScoreVisitor");


                }catch(JSONException e){
                    e.printStackTrace();
                }

                return res;
            }
        }

        @Override
        protected void onPreExecute () {
        }

        @Override
        protected void onPostExecute (String s){
            super.onPostExecute(s);

            teamHome.setText(hometeam);
            teamVisitor.setText(visitorteam);
            scoreHome.setText(homescore + " - ");
            scoreVisitor.setText(visitorscore);

        }
    }

    getMatch gt = new getMatch();
    gt.execute();
}
}

