package com.bananacoding.weather1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bananacoding.weather1.helper.RSSParser;
import com.bananacoding.weather1.model.RSSWeather;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static String weather_url;
    RSSParser parser = new RSSParser();
    RSSWeather weather;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text_view);

        Intent intent = getIntent();
        String description1 = intent.getStringExtra(Main2Activity.EXTRA_MESSAGE);
        String weather_url = ("http://weather.yahooapis.com/forecastrss?w="+ description1 +"&u=c");
        new loadRSSFeedItems().execute(weather_url);

    }


    class loadRSSFeedItems extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(
                    MainActivity.this);
            pDialog.setMessage("Loading weather...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            String rss_url = args[0];
            weather = parser.getRSSFeedWeather(rss_url);

            return null;
        }

        protected void onPostExecute(String args) {

            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {

                    String description = String.format("%s \n\n date: %s \n\n temp: %s \n\n link: %s", weather.getTitle(), weather.getPubdate(), weather.getTemp(), weather.getLink());
                    textView.setText(description);
                }
            });

        }
    }
}
