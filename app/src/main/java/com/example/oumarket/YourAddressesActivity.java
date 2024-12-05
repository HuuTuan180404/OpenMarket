package com.example.oumarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.AnAddress;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Class.Ward;
import com.example.oumarket.Common.AddressType;
import com.example.oumarket.Common.Common;
import com.example.oumarket.ViewHolder.AnAddressAdapter;

import org.checkerframework.checker.units.qual.A;

public class YourAddressesActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    AppCompatButton button;
    TextView addressesIsNull;

    AnAddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_your_addresses);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        addressesIsNull = findViewById(R.id.addressesIsNull);

        if (Common.CURRENTUSER.getAddresses() == null) {
            addressesIsNull.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            addressesIsNull.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new AnAddressAdapter(this, Common.CURRENTUSER.getAddresses());
            SetUpRecyclerView.setupLinearLayout(this, recyclerView, adapter);
        }

        button = findViewById(R.id.btn_add_address);
        button.setOnClickListener((v) -> {
            Intent intent = new Intent(YourAddressesActivity.this, MapsActivity.class);
            startActivity(intent);
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}