package stu.cntt.gkt3c3.myapplication.model;

import android.net.Uri;

import java.sql.Blob;

public class BookClass {
    private int id;
    private String name;
    private String category;
    private byte[] img;
    private int amount;
    private String price;
    public BookClass(){

    }

    public BookClass(int id, String name, String category, byte[] img, int amount, String price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.img = img;
        this.amount = amount;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", img='" + img + '\'' +
                ", amount=" + amount +
                ", price=" + price
                ;
    }
}
