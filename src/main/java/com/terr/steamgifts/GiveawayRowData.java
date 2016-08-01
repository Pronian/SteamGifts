package com.terr.steamgifts;


public class GiveawayRowData
{
    public String title;
    public String timeRemaining;
    public String sg_id;
    public String entries;
    public boolean isEntered;

    public GiveawayRowData(String title, Boolean isEntered, String timeRemaining, String entries, String sg_id)
    {
        this.title = title;
        this.timeRemaining = timeRemaining;
        this.entries = entries;
        this.sg_id = sg_id;
        this.isEntered = isEntered;
    }
}
