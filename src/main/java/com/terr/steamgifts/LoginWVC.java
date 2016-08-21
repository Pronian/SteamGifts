package com.terr.steamgifts;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class LoginWVC extends WebViewClient
{
    private Context context;

    public LoginWVC(Context context)
    {
        this.context = context;
    }

    public void onPageFinished(WebView webView, String url)
    {
        if(url.startsWith("https://www.steamgifts.com"))
        {
            CookieSync.updateCookie(CookieManager.getInstance().getCookie(url),context);
            Log.i(toString(),"Cookie created: " + CookieManager.getInstance().getCookie(url));
            context.startActivity(new Intent(context, GiveawaysActivity.class));
            webView.clearCache(true);
        }
    }

}
