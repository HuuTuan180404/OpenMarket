package com.example.oumarket.ViewHolder;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.Order;
import com.example.oumarket.Database.Database;
import com.example.oumarket.FoodDetail;
import com.example.oumarket.Interface.ItemClickListener;
import com.example.oumarket.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView food_name, food_price, food_rate;
    public ImageView food_image;
    private ItemClickListener itemClickListener;

    AppCompatButton add_to_cart;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        add_to_cart = itemView.findViewById(R.id.add_to_card);
        food_image = itemView.findViewById(R.id.food_image);
        food_name = itemView.findViewById(R.id.food_name);
        food_price = itemView.findViewById(R.id.food_price);
        food_rate = itemView.findViewById(R.id.food_rate);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}

public class FoodAdapter extends RecyclerView.Adapter<FoodViewHolder> {
    private List<Food> list = new ArrayList<>();
    private Context context;

    public FoodAdapter(List<Food> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public FoodAdapter() {
    }

    public List<Food> getList() {
        return list;
    }

    public void setList(List<Food> list) {
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
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(list.get(position).getURL()).into(holder.food_image);
        holder.food_name.setText(list.get(position).getName());
        holder.food_price.setText(list.get(position).getPrice());
        holder.food_rate.setText("Rate: 4 sao");
        holder.add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Add_to_cart", Toast.LENGTH_SHORT).show();
                Order order = new Order(list.get(position).getId(), list.get(position).getName(), list.get(position).getPrice(), "1", list.get(position).getDiscount(), "0");
                Database database1 = new Database(getContext());
                database1.addToCart(order);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foodDetail = new Intent(context, FoodDetail.class);
                foodDetail.putExtra("FoodId", list.get(position).getId());
                context.startActivity(foodDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
