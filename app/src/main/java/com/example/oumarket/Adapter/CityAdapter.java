package com.example.oumarket.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.oumarket.Class.City;
import com.example.oumarket.R;

import java.util.List;

public class CityAdapter extends ArrayAdapter<City> {

    private Context context;
    private List<City> cityList;

    public CityAdapter(@NonNull Context context, int resource, List<City> cities) {
        super(context, resource, cities);
        this.context = context;
        this.cityList = cities;
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

        City currentCity = cityList.get(position);

        if (currentCity != null) {
            textViewName.setText(currentCity.getName().toString());
        }

        return convertView;
    }

}
