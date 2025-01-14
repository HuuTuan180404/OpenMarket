package com.example.oumarket.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oumarket.Activity.CartActivity;
import com.example.oumarket.Activity.SaleListActivity;
import com.example.oumarket.Activity.SearchActivity;
import com.example.oumarket.Class.Category;
import com.example.oumarket.Class.Food;
import com.example.oumarket.Helper.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.example.oumarket.Adapter.BestSellerAdapter;
import com.example.oumarket.Adapter.CategoryAdapter;
import com.example.oumarket.Adapter.FoodAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView_bestSeller, recyclerView_categories, recyclerView_all_food;
    ConstraintLayout buttonSearch;
    private ImageView imageView_cart;
    private TextView textView_topSale;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textView_topSale = view.findViewById(R.id.top_sale);
        imageView_cart = view.findViewById(R.id.imageView_cart);
        buttonSearch = view.findViewById(R.id.button_search);

        //move to searchActivity
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), SearchActivity.class));
            }
        });

        textView_topSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), SaleListActivity.class));
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

        recyclerView_bestSeller = view.findViewById(R.id.recycler_best_seller);
        recyclerView_all_food = view.findViewById(R.id.recyclerView_all_food);
        setupRecyclerViewFoods();
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

    private void setupRecyclerViewFoods() {
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Food> list = new ArrayList<>();
                List<Food> listAllFood = new ArrayList<>();
                Food food;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food = dataSnapshot.getValue(Food.class);
                    food.setId(dataSnapshot.getKey());

                    if (food.getDiscount().equals("0")) {
                        listAllFood.add(food);
                    } else list.add(food);
                }

                list.sort(Food::sortByDiscount);
                Collections.reverse(list);

                List<Food> listTopFood = list.subList(0, Math.min(Common.TOP_BEST_SELLER, list.size()));

                BestSellerAdapter bestSellerAdapter = new BestSellerAdapter(getContext(), listTopFood);
                SetUpRecyclerView.setupGridLayout(getContext(), recyclerView_bestSeller, bestSellerAdapter, 1, RecyclerView.HORIZONTAL);

                FoodAdapter foodAdapter = new FoodAdapter(listAllFood, getContext(), R.layout.item_food_list_view);
                SetUpRecyclerView.setupLinearLayout(getContext(), recyclerView_all_food, foodAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}