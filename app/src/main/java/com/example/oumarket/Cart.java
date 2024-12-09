package com.example.oumarket;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.AnAddress;
import com.example.oumarket.Class.Notification;
import com.example.oumarket.Class.Order;
import com.example.oumarket.Class.Request;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Database.Database;
import com.example.oumarket.ViewHolder.CartAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.rejowan.cutetoast.CuteToast;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {
    RecyclerView recyclerView;

    CartAdapter adapter;

    Toolbar toolbar;

    DatabaseReference data_requests;

    RelativeLayout layout_selected_address;

    TextView tv_basketTotal, tv_discount, tv_total, null_address, name, phone, address, ward_getPath;
    AppCompatButton btn_order;

    List<Order> cart = new ArrayList<>();

    AnAddress diaChi = null;

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
                if (tv_basketTotal.getText().equals("---") && tv_discount.getText().equals("---")) {
                    Toast.makeText(this, "is empty", Toast.LENGTH_SHORT).show();
                } else {
                    showAlertDialog();
                }
            } catch (Exception e) {
                Log.d("ZZZZZ", e.toString());
            }
        });

        recyclerView = findViewById(R.id.list_cart);

        null_address = findViewById(R.id.null_address);
        layout_selected_address = findViewById(R.id.layout_selected_address);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        ward_getPath = findViewById(R.id.ward_getPath);

        if (Common.CURRENTUSER.getAddresses() == null || Common.CURRENTUSER.getAddresses().isEmpty()) {
            null_address.setVisibility(View.VISIBLE);
            layout_selected_address.setVisibility(View.GONE);
        } else {
            null_address.setVisibility(View.GONE);
            layout_selected_address.setVisibility(View.VISIBLE);
            List<AnAddress> list = Common.CURRENTUSER.getAddresses();
            for (AnAddress i : list) {
                if (i.getIsDefault()) {
                    diaChi = i;
                    break;
                }
            }

            name.setText(diaChi.getName());
            phone.setText(diaChi.getPhone());
            address.setText(diaChi.getAddress());
            ward_getPath.setText(diaChi.getWard().getPath());

        }

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

    private void scheduleNotification(int delayTime, String idRequest) {
        Intent notificationIntent = new Intent(Cart.this, Notification.class);

        notificationIntent.putExtra("idRequest", idRequest);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(Cart.this, Common.NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

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

        if (diaChi == null) {

            new Database(getBaseContext()).cleanCart();
            CuteToast.ct(getBaseContext(), "Thank you", Toast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
            finish();

        }

    }

    private void loadListCart() {
        try (Database database = new Database(Cart.this)) {
            cart = database.getCarts();
            adapter = new CartAdapter(cart, Cart.this);
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
                    if (order.getIsBuy().equals("1")) {
                        int price = Integer.parseInt(order.getPrice());
                        int quantity = Integer.parseInt(order.getQuantity());
                        int itemTotal = price * quantity;
                        basketTotal += itemTotal;

                        // Thêm logic giảm giá (nếu có discount)
                        if (!order.getDiscount().isEmpty()) {
                            discount += itemTotal * Integer.parseInt(order.getDiscount()) / 100.0; // Ví dụ giảm giá theo %
                        }
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