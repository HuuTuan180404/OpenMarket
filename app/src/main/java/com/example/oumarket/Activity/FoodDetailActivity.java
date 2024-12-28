package com.example.oumarket.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.oumarket.Class.Food;
import com.example.oumarket.Class.Order;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Database.Database;
import com.example.oumarket.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rejowan.cutetoast.CuteToast;
import com.squareup.picasso.Picasso;

public class FoodDetailActivity extends AppCompatActivity {

    TextView price_after_discount, food_description_detail, price_before_discount;
    RatingBar rating;
    TextView food_name_detail;
    ImageView food_image_detail;
    TextView btn_increase, btn_decrease;
    EditText edittext_quantity;
    RelativeLayout layout_before_discount;
    AppCompatButton button_cart;
    ImageView button_back;

    String foodId = "";

    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

//         init view
        button_cart = findViewById(R.id.button_cart);
        food_description_detail = findViewById(R.id.food_description_detail);
        food_name_detail = findViewById(R.id.food_name);
        price_after_discount = findViewById(R.id.price_after_discount);
        food_image_detail = findViewById(R.id.food_image_detail);
        rating = findViewById(R.id.rating);
        price_before_discount = findViewById(R.id.price_before_discount);
        layout_before_discount = findViewById(R.id.layout_before_discount);
        button_back = findViewById(R.id.button_back);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//         init number picker
        btn_increase = findViewById(R.id.button_Increase);
        btn_decrease = findViewById(R.id.button_Decrease);
        edittext_quantity = findViewById(R.id.edittext_quantity);

        btn_decrease.setOnClickListener((v) -> {
            int quantity = Integer.parseInt(edittext_quantity.getText().toString());
            if (quantity > 1) {
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
            Order order = new Order(foodId, currentFood.getName(), currentFood.getPrice(), edittext_quantity.getText() + "", currentFood.getDiscount(), "0");
            Database database1 = new Database(FoodDetailActivity.this);
            CuteToast.ct(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
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
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentFood = snapshot.getValue(Food.class);

                Picasso.get().load(currentFood.getUrl()).centerCrop().fit().into(food_image_detail);
                food_name_detail.setText(currentFood.getName());
                if(currentFood.getCountRating()>0){
                    rating.setRating((float) currentFood.getCountStars()/currentFood.getCountRating());
                }
                else{
                    rating.setRating(0);
                }
                price_before_discount.setText(currentFood.getPrice());
                price_after_discount.setText(currentFood.getPriceAfterDiscount());
                if (currentFood.getDiscount().equals("0")) {
                    layout_before_discount.setVisibility(View.GONE);
                } else layout_before_discount.setVisibility(View.VISIBLE);
                food_description_detail.setText(currentFood.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}