package com.example.oumarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.example.oumarket.Class.AnAddress;
import com.example.oumarket.Class.Customer_LoadingDialog;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Class.User;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Interface.BottomSheetDialogSave;
import com.example.oumarket.ViewHolder.AnAddressAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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

                AnAddress address = adapter.getList().get(position);

                adapter.removeOrder(position);

                if (address.getIsDefault() && !adapter.getList().isEmpty())
                    adapter.getList().get(0).setIsDefault(true);

                adapter.notifyItemRemoved(position);
                Common.CURRENTUSER.setAddresses(adapter.getList());
                Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.CURRENTUSER.getIdUser()).child("Addresses").setValue(adapter.getList());

                recreate();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        Customer_LoadingDialog loadingDialog = new Customer_LoadingDialog(this, "Loading...");
        loadingDialog.show();
        recreate();
        new android.os.Handler().postDelayed(() -> {
            loadingDialog.dismiss();
        }, 200);
    }
}