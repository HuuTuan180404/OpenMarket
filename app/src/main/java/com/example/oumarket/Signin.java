package com.example.oumarket;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.oumarket.Class.Customer_LoadingDialog;
import com.example.oumarket.Class.User;
import com.example.oumarket.Common.Common;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.rejowan.cutetoast.CuteToast;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class Signin extends AppCompatActivity {

    private AppCompatButton btn_signin;
    private TextView tv_forgotPassword, tv_signupNow;
    private TextInputEditText edit_email, edit_password;
    CheckBox checkBox;

    Customer_LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        Paper.init(this);

        loadingDialog = new Customer_LoadingDialog(this, "Vui lòng đợi...");

        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        tv_forgotPassword = findViewById(R.id.tv_ForgotPass);
        btn_signin = findViewById(R.id.btn_xac_nhan);
        tv_signupNow = findViewById(R.id.tv_SignupNow);
        checkBox = findViewById(R.id.checkbox_remember);

        String user = Paper.book().read(Common.USERNAME_KEY);
        String password = Paper.book().read(Common.PASSWORD_KEY);

        if (user != null && password != null) {
            checkBox.setChecked(true);
            if (!user.isEmpty() && !password.isEmpty()) {
//                mDialog.show();
                loadingDialog.show();
                login(user, password);
            }
        }

//      butotn login
        btn_signin.setOnClickListener(v -> {
            if (TextUtils.isEmpty(edit_email.getText().toString())) {
                Toast.makeText(Signin.this, "Hãy nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(edit_password.getText().toString())) {
                Toast.makeText(Signin.this, "Hãy nhập password!", Toast.LENGTH_SHORT).show();
                return;
            }

            loadingDialog.show();

//            mDialog.show();

            login(edit_email.getText().toString(), edit_password.getText().toString());

        });

        // textview Forgot password - mtv_ForgotPass
        tv_forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(Signin.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        tv_signupNow.setOnClickListener(v -> {
            Intent intent = new Intent(Signin.this, Signup.class);
            startActivity(intent);
            finish();
        });
//


    }

    private void login(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(Signin.this, task -> {
            if (task.isSuccessful()) {
                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {

                    if (checkBox.isChecked()) {
                        Paper.book().write(Common.USERNAME_KEY, edit_email.getText().toString().trim());
                        Paper.book().write(Common.PASSWORD_KEY, edit_password.getText().toString().trim());
                    }

                    String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            Common.CURRENTUSER = user;

                            Intent homeIntent = new Intent(Signin.this, Home.class);
                            startActivity(homeIntent);

//                            mDialog.dismiss();
                            loadingDialog.dismiss();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
//                    mDialog.dismiss();
                    loadingDialog.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(Signin.this);
                    alert.setTitle("Warming!");
                    alert.setMessage("Please check your email and click the URL to activate your account");
                    alert.setIcon(R.drawable.ic_info_24);

                    alert.setPositiveButton("Yes", (dialog, which) -> {
                    });

                    alert.show();
                }
            } else {
                loadingDialog.dismiss();
                CuteToast.ct(Signin.this, "Tài khoản không đúng!", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
            }
        });
    }

}