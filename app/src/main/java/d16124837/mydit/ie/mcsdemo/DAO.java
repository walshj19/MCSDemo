package d16124837.mydit.ie.mcsdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by james on 21/11/2016.
 * This class is a wrapper for the android sql lite database on the device.
 */

// TODO: The database open and close operations are blocking
// TODO: Finish defining database table
public class DAO {
    private class DBHelper extends SQLiteOpenHelper{
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "mcsdemo";

        public DBHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        //this is called when the database is created for the first time
        public void onCreate(SQLiteDatabase db) {
            String create_table = "CREATE TABLE "+
                    DBContract.Image.TABLE_NAME+" ("+
                    DBContract.Image._ID+" INTEGER PRIMARY KEY,"+
                    DBContract.Image.COLUMN_NAME_PATH+" TEXT )";

            db.execSQL(create_table);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String drop_tables = "DROP TABLE IF EXISTS "+DBContract.Image.TABLE_NAME;
            db.execSQL(drop_tables);
            onCreate(db);
        }
    }
}
