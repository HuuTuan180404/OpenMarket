package com.example.oumarket;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oumarket.Class.User;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Interface.BottomSheetDialogSave;
import com.example.oumarket.ui.profile_view_page.ChangePasswordFragment;
import com.example.oumarket.ui.profile_view_page.EditNameUserFragment;
import com.example.oumarket.ui.profile_view_page.EditPhoneUserFragment;
import com.google.android.material.appbar.MaterialToolbar;

public class ProfileActivity extends AppCompatActivity implements BottomSheetDialogSave {

    MaterialToolbar toolbar;

    TextView name, phone, email;

    ImageView ic_next_name, ic_next_phone, ic_next_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);

        name.setText(Common.CURRENTUSER.getName());
        String sPhone = Common.CURRENTUSER.getPhone();
        sPhone = "*******" + sPhone.substring(sPhone.length() - 3);
        phone.setText(sPhone);

        String s = Common.CURRENTUSER.getEmail();
        int index = s.indexOf("@");
        String sEmail = s.substring(0, 1) + "*****" + s.substring(index - 1);

        email.setText(sEmail);

        ic_next_name = findViewById(R.id.ic_next_name);
        ic_next_name.setOnClickListener(v -> {
            EditNameUserFragment fragment = new EditNameUserFragment();
            fragment.show(getSupportFragmentManager(), "EditNameUserFragment");
        });

        ic_next_phone = findViewById(R.id.ic_next_phone);
        ic_next_phone.setOnClickListener(v -> {
            EditPhoneUserFragment fragment = new EditPhoneUserFragment();
            fragment.show(getSupportFragmentManager(), "EditPhoneUserFragment");
        });

        ic_next_password = findViewById(R.id.ic_next_password);
        ic_next_password.setOnClickListener(v -> {
            ChangePasswordFragment fragment = new ChangePasswordFragment();
            fragment.show(getSupportFragmentManager(), "ChangePasswordFragment");
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSave(User user) {
        name.setText(Common.CURRENTUSER.getName());
        String sPhone = Common.CURRENTUSER.getPhone();
        sPhone = "*******" + sPhone.substring(sPhone.length() - 3);
        phone.setText(sPhone);
    }
}