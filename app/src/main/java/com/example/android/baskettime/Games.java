package com.example.android.baskettime;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Fabio on 10/03/2016.
 */
public class Games {

    String teamHome;
    String teamVisitor;
    String championship;
    String date;
    String time;
    Integer id_game;
    Integer scoreHome;
    Integer scoreVisitor;
    Integer quarter;

    Games() {

    }
}

class RVAdapter extends RecyclerView.Adapter<RVAdapter.GamesViewHolder> {
    List<Games> matches;
    String tempTime = "";

    RVAdapter(List<Games> matches) {

        this.matches = matches;
    }

    @Override
    public GamesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card, parent, false);
        GamesViewHolder pvh = new GamesViewHolder(v);
        return pvh;
    }

    void setValue(String value, String temp,TextView textView, LinearLayout linearLayout) {

        if (!value.equals(temp)) {
            textView.setText(value);
            showViews(linearLayout);

        } else {
            textView.setText("");
            hideViews(linearLayout);
        }
    }

    private void showViews(LinearLayout linearLayout) {

        linearLayout.setVisibility(View.VISIBLE);
    }

    private void hideViews(LinearLayout linearLayout) {

        linearLayout.setVisibility(View.GONE);
    }

    public void clear() {
        matches.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Games> list) {
        matches.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(GamesViewHolder holder, int position) {

        String time = String.valueOf(matches.get(position).date);
        setValue(time, tempTime,holder.dateTv, holder.dateLayout);
        tempTime = time;

        holder.timeTv.setText(matches.get(position).time);
        holder.champ.setText(matches.get(position).championship);
        holder.teamHome.setText(matches.get(position).teamHome);
        holder.scoreHome.setText(String.valueOf(matches.get(position).scoreHome));
        holder.teamVisitor.setText(matches.get(position).teamVisitor);
        holder.scoreVisitor.setText(String.valueOf(matches.get(position).scoreVisitor));
        holder.quarter.setText(String.valueOf(matches.get(position).quarter + "°"));
        holder.cv.setTag(position);
        holder.selectedGame = matches.get(position).id_game;

    }

    @Override
    public int getItemCount() {
        if (matches != null) {
            return matches.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class GamesViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        Integer selectedGame;
        View click;
        LinearLayout dateLayout;
        TextView champ;
        ImageView dateImage;
        TextView teamHome;
        TextView teamVisitor;
        TextView scoreHome;
        TextView dateTv;
        TextView timeTv;
        TextView scoreVisitor;
        TextView quarter;
        ImageButton options;

        GamesViewHolder(final View itemView) {

            super(itemView);

            click = itemView;
            cv = (CardView) itemView.findViewById(R.id.cv);
            dateImage = (ImageView) itemView.findViewById(R.id.calendar_icon);
            timeTv = (TextView) itemView.findViewById(R.id.time_cv);
            dateLayout = (LinearLayout) itemView.findViewById(R.id.dateLayout);
            dateTv = (TextView) itemView.findViewById(R.id.date_cv);
            options = (ImageButton) itemView.findViewById(R.id.popupmenu);
            quarter = (TextView) itemView.findViewById(R.id.final_quarter);
            champ = (TextView) itemView.findViewById(R.id.championship_cv);
            teamHome = (TextView) itemView.findViewById(R.id.teamHome_cv);
            teamVisitor = (TextView) itemView.findViewById(R.id.teamVisitor_cv);
            scoreHome = (TextView) itemView.findViewById(R.id.scoreHome_cv);
            scoreVisitor = (TextView) itemView.findViewById(R.id.scoreVisitor_cv);

            click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent matchDetails = new Intent(v.getContext(), Popup0Activity.class);
                    matchDetails.putExtra("matchID", selectedGame);
                    v.getContext().startActivity(matchDetails);

                }
            });

            options.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());

                    popupMenu.setGravity(Gravity.END);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                case R.id.delete:
                                    Toast.makeText(itemView.getContext(), item.getTitle(), Toast.LENGTH_LONG).show();
                                    break;

                                case R.id.modify:
                                    Toast.makeText(itemView.getContext(), item.getTitle(), Toast.LENGTH_LONG).show();
                                    break;
                            }

                            return true;
                        }
                    });
                }
            });


        }
    }
}
