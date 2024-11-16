package com.example.oumarket;

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

public class Signup extends AppCompatActivity {
    TextInputEditText edit_name, edit_email, edit_password, edit_confPass, edit_phone;
    AppCompatButton btn_signup;
    TextView tv_loginnow;

    FirebaseAuth firebaseAuth;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);


        table_user = Common.FIREBASE_DATABASE.getReference("User");

        edit_name = findViewById(R.id.edit_name_user);
        edit_email = findViewById(R.id.edit_email_signup);
        edit_password = findViewById(R.id.edit_password_signup);
        edit_confPass = findViewById(R.id.edit_confPass);
        btn_signup = findViewById(R.id.btn_signup);
        tv_loginnow = findViewById(R.id.tv_loginNow);

        edit_phone = findViewById(R.id.edit_phone);

        firebaseAuth = FirebaseAuth.getInstance();

//         button Signup - mbtn_Signup
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit_name.getText().toString();
                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();
                String confPass = edit_confPass.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(Signup.this, "Hãy nhập tên!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Signup.this, "Hãy nhập email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Signup.this, "Hãy nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(confPass)) {
                    Toast.makeText(Signup.this, "Nhập lại mật khẩu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                createAccount(email, password);
            }
        });

//         tv_loginnow
        tv_loginnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                createInfo();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createInfo() {
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String email = edit_email.getText().toString();
                email = email.substring(0, email.indexOf("@"));
                User user = new User(edit_name.getText().toString(), edit_phone.getText().toString());
                table_user.child(email).setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(Signup.this);
        alert.setTitle("Successfully!");
        alert.setMessage("Please check your email and click the URL to activate your account");
        alert.setIcon(R.drawable.ic_account_circle_24);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        alert.show();
    }
}