package app.hack.inmobi.com.inmobihackapp.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import app.hack.inmobi.com.inmobihackapp.asynctask.SetActionBurstAsyncTask;

public class MonitorService extends Service implements SensorEventListener {
    private Context mContext;
    SensorManager mSensorEventManager;
    Sensor mSensor;

    // BroadcastReceiver for handling ACTION_SCREEN_OFF.
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Check action just to be on the safe side.
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.v("shake mediator screen off", "trying re-registration");
                // Unregisters the listener and registers it again.
                mSensorEventManager.unregisterListener(MonitorService.this);
                mSensorEventManager.registerListener(MonitorService.this, mSensor,
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("shake service startup", "registering for shake");
        mContext = getApplicationContext();
        // Obtain a reference to system-wide sensor event manager.
        mSensorEventManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        // Get the default sensor for accel
        mSensor = mSensorEventManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Register for events.
        mSensorEventManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Register our receiver for the ACTION_SCREEN_OFF action. This will make our receiver
        // code be called whenever the phone enters standby mode.
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        // Unregister our receiver.
        unregisterReceiver(mReceiver);
        // Unregister from SensorManager.
        mSensorEventManager.unregisterListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't need a IBinder interface.
        return null;
    }

    public void onShake() {
        //Poke a user activity to cause wake?
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not used right now
    }

    //Used to decide if it is a shake
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

        final float alpha = 0.8f;

        float gravity[] = new float[3];
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.

        float[] linear_acceleration = new float[3];
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        if (Math.abs(linear_acceleration[0]) > 1.2 && Math.abs(linear_acceleration[1]) > 1.2 && Math.abs(linear_acceleration[2])>1.2) {
            SetActionBurstAsyncTask task = new SetActionBurstAsyncTask(getApplicationContext());
            task.execute();
            Log.v("sensor", "sensor change is verifying " + linear_acceleration[0] + " " + linear_acceleration[1] + " " + linear_acceleration[2]);
        }
    }
}
