package com.terr.steamgifts;


import android.content.ContentValues;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class POSTEnterGiveaway extends AsyncTask<String, Void, String>
{
    @Override
    protected String doInBackground(String... params) {
        //do your request in here so that you don't interrupt the UI thread
        try {
            return uploadContent(params[0], params[1], params[2], params[3], params[4]);
        } catch (IOException e) {
            return "Unable to retrieve data. URL may be invalid.";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //Here you are done with the task
        //Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
    }

    private String uploadContent(String myurl, String cookie, String XSRFtoken, String ga_id, String command) throws IOException
    {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Cookie", cookie);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            ContentValues vals = new ContentValues();
            vals.put("xsrf_token", XSRFtoken + "&");
            vals.put("code", ga_id );
            vals.put("do", command+ "&");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            vals.toString();
            writer.write(vals.toString());
            writer.flush();
            writer.close();
            os.close();

            conn.connect();
            int response = conn.getResponseCode();

            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = Until.convertInputStreamToString(is);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
