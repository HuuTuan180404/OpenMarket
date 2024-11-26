package com.example.oumarket;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
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

    DatabaseReference data_foods;

    RecyclerView recyclerView;

    String categoryId = "";

    SearchView searchView;
    Toolbar toolbar;
    FoodAdapter adapter;

    TextView tv_noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.page_food_list);

        tv_noData = findViewById(R.id.tv_noData);

        toolbar = findViewById(R.id.toolbar_FoodList);

        toolbar.setTitle("Category");
        setSupportActionBar(toolbar);

//         firebase
        data_foods = Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS);

        recyclerView = findViewById(R.id.recycler_food);

//        get intent here
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        if (!categoryId.isEmpty() && categoryId != "") {
            setupRecycler(categoryId);
        }

//        title
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
        data_foods.orderByChild("MenuID").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
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

                if (list.isEmpty()) {
                    tv_noData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tv_noData.setVisibility(View.GONE);
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupRecycler(String categoryId) {
        data_foods.orderByChild("MenuID").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_food_list, menu);

        MenuItem item = menu.findItem(R.id.action_search);

        searchView = (SearchView) item.getActionView();

        searchView.clearFocus();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query.toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText.toLowerCase());
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            searchView.onActionViewCollapsed();
            return true;
        });

        return true;
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