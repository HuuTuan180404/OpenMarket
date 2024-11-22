package com.example.oumarket;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.oumarket.ui.requests_class.FragmentRequests;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class Requests extends AppCompatActivity {

    private Toolbar toolbar;

    BottomNavigationView bottomNavigationView;

    FragmentRequests fragmentRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        toolbar = findViewById(R.id.toolbar_Requests);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.app_name);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.item_prepare) {
                    Toast.makeText(Requests.this, "item_prepare", Toast.LENGTH_SHORT).show();
                    toolbar.setTitle(R.string.title_prepare);
                    fragmentRequests.setStatus("0");
                } else if (id == R.id.item_delivery) {
                    Toast.makeText(Requests.this, "item_delivery", Toast.LENGTH_SHORT).show();
                    fragmentRequests.setStatus("1");
                    toolbar.setTitle(R.string.title_delivery);
                } else if (id == R.id.item_delivered) {
                    Toast.makeText(Requests.this, "item_delivered", Toast.LENGTH_SHORT).show();
                    toolbar.setTitle(R.string.title_delivered);
                    fragmentRequests.setStatus("2");
                } else {
                    toolbar.setTitle(R.string.app_name);
                }

                return false;
            }
        });

        fragmentRequests = new FragmentRequests();

        loadFragment(fragmentRequests);

    }

    private void loadFragment(FragmentRequests fragmentRequests) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frg_Requests, fragmentRequests);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}