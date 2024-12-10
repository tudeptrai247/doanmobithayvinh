package stu.cntt.gkt3c3.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import stu.cntt.gkt3c3.myapplication.database.DatabaseHelper;
import stu.cntt.gkt3c3.myapplication.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user =binding.edtUser.getText().toString();
                String pass =binding.edtPass.getText().toString();

                if (user.equals("") || pass.equals("")){
                    Toast.makeText(Login.this,getString(R.string.toast_botrong),Toast.LENGTH_SHORT).show();
                }else {
                    Boolean checkCredentials = databaseHelper.checkAccount(user,pass);

                    if (checkCredentials == true){
                        Toast.makeText(Login.this,getString(R.string.toast_dangnhapsuccess),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }else
                    {
                        Toast.makeText(Login.this,getString(R.string.toast_dangnhapfail),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}