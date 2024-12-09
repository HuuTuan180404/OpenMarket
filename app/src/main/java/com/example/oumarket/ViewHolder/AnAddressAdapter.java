package com.example.oumarket.ViewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.AnAddress;
import com.example.oumarket.Class.Ward;
import com.example.oumarket.Interface.ItemClickListener;
import com.example.oumarket.R;

import java.util.ArrayList;
import java.util.List;

class AnAddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView name, phone, address, ward_getPath;
    CardView isMap, isDefault, anAddress;

    public AnAddressViewHolder(@NonNull View itemView) {
        super(itemView);

        this.anAddress = itemView.findViewById(R.id.anAddress);
        this.name = itemView.findViewById(R.id.name);
        this.phone = itemView.findViewById(R.id.phone);
        this.address = itemView.findViewById(R.id.address);
        this.ward_getPath = itemView.findViewById(R.id.ward_getPath);

        this.isDefault = itemView.findViewById(R.id.isDefault);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        v.setOnClickListener(this);
    }

//getter+setter//////////////////

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getPhone() {
        return phone;
    }

    public void setPhone(TextView phone) {
        this.phone = phone;
    }

    public TextView getAddress() {
        return address;
    }

    public void setAddress(TextView address) {
        this.address = address;
    }

    public TextView getWard_getPath() {
        return ward_getPath;
    }

    public void setWard_getPath(TextView ward_getPath) {
        this.ward_getPath = ward_getPath;
    }

    public CardView getIsMap() {
        return isMap;
    }

    public void setIsMap(CardView isMap) {
        this.isMap = isMap;
    }

    public CardView getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(CardView isDefault) {
        this.isDefault = isDefault;
    }
}

public class AnAddressAdapter extends RecyclerView.Adapter<AnAddressViewHolder> {

    private List<AnAddress> list = new ArrayList<>();
    private Context context;

    public AnAddressAdapter(Context context, List<AnAddress> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AnAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_address_detail, parent, false);

        return new AnAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnAddressViewHolder holder, int position) {

        AnAddress anAddress = list.get(position);

        holder.name.setText(anAddress.getName());
        holder.phone.setText(anAddress.getPhone());

        holder.address.setText(anAddress.getAddress());
        holder.ward_getPath.setText(anAddress.getWard().getPath());

        if (anAddress.getIsDefault()) {
            holder.isDefault.setVisibility(View.VISIBLE);
        } else {
            holder.isDefault.setVisibility(View.GONE);
        }

        holder.anAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ZZZZZ", "click");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeOrder(int position) {
        list.remove(position);
    }

    public List<AnAddress> getList() {
        return list;
    }

    public void setList(List<AnAddress> list) {
        this.list = list;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
