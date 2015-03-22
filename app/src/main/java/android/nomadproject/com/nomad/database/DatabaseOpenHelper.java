package android.nomadproject.com.nomad.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by David Levayer on 21/03/15.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_COMMENTS = "comments";
    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_INFO = "information";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LON = "longitude";

    private static final String DATABASE_NAME = "markers.db";
    private static final int DATABASE_VERSION = 1;

    private Context mContext;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_COMMENTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, "+ COLUMN_INFO+" text, "+ COLUMN_LAT+" float, "+ COLUMN_LON
            + " float);";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(mContext,"Update from "+oldVersion+" to "+newVersion,Toast.LENGTH_LONG).show();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);
    }
}

