package com.example.oumarket;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Notification;
import com.example.oumarket.Class.Order;
import com.example.oumarket.Class.Request;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Database.Database;
import com.example.oumarket.ViewHolder.CartAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {
    RecyclerView recyclerView;

    CartAdapter adapter;

    Toolbar toolbar;

    DatabaseReference data_requests;

    TextView tv_basketTotal, tv_discount, tv_total;
    AppCompatButton btn_order;

    List<Order> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.page_cart);

        toolbar = findViewById(R.id.toolbar_Cart);
        setSupportActionBar(toolbar);

//        firebase
        data_requests = Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS);

//        init
        tv_discount = findViewById(R.id.discount);
        tv_total = findViewById(R.id.total);
        tv_basketTotal = findViewById(R.id.basketTotal);
        btn_order = findViewById(R.id.btn_order);
        btn_order.setOnClickListener(v -> {
            try (Database database = new Database(getBaseContext())) {
                if (database.getCarts().isEmpty()) {
                    Toast.makeText(this, "is empty", Toast.LENGTH_SHORT).show();
                } else {
                    scheduleNotification(Common.DELAY_TIME);
                    showAlertDialog();
                }
            } catch (Exception e) {
                Log.d("", "");
            }
        });

        recyclerView = findViewById(R.id.list_cart);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        loadListCart();

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Cart.this);
            builder.setTitle("Delete a requests?");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                int position = viewHolder.getAdapterPosition();
                try (Database database1 = new Database(Cart.this)) {
                    database1.removeItems(adapter.getList().get(position));
                    adapter.removeOrder(position);
                    adapter.notifyItemRemoved(position);
                    updateBill();
                } catch (Exception e) {
                    Log.d("ZZZZZ", e.toString());
                }
            });

            builder.setNegativeButton("No", (dialog, which) -> adapter.notifyItemChanged(viewHolder.getAdapterPosition()));

            builder.show();
        }
    };

    private void scheduleNotification(int delayTime) {
        Intent notificationIntent = new Intent(Cart.this, Notification.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                Cart.this, Common.NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            long triggerTime = System.currentTimeMillis() + delayTime; // Thời gian kích hoạt
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
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

    private void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(Cart.this);
        alert.setTitle("Confirm!!");
        alert.setMessage("Enter your address: ");

        final TextInputEditText edit_address = new TextInputEditText(Cart.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        edit_address.setLayoutParams(params);
        alert.setView(edit_address);

        alert.setIcon(R.drawable.ic_add_shopping_cart_24);

        alert.setPositiveButton("Yes", (dialog, which) -> {
            Request request = new Request(Common.CURRENTUSER.getPhone(), Common.CURRENTUSER.getName(), edit_address.getText() != null ? edit_address.getText().toString() : "", tv_basketTotal.getText().toString(), cart);
            String id = String.valueOf(System.currentTimeMillis());
            request.setIdCurrentUser(Common.CURRENTUSER.getIdUser());
            request.setIdRequest(id);
            data_requests.child(id).setValue(request);
            try (Database database = new Database(getBaseContext())) {
                database.cleanCart();
                Toast.makeText(getBaseContext(), "Thank you", Toast.LENGTH_SHORT).show();
                scheduleNotification(Common.DELAY_TIME);
                finish();
            }
        });

        alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        alert.show();

    }

    private void loadListCart() {
        try (Database database = new Database(Cart.this)) {
            cart = database.getCarts();
            adapter = new CartAdapter(cart, Cart.this, tv_basketTotal);
            SetUpRecyclerView.setupLinearLayout(Cart.this, recyclerView, adapter);
            updateBill();
        }
    }

    public void updateBill() {
        try (Database database = new Database(getBaseContext())) {
            List<Order> orders = database.getCarts();
            int basketTotal = 0; // Tổng giá trị
            double discount = 0; // Giảm giá
            if (!orders.isEmpty()) {
                for (Order order : orders) {
                    int price = Integer.parseInt(order.getPrice());
                    int quantity = Integer.parseInt(order.getQuantity());
                    int itemTotal = price * quantity;
                    basketTotal += itemTotal;

                    // Thêm logic giảm giá (nếu có discount)
                    if (!order.getDiscount().isEmpty()) {
                        discount += itemTotal * Integer.parseInt(order.getDiscount()) / 100.0; // Ví dụ giảm giá theo %
                    }
                }

                if (basketTotal >= 0) {
                    tv_basketTotal.setText(String.valueOf(basketTotal));
                } else tv_basketTotal.setText("---");

                if (discount >= 0) {
                    tv_discount.setText(String.valueOf(discount));
                } else tv_discount.setText("---");

                double total = basketTotal - discount;
                if (total >= 0) {
                    tv_total.setText(String.valueOf(total));
                } else tv_total.setText("---");
            } else {
                tv_basketTotal.setText("---");
                tv_discount.setText("---");
                tv_total.setText("---");
            }
        }
    }

}