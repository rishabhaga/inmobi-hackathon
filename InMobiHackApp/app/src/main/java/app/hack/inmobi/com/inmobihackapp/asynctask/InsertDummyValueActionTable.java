package app.hack.inmobi.com.inmobihackapp.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import app.hack.inmobi.com.inmobihackapp.data.IMDBWrapper;

/**
 * Created by webyog on 24/05/15.
 */
public class InsertDummyValueActionTable extends AsyncTask<Void,Void,Void> {

    private Context mContext;

    public InsertDummyValueActionTable(Context context){
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        IMDBWrapper dbWrapper = new IMDBWrapper(mContext);
        dbWrapper.insertDummyValueInActionTable();

        return null;
    }
}
