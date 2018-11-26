package com.example.adapter;

import java.util.ArrayList;

import com.example.amanager.R;
import com.example.model.SelectModel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter{
	LayoutInflater mInflater;
	ArrayList<SelectModel> arr;
	Context context;
	MyListAdapter mAdapter = null;
	public MyListAdapter(Context context,ArrayList<SelectModel> arr){
		this.arr = arr;
		this.context = context;
		mInflater = LayoutInflater.from(context);
		mAdapter = this;
	}
	@Override
	public int getCount() {
		return arr.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arr.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
//		if(arg1 == null){
			holder = new ViewHolder();
			arg1 = mInflater.inflate(R.layout.mylist_item, null);
			holder.tv = (TextView) arg1.findViewById(R.id.tvSelect);
			holder.img = (ImageView) arg1.findViewById(R.id.imgDelete);
//		}else
//			holder = (ViewHolder) arg1.getTag();
		
		
		if(arr.get(arg0).isSelect()){
			holder.img.startAnimation(getRotateAnimation(0, 45, 300));
		}else
			holder.img.startAnimation(getRotateAnimation(45, 0, 300));
		
		holder.tv.setText(arr.get(arg0).getsName());
		return arg1;
	}
	public Animation getRotateAnimation(float fromDegrees ,float toDegrees,int durationMillis){
		RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(durationMillis);
		rotate.setFillAfter(true);
		return rotate;
	}
	class ViewHolder{
		TextView tv;
		ImageView img;
	}
}
