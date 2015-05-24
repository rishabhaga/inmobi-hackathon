package app.hack.inmobi.com.inmobihackapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    private static SharedPreferences preference;
    private static UserPreferences preferenceStore;
    private Context mContext;

    private final String CREDENTIAL_STORE = "credential_store";

    public final static String USER_NUMBER = "user_number";
    public final static String USER_TOKEN = "user_token";
    public final static String USER_ID = "user_id";

    public final static String CURRENT_SLOT = "current_slot";
    public final static String BURST_VALUE = "burst_value";

    public final static String DUMMY_SET = "dummy_set";

    public final static String LAST_BURST_TIME = "last_burst_time";

    public final static String LAST_BURST = "last_burst";

    public final static String ALERT_SHOWN ="alert_shown";

    private UserPreferences(Context context) {
        mContext = context;
        preference = context.getSharedPreferences(CREDENTIAL_STORE,0);
    }

    public static UserPreferences getInstance(Context context) {
        if (preferenceStore == null)
            preferenceStore = new UserPreferences(context);

        return preferenceStore;
    }

    public void setString(String key,String value){
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringValue(String key,String defaultValue){
        return preference.getString(key,defaultValue);
    }

    public void setInt(String key,int value){
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getIntValue(String key,int defaultValue){
        return preference.getInt(key,defaultValue);
    }

    public void setBoolean(String key,boolean value){
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBooleanValue(String key,boolean defaultValue){
        return preference.getBoolean(key,defaultValue);
    }

}

