package com.example.oumarket.Class;

import com.example.oumarket.Common.AddressType;

public class AnAddress {

    private String idAddress;
    private String name;
    private String phone;
    private double latitude;
    private double longtitude;
    private String country = "VN";
    private Ward ward;
    private String address;
    private AddressType typeAddress;
    private boolean isDefault;
    private boolean isMap;

    public AnAddress() {
    }


    //map
    public AnAddress(String name, String phone, double latitude, double longtitude, AddressType typeAddress, boolean isDefault, boolean isMap) {
        this.name = name;
        this.phone = phone;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.typeAddress = typeAddress;
        this.isDefault = isDefault;
        this.isMap = isMap;
    }

    //input
    public AnAddress(String name, String phone, Ward ward, String address, AddressType typeAddress, boolean isDefault, boolean isMap) {
        this.name = name;
        this.phone = phone;
        this.ward = ward;
        this.address = address;
        this.typeAddress = typeAddress;
        this.isDefault = isDefault;
        this.isMap = isMap;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", address, ward.getPath());
    }

//getter+setter//////////////////////////////////////////////////////////////

    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
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

    public boolean getIsMap() {
        return isMap;
    }

    public void setIsMap(boolean map) {
        isMap = map;
    }

//    getter + setter
//////////////////////////////////
}