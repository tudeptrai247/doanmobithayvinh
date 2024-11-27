package stu.cntt.gkt3c3.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.security.PrivateKey;
import java.util.List;

import stu.cntt.gkt3c3.myapplication.R;
import stu.cntt.gkt3c3.myapplication.model.BookClass;

public class adapterEditSP extends ArrayAdapter<BookClass> {
    private Context context;
    private List<BookClass> sanPhamList;

    public adapterEditSP (Context context ,List<BookClass> sanPhamList){
        super(context,0,sanPhamList);
        this.context=context;
        this.sanPhamList=sanPhamList;
    }
    public View getView(int position, View convertView , ViewGroup parent){
        if(convertView ==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_item_edit_sanpham,parent,false);
        }
        ImageView imageView =convertView.findViewById(R.id.imgHinhAnh);
        TextView textMaSP =convertView.findViewById(R.id.tvMaSP);
        TextView textTenSP =convertView.findViewById(R.id.tvTenSP);
        TextView  textTheLoaiSP =convertView.findViewById(R.id.tvTheLoaiSP);

        // gán dữ liệu từ model
        BookClass sanPham=sanPhamList.get(position);
        // hiện thị
        textTenSP.setText(sanPham.getName());
        textTheLoaiSP.setText(sanPham.getCategory());
       textMaSP.setText(String.valueOf(sanPham.getId()));

        if (sanPham.getImg() !=null){
            Glide.with(context)
                    .load(sanPham.getImg())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageView);
        }else{
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }
        return convertView;
    }
}
