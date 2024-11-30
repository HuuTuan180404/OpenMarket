package com.example.oumarket.Class;

public class Order {
    private String ProductId;
    private String ProductName;
    private String Price;
    private String Quantity;
    private String Discount;

    public Order(String productId, String productName, String price, String quantity, String discount) {
        ProductId = productId;
        ProductName = productName;
        Price = price;
        Quantity = quantity;
        Discount = discount;
    }

    public Order(Food food, int quantity) {
        ProductId = food.getId();
        ProductName = food.getName();
        Price = food.getPrice();
        Quantity = String.valueOf(quantity);
        Discount = food.getDiscount();
    }


    public Order() {
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public int compareTo(Order order) {
        return this.ProductId.compareTo(order.getProductId());
    }

    @Override
    public String toString() {
        return "Order{" +
                "ProductId='" + ProductId + '\'' +
                ", ProductName='" + ProductName + '\'' +
                ", Price='" + Price + '\'' +
                ", Quantity='" + Quantity + '\'' +
                ", Discount='" + Discount + '\'' +
                '}';
    }
}
