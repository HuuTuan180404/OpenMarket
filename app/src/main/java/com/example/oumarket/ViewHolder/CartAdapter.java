package com.example.oumarket.ViewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.Order;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Database.Database;
import com.example.oumarket.FoodDetail;
import com.example.oumarket.Interface.ItemClickListener;
import com.example.oumarket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name_cart_item, price_cart_item;
    public ImageView image_cart_item;
    EditText quantity_cart_item;
    AppCompatButton btnIncrease, btnDecrease;

    private ItemClickListener itemClickListener;

    public CartViewHolder(View itemView) {
        super(itemView);
        name_cart_item = itemView.findViewById(R.id.name_cart_item);
        price_cart_item = itemView.findViewById(R.id.price_cart_item);
        image_cart_item = itemView.findViewById(R.id.image_cart_item);
        btnIncrease = itemView.findViewById(R.id.button_Increase);
        btnDecrease = itemView.findViewById(R.id.button_Decrease);
        quantity_cart_item = itemView.findViewById(R.id.edittext_quantity);
        itemView.setOnClickListener(this);

    }

    public TextView getName_cart_item() {
        return name_cart_item;
    }

    public void setName_cart_item(TextView name_cart_item) {
        this.name_cart_item = name_cart_item;
    }

    public TextView getPrice_cart_item() {
        return price_cart_item;
    }

    public void setPrice_cart_item(TextView price_cart_item) {
        this.price_cart_item = price_cart_item;
    }

    public ImageView getImage_cart_item() {
        return image_cart_item;
    }

    public void setImage_cart_item(ImageView image_cart_item) {
        this.image_cart_item = image_cart_item;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> list = new ArrayList<>();
    private Context context;
    private TextView tv_total;

    public CartAdapter(List<Order> list, Context context, TextView tv_total) {
        this.list = list;
        this.context = context;
        this.tv_total = tv_total;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String foodId = list.get(position).getProductId();
        holder.name_cart_item.setText(list.get(position).getProductName());
        holder.price_cart_item.setText(list.get(position).getPrice());
        holder.quantity_cart_item.setText(list.get(position).getQuantity());

        String discount = list.get(position).getDiscount();

        holder.quantity_cart_item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String valueString = s.toString();
                if (valueString.isEmpty() || Integer.parseInt(valueString) < 1) {
                    holder.quantity_cart_item.setText("1");
                }
            }
        });

        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantity_cart_item.getText().toString());
                if (quantity == 1) {
                    Toast.makeText(context, "Lỗi số lượng", Toast.LENGTH_SHORT).show();
                } else {
                    quantity -= 1;

                    Order order = new Order(foodId, holder.name_cart_item.getText().toString(), holder.price_cart_item.getText().toString(), "-1", discount);

                    new Database(context).addToCart(order);

//                    update quantiry to quantity_cart_item
                    holder.quantity_cart_item.setText(String.valueOf(quantity));

//                    update quantity to list_order
                    list.get(position).setQuantity(String.valueOf(quantity));

//                    update price total after quantity was changed
                    updateTotalPrice();
                }
            }
        });

        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantity_cart_item.getText().toString());
                quantity += 1;

                holder.quantity_cart_item.setText(String.valueOf(quantity));
                list.get(position).setQuantity(String.valueOf(quantity));

                Order order = new Order(foodId, holder.name_cart_item.getText().toString(), holder.price_cart_item.getText().toString(), "1", discount);

                new Database(context).addToCart(order);

                updateTotalPrice();

            }
        });

        holder.image_cart_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foodDetail = new Intent(context, FoodDetail.class);
                foodDetail.putExtra("FoodId", foodId);
                context.startActivity(foodDetail);
            }
        });

//        holder.image_cart_item.set
        Common.DATABASE.getReference("Foods").child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food currentFood = snapshot.getValue(Food.class);
                String pathURL = currentFood.getURL();
                Picasso.get().load(pathURL).into(holder.image_cart_item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateTotalPrice() {
        int total = 0;
        for (Order order : list) {
            total += Integer.parseInt(order.getPrice()) * Integer.parseInt(order.getQuantity());
        }
        tv_total.setText(String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}



