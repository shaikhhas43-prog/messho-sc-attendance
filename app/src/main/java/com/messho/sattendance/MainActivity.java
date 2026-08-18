package com.messho.sattendance;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView webView;
    private static final int LOCATION_REQUEST = 1001;

    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get Android device ID
        deviceId = Settings.Secure.getString(
                getContentResolver(),
                Settings.Secure.ANDROID_ID
        );

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);

        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        // Allow JavaScript to get the device ID
        webView.addJavascriptInterface(
                new DeviceBridge(),
                "AndroidDevice"
        );

        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(
                    String origin,
                    GeolocationPermissions.Callback callback) {

                if (android.os.Build.VERSION.SDK_INT >= 23 &&
                        checkSelfPermission(
                                Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
                            new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                            },
                            LOCATION_REQUEST
                    );
                }

                callback.invoke(origin, true, false);
            }

            @Override
            public void onPermissionRequest(
                    final PermissionRequest request) {

                runOnUiThread(() -> {
                    request.grant(request.getResources());
                });
            }
        });

        webView.loadUrl(
                "file:///android_asset/index.html"
        );
    }

    /*
     * JavaScript Bridge
     *
     * JavaScript can call:
     * AndroidDevice.getDeviceId()
     */
    public class DeviceBridge {

        @JavascriptInterface
        public String getDeviceId() {

            if (deviceId == null) {
                return "";
            }

            return deviceId;
        }
    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {

            webView.goBack();

        } else {

            super.onBackPressed();
        }
    }
}
