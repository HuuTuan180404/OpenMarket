package com.example.oumarket.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.oumarket.Class.User;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Fragment.OrderFragment;
import com.example.oumarket.Fragment.ProfileFragment;
import com.example.oumarket.R;
import com.example.oumarket.Fragment.HomeFragment;
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
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private BottomNavigationView bottom_navigation_view;
    HomeFragment homeFragment;
    FragmentManager fragmentManager;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentManager = getSupportFragmentManager();

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();

        requestPermissions();
        replaceFragment(homeFragment, "HomeFragment");
        initView();
    }

    private void initView() {
        bottom_navigation_view = findViewById(R.id.bottom_navigation_view);
        bottom_navigation_view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_home) {
                    Fragment currentFragment = fragmentManager.findFragmentByTag("HomeFragment");
                    if (currentFragment!=null) {
                        if (currentFragment instanceof HomeFragment) {
                            NestedScrollView scrollView = currentFragment.getView().findViewById(R.id.nested_scroll_view);
                            if (scrollView != null) {
                                scrollView.smoothScrollTo(0, 0);
                            }
                        }
                    } else replaceFragment(homeFragment, "HomeFragment");
                    return true;
                } else if (item.getItemId() == R.id.nav_orders) {
                    replaceFragment(new OrderFragment(), "OrderFragment");
                } else if (item.getItemId() == R.id.nav_profile) {
                    replaceFragment(profileFragment, "ProfileFragment");
                    return true;
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction().replace(R.id.fragment_home, fragment, tag).commit();
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

        // Nếu còn quyền chưa được cấp, yêu cầu tất cả trong một lần+
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        }

    }
}

