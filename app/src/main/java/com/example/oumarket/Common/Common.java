package com.example.oumarket.Common;

import com.example.oumarket.Class.User;
import com.google.firebase.database.FirebaseDatabase;

public class Common {
    public static final String REF_CATEGORIES = "Category";
    public static final String USERNAME = "Category";
    public static final String USER_KEY = "USER";
    public static final String PWD_KEY = "Password";
    public static final String REF_FOODS = "Foods";
    public static final String REF_USERS = "User";
    public static final String REF_REQUESTS = "Requests";
    public static User CURRENTUSER;
    public static final String TAG = "ZZZZZ";
    public static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    public static final int n_Category = 1;
    public static final int n_Food = 5;

}
