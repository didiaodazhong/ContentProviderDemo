package com.peixing.contentproviderdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.regex.Matcher;

/**
 * Created by peixing on 2016/12/17.
 */

public class MyContentProvider extends ContentProvider {
    public static final int SUCCESS = 1;
    private static final String TAG = "MyContentProvider";
    private static final String TABLE_NAME = "person";
    private DBOpenHelper dbOpenHelper;
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //    static {
//        mUriMatcher.addURI("com.itheima.db", "account", SUCCESS);
//    }
    private static final int PERSONS = 1;
    private static final int PERSON = 2;

    static {
        mUriMatcher.addURI("com.peixing.contentproviderdemo", "person", PERSONS);
        mUriMatcher.addURI("com.peixing.contentproviderdemo", "person/#", PERSON);
    }

    @Override
    public boolean onCreate() {

        dbOpenHelper = new DBOpenHelper(this.getContext());
        return true;
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
      /*  SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case PERSONS:
                return db.query("person", projection, selection, selectionArgs,
                        null, null, sortOrder);
            case PERSON:
                Log.i(TAG, "query: " + ContentUris.parseId(uri));
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + " and " + where;
                }
                return db.query("person", projection, where, selectionArgs, null,
                        null, sortOrder);
            default:
                throw new IllegalArgumentException(" Query Unknown Uri:" + uri.toString());
        }*/

        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        //UriMatcher中的一个重要方法，判断两个Uri是否相匹配
        switch (mUriMatcher.match(uri)) {
            case PERSONS:
                /*
                 * 当Uri为“content：//com.faith.providers.personprovider/person”时，触发这个模块
                 * 是指查询person表中所有的数据信息（当然条件是selection）
                 */
                return database.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case PERSON:
                /*
                 * 当Uri为“content：//com.faith.providers.personprovider/person/#”时，触发这个模块
                 * 是指查询当id = #(所代表的数字)时的那一条数据的具体信息
                 * 需要添加一个条件（在原先的selection条件的基础上添加）
                 */
                long id = ContentUris.parseId(uri);
//                String where = TABLE_COLUMN_ID + "=" + id;
                String where = "_id" + "=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = where + " and " + selection;
                }
                return database.query(TABLE_NAME, projection, where, selectionArgs, null, null, sortOrder);
            default:
                //均不匹配的时候，抛出异常
                throw new IllegalArgumentException("Unknown Uri : " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case PERSONS:

                return "vnd.android.cursor.dir/person";
            case PERSON:

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
                Log.i(TAG, "insert: " + rowid);
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
