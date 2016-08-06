package com.terr.steamgifts;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

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
        switchFeatured.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hideFeatured = isChecked;
            }
        });
        switchFeatured.setChecked(hideFeatured);
    }

    @Override
    protected  void onStop()
    {
        super.onStop();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.sett_featured_key), hideFeatured);
        editor.commit();
    }
}
