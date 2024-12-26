package com.example.oumarket.ViewHolder;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.Order;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Activity.FoodDetailActivity;
import com.example.oumarket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

class RatingFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView image_food;
    TextView name_of_food, ratingText;
    RatingBar ratingBar;

    public RatingFoodViewHolder(@NonNull View itemView) {
        super(itemView);
        image_food = itemView.findViewById(R.id.image_food);
        name_of_food = itemView.findViewById(R.id.name_of_food);
        ratingText = itemView.findViewById(R.id.ratingText);
        ratingBar = itemView.findViewById(R.id.ratingBar);
    }

    @Override
    public void onClick(View v) {

    }
}

public class RatingFoodAdapter extends RecyclerView.Adapter<RatingFoodViewHolder> {

    List<Order> list;
    Context context;

    @NonNull
    @Override
    public RatingFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_rating_food, parent, false);
        return new RatingFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingFoodViewHolder holder, int position) {
        Order order = list.get(position);
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).child(order.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food food = snapshot.getValue(Food.class);
                Picasso.get().load(food.getUrl()).into(holder.image_food);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.name_of_food.setText(order.getProductName());
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int int_rating = (int) Math.ceil(rating);

                String s;
                int color;

                switch (int_rating) {
                    case 1:
                        s = "Chất lượng sản phẩm:\nTệ";
                        color = context.getResources().getColor(R.color.ratingVeryBad);
                        break;
                    case 2:
                        s = "Chất lượng sản phẩm:\nKhông hài lòng";
                        color = context.getResources().getColor(R.color.ratingBad);
                        break;
                    case 3:
                        s = "Chất lượng sản phẩm:\nBình thường";
                        color = context.getResources().getColor(R.color.ratingNeutral);
                        break;
                    case 4:
                        s = "Chất lượng sản phẩm:\nHài lòng";
                        color = context.getResources().getColor(R.color.ratingGood);
                        break;
                    default:
                        s = "Chất lượng sản phẩm:\nTuyệt vời";
                        color = context.getResources().getColor(R.color.ratingVeryGood);
                        break;
                }
                list.get(position).setCountStars(int_rating);
                holder.ratingText.setText(s);
                holder.ratingBar.setProgressTintList(ColorStateList.valueOf(color));
                holder.ratingBar.setProgressBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.ratingDefault, null)));
                holder.ratingBar.setRating(int_rating);
            }
        });

        holder.image_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodDetailActivity.class);
                intent.putExtra("FoodId", order.getProductId());
                context.startActivity(intent);
            }
        });
    }

    public RatingFoodAdapter(List<Order> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<Order> getList() {
        return list;
    }

    public void setList(List<Order> list) {
        this.list = list;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}


