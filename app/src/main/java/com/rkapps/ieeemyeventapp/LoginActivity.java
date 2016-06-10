package com.rkapps.ieeemyeventapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> personList;
    String myJSON;
    JSONArray peoples = null;
    List<IEEEEvents> events = new ArrayList<IEEEEvents>();
    public SharedPreferences sharedpreferences;
    private static final String TAG_RESULTS="result";
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
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        Button btn = (Button)findViewById(R.id.buttonlogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("log_tag", "button pressed");
                final EditText et1 = (EditText)findViewById(R.id.et1);
                final EditText et2 = (EditText)findViewById(R.id.et2);

                if(!Authentication.injection(et1.getText().toString() + et2.getText().toString())){
                    if(!Authentication.hyperInjection(et1.getText().toString() + et2.getText().toString())){

                       Toast.makeText(getApplicationContext(), "Logging in..", Toast.LENGTH_SHORT).show();
                        getData(et1.getText().toString(), et2.getText().toString());
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Enter correct details, Please!", Toast.LENGTH_SHORT).show();

                }
                else{
                    //TOAST Enter correct details.
                    Toast.makeText(getApplicationContext(), "Enter correct details, Please!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }



    public void getData(String email, String pass){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                Log.e("log_tag","Still Nope" + params[0]);
				//HTTP Login Page
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://irobinz.tk/ieee/login.php?email=" + params[0] + "&pass=" + params[1]);

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
                int flag = 0;
                try {/*
                    final EditText et1 = (EditText)findViewById(R.id.et1);
                    final EditText et2 = (EditText)findViewById(R.id.et2);*/
                    JSONObject jsonObj = new JSONObject(myJSON);
                    peoples = jsonObj.getJSONArray(TAG_RESULTS);
                    for (int i = 0; i < peoples.length(); i++) {
                        JSONObject c = peoples.getJSONObject(i);
                        if(c.getString("Message").equals("1")){
                            flag = 1;
                            sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();


                            editor.putString("Email", c.getString("Email"));
                            editor.putString("Name", c.getString("Name"));
                            editor.putString("Section", c.getString("Section"));
                            editor.putString("SubSection", c.getString("SubSection"));
                            editor.putString("Membership", c.getString("Membership"));
                            editor.commit();
                            Log.e("log_tag", "Downloaded and installed");
                            break;
                        }else{
                            flag =0;
                            Log.e("log_tag", "Cannot get vals");
                        }
                    }//*/


                    if(flag == 1){
                        Log.e("log_tag", "new activity");
                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Log.e("log_tag","Still Nope" + e.toString());
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        try {
            pass = Encryption.word(pass);

          //  Toast.makeText(getApplicationContext(), "Pass encrypted, Pass : " + pass, Toast.LENGTH_SHORT).show();
            g.execute(email, pass); // ADDED PARAMS
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Failure: Cannot encrypt user details", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = new Intent(LoginActivity.this,FullscreenActivity.class);
        startActivity(i);
        finish();
    }
}
