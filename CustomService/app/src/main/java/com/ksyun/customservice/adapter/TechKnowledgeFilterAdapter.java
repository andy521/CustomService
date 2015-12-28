package com.ksyun.customservice.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ksyun.customservice.KnowledgeDetailActivity;
import com.ksyun.customservice.R;
import com.ksyun.customservice.jiekou.MyInterface.NotifyActivityIterface;
import com.ksyun.customservice.object.KnowledgeAllObject.KnowledgeKindObject;

import java.util.List;


public class TechKnowledgeFilterAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<KnowledgeKindObject> list;
    private Context context;
    private int kindPosition=0,clickPosition = 0;
    private NotifyActivityIterface notifyActivityIterface;

    public void setNotifyActivityIterface(NotifyActivityIterface notifyActivityIterface){
        this.notifyActivityIterface=notifyActivityIterface;
    }

    public TechKnowledgeFilterAdapter(int kindPosition, List<KnowledgeKindObject> list, Context context) {
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.kindPosition=kindPosition;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void changeList(List<KnowledgeKindObject> list) {
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.knowledge_filter_item, null);
            holder.konwledge_filter_text = (TextView) convertView
                    .findViewById(R.id.konwledge_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list.size() > 0) {
            KnowledgeKindObject object = list.get(position);
            holder.konwledge_filter_text.setText(object.getTitle());
            holder.konwledge_layout = (RelativeLayout) convertView
                    .findViewById(R.id.konwledge_layout);
            if (position == clickPosition) {
                holder.konwledge_filter_text.setTextColor(0xffdc353b);
            }else{
                holder.konwledge_filter_text.setTextColor(0x99000000);
            }
            final Intent intent = new Intent(context, KnowledgeDetailActivity.class);
            holder.konwledge_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   clickPosition = position;
                    notifyDataSetChanged();
                    notifyActivityIterface.changeListView(kindPosition,position);
                }
            });
        }
        return convertView;

    }

    class ViewHolder {
        TextView konwledge_filter_text;
        RelativeLayout konwledge_layout;
    }
}