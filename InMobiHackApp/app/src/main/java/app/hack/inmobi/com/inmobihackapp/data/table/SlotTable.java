package app.hack.inmobi.com.inmobihackapp.data.table;

import android.content.ContentValues;

import app.hack.inmobi.com.inmobihackapp.data.entities.Slot;

/**
 * Created by webyog on 23/05/15.
 */
public class SlotTable {

    public static final String TABLE_NAME = "slot";

    //columns
    public static final String SLOT_NUM = "slot_num";
    public static final String THRESHOLD = "threshold";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";

    //Create Table query
    public static final String TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + SLOT_NUM + " TEXT PRIMARY KEY,"
                    + THRESHOLD + " TEXT,"
                    + START_TIME + " TEXT,"
                    + END_TIME + " TEXT);";

    public static ContentValues getContentValueObject(Slot slot) {
        ContentValues cv = new ContentValues();
        cv.put(SLOT_NUM, slot.getSlotNumber());
        cv.put(THRESHOLD, slot.getThreshold());
        cv.put(START_TIME, slot.getStartTime());
        cv.put(END_TIME, slot.getEndTime());
        return cv;
    }

}
