package app.hack.inmobi.com.inmobihackapp.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.internal.oauth.OAuth2Token;

import java.util.Date;

import app.hack.inmobi.com.inmobihackapp.R;
import app.hack.inmobi.com.inmobihackapp.helper.UserPreferences;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends ActionBarActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "pRcPgh4VZX5msGEBiXTilrs9b";
    private static final String TWITTER_SECRET = "l1cwWpVIZUg6ZgKahkKHuotUJwUuavq4Eev6E52WrewZozy7ZV";

    private DigitsAuthButton loginButton =  null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());
        setContentView(R.layout.activity_main);
        final UserPreferences preferences = UserPreferences.getInstance(this);


        String token = "";
        if((token = preferences.getStringValue(UserPreferences.USER_TOKEN,null))!= null){
            //TODO: take to home screen this will start the service which should note work if user if not logged in
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
        }

        loginButton = (DigitsAuthButton) findViewById(R.id.login);
        loginButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession digitsSession, String phoneNumber) {
                /*Toast.makeText(MainActivity.this,"Logged in",Toast.LENGTH_SHORT).show();*/


                preferences.setString(UserPreferences.USER_NUMBER,phoneNumber);
                preferences.setString(UserPreferences.USER_TOKEN,digitsSession.getAuthToken().toString());
                preferences.setString(UserPreferences.USER_ID,String.valueOf(digitsSession.getId()));

                //TODO: take to home activity
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                intent.putExtra("id",preferences.getStringValue(UserPreferences.USER_NUMBER,""));
                intent.putExtra("uuid",preferences.getStringValue(UserPreferences.USER_TOKEN,""));
                intent.putExtra("deviceId",new Date().getTime());
                startActivity(intent);


                /*Log.e("Login oauth",digitsSession.getAuthToken().toString());
                Log.e("Login id",digitsSession.getId()+"");
                Log.e("Login session ", digitsSession.toString());*/
            }

            @Override
            public void failure(DigitsException e) {
                //TODO: show message toe user
                Toast.makeText(MainActivity.this,"error occurred",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
