package com.example.android.baskettime;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Fabio on 12/02/2016.
 */
public class Match {

    private ArrayList <String> homeTeam;
    private ArrayList <String> visitorTeam;

    public Match(){

    }


    public Match(ArrayList <String> homeTeam, ArrayList <String> visitorTeam){

        this.homeTeam = homeTeam;
        this.visitorTeam = visitorTeam;
    }

    public ArrayList<String> getHomeTeam(){
        return homeTeam;
    }

    public void setHomeTeam(ArrayList<String> homeTeam){
        this.homeTeam = homeTeam;
    }

    public ArrayList<String> getVisitorTeam(){
        return visitorTeam;
    }

    public void setVisitorTeam(ArrayList<String> visitorTeam){
        this.visitorTeam = visitorTeam;
    }
}
