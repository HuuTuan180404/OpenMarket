package com.example.oumarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.oumarket.Common.Common;
import com.example.oumarket.ui.home_class.FragmentHome;
import com.example.oumarket.ui.home_class.FragmentHomeSearch;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oumarket.databinding.ActivityHomeBinding;

import io.paperdb.Paper;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    TextView txt_full_name;

    FrameLayout frameLayout_home, frameLayout_search;
    SearchView searchView;
    FragmentHomeSearch fragmentHomeSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Paper.init(this);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        content home

        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Home.this, Requests.class);
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
                    Intent intent = new Intent(Home.this, Requests.class);
                    startActivity(intent);
                } else if (id == R.id.nav_log_out) {

                    Paper.book().destroy();

                    Intent intent = new Intent(Home.this, Signin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        fragmentHomeSearch = new FragmentHomeSearch();

        frameLayout_home = findViewById(R.id.fragment_home_categories);
        frameLayout_search = findViewById(R.id.fragment_search);
        vissibaleFragmentSearch();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_categories, new FragmentHome()).commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_search, fragmentHomeSearch).commit();

//        init name for user
        View heardView = navigationView.getHeaderView(0);
        txt_full_name = heardView.findViewById(R.id.txt_full_name);
        txt_full_name.setText(Common.CURRENTUSER.getName());

    }

//    private void setupRecyclerCategory() {
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
//
//
//    }

    private void vissibaleFragmentSearch() {
        frameLayout_home.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Ẩn fragmentContainer2 nếu nó đang hiển thị
                    if (frameLayout_search.getVisibility() == View.VISIBLE) {
                        frameLayout_search.setVisibility(View.GONE);
                    }
                    return true;
                }
                return false;
            }
        });

        frameLayout_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;
                }
                return false;
            }
        });
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
                if (query.trim().isEmpty()) {
                    frameLayout_search.setVisibility(View.GONE);
                } else {
                    fragmentHomeSearch.setText(query.toLowerCase());
                    frameLayout_search.setVisibility(View.VISIBLE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty()) {
                    frameLayout_search.setVisibility(View.GONE);

                } else {
                    fragmentHomeSearch.setText(newText.toLowerCase());
                    frameLayout_search.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            frameLayout_search.setVisibility(View.GONE);
            searchView.onActionViewCollapsed();
            return true;
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return true;
    }
}