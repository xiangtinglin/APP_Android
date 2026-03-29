# 手機醫生 / Phone Doctor (Android App)

## 📌 專案簡介

**手機醫生（Phone Doctor）** 是一個以 **Android + Java** 開發的手機硬體與性能檢測 App，目標是讓使用者能在單一介面中，快速完成多項手機健康檢測，了解裝置目前的運行狀態，並透過直觀、簡潔且具有趣味性的互動設計，提升檢測意願與使用頻率。

本專案結合了 **作業系統資源管理概念**、**硬體檢測 API**、**事件驅動互動設計** 與 **UI/UX 理論**，屬於「作業系統」課程的期末 APP 實作專題。

---

## 🎯 專案目標

本專案以「**預防勝於治療**」為核心概念，期待達成以下目標：

- 即時檢測手機核心硬體與系統狀態
- 幫助使用者提早發現潛在異常，降低維修成本
- 提升使用者對手機健康狀態的理解
- 結合簡約設計與「寵物養成」元素，增加互動性與使用樂趣

---

## 🛠️ Tech Stack

### Development

- **Language:** Java
- **Platform:** Android
- **IDE:** Android Studio

### Core Concepts

- Event-Driven Architecture
- Human-Computer Interaction (HCI)
- Computer Graphics
- Semiotics of Form
- Gestalt Principles

---

## 📱 主要功能

本 App 共整合 **11 項手機檢測功能**：

1. **手機資訊檢測**
   - 顯示品牌、型號、Android 版本等基本裝置資訊

2. **電池資訊檢測**
   - 顯示電量、健康狀態、溫度、充電狀態等資訊

3. **SD 卡檢測**
   - 檢查外部儲存裝置是否掛載、容量與可用空間

4. **SIM 卡檢測**
   - 顯示 SIM 卡狀態、電信商資訊

5. **GPS 測試**
   - 檢查 GPS 是否啟用、取得定位資訊與地址解析

6. **CPU 資訊檢測**
   - 顯示 CPU 型號、核心數與處理器相關資訊

7. **CPU 效能測試**
   - 以排序運算模擬高負載情境，評估 CPU 效能與穩定性

8. **網路通訊測試**
   - 測量網路狀態、延遲、丟包與連線品質

9. **螢幕測試**
   - 檢查螢幕顯示與滑動觸控是否正常

10. **麥克風測試**
    - 即時錄音並以波形可視化音量變化

11. **震動測試**
    - 測試手機震動馬達是否正常運作

---

## 🧱 系統流程

```text
[ User ]
   │
   ▼
[ 開場動畫 ]
   │
   ▼
[ 主畫面 / 檢測功能總覽 ]
   │
   ▼
[ 選擇檢測項目 ]
   ├─ 手機資訊
   ├─ 電池資訊
   ├─ SD 卡檢測
   ├─ SIM 卡檢測
   ├─ GPS 測試
   ├─ CPU 資訊
   ├─ CPU 效能測試
   ├─ 網路通訊測試
   ├─ 螢幕測試
   ├─ 麥克風測試
   └─ 震動測試
   │
   ▼
[ 記錄已完成檢測項目 / 顯示進度 ]
   │
   ├─ 繼續測試 → 返回主畫面
   └─ 結束測試 → 完成流程
```

---

## 🖼️ UI / UX 設計特色

### 設計理念

- **Less is more. Simple is the best.**
- 採用簡約、乾淨、低負擔的介面風格
- 不使用預設模板，改以客製化 UI 規劃實作
- 將 **Figma** 作為前期介面與互動流程設計工具

### 互動亮點

- 加入 **「寵物養成」** 的進度表現形式
- 寵物會依照檢測進度改變狀態，增加使用樂趣
- 結合 **GUI + VUI** 的多模態互動設計
- 按鈕點擊後搭配 **圖示變化 + 語音回饋**，提高即時反饋感

### 版面規劃

- **Fixed 主控區**
  - Toolbar
  - Home
  - Menu / Other Info

- **Scroll 內容區**
  - 檢測進度與動畫
  - 檢測功能區
  - 廣告區
  - 開發者資訊區

---

## 🧠 UI/UX 學理基礎

本專案介面設計結合以下概念：

