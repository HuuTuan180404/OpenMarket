package com.example.oumarket.ViewHolder;

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
import com.example.oumarket.FoodList;
import com.example.oumarket.R;
import com.squareup.picasso.Picasso;

import java.util.List;

class FilterCategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView pic;
    TextView name;

    public FilterCategoriesViewHolder(@NonNull View itemView) {
        super(itemView);
        pic = itemView.findViewById(R.id.pic);
        name = itemView.findViewById(R.id.name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}

public class FilterCategoriesAdapter extends RecyclerView.Adapter<FilterCategoriesViewHolder> {

    Context context;
    List<Category> list;

    public FilterCategoriesAdapter(Context context, List<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FilterCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_filter_categories, parent, false);
        return new FilterCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterCategoriesViewHolder holder, int position) {
        Category item = list.get(position);
        Picasso.get().load(item.getURL()).into(holder.pic);
        holder.name.setText(item.getName());
        holder.itemView.setOnClickListener(v -> {
            Intent foodDetail = new Intent(context, FoodList.class);
            foodDetail.putExtra("categoryId", item.getId());
            context.startActivity(foodDetail);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Category> getList() {
        return list;
    }

    public void setList(List<Category> list) {
        this.list = list;
    }
}
