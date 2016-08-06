package com.terr.steamgifts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GiveawaysActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    public TextView txtPoints;
    GiveawayParser giveawayParser;
    private RecyclerView recyclerView;
    private GiveawayAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<GiveawayRowData> giveawayList = new ArrayList<>();
    private String error = "";
    boolean hideFeaturedAll = false;

    //TODO refresh button
    //TODO multiple pages
    //TODO hide featured option
    //TODO no giveaways found message

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giveaways);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle(getString(R.string.sg_mainpage_title));


        giveawayParser = new GiveawayParser(getString(R.string.sg_mainpage), this);

        //Recycler View Start
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new EnterGAClickListener(giveawayList, giveawayParser, this)));
        adapter = new GiveawayAdapter(giveawayList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //Recycler View End

        updateSettings();
        prepareGiveawayData(true, !hideFeaturedAll);

    }

    @Override
    protected void onResume()
    {
        updateSettings();
        super.onResume();
    }

    private void updateSettings()
    {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.title_activity_settings), Context.MODE_MULTI_PROCESS);
        hideFeaturedAll = sharedPref.getBoolean(getString(R.string.sett_featured_key),false);
    }

    private void prepareGiveawayData(boolean notify, boolean showFeatured)
    {
        giveawayList.clear();
        error = "";
        int n = giveawayParser.getGiveawayNumber();
        int i = 0;
        try
        {
            if(showFeatured)
            {
                for( ; i < giveawayParser.featuredNumber; i++)
                {
                    giveawayList.add(giveawayParser.getGiveaway(i));
                }
            }
            else
            {
                i = giveawayParser.featuredNumber;
            }
            for ( ; i < n; i++)
            {
                giveawayList.add(giveawayParser.getGiveaway(i));
            }
        } catch (SiteDataException e)
        {
            error = e.getMessage();
        }

        if(giveawayList.isEmpty())
        {
            giveawayList.add(new GiveawayRowData("No giveaways found in this category.",false,false,"","","","0"));
        }

        if (notify) adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.giveaways, menu);
        String points = giveawayParser.getPoints();
        txtPoints = new TextView(this);
        txtPoints.setText(points);
        txtPoints.setTextColor(ContextCompat.getColor(this, R.color.colorMainText));
        txtPoints.setPadding(5, 0, 5, 0);
        txtPoints.setTypeface(null, Typeface.BOLD);
        txtPoints.setTextSize(14);
        menu.add(0, 0, 1, points).setActionView(txtPoints).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings)
        //{
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        String page = "";
        String title = "";
        boolean showFeatured = false;
        final Context context = this.getApplicationContext();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        updateSettings();
        if (id == R.id.nav_settings)
        {
            startActivity(new Intent(this,SettingsActivity.class));
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (id == R.id.nav_all)
        {
            page = getString(R.string.sg_mainpage);
            title = getString(R.string.sg_mainpage_title);
            showFeatured = !hideFeaturedAll;
        } else if (id == R.id.nav_wishlist)
        {
            page = getString(R.string.sg_wishlist);
            title = getString(R.string.sg_wishlist_title);
        } else if (id == R.id.nav_recommended)
        {
            page = getString(R.string.sg_recommended);
            title = getString(R.string.sg_recommended_title);
        } else if (id == R.id.nav_group)
        {
            page = getString(R.string.sg_group);
            title = getString(R.string.sg_group_title);
        } else if (id == R.id.nav_new)
        {
            page = getString(R.string.sg_new);
            title = getString(R.string.sg_new_title);
        }
        final String fPage = page;
        final String fTitle = title;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        final Boolean fShowFeatured = showFeatured;
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        Thread mThread = new Thread()
        {
            @Override
            public void run()
            {
                giveawayParser = new GiveawayParser(fPage, context);
                prepareGiveawayData(false, fShowFeatured);
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        adapter.notifyDataSetChanged();
                        txtPoints.setText(giveawayParser.getPoints());
                        setTitle(fTitle);
                        layoutManager.scrollToPosition(0);
                        progressDialog.dismiss();
                        if(!error.isEmpty())
                        {
                            //TODO replace with popup error
                            Toast.makeText(context,error,Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        };
        mThread.start();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
