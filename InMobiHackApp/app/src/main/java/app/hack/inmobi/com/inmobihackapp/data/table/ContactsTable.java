package app.hack.inmobi.com.inmobihackapp.data.table;

/**
 * Created by webyog on 23/05/15.
 */
public class ContactsTable {

    public static final String TABLE_NAME = "contacts";

    //columns
    public static final String CONTACT_NUM = "contact_number";
    public static final String TIME_STAMP = "time_stamp";

    //Table Create query
    public static final String TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + CONTACT_NUM + " TEXT UNIQUE NOT NULL,"
                    + TIME_STAMP + " TEXT );";
}
