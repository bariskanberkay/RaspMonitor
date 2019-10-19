package com.bbb.raspmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bbb.raspmonitor.Db.DeviceList;
import com.bbb.raspmonitor.Db.SQLiteDB;

public class DevicesCrudActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText deviceName;
    private EditText deviceIp;
    private EditText devicePort;
    private EditText deviceTimeout;
    private EditText deviceUsername;
    private EditText devicePassword;

    private Button btnAdd, btnEdit, btnDelete;

    private SQLiteDB sqLiteDB;
    private DeviceList device;



    public static void start(Context context){
        Intent intent = new Intent(context, DevicesCrudActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, DeviceList device){
        Intent intent = new Intent(context, DevicesCrudActivity.class);
        intent.putExtra(DevicesCrudActivity.class.getSimpleName(), device);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_crud);

        deviceName = (EditText) findViewById(R.id.deviceName);
        deviceIp = (EditText) findViewById(R.id.deviceIp);
        devicePort = (EditText) findViewById(R.id.devicePort);
        deviceTimeout = (EditText) findViewById(R.id.deviceTimeout);
        deviceUsername = (EditText) findViewById(R.id.deviceUsername);
        devicePassword = (EditText) findViewById(R.id.devicePassword);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnAdd.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        device = getIntent().getParcelableExtra(DevicesCrudActivity.class.getSimpleName());
        if(device != null){
            btnAdd.setVisibility(View.GONE);

            deviceName.setText(device.getDevice_Name());
            deviceIp.setText(device.getDevice_Ip_Adress());
            devicePort.setText(device.getDevice_Port());
            deviceTimeout.setText(device.getDevice_Timeout());
            deviceUsername.setText(device.getDevice_Username());
            devicePassword.setText(device.getDevice_Password());



        }else{
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        sqLiteDB = new SQLiteDB(this);


    }


    @Override
    public void onClick(View v) {
        if(v == btnAdd){
            device = new DeviceList();
            device.setDevice_Name(deviceName.getText().toString());
            device.setDevice_Ip_Adress(deviceIp.getText().toString());
            device.setDevice_Port(devicePort.getText().toString());
            device.setDevice_Timeout(deviceTimeout.getText().toString());
            device.setDevice_Username(deviceUsername.getText().toString());
            device.setDevice_Password(devicePassword.getText().toString());
            sqLiteDB.create(device);
            Toast.makeText(this, "Inserted!", Toast.LENGTH_SHORT).show();
            finish();
        }else if(v == btnEdit){
            device.setDevice_Name(deviceName.getText().toString());
            device.setDevice_Ip_Adress(deviceIp.getText().toString());
            device.setDevice_Port(devicePort.getText().toString());
            device.setDevice_Timeout(deviceTimeout.getText().toString());
            device.setDevice_Username(deviceUsername.getText().toString());
            device.setDevice_Password(devicePassword.getText().toString());
            sqLiteDB.update(device);

            Toast.makeText(this, "Edited!", Toast.LENGTH_SHORT).show();
            finish();
        }else if(v == btnDelete){
            sqLiteDB.delete(device.getDevice_Id());

            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
