package com.example.oumarket.Common;

import com.example.oumarket.Class.User;
import com.google.firebase.database.FirebaseDatabase;

public class Common {

    public static User CURRENTUSER;

    public static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    public static final String REF_CATEGORIES = "Category";
    public static final String REF_FOODS = "Foods";
    public static final String REF_USERS = "User";
    public static final String REF_REQUESTS = "Requests";

    public static final String ID_USER_KEY = "ID_USER";
    public static final String USERNAME_KEY = "USER_NAME";
    public static final String PASSWORD_KEY = "PASSWORD";

    public static final int n_Category = 1;
    public static final int n_Food = 5;

    public static String CURRENCY = "$";

    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_CART = "1";

    public static final int DELAY_TIME = 10000;

}
