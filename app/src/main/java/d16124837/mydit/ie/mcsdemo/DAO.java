package d16124837.mydit.ie.mcsdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by james on 21/11/2016.
 * This class is a wrapper for the android sql lite database on the device.
 */

// TODO: The database open and close operations are blocking
// TODO: Finish defining database table
class DAO {
    /**
     * Inserts one image into the database
     * @param image this is the data for the image to be added to the database.
     */
    static void insert(Context context, ImageData image){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Image.COLUMN_NAME_PATH, image.getPath());

        db.insert(DBContract.Image.TABLE_NAME, null, values);
    }

    /**
     * This is the database helper class for the sqllite database.
     */
    private static class DBHelper extends SQLiteOpenHelper{
        static final int DATABASE_VERSION = 1;
        static final String DATABASE_NAME = "mcsdemo";

        DBHelper(Context context){
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

    /**
     * This class represents the database schema.
     */
    private static final class DBContract {
        // this inner class represents the schema information for a column in the database
        static class Image implements BaseColumns {
            static final String TABLE_NAME = "IMAGE";
            static final String COLUMN_NAME_PATH = "IMAGE";
            static final String COLUMN_NAME_ = "IMAGE";
        }
    }
}
