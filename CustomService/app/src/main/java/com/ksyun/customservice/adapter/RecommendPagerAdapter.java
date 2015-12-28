package com.ksyun.customservice.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.ksyun.customservice.fragment.RecommendDisplayFragment;

import java.util.List;


public class RecommendPagerAdapter extends FragmentPagerAdapter{
	private List<Integer> list;
	 public RecommendPagerAdapter(FragmentManager fm, List<Integer> list) {
		super(fm);
		this.list=list;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
	return new RecommendDisplayFragment().create(list.get(position));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return PagerAdapter.POSITION_NONE;
			}
//	public void changeList(List<String>list){
//		this.list=list;
//	}
	
}
