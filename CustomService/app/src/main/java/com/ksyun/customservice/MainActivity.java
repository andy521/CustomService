package com.ksyun.customservice;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ksyun.customservice.adapter.BusinessKnowledgeFilterAdapter;
import com.ksyun.customservice.adapter.BusinessKonwledgeAdapter;
import com.ksyun.customservice.adapter.OtherKnowledgeFilterAdapter;
import com.ksyun.customservice.adapter.OtherKonwledgeAdapter;
import com.ksyun.customservice.adapter.RecordKnowledgeFilterAdapter;
import com.ksyun.customservice.adapter.RecordKonwledgeAdapter;
import com.ksyun.customservice.adapter.SearchKonwledgeAdapter;
import com.ksyun.customservice.adapter.TechKnowledgeFilterAdapter;
import com.ksyun.customservice.adapter.TechKonwledgeAdapter;
import com.ksyun.customservice.custom.BounceScrollView;
import com.ksyun.customservice.custom.MyViewPager;
import com.ksyun.customservice.custom.viewpagerindicator.CirclePageIndicator;
import com.ksyun.customservice.jiekou.MyInterface;
import com.ksyun.customservice.jiekou.MyInterface.NotifyActivityIterface;
import com.ksyun.customservice.object.KnowledgeAllObject;
import com.ksyun.customservice.object.KnowledgeAllObject.KnowledgeKindObject;
import com.ksyun.customservice.object.KnowledgeAllObject.KonwledgeObject;
import com.ksyun.customservice.object.UserInfoUtil;
import com.ksyun.customservice.utils.CommonUrl;
import com.ksyun.customservice.utils.CommonUtils;
import com.ksyun.customservice.utils.NetTool;
import com.ksyun.customservice.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener, MyViewPager.OnSingleTouchListener, MyInterface.ScrollViewListener, NotifyActivityIterface, View.OnFocusChangeListener {


    public static final int RECYCLE_SHOW = 1;
    private static MyViewPager game_recommend_viewpager;


    private static int position = 0;
    private CirclePageIndicator viewpager_indicator;
    private MyHandler handler;
    private List<String> techKonwledgeList, businessKonwledgeList, recordKnowledgeList, otherKonwledgeList, problemFilterList;
    private ListView tech_problem_listview, business_problem_listview, record_problem_listview, other_problem_listview;
    private ListView tech_filter_listview, business_filter_listview, record_filter_listview, other_filter_listview;
    private ListView search_result_listview;
    private TextView search_text, user_name,
            problem_tech_suspend, problem_business_suspend, problem_record_suspend, problem_other_suspend;
    private ImageView left_menu_icon, message_icon;
    private BounceScrollView contentScrollView;
    private boolean popupFilter = false;
    private RelativeLayout problem_filter_layout, search_layout;
    public static RelativeLayout shadow_layout;
    private HorizontalScrollView parent_layout;
    private LinearLayout knowledge_kind_layout, knowledge_kind_layout_suspend;
    private LinearLayout menu_message_layout, menu_knowledge_layout, menu_cs_layout, menu_setting_layout, custom_tel_layout, custom_online_layout, problem_feedback_layout;
    private int scrollPX;
    private boolean isFirstPressBack = true, isSuspendVisible = false, isInSearch = false;
    private View above;
    private long lastPressBack;
    private SlidingMenu mMenu;
    private List<KnowledgeAllObject> konwledgeList;
    private Drawable drawable, drawableSelected;
    private boolean isTechLoaded = false, isBusinessLoaded = false, isRecordLoaded = false, isOtherLoaded = false;
    private TechKonwledgeAdapter techKnowledgeAdapter;
    private BusinessKonwledgeAdapter businessKnowledgeAdapter;
    private RecordKonwledgeAdapter recordKnowledgeAdapter;
    private OtherKonwledgeAdapter otherKnowledgeAdapter;
    private SearchKonwledgeAdapter searchAdapter;
    private EditText search_edit;
    private List<KonwledgeObject> questionSearchList = new ArrayList<KonwledgeObject>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Log.d("gaolei", "baseInt----------------------" + baseInt);

        mMenu = (SlidingMenu) findViewById(R.id.parent_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();

            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setBackgroundDrawable(null);
//	            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//	            tintManager.setStatusBarTintColor(R.color.contentTitleColor);
//	            tintManager.setStatusBarTintEnabled(true);
        }
        initViews();
        initJson();
    }

    private void initViews() {

        scrollPX = CommonUtils.getUtilInstance().dp2px(MainActivity.this, 270);
        drawable = getResources().getDrawable(R.mipmap.arrow_down);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        drawableSelected = getResources().getDrawable(R.mipmap.arrow_down_selected);
        drawableSelected.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        handler = new MyHandler();
        game_recommend_viewpager = (MyViewPager) findViewById(R.id.game_recommend_viewpager);
        viewpager_indicator = (CirclePageIndicator) findViewById(R.id.viewpager_indicator);
        contentScrollView = (BounceScrollView) findViewById(R.id.contentScrollView);
        tech_problem_listview = (ListView) findViewById(R.id.tech_problem_listview);
        business_problem_listview = (ListView) findViewById(R.id.business_problem_listview);
        record_problem_listview = (ListView) findViewById(R.id.record_problem_listview);
        other_problem_listview = (ListView) findViewById(R.id.other_problem_listview);
        tech_filter_listview = (ListView) findViewById(R.id.tech_filter_listview);
        business_filter_listview = (ListView) findViewById(R.id.business_filter_listview);
        record_filter_listview = (ListView) findViewById(R.id.record_filter_listview);
        other_filter_listview = (ListView) findViewById(R.id.other_filter_listview);
        search_result_listview = (ListView) findViewById(R.id.search_result_listview);

        search_text = (TextView) findViewById(R.id.search_text);
        user_name = (TextView) findViewById(R.id.user_name);
        user_name.setText(getResources().getString(R.string.dear) + UserInfoUtil.getInstance().getName() + getResources().getString(R.string.hello));
        problem_tech_suspend = (TextView) findViewById(R.id.problem_tech_suspend);
        problem_business_suspend = (TextView) findViewById(R.id.problem_business_suspend);
        problem_record_suspend = (TextView) findViewById(R.id.problem_record_suspend);
        problem_other_suspend = (TextView) findViewById(R.id.problem_other_suspend);
        custom_tel_layout = (LinearLayout) findViewById(R.id.custom_tel_layout);
        custom_online_layout = (LinearLayout) findViewById(R.id.custom_online_layout);
        problem_feedback_layout = (LinearLayout) findViewById(R.id.problem_feedback_layout);
        left_menu_icon = (ImageView) findViewById(R.id.left_menu_icon);
        message_icon = (ImageView) findViewById(R.id.message_icon);
        search_edit = (EditText) findViewById(R.id.search_edit);
        search_edit.setOnFocusChangeListener(this);
        search_edit.setOnClickListener(this);
        search_edit
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            searchProblem();
                            return true;
                        }
                        return false;
                    }
                });
        search_text.setOnClickListener(this);
        problem_filter_layout = (RelativeLayout) findViewById(R.id.problem_filter_layout);
        search_layout = (RelativeLayout) findViewById(R.id.search_layout);
        shadow_layout = (RelativeLayout) findViewById(R.id.shadow_layout);
        shadow_layout.setOnClickListener(this);
        parent_layout = (HorizontalScrollView) findViewById(R.id.parent_layout);
        knowledge_kind_layout = (LinearLayout) findViewById(R.id.knowledge_kind_layout);
        knowledge_kind_layout_suspend = (LinearLayout) findViewById(R.id.knowledge_kind_layout_suspend);
        menu_message_layout = (LinearLayout) findViewById(R.id.menu_message_layout);
        menu_knowledge_layout = (LinearLayout) findViewById(R.id.menu_knowledge_layout);
        menu_cs_layout = (LinearLayout) findViewById(R.id.menu_cs_layout);
        menu_setting_layout = (LinearLayout) findViewById(R.id.menu_setting_layout);
        menu_message_layout.setOnClickListener(this);
        menu_knowledge_layout.setOnClickListener(this);
        menu_cs_layout.setOnClickListener(this);
        menu_setting_layout.setOnClickListener(this);
        problem_tech_suspend.setOnClickListener(this);
        problem_business_suspend.setOnClickListener(this);
        problem_record_suspend.setOnClickListener(this);
        problem_other_suspend.setOnClickListener(this);
        problem_filter_layout.setOnClickListener(this);
        custom_tel_layout.setOnClickListener(this);
        custom_online_layout.setOnClickListener(this);
        problem_feedback_layout.setOnClickListener(this);
        message_icon.setOnClickListener(this);
        game_recommend_viewpager.setOnSingleTouchListener(this);
        contentScrollView.setScrollViewListener(this);
        parent_layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //这一步很重要，使得上面的购买布局和下面的购买布局重合
                onScrollChanged(contentScrollView.getScrollY());
            }
        });
    }

    private void initJson() {
        String tech_problem = NetTool.getJson(this, "knowledge_library.txt");
        try {
//        JSONObject gamesInfoObject = new JSONObject(tech_problem);
            konwledgeList = new Gson().fromJson(tech_problem,
                    new TypeToken<List<KnowledgeAllObject>>() {
                    }.getType());
//            Log.d("gaolei","tech_problem----------------------"+tech_problem);
            Log.d("gaolei", "list.size()----------------------" + konwledgeList.size());
//            Log.d("gaolei","konwledgeList.get(0).getContentList()----------------------"+konwledgeList.get(0).contentList.size());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("gaolei", "e.getMessage()----------------------" + e.getMessage());
        }
        setListViewAdapter(0, tech_filter_listview, tech_problem_listview);

    }

    private void setListViewAdapter(int position, ListView listview1, ListView listview2) {
        if (position == 0) {
             TechKnowledgeFilterAdapter filterAdapter = new TechKnowledgeFilterAdapter(position, konwledgeList.get(position).getKinds(), MainActivity.this);
            tech_filter_listview.setAdapter(filterAdapter);
            techKnowledgeAdapter = new TechKonwledgeAdapter(konwledgeList.get(position).getKinds(), MainActivity.this);
            tech_problem_listview.setAdapter(techKnowledgeAdapter);
            Utils.setListViewHeightBasedOnChildren(listview2);
            filterAdapter.setNotifyActivityIterface(this);
        }
        if (position == 1) {
            BusinessKnowledgeFilterAdapter businessFilterAdapter = new BusinessKnowledgeFilterAdapter(position, konwledgeList.get(position).getKinds(), MainActivity.this);
            listview1.setAdapter(businessFilterAdapter);
            businessKnowledgeAdapter = new BusinessKonwledgeAdapter(konwledgeList.get(position).getKinds(), MainActivity.this);
            listview2.setAdapter(businessKnowledgeAdapter);
            Utils.setListViewHeightBasedOnChildren(listview2);
            businessFilterAdapter.setNotifyActivityIterface(this);
        }
        if (position == 2) {
            RecordKnowledgeFilterAdapter recordFilterAdapter = new RecordKnowledgeFilterAdapter(position, konwledgeList.get(position).getKinds(), MainActivity.this);
            listview1.setAdapter(recordFilterAdapter);
            recordKnowledgeAdapter = new RecordKonwledgeAdapter(konwledgeList.get(position).getKinds(), MainActivity.this);
            listview2.setAdapter(recordKnowledgeAdapter);
            Utils.setListViewHeightBasedOnChildren(listview2);
            recordFilterAdapter.setNotifyActivityIterface(this);
        }
        if (position == 3) {
            OtherKnowledgeFilterAdapter otherFilterAdapter = new OtherKnowledgeFilterAdapter(position, konwledgeList.get(position).getKinds(), MainActivity.this);
            listview1.setAdapter(otherFilterAdapter);
            otherKnowledgeAdapter = new OtherKonwledgeAdapter(konwledgeList.get(position).getKinds(), MainActivity.this);
            listview2.setAdapter(otherKnowledgeAdapter);
            Utils.setListViewHeightBasedOnChildren(listview2);
            otherFilterAdapter.setNotifyActivityIterface(this);
        }
    }

    private void
    switchKonwledgeKind(int i) {
        resetKonwledgeKind();
        switch (i) {
            case 0:
                problem_tech_suspend.setTextColor(0xffdc353b);
                tech_problem_listview.setVisibility(View.VISIBLE);
                tech_filter_listview.setVisibility(View.VISIBLE);
                problem_tech_suspend.setCompoundDrawables(null, null, drawableSelected, null);
                break;
            case 1:
                problem_business_suspend.setTextColor(0xffdc353b);
                business_problem_listview.setVisibility(View.VISIBLE);
                business_filter_listview.setVisibility(View.VISIBLE);
                problem_business_suspend.setCompoundDrawables(null, null, drawableSelected, null);
                if (!isBusinessLoaded) {
                    setListViewAdapter(1, business_filter_listview, business_problem_listview);
                    isBusinessLoaded = true;
                }
                break;
            case 2:
                problem_record_suspend.setTextColor(0xffdc353b);
                record_problem_listview.setVisibility(View.VISIBLE);
                record_filter_listview.setVisibility(View.VISIBLE);
                problem_record_suspend.setCompoundDrawables(null, null, drawableSelected, null);
                if (!isRecordLoaded) {
                    setListViewAdapter(2, record_filter_listview, record_problem_listview);
                    isRecordLoaded = true;
                }
                break;
            case 3:
                problem_other_suspend.setTextColor(0xffdc353b);
                other_problem_listview.setVisibility(View.VISIBLE);
                other_filter_listview.setVisibility(View.VISIBLE);
                problem_other_suspend.setCompoundDrawables(null, null, drawableSelected, null);
                if (!isOtherLoaded) {
                    setListViewAdapter(3, other_filter_listview, other_problem_listview);
                    isOtherLoaded = true;
                }
                break;
        }
    }

    private void resetKonwledgeKind() {
        contentScrollView.smoothScrollTo(0, scrollPX);
        problem_tech_suspend.setCompoundDrawables(null, null, drawable, null);
        problem_business_suspend.setCompoundDrawables(null, null, drawable, null);
        problem_record_suspend.setCompoundDrawables(null, null, drawable, null);
        problem_other_suspend.setCompoundDrawables(null, null, drawable, null);
        problem_tech_suspend.setTextColor(0xff000000);
        problem_business_suspend.setTextColor(0xff000000);
        problem_record_suspend.setTextColor(0xff000000);
        problem_other_suspend.setTextColor(0xff000000);
        tech_problem_listview.setVisibility(View.GONE);
        business_problem_listview.setVisibility(View.GONE);
        record_problem_listview.setVisibility(View.GONE);
        other_problem_listview.setVisibility(View.GONE);
        tech_filter_listview.setVisibility(View.GONE);
        business_filter_listview.setVisibility(View.GONE);
        record_filter_listview.setVisibility(View.GONE);
        other_filter_listview.setVisibility(View.GONE);

    }

    private void resetMenuLayout() {
        menu_message_layout.setBackgroundColor(Color.parseColor("#ffffff"));
        menu_knowledge_layout.setBackgroundColor(Color.parseColor("#ffffff"));
        menu_cs_layout.setBackgroundColor(Color.parseColor("#ffffff"));
        menu_setting_layout.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.problem_tech_suspend:
                popupFilter = true;
                switchKonwledgeKind(0);
                break;
            case R.id.problem_business_suspend:
                popupFilter = true;
                switchKonwledgeKind(1);
                break;
            case R.id.problem_record_suspend:
                popupFilter = true;
                switchKonwledgeKind(2);
                break;
            case R.id.problem_other_suspend:
                popupFilter = true;
                switchKonwledgeKind(3);
                break;
            case R.id.problem_filter_layout:
                problem_filter_layout.setVisibility(View.GONE);

                break;
            case R.id.custom_tel_layout:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-028-9900"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.custom_online_layout:
                intent = new Intent(MainActivity.this, CSOnlineActivity.class);
                startActivity(intent);
                break;
            case R.id.problem_feedback_layout:
                intent = new Intent(MainActivity.this, FeedBackKindActivity.class);
                startActivity(intent);
                break;
            case R.id.message_icon:
                intent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_message_layout:
                mMenu.toggle();
                skipToMessageActivity();
                break;
            case R.id.menu_knowledge_layout:
                switchKonwledgeKind(0);
                contentScrollView.smoothScrollTo(0, scrollPX);
                mMenu.toggle();
                resetMenuLayout();
                menu_knowledge_layout.setBackgroundColor(Color.parseColor("#e5e5e5"));
                break;
            case R.id.menu_cs_layout:
                mMenu.toggle();
                contentScrollView.smoothScrollTo(0, 0);
                problem_filter_layout.setVisibility(View.GONE);
                resetMenuLayout();
                menu_cs_layout.setBackgroundColor(Color.parseColor("#e5e5e5"));
                break;
            case R.id.menu_setting_layout:
//                resetMenuLayout();
//                menu_setting_layout.setBackgroundColor(Color.parseColor("#e5e5e5"));
                intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("exit_login", true);
                startActivity(intent);
                finish();
                break;
            case R.id.search_edit:
//                search_layout.setVisibility(View.VISIBLE);
//                left_menu_icon.setImageResource(R.mipmap.left_arrow_icon);
//                message_icon.setVisibility(View.GONE);
//                search_text.setVisibility(View.VISIBLE);
//                isInSearch = true;
                break;
            case R.id.search_text:
                searchProblem();
                break;
            case R.id.shadow_layout:
                mMenu.toggle();
                break;
        }
    }

    private void searchProblem() {
        questionSearchList.clear();
        String searchText = search_edit.getText().toString();
        Log.d("gaolei", "searchText----------------" + searchText);
        for (int i = 0; i < 4; i++) {
            List<KnowledgeKindObject> kinds = konwledgeList.get(i).getKinds();
            for (int j = 0; j < kinds.size(); j++) {
                List<KonwledgeObject> questionList = kinds.get(j).getContentList();
                for (int k = 0; k < questionList.size(); k++) {
                    if (questionList.get(k).getQuestion().contains(searchText)) {
                        questionSearchList.add(questionList.get(k));
                        Log.d("gaolei", "questionList.get(k).getQuestion()----------------" + questionList.get(k).getQuestion());
                    }
                }
            }
        }
        Log.d("gaolei", "questionSearchList.size()----------------" + questionSearchList.size());
        if (questionSearchList.size() == 0) {
            CommonUtils.getUtilInstance().showLongToast(MainActivity.this, getResources()
                    .getString(R.string.search_null));
        } else {
            if (searchAdapter == null) {
                searchAdapter = new SearchKonwledgeAdapter(questionSearchList, MainActivity.this);
                search_result_listview.setAdapter(searchAdapter);
            } else {
                searchAdapter.changeLibraryList(questionSearchList);
            }
        }
    }

    private void backFromSearchLayout() {
        search_layout.setVisibility(View.GONE);
        left_menu_icon.setImageResource(R.mipmap.expand_menu);
        message_icon.setVisibility(View.VISIBLE);
        search_text.setVisibility(View.GONE);
        search_edit.setText("");
        search_edit.clearFocus();

        if (searchAdapter != null) {
            if (questionSearchList != null) {
                questionSearchList.clear();
                searchAdapter.changeLibraryList(questionSearchList);
            }
        }
        isInSearch = false;
    }

    public void skipToKSYun(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse(CommonUrl.ksyunUrl));
        startActivity(intent);
    }

    public void skipToMessageActivity() {
        Intent intent = new Intent(MainActivity.this, MessageActivity.class);
        startActivity(intent);
        resetMenuLayout();
        menu_message_layout.setBackgroundColor(Color.parseColor("#e5e5e5"));
    }

    public void showCustomService() {
        contentScrollView.smoothScrollTo(0, 0);
    }

    @Override
    public void onScrollChanged(int scrollY) {

        int mBuyLayout2ParentTop = Math.max(scrollY, knowledge_kind_layout.getTop());
//        Log.i("gaolei", "scrollY-------------------" + scrollY);
        if (scrollY != 0) {
            knowledge_kind_layout_suspend.layout(0, mBuyLayout2ParentTop, knowledge_kind_layout_suspend.getWidth(), mBuyLayout2ParentTop + knowledge_kind_layout_suspend.getHeight());
        }
        if (popupFilter) {
                if (scrollY == scrollPX) {
                    problem_filter_layout.setVisibility(View.VISIBLE);
                    Animator animator = AnimatorInflater.loadAnimator(MainActivity.this, R.animator.push_in_from_up);
                    if (tech_filter_listview.getVisibility() == View.VISIBLE) {
                        tech_filter_listview.setPivotY(0);
                        animator.setTarget(tech_filter_listview);
                    }
                    if (business_filter_listview.getVisibility() == View.VISIBLE) {
                        business_filter_listview.setPivotY(0);
                        animator.setTarget(business_filter_listview);
                    }
                    if (record_filter_listview.getVisibility() == View.VISIBLE) {
                        record_filter_listview.setPivotY(0);
                        animator.setTarget(record_filter_listview);
                    }
                    if (other_filter_listview.getVisibility() == View.VISIBLE) {
                        other_filter_listview.setPivotY(0);
                        animator.setTarget(other_filter_listview);
                    }
                    animator.start();
                    popupFilter = false;
            }
        }
    }

    public void onSingleTouch(int position) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            search_layout.setVisibility(View.VISIBLE);
            left_menu_icon.setImageResource(R.mipmap.left_arrow_icon);
            message_icon.setVisibility(View.GONE);
            search_text.setVisibility(View.VISIBLE);
            isInSearch = true;
        }
    }

    public void toggleMenu(View view) {
        if (isInSearch) {
            backFromSearchLayout();
        } else {
            mMenu.toggle();
        }
    }

    static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case RECYCLE_SHOW:
                    if (position > 1) {
                        position = 0;
                    }
                    game_recommend_viewpager.setCurrentItem(position, false);
                    sendEmptyMessageDelayed(RECYCLE_SHOW, 2000);
                    position++;
                    break;

            }
        }
    }

    @Override
    //ctrl+alt+b
    public void changeListView(int kindPosition, int position) {
        Log.d("gaolei", "changeListView-------------" + position);
        problem_filter_layout.setVisibility(View.GONE);
        if (kindPosition == 0) {
            techKnowledgeAdapter.changeLibraryList(position);
        }
        if (kindPosition == 1) {
            businessKnowledgeAdapter.changeLibraryList(position);
        }
        if (kindPosition == 2) {
            recordKnowledgeAdapter.changeLibraryList(position);
        }
        if (kindPosition == 3) {
            otherKnowledgeAdapter.changeLibraryList(position);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (isInSearch) {
                backFromSearchLayout();
                return false;
            }
            if (!isFirstPressBack) {
                if (System.currentTimeMillis() - lastPressBack > 2 * 1000) {
                    lastPressBack = System.currentTimeMillis();
                } else {
                    finish();
                    return false;
                }
            }
            lastPressBack = System.currentTimeMillis();
            CommonUtils.getUtilInstance().showToast(this, getResources()
                    .getString(R.string.exit_app));
            isFirstPressBack = false;
        }
        return false;
    }

    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(RECYCLE_SHOW);
        isTechLoaded = false;
        isBusinessLoaded = false;
        isRecordLoaded = false;
        isOtherLoaded = false;
    }
}
