package com.example.android.baskettime;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;


/**
 * CreatedbyFabioon12/01/2016.
 */
public class ChampActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mtoolbar;
    FloatingActionButton newTeam;
    Button newChamp;
    RelativeLayout champLayout;
    SwipeNumberPicker teamPicker;
    EditText newChampionship;

    @Override
    protected void onCreate(Bundle InstanceState) {
        super.onCreate(InstanceState);
        setContentView(R.layout.activity_champ);
        setTitle("NuovoTorneo");

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        newChampionship = (EditText) findViewById(R.id.champ_text);
        newTeam = (FloatingActionButton) findViewById(R.id.new_team);
        newChamp = (Button) findViewById(R.id.new_champion);
        champLayout = (RelativeLayout) findViewById(R.id.champLayout);
        teamPicker = (SwipeNumberPicker) findViewById(R.id.teamPicker);

        newChamp.setOnClickListener(this);
        newTeam.setOnClickListener(this);

        teamPicker.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {

                teamPicker.setText(String.valueOf(newValue));
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();

        }
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == newTeam) {
            /**secondTeam=newEditText(getBaseContext());
            LinearLayout.LayoutParamslayoutParams=newLinearLayout.LayoutParams(1000,LinearLayout.LayoutParams.WRAP_CONTENT);
            secondTeam.setTextColor(Color.parseColor("#000000"));
            secondTeam.setLayoutParams(layoutParams);
            changeEditTextUnderlineColor(secondTeam);
            secondTeam.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            champLayout.addView(secondTeam);**/
        }

        if (v == newChamp) {
            addTeams();
        }
    }

    private void addTeams() {

        SharedPreferences sharedPreferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, MODE_PRIVATE);

        final String idSession = sharedPreferences.getString(ConfigActivity.SESSION_ID, "");
        final String function = "insertTeams";
        final String championship = newChampionship.getText().toString().trim();


        class addTeams extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(ChampActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... v) {

                HashMap<String, String> params = new HashMap <>();
                params.put(ConfigActivity.KEY_CHAMP, championship);
                params.put(ConfigActivity.KEY_ID_SESSION, idSession);
                params.put("f", function);

                RequestHandler requestHandler = new RequestHandler();
                String res = requestHandler.sendPostRequest(ConfigActivity.ENTRY, params);
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