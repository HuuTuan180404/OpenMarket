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
import com.example.oumarket.ViewHolder.FoodAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FragmentHome extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    DatabaseReference data_categories, data_foods;


    public FragmentHome() {
    }

    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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

//        app:layoutManager="androidx.recyclerview.widget.GridLayoutManager"
//        app:spanCount="2"

//        app:layoutManager="androidx.recyclerview.widget.GridLayoutManager"
//        app:spanCount="1"

        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        firebase
        data_categories = Common.FIREBASE_DATABASE.getReference(Common.REF_CATEGORIES);
        data_foods = Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS);

//        recycler categories
        setupRecyclerCategories(view);

//        recycler category
        setupRecyclerOneCategory(view, 1, 5);

        return view;
    }

    private void setupRecyclerCategories(View view) {

        RecyclerView recycler_categories = view.findViewById(R.id.recycler_categories);

        data_categories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category1 = dataSnapshot.getValue(Category.class);
                    category1.setId(dataSnapshot.getKey());
                    list.add(category1);
                }
                CategoryAdapter adapter = new CategoryAdapter(list, getContext());
                SetUpRecyclerView.setupGridLayout(getContext(), recycler_categories, adapter, 2, androidx.recyclerview.widget.RecyclerView.HORIZONTAL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupRecyclerOneCategory(View view, int n_category, int n_food) {
        data_categories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (!dataSnapshot.getKey().equals("9999")) {
                        Category category1 = dataSnapshot.getValue(Category.class);
                        category1.setId(dataSnapshot.getKey());
                        list.add(category1);
                    }
                }

//                random
                Random random = new Random();
                int n = n_category % list.size();
                while (list.size() != n) {
                    list.remove(random.nextInt(list.size()));
                }


                for (int i = 0; i < n_category; i++) {
                    loadN_Food(view, list.get(i), n_food);
                }

//                adapter = new CategoryAdapter(list, getContext());
//                SetUpRecyclerView.setupGridLayout(getContext(), recycler_categories, adapter, 2, androidx.recyclerview.widget.RecyclerView.HORIZONTAL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadN_Food(View view, Category category, int n_food) {
//        init recycler
        View view_recycler = view.findViewById(R.id.include_category1);
        RecyclerView recyclerView = view_recycler.findViewById(R.id.recycler_category);

//        get data
        data_foods.orderByChild("MenuID").equalTo(category.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Food> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    food.setId(dataSnapshot.getKey());
                    list.add(food);
                }

//                random
                Random random = new Random();
                int n = n_food % list.size();
                while (list.size() != n) {
                    list.remove(random.nextInt(list.size()));
                }

                FoodAdapter adapter = new FoodAdapter(list, getContext());
                SetUpRecyclerView.setupGridLayout(getContext(), recyclerView, adapter, 1, RecyclerView.HORIZONTAL);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}