package d16124837.mydit.ie.mcsdemo;

import android.provider.BaseColumns;

/**
 * Created by James on 21-Nov-16.
 * This class represents the database schema and is the single point at which it is represented.
 */

public final class DBContract {
    // this class should never be instantiated
    private DBContract(){}

    // this inner class represents the schema information for a column in the database
    public static class Image implements BaseColumns{
        public static final String TABLE_NAME = "IMAGE";
        public static final String COLUMN_NAME_PATH = "IMAGE";
        public static final String COLUMN_NAME_ = "IMAGE";
    }
}
