package com.example.oumarket.ui.home_activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.oumarket.Activity.CartActivity;
import com.example.oumarket.Activity.SearchActivity;
import com.example.oumarket.Class.Category;
import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.example.oumarket.ViewHolder.BestSellerAdapter;
import com.example.oumarket.ViewHolder.CategoryAdapter;
import com.example.oumarket.ViewHolder.FoodAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView_bestSeller, recyclerView_categories, recyclerView_all_food;
    private MaterialToolbar toolbar;
    private ImageView imageView_cart;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageView_cart = view.findViewById(R.id.imageView_cart);
        toolbar = view.findViewById(R.id.toolbar);
        //move to searchActivity
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), SearchActivity.class));
            }
        });

        //move to cartActivity
        imageView_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), CartActivity.class));
            }
        });

//        recycler categories
        recyclerView_categories = view.findViewById(R.id.recycler_categories);
        setupRecyclerCategories();

//        recycler best seller
        recyclerView_bestSeller = view.findViewById(R.id.recycler_best_saller);
        setupRecyclerViewBestSeller();

//        recycler all food
        recyclerView_all_food = view.findViewById(R.id.recyclerView_all_food);
        setupRecyclerViewAllFood();

        return view;
    }

    private void setupRecyclerCategories() {
        Common.FIREBASE_DATABASE.getReference(Common.REF_CATEGORIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category1 = dataSnapshot.getValue(Category.class);
                    category1.setId(dataSnapshot.getKey());
                    list.add(category1);
                }
                Category other = list.get(0);
                list.remove(other);
                list.add(other);

                CategoryAdapter adapter = new CategoryAdapter(list, getContext());
                SetUpRecyclerView.setupGridLayout(getContext(), recyclerView_categories, adapter, 2, androidx.recyclerview.widget.RecyclerView.HORIZONTAL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupRecyclerViewBestSeller() {
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Food> list = new ArrayList<>();
                Food food;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food = dataSnapshot.getValue(Food.class);
                    food.setId(dataSnapshot.getKey());
                    list.add(food);
                }
                List<Food> listAdapter = new ArrayList<>();
                if (!list.isEmpty()) {
                    list.sort(Food::sortByTopFood);
                    for (int i = list.size() - 1 - Common.TOP_BEST_SELLER; i < list.size(); i++) {
                        listAdapter.add(list.get(i));
                    }
                }
                Collections.reverse(listAdapter);
                BestSellerAdapter bestSellerAdapter = new BestSellerAdapter(getContext(), listAdapter);
                SetUpRecyclerView.setupGridLayout(getContext(), recyclerView_bestSeller, bestSellerAdapter, 1, RecyclerView.HORIZONTAL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupRecyclerViewAllFood() {
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Food> list = new ArrayList<>();
                Food food;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food = dataSnapshot.getValue(Food.class);
                    food.setId(dataSnapshot.getKey());
                    list.add(food);
                }
                List<Food> listAdapter = new ArrayList<>();
                if (!list.isEmpty()) {
                    list.sort(Food::sortByTopFood);
                    for (int i = 0; i < list.size() - Common.TOP_BEST_SELLER; i++) {
                        listAdapter.add(list.get(i));
                    }
                }
                Collections.shuffle(listAdapter);
                FoodAdapter foodAdapter = new FoodAdapter(listAdapter, getContext(), R.layout.item_food_grid_view);
                SetUpRecyclerView.setupGridLayout(getContext(), recyclerView_all_food, foodAdapter, 2, androidx.recyclerview.widget.RecyclerView.VERTICAL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}