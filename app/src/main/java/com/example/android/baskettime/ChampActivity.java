package com.example.android.baskettime;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * Created by Fabio on 12/01/2016.
 */
public class ChampActivity extends AppCompatActivity {

    Toolbar mtoolbar;
    Spinner numTeam;
    LinearLayout champLayout;

    private String [] arraySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ);
        setTitle("  Nuovo Torneo");

        this.arraySpinner = new String[]{
                "1", "2", "3"
        };

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        champLayout = (LinearLayout)findViewById (R.id.linear_champ);

        numTeam = (Spinner) findViewById(R.id.num_team);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        numTeam.setAdapter(adapter);

        numTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                switch (position) {

                    case 0:
                        EditText et = new EditText(getBaseContext());
                        et.setLayoutParams(
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                        et.setText("Inserisci squadra!");
                        champLayout.addView(et);
                        Toast.makeText(parentView.getContext(), "Numero 1", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }


}
