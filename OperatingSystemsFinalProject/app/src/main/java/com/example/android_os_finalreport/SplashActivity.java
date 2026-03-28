package com.example.android_os_finalreport;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide; // 确保已添加 Glide 的依赖
import com.example.android_os_finalreport.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 获取 ImageView 引用
        ImageView splashImageView = findViewById(R.id.splashImageView);

        // 使用 Glide 加载 GIF 动画
        Glide.with(this)
                .asGif() // 确保加载的是 GIF 格式
                .load(R.drawable.welcome) // qq123.gif 放在 res/drawable 文件夹中
                .into(splashImageView);

        // 延迟 ？ 秒后跳转到主页面
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 6200); // 延迟 ？ 秒
    }
}
