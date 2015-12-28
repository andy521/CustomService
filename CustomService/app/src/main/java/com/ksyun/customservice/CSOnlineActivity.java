package com.ksyun.customservice;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ksyun.customservice.custom.CustomRelativeLayout;
import com.ksyun.customservice.jiekou.MyInterface.FinishActivityListener;
import com.ksyun.customservice.utils.CommonUrl;


/**
 * Created by Administrator on 2015/11/17.
 */
public class CSOnlineActivity extends AppCompatActivity implements FinishActivityListener{


    private final static int FILECHOOSER_RESULTCODE = 1;
    private WebView csonline_webview;
    private ValueCallback<Uri> mUploadMessage;
    private boolean pause = false;
private CustomRelativeLayout parent_layout;
    @Override
    public void onFinishActivity() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.csonline_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ;
        }
        csonline_webview = (WebView) findViewById(R.id.csonline_webview);
        parent_layout = (CustomRelativeLayout) findViewById(R.id.parent_layout);
        parent_layout.setFinishActivityListener(this);
        //启用支持javascript
        WebSettings settings = csonline_webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        csonline_webview.setWebChromeClient(new MyWebClient());


//        try{
//
//            Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE) ;
//
//            m.invoke(csonline_webview, true) ;
//
//        }catch(Throwable ignored)
//
//        {
//
//        }
        // 优先使用缓存
        // csonline_webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //不使用缓存
        //csonline_webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //WebView加载web资源
        csonline_webview.loadUrl(CommonUrl.csUrl);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        csonline_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        Log.d("gaolei","CSOnlineActivity--------------onCreate-----------");
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (csonline_webview.canGoBack()) {
                csonline_webview.goBack();//返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            Log.d("gaolei", "intent.getData()-----------" + intent.getData());
            mUploadMessage.onReceiveValue(result);
            //当你选择上传文件的时候，webview的ValueCallback对象（就是选择图片的回调）会持有这个webview，在没有收到回调之前，你无法对这个webview做任何的操作！
//            mUploadMessage = null;
        }
    }
    public void onStart() {
        super.onStart();
        Log.d("gaolei", "CSOnlineActivity--------------onStart-----------");
    }
    public void onRestart() {
        super.onRestart();
        Log.d("gaolei", "CSOnlineActivity--------------onRestart-----------");
    }
    public void onResume() {
        super.onResume();
        Log.d("gaolei", "CSOnlineActivity--------------onResume-----------");
//        if (csonline_webview != null && pause) {
//            csonline_webview.resumeTimers();
//            csonline_webview.onResume();
//            this.pause = false;
//        }
    }
    public void onPause() {
        super.onPause();
        Log.d("gaolei", "CSOnlineActivity--------------onPause-----------");
//        if (csonline_webview != null) {
//            csonline_webview.pauseTimers();
//            csonline_webview.onPause();
//            this.pause = true;
//            mUploadMessage = null;
//        }
    }


    public void onStop() {
        super.onStop();
        Log.d("gaolei", "CSOnlineActivity--------------onStop-----------");
    }
    public void onDestroy() {
        super.onDestroy();
        Log.d("gaolei", "CSOnlineActivity--------------onDestroy-----------");
        csonline_webview.destroy();
        csonline_webview=null;
    }


    public void finishActivity(View v) {
        finish();
    }


    public class MyWebClient extends WebChromeClient {
        //根据不同版本设置不同category
        // For Android 3.0-
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }


        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
        }


        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }
    }


}

