package com.example.oumarket.ui.my_order_page;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.paperdb.Paper;

public class OngoingFragment extends Fragment {

    RecyclerView recyclerView;

    DatabaseReference data_requests;

    MyOrderAdapter adapter;

    TextView null_orders;

    final String status = "0";

    FrameLayout frameLayout;

    public OngoingFragment() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ongoing, container, false);

        frameLayout = view.findViewById(R.id.orderDetail);

        data_requests = Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS);

        recyclerView = view.findViewById(R.id.recyclerView);

        adapter = new MyOrderAdapter(new ArrayList<>(), getContext());
        SetUpRecyclerView.setupLinearLayout(getContext(), recyclerView, adapter);

        null_orders = view.findViewById(R.id.null_orders);

        loadRequests();

        return view;
    }

    private void loadRequests() {
        Paper.init(getContext());

        String idCurrentUser = Common.CURRENTUSER.getIdUser();

        data_requests.orderByChild("idCurrentUser").equalTo(idCurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Request> data = new ArrayList<>();
                Request request;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    request = dataSnapshot.getValue(Request.class);
                    if (request.getStatus().equals(status)) {
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