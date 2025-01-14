package com.example.oumarket.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Category;
import com.example.oumarket.Class.Food;
import com.example.oumarket.Helper.SetUpRecyclerView;
import com.example.oumarket.Class.User;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Interface.BottomSheetDialogSave;
import com.example.oumarket.R;
import com.example.oumarket.Adapter.FoodAdapter;
import com.example.oumarket.Helper.ActionSetting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FoodListActivity extends AppCompatActivity implements BottomSheetDialogSave{

    DatabaseReference data_foods;

    RecyclerView recyclerView;

    String categoryId;

    Toolbar toolbar;
    FoodAdapter adapter;

    TextView tv_noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_list);

        tv_noData = findViewById(R.id.tv_noData);

        toolbar = findViewById(R.id.toolbar_FoodList);
        setSupportActionBar(toolbar);

//         firebase
        data_foods = Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS);

        recyclerView = findViewById(R.id.recycler_food);

//        get intent here
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("categoryId");
        }
        if (!categoryId.isEmpty() && categoryId != null) {
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
        data_foods.orderByChild("categoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Food> list = new ArrayList<>();
                Food food;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food = dataSnapshot.getValue(Food.class);
                    if (food.getName().toLowerCase().trim().contains(text)) {
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
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupRecycler(String categoryId) {
        data_foods.orderByChild("categoryId").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Food> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    food.setId(dataSnapshot.getKey());
                    list.add(food);
                }
                Collections.shuffle(list);
                adapter = new FoodAdapter(list, FoodListActivity.this, R.layout.item_food_list_view);
                SetUpRecyclerView.setupLinearLayout(FoodListActivity.this, recyclerView, adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_food_list, menu);

        MenuItem item_search = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) item_search.getActionView();
        searchView.setQueryHint("Input");
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query.toLowerCase().trim());

                // Ẩn bàn phím
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        searchView.clearFocus();
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText.toLowerCase().trim());
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
        if (item.getItemId() == R.id.action_setting) {
            ActionSetting fragment = new ActionSetting();
            fragment.show(getSupportFragmentManager(), "Action_Setting");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void inFoodListActivity(String key) {
        switch (key) {
            case "price_desc":
                sortByComparator((a, b) -> -a.sortByPrice(b));
                break;
            case "price_asc":
                sortByComparator((a, b) -> a.sortByPrice(b));
                break;
            case "name":
                sortByComparator((a, b) -> -a.sortByName(b));
                break;
            case "rating":
                sortByComparator((a, b) -> -a.sortByRating(b));
                break;
            case "list_view":
                adapter.setItem_layout(R.layout.item_food_list_view);
                SetUpRecyclerView.setupLinearLayout(this, recyclerView, adapter);
                break;
            case "grid_view":
                adapter.setItem_layout(R.layout.item_food_grid_view);
                SetUpRecyclerView.setupGridLayout(this, recyclerView, adapter, 2, RecyclerView.VERTICAL);
                break;
        }
    }

    private void sortByComparator(Comparator<Food> comparator) {
        List<Food> list = adapter.getList();
        list.sort(comparator);
        adapter.setList(list);
    }

}