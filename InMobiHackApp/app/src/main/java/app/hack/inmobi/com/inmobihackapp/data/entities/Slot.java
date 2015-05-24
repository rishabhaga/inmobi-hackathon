package app.hack.inmobi.com.inmobihackapp.data.entities;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.ResultSet;
import java.sql.SQLException;

import app.hack.inmobi.com.inmobihackapp.data.table.SlotTable;

public class Slot implements Parcelable {

    private String slotNumber;
    private String threshold;
    private String startTime;
    private String endTime;

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        endTime = endTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public Slot(String slotNumber, String threshold, String startTime, String endTime) {
        this.slotNumber = slotNumber;
        this.threshold = threshold;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Slot(ResultSet result) {

        try {
            this.slotNumber = result.getString(SlotTable.SLOT_NUM);
            this.threshold = result.getString(SlotTable.THRESHOLD);
            this.startTime = result.getString(SlotTable.START_TIME);
            this.endTime = result.getString(SlotTable.END_TIME);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Slot(Cursor cursor) {
        this.slotNumber = cursor.getString(cursor.getColumnIndexOrThrow(SlotTable.SLOT_NUM));
        this.threshold = cursor.getString(cursor.getColumnIndexOrThrow(SlotTable.THRESHOLD));
        this.startTime = cursor.getColumnName(cursor.getColumnIndexOrThrow(SlotTable.START_TIME));
        this.endTime = cursor.getColumnName(cursor.getColumnIndexOrThrow((SlotTable.END_TIME)));
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(slotNumber);
        parcel.writeString(threshold);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
    }
}
