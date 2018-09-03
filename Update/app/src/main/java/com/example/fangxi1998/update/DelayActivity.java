package com.example.fangxi1998.update;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DelayActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_delay);
        startLoginAcvtivity();
    }
    private void startLoginAcvtivity() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
//      从welcome界面跳转到登陆界面
                Intent intent = new Intent(DelayActivity.this,LoginActivity.class);
//  启动活动
                startActivity(intent);
//  //结束本Activity ，可以不写
                DelayActivity.this.finish();
            }
        }, 2000);//设置执行时间
    }
}
