package com.terr.steamgifts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!CookieSync.getCookie(this).isEmpty())
        {
            Log.i(this.toString(), "Cookie fetched from resources: " + CookieSync.getCookie(this));
            Intent intent = new Intent(this, GiveawaysActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            this.startActivity(intent);
        } else
        {
            Log.i(this.toString(), "Not logged in, starting webview.");
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.setWebViewClient(new LoginWVC(this));
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.loadUrl("http://www.steamgifts.com/?login");
        }
    }

    @Override
    public void onBackPressed()
    {
        this.moveTaskToBack(true);
    }
}
