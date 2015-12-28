package com.ksyun.customservice.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ksyun.customservice.R;


public class RecommendDisplayFragment extends Fragment implements OnClickListener {

	private ImageView display_big_image;
	private LinearLayout display_image_fragment;
	public static boolean showNetImg=true;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.recommend_display_fragment, null);
		initView(view);

		return view;
	}

	public RecommendDisplayFragment create(int id) {
		RecommendDisplayFragment imageDisplayFragment= new RecommendDisplayFragment();
		Bundle bundle = new Bundle();  
		bundle.putInt("id", id);
//		Log.d("gaolei", "url-----------put-------------"+url);
        imageDisplayFragment.setArguments(bundle); 
	return imageDisplayFragment;
	}

	private void initView(View view) {
		display_big_image = (ImageView) view.findViewById(R.id.display_big_image);
		display_image_fragment= (LinearLayout) view.findViewById(R.id.display_image_fragment);
		display_image_fragment.setOnClickListener(this);
		int id=getArguments().getInt("id");
//		Log.d("gaolei", "imageUrl-----------get-------------"+imageUrl);
//		Log.d("gaolei", "showNetImg-------------"+showNetImg);
//		CommonUtils.getUtilInstance().displayLowQualityInImage(imageUrl, display_big_image);
		display_big_image.setImageResource(id);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		}
	}

}
