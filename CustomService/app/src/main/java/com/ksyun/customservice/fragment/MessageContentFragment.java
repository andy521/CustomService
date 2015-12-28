package com.ksyun.customservice.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ksyun.customservice.R;

public class MessageContentFragment extends Fragment {

    private ListView listView;
    private String[] contentArray;



    public static MessageContentFragment newInstance(String[] contentArray) {
        MessageContentFragment messageContentFragment= new MessageContentFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray("contentArray", contentArray);
        messageContentFragment.setArguments(bundle);
        return messageContentFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.feedback_content_fragment, null);
//        listView = (ListView) view.findViewById(R.id.listView);
//        listView.setAdapter(new FeedBackListViewAdapter(contentArray,getActivity()));

        return view;
    }

}
