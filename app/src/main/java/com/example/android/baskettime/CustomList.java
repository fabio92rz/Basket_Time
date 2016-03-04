package com.example.android.baskettime;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabio on 11/02/2016.
 */
public class CustomList extends BaseAdapter {
    private Activity activity;
    TextView championship2;
    private LayoutInflater inflater;
    private List<Match> matchItems;

    public CustomList (Activity activity, List<Match> matchItems) {
        this.activity = activity;
        this.matchItems = matchItems;
    }

    @Override
    public int getCount() {

        return matchItems.size();
    }

    @Override
    public Object getItem(int location) {

        return matchItems.get(location);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.row, null);

        TextView teamHome = (TextView) convertView.findViewById(R.id.teamHome);
        //TextView teamHome2 = (TextView) convertView.findViewById(R.id.teamHome2);
        TextView scorehome = (TextView) convertView.findViewById(R.id.homeScore);
        //TextView scorehome2 = (TextView) convertView.findViewById(R.id.homeScore2);
        TextView teamVis = (TextView) convertView.findViewById(R.id.teamVis);
        //TextView teamVis2 = (TextView) convertView.findViewById(R.id.teamVis2);
        TextView scoreVisitors = (TextView) convertView.findViewById(R.id.visitorScore);
        //TextView scoreVisitors2 = (TextView) convertView.findViewById(R.id.visitorScore2);
        TextView championship = (TextView) convertView.findViewById(R.id.champ_history);
        //TextView championship2 = (TextView) convertView.findViewById(R.id.champ_history2);


        // Prendo i dati dei march per la riga
        Match m = matchItems.get(position);

        // HomeTeam
        String homeTeamStr = "";
        for (String str : m.getHomeTeam()){
            homeTeamStr += str + "\n";
        }
        teamHome.setText(homeTeamStr);

        //VisitorTeam
        String visTeamStr = "";
        for (String str : m.getVisitorTeam()){
            visTeamStr += str + "\n";
        }
        teamVis.setText(visTeamStr);

        //Prendo i risultati delle partite casalinghe
        String scoreHomestr = "";
        for (Integer i : m.getScoreHome()){
            scoreHomestr += i + " -\n";
        }
        scorehome.setText(scoreHomestr);

        //Prendo i risultati delle partite in trasferta
        String scoreVisstr = "";
        for (Integer i : m.getScoreVisitors()){
            scoreVisstr += i + "\n";
        }
        scoreVisitors.setText(scoreVisstr);

        String champ = "";
        for (String i : m.getChampionship()){

            champ = i;

            if (champ == i){

                championship.setText(champ);
            }

            else{

                championship2.setText(champ);
            }
        }

        return convertView;
    }

}

