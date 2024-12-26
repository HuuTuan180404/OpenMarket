package com.example.oumarket.Adapter;

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

import com.example.oumarket.Activity.FoodDetailActivity;
import com.example.oumarket.Class.Food;
import com.example.oumarket.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchFoodAdapter extends RecyclerView.Adapter<SearchFoodAdapter.ViewHolder> {
    List<Food> list;
    Context context;

    public SearchFoodAdapter(ArrayList<Food> list){
        this.list = list;
    }

    public List<Food> getItems() {
        return list;
    }

    public void setItems(ArrayList<Food> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SearchFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_search_food,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFoodAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(list.get(position).getUrl()).fit().centerCrop().into(holder.pic);
        holder.name.setText(list.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodDetailActivity.class);
                intent.putExtra("FoodId", list.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pic;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pic= itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
        }
    }
}