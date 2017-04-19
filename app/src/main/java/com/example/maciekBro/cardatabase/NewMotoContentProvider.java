package com.example.maciekBro.cardatabase;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by RENT on 2017-03-30.
 */

public class NewMotoContentProvider extends ContentProvider {


    public static final String AUTHORITY = "com.example.maciekBro.cardatabase";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private MotoDatabaseOpenHelper openHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final int CAR_MULTIPLE_ITEM = 1;
    public static final int CAR_SINGLE_ITEM = 2;

    static {    //do rozróżniania uri

        uriMatcher.addURI(AUTHORITY, CarsTableContract.TABLE_NAME, CAR_MULTIPLE_ITEM);      //content provider reaguje na te uri
        uriMatcher.addURI(AUTHORITY, CarsTableContract.TABLE_NAME + "/#", CAR_SINGLE_ITEM);

    }


    @Override
    public boolean onCreate() {
        openHelper = new MotoDatabaseOpenHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase readableDatabase = openHelper.getReadableDatabase();

        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case CAR_SINGLE_ITEM: {
                cursor = readableDatabase.query(CarsTableContract.TABLE_NAME, projection, CarsTableContract._ID + " = ? "
                        , new String[]{uri.getLastPathSegment()}, null, null, sortOrder);      /*tutaj podajemy co beedzie pod znakiem zapytania*/
                break;
            }
            case CAR_MULTIPLE_ITEM: {
                cursor = readableDatabase.query(CarsTableContract.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);      //powiadamianie o zmianach
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        String type = null;
        switch (uriMatcher.match(uri)) {
            case CAR_SINGLE_ITEM: {
                type = "vnd.android.cursor.item/vnd.com.example.maciekBro.cardatabase.cars";    //bo tak chce androidy
                break;
            }
            case CAR_MULTIPLE_ITEM: {
                type = "vnd.android.cursor.dir/vnd.com.example.maciekBro.cardatabase.cars";
                break;
            }
        }

        return type;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = -1;
        switch (uriMatcher.match(uri)) {
            case CAR_MULTIPLE_ITEM: {
                id = openHelper.getWritableDatabase().insert(CarsTableContract.TABLE_NAME, null, values);
                break;
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);   //powiadomienie o zmianach w tabeli
        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase writableDatabase = openHelper.getWritableDatabase();
        int deletedItems = 0;
        if (uriMatcher.match(uri) == CAR_SINGLE_ITEM) {
            deletedItems = writableDatabase.delete(CarsTableContract.TABLE_NAME, CarsTableContract._ID + " = ? ", new String[]{uri.getLastPathSegment()});   //getLastPathSegment to ten nasz ostatni id, ktory wpisujemy w uri i chcemy usunąć
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return deletedItems;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
