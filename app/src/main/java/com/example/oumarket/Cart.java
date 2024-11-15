package com.example.oumarket;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oumarket.Class.Order;
import com.example.oumarket.Class.Request;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Database.Database;
import com.example.oumarket.ViewHolder.CartAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference requests;

    TextView tv_total;
    AppCompatButton btn_order;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

//        firebase
        requests = Common.FIREBASE_DATABASE.getReference("Requests");

//        init
        tv_total = findViewById(R.id.total);
        btn_order = findViewById(R.id.btn_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        recyclerView = findViewById(R.id.list_cart);
        loadListCart();

    }

    private void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(Cart.this);
        alert.setTitle("Confirm!!");
        alert.setMessage("Enter your address: ");

        final TextInputEditText edit_address = new TextInputEditText(Cart.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        edit_address.setLayoutParams(params);
        alert.setView(edit_address);

        alert.setIcon(R.drawable.baseline_add_shopping_cart_24);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(Common.CURRENTUSER.getPhone(), Common.CURRENTUSER.getName(), edit_address.getText().toString(), tv_total.getText().toString(), cart);
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(getBaseContext(), "Thank you", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();

    }

    private void updateTotal() {
        int total = 0;
        for (Order order : cart) {
            total += Integer.parseInt(order.getPrice()) * Integer.parseInt(order.getQuantity());
        }
        tv_total.setText(String.valueOf(total));
    }

    private void loadListCart() {
        cart = new Database(Cart.this).getCarts();
        adapter = new CartAdapter(cart, Cart.this, tv_total);
        SetUpRecyclerView.setupLinearLayout(Cart.this, recyclerView, adapter);
        updateTotal();
    }

}