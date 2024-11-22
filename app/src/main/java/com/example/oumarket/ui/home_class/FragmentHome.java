package com.example.oumarket.ui.home_class;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oumarket.Class.Category;
import com.example.oumarket.Class.SetUpRecyclerView;
import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.example.oumarket.ViewHolder.CategoryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    DatabaseReference data_categories, data_foods;

    androidx.recyclerview.widget.RecyclerView recycler_menu, recycler_category1;

    CategoryAdapter adapter;

    public FragmentHome() {
    }

    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        app:layoutManager="androidx.recyclerview.widget.GridLayoutManager"
//        app:spanCount="2"

//        app:layoutManager="androidx.recyclerview.widget.GridLayoutManager"
//        app:spanCount="1"

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        data_categories = Common.FIREBASE_DATABASE.getReference(Common.REF_CATEGORIES);

        data_foods = Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS);

        recycler_menu = view.findViewById(R.id.recycler_menu);

        setupRecyclerCategories();

        return view;
    }

    private void setupRecyclerCategories() {
        adapter = new CategoryAdapter();
        data_categories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category1 = dataSnapshot.getValue(Category.class);
                    category1.setId(dataSnapshot.getKey());
                    list.add(category1);
                }
                adapter = new CategoryAdapter(list, getContext());
                SetUpRecyclerView.setupGridLayout(getContext(), recycler_menu, adapter, 2, androidx.recyclerview.widget.RecyclerView.HORIZONTAL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}