package com.ksyun.customservice;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ksyun.customservice.adapter.FeedBackViewPagerAdapter;
import com.ksyun.customservice.custom.CustomRelativeLayout;
import com.ksyun.customservice.custom.viewpagerindicator.TabPageIndicator;
import com.ksyun.customservice.custom.viewpagerindicator.UnderlinePageIndicatorEx;
import com.ksyun.customservice.email.MailSender;
import com.ksyun.customservice.jiekou.MyInterface.FinishActivityListener;
import com.ksyun.customservice.jiekou.NetRequest;
import com.ksyun.customservice.object.UserInfoUtil;
import com.ksyun.customservice.utils.CommonUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/11/17.
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener, FinishActivityListener {
    private FeedBackViewPagerAdapter mContentAdapter;
    private ViewPager mPager;
    private TabPageIndicator mTabPageIndicator;
    private UnderlinePageIndicatorEx mUnderlinePageIndicator;
    private ImageView back;
    private int COUNT = 0;
    private List<String[]> mContentList;
    private String title, problemKind, submitProblemInfo;
    private int typeposition;
    private NetRequest netRequest;
    private Button submit_problem;
    private EditText problem_desc_edit, company_name_edit, user_name_edit, user_tel_edit, user_email_edit;
    private CustomRelativeLayout parent_layout;

    @Override
    public void onFinishActivity() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        title = getIntent().getStringExtra("title");
        problemKind = getIntent().getStringExtra("problem_kind");
        typeposition = getIntent().getIntExtra("typeposition",0);
        initViews();

    }

    private void initViews() {
        parent_layout = (CustomRelativeLayout) findViewById(R.id.parent_layout);
        parent_layout.setFinishActivityListener(this);
        submit_problem = (Button) findViewById(R.id.submit_problem);
        submit_problem.setOnClickListener(this);
        problem_desc_edit = (EditText) findViewById(R.id.problem_desc_edit);
        company_name_edit = (EditText) findViewById(R.id.company_name_edit);
        user_name_edit = (EditText) findViewById(R.id.user_name_edit);
        user_tel_edit = (EditText) findViewById(R.id.user_tel_edit);
        user_email_edit = (EditText) findViewById(R.id.user_email_edit);
        UserInfoUtil userInfoUtil = UserInfoUtil.getInstance();
        Log.d("gaolei", "userInfoUtil.getCompany_name()-----" + userInfoUtil.getCompany_name());
        company_name_edit.setText(userInfoUtil.getCompany_name());
        user_name_edit.setText(userInfoUtil.getName());
        user_tel_edit.setText(userInfoUtil.getMobile());
        user_email_edit.setText(userInfoUtil.getEmail());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_problem:
                StringBuilder builder = new StringBuilder();
                String desc = problem_desc_edit.getText().toString();
                String company_name=company_name_edit.getText().toString();
                String user_name=user_name_edit.getText().toString();
                String user_tel=user_tel_edit.getText().toString();
                String user_email=user_email_edit.getText().toString();
                builder.append(getResources().getString(R.string.problem_kind_feedback) + title + "\n");
                Log.d("gaolei","typeposition-------FeedBackActivity----------"+typeposition);
                if(typeposition==0||typeposition==2||typeposition==3) {
                    builder.append(getResources().getString(R.string.consult_type) + problemKind + "\n");
                } if(typeposition==1) {
                    builder.append(getResources().getString(R.string.product_type) + problemKind + "\n");
                }
                builder.append(getResources().getString(R.string.problem_desc) + desc + "\n");
                builder.append(getResources().getString(R.string.company_name) + company_name + "\n");
                builder.append(getResources().getString(R.string.customer_name) + user_name + "\n");
                builder.append(getResources().getString(R.string.tel) + user_tel + "\n");
                builder.append(getResources().getString(R.string.email) + user_email + "\n");

                //3058181964@qq.com     ksyuncs
                SenderRunnable senderRunnable = new SenderRunnable(
                        "1450289937@qq.com", "fyxvinfyaajrhcbi");
                senderRunnable.setMail(getResources().getString(R.string.email_title),
                        builder.toString(), "ksyun_cs@KINGSOFT.COM", null);
                if (desc.length() == 0) {
                    CommonUtils.getUtilInstance().showLongToast(FeedBackActivity.this, getResources()
                            .getString(R.string.feedback_null));
                    return;
                }
                if (desc.length() < 10) {
                    CommonUtils.getUtilInstance().showLongToast(FeedBackActivity.this, getResources()
                            .getString(R.string.feedback_short));
                    return;
                }
                new Thread(senderRunnable).start();

                CommonUtils.getUtilInstance().showLongToast(FeedBackActivity.this, getResources()
                        .getString(R.string.feedback_success));
                break;
        }
    }

    public void editName(View view) {
        Log.d("gaolei", "editName-------------------");
        user_name_edit.requestFocus();
        user_name_edit.setFocusable(true);
    }

    public void editMobile(View view) {
        Log.d("gaolei", "editMobile-------------------");
        user_tel_edit.requestFocus();
    }

    public void editEmail(View view) {
        Log.d("gaolei", "editEmail-------------------");
        user_email_edit.requestFocus();
    }

    class SenderRunnable implements Runnable {

        private String user;
        private String password;
        private String subject;
        private String body;
        private String receiver;
        private MailSender sender;
        private String attachment;

        public SenderRunnable(String user, String password) {
            this.user = user;
            this.password = password;
            sender = new MailSender(user, password);
            String mailhost = user.substring(user.lastIndexOf("@") + 1, user.lastIndexOf("."));
            if (!mailhost.equals("gmail")) {
                mailhost = "smtp." + mailhost + ".com";
                Log.i("hello", mailhost);
                sender.setMailhost(mailhost);
            }
        }

        public void setMail(String subject, String body, String receiver, String attachment) {
            this.subject = subject;
            this.body = body;
            this.receiver = receiver;
            this.attachment = attachment;
        }

        public void run() {
            // TODO Auto-generated method stub
            try {
                sender.sendMail(subject, body, user, receiver, attachment);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
