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
            Log.i(this.toString(),"Cookie fetched from recources: " + CookieSync.getCookie(this));
            Intent intent = new Intent(this, GiveawaysActivity.class);
            this.startActivity(intent);
        }
        else
        {
            setTitle(R.string.title_activity_login);
            Log.i(this.toString(), "Not logged in, starting webview.");
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.setWebViewClient(new LoginWVC(this));
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.loadUrl("http://www.steamgifts.com/?login");
        }
    }
}
