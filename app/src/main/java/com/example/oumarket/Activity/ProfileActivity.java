package com.example.oumarket.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oumarket.Class.User;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Interface.BottomSheetDialogSave;
import com.example.oumarket.R;
import com.example.oumarket.Fragment.ChangePasswordFragment;
import com.example.oumarket.Fragment.EditAvatarFragment;
import com.example.oumarket.Fragment.EditNameUserFragment;
import com.example.oumarket.Fragment.EditPhoneUserFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity implements BottomSheetDialogSave {

    MaterialToolbar toolbar;

    TextView name, phone, email;

    ImageView pic;

    ImageView ic_next_name, ic_next_phone, ic_next_password;
    private TextView buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pic = findViewById(R.id.pic);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);

        if (!Common.CURRENTUSER.getUrl().equals(" "))
            Picasso.get().load(Common.CURRENTUSER.getUrl()).into(pic);

        pic.setOnClickListener(v -> {
            EditAvatarFragment fragmentAvatar = new EditAvatarFragment();
            fragmentAvatar.show(getSupportFragmentManager(), "EditAvatarFragment");
        });

        name.setText(Common.CURRENTUSER.getName());
        String sPhone = Common.CURRENTUSER.getPhone();
        sPhone = "*******" + sPhone.substring(sPhone.length() - 3);
        phone.setText(sPhone);

        String s = Common.CURRENTUSER.getEmail();
        int index = s.indexOf("@");
        String sEmail = s.substring(0, 1) + "*****" + s.substring(index - 1);

        email.setText(sEmail);

        EditNameUserFragment fragmentName = new EditNameUserFragment();
        ic_next_name = findViewById(R.id.ic_next_name);
        ic_next_name.setOnClickListener(v -> {
            fragmentName.show(getSupportFragmentManager(), "EditNameUserFragment");
        });

        EditPhoneUserFragment fragmentPhone = new EditPhoneUserFragment();
        ic_next_phone = findViewById(R.id.ic_next_phone);
        ic_next_phone.setOnClickListener(v -> {
            fragmentPhone.show(getSupportFragmentManager(), "EditPhoneUserFragment");
        });

        ChangePasswordFragment fragmentPassword = new ChangePasswordFragment();
        ic_next_password = findViewById(R.id.ic_next_password);
        ic_next_password.setOnClickListener(v -> {
            fragmentPassword.show(getSupportFragmentManager(), "ChangePasswordFragment");
        });

        buttonLogout = findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
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

        if (!Common.CURRENTUSER.getUrl().equals(" "))
            Picasso.get().load(Common.CURRENTUSER.getUrl()).into(pic);
    }

    @Override
    public void inFoodListActivity(String key) {

    }
}