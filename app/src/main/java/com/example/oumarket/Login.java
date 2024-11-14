package com.example.oumarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.oumarket.Class.User;
import com.example.oumarket.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private AppCompatButton btn_login;
    private TextView tv_forgotPassword, tv_signupNow;
    private EditText edit_email, edit_password;

    ProgressDialog mDialog;
    FirebaseDatabase database;
    DatabaseReference table_user;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mDialog = new ProgressDialog(Login.this);
        mDialog.setMessage("Waiting...");

//         firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();

        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        tv_forgotPassword = findViewById(R.id.tv_ForgotPass);
        btn_login = findViewById(R.id.btn_Login);
        tv_signupNow = findViewById(R.id.tv_SignupNow);

//      butotn login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.show();

                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Hãy nhập email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Hãy nhập password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                signIn(email, password);

            }
        });

        // textview Forgot password - mtv_ForgotPass
        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "ConfPass", Toast.LENGTH_SHORT).show();
            }
        });

        tv_signupNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
                finish();
            }
        });

//
        signIn("nguyenhuutuan1704@gmail.com", "123456");
    }

    private void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "Successfully!", Toast.LENGTH_SHORT).show();
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            String email = edit_email.getText().toString();
                            email = email.substring(0, email.indexOf("@"));

                            User user = snapshot.child(email).getValue(User.class);
                            Intent homeIntent = new Intent(Login.this, Home.class);
                            Common.CURRENTUSER = user;
                            startActivity(homeIntent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Tài khoản không đúng!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}