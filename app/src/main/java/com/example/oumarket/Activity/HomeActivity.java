package com.example.oumarket.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.oumarket.Class.User;
import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.example.oumarket.ui.home_activity.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;

    Toolbar toolbar;
    FloatingActionButton fab, btn_scrollView;
    BottomNavigationView bottom_navigation_view;
    FrameLayout frameLayout_home, frameLayout_search;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);
        requestPermissions();

        if (Common.CURRENTUSER == null) {
            String user = Paper.book().read(Common.USERNAME_KEY);
            String password = Paper.book().read(Common.PASSWORD_KEY);

            if (user != null && password != null) {
                if (!user.isEmpty() && !password.isEmpty()) {
                    login(user, password);
                }
            } else {
                Intent intent = new Intent(HomeActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            }
        }

        replaceFragment(new HomeFragment());
        initView();
    }

    private void initView() {

//        btn_scrollView = findViewById(R.id.btn_scrollView);

//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        fab = findViewById(R.id.fab);
//        fab.setOnClickListener(v -> {
//            Intent intent = new Intent(HomeActivity.this, Cart.class);
//            startActivity(intent);
//        });
        bottom_navigation_view = findViewById(R.id.bottom_navigation_view);
        bottom_navigation_view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    replaceFragment(new HomeFragment());
                    return true;
                } else if (item.getItemId() == R.id.nav_addresses) {
                    Intent intent = new Intent(HomeActivity.this, YourAddressesActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_orders) {
                    Intent intent = new Intent(HomeActivity.this, MyOrderActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_profile) {
                    Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
//                if(item.getItemId() == R.id.nav_order){
//                    replaceFragment(new OrderFragment());
//                    return true;
//                }if(item.getItemId() == R.id.nav_account){
//                    replaceFragment(new AccountFragment());
//                    return true;
//                }
                return true;
            }
        });
//        bottom_navigation_view.setOnItemSelectedListener(menuItem -> {
//            int id = menuItem.getItemId();
//            if (id == R.id.nav_addresses) {
//                Intent intent = new Intent(HomeActivity.this, YourAddressesActivity.class);
//                startActivity(intent);
//            } else if (id == R.id.nav_orders) {
//                Intent intent = new Intent(HomeActivity.this, MyOrder.class);
//                startActivity(intent);
//            } else if (id == R.id.nav_profile) {
//                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
//                startActivity(intent);
//            }
//
//            return true;
//        });

//        homeSearchFragment = new HomeSearchFragment();
//        frameLayout_home = findViewById(R.id.fragment_home_categories);
//        frameLayout_search = findViewById(R.id.fragment_search);
//        vissibaleFragmentSearch();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_categories, new HomeFragment()).commit();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_search, homeSearchFragment).commit();
//
//        btn_scrollView.setOnClickListener(v -> {
//            scrollToTopOfCurrentFragment();
//        });
    }

//    private void scrollToTopOfCurrentFragment() {
//        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_home_categories);
//        if (currentFragment instanceof HomeFragment) {
//            NestedScrollView scrollView = currentFragment.getView().findViewById(R.id.nested_scroll_view);
//            if (scrollView != null) {
//                scrollView.smoothScrollTo(0, 0);
//            }
//        }
//    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_home, fragment)
                .commit();
    }

    private void login(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(HomeActivity.this, task -> {
            if (task.isSuccessful()) {
                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Common.CURRENTUSER = user;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } else {
                Intent intent = new Intent(HomeActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void requestPermissions() {
        List<String> permissionsToRequest = new ArrayList<>();

        // Kiểm tra quyền đọc bộ nhớ
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(android.Manifest.permission.READ_MEDIA_IMAGES);
        }

        // Kiểm tra quyền truy cập vị trí
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(android.Manifest.permission.POST_NOTIFICATIONS);
        }

        // Nếu còn quyền chưa được cấp, yêu cầu tất cả trong một lần
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        }

    }

//    private void vissibaleFragmentSearch() {
//        frameLayout_home.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    // Ẩn fragmentContainer2 nếu nó đang hiển thị
//                    if (frameLayout_search.getVisibility() == View.VISIBLE) {
//                        frameLayout_search.setVisibility(View.GONE);
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        frameLayout_search.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    return true;
//                }
//                return false;
//            }
//        });
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_toolbar_home, menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//        searchView = (SearchView) item.getActionView();
//        searchView.clearFocus();
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if (query.trim().isEmpty()) {
//                    frameLayout_search.setVisibility(View.GONE);
//                } else {
//                    homeSearchFragment.setKeySearch(query.toLowerCase());
//                    frameLayout_search.setVisibility(View.VISIBLE);
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (newText.trim().isEmpty()) {
//                    frameLayout_search.setVisibility(View.GONE);
//
//                } else {
//                    homeSearchFragment.setKeySearch(newText.toLowerCase());
//                    frameLayout_search.setVisibility(View.VISIBLE);
//                }
//                return true;
//            }
//        });
//
//        searchView.setOnCloseListener(() -> {
//            frameLayout_search.setVisibility(View.GONE);
//            searchView.onActionViewCollapsed();
//            return true;
//        });
//
//        MenuItem item_about_me = menu.findItem(R.id.action_about_me);
//        item_about_me.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(@NonNull MenuItem item) {
//                startActivity(new Intent(Home.this, AboutMeActivity.class));
//                return false;
//            }
//        });
//
//        MenuItem log_out = menu.findItem(R.id.action_log_out);
//        log_out.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(@NonNull MenuItem item) {
//                Paper.book().destroy();
//                Intent intent = new Intent(Home.this, Signin.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                return true;
//            }
//        });
//
//        return true;
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        return true;
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        bottom_navigation_view.setSelectedItemId(R.id.nav_home);
//    }
}

