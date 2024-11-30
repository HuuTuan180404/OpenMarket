package com.example.oumarket;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.Order;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Database.Database;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    TextView food_price_detail, food_description_detail;
    Toolbar food_name_detail;
    ImageView food_image_detail;
    Button btn_increase, btn_decrease;
    EditText edittext_quantity;

    FloatingActionButton button_cart;

    String foodId = "";

    DatabaseReference data_foods;

    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.page_food_detail);

//         innit firebase
        data_foods = Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS);

//         init view
        button_cart = findViewById(R.id.button_cart);
        food_description_detail = findViewById(R.id.food_description_detail);
        food_name_detail = findViewById(R.id.toolbar_food_name);
        food_price_detail = findViewById(R.id.food_price_detail);
        food_image_detail = findViewById(R.id.food_image_detail);

//         init number picker
        btn_increase = findViewById(R.id.button_Increase);
        btn_decrease = findViewById(R.id.button_Decrease);
        edittext_quantity = findViewById(R.id.edittext_quantity);

        btn_decrease.setOnClickListener((v) -> {
            int quantity = Integer.parseInt(edittext_quantity.getText().toString());
            if (quantity == 1) {
                Toast.makeText(FoodDetail.this, "Lỗi số lượng", Toast.LENGTH_SHORT).show();
            } else {
                quantity -= 1;
                edittext_quantity.setText(quantity + "");
            }
        });

        btn_increase.setOnClickListener(v -> {
            int quantity = Integer.parseInt(edittext_quantity.getText().toString());
            quantity += 1;
            edittext_quantity.setText(quantity + "");
        });

        edittext_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String valueString = s.toString();
                if (valueString.isEmpty() || Integer.parseInt(valueString) < 1) {
                    edittext_quantity.setText("1");
                }
            }
        });

//        button cart
        button_cart.setOnClickListener(v -> {
            Order order = new Order(foodId, currentFood.getName(), currentFood.getPrice(), edittext_quantity.getText() + "", currentFood.getDiscount());
            Database database1 = new Database(FoodDetail.this);
            database1.addToCart(order);
        });

        if (getIntent() != null) {
            foodId = getIntent().getStringExtra("FoodId");
        }
        if (!foodId.isEmpty()) {
            getDetailFood(foodId);
        }
    }

    private void getDetailFood(String foodId) {
        data_foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentFood = snapshot.getValue(Food.class);

                String pathURL = currentFood.getURL() != null ? currentFood.getURL() : "";
                Picasso picasso = new Picasso.Builder(FoodDetail.this).build();
                picasso.load(pathURL).into(food_image_detail);

                food_name_detail.setTitle(currentFood.getName());

                food_price_detail.setText(currentFood.getPrice());

                food_description_detail.setText(currentFood.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}