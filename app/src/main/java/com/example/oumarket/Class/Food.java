package com.example.oumarket.Class;

public class Food {
    private String Description;
    private String Discount;
    private String Image;
    private String CategoryId;
    private String Name;
    private String Price;
    private String URL;

    public Food() {
    }

    public Food(String description, String discount, String image, String categoryId, String name, String price, String URL) {
        Description = description;
        Discount = discount;
        Image = image;
        CategoryId = categoryId;
        Name = name;
        Price = price;
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
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
