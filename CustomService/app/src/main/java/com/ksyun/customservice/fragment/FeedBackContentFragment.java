package com.ksyun.customservice.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ksyun.customservice.FeedBackActivity;
import com.ksyun.customservice.R;
import com.ksyun.customservice.adapter.FeedBackListViewAdapter;

public class FeedBackContentFragment extends BaseFragment {

    private ListView listView;
    private String[] contentArray;
    private String title;
    private int typeposition=0;

    public static FeedBackContentFragment newInstance(String title,String[] contentArray,int position) {
        FeedBackContentFragment feedBackContentFragment= new FeedBackContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putStringArray("contentArray", contentArray);
        feedBackContentFragment.setArguments(bundle);
        return feedBackContentFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback_content_fragment, null);
        initViews(view);
        return view;
    }
private void initViews(View view){
    title=getArguments().getString("title");
    contentArray=getArguments().getStringArray("contentArray");
    listView = (ListView) view.findViewById(R.id.listView);
    listView.setAdapter(new FeedBackListViewAdapter(contentArray, getActivity()));

    final Intent intent=new Intent(getActivity(), FeedBackActivity.class);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String content =contentArray[position];
            intent.putExtra("title", title);
            intent.putExtra("problem_kind", content);
            intent.putExtra("typeposition", typeposition);
            startActivity(intent);
        }
    });
}

    @Override
    protected void lazyLoad() {
        if(title!=null) {
            if (title.equals(getString(R.string.business))) {
                typeposition = 0;
            }
            if (title.equals(getString(R.string.techonology))) {
                typeposition = 1;
            }
            if (title.equals(getString(R.string.record))) {
                typeposition = 2;
            }
            if (title.equals(getString(R.string.other))) {
                typeposition = 3;
            }
        }
    }
}
