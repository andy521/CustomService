package com.ksyun.customservice;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ksyun.customservice.adapter.WXMessageAdapter;
import com.ksyun.customservice.custom.CustomRelativeLayout;
import com.ksyun.customservice.custom.MyWebView;
import com.ksyun.customservice.custom.MyWebView.OnScrollChangedCallback;
import com.ksyun.customservice.jiekou.MyInterface.FinishActivityListener;
import com.ksyun.customservice.jiekou.MyInterface.NetRequestIterface;
import com.ksyun.customservice.jiekou.NetRequest;
import com.ksyun.customservice.object.WXMessageObject;
import com.ksyun.customservice.utils.CommonUrl;
import com.ksyun.customservice.utils.NetTool;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/11/17.
 */
public class MessageActivity extends BaseActivity implements
        OnScrollChangedCallback, NetRequestIterface, FinishActivityListener {

    private MyWebView message_webview;
    private CustomRelativeLayout parent_layout;
    private RelativeLayout loading_layout;

    @Override
    public void onFinishActivity() {
        finish();
        ;
    }

    private NetRequest netRequest;
    private List<WXMessageObject> messageList;
    private ListView wxmessage_listview;
    private boolean isInWebView = false;
    private TextView title;
    private final int SHOW_WEBVIEW = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SHOW_WEBVIEW:
                    message_webview.setVisibility(View.VISIBLE);
                    title.setText(getString(R.string.message_detail));
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        netRequest = new NetRequest(this, this);

        message_webview = (MyWebView) findViewById(R.id.message_webview);
        wxmessage_listview = (ListView) findViewById(R.id.wxmessage_listview);
        loading_layout = (RelativeLayout) findViewById(R.id.loading_layout);
        title = (TextView) findViewById(R.id.title);
        parent_layout = (CustomRelativeLayout) findViewById(R.id.parent_layout);
        parent_layout.setFinishActivityListener(this);
        message_webview.setOnScrollChangedCallback(this);

        WebSettings settings = message_webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setUseWideViewPort(true);// 这个很关键
//        settings.setLoadWithOverviewMode(true);
        settings.setAllowFileAccess(true);
        settings.setBlockNetworkImage(false);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        message_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub
                handler.proceed();
            }
        });
        new Thread() {
            public void run() {
                try {
                    String getAccessToken = NetTool.doGet(CommonUrl.getAccessToken);
                    JSONObject object = new JSONObject(getAccessToken);
                    String token = object.getString("access_token");
                    Log.d("gaolei", "token-------------" + token);

                    MediaType MEDIA_TYPE_MARKDOWN
                            = MediaType.parse("text/x-markdown; charset=utf-8");

                    String postBody = "{\n" +
                            "    \"type\":\"news\",\n" +
                            "    \"offset\":0,\n" +
                            "    \"count\":28\n" +
                            "}";
                    OkHttpClient mOkHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(CommonUrl.getMaterial + token)
                            .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                            .build();
                    mOkHttpClient.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
                    Response response = mOkHttpClient.newCall(request).execute();
                    String responseresult = response.body().string();
                    Log.d("gaolei", "responseresult--------------MessageActivity------" + responseresult);
                    JSONObject gamesInfoObject = new JSONObject(responseresult);

                    messageList = new Gson().fromJson(
                            gamesInfoObject.getString("item"),
                            new TypeToken<List<WXMessageObject>>() {
                            }.getType());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading_layout.setVisibility(View.GONE);
                            wxmessage_listview.setAdapter(new WXMessageAdapter(messageList, MessageActivity.this));
                        }
                    });

                } catch (Exception e) {
                    Log.d("gaolei", "e.getMessage()--------------MessageActivity------" + e.getMessage());
                }
            }
        }.start();
        wxmessage_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                message_webview.loadUrl(messageList.get(position).getContent().news_item.get(0).getUrl());
                handler.sendEmptyMessageDelayed(SHOW_WEBVIEW, 1000);

                isInWebView = true;
            }
        });
    }

    public void changeView(String result, String requestUrl) {
        if (requestUrl.equals(CommonUrl.getMaterial)) {
            Log.d("gaolei", "result-------getMaterial------" + result);
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {

    }

    private void backFromWebView() {
        message_webview.setVisibility(View.GONE);
        isInWebView = false;
        title.setText(getString(R.string.message));
    }

    @Override
    public void onScroll(int dy) {
//        Log.d("gaolei", "dy-------------------"
//                + dy);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isInWebView) {
                backFromWebView();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void finishActivity(View v) {
        if (isInWebView) {
            backFromWebView();
            return;
        }
        finish();
    }
}
