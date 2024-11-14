package com.example.oumarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.oumarket.Class.Category;
import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Interface.ItemClickListener;
import com.example.oumarket.ViewHolder.CategoryViewHolder;
import com.example.oumarket.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.oumarket.databinding.ActivityHomeBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseDatabase database;
    DatabaseReference category, foodList;
    TextView txt_full_name;

    androidx.recyclerview.widget.RecyclerView recycler_menu, recycler_category1;
    GridLayoutManager layoutManager, layoutManager_category;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapterFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        foodList = Common.DATABASE.getReference("Foods");

        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Cart.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // init name for user
        View heardView = navigationView.getHeaderView(0);
        txt_full_name = heardView.findViewById(R.id.txt_full_name);
        txt_full_name.setText(Common.CURRENTUSER.getName());

        // load recycler menu
        recycler_menu = findViewById(R.id.recycler_menu);
        loadRecycler();

        // load recycler category
        recycler_category1 = findViewById(R.id.recycler_category1);
        loadRecyclerCategory();

    }

    private void loadRecycler() {
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class, R.layout.category_item, CategoryViewHolder.class, category) {
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

    private void loadRecyclerCategory() {
        adapterFood = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item, FoodViewHolder.class, foodList.orderByChild("MenuID").equalTo("0001")) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {

//                load image from github
                String path = food.getURL();

                Picasso.get().load(path).into(foodViewHolder.food_image);

                foodViewHolder.food_name.setText(food.getName());

                foodViewHolder.setItemClickListener((new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodDetail = new Intent(Home.this, FoodDetail.class);
                        foodDetail.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                }));
            }
        };
//        recycler_menu.setAdapter(adapter);
        SetUpRecyclerView.setupGridLayout(this, recycler_category1, adapterFood, 1, androidx.recyclerview.widget.RecyclerView.HORIZONTAL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}