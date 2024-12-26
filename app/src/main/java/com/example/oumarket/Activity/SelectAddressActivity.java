package com.example.oumarket.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.example.oumarket.ViewHolder.SelectAddressAdapter;

public class SelectAddressActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    AppCompatButton button;
    TextView addressesIsNull;

    SelectAddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_address);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        addressesIsNull = findViewById(R.id.addressesIsNull);

        if (Common.CURRENTUSER.getAddresses() == null || Common.CURRENTUSER.getAddresses().isEmpty()) {
            addressesIsNull.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            addressesIsNull.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new SelectAddressAdapter(this, Common.CURRENTUSER.getAddresses());
            SetUpRecyclerView.setupLinearLayout(this, recyclerView, adapter);
        }

        button = findViewById(R.id.btn_add_address);
        button.setOnClickListener((v) -> {
            Intent intent = new Intent(SelectAddressActivity.this, AddNewAddressActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.setList(Common.CURRENTUSER.getAddresses());
        adapter.notifyDataSetChanged();
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