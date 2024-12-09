package com.example.oumarket.ViewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Food;
import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}

public class BestSellerAdapter extends RecyclerView.Adapter<BestSellerViewHolder> {

    Context context;

    List<Food> list = new ArrayList<>();

    private void loadList() {
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food food;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food = dataSnapshot.getValue(Food.class);
                    list.add(food);
                }

                list.sort((a, b) -> a.sortForBestSeller(b));

                for (Food d : list) {
                    if (d.getCountRating() != 0) {
                        float a = d.getCountStars() / d.getCountRating();
                        Log.d("ZZZZZ", a + "");
                    } else Log.d("ZZZZZ", "0");
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
    public void onBindViewHolder(@NonNull BestSellerViewHolder holder, int position) {
        holder.textView_plus.setOnClickListener(v -> {

        });

        holder.itemView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
