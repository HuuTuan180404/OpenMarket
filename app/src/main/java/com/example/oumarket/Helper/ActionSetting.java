package com.example.oumarket.Helper;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.oumarket.Interface.BottomSheetDialogSave;
import com.example.oumarket.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ActionSetting extends BottomSheetDialogFragment {

    BottomSheetDialogSave bottomSheetDialogSave;

    TextView price_desc, price_asc, name, rating;
    LinearLayout list_view, grid_view, layout_view_mode;

    boolean isGone;

    public ActionSetting() {
        isGone = false;
    }

    public ActionSetting(boolean flag) {
        isGone = flag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_setting, container, false);

        price_desc = view.findViewById(R.id.price_desc);
        price_asc = view.findViewById(R.id.price_asc);
        name = view.findViewById(R.id.name);
        rating = view.findViewById(R.id.rating);
        list_view = view.findViewById(R.id.list_view);
        grid_view = view.findViewById(R.id.grid_view);
        layout_view_mode = view.findViewById(R.id.layout_view_mode);

        price_desc.setOnClickListener(v -> {
            bottomSheetDialogSave.inFoodListActivity("price_desc");
        });

        price_asc.setOnClickListener(v -> {
            bottomSheetDialogSave.inFoodListActivity("price_asc");
        });

        name.setOnClickListener(v -> {
            bottomSheetDialogSave.inFoodListActivity("name");
        });

        rating.setOnClickListener(v -> {
            bottomSheetDialogSave.inFoodListActivity("rating");
        });

        list_view.setOnClickListener(v -> {
            bottomSheetDialogSave.inFoodListActivity("list_view");
        });

        grid_view.setOnClickListener(v -> {
            bottomSheetDialogSave.inFoodListActivity("grid_view");
        });

        if (isGone) {
            layout_view_mode.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BottomSheetDialogSave) {
            bottomSheetDialogSave = (BottomSheetDialogSave) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SortOptionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bottomSheetDialogSave = null; // Tránh memory leak
    }
}

