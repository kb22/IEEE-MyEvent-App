package com.rkapps.ieeemyeventapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.ReferenceQueue;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class NewEvent extends AppCompatActivity {

    private SimpleDateFormat dateFormatter;
    public SharedPreferences sharedpreferences;
    EditText start,end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        RadioButton rb = (RadioButton)findViewById(R.id.radioButton);
        rb.setText(sharedpreferences.getString("Section", null));

        RadioButton rb2 = (RadioButton)findViewById(R.id.radioButton2);
        rb2.setText(sharedpreferences.getString("SubSection", null));

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        start = (EditText) findViewById(R.id.editText4);
        start.setInputType(InputType.TYPE_NULL);
        start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewEvent.this);
                    final DatePicker timePicker = new DatePicker(NewEvent.this);
                    builder.setTitle("Set Date");
                    builder.setView(timePicker);
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int year = timePicker.getYear();
                            int month = timePicker.getMonth();
                            int day = timePicker.getDayOfMonth();
                            start.setText(year + "-" + (month+1) + "-" + day);
                        }
                    });
                    builder.show();
                }
            }
        });

        end = (EditText) findViewById(R.id.editText5);
        end.setInputType(InputType.TYPE_NULL);
        end.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewEvent.this);
                    final DatePicker timePicker = new DatePicker(NewEvent.this);
                    builder.setTitle("Set Date");
                    builder.setView(timePicker);
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int year = timePicker.getYear();
                            int month = timePicker.getMonth();
                            int day = timePicker.getDayOfMonth();
                            end.setText(year + "-" + (month+1) + "-" + day);
                        }
                    });
                    builder.show();
                }
            }
        });

        Button btn = (Button)findViewById(R.id.button22);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent(v);
            }
        });

    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;

    }

    public void createEvent(View v) {
        String result = null;
        InputStream is = null;
        EditText editText = (EditText) findViewById(R.id.editText);
        String v1 = editText.getText().toString();
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        String v2 = editText2.getText().toString();
        EditText editText3 = (EditText) findViewById(R.id.editText3);
        String v3 = editText3.getText().toString();
        EditText editText4 = (EditText) findViewById(R.id.editText4);
        String v4 = editText4.getText().toString();
        EditText editText5 = (EditText) findViewById(R.id.editText5);
        String v5 = editText5.getText().toString();
        EditText editText6 = (EditText) findViewById(R.id.editText6);
        String v6 = editText6.getText().toString();
        String v7 = null;
        RadioButton rb = (RadioButton)findViewById(R.id.radioButton);
        RadioButton rb2 = (RadioButton)findViewById(R.id.radioButton2);
        if(rb.isChecked()){
            v7 = rb.getText().toString();
        }else if(rb2.isChecked()){
            v7 = rb2.getText().toString();}
        if(v1.equals("") || v2.equals("") || v3.equals("") || v4.equals("") || v5.equals("") || v6.equals("")){
            Toast.makeText(NewEvent.this, "Fill In Proper Details", Toast.LENGTH_SHORT).show();
        }else {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            nameValuePairs.add(new BasicNameValuePair("f1", v1));
            nameValuePairs.add(new BasicNameValuePair("f2", v2));
            nameValuePairs.add(new BasicNameValuePair("f3", v3));
            nameValuePairs.add(new BasicNameValuePair("f4", v4));
            nameValuePairs.add(new BasicNameValuePair("f5", v5));
            nameValuePairs.add(new BasicNameValuePair("f6", v6));
            nameValuePairs.add(new BasicNameValuePair("f7", v7));
            nameValuePairs.add(new BasicNameValuePair("f8", sharedpreferences.getString("Membership", null)));


            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);


            //http post
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Log.e("log_tag", "tet1");
                HttpPost httppost = new HttpPost("http://irobinz.tk/ieee/newevent.php");
                Log.e("log_tag", "tet2 ");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();

                //Log.e("log_tag", "connection success ");
                Toast.makeText(getApplicationContext(), "Event Successfully Created", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //Log.e("log_tag", "Error in http connection " + e.toString());
                Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();

            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("log_tag", "Error " + e.toString());
            }

            try {

                JSONObject json_data = new JSONObject(result);
                CharSequence w = (CharSequence) json_data.get("re");
                //Toast.makeText(getApplicationContext(), w, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(NewEvent.this, MainActivity.class);
                startActivity(i);
                finish();
            } catch (JSONException e) {
                Log.e("log_tag", "Error " + e.toString());
            }
            Intent i = new Intent(NewEvent.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = new Intent(NewEvent.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
