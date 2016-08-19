package com.terr.steamgifts;

import android.content.Context;
import android.util.Log;
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
            CookieSync.updateToken(giveawayParser.getXSRFtoken(),context);
            result = eg.execute(context.getResources().getString(R.string.sg_enter_link), CookieSync.getCookie(context), CookieSync.getToken(context), ga.sg_id, command).get();

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
            Log.e(this.toString(),"JSON Error upon parsing request response: " + e.getMessage() + ",\nresult to be parsed: " + result);
        }
        catch (Exception e)
        {
            //TODO Add error popup
            Log.e(this.toString(),"Error upon executing request: " + e.getMessage());
        }

    }

    @Override
    public void onLongClick(View view, int position) {
        //TODO add menu options: Details, Steam page, Hide Giveaway
        Log.v(this.toString(),"LongClicked on giveaway number " + position);
    }
}
