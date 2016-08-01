package com.terr.steamgifts;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPRequestGET extends AsyncTask<String, Void, String>
{

        @Override
        protected String doInBackground(String... params) {
            //do your request in here so that you don't interrupt the UI thread
            try {
                return downloadContent(params[0], params[1]);
            } catch (IOException e) {
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Here you are done with the task
            //Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
        }

        private String downloadContent(String myurl, String cookie) throws IOException
        {
            InputStream is = null;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //conn.setReadTimeout(10000 /* milliseconds */);
                //conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestProperty("Cookie", cookie);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                int response = conn.getResponseCode();
                //Log.d(TAG, "The response is: " + response);
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
