package com.example.oumarket.Class;

public class Category {

    private String Image;
    private String Name;
    private String URL;
    private String id="";


    public Category() {
    }

    public Category(String image, String name, String uRL) {
        Image = image;
        Name = name;
        URL = uRL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "Image='" + Image + '\'' +
                ", Name='" + Name + '\'' +
                ", URL='" + URL + '\'' +
                '}';
    }
}
