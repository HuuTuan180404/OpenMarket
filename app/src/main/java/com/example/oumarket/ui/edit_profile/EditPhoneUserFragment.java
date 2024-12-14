package com.example.oumarket.ui.edit_profile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

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

public class EditPhoneUserFragment extends BottomSheetDialogFragment implements BottomSheetDialogSave {

    TextView btn_luu;
    TextInputEditText input;

    BottomSheetDialogSave bottomSheetDialogSave;

    public EditPhoneUserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_phone_user, container, false);

        btn_luu = view.findViewById(R.id.btn_luu);
        input = view.findViewById(R.id.input);

        input.setText(Common.CURRENTUSER.getPhone());

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                if (newText.equals(Common.CURRENTUSER.getPhone()) || newText.length() != 10) {
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
                if (newText.equals(Common.CURRENTUSER.getPhone()) || newText.length() != 10) {
                    btn_luu.setTextColor(getContext().getResources().getColor(R.color.gray));
                    btn_luu.setEnabled(false);
                } else {
                    btn_luu.setTextColor(getContext().getResources().getColor(R.color.xanh_bien));
                    btn_luu.setEnabled(true);
                }
            }
        });

        btn_luu.setOnClickListener(v -> {
            String newText = input.getText().toString().trim();
            Common.CURRENTUSER.setPhone(newText);
            Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.CURRENTUSER.getIdUser()).child("phone").setValue(newText);

            bottomSheetDialogSave.onSave(Common.CURRENTUSER);

            dismiss();
        });

        return view;
    }

    @Override
    public void onSave(User user) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        bottomSheetDialogSave = (BottomSheetDialogSave) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bottomSheetDialogSave = null;
    }
}