package app.hack.inmobi.com.inmobihackapp.data.table;

import android.content.ContentValues;

import app.hack.inmobi.com.inmobihackapp.data.entities.Action;
import app.hack.inmobi.com.inmobihackapp.data.entities.Slot;

/**
 * Created by webyog on 23/05/15.
 */
public class ActionTable {

    public static final String TABLE_NAME = "action_table";

    //columns
    public static final String SLOT_NUM = "slot_num";
    public static final String BURST_SEQUENCE = "burst_sequence";
    public static final String SLOT_START_TIME = "start_time";

    //Create Table Query
    public static final String TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + SLOT_NUM + " TEXT PRIMARY KEY,"
                    + BURST_SEQUENCE + " TEXT,"
                    +SLOT_START_TIME+ " TEXT);";

    public static ContentValues getContentValueObject(Action action) {
        ContentValues cv = new ContentValues();
        cv.put(SLOT_NUM, action.getSlotNumber());
        cv.put(BURST_SEQUENCE, action.getBurstSequence());
        cv.put(SLOT_START_TIME,action.getStartTime());
        return cv;
    }

}
