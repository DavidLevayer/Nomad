package android.nomadproject.com.nomad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Levayer on 21/03/15.
 */
public class MarkerDataSource {

    private Context mContext;

    // Database fields
    private SQLiteDatabase database;
    private DatabaseOpenHelper dbHelper;
    private String[] allColumns = {
            DatabaseOpenHelper.COLUMN_ID,
            DatabaseOpenHelper.COLUMN_NAME,
            DatabaseOpenHelper.COLUMN_INFO,
            DatabaseOpenHelper.COLUMN_LAT,
            DatabaseOpenHelper.COLUMN_LON};

    public MarkerDataSource(Context context) {
        dbHelper = new DatabaseOpenHelper(context);
        mContext = context;
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public CustomMarker createCustomMarker(CustomMarker marker) {

        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_NAME, marker.getName());
        values.put(DatabaseOpenHelper.COLUMN_INFO, marker.getInformation());
        values.put(DatabaseOpenHelper.COLUMN_LAT, marker.getLat());
        values.put(DatabaseOpenHelper.COLUMN_LON, marker.getLon());

        long insertId = database.insert(DatabaseOpenHelper.TABLE_COMMENTS, null,
                values);
        Cursor cursor = database.query(DatabaseOpenHelper.TABLE_COMMENTS,
                allColumns, DatabaseOpenHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        CustomMarker newMarker = cursorToCustomMarker(cursor);
        cursor.close();
        return newMarker;
    }

    private CustomMarker cursorToCustomMarker(Cursor cursor) {
        CustomMarker marker = new CustomMarker();
        marker.setId(cursor.getLong(0));
        marker.setName(cursor.getString(1));
        marker.setInformation(cursor.getString(2));
        marker.setLat(cursor.getFloat(3));
        marker.setLon(cursor.getFloat(4));
        return marker;
    }

    public void deleteCustomMarker(CustomMarker marker) {
        long id = marker.getId();
        database.delete(DatabaseOpenHelper.TABLE_COMMENTS, DatabaseOpenHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllMarkers(){
        database.delete(DatabaseOpenHelper.TABLE_COMMENTS, null, null);
    }

    public List<CustomMarker> getAllCustomMarkers() {
        List<CustomMarker> comments = new ArrayList<CustomMarker>();

        Cursor cursor = database.query(DatabaseOpenHelper.TABLE_COMMENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CustomMarker comment = cursorToCustomMarker(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }
}
