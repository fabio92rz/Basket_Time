package com.example.android.baskettime;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    EditText secondTeam;
    EditText newChampionship;

    @Override
    protected void onCreate(Bundle InstanceState) {
        super.onCreate(InstanceState);
        setContentView(R.layout.activity_champ);
        setTitle("Nuovo Torneo");

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        newChampionship = (EditText) findViewById(R.id.champ_text);
        firstTeam = (EditText) findViewById(R.id.team1_text);
        newTeam = (FloatingActionButton) findViewById(R.id.new_team);
        newChamp = (Button) findViewById(R.id.new_champion);
        champLayout = (LinearLayout) findViewById(R.id.linear_champ);

        newChamp.setOnClickListener(this);
        newTeam.setOnClickListener(this);


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

    @Override
    public void onClick(View v) {

        if (v == newTeam) {
            secondTeam = new EditText(getBaseContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1000, LinearLayout.LayoutParams.WRAP_CONTENT);
            secondTeam.setTextColor(Color.parseColor("#000000"));
            secondTeam.setLayoutParams(layoutParams);
            changeEditTextUnderlineColor(secondTeam);
            secondTeam.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            champLayout.addView(secondTeam);
        }

        if (v == newChamp) {
            addTeams();
        }
    }

    private void addTeams() {

        final String team = firstTeam.getText().toString().trim();
        final String championship = newChampionship.getText().toString().trim();

        class addTeams extends AsyncTask<String, Void, String>{

            @Override
            protected void onPreExecute() {}

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(ChampActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... v){

                HashMap<String, String> params = new HashMap<>();
                params.put(ConfigActivity.KEY_TEAM, team);
                params.put(ConfigActivity.KEY_CHAMP, championship);

                RequestHandler requestHandler = new RequestHandler();
                String res = requestHandler.sendPostRequest(ConfigActivity.INSERT_TEAMS, params);
                return res;
            }
        }

        addTeams at = new addTeams();
        at.execute();
    }

    public static void changeEditTextUnderlineColor(EditText editText) {
        int color = Color.parseColor("#757575");
        Drawable drawable = editText.getBackground();
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            editText.setBackground(drawable);
        } else {
            editText.setCompoundDrawables(null, null, drawable, null);
        }
    }
}
