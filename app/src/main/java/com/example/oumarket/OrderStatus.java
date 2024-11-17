package com.example.oumarket;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Request;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

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

        databaseReference = Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS);

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