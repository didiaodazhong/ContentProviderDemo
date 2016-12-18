package com.peixing.contentproviderdemo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button insert;
    private Button delete;
    private Button update;
    private Button find;
//    private RecyclerView recyclerview;
    ContentResolver contentResolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insert = (Button) findViewById(R.id.insert);
        delete = (Button) findViewById(R.id.delete);
        update = (Button) findViewById(R.id.update);
        find = (Button) findViewById(R.id.find);
//        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
//        insert.setOnClickListener(this);
//        delete.setOnClickListener(this);
//        update.setOnClickListener(this);
//        find.setOnClickListener(this);
//        contentResolver = getContentResolver();
//        ContentResolver contentResolver = getContentResolver();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert:
                Uri insertUri = Uri.parse("content://com.peixing.contentproviderdemo/person");
                for (int i = 0; i < 13; i++) {
                    ContentValues values = new ContentValues();
                    values.put("name", "wangkuifeng" + i);
                    values.put("age", (i + 5) + "");
                    Uri uri = contentResolver.insert(insertUri, values);
                }
                break;
            case R.id.delete:

                break;
            case R.id.update:

                break;
            case R.id.find:
                Uri selectUri = Uri.parse("content://com.peixing.contentproviderdemo/person");
                Cursor cursor = contentResolver.query(selectUri, null, null, null, null);
                ArrayList<HashMap<String, String>> persons = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> person;
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        person = new HashMap<>();
                        person.put("name", cursor.getString(1));
                        person.put("age", cursor.getString(2));
                        persons.add(person);
                    }
                    cursor.close();
                    PersonAdapter personAdapter = new PersonAdapter(MainActivity.this, persons);
//                    recyclerview.setAdapter(personAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "当前数据库为空！", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}
