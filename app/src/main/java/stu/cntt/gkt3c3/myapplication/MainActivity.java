package stu.cntt.gkt3c3.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import stu.cntt.gkt3c3.myapplication.adapter.adapterEditSP;
import stu.cntt.gkt3c3.myapplication.database.DatabaseHelper;
import stu.cntt.gkt3c3.myapplication.model.BookClass;

public class MainActivity extends AppCompatActivity {
    ListView lvSanPham;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addControls();
        updateListView();
        addEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.new_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==R.id.itemTL){
            Intent intent = new Intent(this,PhanLoaiActivity.class);
            startActivity(intent);
        }if(item.getItemId() ==R.id.itemSP){
            Intent intent = new Intent(this,SanPhamActivity.class);
            startActivity(intent);
        }if(item.getItemId() ==R.id.itemGT){
            Intent intent = new Intent(this,AboutUs.class);
            startActivity(intent);
        }if(item.getItemId() ==R.id.trangchu){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void addControls() {
        lvSanPham = findViewById(R.id.lvSanPham);
        databaseHelper = new DatabaseHelper(this);
        ArrayList<BookClass> bookList = new ArrayList<>();
        ArrayAdapter<BookClass> adapter = new ArrayAdapter<>(
                this,
                R.layout.activity_item_edit_sanpham,
                bookList
        );
        lvSanPham.setAdapter(adapter);

    }

    private void updateListView() {
        List<BookClass> books = databaseHelper.getAllBook();
        adapterEditSP adapter =new adapterEditSP(this,books);
        lvSanPham.setAdapter(adapter);
    }

    private void addEvents() {
        lvSanPham.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                BookClass selectBook = (BookClass) adapterView.getItemAtPosition(position);
                int bookID =selectBook.getId();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Xác Nhận Xóa")
                        .setMessage("Bạn có muốn xóa không")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int result =databaseHelper.deleteBook(bookID);
                                if(result >0)
                                    {
                                        // tải lại danh sách gọi từ hàm update mỗi khi xóa
                                        Toast.makeText(MainActivity.this,"Xóa thành công",Toast.LENGTH_SHORT).show();
                                        updateBookList();
                                    }else {
                                        Toast.makeText(MainActivity.this, "Xóa Thất bại", Toast.LENGTH_SHORT).show();
                                    }
                            }
                        }).setNegativeButton("Không",null)
                        .show();
                return true;
            }
        });
        lvSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                BookClass selectedBook = (BookClass) adapterView.getItemAtPosition(position);
                byte[] imageByte =selectedBook.getImg();
                Bitmap bitmap =BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);



                Intent intent = new Intent(MainActivity.this, SanPhamActivity.class);

                // đưa gói hàng lên

                intent.putExtra("id", selectedBook.getId());
                intent.putExtra("name", selectedBook.getName());
                intent.putExtra("category", selectedBook.getCategory());
                intent.putExtra("img",selectedBook.getImg());
                intent.putExtra("amount", selectedBook.getAmount());
                intent.putExtra("price", selectedBook.getPrice());
                startActivity(intent);
            }
        });
    }


    private void updateBookList() {
        List<BookClass> book = databaseHelper.getAllBook();
        adapterEditSP adapter =new adapterEditSP(this,book);
        lvSanPham.setAdapter(adapter);
    }


}