package com.example.android.baskettime;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Fabio on 11/02/2016.
 */
public class CustomList extends BaseAdapter {

    ArrayList<String> homeTeam;
    ArrayList<String> visTeam;

    Context mContext;
    //constructor
    public CustomList(Context mContext, ArrayList<String> homeTeam, ArrayList<String> visTeam) {
        this.mContext = mContext;
        this.homeTeam = homeTeam;
        this.visTeam = visTeam;
    }


    @Override
    public int getCount() {
        return 0;
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View arg1, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row, viewGroup, false);

        TextView teamHome = (TextView) row.findViewById(R.id.teamHome);
        TextView teamVis = (TextView) row.findViewById(R.id.teamVis);

        teamHome.setText(homeTeam.get(position));
        teamVis.setText(visTeam.get(position));


        return row;
    }
}
