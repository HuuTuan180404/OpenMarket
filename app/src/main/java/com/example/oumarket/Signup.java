package com.example.oumarket;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oumarket.Class.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {
    EditText edit_name, edit_email, edit_password, edit_confPass;
    AppCompatButton btn_signup;
    TextView tv_loginnow;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);


        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        edit_name = findViewById(R.id.edit_name_user);
        edit_email = findViewById(R.id.edit_email_signup);
        edit_password = findViewById(R.id.edit_password_signup);
        edit_confPass = findViewById(R.id.edit_confPass);
        btn_signup = findViewById(R.id.btn_signup);
        tv_loginnow = findViewById(R.id.tv_loginNow);

        firebaseAuth = FirebaseAuth.getInstance();


//         button Signup - mbtn_Signup
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Signup.this, ConfirmationCode.class);
//                intent.putExtra("email", edt_email.getText().toString());
//                intent.putExtra("password", medt_Pass.getText().toString());
//                startActivity(intent);
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

                createAccount(email, password, name);
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

    private void createAccount(String email, String password, String name) {
        // [START create_user_with_email]
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                            data(name);
                        } else {
                            Toast.makeText(getApplicationContext(), "Tài khoản đã tồn ta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void data(String name) {
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String email = edit_email.getText().toString();
                email = email.substring(0, email.indexOf("@"));
                User user = new User(edit_name.getText().toString());
                table_user.child(email).setValue(user);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}