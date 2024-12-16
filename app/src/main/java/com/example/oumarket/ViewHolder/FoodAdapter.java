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
import android.widget.RelativeLayout;
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
import com.rejowan.cutetoast.CuteToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView food_name, food_price, food_rate, discount;
    public ImageView food_image;
    private ItemClickListener itemClickListener;
    RelativeLayout layout_discount;

    LinearLayout add_to_cart;

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

        discount = itemView.findViewById(R.id.discount);
        layout_discount = itemView.findViewById(R.id.layout_discount);

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
        Food food = list.get(position);
        Picasso.get().load(food.getURL()).into(holder.food_image);

        holder.food_name.setText(food.getName());
        holder.food_price.setText(food.getPrice());

        holder.discount.setText(food.getDiscount() + "%");

        if (food.getDiscount().equals("0")) {
            holder.layout_discount.setVisibility(View.GONE);
        }

        if (food.getCountRating() != 0) {
            float a = food.getCountStars() / food.getCountRating();
            holder.food_rate.setText(a + "");
        } else {
            holder.food_rate.setText("---");
        }

        holder.add_to_cart.setOnClickListener(v -> {
            CuteToast.ct(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
            Order order = new Order(food.getId(), food.getName(), food.getPrice(), "1", food.getDiscount(), "0");
            Database database1 = new Database(context);
            database1.addToCart(order);
        });
        holder.itemView.setOnClickListener(v -> {
            Intent foodDetail = new Intent(context, FoodDetail.class);
            foodDetail.putExtra("FoodId", food.getId());
            context.startActivity(foodDetail);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
