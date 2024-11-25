package com.example.oumarket;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.oumarket.Class.User;
import com.example.oumarket.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class Signin extends AppCompatActivity {

    private AppCompatButton btn_signin;
    private TextView tv_forgotPassword, tv_signupNow;
    private TextInputEditText edit_email, edit_password;
    CheckBox checkBox;

    ProgressDialog mDialog;
    DatabaseReference table_user;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.page_log_in);

        mDialog = new ProgressDialog(Signin.this);
        mDialog.setTitle("Signin");
        mDialog.setMessage("Waiting...");

//         firebase
        table_user = Common.FIREBASE_DATABASE.getReference(Common.REF_USERS);
        firebaseAuth = FirebaseAuth.getInstance();

        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        tv_forgotPassword = findViewById(R.id.tv_ForgotPass);
        btn_signin = findViewById(R.id.btn_Login);
        tv_signupNow = findViewById(R.id.tv_SignupNow);
        checkBox = findViewById(R.id.checkbox_remember);

//      butotn login
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edit_email.getText().toString())) {
                    Toast.makeText(Signin.this, "Hãy nhập email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edit_password.getText().toString())) {
                    Toast.makeText(Signin.this, "Hãy nhập password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mDialog.show();

                login(edit_email.getText().toString(), edit_password.getText().toString());

            }
        });

        // textview Forgot password - mtv_ForgotPass
        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Signin.this, "forgotPassword", Toast.LENGTH_SHORT).show();
            }
        });

        tv_signupNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this, Signup.class);
                startActivity(intent);
                finish();
            }
        });

//
        Paper.init(this);

        String user = Paper.book().read(Common.USER_KEY);
        String password = Paper.book().read(Common.PWD_KEY);

        if (user != null && password != null) {
            if (!user.isEmpty() && !password.isEmpty()) {
                mDialog.show();
                login(user, password);
            }
        }

//        login("ngay26thang6nam2021@gmail.com", "123456");

    }

    private void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Signin.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        if (checkBox.isChecked()) {
                            Paper.book().write(Common.USER_KEY, edit_email.getText().toString());
                            Paper.book().write(Common.PWD_KEY, edit_password.getText().toString());
                        }
                        String email = edit_email.getText().toString();
                        email = email.substring(0, email.indexOf("@"));

                        table_user.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                Intent homeIntent = new Intent(Signin.this, Home.class);
                                Common.CURRENTUSER = user;
                                startActivity(homeIntent);
                                mDialog.dismiss();
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    } else {
                        mDialog.dismiss();
                        AlertDialog.Builder alert = new AlertDialog.Builder(Signin.this);
                        alert.setTitle("Warming!");
                        alert.setMessage("Please check your email and click the URL to activate your account");
                        alert.setIcon(R.drawable.ic_info_24);

                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.show();
                    }
                } else {
                    mDialog.dismiss();
                    Toast.makeText(Signin.this, "Tài khoản không đúng!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}