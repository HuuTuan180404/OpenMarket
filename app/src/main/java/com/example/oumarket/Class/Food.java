package com.example.oumarket.Class;

public class Food {

    private String id;
    private String categoryId;
    private String name;
    private String description;
    private String discount;
    private String price;
    private String url;

    private int countRating;
    private int countStars;

    public Food() {
    }

    public Food(String description, String discount, String categoryId, String name, String price, String URL) {
        this.description = description;
        this.discount = discount;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.url = URL;
    }

    public Food(String description, String discount, String categoryId, String name, String price, String URL, int countRating, int countStars) {
        this.description = description;
        this.discount = discount;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.url = URL;
        this.countStars = countStars;
        this.countRating = countRating;
    }

    public Food(String categoryId, int countRating, int countStars, String description, String discount, String id, String name, String price, String URL) {
        this.id = id;
        this.description = description;
        this.discount = discount;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.url = URL;
        this.countStars = countStars;
        this.countRating = countRating;
    }

    public int sortForBestSeller(Food other) {
        if (this.getCountRating() != 0 && other.getCountRating() != 0) {
            double ratingA = (double) this.getCountStars() / this.countRating;
            double ratingB = (double) other.getCountStars() / other.countRating;
            if (ratingA == ratingB) {
                double priceA = Double.parseDouble(this.getDiscount());
                double priceB = Double.parseDouble(other.getDiscount());

                return priceA > priceB ? 1 : -1;
            }
            return ratingA > ratingB ? 1 : -1;
        } else if (this.getCountRating() == 0 && other.getCountRating() == 0) {
            double priceA = Double.parseDouble(this.getDiscount());
            double priceB = Double.parseDouble(other.getDiscount());

            return priceA > priceB ? 1 : -1;
        }
        return this.getCountRating() > 0 ? 1 : -1;
    }

    public int sortByPrice(Food other) {
        double priceA = Double.parseDouble(this.price) * (100 - Double.parseDouble(this.getDiscount()));
        double priceB = Double.parseDouble(other.price) * (100 - Double.parseDouble(other.getDiscount()));

        return priceA > priceB ? 1 : -1;

    }

    public int sortByName(Food other) {
        return this.getName().compareToIgnoreCase(other.getName());
    }

    public int sortByRating(Food other) {
        return this.sortForBestSeller(other);
    }

    public String getPriceAfterDiscount() {
        double i = Double.parseDouble(this.getPrice()) * (100 - Double.parseDouble(this.getDiscount())) / 100;
        return String.format("%.0f", i);
    }

    public String getRating() {
        if (this.countRating == 0) return "---";
        double i = (double) this.getCountStars() / this.countRating;
        return String.format("%.1f", i);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCountRating() {
        return countRating;
    }

    public void setCountRating(int countRating) {
        this.countRating = countRating;
    }

    public int getCountStars() {
        return countStars;
    }

    public void setCountStars(int countStars) {
        this.countStars = countStars;
    }
}
