package com.example.oumarket.ui.my_order_activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.oumarket.Class.Request;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.example.oumarket.ViewHolder.MyOrderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.paperdb.Paper;

public class HistoryFragment extends Fragment {

    RecyclerView recyclerView;

    MyOrderAdapter adapter;

    final String status = "0";
    FrameLayout frameLayout;

    TextView null_orders;

    public HistoryFragment() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        null_orders = view.findViewById(R.id.null_orders);

        frameLayout = view.findViewById(R.id.orderDetail);

        adapter = new MyOrderAdapter(new ArrayList<>(), getContext());
        SetUpRecyclerView.setupLinearLayout(getContext(), recyclerView, adapter);

        loadRequests();

        return view;
    }

    private void loadRequests() {
        Paper.init(getContext());

        String idCurrentUser = Common.CURRENTUSER.getIdUser();

        Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).orderByChild("idCurrentUser").equalTo(idCurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Request> data = new ArrayList<>();
                Request request;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    request = dataSnapshot.getValue(Request.class);
                    if (!request.getStatus().equals(status)) {
                        request.setIdRequest(dataSnapshot.getKey());
                        data.add(request);
                    }
                }

                Collections.reverse(data);

                adapter.setList(data);
                adapter.notifyDataSetChanged();

                if (adapter.getList().isEmpty()) null_orders.setVisibility(View.VISIBLE);
                else null_orders.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}