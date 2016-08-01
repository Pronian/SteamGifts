package com.terr.steamgifts;


import android.content.Context;
import android.content.SharedPreferences;

public class CookieSync
{
    private static String cookie = "";

    public static void updateCookie(String cookie, Context context)
    {
        String key = context.getResources().getString(R.string.cookie_key);
        SharedPreferences sharedPref = context.getSharedPreferences( key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, cookie);
        editor.commit();
        CookieSync.cookie = cookie;
    }

    /**
     *
     * @return empty string if nothing has been found
     */
    public static String getCookie(Context context)
    {
        if (cookie.isEmpty())
        {
            String key = context.getResources().getString(R.string.cookie_key);
            SharedPreferences sharedPref = context.getSharedPreferences( key, Context.MODE_PRIVATE);
            cookie = sharedPref.getString(key, "");
        }
        return cookie;
    }
}
