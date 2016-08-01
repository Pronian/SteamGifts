package com.terr.steamgifts;

import android.content.Context;
import android.content.Intent;
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
            Toast.makeText(context, CookieManager.getInstance().getCookie(url), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, GiveawaysActivity.class);
            context.startActivity(intent);
            webView.clearCache(true);
        }
    }
}
