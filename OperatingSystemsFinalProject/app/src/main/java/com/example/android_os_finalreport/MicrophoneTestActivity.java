package com.example.android_os_finalreport;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import androidx.navigation.ui.NavigationUI;

import com.example.android_os_finalreport.databinding.FragmentSecondBinding;
import com.google.android.material.snackbar.Snackbar;



public class MicrophoneTestActivity extends AppCompatActivity {


    private static final int SAMPLE_RATE = 44100; // 採樣率
    private AudioRecord audioRecord;
    private EKGView ekgView; // 替換 ProgressBar
    private TextView textVolume;
    private Button buttonSoundTest;
    private boolean isRecording = false;
    private Handler handler = new Handler();
    private int bufferSize;

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microphone_test);

        // 初始化麥克風測試相關視圖
        ekgView = findViewById(R.id.ekgView); // 替換 ProgressBar
        textVolume = findViewById(R.id.textVolume);
        buttonSoundTest = findViewById(R.id.buttonSoundTest);

        bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        buttonSoundTest.setOnClickListener(v -> {
            if (isRecording) {
                stopSoundTest();
            } else {
                startSoundTest();
            }
        });


        // 設置 Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 禁用標題文字
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


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


    }

    private void startSoundTest() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // 請求麥克風權限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            return;
        }

        // 初始化 AudioRecord
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);

        audioRecord.startRecording();
        isRecording = true;
        buttonSoundTest.setText("Stop Test");
        updateVolumeLevel();
    }

    // 處理用戶授予或拒絕權限的回調
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) { // 請求碼與 `requestPermissions` 中保持一致
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用戶授予權限後，重新啟動錄音流程
                startSoundTest();
            } else {
                // 用戶拒絕權限，提示用戶需要授權
                Toast.makeText(this, "Permission for microphone is required to test sound.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void stopSoundTest() {
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        isRecording = false;
        buttonSoundTest.setText("Start Test");
        handler.removeCallbacksAndMessages(null); // 移除所有延遲的任務
        ekgView.updateEKGPoints(0); // 重置心電圖
    }

    private void updateVolumeLevel() {
        if (isRecording) {
            handler.postDelayed(() -> {
                if (isRecording && audioRecord != null) {
                    short[] buffer = new short[bufferSize / 2];
                    int read = audioRecord.read(buffer, 0, buffer.length);
                    if (read > 0) {
                        int maxAmplitude = 0;
                        for (short sample : buffer) {
                            maxAmplitude = Math.max(maxAmplitude, Math.abs(sample));
                        }

                        // 調整音量計算方法，擴展音量範圍
                        float volume = maxAmplitude / 32767f * 142; // 將音量映射到 0-100 的範圍
                        ekgView.updateEKGPoints(volume/8); // 更新心電圖 View，放大震幅
                        textVolume.setText("Microphone Volume: " + (int) volume);
                    }

                    updateVolumeLevel(); // 繼續更新
                }
            }, 100); // 調整更新頻率
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        // 確保導航到主頁 Fragment
        navController.navigate(R.id.FirstFragment);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_search) {
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSoundTest();
    }
}
