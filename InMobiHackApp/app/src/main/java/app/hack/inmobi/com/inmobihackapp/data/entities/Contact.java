package app.hack.inmobi.com.inmobihackapp.data.entities;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.ResultSet;
import java.sql.SQLException;

import app.hack.inmobi.com.inmobihackapp.data.table.ContactsTable;

/**
 * Created by webyog on 23/05/15.
 */
public class Contact implements Parcelable {

    private String number;
    private String timeStamp;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Contact(Cursor cursor) {
        this.number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsTable.CONTACT_NUM));
        this.timeStamp = cursor.getString(cursor.getColumnIndexOrThrow(ContactsTable.TIME_STAMP));
    }

    public Contact(ResultSet result) {
        try {
            this.number = result.getString(ContactsTable.CONTACT_NUM);
            this.timeStamp = result.getString(ContactsTable.TIME_STAMP);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(number);
        parcel.writeString(timeStamp);
    }
}
