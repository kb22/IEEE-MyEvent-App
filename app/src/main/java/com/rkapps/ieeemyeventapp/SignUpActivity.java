package com.rkapps.ieeemyeventapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    private EditText dob;
    private SimpleDateFormat dateFormatter;
    public SharedPreferences sharedpreferences;
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 0;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
            }
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
                ;
            }
            return false;
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        dob = (EditText) findViewById(R.id.dob);
        dob.setInputType(InputType.TYPE_NULL);
        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    final DatePicker timePicker = new DatePicker(SignUpActivity.this);
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
                            dob.setText(year + "-" + (month+1) + "-" + day);
                        }
                    });
                    builder.show();
                }
            }
        });

        Button buttonSignup = (Button) findViewById(R.id.buttonsignup);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.ed1);
                String v1 = editText.getText().toString();
                EditText editText2 = (EditText) findViewById(R.id.dob);
                String v2 = editText2.getText().toString();
                EditText editText3 = (EditText) findViewById(R.id.ed3);
                String v3 = editText3.getText().toString();
                EditText editText4 = (EditText) findViewById(R.id.ed4);
                String v4 = editText4.getText().toString();
                EditText editText5 = (EditText) findViewById(R.id.ed5);
                String v5 = editText5.getText().toString();
                EditText editText6 = (EditText) findViewById(R.id.ed6);
                String v6 = editText6.getText().toString();
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                String v7 = spinner.getSelectedItem().toString();
                Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
                String v8 = spinner2.getSelectedItem().toString();

                if(v4.equals(v5)){
                }else{
                    Toast.makeText(SignUpActivity.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this,FullscreenActivity.class);
                    startActivity(i);
                    finish();
                }

                sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("Email", v3);
                editor.putString("Name", v1);
                editor.putString("Section", v7);
                editor.putString("SubSection", v8);
                editor.putString("Membership", v6);
                editor.commit();

                Log.e("log_tag","Section in signup: " + v7 + " Sub: " + v8);
                new createEvent().execute(v1,v2,v3,v4,v6,v7,v8);
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(0);
    }


    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    class createEvent extends AsyncTask<String, String, String> {
        boolean flag = false;
        @Override
        protected String doInBackground(String... args) {

            String result = null;
            InputStream is = null;
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            //SharedPreferences.Editor editor = sharedpreferences.edit();

            nameValuePairs.add(new BasicNameValuePair("f1", args[0]));
            nameValuePairs.add(new BasicNameValuePair("f2", args[1]));
            nameValuePairs.add(new BasicNameValuePair("f3", args[2]));
            nameValuePairs.add(new BasicNameValuePair("f4", args[3]));
            nameValuePairs.add(new BasicNameValuePair("f5", args[4]));
            nameValuePairs.add(new BasicNameValuePair("f6", args[5]));
            nameValuePairs.add(new BasicNameValuePair("f7", args[6]));

            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
            //http post
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Log.e("log_tag", "tet1");
                HttpPost httppost = new HttpPost("http://irobinz.tk/ieee/signup.php");
                Log.e("log_tag", "tet2 ");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                    Intent i = new Intent(getBaseContext(),MainActivity.class);
                    startActivity(i);
                }
                is.close();

                result=sb.toString();
                JSONObject json_data = new JSONObject(result);

                CharSequence w = (CharSequence) json_data.get("re");
                Log.e("log_tag", "connection success ");
                flag = true;
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
                flag = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            Log.e("log_tag", "Result Signup: " + result);
            if(flag == true) {
                Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(i);
            }
            else {
                Toast.makeText(getApplicationContext(), "Could Not sign up", Toast.LENGTH_SHORT).show();
                sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SignUp Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.rkapps.ieeemyeventapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SignUp Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.rkapps.ieeemyeventapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = new Intent(SignUpActivity.this,FullscreenActivity.class);
        startActivity(i);
        finish();
    }
}
