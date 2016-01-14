package com.bananacoding.weather1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bananacoding.weather1.helper.RSSParser2;
import com.bananacoding.weather1.model.RSSWeather2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main2Activity extends AppCompatActivity {

    private ProgressDialog pDialog;

    RSSParser2 parser1 = new RSSParser2();
    RSSWeather2 weather2;
    TextView textView2;
    private static String weather_url;
    public static final String EXTRA_MESSAGE = ("Send woeid");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView2 = (TextView) findViewById(R.id.textView2);

        Button btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText txt_lat = (EditText) findViewById(R.id.txt_lat);
                String message1 = txt_lat.getText().toString();
                Pattern pLat = Pattern.compile("^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$");
                Matcher mLat = pLat.matcher(message1);

                EditText txt_long = (EditText) findViewById(R.id.txt_long);
                String message2 = txt_long.getText().toString();
                Pattern pLong = Pattern.compile("^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$");
                Matcher mLong = pLong.matcher(message2);

                if (mLong.matches() && mLat.matches()) {

                    String weather1_url = ("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.placefinder%20where%20text=%22" + message1 + "," + message2 + "%22%20and%20gflags=%22R%22");

                    new loadRSSFeedItems().execute(weather1_url);

                } else {
                    Toast.makeText(Main2Activity.this, "คุณใส่ค่าพิกัดไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                    textView2.setText("*Input New Latitude and Longitude*");
                }
            }

            class loadRSSFeedItems extends AsyncTask<String, String, String> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(Main2Activity.this);
                    pDialog.setMessage("Loading weather...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                }

                @Override
                protected String doInBackground(String... args) {

                    String query_url = args[0];
                    weather2 = parser1.getRSSFeedWeather(query_url);

                    return null;
                }
                protected void onPostExecute(String args) {

                    pDialog.dismiss();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                            String description1 = weather2.getwoeid();
                            intent.putExtra(EXTRA_MESSAGE, description1);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

    }
}
