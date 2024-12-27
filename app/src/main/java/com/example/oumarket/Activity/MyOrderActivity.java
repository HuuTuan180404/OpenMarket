package com.example.oumarket.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import com.example.oumarket.Adapter.ViewPagerMyOrderAdapter;
import com.example.oumarket.Helper.Customer_LoadingDialog;
import com.example.oumarket.Class.User;
import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MyOrderActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager2;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_order);

        Paper.init(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);

        ViewPagerMyOrderAdapter viewPageMyOrderAdapter = new ViewPagerMyOrderAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        if (Common.CURRENTUSER == null) {

            Customer_LoadingDialog dialog = new Customer_LoadingDialog(this, "Loading...");
            dialog.show();

            String user = Paper.book().read(Common.USERNAME_KEY);
            String password = Paper.book().read(Common.PASSWORD_KEY);

            FirebaseAuth.getInstance().signInWithEmailAndPassword(user, password).addOnCompleteListener(MyOrderActivity.this, task -> {

                if (task.isSuccessful()) {
                    String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            Common.CURRENTUSER = user;

                            viewPager2.setAdapter(viewPageMyOrderAdapter);

                            tabLayout.setupWithViewPager(viewPager2);
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    dialog.dismiss();
                }
            });

        } else {

            viewPager2.setAdapter(viewPageMyOrderAdapter);

            tabLayout.setupWithViewPager(viewPager2);
        }

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