package com.example.oumarket.ui.home_class;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oumarket.Class.Category;
import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.example.oumarket.ViewHolder.CategoryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentHomeSearch extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private String text;

    List<Food> foods = new ArrayList<>();

    DatabaseReference data_categories, data_foods;

    RecyclerView recyclerView;

    CategoryAdapter adapter;

    public List<Food> getFoods() {
        return foods;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        filter();
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public FragmentHomeSearch(String s) {
        this.text = s;
    }

    public static FragmentHomeSearch newInstance(String param1, String param2) {
        FragmentHomeSearch fragment = new FragmentHomeSearch("");
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
        View view = inflater.inflate(R.layout.fragment_home_search, container, false);


        data_categories = Common.FIREBASE_DATABASE.getReference(Common.REF_CATEGORIES);

        data_foods = Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS);

        recyclerView = view.findViewById(R.id.recycler_filter);

        adapter = new CategoryAdapter(new ArrayList<>(), getContext());
        SetUpRecyclerView.setupGridLayout(getContext(), recyclerView, adapter, 1, androidx.recyclerview.widget.RecyclerView.HORIZONTAL);

        return view;
    }

    private void filter() {
        data_categories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category1 = dataSnapshot.getValue(Category.class);
                    if (category1.getName().toLowerCase().contains(text)) {
                        category1.setId(dataSnapshot.getKey());
                        list.add(category1);
                    }
                }
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}