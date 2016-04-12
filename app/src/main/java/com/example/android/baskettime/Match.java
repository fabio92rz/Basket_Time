package com.example.android.baskettime;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Fabio on 12/02/2016.
 */
public class Match {

    private ArrayList<Integer> scoreHome;
    private ArrayList<Integer> scoreVisitors;

    private ArrayList<String> homeTeam;
    private ArrayList<String> visitorTeam;

    private ArrayList<String> championship;

    private ArrayList<String> date;
    private ArrayList<String> time;


    public Match(ArrayList<String> homeTeam, ArrayList<String> visitorTeam, ArrayList<Integer> scoreHome, ArrayList<Integer> scoreVisitors, ArrayList<String> championship, ArrayList<String> date, ArrayList<String> time) {

        this.homeTeam = homeTeam;
        this.visitorTeam = visitorTeam;
        this.scoreHome = scoreHome;
        this.scoreVisitors = scoreVisitors;
        this.championship = championship;
        this.date = date;
        this.time = time;
    }

    public Match() {

    }

    public ArrayList<String> getHomeTeam() {

        return homeTeam;
    }

    public void setHomeTeam(ArrayList<String> homeTeam) {

        this.homeTeam = homeTeam;
    }

    public ArrayList<String> getVisitorTeam() {

        return visitorTeam;
    }

    public void setVisitorTeam(ArrayList<String> visitorTeam) {

        this.visitorTeam = visitorTeam;
    }

    public ArrayList<Integer> getScoreHome() {

        return scoreHome;
    }

    public void setScoreHome(ArrayList<Integer> scoreHome) {

        this.scoreHome = scoreHome;
    }

    public ArrayList<Integer> getScoreVisitors() {

        return scoreVisitors;

    }
    public void setScoreVisitors(ArrayList<Integer> scoreVisitors) {

        this.scoreVisitors = scoreVisitors;
    }

    public void setChampionship(ArrayList<String> championship){

        this.championship = championship;
    }

    public ArrayList<String> getChampionship(){

        return championship;
    }

    public void setDate(ArrayList<String> date){

        this.date = date;
    }

    public ArrayList<String> getDate (ArrayList<String> date){

        return date;
    }

    public void setTime(ArrayList<String> time){

        this.time = time;
    }

    public ArrayList<String> getTime (ArrayList<String> time){

        return time;
    }

}
