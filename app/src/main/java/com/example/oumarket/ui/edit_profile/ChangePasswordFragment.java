package com.example.oumarket.ui.edit_profile;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rejowan.cutetoast.CuteToast;

import io.paperdb.Paper;

public class ChangePasswordFragment extends BottomSheetDialogFragment {

    TextView btn_luu;
    TextInputEditText input_oldPassword, input_newPassword_1, input_newPassword_2;

    TextInputLayout layout_oldPassword, layout_newPassword1, layout_newPassword2;

    String password, password_1, password_2;

    public ChangePasswordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        btn_luu = view.findViewById(R.id.btn_luu);
        input_oldPassword = view.findViewById(R.id.old_password);
        input_newPassword_1 = view.findViewById(R.id.new_password_1);
        input_newPassword_2 = view.findViewById(R.id.new_password_2);

        layout_oldPassword = view.findViewById(R.id.layout_oldPassword);
        layout_newPassword1 = view.findViewById(R.id.layout_newPassword1);
        layout_newPassword2 = view.findViewById(R.id.layout_newPassword2);

        btn_luu.setOnClickListener(v -> {
            password = input_oldPassword.getText().toString().trim();
            password_1 = input_newPassword_1.getText().toString().trim();
            password_2 = input_newPassword_2.getText().toString().trim();

            if (isNotError()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    AuthCredential credential = EmailAuthProvider.getCredential(Common.CURRENTUSER.getEmail(), password);

                    user.reauthenticate(credential).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.updatePassword(password_1).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Paper.init(getContext());
                                    Paper.book().write(Common.PASSWORD_KEY, password_1);
                                    CuteToast.ct(getContext(), "Mật khẩu đã được thay đổi", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                                    dismiss();
                                } else {
                                    CuteToast.ct(getContext(), "Đã xảy ra lỗi, hãy thử lại", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
                                }
                            });
                        }
                    });
                }
            }

        });
        return view;
    }

    private boolean isNotError() {

        if (TextUtils.isEmpty(password)) {
            layout_oldPassword.setError("isBlank");
            return false;
        } else {
            layout_oldPassword.setError(null);
        }

        if (TextUtils.isEmpty(password_1)) {
            layout_newPassword1.setError("isBlank");
            return false;
        } else {
            layout_newPassword1.setError(null);
        }

        if (TextUtils.isEmpty(password_2)) {
            layout_newPassword2.setError("isBlank");
            return false;
        } else {
            layout_newPassword2.setError(null);
        }

        if (password_1.equals(password)) {
            layout_newPassword1.setError("Trùng với mật khẩu cũ");
            return false;
        } else {
            layout_newPassword1.setError(null);
        }

        if (!password_1.equals(password_2)) {
            layout_newPassword2.setError("Xác nhập không đúng");
            return false;
        } else {
            layout_newPassword2.setError(null);
        }

        return true;
    }
}