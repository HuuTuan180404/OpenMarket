package com.example.oumarket.Class;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.oumarket.Common.AddressType;

public class AnAddress implements Parcelable {

    private String address;
    private String country;
    private boolean isDefault;
    private String name;
    private String phone;
    private AddressType typeAddress;
    private Ward ward;

    // load from firebase
    public AnAddress(String address, String country, boolean isDefault, String name, String phone, AddressType typeAddress, Ward ward) {
        this.name = name;
        this.phone = phone;
        this.ward = ward;
        this.address = address;
        this.typeAddress = typeAddress;
        this.isDefault = isDefault;
        this.country = country;
    }

    public AnAddress() {
    }

    public static final Creator<AnAddress> CREATOR = new Creator<AnAddress>() {
        @Override
        public AnAddress createFromParcel(Parcel in) {
            return new AnAddress(in);
        }

        @Override
        public AnAddress[] newArray(int size) {
            return new AnAddress[size];
        }
    };

    @Override
    public String toString() {
        return String.format("%s, %s", address, ward.getPath());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected AnAddress(Parcel in) {
        address = in.readString();
        country = in.readString();
        isDefault = in.readByte() != 0; // true nếu != 0
        name = in.readString();
        phone = in.readString();
        // Đọc giá trị AddressType từ String
        typeAddress = AddressType.valueOf(in.readString());
        // Nếu dùng Ward, đảm bảo Ward cũng implement Parcelable
        ward = in.readParcelable(Ward.class.getClassLoader());
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(country);
        dest.writeByte((byte) (isDefault ? 1 : 0));
        dest.writeString(name);
        dest.writeString(phone);
        // Lưu giá trị AddressType dưới dạng String
        dest.writeString(typeAddress.name());
        dest.writeParcelable(ward, flags);
    }

//getter+setter//////////////////////////////////////////////////////////////

//    public String getIdAddress() {
//        return idAddress;
//    }
//
//    public void setIdAddress(String idAddress) {
//        this.idAddress = idAddress;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AddressType getTypeAddress() {
        return typeAddress;
    }

    public void setTypeAddress(AddressType typeAddress) {
        this.typeAddress = typeAddress;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean aDefault) {
        isDefault = aDefault;
    }

//    getter + setter
//////////////////////////////////
}