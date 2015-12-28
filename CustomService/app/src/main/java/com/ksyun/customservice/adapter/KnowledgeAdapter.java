package com.ksyun.customservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksyun.customservice.R;


public class KnowledgeAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private String[] contentArray;

	public KnowledgeAdapter(String[] contentArray, Context context) {
		inflater = LayoutInflater.from(context);
		this.contentArray = contentArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contentArray.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contentArray[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.knowledge_library_item, null);


			holder.share_app_text = (TextView) convertView
					.findViewById(R.id.konwledge_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (contentArray.length> 0) {

			holder.share_app_text.setText(contentArray[position]);

		}
		return convertView;

	}

	class ViewHolder {
		TextView share_app_text;

	}
}