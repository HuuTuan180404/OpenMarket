package com.example.oumarket.Adapter;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Activity.AddNewAddressActivity;
import com.example.oumarket.Class.AnAddress;
import com.example.oumarket.Helper.AddressType;
import com.example.oumarket.R;

import java.util.ArrayList;
import java.util.List;

class SelectAddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView name, phone, address, ward_getPath, typeAddress, updateAddress;
    CardView isDefault;
    RelativeLayout relativeLayout;

    public SelectAddressViewHolder(@NonNull View itemView) {
        super(itemView);

        relativeLayout = itemView.findViewById(R.id.relativeLayout);
        this.name = itemView.findViewById(R.id.name);
        this.phone = itemView.findViewById(R.id.phone);
        this.address = itemView.findViewById(R.id.address);
        this.typeAddress = itemView.findViewById(R.id.typeAddress);
        this.ward_getPath = itemView.findViewById(R.id.ward_getPath);

        this.isDefault = itemView.findViewById(R.id.isDefault);

        this.updateAddress = itemView.findViewById(R.id.update_address);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}

public class SelectAddressAdapter extends RecyclerView.Adapter<SelectAddressViewHolder> {

    private List<AnAddress> list = new ArrayList<>();
    private Context context;

    public SelectAddressAdapter(Context context, List<AnAddress> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SelectAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_address_detail, parent, false);
        return new SelectAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectAddressViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AnAddress anAddress = list.get(position);
        holder.name.setText(anAddress.getName());
        holder.phone.setText(anAddress.getPhone());

        holder.address.setText(anAddress.getAddress());

        if (anAddress.getTypeAddress() == AddressType.HOME)
            holder.typeAddress.setText("HOME");
        else if (anAddress.getTypeAddress() == AddressType.WORK)
            holder.typeAddress.setText("WORK");
        else holder.typeAddress.setText("OTHER");

        holder.ward_getPath.setText(anAddress.getWard().getPath());

        if (anAddress.getIsDefault()) holder.isDefault.setVisibility(View.VISIBLE);
        else holder.isDefault.setVisibility(View.GONE);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedItem", position);
                ((Activity) context).setResult(RESULT_OK, resultIntent);
                ((Activity) context).finish();
            }
        });

        holder.updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, AddNewAddressActivity.class).putExtra("editAnAddress", position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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
