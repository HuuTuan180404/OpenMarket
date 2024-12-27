package com.example.oumarket.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.Order;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Database.Database;
import com.example.oumarket.Activity.FoodDetailActivity;
import com.example.oumarket.R;
import com.rejowan.cutetoast.CuteToast;
import com.squareup.picasso.Picasso;

import java.util.List;

class BestSellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView pic;
    TextView name_of_food, price, count_star, textView_plus;

    public BestSellerViewHolder(@NonNull View itemView) {
        super(itemView);
        pic = itemView.findViewById(R.id.pic);
        name_of_food = itemView.findViewById(R.id.name_of_food);
        price = itemView.findViewById(R.id.price_before_discount);
        count_star = itemView.findViewById(R.id.count_star);
        textView_plus = itemView.findViewById(R.id.textView_plus);
    }

    @Override
    public void onClick(View v) {

    }
}

public class BestSellerAdapter extends RecyclerView.Adapter<BestSellerViewHolder> {

    Context context;

    List<Food> list;

    public BestSellerAdapter(Context context, List<Food> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BestSellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_best_seller, parent, false);
        return new BestSellerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellerViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Food food = list.get(position);

        Picasso.get().load(food.getUrl()).into(holder.pic);
        holder.name_of_food.setText(food.getName());
        holder.price.setText(food.getPrice());
        holder.pic.setOnClickListener(v -> {
            Intent foodDetail = new Intent(context, FoodDetailActivity.class);
            foodDetail.putExtra("FoodId", food.getId());
            context.startActivity(foodDetail);
        });

        if (food.getCountRating() != 0) {
            holder.count_star.setText(food.getRating() + "");
        } else {
            holder.count_star.setText("chưa có");
        }

        holder.textView_plus.setOnClickListener(v -> {
            CuteToast.ct(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
            Order order = new Order(food.getId(), food.getName(), food.getPrice(), "1", food.getDiscount(), "0");
            Database database1 = new Database(context);
            database1.addToCart(order);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
