package com.terr.steamgifts;


import android.content.Context;
import android.view.View;

import java.util.List;

public class EnterGAClickListener implements ClickListener
{
    private Context context;
    private List<GiveawayRowData> giveawayList;
    private GiveawayParser giveawayParser;

    public EnterGAClickListener(List<GiveawayRowData> giveawayList , GiveawayParser giveawayParser,Context context)
    {
        this.giveawayList = giveawayList;
        this.giveawayParser =giveawayParser;
        this.context = context;
    }

    @Override
    public void onClick(View view, int position) {
        GiveawayRowData ga = giveawayList.get(position);
        POSTEnterGiveaway eg = new POSTEnterGiveaway();
        eg.execute(context.getResources().getString(R.string.sg_enter_link),CookieSync.getCookie(context), giveawayParser.getXSRFtoken(), ga.sg_id);
    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
