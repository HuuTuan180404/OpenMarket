package com.example.oumarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oumarket.Class.Category;
import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Interface.ItemClickListener;
import com.example.oumarket.ViewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oumarket.databinding.ActivityHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    DatabaseReference category, foodList;
    TextView txt_full_name;

    androidx.recyclerview.widget.RecyclerView recycler_menu, recycler_category1;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;
//    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapterFood, foodSuggest;

    List<Food> foods = new ArrayList<>();

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        category = Common.FIREBASE_DATABASE.getReference(Common.REF_CATEGORIES);

        foodList = Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS);

        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Cart.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = binding.drawerLayout;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, binding.appBarHome.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_menu) {
                } else if (id == R.id.nav_cart) {
                    Intent intent = new Intent(Home.this, Cart.class);
                    startActivity(intent);
                } else if (id == R.id.nav_orders) {
                    Intent intent = new Intent(Home.this, OrderStatus.class);
                    startActivity(intent);
                } else if (id == R.id.nav_log_out) {
                    Intent intent = new Intent(Home.this, Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // init name for user
        View heardView = navigationView.getHeaderView(0);
        txt_full_name = heardView.findViewById(R.id.txt_full_name);
        txt_full_name.setText(Common.CURRENTUSER.getName());

        // load recycler menu
        recycler_menu = findViewById(R.id.recycler_menu);
        setupRecyclerCategories();

        // load recycler category
        recycler_category1 = findViewById(R.id.recycler_category1);
        setupRecyclerCategory();

        loadAllFood();

    }

    private void setupRecyclerCategories() {
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class, R.layout.item_category, CategoryViewHolder.class, category) {
            @Override
            protected void populateViewHolder(CategoryViewHolder menuViewHolder, Category category, int i) {
                // load theo firebase storage
                String path = category.getURL();
                Picasso picasso = new Picasso.Builder(Home.this).build();
                picasso.load(path).into(menuViewHolder.imageView);
                menuViewHolder.txtMenuName.setText(category.getName());

//                final Category clickItem = category;
                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodList = new Intent(Home.this, FoodList.class);
                        foodList.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });
            }
        };
        SetUpRecyclerView.setupGridLayout(this, recycler_menu, adapter, 2, androidx.recyclerview.widget.RecyclerView.HORIZONTAL);
    }

    private void setupRecyclerCategory() {
//        adapterFood = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.item_food, FoodViewHolder.class, foodList.orderByChild("MenuID").equalTo("0001")) {
//            @Override
//            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
//
////                load image from github
//                String path = food.getURL();
//
//                Picasso.get().load(path).into(foodViewHolder.food_image);
//
//                foodViewHolder.food_name.setText(food.getName());
//
//                foodViewHolder.setItemClickListener((new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                        Intent foodDetail = new Intent(Home.this, FoodDetail.class);
//                        foodDetail.putExtra("FoodId", adapter.getRef(position).getKey());
//                        startActivity(foodDetail);
//                    }
//                }));
//            }
//        };
//        SetUpRecyclerView.setupGridLayout(this, recycler_category1, adapterFood, 1, androidx.recyclerview.widget.RecyclerView.HORIZONTAL);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterList(newText.toLowerCase());

                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            searchView.onActionViewCollapsed(); // Đóng SearchView
            return true;
        });

        return true;
    }

    private void filterList(String text) {
        List<Food> filter = new ArrayList<>();
        for (Food food : foods) {
            if (food.getName().toLowerCase().contains(text)) {
                filter.add(food);
            }
        }

        if (filter.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {

        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void loadAllFood() {
        foodList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    foods.add(food);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}