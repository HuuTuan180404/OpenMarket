package com.example.oumarket.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.Order;
import com.example.oumarket.Class.Request;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Database.Database;
import com.example.oumarket.Interface.ItemClickListener;
import com.example.oumarket.R;
import com.example.oumarket.Activity.RatingFoodActivity;
import com.example.oumarket.Fragment.OrderDetailFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rejowan.cutetoast.CuteToast;

import java.util.List;

class MyOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView idRequest, total, time, countItems, item_items;
    ImageView imageView;

    AppCompatButton rate, reOrder, cancel;

    LinearLayout buttonLayoutOngoing, buttonLayoutHistory;
    TextView status;

    private ItemClickListener itemClickListener;

    public MyOrderViewHolder(@NonNull View itemView) {
        super(itemView);
        status = itemView.findViewById(R.id.status);
        idRequest = itemView.findViewById(R.id.order_id);
        total = itemView.findViewById(R.id.total);
        time = itemView.findViewById(R.id.time);
        countItems = itemView.findViewById(R.id.countItems);
        item_items = itemView.findViewById(R.id.item_items);

        buttonLayoutHistory = itemView.findViewById(R.id.buttonLayoutHistory);
        rate = itemView.findViewById(R.id.rate);
        reOrder = itemView.findViewById(R.id.reOrder);

        buttonLayoutOngoing = itemView.findViewById(R.id.buttonLayoutOngoing);
        cancel = itemView.findViewById(R.id.cancel);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}


public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderViewHolder> {

    List<Request> list;
    Context context;

    public MyOrderAdapter(List<Request> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public List<Request> getList() {
        return list;
    }

    public void setList(List<Request> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_my_order, parent, false);
        return new MyOrderViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyOrderViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.idRequest.setText(list.get(position).getIdRequest());
        holder.total.setText(list.get(position).getTotal());
        holder.time.setText(list.get(position).getTime());

        int countItems = 0;
        for(Order order : list.get(position).getOrders()){
            countItems += Integer.parseInt(order.getQuantity());
        }
        holder.countItems.setText(String.valueOf(countItems));
        holder.item_items.setText(list.size() == 1 ? "item" : "items");

        holder.rate.setVisibility(View.INVISIBLE);
        for (Order order : list.get(position).getOrders()) {
            if (!order.getIsRate()) {
                holder.rate.setVisibility(View.VISIBLE);
                break;
            }
        }
        holder.rate.setOnClickListener(v -> {

            Intent intent = new Intent(context, RatingFoodActivity.class);
            intent.putExtra("idRequest", list.get(position).getIdRequest());
            context.startActivity(intent);

        });

        holder.reOrder.setOnClickListener(v -> {

            List<Order> orders = list.get(position).getOrders();

            for (Order order : orders) {
                order.setCountStars(5);
                order.setIsBuy("0");
                order.setIsRate(false);
                Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).child(order.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Food food = snapshot.getValue(Food.class);
                        order.setDiscount(food.getDiscount());
                        order.setPrice(food.getPrice());
                        new Database(context).addToCart(order);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            CuteToast.ct(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();

        });

        holder.cancel.setOnClickListener(v -> {
            Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(list.get(position).getIdRequest()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Warming");
                        builder.setMessage("Bạn chắc chắn muốn hủy đơn hàng này?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(list.get(position).getIdRequest()).child("status").setValue("-1");
                                list.get(position).setStatus("-1");
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        builder.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });

        holder.itemView.setOnClickListener(v -> {
            OrderDetailFragment fragment = new OrderDetailFragment(list.get(position).getIdRequest());
            AppCompatActivity activity = (AppCompatActivity) context;
            fragment.show(activity.getSupportFragmentManager(), "OrderDetailFragment");
        });

        if (list.get(position).getStatus().equals("0")) { //ongoing
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setText(R.string.status_0);
            holder.buttonLayoutHistory.setVisibility(View.GONE);
        } else {
            holder.buttonLayoutOngoing.setVisibility(View.GONE);
            holder.status.setText(R.string.status_1);
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.xanh_chuoi));
            if (list.get(position).getStatus().equals("-1")) {
                holder.rate.setVisibility(View.INVISIBLE);
                holder.status.setText(R.string.status__1);
                holder.status.setTextColor(ContextCompat.getColor(context, R.color.Do));
            }
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
