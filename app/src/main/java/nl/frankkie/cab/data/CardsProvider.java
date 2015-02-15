package nl.frankkie.cab.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by FrankkieNL on 15-2-2015.
 */
public class CardsProvider extends ContentProvider {

    /*
     *
     */
    //content://nl.frankkie.cab/blackcard/ (LIST)
    public static final int BLACKCARD = 100;
    //content://nl.frankkie.cab/blackcard/ID (ITEM)
    public static final int BLACKCARD_ID = 101;
    //content://nl.frankkie.cab/whitecard/ (LIST)
    public static final int WHITECARD = 200;
    //content://nl.frankie.cab/whitecard/ID (ITEM)
    public static final int WHITECARD_ID = 201;


    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
