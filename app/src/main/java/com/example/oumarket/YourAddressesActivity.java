package com.example.oumarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Database.Database;
import com.example.oumarket.ViewHolder.AnAddressAdapter;

import java.util.ArrayList;

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

        if (Common.CURRENTUSER.getAddresses() == null || Common.CURRENTUSER.getAddresses().isEmpty()) {
            addressesIsNull.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            addressesIsNull.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new AnAddressAdapter(this, Common.CURRENTUSER.getAddresses());
            SetUpRecyclerView.setupLinearLayout(this, recyclerView, adapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }

        button = findViewById(R.id.btn_add_address);
        button.setOnClickListener((v) -> {
            Intent intent = new Intent(YourAddressesActivity.this, AddNewAddressActivity.class);
            startActivity(intent);
            finish();
        });

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(YourAddressesActivity.this);
            builder.setTitle("Delete a requests?");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                int position = viewHolder.getAdapterPosition();
                adapter.removeOrder(position);
                adapter.notifyItemRemoved(position);
                Common.CURRENTUSER.setAddresses(adapter.getList());
                Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.CURRENTUSER.getIdUser()).child("Addresses").setValue(adapter.getList());
                if (Common.CURRENTUSER.getAddresses() == null || Common.CURRENTUSER.getAddresses().isEmpty()) {
                    addressesIsNull.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            });

            builder.setNegativeButton("No", (dialog, which) -> adapter.notifyItemChanged(viewHolder.getAdapterPosition()));

            builder.show();
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}