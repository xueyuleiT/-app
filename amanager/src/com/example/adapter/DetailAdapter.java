package com.example.adapter;

import java.util.ArrayList;

import com.example.amanager.R;
import com.example.model.XiaoshouDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DetailAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<XiaoshouDetail> xLst;
	Context context;
	public DetailAdapter(Context context,ArrayList<XiaoshouDetail> xLst){
		this.context = context;
		this.xLst = xLst;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return xLst.size();
	}

	@Override
	public Object getItem(int position) {
		return xLst.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		XiaoshouDetail model = xLst.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.detail_item, null);
			holder.callIn = (TextView) convertView.findViewById(R.id.in);
			holder.phone = (TextView) convertView.findViewById(R.id.phone);
			holder.startTime = (TextView) convertView.findViewById(R.id.startTime);
			holder.stopTime = (TextView) convertView.findViewById(R.id.stopTime);
			convertView.setTag(holder);
		}else
			holder = (ViewHolder) convertView.getTag();
		
		if(model.getCallIn().equals("true"))
			holder.callIn.setText("打进");
		else
			holder.callIn.setText("打出");
		holder.phone.setText(model.getPhone());
		holder.startTime.setText(model.getStartCallTime());
		holder.stopTime.setText(model.getStopCallTime());
		return convertView;
	}

	class ViewHolder{
		TextView callIn,startTime,stopTime,phone;
	}
}
