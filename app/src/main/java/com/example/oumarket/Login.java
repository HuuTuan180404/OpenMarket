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

public class Login extends AppCompatActivity {

    private AppCompatButton btn_login;
    private TextView tv_forgotPassword, tv_signupNow;
    private TextInputEditText edit_email, edit_password;

    ProgressDialog mDialog;
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
        table_user = Common.FIREBASE_DATABASE.getReference("User");
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


//                String email = edit_email.getText().toString();
//                String password = edit_password.getText().toString();

                if (TextUtils.isEmpty(edit_email.getText().toString())) {
                    Toast.makeText(Login.this, "Hãy nhập email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edit_password.getText().toString())) {
                    Toast.makeText(Login.this, "Hãy nhập password!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Login.this, "forgot password", Toast.LENGTH_SHORT).show();
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
        login("ngay26thang6nam2021@gmail.com", "123456");
    }

    private void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (firebaseAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(Login.this, "Successfully!", Toast.LENGTH_SHORT).show();
                                String email = edit_email.getText().toString();
                                email = email.substring(0, email.indexOf("@"));

                                User user = snapshot.child(email).getValue(User.class);
                                Intent homeIntent = new Intent(Login.this, Home.class);
                                Common.CURRENTUSER = user;
                                startActivity(homeIntent);
                                finish();
                            }
                            else {
                                AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
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