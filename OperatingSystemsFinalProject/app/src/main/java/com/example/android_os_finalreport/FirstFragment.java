package com.example.android_os_finalreport;

import static android.content.Context.VIBRATOR_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;

import com.example.android_os_finalreport.databinding.FragmentFirstBinding;

import android.Manifest;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;

import android.widget.Toast;
import android.os.Vibrator;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.provider.Settings;
import android.os.Looper;
import android.widget.VideoView;
import android.location.Geocoder;
import android.location.Address;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private GLSurfaceView glSurfaceView;
    private Button startTestButton;
    private TextView fpsTextView;
    private static final String TAG = "FirstFragment";
    private int Status_cnt=0;
    private LocationManager locationManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private ProgressBar progressBar;
    private int progress = 0; // 追蹤進度
    private Set<Integer> clickedButtons = new HashSet<>();
    private static final String PREFS_NAME = "ProgressPrefs";
    private static final String KEY_PROGRESS = "progress";
    private static final String KEY_CLICKED_BUTTONS = "clicked_buttons";

    private SharedPreferences preferences;

    // 追蹤進度
    private final int[] buttonIds = new int[] {
            R.id.buttonEthernetSpeed,
            R.id.buttonReadsdcard,
            R.id.buttonSoundTest,
            R.id.buttonGPSTest,
            R.id.buttonVibrateTest,
            R.id.buttonReadCPU,
            R.id.buttonReadBattery,
            R.id.buttonReadSim,
            R.id.buttonPhoneInformation,
            R.id.buttonTestCPUPerformance,
            R.id.buttonTestScreen
    };
    private TextView textViewProgress;
    private VideoView progressVideoView; // 新增成員變數


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 使用 ViewBinding 来获取视图
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        // 设置按钮点击事件
        binding.buttonReadCPU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建并显示 AlertDialog
                new AlertDialog.Builder(requireContext())  // 使用 requireContext() 获取上下文
                        .setTitle("訊息")
                        .setMessage("這是CPU資訊")
                        .setPositiveButton("確定", null)
                        .show();
            }
        });
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonResetProgress.setOnClickListener(v -> {
            resetProgress();
            Snackbar.make(v, "進度已重置", Snackbar.LENGTH_SHORT).show();
        });


        // 初始化進度條和文字
        progressBar = binding.StatusProgressBar;
        progressBar.setMax(buttonIds.length);
        textViewProgress = binding.textViewProgress;
        textViewProgress.setText("測試進度 " + progress + "/" + buttonIds.length + "項");

        preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // 恢復進度
        progress = preferences.getInt(KEY_PROGRESS, 0);

        // 將 Set<String> 轉換回 Set<Integer>
        Set<String> clickedButtonsStringSet = preferences.getStringSet(KEY_CLICKED_BUTTONS, new HashSet<>());
        clickedButtons.clear();
        if (clickedButtonsStringSet != null) {
            for (String buttonId : clickedButtonsStringSet) {
                clickedButtons.add(Integer.parseInt(buttonId));
            }
        }

        progressBar = binding.StatusProgressBar;
        textViewProgress = binding.textViewProgress;

        progressBar.setMax(buttonIds.length);
        progressBar.setProgress(progress);
        textViewProgress.setText("測試進度 " + progress + "/" + buttonIds.length + "項");

        for (int buttonId : buttonIds) {
            view.findViewById(buttonId).setOnClickListener(v -> updateProgress(buttonId));
        }
        // 初始化 VideoView
        progressVideoView = view.findViewById(R.id.videoView_progress);


        // 初始化 MP4 顯示
        updateVideo();

        // 設定按鈕點擊監聽事件，動態更新進度
        for (int buttonId : buttonIds) {
            view.findViewById(buttonId).setOnClickListener(v -> updateProgress(buttonId));
        }


        // 【battery】設置按鈕的點擊監聽事件
        binding.buttonReadBattery.setOnClickListener(v ->

        {
            readBatteryInfo();
            updateProgress(v.getId());
        });
        binding.buttonReadSim.setOnClickListener(v ->

        {
            readSimInfo();
            updateProgress(v.getId());
        });
        binding.buttonReadsdcard.setOnClickListener(v ->

        {
            readSDCardInfo();
            updateProgress(v.getId());
        });
        binding.buttonReadCPU.setOnClickListener(v ->

        {
            readCPUInfo();
            updateProgress(v.getId());
        }); // 修改這行
        binding.buttonTestCPUPerformance.setOnClickListener(v ->

        {
            testCpuPerformance();
            updateProgress(v.getId());
        });
        binding.buttonTestScreen.setOnClickListener(v ->

        {
            // 更換按鈕圖片為 change.png
            v.setBackgroundResource(R.drawable.icon_screen_test_colorful);

            v.postDelayed(() -> {
                // 恢復按鈕圖片為原圖
                v.setBackgroundResource(R.drawable.icon_screen_test);

                // 再延遲執行導航事件，確保換圖完成
                v.postDelayed(() -> {
                    // 執行導航到 SecondFragment
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);

                    // 調整音量
                    AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                    if (audioManager != null) {
                        // 最大音量
                        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                        // 設置目標音量比例，例如 40% 音量
                        int targetVolume = (int) (maxVolume * 0.8);

                        // 設置音量
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, targetVolume, 0);
                    }

                    // 播放音檔
                    MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.voice_screen);
                    mediaPlayer.start();

                    // 釋放資源
                    mediaPlayer.setOnCompletionListener(mp -> {
                        mediaPlayer.release();
                    });
                }, 0); // 無延遲立即執行導航事件
            }, 250); // 延遲 250 毫秒恢復原圖
            updateProgress(v.getId());
        });

        binding.buttonEthernetSpeed.setOnClickListener(v ->

        {
            testNetworkSpeed();
            updateProgress(v.getId());
        });
        binding.buttonVibrateTest.setOnClickListener(v ->

        {
            testVibrate();
            updateProgress(v.getId());
        });
        binding.buttonGPSTest.setOnClickListener(v ->

        {
            testGPS();
            updateProgress(v.getId());
        });
        binding.buttonPhoneInformation.setOnClickListener(v ->

        {
            testPhone_inform();
            updateProgress(v.getId());
        });
        binding.buttonSoundTest.setOnClickListener(v ->

        {
            testSound();
            updateProgress(v.getId());
        });


        //廣告區
        VideoView videoView = view.findViewById(R.id.videoView_ad);
        // 設置影片來源
        String videoPath = "android.resource://" + requireContext().getPackageName() + "/" + R.raw.pic_ad1;
        videoView.setVideoPath(videoPath);

        // 啟動自動播放
        videoView.setOnPreparedListener(mp ->

        {
            mp.setLooping(true); // 循環播放
            videoView.start();
        });

        // 如果需要，添加播放控制條
        videoView.setMediaController(new android.widget.MediaController(

                requireContext()));

    }

    private void resetProgress() {
        progress = 0;
        clickedButtons.clear();
        progressBar.setProgress(progress);
        textViewProgress.setText("測試進度 " + progress + "/" + buttonIds.length + "項");

        // 清空SharedPreferences的儲存
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_PROGRESS, progress);
        editor.putStringSet(KEY_CLICKED_BUTTONS, new HashSet<>());
        editor.apply();
    }


    private void updateProgress(int buttonId) {
        // 檢查按鈕是否已經被點擊過
        if (!clickedButtons.contains(buttonId)) {
            clickedButtons.add(buttonId); // 標記按鈕為已點擊
            progress++;
            progressBar.setProgress(progress);
            textViewProgress.setText("測試進度 " + progress + "/" + buttonIds.length + "項");

        }
        // 動態更新 VideoView 顯示的 MP4 文件
        updateVideo();

    }

    @Override
    public void onPause() {
        super.onPause();
        // 保存進度
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_PROGRESS, progress);

        // 將 Set<Integer> 轉換為 Set<String>
        Set<String> clickedButtonsStringSet = new HashSet<>();
        for (Integer buttonId : clickedButtons) {
            clickedButtonsStringSet.add(String.valueOf(buttonId));
        }
        editor.putStringSet(KEY_CLICKED_BUTTONS, clickedButtonsStringSet);
        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 在應用真正退出時重置進度
        requireActivity().getApplication().registerActivityLifecycleCallbacks(new ResetProgressOnExit());

    }

    private class ResetProgressOnExit implements android.app.Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            // 不需要處理
        }

        @Override
        public void onActivityStarted(Activity activity) {
            // 不需要處理
        }

        @Override
        public void onActivityResumed(Activity activity) {
            // 不需要處理
        }

        @Override
        public void onActivityPaused(Activity activity) {
            // 不需要處理
        }

        @Override
        public void onActivityStopped(Activity activity) {
            // 不需要處理
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            // 不需要處理
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (activity.isTaskRoot()) {
                // 只有在應用退出時重置進度
                progress = 0;
                clickedButtons.clear();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(KEY_PROGRESS, 0);
                editor.putStringSet(KEY_CLICKED_BUTTONS, new HashSet<>());
                editor.apply();
            }
        }

    }


    private void updateVideo() {
        String videoPath;

        // 根據進度決定要顯示的 MP4 文件
        if (progress <= 2) {
            videoPath = "android.resource://" + requireContext().getPackageName() + "/" + R.raw.egg1;
        } else if (progress > 2 && progress <= 4) {
            videoPath = "android.resource://" + requireContext().getPackageName() + "/" + R.raw.egg2;
        } else {
            videoPath = "android.resource://" + requireContext().getPackageName() + "/" + R.raw.egg3;
        }

        // 設定 VideoView 資源
        progressVideoView.setVideoPath(videoPath);
        progressVideoView.setOnPreparedListener(mp -> {
            mp.setLooping(true); // 循環播放
            progressVideoView.start(); // 啟動播放
        });
    }


    private void testSound() {
        // 在主執行緒中更換按鈕圖示
        requireActivity().runOnUiThread(() -> {
            binding.buttonSoundTest.setBackgroundResource(R.drawable.icon_microphone_coloful);
        });
        // 延遲 150 毫秒後恢復原圖，再繼續執行事件
        binding.buttonSoundTest.postDelayed(() -> {
            // 恢復原圖示
            requireActivity().runOnUiThread(() -> {
                binding.buttonSoundTest.setBackgroundResource(R.drawable.icon_microphone);
            });

            Intent intent = new Intent(getActivity(), MicrophoneTestActivity.class);
            startActivity(intent);
        }, 150); // 延遲 150 毫秒
    }


    private void testPhone_inform() {
        // 獲取手機相關資訊
        String phoneInformation = getPhoneInformation();

        // 使用新的方法顯示對話框
        showCustomAlertDialog("手機身分證", phoneInformation);
    }

    // 新的方法，用於顯示自定義內容
    private void showCustomAlertDialog(String title, String message) {
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("確定", null)
                .show();
    }

    private String getPhoneInformation() {
        return
                "製造商: " + Build.MANUFACTURER + "\n" +
                        "型號: " + Build.MODEL + "\n" +
                        "產品名稱: " + Build.PRODUCT + "\n" +
                        "硬體名稱: " + Build.HARDWARE + "\n" +
                        "品牌: " + Build.BRAND + "\n" +
                        "Android 版本: " + Build.VERSION.RELEASE + "\n" +
                        "API 等級: " + Build.VERSION.SDK_INT;
    }


    private void testGPS() {
        // 在主執行緒中更換按鈕圖示
        requireActivity().runOnUiThread(() -> {
            binding.buttonGPSTest.setBackgroundResource(R.drawable.icon_gps_colorful);
        });
        // 延遲 150 毫秒後恢復原圖，再繼續執行事件
        binding.buttonGPSTest.postDelayed(() -> {
            // 恢復按鈕圖示
            requireActivity().runOnUiThread(() -> {
                binding.buttonGPSTest.setBackgroundResource(R.drawable.icon_gps);
            });

            // 獲取系統服務
            LocationManager locationManager = (LocationManager) requireContext().getSystemService(LocationManager.class);

            // 檢查並確保 GPS 已啟用
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGPSEnabled) {
                // 提示用戶打開 GPS
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            } else {
                // 獲取定位權限
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    // 已經擁有權限，獲取定位信息
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    // 如果定位信息不為空
                    if (location != null) {
                        // 獲取當前位置
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        handleLocationAndCountry(latitude, longitude, location.getAccuracy());
                    } else {
                        // 如果沒有獲取到定位信息，嘗試請求一次定位
                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, newLocationListener, Looper.getMainLooper());
                    }
                }
            }
        }, 150); // 延遲 150 毫秒
    }

    // 處理經緯度並顯示國家名稱和信號質量
    private void handleLocationAndCountry(double latitude, double longitude, float accuracy) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String countryName = addresses != null && !addresses.isEmpty() ? addresses.get(0).getCountryName() : "無法獲取國家名稱";

            // 根據精度評估 GPS 信號質量
            String signalQuality;
            if (accuracy < 50) {
                signalQuality = "優";
            } else if (accuracy < 200) {
                signalQuality = "一般";
            } else {
                signalQuality = "弱";
            }

            // 使用 AlertDialog 顯示位置信息
            String locationText = "緯度 : " + latitude + "\n" + "經度 : " + longitude + "\n" +
                    "國家 : " + countryName + "\n"
                    + "\n" +
                    "GPS 信號品質: " + signalQuality + "\n" +
                    "GPS 精準度  : " + accuracy + "米";

            new AlertDialog.Builder(requireContext())
                    .setTitle("定位信息")
                    .setMessage(locationText)
                    .setPositiveButton("確定", (dialog, which) -> dialog.dismiss())
                    .show();
        } catch (IOException e) {
            e.printStackTrace();
            new AlertDialog.Builder(requireContext())
                    .setTitle("錯誤")
                    .setMessage("無法獲取地理資訊")
                    .setPositiveButton("確定", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }

    // 監聽定位變化的監聽器
    private final LocationListener newLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                // 每當位置變化時，調用此方法
                handleLocationAndCountry(location.getLatitude(), location.getLongitude(), location.getAccuracy()); // 處理新的定位信息
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    // 現有的 onRequestPermissionsResult 方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 獲取定位權限後，開始請求定位信息
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) requireContext().getSystemService(LocationManager.class);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, newLocationListener);
                    // 在此調用 testGPS() 方法獲取GPS信息
                    testGPS();
                }
            } else {
                new AlertDialog.Builder(requireContext())
                        .setTitle("權限被拒絕")
                        .setMessage("權限被拒絕，無法獲取 GPS 信息")
                        .setPositiveButton("確定", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        }

        // 您可以在這裡添加其他請求碼的處理邏輯
        // 例如：
        // if (requestCode == ANOTHER_PERMISSION_REQUEST_CODE) {
        //     // 處理其他權限的回應
        // }
    }






    private void testVibrate() {

        // 在主執行緒中更換按鈕圖示
        requireActivity().runOnUiThread(() -> {
            binding.buttonVibrateTest.setBackgroundResource(R.drawable.icon_vibrate_colorful);
        });
        // 延遲 150 毫秒後恢復原圖，再繼續執行事件
        binding.buttonVibrateTest.postDelayed(() -> {
            // 恢復原圖示
            requireActivity().runOnUiThread(() -> {
                binding.buttonVibrateTest.setBackgroundResource(R.drawable.icon_vibrate);
            });

            Vibrator vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);

            if (vibrator != null) {
                // Android 6.0 使用 vibrate(long) 或 vibrate(pattern, repeat)
                long[] pattern = {0, 500, 200, 500}; // 震动500ms，暂停200ms，再震动500ms
                vibrator.vibrate(pattern, -1); // -1 表示不重复震动

                // 顯示一個 Toast
                Toast.makeText(requireContext(), "Vibrator Test Complete", Toast.LENGTH_SHORT).show();

                // 如果想在 Logcat 中輸出
                Log.d("VibrationTest", "Vibrator Test Complete");
            } else {
                // 顯示一個 Toast 提示震動服務不可用
                Toast.makeText(requireContext(), "Vibrator service not available", Toast.LENGTH_SHORT).show();
            }
        }, 150); // 延遲 150 毫秒
    }


    private void readSimInfo() {

        // 在主程序中換圖
        requireActivity().runOnUiThread(() -> {
            // 更换按鈕圖
            binding.buttonReadSim.setBackgroundResource(R.drawable.icon_sim_colorful);
        });

        // 延遲 ? 秒後恢復原圖，再接續執行事件
        binding.buttonReadSim.postDelayed(() -> {
            // 確保恢復原圖
            requireActivity().runOnUiThread(() -> {
                binding.buttonReadSim.setBackgroundResource(R.drawable.icon_sim);
            });

            //開始執行檢測
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                return;
            }

            TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                String operatorName = tm.getNetworkOperatorName();
                String operatorCode = tm.getNetworkOperator();

                if (operatorName == null || operatorName.isEmpty() || operatorCode == null || operatorCode.isEmpty()) {
                    operatorName = "未插入 SIM 卡";
                    operatorCode = "無資訊";
                }

                // 顯示訊息在 AlertDialog 中
                new AlertDialog.Builder(getContext())
                        .setTitle("SIM 卡資訊")
                        .setMessage("運營商名稱: " + operatorName + "\n運營商代碼: " + operatorCode)
                        .setPositiveButton("確定", null)
                        .show();
            }
        }, 150); // 延遲 150 毫秒
    }


    private void readSDCardInfo() {
        // 在主執行緒中更換按鈕圖片
        requireActivity().runOnUiThread(() -> {
            // 假設按鈕為 binding.buttonReadSDCardInfo，將按鈕圖片更換為 change.png
            binding.buttonReadsdcard.setBackgroundResource(R.drawable.icon_sd_info_colorful);
        });

        // 延遲 500 毫秒後恢復圖片，然後執行原有邏輯
        binding.buttonReadsdcard.postDelayed(() -> {
            // 在主執行緒中恢復按鈕原始圖片
            requireActivity().runOnUiThread(() -> {
                binding.buttonReadsdcard.setBackgroundResource(R.drawable.icon_sd_info);
            });

            // 執行SD的檢測
            String info;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
                long blockSize = stat.getBlockSizeLong();
                long availableBlocks = stat.getAvailableBlocksLong();
                long totalBlocks = stat.getBlockCountLong();

                long availableSpace = availableBlocks * blockSize;
                long totalSpace = totalBlocks * blockSize;

                info = "SD 卡掛載成功\n" +
                        "可用空間: " + formatSize(availableSpace) + "\n" +
                        "總空間: " + formatSize(totalSpace);
            } else {
                info = "SD 卡未插入或無法掛載";
            }

            // 顯示訊息在 AlertDialog 中
            new AlertDialog.Builder(getContext())
                    .setTitle("SD 卡資訊")
                    .setMessage(info)
                    .setPositiveButton("確定", null)
                    .show();
        }, 150); // 延遲 150 毫秒
    }

    private String formatSize(long size) {
        if (size >= 1024 * 1024 * 1024) {
            return String.format("%.2f GB", (float) size / (1024 * 1024 * 1024));
        } else if (size >= 1024 * 1024) {
            return String.format("%.2f MB", (float) size / (1024 * 1024));
        } else if (size >= 1024) {
            return String.format("%.2f KB", (float) size / 1024);
        } else {
            return String.format("%d B", size);
        }
    }

    //CPU_info
    private void readCPUInfo() {

        // 在主執行緒中更換按鈕圖示
        requireActivity().runOnUiThread(() -> {
            binding.buttonReadCPU.setBackgroundResource(R.drawable.icon_cpu_info_colorful);
        });

        // 延遲 150 毫秒後恢復原圖，再繼續執行事件
        binding.buttonReadCPU.postDelayed(() -> {
            // 恢復原圖示
            requireActivity().runOnUiThread(() -> {
                binding.buttonReadCPU.setBackgroundResource(R.drawable.icon_cpu_info);
            });
            String cpuInfo = getCpuInfo();

            // 建立 AlertDialog 來顯示 CPU 資訊
            new AlertDialog.Builder(requireContext())
                    .setTitle("CPU 資訊")
                    .setMessage(cpuInfo)
                    .setPositiveButton("確定", (dialog, which) -> dialog.dismiss())
                    .show();
        }, 150); // 延遲 150 毫秒
    }

    private String getCpuInfo() {

        StringBuilder cpuInfo = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/cpuinfo"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                cpuInfo.append(line).append("\n");
            }
        } catch (IOException e) {
            cpuInfo.append("無法讀取 CPU 資訊");
        }
        return cpuInfo.toString();
    }


    public void testCpuPerformance() {
        requireActivity().runOnUiThread(() -> {
            binding.buttonTestCPUPerformance.setBackgroundResource(R.drawable.icon_cpu_colorful);
        });

        binding.buttonTestCPUPerformance.postDelayed(() -> {
            requireActivity().runOnUiThread(() -> {
                binding.buttonTestCPUPerformance.setBackgroundResource(R.drawable.icon_cpu_performance_test);
            });

            // 開始測量CPU性能
            long startTime = System.nanoTime();
            int size = 1000000;
            int[] numbers = new int[size];

            // 填充数组並進行排序
            for (int i = 0; i < size; i++) {
                numbers[i] = (int) (Math.random() * 1000000);
            }

            Arrays.sort(numbers);

            // 測量結束時間
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;

            // 判斷CPU性能等級
            String performanceRating;
            if (duration < 100) {
                performanceRating = "優秀";
            } else if (duration < 200) {
                performanceRating = "良好";
            } else if (duration < 500) {
                performanceRating = "中等";
            } else if (duration < 1000) {
                performanceRating = "普通";
            } else {
                performanceRating = "較差";
            }

            // 記錄CPU性能測試結果
            Log.d("CPU Performance", "Test duration: " + duration + " ms");
            Log.d("CPU Performance", "Performance rating: " + performanceRating);

            // 顯示性能測試結果
            new AlertDialog.Builder(requireContext())
                    .setTitle("CPU 性能測試結果")
                    .setMessage("運行時間: " + duration + " ms\n" + "性能等級: " + performanceRating)
                    .setPositiveButton("確定", (dialog, which) -> dialog.dismiss())
                    .show();

            // 調整音量
            AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                // 最大音量
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                // 設置目標音量比例，例如 40% 音量
                int targetVolume = (int) (maxVolume * 0.8);

                // 設置音量
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, targetVolume, 0);
            }

            // 播放音檔
            MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.voice_cpu_performance_test);
            mediaPlayer.start();

            // 釋放資源
            mediaPlayer.setOnCompletionListener(mp -> {
                mediaPlayer.release();
            });

        }, 150); // 延遲 150 毫秒
    }




    private void readBatteryInfo() {
        // 確保在主執行緒中更換按鈕圖片
        requireActivity().runOnUiThread(() -> {
            // 更換按鈕圖片為 icon_battery_colorful
            binding.buttonReadBattery.setBackgroundResource(R.drawable.icon_battery_colorful);
        });

        // 延遲 150 毫秒後恢復圖片並執行性能測試
        binding.buttonReadBattery.postDelayed(() -> {
            // 確保在主執行緒中恢復圖片
            requireActivity().runOnUiThread(() -> {
                binding.buttonReadBattery.setBackgroundResource(R.drawable.icon_battery);
            });

            BatteryManager batteryManager = (BatteryManager) requireContext().getSystemService(Context.BATTERY_SERVICE);

            int batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            boolean isCharging = batteryManager.isCharging();

            // 使用 Intent 獲取電池健康狀態
            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = requireContext().registerReceiver(null, filter);
            int health = BatteryManager.BATTERY_HEALTH_UNKNOWN;
            int voltage = 0;
            int temperature = 0;
            if (batteryStatus != null) {
                health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN);
                voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1); // 電壓以毫伏(mV)為單位
                temperature = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1); // 溫度以攝氏度的0.1倍為單位
            }

            float voltageInVolts = voltage / 1000.0f; // 將毫伏轉換為伏特

            String BatteryHealthy = "";
            switch (health) {
                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                    BatteryHealthy = "未知";
                    break;
                case BatteryManager.BATTERY_HEALTH_GOOD:
                    BatteryHealthy = "健康";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    BatteryHealthy = "過熱";
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD:
                    BatteryHealthy = "電池損壞";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    BatteryHealthy = "過電壓";
                    break;
                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    BatteryHealthy = "未指定的故障";
                    break;
                case BatteryManager.BATTERY_HEALTH_COLD:
                    BatteryHealthy = "電池過冷";
                    break;
                default:
                    BatteryHealthy = "未知狀態";
                    break;
            }

            String batteryInfo = "電池電量: " + batteryLevel + "%\n" +
                    "充電狀態: " + (isCharging ? "充電中" : "未充電") + "\n" +
                    "電壓: " + String.format("%.3f", voltageInVolts) + " V\n" +
                    "溫度: " + (temperature / 10.0) + " °C\n" +
                    "電池健康狀態: " + BatteryHealthy;

            // 使用 AlertDialog 彈出視窗顯示電池資訊
            new AlertDialog.Builder(requireContext())
                    .setTitle("電池資訊")
                    .setMessage(batteryInfo)
                    .setPositiveButton("確定", (dialog, which) -> dialog.dismiss())
                    .show();
        }, 150); // 延遲 150 毫秒

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onbtnPingClicked(View v) {
        // 主執行緒更新按鈕圖示
        requireActivity().runOnUiThread(() -> {
            binding.buttonEthernetSpeed.setBackgroundResource(R.drawable.icon_network_speed_colorful);
        });

        // 延遲後執行檢測
        binding.buttonEthernetSpeed.postDelayed(() -> {
            // 恢復按鈕圖示
            requireActivity().runOnUiThread(() -> {
                binding.buttonEthernetSpeed.setBackgroundResource(R.drawable.icon_network_speed);
            });

            // 檢測網路連線狀態
            if (isConnected()) {
                testNetworkSpeed(); // 如果連線，進行網速測試
            } else {
                showAlertDialog("網路狀態", "斷線中");
            }
        }, 150); // 延遲 150 毫秒
    }

    private boolean isConnected() {
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager != null ? connManager.getActiveNetworkInfo() : null;

        if (info != null && info.isConnected() && info.isAvailable()) {
            Log.d(TAG, "網路已連線且可用");
            return true;
        } else {
            Log.d(TAG, "網路不可用或未連線");
            return false;
        }
    }

    private void testNetworkSpeed() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    Process process = new ProcessBuilder()
                            .command("/system/bin/ping", "-c", "4", "-s", "64", "8.8.8.8")
                            .redirectErrorStream(true)
                            .start();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    StringBuilder parsedOutput = new StringBuilder();
                    String line;

                    int sentPackets = 0, receivedPackets = 0, rttCount = 0;
                    double totalRtt = 0;

                    while ((line = reader.readLine()) != null) {
                        if (line.contains("bytes from")) {
                            String time = line.split("time=")[1].split(" ")[0];
                            totalRtt += Double.parseDouble(time);
                            rttCount++;
                        }

                        if (line.contains("packets transmitted")) {
                            String[] summary = line.split(",");
                            sentPackets = Integer.parseInt(summary[0].split(" ")[0]);
                            receivedPackets = Integer.parseInt(summary[1].trim().split(" ")[0]);
                        }
                    }

                    int lostPackets = sentPackets - receivedPackets;
                    double lossRate = ((double) lostPackets / sentPackets) * 100;
                    double avgRtt = (rttCount > 0) ? (totalRtt / rttCount) : 0;

                    String quality = evaluateConnectionQuality(avgRtt);
                    parsedOutput.append("傳送封包數: ").append(sentPackets).append("\n")
                            .append("接收封包數: ").append(receivedPackets).append("\n")
                            .append("丟包數量: ").append(lostPackets).append("\n")
                            .append("丟包率: ").append(String.format("%.2f", lossRate)).append("%\n")
                            .append("平均延遲時間: ").append(String.format("%.2f", avgRtt)).append(" ms\n")
                            .append(quality).append("\n");

                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        return parsedOutput.toString();
                    } else {
                        return "Ping 測試失敗";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Ping 測試失敗: " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                showAlertDialog("網路速度測試結果", result);
            }
        }.execute();
    }

    private String evaluateConnectionQuality(double avgRtt) {
        if (avgRtt < 20) {
            return "連線品質: 優秀";
        } else if (avgRtt < 50) {
            return "連線品質: 良好";
        } else if (avgRtt < 100) {
            return "連線品質: 可接受";
        } else if (avgRtt < 200) {
            return "連線品質: 不佳";
        } else {
            return "連線品質: 極差";
        }
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("確定", null)
                .show();
    }



}
