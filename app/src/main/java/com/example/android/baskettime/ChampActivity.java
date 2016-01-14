package com.example.android.baskettime;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Fabio on 12/01/2016.
 */
public class ChampActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mtoolbar;
    FloatingActionButton newTeam;
    Button newChamp;
    LinearLayout champLayout;
    EditText firstTeam;
    EditText newChampionship;

    @Override
    protected void onCreate(Bundle InsanceState) {
        super.onCreate(InsanceState);
        setContentView(R.layout.activity_champ);
        setTitle("  Nuovo Torneo");

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        newChampionship = (EditText) findViewById(R.id.champ_text);
        firstTeam = (EditText) findViewById(R.id.team1_text);
        newTeam = (FloatingActionButton) findViewById(R.id.new_team);
        newChamp = (Button) findViewById(R.id.new_champion);
        champLayout = (LinearLayout) findViewById(R.id.linear_champ);

        newChamp.setOnClickListener(this);
        newTeam.setOnClickListener(this);


    }

    private void addChampionship() {

        final String championship = newChampionship.getText().toString().trim();
        final String team = firstTeam.getText().toString().trim();

        class addChampionship extends AsyncTask<Void, Void, String>{

            @Override
            protected void onPreExecute() {}

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(ChampActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v){

                HashMap<String, String> params = new HashMap<>();
                params.put(ConfigActivity.KEY_CHAMP, championship);
                params.put(ConfigActivity.KEY_TEAM, team);

                RequestHandler requestHandler = new RequestHandler();
                String res = requestHandler.sendPostRequest(ConfigActivity.INSERT_URL, params);
                return res;
            }
        }

        addChampionship ac = new addChampionship();
        ac.execute();
    }

    @Override
    public void onClick(View v) {

        if (v == newTeam) {
            EditText team2 = new EditText(getBaseContext());
            team2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            team2.setHint("Inserisci squadra");
            team2.setHintTextColor(Color.parseColor("#000000"));
            champLayout.addView(team2);
        }

        if (v == newChamp) {
            addChampionship();
        }
    }
}
