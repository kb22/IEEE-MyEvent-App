package com.rkapps.ieeemyeventapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ShowEvent extends AppCompatActivity {

    String myJSON,mem;
    JSONArray peoples = null;
    int eventID;
    public SharedPreferences sharedpreferences;
    private static final String TAG_RESULTS="result";
    TextView ieeeSec,Name,LongDescription,StartDate,EndDate,ContactDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent i = getIntent();
        eventID = i.getIntExtra("Event_ID", 0); // use the event ID to display the post/event
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        getData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });

    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                Intent i = getIntent();
                int eventID = i.getIntExtra("Event_ID", 0);
                ieeeSec = (TextView)findViewById(R.id.textView18);
                Name = (TextView)findViewById(R.id.textView13);
                LongDescription = (TextView)findViewById(R.id.textView15);
                StartDate = (TextView)findViewById(R.id.textView17);
                EndDate = (TextView)findViewById(R.id.textView30);
                ContactDetails = (TextView)findViewById(R.id.textView20);
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://irobinz.tk/ieee/gettingevents.php?id=" +eventID);

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                Log.e("log_tag","result2 " + result);
                try {
                    Log.e("log_tag", "Here -1");
                    JSONObject jsonObj = new JSONObject(myJSON);
                    Log.e("log_tag", "Here 0");
                    peoples = jsonObj.getJSONArray(TAG_RESULTS);
                    Log.e("log_tag", "Here 1");
                    for (int i = 0; i < peoples.length(); i++) {
                        JSONObject c = peoples.getJSONObject(i);
                        ieeeSec.setText(c.getString("eventtype"));
                        Name.setText(c.getString("Name"));
                        LongDescription.setText(c.getString("LongDescription"));
                        StartDate.setText(c.getString("StartDate"));
                        EndDate.setText(c.getString("EndDate"));
                        ContactDetails.setText(c.getString("ContactDetails"));
                        mem = c.getString("Membership");
                    }
                    TextView tx = (TextView)findViewById(R.id.textView14);
                    tx.setText("Description");
                    TextView tx1 = (TextView)findViewById(R.id.textView16);
                    tx1.setText("Date");
                    TextView tx2 = (TextView)findViewById(R.id.textView29);
                    tx2.setText(" to ");
                    TextView tx3 = (TextView)findViewById(R.id.textView19);
                    tx3.setText("Contact Details");

                }
                catch (Exception e){
                    Log.e("log_tag","Still Nope" + e.toString());
                }
                sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                if(sharedpreferences.getString("Membership", "null").equals(mem)) {
                    fab.setVisibility(View.VISIBLE);
                    Log.e("log_tag","fabyes: " + sharedpreferences.getString("Membership", "null"));
                }
                else{
                    fab.setVisibility(View.GONE);
                    Log.e("log_tag","fabno: " + sharedpreferences.getString("Membership", "null"));

                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    public void deleteData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {
        boolean flag = false;
            @Override
            protected String doInBackground(String... params) {
                flag = false;
                Intent i = getIntent();
                int eventID = i.getIntExtra("Event_ID", 0);
                InputStream inputStream = null;
                String result = null;
                try {
                    DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                    HttpGet httppost = new HttpGet("http://irobinz.tk/ieee/delete.php?id=" +eventID);
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    Log.e("log_tag","EventID = " + eventID);
                    flag = true;
                } catch (Exception e) {
                    flag = false;
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (flag == true){
                    Toast.makeText(ShowEvent.this, "Event Successfully Deleted", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(ShowEvent.this, MainActivity.class);
                    startActivity(in);
                    finish();
                }

                else{
                    Intent in = new Intent(ShowEvent.this, MainActivity.class);
                    startActivity(in);
                    finish();
                    Toast.makeText(ShowEvent.this, "Could Not Delete Event", Toast.LENGTH_SHORT).show();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent i = new Intent(ShowEvent.this, MainActivity.class);
        startActivity(i);
        finish();
        return true;

    }

    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = new Intent(ShowEvent.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
