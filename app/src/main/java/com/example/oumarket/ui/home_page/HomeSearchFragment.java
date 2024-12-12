package com.example.oumarket.ui.home_page;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oumarket.Class.Category;
import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.example.oumarket.ViewHolder.CategoryAdapter;
import com.example.oumarket.ViewHolder.FilterCategoriesAdapter;
import com.example.oumarket.ViewHolder.FilterFoodsAdapter;
import com.example.oumarket.ViewHolder.FoodAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeSearchFragment extends Fragment {

    private String keySearch = "";

    RecyclerView recycler_foods, recycler_categories;

    FilterCategoriesAdapter categoryAdapter;

    FilterFoodsAdapter foodAdapter;

    TextView tv_noData;

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
        filterCategories();
        filterFoods();
    }

    public HomeSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_search, container, false);

        tv_noData = view.findViewById(R.id.tv_noData);

        recycler_categories = view.findViewById(R.id.recycler_categories);
        recycler_foods = view.findViewById(R.id.recycler_foods);

        categoryAdapter = new FilterCategoriesAdapter(getContext(), new ArrayList<>());
        SetUpRecyclerView.setupGridLayout(getContext(), recycler_categories, categoryAdapter, 1, androidx.recyclerview.widget.RecyclerView.HORIZONTAL);

        foodAdapter = new FilterFoodsAdapter(getContext(), new ArrayList<>());
        SetUpRecyclerView.setupGridLayout(getContext(), recycler_foods, foodAdapter, 1, androidx.recyclerview.widget.RecyclerView.HORIZONTAL);

        return view;
    }

    private void filterCategories() {
        Common.FIREBASE_DATABASE.getReference(Common.REF_CATEGORIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category1 = dataSnapshot.getValue(Category.class);
                    if (category1.getName().toLowerCase().contains(keySearch)) {
                        category1.setId(dataSnapshot.getKey());
                        list.add(category1);
                    }
                }

                if (list.isEmpty()) {
                    tv_noData.setVisibility(View.VISIBLE);
                    recycler_categories.setVisibility(View.GONE);
                } else {
                    tv_noData.setVisibility(View.GONE);
                    categoryAdapter.setList(list);
                    categoryAdapter.notifyDataSetChanged();
                    recycler_categories.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void filterFoods() {
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Food> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    if (food.getName().toLowerCase().contains(keySearch)) {
                        food.setId(dataSnapshot.getKey());
                        list.add(food);
                    }
                }

                if (list.isEmpty()) {
                    tv_noData.setVisibility(View.VISIBLE);
                    recycler_foods.setVisibility(View.GONE);
                } else {
                    tv_noData.setVisibility(View.GONE);
                    foodAdapter.setList(list);
                    foodAdapter.notifyDataSetChanged();
                    recycler_foods.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}