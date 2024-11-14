package com.example.oumarket.Common;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncListUtil;

import com.example.oumarket.Class.Category;
import com.example.oumarket.Class.User;
import com.example.oumarket.Interface.DataCallBack;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Common {
    public static User CURRENTUSER;
    public static final String TAG = "ZZZZZ";
    public static final FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();

    public static Map<String, Category> CATEGORY;

    public static void initCATEGORY(@Nullable final DataCallBack callback){
        DATABASE.getReference("Category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CATEGORY.clear();  // Xóa dữ liệu cũ nếu có
                for (DataSnapshot child : snapshot.getChildren()) {
                    String key = child.getKey();
                    Category category = child.getValue(Category.class);
                    CATEGORY.put(key, category);  // Lưu từng mục vào CATEGORY
                }

                if (callback != null) {
                    callback.onDataLoaded();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Lỗi: " + error.getMessage());
            }
        });
        Log.d(TAG,"hello");
        for (Map.Entry<String, Category> entry :CATEGORY.entrySet()){
            Log.d(TAG,entry.getValue().toString()+"");
        }
    }
}
