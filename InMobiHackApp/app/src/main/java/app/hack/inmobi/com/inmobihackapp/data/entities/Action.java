package app.hack.inmobi.com.inmobihackapp.data.entities;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.ResultSet;
import java.sql.SQLException;

import app.hack.inmobi.com.inmobihackapp.data.table.ActionTable;

/**
 * Created by webyog on 23/05/15.
 */
public class Action implements Parcelable {

    private String slotNumber;
    private String burstSequence;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    private String startTime;

    public String getBurstSequence() {
        return burstSequence;
    }

    public void setBurstSequence(String burstSequence) {
        this.burstSequence = burstSequence;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Action(Cursor cursor) {
        this.slotNumber = cursor.getString(cursor.getColumnIndexOrThrow(ActionTable.SLOT_NUM));
        this.burstSequence = cursor.getString(cursor.getColumnIndexOrThrow(ActionTable.BURST_SEQUENCE));
        this.startTime = cursor.getString(cursor.getColumnIndexOrThrow(ActionTable.SLOT_START_TIME));
    }


    public Action(ResultSet result) {
        try {
            this.slotNumber = result.getString(ActionTable.SLOT_NUM);
            this.burstSequence = result.getString(ActionTable.BURST_SEQUENCE);
            this.startTime = result.getString(ActionTable.SLOT_START_TIME);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Action(){}


    public Action(String slotNumber, String burstSequence,String startTime) {
        this.slotNumber = slotNumber;
        this.burstSequence = burstSequence;
        this.startTime = startTime;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(slotNumber);
        parcel.writeString(burstSequence);

    }


}
