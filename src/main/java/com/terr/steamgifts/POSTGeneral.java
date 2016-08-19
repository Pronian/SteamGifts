package com.terr.steamgifts;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class POSTGeneral extends AsyncTask<String, Void, String>
{
    @Override
    protected String doInBackground(String... params) {
        //do your request in here so that you don't interrupt the UI thread
        try {
            return uploadContent(params[0], params[1], params[2]);
        } catch (IOException e) {
            return "Unable to retrieve data. URL may be invalid.";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //Here you are done with the task
        //Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
    }

    private String uploadContent(String myurl, String cookie, String postData) throws IOException
    {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(30000 /* milliseconds */);
            conn.setConnectTimeout(35000 /* milliseconds */);
            conn.setRequestProperty("Cookie", cookie);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(postData);
            writer.flush();
            writer.close();
            os.close();

            Log.i(this.toString(),"Executing post on url: " + myurl + ",\nwith cookie: " + cookie + ",\nwith data: " + postData);
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
