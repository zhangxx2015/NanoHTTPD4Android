package org.fooler.zhangxx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    public static WebView wv;
    private static Context That;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {// Check if the key event was the Back button and if there's history
            wv.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private void toast(String text){
        Toast.makeText(That,text,Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        That=this;
//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {

                wv=findViewById(R.id.wv);
                wv.setWebChromeClient(new WebChromeClient(){
                    @Override
                    public boolean onJsAlert(WebView v, String u, String m, final JsResult r) {
                        return super.onJsAlert(v,u,m,r);
                    }
                    @Override
                    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                        return super.onJsPrompt(view, url, message, defaultValue, result);
                    }
                    @Override
                    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                        return super.onJsConfirm(view, url, message, result);
                    }
                });
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                        if (Uri.parse(url).getHost().endsWith("jianshu.com")) {
                            view.loadUrl(url);//若是指定服务器的链接则在当前webView中跳转
                            return false;
//                        } else if (Uri.parse(url).getHost().length() == 0) {
//                            // 本地链接的话直接在webView中跳转
//                            return false;
//                        }
//                        // 其他情况则使用系统浏览器打开网址
//                        Uri uri = Uri.parse(url);
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        startActivity(intent);
//                        return true;
                    }
                });
                wv.getSettings().setJavaScriptEnabled(true);
                //wv.addJavascriptInterface(new WebAppInterface(this), "Android");


                int port=8080;
                try {
                    File root= new File( That.getExternalFilesDir("www").toString());
                    Httpd myHttpServer = new Httpd(port,root);
                    myHttpServer.start(4000, false);
                } catch (Exception ex) {
                    toast(ex.getMessage());
                }
                String url=String.format("http://%s:%d/",Httpd.LocalIpAddress(),port);
                wv.loadUrl(url);
//            }
//        });
    }


}
