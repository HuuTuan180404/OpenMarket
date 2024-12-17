package com.example.oumarket.ui.edit_profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.oumarket.Class.Customer_LoadingDialog;
import com.example.oumarket.Class.User;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Interface.BottomSheetDialogSave;
import com.example.oumarket.R;
import com.example.oumarket.Signin;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rejowan.cutetoast.CuteToast;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditAvatarFragment extends BottomSheetDialogFragment implements BottomSheetDialogSave {

    static final int PIC_REQ = 1;
    Uri uri;

    ImageView pic;
    TextView btn_luu;

    BottomSheetDialogSave bottomSheetDialogSave;

    public EditAvatarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_avatar, container, false);

        initConfig();

        btn_luu = view.findViewById(R.id.btn_luu);
        btn_luu.setOnClickListener(v -> {


            if (uri == null) {
                return;
            }

            Customer_LoadingDialog dialog = new Customer_LoadingDialog(getContext(), "Loading...");
            dialog.show();

            MediaManager.get().upload(uri).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    Common.CURRENTUSER.setUrl((String) resultData.get("url"));
                    Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.CURRENTUSER.getIdUser()).child("url").setValue(Common.CURRENTUSER.getUrl());
                    bottomSheetDialogSave.onSave(Common.CURRENTUSER);
                    dialog.dismiss();
                    CuteToast.ct(getContext(), "Thành công!", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                    dismiss();
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    dialog.dismiss();
                    CuteToast.ct(getContext(), "Lỗi!", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                    dialog.dismiss();
                    CuteToast.ct(getContext(), "Lỗi!", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                }
            }).dispatch();

        });

        pic = view.findViewById(R.id.pic);
        if (!Common.CURRENTUSER.getUrl().equals(" ")) {
            Picasso.get().load(Common.CURRENTUSER.getUrl()).into(pic);
        }

        pic.setOnClickListener(v -> {
            requestPermissions();
        });

        return view;
    }

    private void initConfig() {
        Map config = new HashMap();
        config.put("cloud_name", "dv2zyrxsv");
        config.put("api_key", "564718357889346");
        config.put("api_secret", "TfxQE6I3edoX7yeQrglD0avshDQ");
        MediaManager.init(getContext(), config);
    }


    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            selectPic();
        } else {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, PIC_REQ);
        }
    }

    private void selectPic() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PIC_REQ);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIC_REQ && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Picasso.get().load(uri).into(pic);
        }
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

    @Override
    public void inFoodListActivity(String key) {

    }
}