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
import com.example.oumarket.Class.Food;
import com.example.oumarket.FoodDetail;
import com.example.oumarket.FoodList;
import com.example.oumarket.R;
import com.squareup.picasso.Picasso;

import java.util.List;

class FilterFoodsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView pic;
    TextView name;

    public FilterFoodsViewHolder(@NonNull View itemView) {
        super(itemView);
        pic = itemView.findViewById(R.id.pic);
        name = itemView.findViewById(R.id.name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}

public class FilterFoodsAdapter extends RecyclerView.Adapter<FilterFoodsViewHolder> {

    Context context;
    List<Food> list;

    public FilterFoodsAdapter(Context context, List<Food> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FilterFoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_filter_foods, parent, false);
        return new FilterFoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterFoodsViewHolder holder, int position) {
        Food item = list.get(position);
        Picasso.get().load(item.getUrl()).into(holder.pic);
        holder.name.setText(item.getName());
        holder.itemView.setOnClickListener(v -> {
            Intent foodDetail = new Intent(context, FoodDetail.class);
            foodDetail.putExtra("FoodId", item.getId());
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

    public List<Food> getList() {
        return list;
    }

    public void setList(List<Food> list) {
        this.list = list;
    }
}
