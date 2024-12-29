package com.example.oumarket.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.oumarket.Common.Common;
import com.example.oumarket.Helper.Customer_LoadingDialog;
import com.example.oumarket.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.rejowan.cutetoast.CuteToast;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class EditAvatarActivity extends AppCompatActivity {
    static final int PIC_REQ = 1;
    Uri uri;
    Toolbar toolbar;
    ImageView pic;
    private AppCompatButton btn_luu;
    private ImageView buttonChangeImage;
    Customer_LoadingDialog customerLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_avatar);

        customerLoadingDialog = new Customer_LoadingDialog(this, "Đang lưu...");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Load Image
        pic = findViewById(R.id.pic);
        Picasso.get().load(Common.CURRENTUSER.getUrl()).into(pic);

        //Change Image
        buttonChangeImage= findViewById(R.id.button_changeImage);
        buttonChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
                selectPic();
            }
        });



        //Button save
        btn_luu = findViewById(R.id.btn_luu);
        btn_luu.setOnClickListener(v -> {
            if (uri == null) {
                CuteToast.ct(this, "Chưa chọn ảnh!", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
            }
            else {
                customerLoadingDialog.show();
                MediaManager.get().upload(uri).callback(new UploadCallback()
                {
                    @Override
                    public void onStart(String requestId) {

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        Common.CURRENTUSER.setUrl((String) resultData.get("secure_url"));
                        Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.CURRENTUSER.getIdUser()).child("url").setValue(Common.CURRENTUSER.getUrl());
                        customerLoadingDialog.dismiss();
                        CuteToast.ct(EditAvatarActivity.this, "Lưu thành công!", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                        finish();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        customerLoadingDialog.dismiss();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        customerLoadingDialog.dismiss();
                    }
                }).dispatch();
            }
        });



    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(EditAvatarActivity.this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            selectPic();
        } else {
            ActivityCompat.requestPermissions( EditAvatarActivity.this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, PIC_REQ);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}