- **HCI（Human-Computer Interaction）**
  - Fitts’s Law
  - Von Restorff Effect
  - Redundancy Effect

- **Computer Graphics**
  - 按鈕縮放 / 漸變動畫
  - 半透明 / 霧面玻璃視覺效果

- **Semiotics of Form**
  - 以文化上直觀的圖示傳達功能，例如房屋圖示代表首頁

- **Gestalt Principles**
  - 接近性、相似性、一致性的視覺編排

---

## 🔌 使用到的 Android API

### 1. Battery / Power
- `BatteryManager`
- `Intent.ACTION_BATTERY_CHANGED`

### 2. CPU / System Info
- `/proc/cpuinfo`
- `BufferedReader`

### 3. Performance Test
- `System.nanoTime()`
- `Arrays.sort()`

### 4. Network Test
- `ConnectivityManager`
- `ProcessBuilder`
- `AsyncTask`

### 5. Vibration
- `Vibrator`

### 6. Storage
- `Environment`
- `StatFs`

### 7. GPS / Location
- `LocationManager`
- `Geocoder`

### 8. Device Info
- `Build`

### 9. Screen / Touch
- `MotionEvent`
- `Canvas`
- `Paint`

### 10. Microphone
- `AudioRecord`
- custom `EKGView`

### 11. SIM Info
- `TelephonyManager`

---

## 🔐 需要的權限

依功能需求，App 會使用下列 Android 權限：

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

---

## ✨ 技術亮點

- 以 Android 原生 API 直接整合硬體資訊，不需依賴第三方硬體函式庫
- 結合 **系統監控 + 使用者互動設計**
- 多數功能可即時視覺化顯示結果
- 使用事件驅動架構處理按鈕互動與功能切換
- 將檢測結果與進度追蹤結合，提升完整使用體驗
- 麥克風測試支援動態波形顯示
- CPU 效能測試以實際運算模擬高負載情境
- 網路測試採背景執行，避免阻塞主執行緒

---

## 🚀 未來展望

- **異常預警機制**
  - 偵測可能的硬體異常並主動提醒使用者

- **維修資訊整合**
  - 提供維修中心、維修政策、費用估算等資訊

- **使用者教育**
  - 提供裝置保養建議與使用習慣提醒

- **在地化與多語言支援**
  - 若加入更多語言使用者，可延伸語音與內容本地化設計

---

## 📊 與相似 App 的比較方向

專案報告中曾將本 App 與下列類型產品進行比較：

- Phone Doctor Plus
- 設備信息（Device Info）
- AccuBattery

比較維度包含：

- 功能完整度
- 數據呈現方式
- 使用者介面易讀性
- 廣告干擾程度
- 是否提供維修/健康管理視角

---

## 📂 Repository Note

目前 GitHub 倉庫中的 README 提到：

- 專案名稱：`APP_Android`
- 專案與「Operating Systems Final Project」相關
- 使用 Android / Java / Android Studio 開發
- 曾整理 Figma 設計稿、雲端文件與簡報資源
- README 中列出的已實作功能包含：
  - SD 卡
  - 電池資訊
  - SIM 卡資訊
  - CPU 表現測試
  - 螢幕測試
  - 網路測試

---

## 👥 Team

- 湯昀翔
- 廖宜德
- 林湘婷
- 蘇博修

---

## 📘 Course Context

- Course: **Operating Systems**
- Project Type: Final Project / Innovative App Design
- App Theme: **手機醫生（Phone Doctor）**

---

## 📎 References / Source Materials

---

## 🌐 Resources / Demo

- 📦 **Project Package（完整專案）**  
  https://drive.google.com/drive/folders/1TYIGh2rJy3fZPL82bS1jHdFaj0vmPPoF?usp=sharing  

- 🎥 **Demo Video（操作展示）**  
  https://drive.google.com/file/d/1S8bs7YEn3jKwhUw3jo6JPsZjp791hxTD/view?usp=drive_link  

- 📊 **Project Slides（專案簡報）**  
  https://docs.google.com/presentation/d/1oS2mVt0Qes8Ev2nMNKOJo23qGeT9TbSn_rzbvZtV-mA/edit?usp=drive_link  

---


- Final report
- Oral presentation slides
- GitHub README / project notes
