package com.bbb.raspmonitor.Connection;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.bbb.raspmonitor.Listeners.AsyncTaskListener;
import com.bbb.raspmonitor.MainActivity;
import com.bbb.raspmonitor.R;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class SSHRunner extends AsyncTask<String, String, String> {

    private Context context;

    private String resp;
    private String command;
    private String host;
    private String user;
    private String password;


    private int port;
    private boolean isConnected;
    private boolean isLoop;

    public AsyncTaskListener delegate = null;


    public boolean ConnectSessionStatus;
    public boolean ConnectChannelStatus;


    public String ConnectSessionStatusString;
    public String ConnectChannelStatusString;



    public SSHRunner(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {


        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {


       /* params[0] = ip
        params[1] = port
        params[2] = timout
        params[3] = username
        params[4] = password
        params[5] = command
        params[6] = isConnected ? true:false
        params[7] = isLoop ? true:false*/

        user = params[3];
        host = params[0];
        password = params[4];
        port = Integer.parseInt(params[1]);


        isConnected = Boolean.parseBoolean(params[6]);
        isLoop = Boolean.parseBoolean(params[7]);

        switch(params[5]){
            case "cpuutils":
                command = "python /home/pi/Desktop/lamp.py";
                break;
            case "benchmark":
                command = "sysbench --test=cpu --cpu-max-prime=20000 --num-threads=4 run";
                break;
        }

        try{


            Log.d("SSH", "Connect params for " + params[5] + " command set.");

            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(password);
            session.connect();
            Log.d("SSH", "Session connected.");


            Channel channel = session.openChannel("exec");
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            channel.setOutputStream(baos);
            ((ChannelExec)channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);

            InputStream input = channel.getInputStream();
            channel.connect();
            Log.d("SSH", "Channel connected to machine " + host + " server with command: " + command);


            if(session.isConnected()){


                ConnectSessionStatus = true;
                ConnectSessionStatusString = "Connected Session";

                if(channel.isConnected()){


                    ConnectChannelStatus = true;
                    ConnectChannelStatusString = "Connected Channel";

                }else{

                    ConnectChannelStatus = false;
                    ConnectChannelStatusString = " Not Connected Channel";
                }
            }else{

                ConnectSessionStatus = false;
                ConnectSessionStatusString = " Not Connected Session";
            }




            resp = getChannelOutput(channel, input);
            channel.disconnect();
            session.disconnect();


        } catch (JSchException ex) {
            Log.e("SSH","Something went wrong 2");

            ConnectChannelStatus = false;
            ConnectSessionStatus = false;
            ConnectSessionStatusString = "Not Connected :(";
            ConnectChannelStatusString = "Not Connected :(";

            ex.printStackTrace();
        } catch (IOException e) {
            Log.d("SSH", e.toString());
            ConnectChannelStatus = false;
            ConnectSessionStatus = false;
            ConnectSessionStatusString = "Something went wrong";
            ConnectChannelStatusString = "Something went wrong";
            e.printStackTrace();
        }
        return "";


    }

    protected void onPostExecute(String output){
        super.onPostExecute(output);

        if(ConnectSessionStatus){

            if(ConnectChannelStatus){
                delegate.processFinish(resp);


            }else{
                delegate.processFinish("Not Connected Channel");
            }

        }else{
            delegate.processFinish("Not Connected Session");

        }


    }



    private String getChannelOutput(Channel channel, InputStream in) throws IOException{

        byte[] buffer = new byte[1024];
        StringBuilder strBuilder = new StringBuilder();

        String line = "";
        while (true){
            while (in.available() > 0) {
                int i = in.read(buffer, 0, 1024);
                if (i < 0) {
                    break;
                }
                strBuilder.append(new String(buffer, 0, i));

            }

            if(line.contains("logout")){
                break;
            }

            if (channel.isClosed()){
                System.out.println("Closed");
                break;
            }
            try {
                Thread.sleep(100);
            } catch (Exception ee){}
        }

        return strBuilder.toString();
    }




}
