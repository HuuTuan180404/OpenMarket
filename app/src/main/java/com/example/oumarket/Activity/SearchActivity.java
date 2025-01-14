package com.example.oumarket.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Adapter.SearchFoodAdapter;
import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.User;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Helper.ActionSetting;
import com.example.oumarket.Interface.BottomSheetDialogSave;
import com.example.oumarket.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements BottomSheetDialogSave {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView tv_noData;

    SearchFoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_food);
        loadListFood();

        tv_noData = findViewById(R.id.tv_noData);
    }

    private void loadListFood() {
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Food> list = new ArrayList<>();
                Food food;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food = dataSnapshot.getValue(Food.class);
                    food.setId(dataSnapshot.getKey());
                    list.add(food);
                }

                Collections.shuffle(list);

                if (list.size() > 0) {
                    recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
                    adapter = new SearchFoodAdapter(list);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String removeDiacritics(String input) {
        // Chuẩn hóa chuỗi Unicode thành dạng Normal Form Decomposed (NFD)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        // Loại bỏ các dấu bằng cách chỉ giữ lại các ký tự không thuộc nhóm ký tự tổ hợp (diacritics)
        return normalized.replaceAll("\\p{M}", "");
    }

    private void filter(String text) {
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Food> list = new ArrayList<>();
                Food food;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food = dataSnapshot.getValue(Food.class);
                    if (removeDiacritics(food.getName().toLowerCase().trim()).contains(text) || food.getName().toLowerCase().trim().contains(text)) {
                        food.setId(dataSnapshot.getKey());
                        list.add(food);
                    }
                }

                Collections.shuffle(list);

                if (list.isEmpty()) {
                    tv_noData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tv_noData.setVisibility(View.GONE);
                    adapter.setItems(list);
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_search_activity, menu);

        MenuItem item_search = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) item_search.getActionView();
        searchView.setQueryHint("Tìm món ngon");
        searchView.setIconified(false); // Mở rộng SearchView
        searchView.requestFocus();     // Focus vào ô tìm kiếm

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_setting) {
            ActionSetting actionSetting = new ActionSetting(true);
            actionSetting.show(getSupportFragmentManager(), "Action_Setting");
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
        }
    }

    private void sortByComparator(Comparator<Food> comparator) {
        List<Food> list = adapter.getItems();
        list.sort(comparator);
        adapter.setItems(list);
    }
}