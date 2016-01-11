package com.example.android.baskettime;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by fabio on 11/11/2015.
 */
public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    //TextView per i dati dell'utente
    private TextView tvName_surname;
    private TextView tvEmail;

    //Bottone per il logout
    private Button logoutButton;
    private FloatingActionButton newgame;


    private View mLayout0, mLayout1, mLayout2;

    private Toolbar toolbar;
    private NavigationView navigationView;
    protected DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("Storico Partite");

        //Creo un inflater per inflazionare il layout dell'header
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Inizializzo il Bottone per il logout
        logoutButton = (Button) findViewById(R.id.logout_button);
        newgame = (FloatingActionButton) findViewById(R.id.new_game);
        logoutButton.setOnClickListener(this);
        newgame.setOnClickListener(this);


        //Inizializzo la Toolbar e la inserisco nell'actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Catturo i dati e li inserisco nell'header
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(ConfigActivity.EMAIL_SHARED_PREF, "Not Available");

        //Inizializzo la NavigationView, utilizzata per il drawer
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Inflaziono i layout in modo tale da mostralo nella Navigation View
        View vi = inflater.inflate(R.layout.header, navigationView, false );

        //Inizializzo ed imposto la mail della persona loggata
        tvEmail = (TextView)vi.findViewById(R.id.email_header);
        tvEmail.setText(email);

        //Aggiungo la View
        navigationView.addHeaderView(vi);

        //Imposto la NavigationView con un clicklistener per gestire gli eventi della navigazione del menù **/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            //Questo metodo gestisce i click della navigazione
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Controlla se l'elemento è segnato o no, sennò lo segna
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Chiude il drawer dopo un click
                drawerLayout.closeDrawers();

                //Controlla quale click è stato fatto e esegue la giusta operazione
                switch (menuItem.getItemId()) {

                    case R.id.live:
                        Intent live = new Intent(HistoryActivity.this, LiveActivity.class);
                        startActivity(live);
                        break;

                    case R.id.storico_partite:
                        Intent history = new Intent(HistoryActivity.this, HistoryActivity.class);
                        startActivity(history);
                        break;
                }
                return true;
            }
        });

        //Inizializza il drawer layout e il Toggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerClosed(drawerView);
            }
        };

        //Imposto il toggle e lo indirizzio al drawer
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //Chiamo syncState per far comparire il toggle
        actionBarDrawerToggle.syncState();

        //Inizializzo i tre layout
        mLayout0 = findViewById(R.id.linear_layout0);
        mLayout1 = findViewById(R.id.linear_layout1);
        mLayout2 = findViewById(R.id.linear_layout2);


        //Imposto il click per il layout per aprire il popup

        mLayout0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, Popup0Activity.class));
            }
        });

        mLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, Popup0Activity.class));
            }
        });

        mLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, Popup0Activity.class));
            }
        });
    }



    //Funzione Logout
    private void logout() {
        //Credo un dialogo di allerta per confermare il logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Sei sicuro di voler uscire?");
        alertDialogBuilder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Rimuovo le SharedPreference
                        SharedPreferences preferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                        //Prendo l'editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Setto il valore Booleano a Falso
                        editor.putBoolean(ConfigActivity.LOGGEDIN_SHARED_PREF, false);

                        //Metto un valore vuto nella mail
                        editor.putString(ConfigActivity.EMAIL_SHARED_PREF, "");

                        //Salvo le SharedPreference
                        editor.commit();

                        //Faccio partire la LoginActivity
                        Intent intent = new Intent(HistoryActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    //Elimina gli effetti di transizione
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v == logoutButton) {
            logout();
        }

        if (v == newgame){
            Intent newgame = new Intent(HistoryActivity.this, NewGameActivity.class);
            startActivity(newgame);
        }
    }
}


