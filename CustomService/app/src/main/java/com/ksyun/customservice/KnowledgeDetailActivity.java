package com.ksyun.customservice;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksyun.customservice.custom.CustomRelativeLayout;
import com.ksyun.customservice.jiekou.MyInterface.FinishActivityListener;
import com.ksyun.customservice.jiekou.NetRequest;

import java.util.List;

/**
 * Created by Administrator on 2015/11/17.
 */
public class  KnowledgeDetailActivity extends BaseActivity implements View.OnClickListener,FinishActivityListener {

    private ImageView back;
    private List<String[]> mContentList;
    private String question, answer;
    private CustomRelativeLayout parent_layout;
    @Override
    public void onFinishActivity() {
        finish();
    }

    private NetRequest netRequest;
    private TextView questionText, answerText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.knowledge_detail_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        question = getIntent().getStringExtra("question");
        answer = getIntent().getStringExtra("answer");
        Log.d("gaolei","question---------------------"+question);
        Log.d("gaolei","answer---------------------"+answer);
        initViews();
    }

    private void initViews() {
        parent_layout = (CustomRelativeLayout) findViewById(R.id.parent_layout);
        parent_layout.setFinishActivityListener(this);
        questionText = (TextView) findViewById(R.id.question);
        answerText = (TextView) findViewById(R.id.answer);
        questionText.setText(question);
        answerText.setText(answer);
    }
    public void skipToMessageActivity(View view){
        Intent intent = new Intent(this, CSOnlineActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
