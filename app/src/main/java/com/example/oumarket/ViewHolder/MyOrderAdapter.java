package com.example.oumarket.ViewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

import com.example.oumarket.Class.Request;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Interface.ItemClickListener;
import com.example.oumarket.R;
import com.example.oumarket.ui.my_order_page.OrderDetailFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
        if (list.get(position).getStatus().equals("0")) { //ongoing
            holder.status.setVisibility(View.GONE);
            holder.status.setText(R.string.status_0);
            holder.buttonLayoutHistory.setVisibility(View.GONE);
        } else { // history
            holder.buttonLayoutOngoing.setVisibility(View.GONE);
            holder.status.setText(R.string.status_1);
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.xanh_chuoi));
            if (list.get(position).getStatus().equals("-1")) {
                holder.rate.setVisibility(View.INVISIBLE);
                holder.status.setText(R.string.status__1);
                holder.status.setTextColor(ContextCompat.getColor(context, R.color.Do));
            }
        }

        holder.idRequest.setText(list.get(position).getIdRequest());
        holder.total.setText(list.get(position).getTotal());
        holder.time.setText("TIME");

        int countItems = (list.get(position).getFoods()).size();
        holder.countItems.setText(String.valueOf(countItems));
        holder.item_items.setText(list.size() == 1 ? "item" : "items");

        holder.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "rate", Toast.LENGTH_SHORT).show();
            }
        });

        holder.reOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "reOrder", Toast.LENGTH_SHORT).show();
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(list.get(position).getIdRequest()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Warming");
//                            builder.setIcon()
                            builder.setMessage("Bạn chắc chắn muốn hủy đơn hàng này?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(list.get(position).getIdRequest()).child("status").setValue("-1");
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
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailFragment fragment = new OrderDetailFragment(list.get(position).getIdRequest());

                AppCompatActivity activity = (AppCompatActivity) context;
                fragment.show(activity.getSupportFragmentManager(), "OrderDetailFragment");

            }
        });

    }

    private String status(String s) {
        if (s.equals("0")) {
            return "Đang chuẩn bị hàng";
        }
        if (s.equals("1")) {
            return "Đang vận chuyển";
        }
        return "Đã giao hàng";
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
