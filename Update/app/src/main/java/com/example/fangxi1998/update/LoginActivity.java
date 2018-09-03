package com.example.fangxi1998.update;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private TextView textView;
    private Button login;
    private EditText username,password;
    private Handler handler;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        handler = new MyHandler();
        textView=findViewById(R.id.textview);
        login = findViewById(R.id.login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,Mainactivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pas = password.getText().toString();

                if ("".equals(user) || user == null) {
                    Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();


                } else if ("".equals(pas) || pas == null) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();


                } else {
                    url = "http://39.106.50.90/login/login.php?username=" + user + "&password=" + pas;
                    HttpUtils.getDataJSON(url,handler);


                }
            }
        });
    }
    class MyHandler extends Handler{
            @Override
            public void handleMessage(Message msg){
                String s = (String) msg.obj;
              //  Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();


                if(s.equals(" 1")) {
                    Intent intent = new Intent(LoginActivity.this, Mainactivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "用户名或密码不正确", Toast.LENGTH_SHORT).show();
                }
            }
        }
}
