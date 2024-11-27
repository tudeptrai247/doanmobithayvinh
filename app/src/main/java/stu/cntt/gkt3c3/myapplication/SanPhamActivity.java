package stu.cntt.gkt3c3.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import stu.cntt.gkt3c3.myapplication.database.DatabaseHelper;
import stu.cntt.gkt3c3.myapplication.model.BookClass;
import stu.cntt.gkt3c3.myapplication.model.CategoryClass;

import stu.cntt.gkt3c3.myapplication.adapter.adapterEditSP;

public class SanPhamActivity extends AppCompatActivity {
    EditText txtTenSp , txtSL ,txtGia ,txtAnh;
    Spinner txtTheLoai;
    Button  btnLuu , btnChonAnh;
    ImageView imgHinhAnh;
    ListView lvSanPhamEdit;
    DatabaseHelper databaseHelper;
    Uri imageUri;
    ArrayAdapter<CategoryClass> adapter;

    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addControls();
        addEvents();
        hienThiDuLieuEdit();
    }
    private int selectedIdBook=-1;
    private void hienThiDuLieuEdit() {
        Intent intent = getIntent();

        if(intent != null){
            int id = intent.getIntExtra("id",-1);
            selectedIdBook =id;
            String name =intent.getStringExtra("name");
            String category =intent.getStringExtra("category");
            int amount =intent.getIntExtra("amount",0);
            String price =intent.getStringExtra("price");

            byte[] imageByteArray =databaseHelper.getBookImageByID(id);

            if(imageByteArray != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray,0,imageByteArray.length);
                imgHinhAnh.setImageBitmap(bitmap);
            }
            if(name != null)
                txtTenSp.setText(name);
            if(category != null) {
                List<CategoryClass> categories =databaseHelper.getAllCategory();
                for(int i=0;i<categories.size();i++){
                    if(categories.get(i).getName().equals(category)) {
                        txtTheLoai.setSelection(i);
                        break;
                    }
                }
            }
            if(amount != 0)
                txtSL.setText(String.valueOf(amount));
            if(price != null)
                txtGia.setText(price);
        }
    }
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
        txtTenSp = findViewById(R.id.edtTen);
        txtSL = findViewById(R.id.edtSL);
        txtGia =findViewById(R.id.edtGia);
        txtTheLoai = findViewById(R.id.edtTheLoai);
        btnChonAnh=findViewById(R.id.btnChonAnh);
        btnLuu=findViewById(R.id.btnLuuSP);
        imgHinhAnh =findViewById(R.id.imgHinhanh);
        databaseHelper= new DatabaseHelper(this);


        List<CategoryClass> categories = databaseHelper.getAllCategory();
        ArrayAdapter<CategoryClass> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtTheLoai.setAdapter(adapter);


    }

    private void addEvents() {
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name =txtTenSp.getText().toString();
                String price =txtGia.getText().toString();
                int categoryPosition =txtTheLoai.getSelectedItemPosition();
                CategoryClass selectedCategory =(CategoryClass) txtTheLoai.getSelectedItem();
                int categoryID =selectedCategory.getId();
                String amountStr =txtSL.getText().toString();
                String catagoryName =selectedCategory.getName();


                if(name.isEmpty() || price.isEmpty() || amountStr.isEmpty()){
                    Toast.makeText(SanPhamActivity.this,"Vui Lòng Điền Đầy Đủ Thông Tin",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(imageUri == null && selectedIdBook ==-1){
                    Toast.makeText(SanPhamActivity.this,"Vui Lòng Điền Đầy Đủ Thông Tin",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {

                    int amount = Integer.parseInt(amountStr);
                   byte[] image =null;
                   if(imageUri != null){
                       image =imageUriToByteArray(imageUri);
                   }else if(selectedIdBook != -1){
                       Intent intent =getIntent();
                       image = intent.getByteArrayExtra("img");
                   }
                    boolean result;
                    if (selectedIdBook == -1) {
                        result = databaseHelper.insertBook(name, catagoryName, image, amount, price);
                        if (result) {
                            Toast.makeText(SanPhamActivity.this, "Sản Phẩm Đã Được Lưu", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SanPhamActivity.this, "Lưu Thất Bại", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        result= databaseHelper.updateBook(selectedIdBook,name,catagoryName,image,amount,price);
                        if (result){
                            Toast.makeText(SanPhamActivity.this,"Sản Phẩm Được Cập Nhật",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SanPhamActivity.this,"Cập Nhật Thất Bại",Toast.LENGTH_SHORT).show();
                        }
                        selectedIdBook=-1;
                    }
                    if(result){
                        Intent intent = new Intent(SanPhamActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(SanPhamActivity.this,"Lỗi khi chuyển ảnh",Toast.LENGTH_SHORT).show();
                }

// cập nhật listview
                Intent intent = new Intent(SanPhamActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
    private byte[] imageUriToByteArray(Uri uri)throws IOException{
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);  // Nén ảnh thành byte array
        return byteArrayOutputStream.toByteArray();
    }
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                // Hiển thị ảnh trong ImageView
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imgHinhAnh.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}