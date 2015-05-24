package app.hack.inmobi.com.inmobihackapp.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaRouter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.Calendar;

import app.hack.inmobi.com.inmobihackapp.AlarmReceiver;
import app.hack.inmobi.com.inmobihackapp.R;
import app.hack.inmobi.com.inmobihackapp.asynctask.InsertDummyValueActionTable;
import app.hack.inmobi.com.inmobihackapp.helper.UserPreferences;
import app.hack.inmobi.com.inmobihackapp.service.CheckService;
import app.hack.inmobi.com.inmobihackapp.service.MonitorService;

public class HomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserPreferences preferences = UserPreferences.getInstance(getApplicationContext());
                preferences.setString(UserPreferences.USER_TOKEN,null);

                HomeActivity.this.finish();
                Intent i = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        registerUser();

        UserPreferences preferences = UserPreferences.getInstance(getApplicationContext());

        if(preferences.getBooleanValue(UserPreferences.DUMMY_SET,false)) {
            new InsertDummyValueActionTable(getApplicationContext()).execute();
            preferences.setBoolean(UserPreferences.DUMMY_SET,true);
        }

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String uuid = intent.getStringExtra("uuid");
        String deviceId = "124e5u73242";

        RequestQueue queue = Volley.newRequestQueue(this);
        String url= "http://10.14.120.177:9000/rest-api/user/register";
        try {
            /*
            JSONObject jobj = new JSONObject();
            jobj.put("id",id);
            jobj.put("uuid",uuid);
            jobj.put("deviceId",deviceId);*/

        JSONObject jobj = new JSONObject(buildJsonPayload(id,uuid,deviceId));

            JsonObjectRequest request = new JsonObjectRequest(url,jobj,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {

                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        startAlarm();

        startService(new Intent(this, MonitorService.class));
    }

    private String buildJsonPayload( String id, String startTime, String burst ) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"id\":\"" + id + "\"" + ",");
        stringBuilder.append("\"uuid\":\"" + startTime + "\"" + ",");
        stringBuilder.append("\"deviceId\":\"" + burst + "\"");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }


    private void registerUser(){


    }

    private void  startAlarm(){
        Calendar cal = Calendar.getInstance();
        /*Intent intent = new Intent(this, CheckService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
*/
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pintent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(this.ALARM_SERVICE);
// schedule for every 30 seconds
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 2*1000, pintent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
