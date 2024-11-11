package com.example.oumarket.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Order;
import com.example.oumarket.Interface.ItemClickListener;
import com.example.oumarket.R;

import java.util.ArrayList;
import java.util.List;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name_cart_item, price_cart_item;
    public ImageView image_cart_item;
    public TextView quantity_cart_item;

    private ItemClickListener itemClickListener;

    public TextView getName_cart_item() {
        return name_cart_item;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        name_cart_item = itemView.findViewById(R.id.name_cart_item);
        price_cart_item = itemView.findViewById(R.id.price_cart_item);
        image_cart_item = itemView.findViewById(R.id.image_cart_item);
        quantity_cart_item = itemView.findViewById(R.id.quantity_cart_item);
    }

    public void setName_cart_item(TextView name_cart_item) {
        this.name_cart_item = name_cart_item;
    }

    public TextView getPrice_cart_item() {
        return price_cart_item;
    }

    public void setPrice_cart_item(TextView price_cart_item) {
        this.price_cart_item = price_cart_item;
    }

    public ImageView getImage_cart_item() {
        return image_cart_item;
    }

    public void setImage_cart_item(ImageView image_cart_item) {
        this.image_cart_item = image_cart_item;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> list=new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView= layoutInflater.inflate(R.layout.cart_item,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.name_cart_item.setText(list.get(position).getProductName());
        holder.price_cart_item.setText(list.get(position).getPrice());
        holder.quantity_cart_item.setText(list.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
