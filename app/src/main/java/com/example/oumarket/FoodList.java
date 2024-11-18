package com.example.oumarket;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Category;
import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.ViewHolder.FoodAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    DatabaseReference foodList;

    RecyclerView recyclerView;

    String categoryId = "";

    SearchView searchView;
    Toolbar toolbar;
//    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    FoodAdapter adapter;

    List<Food> allSuggest = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_list);


        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Category");
        setSupportActionBar(toolbar);

        // Hiển thị nút mũi tên trở về
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//         firebase
        foodList = Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS);

        recyclerView = findViewById(R.id.recycler_food);

        searchView = findViewById(R.id.search_bar_food_list);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

//        get intent here
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        if (!categoryId.isEmpty() && categoryId != "") {
            setupRecycler(categoryId);
        }

//        titel
        Common.FIREBASE_DATABASE.getReference(Common.REF_CATEGORIES).child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(Category.class).getName();
                getSupportActionBar().setTitle(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setupRecycler(categoryId);

    }

    private void filter(String text) {
        foodList.orderByChild("MenuID").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Food> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    if (food.getName().toLowerCase().contains(text)) {
                        food.setId(dataSnapshot.getKey());
                        list.add(food);
                    }
                }

                if (!list.isEmpty()) {
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupRecycler(String categoryId) {
        foodList.orderByChild("MenuID").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Food> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    food.setId(dataSnapshot.getKey());
                    list.add(food);
                }

                adapter = new FoodAdapter(list, FoodList.this);
                SetUpRecyclerView.setupGridLayout(FoodList.this, recyclerView, adapter, 2, RecyclerView.VERTICAL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}