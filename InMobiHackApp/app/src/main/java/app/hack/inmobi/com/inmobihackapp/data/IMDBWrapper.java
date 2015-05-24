package app.hack.inmobi.com.inmobihackapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import app.hack.inmobi.com.inmobihackapp.data.entities.Action;
import app.hack.inmobi.com.inmobihackapp.data.table.ActionTable;

/**
 * Created by webyog on 23/05/15.
 */
public class IMDBWrapper {

    private IMOpenHelper dbHelper;
    private Context mContext;

    public IMDBWrapper(Context context) {
        dbHelper = new IMOpenHelper(context);
        this.mContext = context;
    }

    public long insertSlotsIntoAction(Action action) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        long row = sqlite.insertWithOnConflict(ActionTable.TABLE_NAME, null, ActionTable.getContentValueObject(action), SQLiteDatabase.CONFLICT_IGNORE);
        sqlite.close();

        return row;
    }

    public void UpdateActionTableBurst(Action action) {

        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = "UPDATE OR IGNORE " + ActionTable.TABLE_NAME
                + " SET " + ActionTable.BURST_SEQUENCE + " = ? "
                + " WHERE " + ActionTable.SLOT_NUM + " = ?;";
        String arg[] = new String[]{action.getBurstSequence(), action.getSlotNumber()};

        sqlite.execSQL(sql, arg);
    }

    public void UpdateActionTableStartTime(Action action) {

        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = "UPDATE OR IGNORE " + ActionTable.TABLE_NAME
                + " SET " + ActionTable.SLOT_START_TIME + " = ? "
                + " WHERE " + ActionTable.SLOT_NUM + " = ?;";
        String arg[] = new String[]{action.getStartTime(), action.getSlotNumber()};

        sqlite.execSQL(sql, arg);
    }

    public void insertDummyValueInActionTable(){

        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        for(int i=1;i<=8;i++) {
            Action action = new Action(String.valueOf(i),"","");
            long row = sqlite.insertWithOnConflict(ActionTable.TABLE_NAME, null, ActionTable.getContentValueObject(action), SQLiteDatabase.CONFLICT_IGNORE);
            sqlite.close();
        }
    }

   /* public String getBurstData(String slotNumber) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        String sql = "SELECT " + ActionTable.BURST_SEQUENCE
                + "FROM " + ActionTable.TABLE_NAME
                + "WHERE " + ActionTable.SLOT_NUM + " = ?;";
        String arg[] = new String[]{slotNumber};

        sqlite.execSQL(sql, arg);
    }*/
}
