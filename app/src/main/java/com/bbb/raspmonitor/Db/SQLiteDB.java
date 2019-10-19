package com.bbb.raspmonitor.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteDB extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Devicesk";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DevicesConstants.TABLE_NAME + " (" +
                    DevicesConstants.DEVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DevicesConstants.DEVICE_NAME + TEXT_TYPE + COMMA_SEP +
                    DevicesConstants.DEVICE_IP_ADRESS + TEXT_TYPE + COMMA_SEP +
                    DevicesConstants.DEVICE_PORT + TEXT_TYPE + COMMA_SEP +
                    DevicesConstants.DEVICE_TIMEOUT + TEXT_TYPE + COMMA_SEP +
                    DevicesConstants.DEVICE_USERNAME + TEXT_TYPE + COMMA_SEP +
                    DevicesConstants.DEVICE_PASSWORD + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DevicesConstants.TABLE_NAME;

    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public void create(DeviceList deviceList){
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DevicesConstants.DEVICE_NAME, deviceList.getDevice_Name());
        values.put(DevicesConstants.DEVICE_IP_ADRESS, deviceList.getDevice_Ip_Adress());
        values.put(DevicesConstants.DEVICE_PORT, deviceList.getDevice_Port());
        values.put(DevicesConstants.DEVICE_TIMEOUT, deviceList.getDevice_Timeout());
        values.put(DevicesConstants.DEVICE_USERNAME, deviceList.getDevice_Username());
        values.put(DevicesConstants.DEVICE_PASSWORD, deviceList.getDevice_Password());

        Log.d("TEST",values.toString());
        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DevicesConstants.TABLE_NAME,
                null,
                values);
    }

    public Cursor retrieve(){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DevicesConstants.DEVICE_ID,
                DevicesConstants.DEVICE_NAME,
                DevicesConstants.DEVICE_IP_ADRESS,
                DevicesConstants.DEVICE_PORT,
                DevicesConstants.DEVICE_TIMEOUT,
                DevicesConstants.DEVICE_USERNAME,
                DevicesConstants.DEVICE_PASSWORD
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DevicesConstants.DEVICE_NAME + " ASC";

        Cursor c = db.query(
                DevicesConstants.TABLE_NAME,                    // The table to query
                projection,                                 // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );
        if (c.moveToFirst()) {
            do {
                StringBuilder sb = new StringBuilder();
                int columnsQty = c.getColumnCount();
                for (int idx=0; idx<columnsQty; ++idx) {
                    sb.append(c.getString(idx));
                    if (idx < columnsQty - 1)
                        sb.append("; ");
                }
                Log.v("TEST", String.format("Row: %d, Values: %s", c.getPosition(),
                        sb.toString()));
            } while (c.moveToNext());
        }
        return c;
    }


    public void update(DeviceList deviceList){
        SQLiteDatabase db = getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DevicesConstants.DEVICE_NAME, deviceList.getDevice_Name());
        values.put(DevicesConstants.DEVICE_IP_ADRESS, deviceList.getDevice_Ip_Adress());
        values.put(DevicesConstants.DEVICE_PORT, deviceList.getDevice_Port());
        values.put(DevicesConstants.DEVICE_TIMEOUT, deviceList.getDevice_Timeout());
        values.put(DevicesConstants.DEVICE_USERNAME, deviceList.getDevice_Username());
        values.put(DevicesConstants.DEVICE_PASSWORD, deviceList.getDevice_Password());

        // Which row to update, based on the ID
        String selection = DevicesConstants.DEVICE_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(deviceList.getDevice_Id()) };

        int count = db.update(
                DevicesConstants.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void delete(int id){
        SQLiteDatabase db = getReadableDatabase();

        // Define 'where' part of query.
        String selection = DevicesConstants.DEVICE_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(id) };
        // Issue SQL statement.
        db.delete(DevicesConstants.TABLE_NAME, selection, selectionArgs);
    }

}
