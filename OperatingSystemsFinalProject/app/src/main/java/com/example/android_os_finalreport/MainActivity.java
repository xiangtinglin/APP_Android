package com.example.android_os_finalreport;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.android_os_finalreport.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.android_os_finalreport.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.graphics.Color;  // 導入 Color 類
import android.widget.TextView;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 使用 ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 設置 Toolbar
        setSupportActionBar(binding.toolbar);

        // 禁用標題文字
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // 添加自訂圖片到 Toolbar
        ImageView menuIcon = new ImageView(this);
        menuIcon.setImageResource(R.drawable.menu); // 替換為你的圖片資源
        menuIcon.setContentDescription("Menu Icon");

        // 設置圖片的佈局參數
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(50, 70, 0, 0); // 左側間距
        params.gravity = android.view.Gravity.START; // 確保圖片在左上角
        menuIcon.setLayoutParams(params);

        // 將圖片添加到 Toolbar
        binding.toolbar.addView(menuIcon);

        // 設置圖片點擊事件
        menuIcon.setOnClickListener(v -> {
            // 點擊行為，例如彈出對話框
            showMenuDialog();
        });

        // 設置導航控制器
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // 設置浮動按鈕行為
        binding.fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show()
        );

        
        // 設置 Home 按鈕點擊事件    
        ImageView homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> {
            // 執行黃色閃光特效
            homeButton.setColorFilter(Color.LTGRAY); // 設置按鈕為黃色
            homeButton.animate()
                    .alpha(0.5f) // 透明度降低至 50%
                    .scaleX(1.5f) // 放大 1.5 倍
                    .scaleY(1.5f) // 放大 1.5 倍
                    .setDuration(150) // 持續時間 150 毫秒
                    .withEndAction(() -> {
                        homeButton.setColorFilter(null); // 恢復原始顏色
                        homeButton.animate()
                                .alpha(1.0f) // 恢復透明度
                                .scaleX(1f) // 恢復按鈕大小
                                .scaleY(1f) // 恢復按鈕大小
                                .setDuration(150) // 持續時間 150 毫秒
                                .withEndAction(() -> {
                                    // 執行導航到首頁
                                    NavController homeNavController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                                    homeNavController.navigate(R.id.FirstFragment);
                                })
                                .start();
                    })
                    .start();
        });

        // 找到 TextView，並設定文字
        TextView textView = findViewById(R.id.textViewTitle);
        textView.setText("【 Detection｜檢測項目 】");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 載入選單資源
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search) {
            // 点击搜索按钮后执行的
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }




    private void showMenuDialog() {
        // 創建一個全屏的深色背景來暗掉其他部分，顏色設為純黑色
        View darkBackground = new View(this);
        darkBackground.setBackgroundColor(Color.parseColor("#000000")); // 更深的背景顏色，純黑色
        darkBackground.setAlpha(0.5f); // 完全不透明背景

        // 創建 ImageView 用來顯示 catalog.png
        ImageView catalogImage = new ImageView(this);
        catalogImage.setImageResource(R.drawable.catalog); // 確保 catalog.png 圖片存在於 res/drawable 目錄中

        // 設置圖片的位置和大小，並將圖片與 menu 一樣高度
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        // 假設工具列的高度為 130dp（可以根據您的設置調整）
        int toolbarHeight = (int) getResources().getDimension(R.dimen.toolbar_height); // 取得工具列高度，需在 dimens.xml 定義

        params.topMargin = toolbarHeight; // 設置圖片與 menu 按鈕一樣的高度
        catalogImage.setLayoutParams(params);

        // 將圖片和暗背景加到根佈局
        FrameLayout rootLayout = findViewById(android.R.id.content); // 根佈局
        rootLayout.addView(darkBackground);  // 顯示純黑色背景
        rootLayout.addView(catalogImage);    // 顯示圖片

        // 設置圖片點擊事件，點擊時隱藏圖片和背景
        catalogImage.setOnClickListener(v -> hideCatalogImage(darkBackground, catalogImage));

        // 設置背景點擊事件，點擊時隱藏圖片和背景
        darkBackground.setOnClickListener(v -> hideCatalogImage(darkBackground, catalogImage));
    }

    // 隱藏圖片和背景
    private void hideCatalogImage(View darkBackground, ImageView catalogImage) {
        FrameLayout rootLayout = findViewById(android.R.id.content); // 根佈局
        rootLayout.removeView(darkBackground);  // 移除背景
        rootLayout.removeView(catalogImage);    // 移除圖片
    }




}
