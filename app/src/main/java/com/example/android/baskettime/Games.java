package com.example.android.baskettime;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    void setValue(String value, String temp,ImageView imageView, TextView textView) {

        if (!value.equals(temp)) {
            textView.setText(value);
            showViews(imageView, textView);

        } else {
            textView.setText("");
            hideViews(imageView, textView);
        }
    }

    private void showViews(ImageView imageView, TextView textView) {

        imageView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
    }

    private void hideViews(ImageView imageView, TextView textView) {

        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
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

        setValue(time, tempTime, holder.dateImage, holder.dateTv);

        tempTime = String.valueOf(matches.get(position).date);

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
            cv = (CardView) itemView.findViewById(R.id.cv_games);
            dateImage = (ImageView) itemView.findViewById(R.id.calendar_icon);
            timeTv = (TextView) itemView.findViewById(R.id.time_cv);
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

                    final PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());

                    popupMenu.setGravity(Gravity.END);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(final MenuItem item) {

                            switch (item.getItemId()) {

                                case R.id.delete:

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(itemView.getContext());
                                    alertDialogBuilder.setMessage("Sicuro di voler eliminare la partita?");
                                    alertDialogBuilder.setPositiveButton("Si",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                    deleteMatch(String.valueOf(selectedGame));
                                                }
                                            });

                                    alertDialogBuilder.setNegativeButton("No",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                }
                                            });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
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
        public void deleteMatch(final String idGame){

            final StringRequest deleteMatch = new StringRequest(Request.Method.POST, ConfigActivity.ENTRY, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    String deletedMatch = "";

                    try {
                        JSONObject j = null;
                        j = new JSONObject(response);

                        String risposta = j.getString("result");
                        deletedMatch = j.getString("match");

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                    Toast.makeText(itemView.getContext(), deletedMatch, Toast.LENGTH_LONG).show();
                    itemView.setVisibility(View.GONE);
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
                    SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    params.put(ConfigActivity.KEY_ID_SESSION, sharedPreferences.getString(ConfigActivity.SESSION_ID, ""));
                    params.put("f", function);
                    params.put("id", idGame);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
            requestQueue.add(deleteMatch);
        }


    }
}
