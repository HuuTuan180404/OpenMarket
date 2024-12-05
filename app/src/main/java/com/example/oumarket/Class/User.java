package com.example.oumarket.Class;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String idUser;
    private String Name;
    private String Phone;
    private List<AnAddress> Addresses;

    public User() {
    }

    public User(String name, String phone) {
        Name = name;
        Phone = phone;
    }

    public User(String name, String phone, List<AnAddress> addresses) {
        Name = name;
        Phone = phone;
        this.Addresses = addresses;
    }

    public User(String idUser, String name, String phone, List<AnAddress> addresses) {
        this.idUser = idUser;
        Name = name;
        Phone = phone;
        this.Addresses = addresses;
    }

    public void addAddress(AnAddress address) {
        if (this.Addresses == null) {
            this.Addresses = new ArrayList<>();
        }
        this.Addresses.add(address);
    }

    //getter + setter/////////////////////////////////////////////////////////

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public List<AnAddress> getAddresses() {
        return Addresses;
    }

    public void setAddresses(List<AnAddress> addresses) {
        this.Addresses = addresses;
    }
}
