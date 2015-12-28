package com.ksyun.customservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ksyun.customservice.jiekou.MyInterface.NetRequestIterface;
import com.ksyun.customservice.jiekou.NetRequest;
import com.ksyun.customservice.object.UserInfoUtil;
import com.ksyun.customservice.utils.CommonUrl;
import com.ksyun.customservice.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/9.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, NetRequestIterface {

    private EditText username_edit, password_edit;
    private Button login_bt;
    private NetRequest netRequest;
    public static SharedPreferences.Editor editor;
    private SharedPreferences account;
    private String username="", password="", inputUserName, inputPassWord;
    private ImageView splash;
    private final int USERNAME_PASSWORD_ERROR = 0;
    private final int SKIP_TO_MAIN = 1;
    private final int SKIP_SPLASH_SCREEN = 2;
    private final int LOGIN_EXCEPTION = 3;
    private boolean autoLogin = true,isExit = false;
    private RelativeLayout login_progress_layout;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case USERNAME_PASSWORD_ERROR:
                    Log.d("gaolei", "case USERNAME_PASSWORD_ERROR------------------");
//                    splash.setVisibility(View.GONE);
                    CommonUtils.getUtilInstance().showLongToast(LoginActivity.this, getResources()
                            .getString(R.string.username_password_error));
                    break;
                case SKIP_SPLASH_SCREEN:
                    Log.d("gaolei", "case SKIP_SPLASH_SCREEN------------------");
                    splash.setVisibility(View.GONE);

                    break;
                case SKIP_TO_MAIN:
//                    login_progress_layout.setVisibility(View.GONE);
                    Log.d("gaolei", "case SKIP_TO_MAIN------------------");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    break;
                case LOGIN_EXCEPTION:
                    splash.setVisibility(View.GONE);
                    CommonUtils.getUtilInstance().showLongToast(LoginActivity.this, getResources()
                            .getString(R.string.login_exception));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        isExit = getIntent().getBooleanExtra("exit_login", false);
        initViews();
        if (isExit) {
            username_edit.setText("");
            password_edit.setText("");
            editor.clear();
            editor.commit();
        }
    }

    private void initViews() {
        username_edit = (EditText) findViewById(R.id.username_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);
        login_bt = (Button) findViewById(R.id.login_bt);
        splash = (ImageView) findViewById(R.id.splash);
        login_progress_layout = (RelativeLayout) findViewById(R.id.login_progress_layout);
        login_bt.setOnClickListener(this);
        netRequest = new NetRequest(this, this);
        if (!isExit) {
            account = getSharedPreferences("account", 0);
            editor = account.edit();
            getAccountSharedPreferences();
        } else {
            splash.setVisibility(View.GONE);
        }
    }

    private void getAccountSharedPreferences() {
        username = account.getString("username", "");
        password = account.getString("password", "");
        Log.d("gaolei", "username------------------" + username);
        Log.d("gaolei", "password------------------" + password);
        if (username.length() > 0 && password.length() > 0) {
            loginAccount(username, password);
        } else {
            handler.sendEmptyMessageDelayed(SKIP_SPLASH_SCREEN, 2000);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bt:
                Log.d("gaolei", "login_bt-------------");
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("from", 0);
//                map.put("to", 10);
//                netRequest.httpRequest(map, CommonUrl.getThemeList);
//                new Thread() {
//                    public void run() {
                inputUserName = username_edit.getText().toString();
                inputPassWord = password_edit.getText().toString();
                loginAccount(inputUserName, inputPassWord);
//                login_progress_layout.setVisibility(View.VISIBLE);

//                        try {
//                            String result = NetTool.httpURLConnectionPost(CommonUrl.loginUrl, map, "utf-8");
//                            Log.d("gaolei", "result-------login------" + result);
//                        } catch (Exception e) {
//                            Log.d("gaolei", " e.getMessage()-------login------" + e.getMessage());
//                        }
//                }.start();
                break;
        }
    }

    private void loginAccount(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", username);
        map.put("password", password);
        netRequest.httpRequest(map, CommonUrl.loginUrl);
    }

    @Override
    public void changeView(String result, String requestUrl) {
        if (requestUrl.equals(CommonUrl.loginUrl)) {
            Log.d("gaolei", "result-------login------" + result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.isNull("error_code")) {
                    JSONObject object=new JSONObject(result);
                    UserInfoUtil userInfoUtil=UserInfoUtil.getInstance();
                    userInfoUtil.setId(object.getString("id"));
                    userInfoUtil.setEmail(object.getString("email"));
                    userInfoUtil.setMobile(object.getString("mobile"));
                    userInfoUtil.setName(object.getString("name"));
                    userInfoUtil.setCompany_name(object.getString("company_name"));

                    if(username.length()==0&&password.length()==0) {
                        editor.putString("username", inputUserName);
                        editor.putString("password", inputPassWord);
                        editor.commit();
                    }
                    handler.sendEmptyMessage(SKIP_TO_MAIN);
                    Log.d("gaolei", "SKIP_TO_MAIN------------------");
                } else {
                    handler.sendEmptyMessage(USERNAME_PASSWORD_ERROR);
                    Log.d("gaolei", "SKIP_SPLASH_SCREEN------------------");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {
        if (requestUrl.equals(CommonUrl.loginUrl)) {
            Log.d("gaolei", "e.getMessage()-------------" + e.getMessage());
            handler.sendEmptyMessage(LOGIN_EXCEPTION);
        }
    }
}
