package com.example.oumarket.Adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.Order;
import com.example.oumarket.Database.Database;
import com.example.oumarket.Activity.FoodDetailActivity;
import com.example.oumarket.Interface.ItemClickListener;
import com.example.oumarket.R;
import com.rejowan.cutetoast.CuteToast;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView food_name, food_price, food_rate, discount;
    public ImageView food_image;
    private ItemClickListener itemClickListener;
    TextView add_to_cart;

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

    private int item_layout;

    public FoodAdapter(List<Food> list, Context context, int item_layout) {
        this.list = list;
        this.context = context;
        this.item_layout = item_layout;
    }

    public void setList(List<Food> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Food> getList() {
        return list;
    }

    public void setItem_layout(int item_layout) {
        this.item_layout = item_layout;
    }


    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView;
        if (item_layout == R.layout.item_food_grid_view)
            itemView = layoutInflater.inflate(R.layout.item_food_grid_view, parent, false);
        else itemView = layoutInflater.inflate(R.layout.item_food_list_view, parent, false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Food food = list.get(position);
        Picasso.get().load(food.getUrl()).into(holder.food_image);

        holder.food_name.setText(food.getName());

        String priceString = list.get(position).getPrice();
        double price = Double.parseDouble(priceString);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(price);
        holder.food_price.setText(formattedPrice);



        holder.discount.setText(food.getDiscount() + "%");
        if (food.getDiscount().equals("0")) {
            holder.discount.setVisibility(View.GONE);
        }
        if (food.getCountRating() != 0) {
            holder.food_rate.setText(food.getRating());
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
            Intent foodDetail = new Intent(context, FoodDetailActivity.class);
            foodDetail.putExtra("FoodId", food.getId());
            context.startActivity(foodDetail);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
