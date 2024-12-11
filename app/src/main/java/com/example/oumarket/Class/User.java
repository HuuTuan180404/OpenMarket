package com.example.oumarket.Class;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String idUser;
    private String name;
    private String phone;
    private String email;
    private List<AnAddress> Addresses;

    public User() {
    }

    public User(String idUser, String name, String phone, String email, List<AnAddress> addresses) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        this.email = email;
        Addresses = addresses;
    }

    public User(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public void addAddress(AnAddress address) {
        if (this.Addresses == null) {
            this.Addresses = new ArrayList<>();
        }
        this.Addresses.add(address);
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser='" + idUser + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    //getter + setter/////////////////////////////////////////////////////////

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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

    public List<AnAddress> getAddresses() {
        return Addresses;
    }

    public void setAddresses(List<AnAddress> addresses) {
        this.Addresses = addresses;
    }
}
