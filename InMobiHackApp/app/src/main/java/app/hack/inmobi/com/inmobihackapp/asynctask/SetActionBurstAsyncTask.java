package app.hack.inmobi.com.inmobihackapp.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.hack.inmobi.com.inmobihackapp.data.IMDBWrapper;
import app.hack.inmobi.com.inmobihackapp.data.entities.Action;
import app.hack.inmobi.com.inmobihackapp.helper.UserPreferences;

/**
 * Created by webyog on 24/05/15.
 */
public class SetActionBurstAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;

    public SetActionBurstAsyncTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        int minutes = (int) getMinutes();
        int burst = (int) Math.ceil((minutes + 1 / 1440) * 288);
        int slot = (int) Math.ceil(burst / 36);
        int actualBurst = (burst) % 36 == 0 ? 36 : (burst % 36);


        Date d = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        long slotStartTime = date.getTime() + ((slot - 1) * 180);
        String burstData = null;

       /* if(actualBurst == 1){
            burstData = "000000000000000000000000000000000000";
        }
        //TODO: burst value*/
        Action action = new Action();


        UserPreferences preferences = UserPreferences.getInstance(mContext);

        preferences.setString(UserPreferences.LAST_BURST_TIME,d.getTime()+"");

        burstData = preferences.getStringValue(UserPreferences.BURST_VALUE,"");

        if (burstData.isEmpty()) {
            burstData = String.valueOf(actualBurst);
        } else {
            burstData = burstData + "," + actualBurst;
        }

        String lastSlot = preferences.getStringValue(UserPreferences.CURRENT_SLOT,"");
        if(!lastSlot.equals(String.valueOf(slot)) && !lastSlot.isEmpty()){
            action.setSlotNumber(lastSlot);
            action.setBurstSequence(burstData);
            action.setStartTime(String.valueOf(date.getTime() + ((Integer.valueOf(lastSlot) - 1) * 180)));
            IMDBWrapper dbWrapper = new IMDBWrapper(mContext);
            dbWrapper.UpdateActionTableBurst(action);
            dbWrapper.UpdateActionTableStartTime(action);

        }
        preferences.setString(UserPreferences.CURRENT_SLOT,String.valueOf(slot));

        preferences.setString(UserPreferences.BURST_VALUE,burstData);

      /*  int lastBurst = preferences.getIntValue(UserPreferences.LAST_BURST,0);

        if(lastBurst+1 != actualBurst){
            //TODO: set the previous burst as 0
        }
        if(actualBurst == 36){
            preferences.setInt(UserPreferences.LAST_BURST,0);
            //TODO: write burst to DB
        }else{
            preferences.setInt(UserPreferences.LAST_BURST,actualBurst);
        }*//*

        IMDBWrapper dbWrapper = new IMDBWrapper(mContext);
        dbWrapper.insertSlotsIntoAction(action);*/

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
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
