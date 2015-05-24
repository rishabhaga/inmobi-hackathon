package app.hack.inmobi.com.inmobihackapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.core.models.User;

import java.util.Calendar;
import java.util.Date;

import app.hack.inmobi.com.inmobihackapp.helper.UserPreferences;

/**
 * Created by webyog on 24/05/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        UserPreferences preferences = UserPreferences.getInstance(context);

        String time = preferences.getStringValue(UserPreferences.LAST_BURST_TIME, "0");

        long intTime = Long.valueOf(time);

        Date d = new Date();
        long currentTime = d.getTime();

        long diff = currentTime - intTime;

        int minutes = (int) getMinutes();
        int burst = (int) Math.ceil((minutes + 1 / 1440) * 288);
        int slot = (int) Math.ceil(burst / 36);
        int actualBurst = (burst) % 36 == 0 ? 36 : (burst % 36);

        if (diff > 30000) {

            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(context, notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Toast.makeText(context,"Test Alarm",Toast.LENGTH_SHORT).show();

            if (preferences.getBooleanValue(UserPreferences.ALERT_SHOWN, false)) {

                preferences.setBoolean(UserPreferences.ALERT_SHOWN,true);
                /*
                Intent i = new Intent();
                i.setClassName("app.hack.inmobi.com.inmobihackapp.activities.HomeActivity", "package app.hack.inmobi.com.inmobihackapp.activities.PlaySound");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);*/

                Toast.makeText(context,"Test Alarm",Toast.LENGTH_SHORT).show();

            }
        }

    }

    private double getMinutes() {

        Date d = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);


        double minutesDiff = d.getTime() - cal.getTime().getTime();
        minutesDiff = minutesDiff / (60 * 1000);
        return minutesDiff;

    }
}
