package com.terr.steamgifts;


public class GiveawayRowData
{
    public String title;
    public String timeRemaining;
    public String sg_id;
    public String entries;
    public boolean isEntered;
    public boolean isFeatured;
    public String url;
    public String steamLink;
    public String dataGameId;

    public GiveawayRowData(String title, Boolean isEntered, Boolean isFeatured, String timeRemaining, String entries, String url,String sg_id, String steamLink, String dataGameId)
    {
        this.title = title;
        this.timeRemaining = timeRemaining;
        this.entries = entries;
        this.sg_id = sg_id;
        this.isEntered = isEntered;
        this.url = url;
        this.isFeatured = isFeatured;
        this.steamLink = steamLink;
        this.dataGameId = dataGameId;
    }
}
