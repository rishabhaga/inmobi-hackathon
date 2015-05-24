package app.hack.inmobi.com.inmobihackapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by webyog on 24/05/15.
 */
public class CheckService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("service","runing service");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
