package d16124837.mydit.ie.mcsdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by james on 21/11/2016.
 * This class is a wrapper for the android sql lite database on the device.
 */

// TODO: The database open and close operations are blocking
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
        if (image.getTags() != null) {
            values.put(DBContract.Image.COLUMN_NAME_TAGS, arrayListToString(image.getTags()));
            values.put(DBContract.Image.COLUMN_NAME_DESCRIPTION, arrayListToString(image.getDescription()));
            values.put(DBContract.Image.COLUMN_NAME_CAPTION, image.getCaption());
            values.put(DBContract.Image.COLUMN_NAME_COLORS, arrayListToString(image.getColors()));
        }else{
            values.put(DBContract.Image.COLUMN_NAME_TAGS, "");
            values.put(DBContract.Image.COLUMN_NAME_DESCRIPTION, "");
            values.put(DBContract.Image.COLUMN_NAME_CAPTION, "");
            values.put(DBContract.Image.COLUMN_NAME_COLORS, "");
        }

        db.insert(DBContract.Image.TABLE_NAME, null, values);
    }

    /**
     * Gets all of the images in the database.
     */
    static ArrayList<ImageData> get(Context context){
        //get an instance of the database helper for the current context
        DBHelper dbHelper = new DBHelper(context);
        //get a read only database to query
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<ImageData> images = new ArrayList<>();

        //make the query for all rows in the table
        Cursor result = db.query(true, DBContract.Image.TABLE_NAME, null, null, null, null, null, null, null);

        //return an empty array if there are no results
        if (result.moveToFirst()){
            //otherwise unpack the data into the array
            for (int i = 0; i < images.size(); i++) {
                //add the imageData to the array
                images.add(cursorToImageData(result));
                result.moveToNext();
            }
        }

        return images;
    }

    /**
     * Get one row from the database by an image path
     */
    static ImageData get(Context context, String imagePath){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ImageData image;

        Cursor cursor = db.query(
                DBContract.Image.TABLE_NAME,
                null,
                DBContract.Image.COLUMN_NAME_PATH+" like "+imagePath,
                null,
                null,
                null,
                null);

        if (!cursor.moveToFirst()){
            image = null;
        }else {
            image = cursorToImageData(cursor);
        }

        return image;
    }

    /**
     * Update an entry in the database indexed by it's path
     */
    static void update(Context context, ImageData image){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Image.COLUMN_NAME_PATH, image.getPath());
        values.put(DBContract.Image.COLUMN_NAME_TAGS, arrayListToString(image.getTags()));
        values.put(DBContract.Image.COLUMN_NAME_DESCRIPTION, arrayListToString(image.getDescription()));
        values.put(DBContract.Image.COLUMN_NAME_CAPTION, image.getCaption());
        values.put(DBContract.Image.COLUMN_NAME_COLORS, arrayListToString(image.getColors()));

        db.update(DBContract.Image.TABLE_NAME,values,DBContract.Image.COLUMN_NAME_PATH+" like "+image.getPath(),null);
    }

    /**
     * unpack one cursor result row into an ImageData object.
     */
    private static ImageData cursorToImageData(Cursor cursor){
        ImageData image = new ImageData();

        image.setPath(cursor.getString(cursor.getColumnIndex(DBContract.Image.COLUMN_NAME_PATH)));
        String tags = cursor.getString(cursor.getColumnIndex(DBContract.Image.COLUMN_NAME_TAGS));
        if(tags != null && !tags.equals("")) {
            image.setTags(stringToArrayList(tags));
            image.setDescription(stringToArrayList(cursor.getString(cursor.getColumnIndex(DBContract.Image.COLUMN_NAME_DESCRIPTION))));
            image.setCaption(cursor.getString(cursor.getColumnIndex(DBContract.Image.COLUMN_NAME_CAPTION)));
            image.setColors(stringToArrayList(cursor.getString(cursor.getColumnIndex(DBContract.Image.COLUMN_NAME_COLORS))));
        }
        return image;
    }

    /**
     * parse a comma separated list into an arraylist of strings
     */
    private static ArrayList<String> stringToArrayList(String str){
        return new ArrayList<>(Arrays.asList(TextUtils.split(str,",")));
    }

    /**
     * generate a comma separated string from an arraylist of strings
     */
    private static String arrayListToString(ArrayList<String> array){
        return TextUtils.join(",",array);
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
                    DBContract.Image._ID+" INTEGER PRIMARY KEY, "+
                    DBContract.Image.COLUMN_NAME_PATH+" TEXT, "+
                    DBContract.Image.COLUMN_NAME_TAGS+" TEXT, "+
                    DBContract.Image.COLUMN_NAME_DESCRIPTION+" TEXT, "+
                    DBContract.Image.COLUMN_NAME_CAPTION+" TEXT, "+
                    DBContract.Image.COLUMN_NAME_COLORS+" TEXT);";

            Log.d("MCSDemo", create_table);
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
            static final String COLUMN_NAME_PATH = "PATH";
            static final String COLUMN_NAME_TAGS = "TAGS";
            static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
            static final String COLUMN_NAME_CAPTION = "CAPTION";
            static final String COLUMN_NAME_COLORS = "COLORS";
        }
    }
}
