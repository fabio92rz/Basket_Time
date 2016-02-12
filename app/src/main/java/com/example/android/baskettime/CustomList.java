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
        TextView teamVis = (TextView) convertView.findViewById(R.id.teamVis);

        // Prendo i dati dei march per la riga
        Match m = matchItems.get(position);

        // HomeTeam
        String homeTeamStr = "";
        for (String str : m.getHomeTeam()){
            homeTeamStr += str + " ";
        }
        teamHome.setText(homeTeamStr);

        //VisitorTeam
        String visTeamStr = "";
        for (String str : m.getVisitorTeam()){
            visTeamStr += str + " ";
        }
        teamVis.setText(visTeamStr);

        return convertView;
    }

}
