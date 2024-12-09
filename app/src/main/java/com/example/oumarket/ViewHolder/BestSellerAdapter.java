package com.example.oumarket.ViewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.oumarket.FoodDetail;
import com.example.oumarket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rejowan.cutetoast.CuteToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class BestSellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView pic;
    TextView name_of_food, price, count_star, textView_plus;

    public BestSellerViewHolder(@NonNull View itemView) {
        super(itemView);
        pic = itemView.findViewById(R.id.pic);
        name_of_food = itemView.findViewById(R.id.name_of_food);
        price = itemView.findViewById(R.id.price);
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

    private void loadList() {
        list = new ArrayList<>();
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food food;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food = dataSnapshot.getValue(Food.class);
                    food.setId(dataSnapshot.getKey());
                    list.add(food);
                }

                list.sort((a, b) -> a.sortForBestSeller(b));

                for (int i = list.size() - 1; i >= Common.TOP_BEST_SELLER; i--) {
                    list.remove(i);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public BestSellerAdapter(Context context) {
        this.context = context;
        loadList();
    }

    @NonNull
    @Override
    public BestSellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.test2, parent, false);
        return new BestSellerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellerViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Picasso.get().load(list.get(position).getURL()).into(holder.pic);
        holder.name_of_food.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getPrice());
        holder.pic.setOnClickListener(v -> {
            Intent foodDetail = new Intent(context, FoodDetail.class);
            foodDetail.putExtra("FoodId", list.get(position).getId());
            context.startActivity(foodDetail);
        });

        holder.textView_plus.setOnClickListener(v -> {
            CuteToast.ct(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
            Order order = new Order(list.get(position).getId(), list.get(position).getName(), list.get(position).getPrice(), "1", list.get(position).getDiscount(), "0");
            Database database1 = new Database(context);
            database1.addToCart(order);

//            Log.d("ZZZZZZ", list.get(position).getId());

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
