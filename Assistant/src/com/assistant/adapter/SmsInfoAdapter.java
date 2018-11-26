package com.assistant.adapter;

import java.util.ArrayList;

import com.assistant.model.SmsInfo;
import com.example.assistant.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SmsInfoAdapter extends BaseAdapter {

	ArrayList<SmsInfo> infoList;
	Context context;
	LayoutInflater mInflater;
	
	public SmsInfoAdapter(Context context, ArrayList<SmsInfo> infoList) {
		this.context = context;
		this.infoList = infoList;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return infoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return infoList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		SmsInfo info = infoList.get(position);
		
		if(info.getType().equals("1"))
			convertView = mInflater.inflate(R.layout.chat_message_come, null);
		else
			convertView = mInflater.inflate(R.layout.chat_message_send, null);
		
		holder.tv_name = (TextView) convertView.findViewById(R.id.tv_userName);
		holder.tv_chatContent = (TextView) convertView.findViewById(R.id.tv_chatContent);
		holder.tv_time = (TextView) convertView.findViewById(R.id.tv_sendTime);
		
		if(info.getType().equals("1"))
			holder.tv_name.setText(info.getName());
		else
			holder.tv_name.setText("我");
		holder.tv_time.setText(info.getDate());
		holder.tv_chatContent.setText(info.getSmsbody());
		
		return convertView;
	}

	class ViewHolder {
		TextView tv_name, tv_time, tv_chatContent;
	}

}
