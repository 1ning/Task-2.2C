package com.example.myapplication2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication2.Database.DatabaseHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MainActivity2 extends AppCompatActivity implements SensorEventListener {
    private DatabaseHelper dbHelper;
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView mTvLight;
    private TextView time;
    private Button Button1;
    String b="18";
    String data;
    float temperature=18;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mTvLight=findViewById(R.id.textView1);
        time=findViewById(R.id.time);
        Button1=findViewById(R.id.submit);
        dbHelper = new DatabaseHelper(this, "database2", null, 1);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        Startthread();
        Button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                db = dbHelper.getWritableDatabase();
                data=time.getText().toString();
                ContentValues values = new ContentValues();
                values.put("temperature",temperature);
                values.put("date", data);
                long result = db.insert("tem", null, values);
                db.close();
                dbHelper.close();
                if (result > 0) {
                    Toast.makeText(MainActivity2.this, "date successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    //开启一个子线程
    private void Startthread(){
        new Thread(){
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                        Message message=new Message();
                        message.what=1;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while (true);
            }
        }.start();
    }

    //在主线程中进行数据处理
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    long time1 = System.currentTimeMillis();
                    CharSequence format = DateFormat.format("hh:mm:ss yyyy-MM-dd", time1);
                    time.setText(format);
                    break;
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        //添加监听器
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            //解除注册,不再接收任何传感器的更新。
            sensorManager.unregisterListener(this,sensor);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_AMBIENT_TEMPERATURE){
             temperature=event.values[0];
            mTvLight.setText(String.valueOf(temperature)+"°C");
        }else {

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}