package in.vesely.eclub.yodaqa.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vesely on 6/17/15.
 */
public class SearchItem {
    public static abstract class SearchEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_MDATE = "mdate";
        public static final String COLUMN_NAME_TEXT = "textt";
    }

    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_SEARCH_TABLE =
            "CREATE TABLE " + SearchEntry.TABLE_NAME + " (" +
                    SearchEntry._ID + " INTEGER PRIMARY KEY," +
                    SearchEntry.COLUMN_NAME_TEXT + " TEXT UNIQUE" + COMMA_SEP +
                    SearchEntry.COLUMN_NAME_MDATE + " INTEGER" +
                    " )";

    public static final String SQL_DELETE_SEARCH_TABLE =
            "DROP TABLE IF EXISTS " + SearchEntry.TABLE_NAME;

    public static Collection<String> select(SQLiteDatabase db) {
        Cursor cursor = db.query(SearchEntry.TABLE_NAME,
                new String[]{SearchEntry.COLUMN_NAME_TEXT},
                null,
                null,
                null,
                null,
                SearchEntry.COLUMN_NAME_MDATE + " DESC");
        List<String> items = new LinkedList<>();
        while (cursor.moveToNext()) {
            items.add(cursor.getString(cursor.getColumnIndex(SearchEntry.COLUMN_NAME_TEXT)));
        }
        cursor.close();
        return items;
    }

    public static void insert(String text, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(SearchEntry.COLUMN_NAME_MDATE, new Date().getTime());
        cv.put(SearchEntry.COLUMN_NAME_TEXT, text);
        db.insertWithOnConflict(SearchEntry.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
