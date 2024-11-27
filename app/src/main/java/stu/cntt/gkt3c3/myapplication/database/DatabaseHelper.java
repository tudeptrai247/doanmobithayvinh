package stu.cntt.gkt3c3.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import stu.cntt.gkt3c3.myapplication.model.BookClass;
import stu.cntt.gkt3c3.myapplication.model.CategoryClass;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME ="dbqlsach.sqlite";
    private static final String DB_PATH = "/data/data/stu.cntt.gkt3c3.myapplication/databases/";
    private final Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context,DB_NAME,null,2);
        this.context =context;
        copyDatabase();
    }



    @Override
    public void onCreate(SQLiteDatabase MyDataBase) {
        MyDataBase.execSQL("CREATE TABLE IF NOT EXISTS account (id INTEGER PRIMARY KEY, user TEXT, pass TEXT)");
        MyDataBase.execSQL("CREATE TABLE IF NOT EXISTS category (id INTEGER PRIMARY KEY, name TEXT)");
        MyDataBase.execSQL("CREATE TABLE IF NOT EXISTS book(id INTEGER PRIMARY KEY,name TEXT,category TEXT,img BLOB,amount INTEGER,price TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion <2){
            //bảng category
            db.execSQL("DROP TABLE IF EXISTS category");
            db.execSQL("CREATE TABLE category (id INTEGER PRIMARY KEY,name TEXT)");
            //bảng account
            db.execSQL("DROP TABLE IF EXISTS account");
            db.execSQL("CREATE TABLE account (id INTEGER PRIMARY KEY,user TEXT,pass TEXT)");
            //bảng book
            db.execSQL("DROP TABLE IF EXISTS book");
            db.execSQL("CREATE TABLE book (id INTEGER PRIMARY KEY,name TEXT,category TEXT,img BLOB,amount INTEGER,price TEXT)");
        }
    }
    private void copyDatabase() {
        File dbFile = context.getDatabasePath(DB_NAME); // Đường dẫn tới database
        if (!dbFile.exists()) { // Nếu cơ sở dữ liệu chưa tồn tại
            try {
                // Tạo thư mục databases nếu chưa tồn tại
                File dbDir = dbFile.getParentFile();
                if (!dbDir.exists()) {
                    dbDir.mkdirs();
                }

                // Sao chép tệp từ assets
                InputStream inputStream = context.getAssets().open(DB_NAME);
                OutputStream outputStream = new FileOutputStream(dbFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

                Log.d("DatabaseHelper", "Database copied successfully!");
            } catch (IOException e) {
                Log.e("DatabaseHelper", "Error copying database: " + e.getMessage());
            }
        } else {
            Log.d("DatabaseHelper", "Database already exists.");
        }

        // Kiểm tra và thêm bảng thiếu
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS category (id INTEGER PRIMARY KEY, name TEXT)");
        db.close();
    }


    public boolean checkAccount(String user ,String pass)
    {
        SQLiteDatabase MyDatabase =this.getWritableDatabase();
        Cursor cursor =MyDatabase.rawQuery("Select * from account where user= ? and pass =?",new String[]{user,pass});
        if (cursor.getCount() >0)
        {
            return true;
        }else{
            return false;
        }
    }
    public boolean insertCategory(String name)
    {
        SQLiteDatabase MyDatabase =this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put("name" ,name);
        long result = MyDatabase.insert("category",null,contentValues);
        if(result ==-1)
        {
            return false;
        }else {
            return true;
        }
    }
    public int deleteCategory(int id)
    {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor =MyDatabase.rawQuery("SELECT COUNT(*) FROM book WHERE id= ?",new String[]{String.valueOf(id)});
        int productCount =-1;
        if(cursor.moveToFirst()){
            productCount=cursor.getInt(0);
        }
        cursor.close();

        if(productCount > 0){
            return -1;
        }
        int result =MyDatabase.delete("category","id=?",new String[]{String.valueOf(id)});
        MyDatabase.close();
        return result;
    }
    public boolean updateCategory(int id , String name)
    {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name" , name);
        // cập nhật
        int result =MyDatabase.update("category",contentValues,"id=?",new String[]{String.valueOf(id)});
        MyDatabase.close();
        return result>0;
    }
    public List<CategoryClass> getAllCategory(){
        List<CategoryClass> category = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =db.rawQuery("SELECT id,name FROM category",null);
        if(cursor != null)
        {
            while (cursor.moveToNext()){
                int id =cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                category.add(new CategoryClass(id,name));
            }
            cursor.close();
        }
        db.close();
        return category;
    }
    public  boolean insertBook(String name, String category,byte[] img, int amount, String price){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put("name",name);
        contentValues.put("category",category);
        contentValues.put("img",img);
        contentValues.put("amount",amount);
        contentValues.put("price",price);


        long result =db.insert("book",null,contentValues);
        db.close();
        return result !=-1;
    }
    public List<BookClass> getAllBook(){
        List<BookClass> books =new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =db.rawQuery("SELECT * FROM book",null);
        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String category =cursor.getString(cursor.getColumnIndexOrThrow("category"));
                byte[] img =cursor.getBlob(cursor.getColumnIndexOrThrow("img"));
                int amount=cursor.getInt(cursor.getColumnIndexOrThrow("amount"));
                String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));

                books.add(new BookClass(id,name,category,img,amount,price));

            }while (cursor.moveToNext());
        }
        cursor.close();
        return books;
    }
    public int deleteBook(int id)
    {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        int result =MyDatabase.delete("book","id=?",new String[]{String.valueOf(id)});
        MyDatabase.close();
        return result;
    }
    public boolean updateBook(int id , String name , String category , byte[] img , int amount, String price) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("category", category);
        contentValues.put("img", img);
        contentValues.put("amount", amount);
        contentValues.put("price", price);
        // cập nhật
        int result = MyDatabase.update("book", contentValues, "id=?", new String[]{String.valueOf(id)});
        MyDatabase.close();
        return result > 0;
    }
    public byte[] getBookImageByID(int id){
        SQLiteDatabase MyDatabase =this.getReadableDatabase();
        Cursor cursor =MyDatabase.rawQuery("SELECT img FROM book where id=?",new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()){
            return cursor.getBlob(0);
        }
        cursor.close();
        return null;
    }
}
