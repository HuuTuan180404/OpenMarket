package com.example.oumarket.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.oumarket.Class.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "OuMarket.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductName", "ProductId", "Price", "Quantity", "Discount"};
        String sqlTable = "OrderDetail";

        sqLiteQueryBuilder.setTables(sqlTable);
        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, sqlSelect, null, null, null, null, null);

        List<Order> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int productIdIndex = cursor.getColumnIndex("ProductId");
                int productNameIndex = cursor.getColumnIndex("ProductName");
                int priceIndex = cursor.getColumnIndex("Price");
                int quantityIndex = cursor.getColumnIndex("Quantity");
                int discountIndex = cursor.getColumnIndex("Discount");

                // Kiểm tra nếu các cột đều tồn tại
                if (productIdIndex != -1 && priceIndex != -1 && productNameIndex != -1 && quantityIndex != -1 && discountIndex != -1) {
                    result.add(new Order(cursor.getString(productIdIndex), cursor.getString(productNameIndex), cursor.getString(priceIndex), cursor.getString(quantityIndex), cursor.getString(discountIndex)));
                } else {

                }
            } while (cursor.moveToNext());
        }

        removeDuplicates(result);

        return result;
    }

    public void add_list_to_cart(List<Order> list) {
        for (Order order : list) {
            addToCart(order);
        }
    }

    public void removeDuplicates(List<Order> result) {
        result.sort((a, b) -> a.compareTo(b));
        for (int i = 0; i < result.size() - 1; i++) {
            int j = i + 1;
            while (j < result.size()) {
                if (result.get(i).getProductId().equals(result.get(j).getProductId())) {
                    int x = Integer.parseInt(result.get(i).getQuantity()) + Integer.parseInt(result.get(j).getQuantity());
                    result.get(i).setQuantity(x + "");
                    result.remove(j);
                } else {
                    j++;
                }
            }
        }
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("ProductId", order.getProductId());
        values.put("ProductName", order.getProductName());
        values.put("Price", order.getPrice());
        values.put("Quantity", order.getQuantity());
        values.put("Discount", order.getDiscount());
        db.insert("OrderDetail", null, values);
        db.close();
    }

    public void updateQuantity(String productId, String newQuantity) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Quantity", newQuantity);

        db.update("OrderDetail", values, "ProductId = ?", new String[]{productId});

        db.close();
    }

    public void removeItems(Order order) {
        SQLiteDatabase db = getWritableDatabase(); // Lấy cơ sở dữ liệu ở chế độ ghi
        String whereClause = "ProductId = ?";
        String[] whereArgs = {order.getProductId()};

        db.delete("OrderDetail", whereClause, whereArgs);
        db.close();
    }

    public void cleanCart() {
        deleteTable("OrderDetail");
    }

    ////////////////////////////////

    public void deleteTable(String table) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("DELETE FROM %s", table);
        sqLiteDatabase.execSQL(query);
    }
}
