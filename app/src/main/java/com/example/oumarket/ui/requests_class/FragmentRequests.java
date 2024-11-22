package com.example.oumarket.ui.requests_class;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oumarket.Class.Request;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.example.oumarket.ViewHolder.RequestAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentRequests extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    RecyclerView recyclerView;

    DatabaseReference data_requests;

    RequestAdapter adapter;

    String status = "0";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        loadRequests("123456");
    }

    public FragmentRequests() {

    }

    public static FragmentRequests newInstance(String param1, String param2) {
        FragmentRequests fragment = new FragmentRequests();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        data_requests = Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS);
//
        recyclerView = view.findViewById(R.id.recycler_fgm_re);

        adapter = new RequestAdapter(new ArrayList<>(), getContext());
        SetUpRecyclerView.setupLinearLayout(getContext(), recyclerView, adapter);

        loadRequests("123456");

        return view;
    }

    private void loadRequests(String phone) {
        Log.d("ZZZZZ", "loadRequests()");
        data_requests.orderByChild("phone").equalTo(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Request> data = new ArrayList<>();
                Request request;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    request = dataSnapshot.getValue(Request.class);
                    if (request.getStatus().equals(status)) {
                        request.setId(dataSnapshot.getKey());
                        data.add(request);
                    }
                }

                adapter.setList(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}