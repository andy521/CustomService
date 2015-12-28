package com.ksyun.customservice.jiekou;

import java.io.IOException;

/**
 * Created by Administrator on 2015/11/16.
 */
public class MyInterface {

    public interface ScrollViewListener{
        void onScrollChanged( int scrollY);
    }
    public  interface NetRequestIterface {
        void changeView(String result, String requestUrl);
        void exception(IOException e,String requestUrl);
//        void showProgressBar(int bytesWritten, int totalSize);
    }
    public  interface NotifyActivityIterface {
        void changeListView(int kindPosition,int position);
    }
    public  interface FinishActivityListener {
        void onFinishActivity();
    }
}
