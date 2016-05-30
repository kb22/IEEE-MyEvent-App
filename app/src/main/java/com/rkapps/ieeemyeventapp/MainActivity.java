package com.rkapps.ieeemyeventapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.params.BasicHttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> personList;
    String myJSON;
    JSONArray peoples = null;
    List<IEEEEvents> events = new ArrayList<IEEEEvents>();
    public SharedPreferences sharedpreferences;
    private static final String TAG_RESULTS="result";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NewEvent.class);
                startActivity(i);
                finish();
            }
        });

        InputStream is = null;
        String result = null;


        personList = new ArrayList<HashMap<String,String>>();

        getData();

    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://irobinz.tk/ieee/select.php");

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
                Log.e("log_tag","result " + result);
                try {
                    Log.e("log_tag", "Here -1");
                    JSONObject jsonObj = new JSONObject(myJSON);
                    Log.e("log_tag", "Here 0");
                    peoples = jsonObj.getJSONArray(TAG_RESULTS);
                    Log.e("log_tag", "Here 1");
                    for (int i = 0; i < peoples.length(); i++) {
                        JSONObject c = peoples.getJSONObject(i);
                        events.add(new IEEEEvents(c.getString("Name"), c.getString("ShortDescription"),c.getString("StartDate"), c.getInt("EventID")));
                        Log.e("log_tag", "Value: " + c.getString("Name"));
                    }
                    RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
                    rv.setHasFixedSize(true);
                    LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
                    rv.setLayoutManager(llm);
                    RVAdapter adapter = new RVAdapter(events);
                    rv.setAdapter(adapter);

                }
                catch (Exception e){
                    Log.e("log_tag","Still Nope" + e.toString());
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent i = new Intent(MainActivity.this, FullscreenActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
            moveTaskToBack(true);
            startActivity(i);
            finish();

            return true;
        }

        if(id == R.id.action_about){

            Intent i = new Intent(MainActivity.this, About.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        moveTaskToBack(true);
        finish();
    }
}
