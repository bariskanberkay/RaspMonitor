package com.bbb.raspmonitor.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bbb.raspmonitor.Db.DeviceList;
import com.bbb.raspmonitor.Listeners.DL_RecyclerItemClickListener;
import com.bbb.raspmonitor.Listeners.DL_RecyclerItemLongClickListener;
import com.bbb.raspmonitor.R;
import com.bbb.raspmonitor.Utils.LetterTile;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListHolder> {


    private List<DeviceList> deviceList;
    private Context context;

    private DL_RecyclerItemClickListener recyclerItemClickListener;
    private DL_RecyclerItemLongClickListener recyclerItemLongClickListener;


    public DeviceListAdapter(Context context) {
        this.context = context;
        this.deviceList = new ArrayList<>();
    }

    private void add(DeviceList item) {
        deviceList.add(item);
        notifyItemInserted(deviceList.size() - 1);
    }

    public void addAll(List<DeviceList> deviceList) {
        for (DeviceList device : deviceList) {
            add(device);
        }
    }

    public void remove(DeviceList item) {
        int position = deviceList.indexOf(item);
        if (position > -1) {
            deviceList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public DeviceList getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public DeviceListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_single_item, parent, false);

        final DeviceListHolder deviceHolder = new DeviceListHolder(view);

        deviceHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPos = deviceHolder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    if (recyclerItemClickListener != null) {
                        recyclerItemClickListener.onItemClick(adapterPos, deviceHolder.itemView);

                    }
                }
            }
        });

        deviceHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean  onLongClick(View v) {
                int adapterPos = deviceHolder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    if (recyclerItemLongClickListener != null) {
                        recyclerItemLongClickListener.onItemLongClick(adapterPos, deviceHolder.itemView);

                    }
                }
                return true;
            }
        });

        return deviceHolder;
    }

    @Override
    public void onBindViewHolder(DeviceListHolder holder, int position) {
        final DeviceList device = deviceList.get(position);

        final Resources res = context.getResources();
        final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);

        LetterTile letterTile = new LetterTile(context);

        Bitmap letterBitmap = letterTile.getLetterTile(device.getDevice_Name(),
                String.valueOf(device.getDevice_Id()), tileSize, tileSize);

        holder.thumb.setImageBitmap(letterBitmap);
        holder.name.setText(device.getDevice_Name());
        holder.phone.setText(device.getDevice_Ip_Adress());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public void setOnItemClickListener(DL_RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    public void setOnItemLongClickListener(DL_RecyclerItemLongClickListener recyclerItemLongClickListener) {
        this.recyclerItemLongClickListener = recyclerItemLongClickListener;
    }


    static class DeviceListHolder extends RecyclerView.ViewHolder {

        ImageView thumb;
        TextView name;
        TextView phone;

        public DeviceListHolder(View itemView) {
            super(itemView);

            thumb = (ImageView) itemView.findViewById(R.id.thumb);
            name = (TextView) itemView.findViewById(R.id.device_name);
            phone = (TextView) itemView.findViewById(R.id.device_ip);

        }
    }

}
