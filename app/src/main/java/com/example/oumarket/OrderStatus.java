package com.example.oumarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.Request;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Interface.ItemClickListener;
import com.example.oumarket.ViewHolder.FoodViewHolder;
import com.example.oumarket.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference databaseReference;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_status);

        databaseReference = Common.DATABASE.getReference("Requests");

        recyclerView = findViewById(R.id.recycler_order);

        loadOrders(Common.CURRENTUSER.getPhone());
    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class, R.layout.item_order, OrderViewHolder.class, databaseReference.orderByChild("Phone").equalTo(phone)) {

            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request request, int i) {
                orderViewHolder.tvOrderId.setText(adapter.getRef(i).getKey());
                orderViewHolder.tvOrderStatus.setText(status(request.getStatus()));
                orderViewHolder.tvOrderPhone.setText(request.getPhone());
                orderViewHolder.tvOrderAddress.setText(request.getAddress());
            }
        };

        SetUpRecyclerView.setupLinearLayout(OrderStatus.this, recyclerView, adapter);
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


}