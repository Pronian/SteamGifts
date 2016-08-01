package com.terr.steamgifts;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GiveawayAdapter extends RecyclerView.Adapter<GiveawayAdapter.GiveawayViewHolder> {

private List<GiveawayRowData> giveawayList;

public class GiveawayViewHolder extends RecyclerView.ViewHolder {
    public TextView title, timeRemaining, entries;

    public GiveawayViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        timeRemaining = (TextView) view.findViewById(R.id.time);
        entries = (TextView) view.findViewById(R.id.entries);
    }
}


    public GiveawayAdapter(List<GiveawayRowData> giveawayList) {
        this.giveawayList = giveawayList;
    }

    @Override
    public GiveawayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.giveaway_list_row, parent, false);

        return new GiveawayViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GiveawayViewHolder holder, int position) {
        GiveawayRowData ga = giveawayList.get(position);
        holder.title.setText(ga.title);
        holder.timeRemaining.setText(ga.timeRemaining);
        holder.entries.setText(ga.entries);
        if (ga.isEntered)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#9CCC65"));
        }
    }

    @Override
    public int getItemCount() {
        return giveawayList.size();
    }
}