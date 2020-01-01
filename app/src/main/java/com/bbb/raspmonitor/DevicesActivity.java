package com.bbb.raspmonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bbb.android_fontawesome_lib.FontAwesomeDrawable;
import com.bbb.raspmonitor.Adapters.DeviceListAdapter;
import com.bbb.raspmonitor.Db.DeviceList;
import com.bbb.raspmonitor.Db.SQLiteDB;
import com.bbb.raspmonitor.Listeners.DL_RecyclerItemClickListener;
import com.bbb.raspmonitor.Listeners.DL_RecyclerItemLongClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DevicesActivity extends AppCompatActivity implements DL_RecyclerItemClickListener, DL_RecyclerItemLongClickListener {


    private RecyclerView lvDevices;
    private FloatingActionButton btnAdd;

    private DeviceListAdapter deviceListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private SQLiteDB sqLiteDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        lvDevices = (RecyclerView) findViewById(R.id.lvContact);
        btnAdd = (FloatingActionButton) findViewById(R.id.add);

        linearLayoutManager = new LinearLayoutManager(this);
        deviceListAdapter = new DeviceListAdapter(this);
        deviceListAdapter.setOnItemClickListener(this);
        deviceListAdapter.setOnItemLongClickListener(this);

        lvDevices.setLayoutManager(linearLayoutManager);
        lvDevices.setAdapter(deviceListAdapter);

        FontAwesomeDrawable drawable = new FontAwesomeDrawable(this, R.string.fa_plus_solid, true, true);
        drawable.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        btnAdd.setImageDrawable(drawable);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevicesCrudActivity.start(DevicesActivity.this);
            }
        });





    }


    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    void loadData(){
        sqLiteDB = new SQLiteDB(this);

        List<DeviceList> contactList = new ArrayList<>();



        Cursor cursor = sqLiteDB.retrieve();
        DeviceList device;

        if (cursor.moveToFirst()) {
            do {

                device = new DeviceList();

                device.setDevice_Id(cursor.getInt(0));
                device.setDevice_Name(cursor.getString(1));
                device.setDevice_Ip_Adress(cursor.getString(2));
                device.setDevice_Port(cursor.getString(3));
                device.setDevice_Timeout(cursor.getString(4));
                device.setDevice_Username(cursor.getString(5));
                device.setDevice_Password(cursor.getString(6));

                contactList.add(device);
            }while (cursor.moveToNext());
        }

        deviceListAdapter.clear();
        deviceListAdapter.addAll(contactList);
    }

    @Override
    public void onItemClick(int position, View view) {
        MainActivity.start(DevicesActivity.this, deviceListAdapter.getItem(position));
    }


    @Override
    public void onItemLongClick(int position, View view) {
        DevicesCrudActivity.start(this, deviceListAdapter.getItem(position));
    }
}
