package app.hack.inmobi.com.inmobihackapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.hack.inmobi.com.inmobihackapp.data.table.ActionTable;
import app.hack.inmobi.com.inmobihackapp.data.table.ContactsTable;
import app.hack.inmobi.com.inmobihackapp.data.table.SlotTable;

public class IMOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "twitter_login.db";
    public static final int DATABASE_VERSION = 1;

    public IMOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SlotTable.TABLE_CREATE);
        db.execSQL(ActionTable.TABLE_CREATE);
        db.execSQL(ContactsTable.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
