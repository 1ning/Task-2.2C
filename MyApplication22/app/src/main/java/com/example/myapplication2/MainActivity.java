package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import com.example.myapplication2.Database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    FloatingActionButton create;
    private SQLiteDatabase db;
    private RecyclerView mRecyclerView;
    public List<tem>list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this, "database2", null, 1);
        db = dbHelper.getWritableDatabase();
        mRecyclerView = findViewById(R.id.recycler_view);
        create=findViewById(R.id.Create);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Get the data you need from the database
        List<tem> list = queryAllTrucksData();
        //Initialise the recycleview after getting the data
        if (this != null) {
            TemAdapter temAdapter = new TemAdapter(this, list);
            mRecyclerView.setAdapter(temAdapter);
        }
        //Skip to order creation activity
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });
    }



    public List<tem> queryAllTrucksData(){
        Cursor cursor = db.query("tem",null,null,null,null,null,null,null);
        List<tem> list = new ArrayList<>();
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Float context = cursor.getFloat(cursor.getColumnIndexOrThrow("temperature"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                tem model = new tem();
                model.context=context;
                model.date=date;
                list.add(model);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return list;
    }

}
