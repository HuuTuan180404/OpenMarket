package com.example.oumarket.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.oumarket.Class.Ward;
import com.example.oumarket.R;

import java.util.List;

public class WardAdapter extends ArrayAdapter<Ward> {

    private Context context;
    private List<Ward> wardList;

    public WardAdapter(@NonNull Context context, int resource, @NonNull List<Ward> wards) {
        super(context, resource, wards);
        this.context = context;
        this.wardList = wards;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.item_address);

        Ward ward = wardList.get(position);

        if (ward != null) {
            textViewName.setText(ward.getName());
        }

        return convertView;
    }

//gettter + settter /////////////////////////////////////

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Ward> getWardList() {
        return wardList;
    }

    public void setWardList(List<Ward> wardList) {
        this.wardList.clear();
        this.wardList.add(new Ward("-1", "Chọn Xã/Phường", "-1", "-1"));
        this.wardList.addAll(wardList);
    }

}
