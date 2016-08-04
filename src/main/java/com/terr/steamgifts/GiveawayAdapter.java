package com.terr.steamgifts;


import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GiveawayAdapter extends RecyclerView.Adapter<GiveawayAdapter.GiveawayViewHolder> {

private List<GiveawayRowData> giveawayList;

public class GiveawayViewHolder extends RecyclerView.ViewHolder {
    public TextView title, timeRemaining, entries;
    public ImageView image;

    public GiveawayViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        timeRemaining = (TextView) view.findViewById(R.id.time);
        entries = (TextView) view.findViewById(R.id.entries);
        image = (ImageView) view.findViewById(R.id.image);

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
        if(!ga.url.isEmpty()) Picasso.with(holder.itemView.getContext()).load(ga.url).into(holder.image);
        else holder.image.setImageResource(0);
        if (ga.isEntered)
        {
            int color = holder.itemView.getContext().getResources().getColor(R.color.giveawayEntered);
            holder.itemView.setBackgroundColor(color);
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }


    @Override
    public int getItemCount() {
        return giveawayList.size();
    }
}