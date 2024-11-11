package com.example.oumarket.Class;

public class Food {
    private String Description;
    private String Discount;
    private String Image;
    private String CategoryId;
    private String Name;
    private String Price;

    public Food() {
    }

    public Food(String description, String discount, String image, String menuID, String name, String price) {
        Description = description;
        Discount = discount;
        Image = image;
        CategoryId = menuID;
        Name = name;
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
