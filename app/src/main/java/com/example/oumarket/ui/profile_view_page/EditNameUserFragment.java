package com.example.oumarket.ui.profile_view_page;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oumarket.Class.User;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Interface.BottomSheetDialogSave;
import com.example.oumarket.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditNameUserFragment extends BottomSheetDialogFragment implements BottomSheetDialogSave {

    TextView btn_luu;
    TextInputEditText input;

    BottomSheetDialogSave bottomSheetDialogSave;

    public EditNameUserFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_name_user, container, false);

        btn_luu = view.findViewById(R.id.btn_luu);
        input = view.findViewById(R.id.input);

        input.setText(Common.CURRENTUSER.getName());

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                if (newText.equals(Common.CURRENTUSER.getName()) || newText.isBlank()) {
                    btn_luu.setTextColor(getContext().getResources().getColor(R.color.gray));
                    btn_luu.setEnabled(false);
                } else {
                    btn_luu.setTextColor(getContext().getResources().getColor(R.color.xanh_bien));
                    btn_luu.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString().trim();
                if (newText.equals(Common.CURRENTUSER.getName()) || newText.isBlank()) {
                    btn_luu.setTextColor(getContext().getResources().getColor(R.color.gray));
                    btn_luu.setEnabled(false);
                } else {
                    btn_luu.setTextColor(getContext().getResources().getColor(R.color.xanh_bien));
                    btn_luu.setEnabled(true);
                }
            }
        });

        btn_luu.setOnClickListener(v -> {
            String newName = input.getText().toString().trim();
            Common.CURRENTUSER.setName(newName);
            Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.CURRENTUSER.getIdUser()).child("name").setValue(newName);

            bottomSheetDialogSave.onSave(Common.CURRENTUSER);

            dismiss();
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bottomSheetDialogSave = (BottomSheetDialogSave) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bottomSheetDialogSave = null;
    }

    @Override
    public void onSave(User user) {

    }
}