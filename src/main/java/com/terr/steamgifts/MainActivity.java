package com.terr.steamgifts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
            Toast.makeText(this, CookieSync.getCookie(this), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, GiveawaysActivity.class);
            this.startActivity(intent);
        }
        setTitle(R.string.title_activity_login);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new LoginWVC(this));
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl("http://www.steamgifts.com/?login");
    }
}
