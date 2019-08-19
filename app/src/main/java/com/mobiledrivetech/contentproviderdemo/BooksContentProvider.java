package com.mobiledrivetech.contentproviderdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

public class BooksContentProvider extends ContentProvider {
    private static final String TAG = "BookContentProvider";
    private static UriMatcher uriMatcher = null;
    private static final int BOOKS = 1;
    private static final int BOOK = 2;

    private ContentProviderDBHelper dbHelper;
    private SQLiteDatabase db;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(IProviderMetaData.AUTHORITY,
                IProviderMetaData.BookTableMetaData.TABLE_NAME, BOOKS);
        uriMatcher.addURI(IProviderMetaData.AUTHORITY,
                IProviderMetaData.BookTableMetaData.TABLE_NAME + "/#",
                BOOK);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new ContentProviderDBHelper(getContext());
        return (dbHelper == null) ? false : true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BOOKS:
                return IProviderMetaData.BookTableMetaData.CONTENT_LIST;
            case BOOK:
                return IProviderMetaData.BookTableMetaData.CONTENT_ITEM;
            default:
                throw new IllegalArgumentException("This is a unKnow Uri"
                        + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case BOOKS:
                db = dbHelper.getWritableDatabase();
                long rowId = db.insert(
                        IProviderMetaData.BookTableMetaData.TABLE_NAME,
                        IProviderMetaData.BookTableMetaData.BOOK_ID,
                        values);
                Uri insertUri = Uri.withAppendedPath(uri, "/" + rowId);
                Log.i(TAG, "insertUri:" + insertUri.toString());
                getContext().getContentResolver().notifyChange(uri, null);
                return insertUri;
            case BOOK:

            default:
                throw new IllegalArgumentException("This is a unKnow Uri"
                        + uri.toString());
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        db = dbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case BOOKS:
                return db.query(IProviderMetaData.BookTableMetaData.TABLE_NAME,
                        projection, selection, selectionArgs, null, null,
                        sortOrder);
            case BOOK:
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + " and " + where;
                }
                return db.query(IProviderMetaData.BookTableMetaData.TABLE_NAME,
                        projection, where, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("This is a unKnow Uri"
                        + uri.toString());
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case BOOKS:
                return db.delete(IProviderMetaData.BookTableMetaData.TABLE_NAME,
                        selection, selectionArgs);
            case BOOK:
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + " and " + where;
                }
                return db.delete(IProviderMetaData.BookTableMetaData.TABLE_NAME,
                        selection, selectionArgs);
            default:
                throw new IllegalArgumentException("This is a unKnow Uri"
                        + uri.toString());
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case BOOKS:
                return db.update(IProviderMetaData.BookTableMetaData.TABLE_NAME,
                        values, null, null);
            case BOOK:
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + " and " + where;
                }
                return db.update(IProviderMetaData.BookTableMetaData.TABLE_NAME,
                        values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("This is a unKnow Uri"
                        + uri.toString());
        }
    }
}
