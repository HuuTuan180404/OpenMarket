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

import com.example.oumarket.Activity.CartActivity;
import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.Order;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Database.Database;
import com.example.oumarket.Activity.FoodDetailActivity;
import com.example.oumarket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name_cart_item, price_cart_item;
    public ImageView image_cart_item;
    EditText quantity_cart_item;
    CheckBox isBuy;
    AppCompatButton btnIncrease, btnDecrease;


    public CartViewHolder(View itemView) {
        super(itemView);
        name_cart_item = itemView.findViewById(R.id.item_cart_name);
        price_cart_item = itemView.findViewById(R.id.itemcart_price);
        image_cart_item = itemView.findViewById(R.id.itemcart_image);
        btnIncrease = itemView.findViewById(R.id.button_Increase);
        btnDecrease = itemView.findViewById(R.id.button_Decrease);
        quantity_cart_item = itemView.findViewById(R.id.edittext_quantity);
        isBuy = itemView.findViewById(R.id.buy);

    }

    @Override
    public void onClick(View v) {

    }

    /////////////////////////////////////////////////////
//    getter + setter
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

//    getter + setter
///////////////////////////////////////////////////////////

}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> list = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> list, Context context) {
        this.list = list;
        this.context = context;
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

                int quantity = Integer.parseInt(holder.quantity_cart_item.getText().toString());
                new Database(context).updateQuantity(list.get(position).getProductId(), holder.quantity_cart_item.getText().toString());
                list.get(position).setQuantity(String.valueOf(quantity));
                ((CartActivity) context).updateBill();
            }
        });

        holder.btnDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(holder.quantity_cart_item.getText().toString());
            if (quantity == 1) {
                Toast.makeText(context, "Lỗi số lượng", Toast.LENGTH_SHORT).show();
            } else {
                quantity -= 1;

                new Database(context).updateQuantity(list.get(position).getProductId(), holder.quantity_cart_item.getText().toString());

//                    update quantiry to quantity_cart_item
                holder.quantity_cart_item.setText(String.valueOf(quantity));

//                    update quantity to list_order
                list.get(position).setQuantity(String.valueOf(quantity));

                ((CartActivity) context).updateBill();
            }
        });

        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.quantity_cart_item.getText().toString());
                quantity += 1;

                holder.quantity_cart_item.setText(String.valueOf(quantity));

                list.get(position).setQuantity(String.valueOf(quantity));

                new Database(context).updateQuantity(list.get(position).getProductId(), holder.quantity_cart_item.getText().toString());

                ((CartActivity) context).updateBill();

            }
        });

        holder.isBuy.setChecked(list.get(position).getIsBuy().equals("1") ? true : false);

        holder.isBuy.setOnClickListener(v -> {
            list.get(position).setIsBuy(holder.isBuy.isChecked() == true ? "1" : "0");
            new Database(context).updateIsBuy(list.get(position).getProductId(), list.get(position).getIsBuy());
            ((CartActivity) context).updateBill();
        });

        holder.image_cart_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foodDetail = new Intent(context, FoodDetailActivity.class);
                foodDetail.putExtra("FoodId", foodId);
                context.startActivity(foodDetail);
            }
        });

//        holder.image_cart_item.set
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food currentFood = snapshot.getValue(Food.class);
                Picasso.get().load(currentFood.getUrl()).into(holder.image_cart_item);
                String pathURL = currentFood.getUrl();
                Picasso.get().load(pathURL).into(holder.image_cart_item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeOrder(int pos) {
        list.remove(pos);
        new Database(getContext()).cleanCart();
        Database database1 = new Database(getContext());
        database1.add_list_to_cart(list);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    ///////////////////////////////////////////////////////////
//    getter + setter
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

//    getter + setter
    ///////////////////////////////////////////////////////////
}



