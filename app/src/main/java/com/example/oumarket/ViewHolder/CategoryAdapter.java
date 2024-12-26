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

import com.example.oumarket.Class.Category;
import com.example.oumarket.Activity.FoodListActivity;
import com.example.oumarket.Interface.ItemClickListener;
import com.example.oumarket.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView imageView;
    public TextView txtMenuName;

    private ItemClickListener itemClickListener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image_category);
        txtMenuName = itemView.findViewById(R.id.name_category);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    List<Category> list = new ArrayList<>();
    Context context;

    public CategoryAdapter() {
    }

    public CategoryAdapter(List<Category> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(list.get(position).getURL()).into(holder.imageView);
        holder.txtMenuName.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foodDetail = new Intent(context, FoodListActivity.class);
                foodDetail.putExtra("categoryId", list.get(position).getId());
                context.startActivity(foodDetail);
            }
        });
    }

    public List<Category> getList() {
        return list;
    }

    public void setList(List<Category> list) {
        this.list = list;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
