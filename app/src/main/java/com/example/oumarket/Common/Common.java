package com.example.oumarket.Common;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.oumarket.Class.Category;
import com.example.oumarket.Class.User;
import com.example.oumarket.Interface.DataCallBack;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Common {
    public static User CURRENTUSER;
    public static final String TAG = "ZZZZZ";
    public static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
}
