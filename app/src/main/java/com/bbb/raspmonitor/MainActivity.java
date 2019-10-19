package com.bbb.raspmonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bbb.raspmonitor.Connection.SSHRunner;
import com.bbb.raspmonitor.Db.DeviceList;
import com.bbb.raspmonitor.Db.SQLiteDB;
import com.bbb.raspmonitor.Listeners.AsyncTaskListener;
import com.bbb.raspmonitor.Utils.Tools;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements AsyncTaskListener {

    private SQLiteDB sqLiteDB;
    private DeviceList device;


    private String deviceIp;
    private String devicePort;
    private String deviceTimeout;
    private String deviceUsername;
    private String devicePassword;
    private String deviceName;

    private TextView Device_Name, Cpu_Temp, Cpu_Live, Cpu_Min, Cpu_Max;

    private SSHRunner runner;

    private boolean isLoop;



    private TabLayout tab_layout;
    private NestedScrollView nested_scroll_view;

    public static void start(Context context, DeviceList device){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.class.getSimpleName(), device);
        context.startActivity(intent);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.light_blue_500), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_add), 0);
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_add), 1);
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_add), 2);
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_add), 3);
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_add), 4);

        // set icon color pre-selected
        tab_layout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.light_blue_100), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.light_blue_700), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.light_blue_700), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.light_blue_700), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(4).getIcon().setColorFilter(getResources().getColor(R.color.light_blue_700), PorterDuff.Mode.SRC_IN);

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.light_blue_100), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.light_blue_700), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });




        Device_Name = findViewById(R.id.textView_Device_Name);
        Cpu_Temp = findViewById(R.id.textView_Cpu_Temp);
        Cpu_Live = findViewById(R.id.textView_Cpu_Live_Speed);
        Cpu_Min = findViewById(R.id.textView_Cpu_Min);
        Cpu_Max = findViewById(R.id.textView_Cpu_Max);



        device = getIntent().getParcelableExtra(MainActivity.class.getSimpleName());
        if(device != null){



            deviceIp = device.getDevice_Ip_Adress();
            devicePort = device.getDevice_Port();
            deviceTimeout = device.getDevice_Timeout();
            deviceUsername = device.getDevice_Username();
            devicePassword = device.getDevice_Password();
            deviceName = device.getDevice_Name();


            StartSSH();

            isLoop = true;

        }

        sqLiteDB = new SQLiteDB(this);

        Device_Name.setText(deviceName);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.light_blue_500));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    public void StartSSH(){
        /* params[0] = ip
        params[1] = port
        params[2] = timout
        params[3] = username
        params[4] = password
        params[5] = command
        params[6] = isConnected ? true:false*/


        runner = new SSHRunner(this);
        runner.delegate = this;
        String[] args = {deviceIp,devicePort,deviceTimeout,deviceUsername,devicePassword,"cpuutils","true",Boolean.toString(isLoop)};
        runner.execute(args);

    }

    @Override
    public void processFinish(String output){

       // testText.setText(output);
        String[] parts = output.split("##########");
        if (isLoop){
            StartSSH();
        }
        Cpu_Temp.setText(parts[0]);
        Cpu_Min.setText(parts[1]);
        Cpu_Max.setText(parts[2]);
        Cpu_Live.setText(parts[3]);

      /*  if(output.equals("Not Connected")){
           // testText.setText(output);
        }else if(output.equals("Connected...")){
            //testText.setText(output);
        } else{
            StartSSH();
        }*/



    }


    @Override
    protected void onDestroy() {
        if (runner != null) {
            runner.cancel(true);
        }

        super.onDestroy();
    }
}
