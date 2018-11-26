package com.assistant.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.assistant.model.TodayStatistics;
import com.example.assistant.R;

public class DayAdapter extends BaseAdapter{

	
	ArrayList<TodayStatistics> toDayList;
	LayoutInflater mInflater;
	Context context;
	int nowPosition = 0;
	public DayAdapter(Context context,ArrayList<TodayStatistics> toDayList){
		this.context = context;
		this.toDayList = toDayList;
		mInflater = LayoutInflater.from(context);
		
		toDayList.get(nowPosition).setClick(true);
	}
	
	@Override
	public int getCount() {
		return toDayList.size();
	}

	@Override
	public Object getItem(int position) {
		return toDayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.day_item, null);
			holder = new ViewHolder();
			holder.day = (TextView) convertView.findViewById(R.id.day);
			holder.month = (TextView) convertView.findViewById(R.id.month);
			holder.timeLinePoint = (ImageView) convertView.findViewById(R.id.timePoint);
			convertView.setTag(holder);
		}else
			holder = (ViewHolder) convertView.getTag();
		holder.day.setText(toDayList.get(position).getDay());
		holder.month.setText(toDayList.get(position).getMonth());
		
		if(toDayList.get(position).isClick())
			holder.timeLinePoint.setImageResource(R.drawable.yuan);
		else
			holder.timeLinePoint.setImageResource(R.drawable.left_line);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(position != nowPosition){
				if(!toDayList.get(position).isClick()){
					toDayList.get(position).setClick(true);
					toDayList.get(nowPosition).setClick(false);
					DayAdapter.this.notifyDataSetChanged();
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							Intent it = new Intent();
							it.setAction("SETDATA");
							it.putExtra("position", position);
							context.sendBroadcast(it);
						}
					}).start();
				}
				nowPosition = position;
				}
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		ImageView timeLinePoint;
		TextView day,month;
	}
}
