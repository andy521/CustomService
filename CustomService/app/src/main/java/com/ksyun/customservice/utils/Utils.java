package com.ksyun.customservice.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utils {

	public static Utils utils;
	public SharedPreferences accountSP, firstEnterSP;
	public SharedPreferences loginOrShareSP;
	public SharedPreferences.Editor accountEditor, firstEnterEditor,
			loginOrShareEditor;

	private Utils() {
	}

	public static Utils getInstance() {
		if (utils == null) {
			synchronized (Utils.class) {
				utils = new Utils();
			}
		}
		return utils;
	}

	public void generateSP(Context context) {
		// if(which.equals("account")){
		// accountSP =context.getSharedPreferences("account", 0);
		// accountEditor = accountSP.edit();
		// }
		// if(which.equals("firstEnter")){
		// firstEnterSP =context.getSharedPreferences("firstEnter", 0);
		// firstEnterEditor = firstEnterSP.edit();
		// }
		// if(which.equals("loginOrShare")){
		loginOrShareSP = context.getSharedPreferences("loginOrShare", 0);
		loginOrShareEditor = loginOrShareSP.edit();
		// }
	}

	public void updateSP(String name, String value) {
		// for (Map.Entry<String, String> entry : map.entrySet()) {
		loginOrShareEditor.putString(name, value);
		// }
		loginOrShareEditor.commit();
	}


	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))
				+ listView.getPaddingTop() + listView.getPaddingBottom();
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	public static void setListViewHeightBasedOnChildren(GridView listView) {
		if (listView == null)
			return;

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		Log.e("gaolei",
				"listAdapter.getCount()-------1---------"
						+ listAdapter.getCount());
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
			Log.e("gaolei",
					"listItem.getMeasuredHeight()---------"
							+ listItem.getMeasuredHeight());
		}
		Log.e("gaolei",
				"listAdapter.getCount()-------2---------"
						+ listAdapter.getCount());
		Log.e("gaolei", "totalHeight----------------" + totalHeight);
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight;
		listView.setLayoutParams(params);
	}
	public String unicodeToString(String utfString){
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;

		while((i=utfString.indexOf("\\u", pos)) != -1){
			sb.append(utfString.substring(pos, i));
			if(i+5 < utfString.length()){
				pos = i+6;
				sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
			}
		}

		return sb.toString();
	}
}
