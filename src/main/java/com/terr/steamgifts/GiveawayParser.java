package com.terr.steamgifts;


import android.content.Context;
import android.net.ParseException;

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
    public boolean hasNextPage;

    public int featuredNumber;
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
            featuredNumber = getFeaturedGiveawayNumber();
            Elements els = pageDoc.getElementsByClass("pagination__navigation");
            if(els == null) hasNextPage = false;
            else hasNextPage = els.first().text().contains("Next");

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

    public int getFeaturedGiveawayNumber()
    {
        return pageDoc.getElementsByClass("pinned-giveaways__outer-wrap").first().getElementsByClass("giveaway__row-outer-wrap").size();
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

    public GiveawayRowData getGiveaway(int n) throws SiteDataException
    {
        String sUrl = "";
        Element url;
        Element giveaway = pageDoc.getElementsByClass("giveaway__row-outer-wrap").get(n);
        if(giveaway == null)
        {
            throw new SiteDataException(context.getResources().getString(R.string.err_no_content));
        }
        Element nameAndPoints = giveaway.getElementsByClass("giveaway__heading").get(0);
        String name = nameAndPoints.text();
        Element ID = giveaway.getElementsByClass("giveaway__heading__name").get(0);
        Element divFade = giveaway.getElementsByTag("div").get(1);
        Element timeLeft1 = giveaway.getElementsByClass("giveaway__columns").first();
        if(timeLeft1 == null || ID == null || divFade == null)
        {
            throw new SiteDataException(context.getResources().getString(R.string.err_no_content));
        }
        Element timeLeft2 = timeLeft1.getElementsByTag("span").first();
        Element entriesAndComments = giveaway.getElementsByClass("giveaway__links").first();
        Element entries = entriesAndComments.getElementsByTag("span").get(0);
        Element comments = entriesAndComments.getElementsByTag("span").get(1);
        Elements iurl = giveaway.getElementsByClass("global__image-inner-wrap");
        if(iurl != null && iurl.size() > 1)
        {
            url = iurl.get(1);
            sUrl = url.attr("style").split("\\(")[1];
            sUrl = sUrl.substring(0,sUrl.length()-2);
        }
        if(timeLeft2 == null || entries == null || comments == null)
        {
            throw new SiteDataException(context.getResources().getString(R.string.err_no_content));
        }
        Boolean isFeatured;
        if (n + 1 <= featuredNumber) isFeatured = true;
        else isFeatured = false;

        return new GiveawayRowData(nameAndPoints.text(),
                divFade.hasClass("is-faded"),
                isFeatured,
                timeLeft2.text(),
                entries.text() + " • " +comments.text(),
                sUrl,
                ID.attr("href").split("/")[2]
                );
    }

    public boolean hasNextPage()
    {
        Elements els = pageDoc.getElementsByClass("pagination__navigation");
        if(els == null) return false;
        return els.first().text().contains("Next");
    }


}
