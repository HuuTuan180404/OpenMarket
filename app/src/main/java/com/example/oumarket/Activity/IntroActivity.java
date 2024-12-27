package com.example.oumarket.Activity;

import static kotlinx.coroutines.DelayKt.delay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oumarket.Class.User;
import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rejowan.cutetoast.CuteToast;

import org.checkerframework.checker.units.qual.A;

import io.paperdb.Paper;

public class IntroActivity extends AppCompatActivity {
    private final long SPLASH_TIME_OUT = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Paper.init(this);
        if (Common.CURRENTUSER == null) {
            String user = Paper.book().read(Common.USERNAME_KEY);
            String password = Paper.book().read(Common.PASSWORD_KEY);

            if (user != null && password != null) {
                if (!user.isEmpty() && !password.isEmpty()) {
                    login(user, password);
                }
            } else {
                Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        }
    }

    void login(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(IntroActivity.this, task -> {
            if (task.isSuccessful()) {
                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Common.CURRENTUSER = user;
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(IntroActivity.this, HomeActivity.class));
                                finish();
                            }
                        }, SPLASH_TIME_OUT);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }


}