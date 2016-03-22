package com.example.android.baskettime;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Fabio on 22/03/2016.
 */
public class Quarters {

    Integer scoreHome;
    Integer scoreVisitor;
    Integer nquarter;

    Quarters() {

    }
}

class QuarterAdapter extends RecyclerView.Adapter<QuarterAdapter.QuarterViewHolder> {
    List<Quarters> quarter;

    QuarterAdapter(List<Quarters> quarter) {

        this.quarter = quarter;
    }

    @Override
    public QuarterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card_popup, parent, false);
        QuarterViewHolder pvh = new QuarterViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(QuarterViewHolder holder, int position) {

        holder.scoreHome.setText(String.valueOf(quarter.get(position).scoreHome));
        holder.scoreVisitor.setText(String.valueOf(quarter.get(position).scoreVisitor));
        holder.quarter.setText(String.valueOf(quarter.get(position).nquarter + "° Quarto"));

    }

    @Override
    public int getItemCount() {
        if (quarter != null) {
            return quarter.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class QuarterViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView scoreHome;
        TextView scoreVisitor;
        TextView quarter;

        QuarterViewHolder(final View itemView) {

            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cv_popup);
            quarter = (TextView) itemView.findViewById(R.id.quarterPopupCV);
            scoreHome = (TextView) itemView.findViewById(R.id.scoreHomeQuarterCV);
            scoreVisitor = (TextView) itemView.findViewById(R.id.scoreVisitorQuarterCV);

        }
    }
}
