package com.example.oumarket.ui.my_order_page;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
import java.util.stream.Collectors;

import io.paperdb.Paper;

public class HistoryFragment extends Fragment {

    RecyclerView recyclerView;

    DatabaseReference data_requests;

    MyOrderAdapter adapter;

    final String status = "0";
    FrameLayout frameLayout;

    public HistoryFragment() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        data_requests = Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS);

        recyclerView = view.findViewById(R.id.recyclerView);

        frameLayout = view.findViewById(R.id.orderDetail);

        adapter = new MyOrderAdapter(new ArrayList<>(), getContext());
        SetUpRecyclerView.setupLinearLayout(getContext(), recyclerView, adapter);

        loadRequests();

        return view;
    }

    private void loadRequests() {
        Paper.init(getContext());

        String idCurrentUser=Paper.book().read(Common.ID_USER_KEY);

        data_requests.orderByChild("idCurrentUser").equalTo(idCurrentUser).addValueEventListener(new ValueEventListener() {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}