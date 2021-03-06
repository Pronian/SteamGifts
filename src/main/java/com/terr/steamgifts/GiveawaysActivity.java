package com.terr.steamgifts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GiveawaysActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    public TextView txtPoints;
    GiveawayParser giveawayParser;
    private GiveawayAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<GiveawayRowData> giveawayList = new ArrayList<>();
    private String error = "";
    private boolean hideFeaturedAll = false;
    private String currentPageUrl;
    private short currentPageNumber = 1;
    final private Context mContext = this;

    //TODO refresh button

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
        View header = navigationView.getHeaderView(0);

        setTitle(getString(R.string.sg_mainpage_title));

        currentPageUrl = getString(R.string.sg_mainpage);

        giveawayParser = new GiveawayParser(currentPageUrl, this);

        //Recycler View Start
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new EnterGAClickListener(giveawayList, giveawayParser, this)));
        adapter = new GiveawayAdapter(giveawayList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        registerForContextMenu(recyclerView);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                if (giveawayParser.hasNextPage == false) return;
                final ProgressDialog progressDialog = new ProgressDialog(mContext);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Loading next page...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
                Thread mThread = new Thread()
                {
                    @Override
                    public void run()
                    {
                        addGiveawayData(false);
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                adapter.notifyDataSetChanged();
                                txtPoints.setText(giveawayParser.getPoints());
                                progressDialog.dismiss();
                            }
                        });
                        Log.i(this.toString(), "Loaded more giveaways, current number: " + adapter.getItemCount());

                    }
                };
                mThread.start();
            }
        });
        //Recycler View End

        ImageView usrPic = (ImageView) header.findViewById(R.id.userPic);
        try
        {
            Picasso.with(this).load(giveawayParser.getUserAvatar()).into(usrPic);
        } catch (SiteDataException e)
        {
            //TODO manage this
            Log.e(this.toString(), "Parse error: " + e.getMessage());
        }
        TextView usrName = (TextView) header.findViewById(R.id.userName);
        usrName.setText(giveawayParser.getAccountName() + " • " + giveawayParser.getLevel());

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
        hideFeaturedAll = sharedPref.getBoolean(getString(R.string.sett_featured_key), false);
    }

    private void prepareGiveawayData(boolean notify, boolean showFeatured)
    {
        giveawayList.clear();
        error = "";
        int n = giveawayParser.getGiveawayNumber();
        int i = 0;
        try
        {
            if (showFeatured)
            {
                for (; i < giveawayParser.featuredNumber; i++)
                {
                    giveawayList.add(giveawayParser.getGiveaway(i));
                }
            } else
            {
                i = giveawayParser.featuredNumber;
            }
            for (; i < n; i++)
            {
                giveawayList.add(giveawayParser.getGiveaway(i));
            }
        } catch (SiteDataException e)
        {
            error = e.getMessage();
            Log.e(this.toString(), "Parse error upon loading giveaways: " + e.getMessage());
        }

        if (giveawayList.isEmpty())
        {
            giveawayList.add(new GiveawayRowData("No giveaways found in this category.", false, false, "", "", "", "0", "", ""));
        }

        if (notify) adapter.notifyDataSetChanged();
    }

    private void addGiveawayData(boolean notify)
    {
        currentPageNumber++;
        StringBuilder sb = new StringBuilder();
        sb.append(currentPageUrl);
        while (sb.charAt(sb.length() - 1) != '&')
        {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("page=");
        sb.append(currentPageNumber);

        giveawayParser = new GiveawayParser(sb.toString(), this);
        int n = giveawayParser.getGiveawayNumber();
        try
        {
            for (int i = giveawayParser.featuredNumber; i < n; i++)
            {
                giveawayList.add(giveawayParser.getGiveaway(i));
            }
        } catch (SiteDataException e)
        {
            error = e.getMessage();
            Log.e(this.toString(), "Parse error upon adding giveaways: " + e.getMessage());
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
            this.moveTaskToBack(true);
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
        String title = "";
        boolean showFeatured = false;
        final Context context = this.getApplicationContext();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        updateSettings();
        if (id == R.id.nav_settings)
        {
            CookieSync.updateToken(giveawayParser.getXSRFtoken(), context);
            startActivity(new Intent(this, SettingsActivity.class));
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_all)
        {
            currentPageUrl = getString(R.string.sg_mainpage);
            title = getString(R.string.sg_mainpage_title);
            showFeatured = !hideFeaturedAll;
        } else if (id == R.id.nav_wishlist)
        {
            currentPageUrl = getString(R.string.sg_wishlist);
            title = getString(R.string.sg_wishlist_title);
        } else if (id == R.id.nav_recommended)
        {
            currentPageUrl = getString(R.string.sg_recommended);
            title = getString(R.string.sg_recommended_title);
        } else if (id == R.id.nav_group)
        {
            currentPageUrl = getString(R.string.sg_group);
            title = getString(R.string.sg_group_title);
        } else if (id == R.id.nav_new)
        {
            currentPageUrl = getString(R.string.sg_new);
            title = getString(R.string.sg_new_title);
        }
        final String fPage = currentPageUrl;
        final String fTitle = title;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        final Boolean fShowFeatured = showFeatured;
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        currentPageNumber = 1;
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
                        if (!error.isEmpty())
                        {
                            //TODO replace with popup error
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Inflate Menu from xml resource
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.giveaway_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        ContextMenuRecyclerView.RecyclerContextMenuInfo info = (ContextMenuRecyclerView.RecyclerContextMenuInfo) item.getMenuInfo();
        final int n = info.position;

        int itemId = item.getItemId();
        if (itemId == R.id.contm_steam_page)
        {
            String link = giveawayList.get(n).steamLink;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
        } else if (itemId == R.id.contm_details)
        {

        } else if (itemId == R.id.contm_hide)
        {
            final Context context = this;
            Thread mThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    String gameId = giveawayList.get(n).dataGameId;
                    POSTGeneral req = new POSTGeneral();
                    String cookie = CookieSync.getCookie(context);
                    StringBuilder reqData = new StringBuilder();
                    reqData.append("xsrf_token=");
                    reqData.append(CookieSync.getToken(context));
                    reqData.append("&game_id=");
                    reqData.append(gameId);
                    reqData.append("&do=hide_giveaways_by_game_id");
                    try
                    {
                        req.execute(context.getString(R.string.sg_enter_link), cookie, reqData.toString());
                        for (int i = 0; i < giveawayList.size(); i++)
                        {
                            if (gameId.equals(giveawayList.get(i).dataGameId))
                            {
                                giveawayList.remove(i);
                                Log.d(toString(), "Giveaway number " + i + " removed from list");
                            }
                        }

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                adapter.notifyDataSetChanged();
                                Toast.makeText(context, "Game Hidden", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e)
                    {
                        Log.e(this.toString(), "Error upon hiding game: " + e.getMessage());
                    }
                }
            });
            mThread.start();
        } else return false;

        return true;
    }
}
