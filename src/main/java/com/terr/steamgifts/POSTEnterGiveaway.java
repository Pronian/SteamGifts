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
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestProperty("Cookie", cookie);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            StringBuilder sb = new StringBuilder();
            sb.append("xsrf_token=");
            sb.append(XSRFtoken);
            sb.append("&");
            sb.append("code=");
            sb.append(ga_id);
            sb.append("&");
            sb.append("do=");
            sb.append(command);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(sb.toString());
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
