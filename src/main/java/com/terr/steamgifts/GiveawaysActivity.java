package com.terr.steamgifts;

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
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GiveawaysActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    TextView txtPoints;
    GiveawayParser giveawayParser;
    private RecyclerView recyclerView;
    private GiveawayAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<GiveawayRowData> giveawayList = new ArrayList<>();

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

        giveawayParser = new GiveawayParser(getString(R.string.sg_recommended),this);

        //Recycler View Start
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new EnterGAClickListener(giveawayList,giveawayParser,this) ));
        adapter = new GiveawayAdapter(giveawayList);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        //Recycler View End

        //TextView t = (TextView) findViewById(R.id.temptxt);


        //t.setText(giveawayParser.getLevel() + giveawayParser.getGiveawayNumber() + giveawayParser.getGiveawayNameAndPoints(0) + " " + giveawayParser.getXSRFtoken());
        //POSTEnterGiveaway eg = new POSTEnterGiveaway();
        //eg.execute(getString(R.string.sg_enter_link),CookieSync.getCookie(this), giveawayParser.getXSRFtoken(), "P2KNa");
        prepareGiveawayData();

    }

    private void prepareGiveawayData()
    {
        GiveawayRowData ga;
        int n = giveawayParser.getGiveawayNumber();
        for (int i = 0; i < n; i++)
        {
            ga = new GiveawayRowData(giveawayParser.getGiveawayNameAndPoints(i),giveawayParser.IsGivieawayEntered(i), giveawayParser.getGiveawayID(i),giveawayParser.getGiveawayID(i),giveawayParser.getGiveawayID(i));
            giveawayList.add(ga);
        }
        adapter.notifyDataSetChanged();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {
            // Handle the camera action
        } else if (id == R.id.nav_gallery)
        {

        } else if (id == R.id.nav_slideshow)
        {

        } else if (id == R.id.nav_manage)
        {

        } else if (id == R.id.nav_share)
        {

        } else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
