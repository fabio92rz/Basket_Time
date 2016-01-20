package com.example.android.baskettime;

/**
 * Created by fabio on 16/12/2015.
 */
public class ConfigActivity {

    //Url vari
    public static final String LOGIN_URL = "http://95.85.23.84/prova2.php";
    public static final String INSERT_CHAMP = "http://95.85.23.84/insertChamp2.php";
    public static final String INSERT_TEAMS = "http://95.85.23.84/insertTeams.php";
    public static final String GET_TEAMS_URL = "http://95.85.23.84/getTeams.php";
    public static final String INSERT_GAME = "http://95.85.23.84/insertGames.php";
    public static final String REGISTER_URL = "http://95.85.23.84/register.php";

    //Tag JSON
    public static final String JSON_ARRAY = "result";
    public static final String TAG_ID = "id";
    public static final String TAG_HOME_TEAM = "name";
    public static final String TAG_VISITOR_TEAM = "name";
    public static final String TAG_ARENA = "arena";

    //Keys per Email e Password come definito nel file prova.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CHAMP = "championship";
    public static final String KEY_TEAM = "team";
    public static final String KEY_HOME_VISITOR = "id_home_visitor";
    public static final String KEY_HOME_TEAM = "id_home_team";
    public static final String KEY_SCORE_HOME = "final_score_home";
    public static final String KEY_SCORE_VISITOR = "final_score_visitor";


    //Se il Server risponde e quindi il login è andato bene
    public static final String LOGIN_SUCCESS = "success";

    //Keys per le Shared Preferences
    //Questo sarà il nome delle nostre Shared Preferences
    public static final String SHARED_PREF_NAME = "Dati Personali";

    //Questo verrà usato per conservare l'email dell'utente corrente
    public static final String EMAIL_SHARED_PREF = "email";

    //Userò questa variabile booleana nelle Shared Preference pre seguire se l'utente è dentro o no
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

}
