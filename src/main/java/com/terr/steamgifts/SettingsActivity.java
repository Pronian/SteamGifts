package com.terr.steamgifts;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONObject;

public class SettingsActivity extends AppCompatActivity
{

    private boolean hideFeatured;
    private SharedPreferences sharedPref;
    private Switch switchFeatured;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPref = this.getSharedPreferences(getString(R.string.title_activity_settings), Context.MODE_MULTI_PROCESS);
        hideFeatured = sharedPref.getBoolean(getString(R.string.sett_featured_key), false);

        switchFeatured = (Switch) findViewById(R.id.set_hide_featured);
        switchFeatured.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                hideFeatured = isChecked;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.sett_featured_key), hideFeatured);
                editor.commit();
            }
        });
        switchFeatured.setChecked(hideFeatured);

        final Context context = this;
        Button syncButt = (Button) findViewById(R.id.sync_button);
        syncButt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Thread mThread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        POSTGeneral req = new POSTGeneral();
                        String cookie = CookieSync.getCookie(context);
                        String reqData = "xsrf_token=" + CookieSync.getToken(context) + "&do=sync";
                        try
                        {
                            String result = req.execute(context.getString(R.string.sg_enter_link), cookie, reqData).get();
                            JSONObject jObject = new JSONObject(result);
                            //String resultType = jObject.getString("type");
                            final String resultMsg = jObject.getString("msg");
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(context, resultMsg, Toast.LENGTH_LONG).show();
                                }
                            });


                        } catch (Exception e)
                        {
                            Log.e(this.toString(),"Error upon syncing with Steam: " + e.getMessage());
                        }
                    }
                });
                mThread.start();
            }
        });
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.sett_featured_key), hideFeatured);
        editor.commit();
    }
}
