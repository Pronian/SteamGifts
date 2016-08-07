package com.terr.steamgifts;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EnterGAClickListener implements ClickListener
{
    private Context context;
    private GiveawaysActivity activity;
    private List<GiveawayRowData> giveawayList;
    private GiveawayParser giveawayParser;

    public String resultType;
    public int resultPoints;

    public EnterGAClickListener(List<GiveawayRowData> giveawayList , GiveawayParser giveawayParser, GiveawaysActivity a)
    {
        this.giveawayList = giveawayList;
        this.giveawayParser =giveawayParser;
        this.context = a;
        this.activity = a;
    }

    @Override
    public void onClick(View view, int position) {
        if(giveawayList.get(position).sg_id.equals("0")) return;
        String result = "Parse error";
        GiveawayRowData ga = giveawayList.get(position);
        POSTEnterGiveaway eg = new POSTEnterGiveaway();
        try
        {
            String command;
            int color;
            CharSequence mess;
            Boolean isEntered;
            if(giveawayList.get(position).isEntered)
            {
                command = context.getResources().getString(R.string.command_leave);
                if(giveawayList.get(position).isFeatured) color = context.getResources().getColor(R.color.giveawayFeatured);
                else color = context.getResources().getColor(R.color.colorMainText);
                mess = context.getResources().getText(R.string.succ_leave);
                isEntered = false;
            }
            else
            {
                command = context.getResources().getString(R.string.command_enter);
                color = context.getResources().getColor(R.color.giveawayEntered);
                mess = context.getResources().getText(R.string.succ_enter);
                isEntered = true;
            }

            result = eg.execute(context.getResources().getString(R.string.sg_enter_link), CookieSync.getCookie(context), giveawayParser.getXSRFtoken(), ga.sg_id, command).get();

            JSONObject jObject = new JSONObject(result);
            resultType = jObject.getString("type");
            resultPoints = jObject.getInt("points");
            if (resultType.equals("success"))
            {
                giveawayList.get(position).isEntered = isEntered;
                view.setBackgroundColor(color);
                Toast.makeText(context, mess , Toast.LENGTH_LONG).show();
            }
            else if(resultType.equals("error"))
            {
                mess = jObject.getString("msg");
                Toast.makeText(context, mess , Toast.LENGTH_LONG).show();
            }
            activity.txtPoints.setText(resultPoints + "P ");

        }
        catch (JSONException e)
        {
            Toast.makeText(context, result , Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            //TODO Add error popup
            e.printStackTrace();
        }

    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
