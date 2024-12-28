package com.example.oumarket.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.google.android.material.textfield.TextInputEditText;

public class EditPhoneUserActivity extends AppCompatActivity {
    AppCompatButton btn_luu;
    TextInputEditText input;
    private ImageView buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone_user);

        buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_luu = findViewById(R.id.btn_luu);
        input = findViewById(R.id.input);

        input.setText(Common.CURRENTUSER.getPhone());


        btn_luu.setOnClickListener(v -> {
            String newText = input.getText().toString().trim();
            Common.CURRENTUSER.setPhone(newText);
            Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.CURRENTUSER.getIdUser()).child("phone").setValue(newText);
            finish();

        });
    }
}