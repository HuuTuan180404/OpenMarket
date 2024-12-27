package com.example.oumarket.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oumarket.Activity.AddNewAddressActivity;
import com.example.oumarket.Activity.ChangePasswordActivity;
import com.example.oumarket.Activity.EditAvatarActivity;
import com.example.oumarket.Activity.EditNameUserActivity;
import com.example.oumarket.Activity.EditPhoneUserActivity;
import com.example.oumarket.Activity.SignInActivity;
import com.example.oumarket.Common.Common;
import com.example.oumarket.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class ProfileFragment extends Fragment {

    private TextView name, phone, email, logout;

    private ImageView pic;

    private ImageView ic_next_name, ic_next_phone, ic_next_password;

    private ImageView buttonChangeImage;
    private ImageView ic_next_address;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        pic = view.findViewById(R.id.pic);
        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        logout = view.findViewById(R.id.button_logout);
        buttonChangeImage = view.findViewById(R.id.button_changeImage);
        ic_next_address = view.findViewById(R.id.ic_next_address);

        ic_next_address.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AddNewAddressActivity.class));
        });

        //logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.CURRENTUSER = null;
                FirebaseAuth.getInstance().signOut();
                Paper.clear(requireActivity());
                startActivity(new Intent(requireActivity(), SignInActivity.class));
                requireActivity().finish();
            }
        });


        if (!Common.CURRENTUSER.getUrl().equals(" ")) {
            Picasso.get().load(Common.CURRENTUSER.getUrl()).into(pic);
        }
        buttonChangeImage.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), EditAvatarActivity.class));
        });

        name.setText(Common.CURRENTUSER.getName());
        String sPhone = Common.CURRENTUSER.getPhone();
        sPhone = "*******" + sPhone.substring(sPhone.length() - 3);
        phone.setText(sPhone);

        String s = Common.CURRENTUSER.getEmail();
        int index = s.indexOf("@");
        String sEmail = s.substring(0, 1) + "*****" + s.substring(index - 1);

        email.setText(sEmail);

        ic_next_name = view.findViewById(R.id.ic_next_name);
        ic_next_name.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), EditNameUserActivity.class));
        });

        ic_next_phone = view.findViewById(R.id.ic_next_phone);
        ic_next_phone.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), EditPhoneUserActivity.class));
        });

        ic_next_password = view.findViewById(R.id.ic_next_password);
        ic_next_password.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), ChangePasswordActivity.class));
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        phone.setText(Common.CURRENTUSER.getPhone());
        name.setText(Common.CURRENTUSER.getName());
        Picasso.get().load(Common.CURRENTUSER.getUrl()).into(pic);
    }
}