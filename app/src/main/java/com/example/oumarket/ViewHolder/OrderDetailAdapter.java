package com.example.oumarket.ViewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.Order;
import com.example.oumarket.Common.Common;
import com.example.oumarket.FoodDetail;
import com.example.oumarket.Interface.ItemClickListener;
import com.example.oumarket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class OrderDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView image;
    TextView name, price, quantity, discount;

    ItemClickListener itemClickListener;

    public OrderDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.image);
        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
        quantity = itemView.findViewById(R.id.quantity);
        discount = itemView.findViewById(R.id.discount);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailViewHolder> {

    List<Order> list = new ArrayList<>();

    Context context;

    public OrderDetailAdapter(List<Order> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public OrderDetailAdapter() {
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(list.get(position).getProductName());
        holder.price.setText(list.get(position).getPrice());
        holder.quantity.setText(list.get(position).getQuantity());
        holder.discount.setText(list.get(position).getDiscount());

        holder.image.setOnClickListener(v -> {
            Intent foodDetail = new Intent(context, FoodDetail.class);
            foodDetail.putExtra("FoodId", list.get(position).getProductId());
            context.startActivity(foodDetail);
        });

        String keyFood = list.get(position).getProductId();
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).child(keyFood).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Food food = snapshot.getValue(Food.class);
                    Picasso.get().load(food.getURL()).into(holder.image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
