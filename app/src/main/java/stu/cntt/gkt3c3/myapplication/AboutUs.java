package stu.cntt.gkt3c3.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class AboutUs extends AppCompatActivity  {
    private MapView mapView;
    Button btnPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getApplicationContext().getPackageName());
        setContentView(R.layout.activity_about_us);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addControls();
        addEvents();
    }
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

        mapView= findViewById(R.id.map);
        btnPhone = findViewById(R.id.btnPhone);
    }

    private void addEvents() {
        hienthiMap();

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(android.net.Uri.parse("tel:0904122412"));
                startActivity(intent);
            }
        });
    }


    private void hienthiMap() {
        mapView.setMultiTouchControls(true);

        mapView.getController().setZoom(15.0);
        mapView.getController().setCenter(new GeoPoint(10.738841, 106.677746));

        Marker marker =new Marker(mapView);
        marker.setPosition(new GeoPoint(10.738841, 106.677746));
        marker.setTitle("Đại Học STU");
        mapView.getOverlays().add(marker);
    }

    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }

    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }





}