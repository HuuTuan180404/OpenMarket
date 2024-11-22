package com.example.oumarket.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Order;
import com.example.oumarket.Class.Request;
import com.example.oumarket.Interface.ItemClickListener;
import com.example.oumarket.R;

import java.util.ArrayList;
import java.util.List;

class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tvOrderId, tvOrderStatus, tvOrderPhone, tvOrderAddress;

    private ItemClickListener itemClickListener;

    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);
        tvOrderId = itemView.findViewById(R.id.order_id);
        tvOrderStatus = itemView.findViewById(R.id.order_status);
        tvOrderPhone = itemView.findViewById(R.id.order_phone);
        tvOrderAddress = itemView.findViewById(R.id.order_address);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}


public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder> {

    List<Request> list = new ArrayList<>();
    Context context;

    public RequestAdapter(List<Request> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public List<Request> getList() {
        return list;
    }

    public void setList(List<Request> list) {
        this.list = list;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        holder.tvOrderId.setText(list.get(position).getId());
        holder.tvOrderPhone.setText(list.get(position).getPhone());
        holder.tvOrderAddress.setText(list.get(position).getAddress());
        holder.tvOrderStatus.setText(status(list.get(position).getStatus()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String status(String s) {
        if (s.equals("0")) {
            return "Đang chuẩn bị hàng";
        }
        if (s.equals("1")) {
            return "Đang vận chuyển";
        }
        return "Đã giao hàng";
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
