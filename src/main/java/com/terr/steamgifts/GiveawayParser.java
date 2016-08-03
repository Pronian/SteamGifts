package com.terr.steamgifts;


import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GiveawayParser
{
    private String giveaway_url;
    private Context context;
    public String errMess;
    public short points;

    public String XSRFtoken;

    private Document pageDoc;

    public GiveawayParser(String giveaway_url, Context context)
    {
        this.giveaway_url = giveaway_url;
        this.context = context;
        HTTPRequestGET httpRequestGET = new HTTPRequestGET();
        try
        {
            String pageContent = httpRequestGET.execute(this.giveaway_url, CookieSync.getCookie(this.context)).get();
            pageDoc = Jsoup.parse(pageContent);
        } catch (Exception e)
        {
            errMess = context.getResources().getString(R.string.err_connection);
        }
    }

    private void ClearErr()
    {
        errMess = "";
    }

    public void updateContent()
    {
        ClearErr();
        HTTPRequestGET httpRequestGET = new HTTPRequestGET();
        try
        {
            String pageContent = httpRequestGET.execute(this.giveaway_url, CookieSync.getCookie(this.context)).get();
            pageDoc = Jsoup.parse(pageContent);
        } catch (Exception e)
        {
            errMess = context.getResources().getString(R.string.err_connection);
        }
    }

    public String getAccountName()
    {
        ClearErr();
        Element el = pageDoc.getElementsByClass("nav__avatar-outer-wrap").first();
        if (el == null)
        {
            errMess = context.getResources().getString(R.string.err_logged_out);
            return "";
        }
        String result = el.attr("href");
        return result.substring(6);
    }

    public String getPoints()
    {
        ClearErr();
        Element both = pageDoc.getElementsByClass("nav__button-container").get(9);
        Element el = both.getElementsByTag("span").get(0);
        if (el == null)
        {
            errMess = context.getResources().getString(R.string.err_logged_out);
            return "";
        }
        String points = el.text();
        this.points = Short.parseShort(points);
        return points + "P ";
    }

    public String getLevel()
    {
        ClearErr();
        Element both = pageDoc.getElementsByClass("nav__button-container").get(9);
        Element el = both.getElementsByTag("span").get(1);

        if (el == null)
        {
            errMess = context.getResources().getString(R.string.err_logged_out);
            return "";
        }

        return el.text();
    }

    public String getXSRFtoken()
    {
        ClearErr();
        Element el = pageDoc.getElementsByTag("form").first().getElementsByTag("input").first();
        if (el == null)
        {
            errMess = context.getResources().getString(R.string.err_logged_out);
            return "";
        }
        XSRFtoken = el.attr("value");
        return XSRFtoken;
    }

    public int getGiveawayNumber()
    {
        return pageDoc.getElementsByClass("giveaway__row-outer-wrap").size();
    }

    public String getGiveawayNameAndPoints(int n)
    {
        ClearErr();
        Element el = pageDoc.getElementsByClass("giveaway__row-outer-wrap").get(n).getElementsByClass("giveaway__heading").get(0);
        if (el == null)
        {
            errMess = context.getResources().getString(R.string.err_no_content);
            return "";
        }
        return el.text();
    }

    public String getGiveawayID(int n)
    {
        ClearErr();
        Element el = pageDoc.getElementsByClass("giveaway__row-outer-wrap").get(n).getElementsByClass("giveaway__heading__name").get(0);
        if (el == null)
        {
            errMess = context.getResources().getString(R.string.err_no_content);
            return "";
        }
        String link = el.attr("href");
        return link.split("/")[2];
    }

    public boolean IsGivieawayEntered(int n)
    {
        Element ell = pageDoc.getElementsByClass("giveaway__row-outer-wrap").get(n);
        Element el = ell.getElementsByTag("div").get(1);
                //.getElementsByClass("giveaway__row-inner-wrap is-faded");
        //Element el = els.get(0);
        if (el.hasClass("is-faded"))
        {
            return true;
        }
        return false;

    }

    public String getTimeLeft(int n)
    {
        Element ell = pageDoc.getElementsByClass("giveaway__row-outer-wrap").get(n).getElementsByClass("giveaway__columns").first();
        if (ell == null)
        {
            errMess = context.getResources().getString(R.string.err_no_content);
            return "";
        }
        Element el = ell.getElementsByTag("span").first();
        if (el == null)
        {
            errMess = context.getResources().getString(R.string.err_no_content);
            return "";
        }

        return el.text();
    }

    public String getEntriesAndComments(int n)
    {
        Element ell = pageDoc.getElementsByClass("giveaway__row-outer-wrap").get(n).getElementsByClass("giveaway__links").first();
        if (ell == null)
        {
            errMess = context.getResources().getString(R.string.err_no_content);
            return "";
        }
        Element entries = ell.getElementsByTag("span").get(0);
        Element comments = ell.getElementsByTag("span").get(1);
        return entries.text() + " • " +comments.text();
    }

    public String getImageUrl(int n)
    {
        try
        {
            Element ell = pageDoc.getElementsByClass("giveaway__row-outer-wrap").get(n).getElementsByClass("global__image-inner-wrap").get(1);
            if (ell == null)
            {
                errMess = context.getResources().getString(R.string.err_no_content);
                return "";
            }
            String partial = ell.attr("style").split("\\(")[1];
            return partial.substring(0,partial.length()-2);
        }
        catch (Exception e)
        {
            return "";
        }

    }


}
