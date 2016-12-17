package com.peixing.contentproviderdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.regex.Matcher;

/**
 * Created by peixing on 2016/12/17.
 */

public class MyContentProvider extends ContentProvider {
    public static final int SUCCESS = 1;
    private DBOpenHelper dbOpenHelper;
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //    static {
//        mUriMatcher.addURI("com.itheima.db", "account", SUCCESS);
//    }
    private static final int PERSONS = 1;
    private static final int PERSON = 2;

    static {
        mUriMatcher.addURI("com.peixing.contentproviderdemo", "person", PERSON);
        mUriMatcher.addURI("com.peixing.contentproviderdemo", "person/#", PERSONS);
    }

    @Override
    public boolean onCreate() {

        dbOpenHelper = new DBOpenHelper(this.getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        /*SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        switch (mUriMatcher.match(uri)) {
            case PERSON:
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + "and" + where;
                }
                return db.query("person", projection, where, selectionArgs, null, null, sortOrder);
            case PERSONS:
                return db.query("person", projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("unknown URI" + uri.toString());
        }*/
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        switch (mUriMatcher.match(uri)) {
            case PERSONS:
                return db.query("person", projection, selection, selectionArgs,
                        null, null, sortOrder);

            case PERSON:
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + " and " + where;
                }
                return db.query("person", projection, where, selectionArgs, null,
                        null, sortOrder);

            default:
                throw new IllegalArgumentException("Unknown Uri:" + uri.toString());
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case PERSON:

                return "vnd.android.cursor.dir/person";
            case PERSONS:

                return "vnd.android.cursor.item/person";
            default:
                throw new IllegalArgumentException("Unknown Uri:" + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case PERSONS:
            case PERSON:

//            db.delete("person", selection, selectionArgs);
                long rowid = db.insert("person", "name", values);
                Uri insertUri = ContentUris.withAppendedId(uri, rowid);
                getContext().getContentResolver().notifyChange(uri, null);
                return insertUri;
            default:
                throw new IllegalArgumentException("Unknown Uri：" + uri.toString());
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code = mUriMatcher.match(uri);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int count = 0;
        switch (mUriMatcher.match(uri)) {
//            DBOpenHelper dbOpenHelper = new DBOpenHelper(getContext());
            case PERSONS:
                count = db.delete("person", selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            case PERSON:
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + "and" + where;
                }
                count = db.delete("person", where, selectionArgs);
                return count;
            default:
                throw new IllegalArgumentException("口令不正确！");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int count = 0;
        switch (mUriMatcher.match(uri)) {
            case PERSONS:
                count = db.update("person", values, selection, selectionArgs);
                return count;
            case PERSON:
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + " and " + where;
                }
                count = db.update("person", values, where, selectionArgs);
                return count;
            default:
                throw new IllegalArgumentException("Unknown Uri:" + uri.toString());
        }
    }
}
