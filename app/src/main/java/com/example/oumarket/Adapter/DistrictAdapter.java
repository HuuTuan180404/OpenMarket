package com.example.oumarket.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.oumarket.Class.District;
import com.example.oumarket.R;

import java.util.List;

public class DistrictAdapter extends ArrayAdapter<District> {
    private Context context;
    private List<District> districtList;

    public DistrictAdapter(@NonNull Context context, int resource, List<District> districtList) {
        super(context, resource, districtList);
        this.context = context;
        this.districtList = districtList;
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

        District district = districtList.get(position);

        if (district != null) {
            textViewName.setText(district.getName().toString());
        }

        return convertView;
    }

    //getter + setter//////////////////////////////////////
    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<District> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<District> districtList) {
        this.districtList.clear();
        this.districtList.add(new District("-1", "Chọn Quận/Huyện", "-1"));
        this.districtList.addAll(districtList);

    }
}
