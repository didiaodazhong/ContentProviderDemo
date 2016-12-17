package com.peixing.contentproviderdemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

public class PersonService {
    private DBOpenHelper dbOpenHelper;

    public PersonService(Context context) {
        dbOpenHelper = new DBOpenHelper(context);

    }

    public void save(Person person) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL("insert into person(name,age) value(?,?)", new Object[]{person.getName(), person.getAge()});
    }

    public void delete(Integer _id) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        db.execSQL("delete from person where _id=?", new Object[]{_id});
    }

    public Person find(Integer _id) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from person where _id=?", new String[]{_id.toString()});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String age = cursor.getString(cursor.getColumnIndex("age"));
            Person person = new Person();
            person.set_id(id);
            person.setName(name);
            person.setAge(age);
            return person;
        }
        return null;
    }

    public List<Person> findAll() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        List<Person> persons = new ArrayList<Person>();
        Cursor cursor = db.rawQuery("select * from person", null);
        while (cursor.moveToNext()) {
            Person person = new Person();
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String age = cursor.getString(cursor.getColumnIndex("age"));

            person.set_id(id);
            person.setName(name);
            person.setAge(age);
            persons.add(person);
        }
        return persons;
    }

    public void update(Person person) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//        db.execSQL("insert into person(name,age) value(?,?)", new Object[]{person.getName(), person.getAge()});
        db.execSQL("update person set name=? where" + "id=?", new Object[]{person.getName(), person.get_id()});

    }

    public long getCount() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//        db.execSQL("insert into person(name,age) value(?,?)", new Object[]{person.getName(), person.getAge()});
        Cursor cursor = db.rawQuery("select count(*) from person", null);
        cursor.moveToFirst();
        return cursor.getLong(0);
    }
}
