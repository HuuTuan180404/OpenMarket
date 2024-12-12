package com.example.oumarket;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class LoadingDialog extends Dialog {

    private String message;
    TextView tvMessage;

    public LoadingDialog(@NonNull Context context, String message) {
        super(context);
        this.message = message;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_loadding_dialog);

        setCancelable(false);

        // Áp dụng nền trong suốt
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Gán tin nhắn nếu có
        tvMessage = findViewById(R.id.tv_message);
        if (message != null && !message.isEmpty()) {
            tvMessage.setText(message);
        }
    }
}
