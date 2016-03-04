package com.example.android.baskettime;

/**
 * Created by fabio on 16/12/2015.
 */
public class ConfigActivity {

    //Url vari
    public static final String LOGIN_URL = "http://95.85.23.84/login2.php";
    public static final String INSERT_CHAMP = "http://95.85.23.84/insertChamp2.php";
    public static final String INSERT_TEAMS = "http://95.85.23.84/insertTeams.php";
    public static final String GET_TEAMS_URL = "http://95.85.23.84/getTeams.php";
    public static final String GET_GAME = "http://95.85.23.84/getGames.php";
    public static final String INSERT_GAMES = "http://95.85.23.84/insertGames.php";
    public static final String REGISTER_URL = "http://95.85.23.84/register.php";
    public static final String INSERT_QUARTER = "http://95.85.23.84/insertQuarter.php";

    //Tag JSON
    public static final String JSON_ARRAY = "result";
    public static final String JSON_GAMES_TAG= "games";
    public static final String TAG_CURRENT_TEAM_HOME = "teamHome";
    public static final String TAG_CURRENT_TEAM_VISITOR = "teamVisitor";
    public static final String TAG_ID = "id";
    public static final String TAG_ID_GAME = "id";
    public static final String TAG_CURRENT_GAME = "result";
    public static final String TAG_CURRENT_ID ="id_game";
    public static final String TAG_HOME_TEAM = "name";
    public static final String TAG_VISITOR_TEAM = "name";
    public static final String TAG_HOME_TEAM_ID = "homeTeam";
    public static final String TAG_VISITOR_TEAM_ID = "visitorTeam";
    public static final String TAG_ARENA = "arena";
    public static final String TAG_TEAM = "teams";
    public static final String TAG_LOGIN = "user";
    public static final String TAG_NAME = "name";
    public static final String TAG_SURNAME = "surname";
    public static final String TAG_STATUS = "status";
    public static final String TAG_QUARTER = "quarter";
    public static final String TAG_CHAMP = "championships";
    public static final String TAG_SPECIFIC_CHAMP = "championship";
    public static final String TAG_GAMES_CHAMP = "id_champ";
    public static final String TAG_CHAMP_HIST = "champ";

    //Keys per Email e Password come definito nel file prova.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CHAMP = "championship";
    public static final String KEY_TEAM = "team";
    public static final String KEY_MATCH_ID = "id";
    public static final String KEY_TEAM2 = "team";
    public static final String KEY_VISITOR_TEAM = "id_home_visitor";
    public static final String KEY_HOME_TEAM = "id_home_team";
    public static final String TAG_SCORE_HOME = "final_score_home";
    public static final String TAG_SCORE_VISITOR = "final_score_visitor";


    //Se il Server risponde e quindi il login è andato bene
    public static final String LOGIN_SUCCESS = "success";

    //Keys per le Shared Preferences
    //Questo sarà il nome delle nostre Shared Preferences
    public static final String SHARED_PREF_NAME = "Dati Personali";
    public static final String ID_GAME = "id_game";

    //Questo verrà usato per conservare l'email dell'utente corrente
    public static final String EMAIL_SHARED_PREF = "email";
    public static final String NAME_SURNAME_PREF = "nameSurname";

    //Userò questa variabile booleana nelle Shared Preference pre seguire se l'utente è dentro o no
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    //Chiavi per l'inserimento dei quarti
    public static final String KEY_SCORE_HOME_TEAM = "score_home_team";
    public static final String KEY_SCORE_VISITOR_TEAM = "score_visitor_team";
    public static final String KEY_SCORE_QUARTER= "nquarter";
    public static final String KEY_ID_CURRENT_MATCH = "id_game";


}
