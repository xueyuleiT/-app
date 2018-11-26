package com.example.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.amanager.R;
import com.example.fragment.home;
import com.example.model.XiaoshouModel;
import com.example.view.Detail;

public class XiaoshouAdapter extends BaseAdapter {
	LayoutInflater mInflater = null;
	ArrayList<XiaoshouModel> lst;
	Context context;
	public XiaoshouAdapter(Context context,ArrayList<XiaoshouModel> lst){
		this.lst = lst;
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return lst.size();
	}

	@Override
	public Object getItem(int position) {
		return lst.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.xiaoshou_item, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.name);
			holder.tv_phone = (TextView) convertView.findViewById(R.id.phone);
			holder.detail = (Button) convertView.findViewById(R.id.detail);
			convertView.setTag(holder);
		}else
			holder = (ViewHolder) convertView.getTag();
		holder.tv_name.setText(lst.get(position).getName());
		holder.tv_phone.setText(lst.get(position).getPhone());
		holder.detail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(context,Detail.class);
				it.putExtra("phone", lst.get(position).getPhone());
				it.putExtra("name", lst.get(position).getName());
				context.startActivity(it);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv_name,tv_phone;
		Button detail;
	}
}
