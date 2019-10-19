package com.bbb.raspmonitor.Db;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceList implements Parcelable {


    private int Device_Id;
    private String Device_Name;
    private String Device_Ip_Adress;
    private String Device_Port;
    private String Device_Timeout;
    private String Device_Username;
    private String Device_Password;

    public DeviceList() {
    }

    protected DeviceList(Parcel in) {
        this.Device_Id = in.readInt();
        this.Device_Name = in.readString();
        this.Device_Ip_Adress = in.readString();
        this.Device_Port = in.readString();
        this.Device_Timeout = in.readString();
        this.Device_Username = in.readString();
        this.Device_Password = in.readString();
    }

    public static final Parcelable.Creator<DeviceList> CREATOR = new Parcelable.Creator<DeviceList>() {
        @Override
        public DeviceList createFromParcel(Parcel source) {
            return new DeviceList(source);
        }

        @Override
        public DeviceList[] newArray(int size) {
            return new DeviceList[size];
        }
    };

    public int getDevice_Id() {
        return Device_Id;
    }

    public void setDevice_Id(int device_Id) {
        Device_Id = device_Id;
    }

    public String getDevice_Name() {
        return Device_Name;
    }

    public void setDevice_Name(String device_Name) {
        Device_Name = device_Name;
    }

    public String getDevice_Ip_Adress() {
        return Device_Ip_Adress;
    }

    public void setDevice_Ip_Adress(String device_Ip_Adress) {
        Device_Ip_Adress = device_Ip_Adress;
    }

    public String getDevice_Port() {
        return Device_Port;
    }

    public void setDevice_Port(String device_Port) {
        Device_Port = device_Port;
    }

    public String getDevice_Timeout() {
        return Device_Timeout;
    }

    public void setDevice_Timeout(String device_Timeout) {
        Device_Timeout = device_Timeout;
    }

    public String getDevice_Username() {
        return Device_Username;
    }

    public void setDevice_Username(String device_Username) {
        Device_Username = device_Username;
    }

    public String getDevice_Password() {
        return Device_Password;
    }

    public void setDevice_Password(String device_Password) {
        Device_Password = device_Password;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Device_Id);
        dest.writeString(this.Device_Name);
        dest.writeString(this.Device_Ip_Adress);
        dest.writeString(this.Device_Port);
        dest.writeString(this.Device_Timeout);
        dest.writeString(this.Device_Username);
        dest.writeString(this.Device_Password);
    }
}
