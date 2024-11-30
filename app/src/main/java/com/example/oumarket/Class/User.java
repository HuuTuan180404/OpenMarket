package com.example.oumarket.Class;

public class User {
    private String idUser;
    private String Name;
    private String Phone;


    public User() {
    }

    public User(String name, String phone) {
        Name = name;
        Phone = phone;
    }

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
}
