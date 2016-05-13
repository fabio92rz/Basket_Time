package com.example.android.baskettime;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Base64;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.jensdriller.libs.undobar.UndoBar;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by fabio on 11/11/2015.
 */
public class HistoryActivity extends AppCompatActivity implements View.OnClickListener, ListView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rv;
    LinearLayoutManager llm;
    List<Games> matchList;

    SwipeRefreshLayout swipeRefreshLayout;

    //TextView per i dati dell'utente
    private TextView tvEmail;
    private TextView tvNameSurname;
    private CircleImageView profilePicture;
    public static int SELECT_PICTURE = 1;

    //Bottone per il logout
    private Button logoutButton;
    private FloatingActionButton newgame;
    private FloatingActionButton prova;

    private Toolbar toolbar;
    private NavigationView navigationView;
    protected DrawerLayout drawerLayout;
    public Bitmap scaled;
    Games positionTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        setTitle("Storico Partite");

        matchList = new ArrayList<>();

        rv = (RecyclerView) findViewById(R.id.recyclerView);

        final RVAdapter rvAdapter = new RVAdapter(matchList);
        rv.setAdapter(rvAdapter);

        rv.setItemViewCacheSize(ViewGroup.PERSISTENT_SCROLLING_CACHE);
        rv.setItemViewCacheSize(ViewGroup.PERSISTENT_ALL_CACHES);
        rv.setItemViewCacheSize(10000);

        rv.setHasFixedSize(true);

        llm = new LinearLayoutManager(HistoryActivity.this);
        rv.setLayoutManager(llm);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //Creo un inflater per inflazionare il layout dell'header
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Inizializzo il Bottone per il logout
        logoutButton = (Button) findViewById(R.id.logout_button);
        newgame = (FloatingActionButton) findViewById(R.id.new_game);

        logoutButton.setOnClickListener(this);
        newgame.setOnClickListener(this);


        //Inizializzo la Toolbar e la inserisco nell'actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Catturo i dati e li inserisco nell'header
        final SharedPreferences sharedPreferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(ConfigActivity.EMAIL_SHARED_PREF, "Not Available");
        String nameSurname = sharedPreferences.getString(ConfigActivity.NAME_SURNAME_PREF, "Not Available");
        final String profilePic = sharedPreferences.getString(ConfigActivity.SERVER_PATH, "");


        //Log.d("Prova della foto server", "Path = " + serverPicturePath);

        final String idSession = sharedPreferences.getString(ConfigActivity.SESSION_ID, "");

        //Inizializzo la NavigationView, utilizzata per il drawer
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Inflaziono i layout in modo tale da mostralo nella Navigation View
        View vi = inflater.inflate(R.layout.header, navigationView, false);

        ImageView background = (ImageView) vi.findViewById(R.id.header_image);
        Picasso.with(this).load("http://i.imgur.com/6etUX3l.jpg").into(background);


        //Inizializzo ed imposto la mail della persona loggata
        tvEmail = (TextView) vi.findViewById(R.id.email_header);
        tvEmail.setText(email);

        tvNameSurname = (TextView) vi.findViewById(R.id.username_header);
        tvNameSurname.setText(nameSurname);

        profilePicture = (CircleImageView) vi.findViewById(R.id.profile_image);

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

                //Controlla quale click è stato fatto e esegue la giusta operazione
                switch (menuItem.getItemId()) {

                    case R.id.live:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent live = new Intent(HistoryActivity.this, LiveActivity.class);
                                startActivity(live);
                            }
                        }, 340);
                        break;

                    case R.id.storico_partite:
                        break;

                    case R.id.insert_championship:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent championship = new Intent(HistoryActivity.this, ChampActivity.class);
                                startActivity(championship);
                            }
                        }, 340);
                        break;
                }

                //Chiude il drawer dopo un click
                drawerLayout.closeDrawers();

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

                super.onDrawerOpened(drawerView);
            }
        };

        //Imposto il toggle e lo indirizzio al drawer
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //Chiamo syncState per far comparire il toggle
        actionBarDrawerToggle.syncState();
        getGames();

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(rv,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {

                            @Override
                            public boolean canSwipeLeft(int position) {
                                return false;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {

                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                }
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {


                                    String idGame = String.valueOf(matchList.get(position).id_game);

                                    positionTemp = rvAdapter.getItem(position);

                                    deleteMatch(idGame);
                                    matchList.remove(matchList.get(position));
                                    rvAdapter.notifyItemRemoved(position);
                                    rv.setAdapter(rvAdapter);
                                }

                                rvAdapter.notifyDataSetChanged();
                            }
                        });

        rv.addOnItemTouchListener(swipeTouchListener);
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
                        final SharedPreferences preferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                        //Prendo l'editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Setto il valore Booleano a Falso
                        editor.clear();
                        editor.apply();

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

        if (v == newgame) {
            Intent newgame = new Intent(HistoryActivity.this, NewGameActivity.class);
            startActivity(newgame);
            //overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void loadImagefromGallery(View view) {

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(galleryIntent, "Seleziona Foto"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        final SharedPreferences sharedPreferences = HistoryActivity.this.getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();

                try {

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    CircleImageView imageView = (CircleImageView) findViewById(R.id.profile_image);
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                    int nh = (int) (bmp.getHeight() * (512.0 / bmp.getWidth()));
                    scaled = Bitmap.createScaledBitmap(bmp, 512, nh, true);

                    String path = FileUtility.getRealPathFromURI(getApplicationContext(), Uri.parse("file://" + selectedImageUri.getPath()));
                    editor.putString(ConfigActivity.PROFILE_PIC, path);

                    ExifInterface exif = new ExifInterface(path);

                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    switch (rotation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            scaled = rotateImage(scaled, 90);
                            String profile90 = getStringImage(scaled);
                            editor.putString(ConfigActivity.PROFILE_PICTURE, profile90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            scaled = rotateImage(scaled, 180);
                            String profile180 = getStringImage(scaled);
                            editor.putString(ConfigActivity.PROFILE_PICTURE, profile180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            scaled = rotateImage(scaled, 270);
                            String profile270 = getStringImage(scaled);
                            editor.putString(ConfigActivity.PROFILE_PICTURE, profile270);
                            break;
                    }

                    editor.putBoolean(ConfigActivity.PROFILE_PIC_BOOLEAN, true);
                    editor.apply();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        StringRequest pictureRequest = new StringRequest(Request.Method.POST, ConfigActivity.ENTRY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject risposta = null;
                final SharedPreferences preferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                try {

                    risposta = new JSONObject(response);
                    String serverPath = risposta.getString("image");

                    View header = navigationView.getHeaderView(0);
                    CircleImageView profilePict = (CircleImageView) header.findViewById(R.id.profile_image);

                    Picasso.with(getBaseContext()).load(serverPath).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.account_circle).into(profilePict);

                    editor.remove(ConfigActivity.SERVER_PATH);
                    editor.putString(ConfigActivity.SERVER_PATH, serverPath);

                    editor.apply();

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

                SharedPreferences preferences2 = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                final String function = "uploadPhoto";
                final String profilePic = preferences2.getString(ConfigActivity.PROFILE_PICTURE, "");

                Log.d("HistoryActivity", "prova upload" + profilePic);

                params.put("f", function);
                params.put("profilePicture", profilePic);
                params.put(ConfigActivity.KEY_ID_SESSION, preferences2.getString(ConfigActivity.SESSION_ID, ""));
                params.put("id", String.valueOf(preferences2.getInt(ConfigActivity.userId, 0)));

                return params;
            }
        };


        RequestQueue pictureQueue = Volley.newRequestQueue(this);
        pictureQueue.add(pictureRequest);

    }

    @Override
    public void onResume() {
        super.onResume();

        final SharedPreferences sharedPreferences = HistoryActivity.this.getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, MODE_PRIVATE);
        boolean profilePictureBoolean = sharedPreferences.getBoolean(ConfigActivity.PROFILE_PIC_BOOLEAN, false);

        if (profilePictureBoolean) {

            String profilePicture = sharedPreferences.getString(ConfigActivity.SERVER_PATH, "");
            Log.d("HistoryActivity", "profilePictureOnResume" + profilePicture);

            View header = navigationView.getHeaderView(0);
            CircleImageView profilePict = (CircleImageView) header.findViewById(R.id.profile_image);

            Picasso.with(this)
                    .load(profilePicture)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .placeholder(R.drawable.account_circle)
                    .into(profilePict);
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    public String getStringImage(Bitmap source) {
        ByteArrayOutputStream profilePictureByte = new ByteArrayOutputStream();
        source.compress(Bitmap.CompressFormat.JPEG, 100, profilePictureByte);
        byte[] profilePicBytes = profilePictureByte.toByteArray();
        String encodedProfilePic = Base64.encodeToString(profilePicBytes, Base64.DEFAULT);

        return encodedProfilePic;
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getGamesRefreshed();
                swipeRefreshLayout.setRefreshing(false);

            }
        }, 2000);
    }

    public void getGamesRefreshed() {

        StringRequest gameRequest = new StringRequest(Request.Method.POST, ConfigActivity.ENTRY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    final RVAdapter rvAdapter = new RVAdapter(matchList);
                    rv.setAdapter(rvAdapter);
                    rvAdapter.clear();

                    JSONObject j = null;
                    j = new JSONObject(response);
                    JSONArray matches = j.getJSONArray(ConfigActivity.JSON_GAMES_TAG);

                    for (int i = 0; i < matches.length(); i++) {

                        JSONObject jsonObject = matches.getJSONObject(i);
                        Games games = new Games();


                        games.date = jsonObject.getString("day");
                        games.time = jsonObject.getString("time");
                        games.championship = jsonObject.getString(ConfigActivity.TAG_CHAMP_HIST);
                        games.teamHome = jsonObject.getString(ConfigActivity.TAG_HOME_TEAM_ID);
                        games.scoreHome = jsonObject.getInt(ConfigActivity.TAG_SCORE_HOME);
                        games.teamVisitor = jsonObject.getString(ConfigActivity.TAG_VISITOR_TEAM_ID);
                        games.scoreVisitor = jsonObject.getInt(ConfigActivity.TAG_SCORE_VISITOR);
                        games.quarter = jsonObject.getInt(ConfigActivity.TAG_QUARTER);
                        games.id_game = jsonObject.getInt(ConfigActivity.TAG_ID_GAME);

                        matchList.add(games);

                    }

                    rvAdapter.notifyDataSetChanged();

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

                SharedPreferences sharedPreferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                final String function = "getGames";

                //Aggiungo i parametri alla richiesta
                params.put(ConfigActivity.KEY_ID_SESSION, sharedPreferences.getString(ConfigActivity.SESSION_ID, ""));
                params.put("f", function);

                //Ritorno i paramentri
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.add(gameRequest);
    }

    public void getGames() {

        StringRequest gameRequest = new StringRequest(Request.Method.POST, ConfigActivity.ENTRY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final RVAdapter rvAdapter = new RVAdapter(matchList);
                rv.setAdapter(rvAdapter);

                try {

                    JSONObject j = null;
                    j = new JSONObject(response);
                    JSONArray matches = j.getJSONArray(ConfigActivity.JSON_GAMES_TAG);

                    for (int i = 0; i < matches.length(); i++) {

                        JSONObject jsonObject = matches.getJSONObject(i);
                        Games games = new Games();


                        games.date = jsonObject.getString("day");
                        games.time = jsonObject.getString("time");
                        games.championship = jsonObject.getString(ConfigActivity.TAG_CHAMP_HIST);
                        games.teamHome = jsonObject.getString(ConfigActivity.TAG_HOME_TEAM_ID);
                        games.scoreHome = jsonObject.getInt(ConfigActivity.TAG_SCORE_HOME);
                        games.teamVisitor = jsonObject.getString(ConfigActivity.TAG_VISITOR_TEAM_ID);
                        games.scoreVisitor = jsonObject.getInt(ConfigActivity.TAG_SCORE_VISITOR);
                        games.quarter = jsonObject.getInt(ConfigActivity.TAG_QUARTER);
                        games.id_game = jsonObject.getInt(ConfigActivity.TAG_ID_GAME);

                        matchList.add(games);

                    }

                    rvAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rvAdapter.notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                SharedPreferences sharedPreferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                final String function = "getGames";

                //Aggiungo i parametri alla richiesta
                params.put(ConfigActivity.KEY_ID_SESSION, sharedPreferences.getString(ConfigActivity.SESSION_ID, ""));
                params.put("f", function);

                //Ritorno i paramentri
                //Ritorno i paramentri
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.add(gameRequest);
    }

    public void deleteMatch(final String idGame) {

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        Snackbar
                .make(coordinatorLayout, "Match eliminato!", Snackbar.LENGTH_LONG)

                .setCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, DISMISS_EVENT_TIMEOUT);

                        final StringRequest deleteMatch = new StringRequest(Request.Method.POST, ConfigActivity.ENTRY, new Response.Listener<String>() {
                            @Override

                            public void onResponse(String response) {


                                try {
                                    JSONObject j = null;
                                    j = new JSONObject(response);

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

                                final String function = "deleteMatch";
                                SharedPreferences sharedPreferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                params.put(ConfigActivity.KEY_ID_SESSION, sharedPreferences.getString(ConfigActivity.SESSION_ID, ""));
                                params.put("f", function);
                                params.put("id", idGame);

                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                        requestQueue.add(deleteMatch);
                    }
                })
                .setAction("ANNULLA", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                        RVAdapter adapter = new RVAdapter(matchList);

                        matchList.add(positionTemp);
                        adapter.notifyItemInserted(matchList.size());
                        adapter.notifyDataSetChanged();

                        recyclerView.setAdapter(adapter);

                    }
                })
                .show();
    }
}


