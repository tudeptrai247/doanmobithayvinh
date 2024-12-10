package stu.cntt.gkt3c3.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import stu.cntt.gkt3c3.myapplication.database.DatabaseHelper;
import stu.cntt.gkt3c3.myapplication.model.CategoryClass;

public class PhanLoaiActivity extends AppCompatActivity {

    EditText txtTheLoai;
    Button btnLuu;
    DatabaseHelper databaseHelper;
    ListView lvDSTheLoai;
    ArrayAdapter<CategoryClass> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phan_loai);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addControls();
        addEvents();
        updateCategoryList();
    }

    private int selectedCategoryID = -1;
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.new_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==R.id.itemTL){
            Intent intent = new Intent(this,PhanLoaiActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() ==R.id.itemSP){
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
        txtTheLoai =findViewById(R.id.edtTheLoai);
        btnLuu =findViewById(R.id.btnLuuTheLoai);
        lvDSTheLoai = findViewById(R.id.lvTheLoai);
        databaseHelper = new DatabaseHelper(this);
    }

    private void addEvents() {
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name =txtTheLoai.getText().toString().trim();
                if(selectedCategoryID == -1){
                    if(databaseHelper.insertCategory(name)) {
                        Toast.makeText(PhanLoaiActivity.this,getString(R.string.toast_themsp), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PhanLoaiActivity.this, "Thêm Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if(databaseHelper.updateCategory(selectedCategoryID,name)){
                        Toast.makeText(PhanLoaiActivity.this,getString(R.string.toast_updatesp) ,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PhanLoaiActivity.this,"Cập Nhật Thất Bại" ,Toast.LENGTH_SHORT).show();
                    }
                    //reset selected sau khi cập nhật
                    selectedCategoryID =-1;
                }
                txtTheLoai.setText("");
                updateCategoryList();
            }
        });
        lvDSTheLoai.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
               CategoryClass selectCategory = (CategoryClass) adapterView.getItemAtPosition(position);
               int categoryID =selectCategory.getId();
               String messageTitle= getString(R.string.toast_thongbao);
               String tieude=getString(R.string.toast_tieudexoa);
               String yes =getString(R.string.toast_positive);
               String no= getString(R.string.toast_negative);
               new AlertDialog.Builder(PhanLoaiActivity.this)
                       .setTitle(tieude)
                       .setMessage(messageTitle)
                       .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               try{
                                   int result =databaseHelper.deleteCategory(categoryID);
                                   if(result >0){

                                       // tải lại danh sách gọi từ hàm update mỗi khi xóa
                                       Toast.makeText(PhanLoaiActivity.this,getString(R.string.toast_deletesp),Toast.LENGTH_SHORT).show();
                                       updateCategoryList();
                                   }else {
                                       Toast.makeText(PhanLoaiActivity.this, "Xóa Thất bại", Toast.LENGTH_SHORT).show();
                                   }
                               }catch(SQLiteConstraintException e){
                                   String message = getString(R.string.toast_cantdeletesp);
                                   showAlert(message);
                               }
                           }
                       })
                       .setNegativeButton(no,null)
                       .show();
                updateCategoryList();
                return true;
            }
        });
        lvDSTheLoai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CategoryClass selectedCategory =(CategoryClass) adapterView.getItemAtPosition(position);
                // hiện thị dữ liệu lên edittext
                selectedCategoryID = selectedCategory.getId();
                txtTheLoai.setText(String.valueOf(selectedCategory.getName()));

            }
        });
    }
    private void updateCategoryList() {
        List<CategoryClass> category = databaseHelper.getAllCategory(); // lấy ds thể loại csdl
        ArrayAdapter<CategoryClass> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,category);
        lvDSTheLoai.setAdapter(adapter);
    }
    private void showAlert(String message){
        AlertDialog.Builder builder =new AlertDialog.Builder(PhanLoaiActivity.this);
        String yes =getString(R.string.toast_positive);
        builder.setMessage(message)
                .setPositiveButton(yes,null)
                .show();
    }
}