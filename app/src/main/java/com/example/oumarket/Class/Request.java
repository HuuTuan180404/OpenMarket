package com.example.oumarket.Class;

import java.util.List;

public class Request {
    private String idRequest = "";
    private String phone;
    private String name;
    private String address;
    private String total;
    private List<Order> Orders;
    private String idCurrentUser = "";

    private String status; // -1: canceled  0: ordered  1:completed

    public Request() {
    }

    public Request(String phone, String name, String address, String total, List<Order> foods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.Orders = foods;
        status = "0";
    }

    public Request(String idRequest, String phone, String name, String address, String total, List<Order> foods, String idCurrentUser, String status) {
        this.idRequest = idRequest;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.Orders = foods;
        this.idCurrentUser = idCurrentUser;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Request{" +
                "phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", total='" + total + '\'' +
                ", foods=" + Orders +
                ", id='" + idRequest + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    ////////////////////////////////
//    getter + setter
    public String getIdCurrentUser() {
        return idCurrentUser;
    }

    public void setIdCurrentUser(String idCurrentUser) {
        this.idCurrentUser = idCurrentUser;
    }

    public String getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(String idRequest) {
        this.idRequest = idRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getOrders() {
        return Orders;
    }

    public void setOrders(List<Order> orders) {
        Orders = orders;
    }

    //    getter + setter
///////////////////////////////////////////

}
