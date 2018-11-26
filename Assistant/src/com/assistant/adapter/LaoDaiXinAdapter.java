package com.assistant.adapter;

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

import com.assistant.model.ConsumerModel;
import com.assistant.view.LaoDaiXin;
import com.example.assistant.R;

public class LaoDaiXinAdapter extends BaseAdapter {
	ArrayList<ConsumerModel> conArr ;
	Context context;
	LayoutInflater mInflater;
	public LaoDaiXinAdapter(Context context,ArrayList<ConsumerModel> conArr){
		this.context = context;
		this.conArr = conArr;
		mInflater = LayoutInflater.from(context);
	}
	

	@Override
	public int getCount() {
		return conArr.size();
	}

	@Override
	public Object getItem(int arg0) {
		return conArr.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ConsumerModel model = conArr.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.laodaixin_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.phone = (TextView) convertView.findViewById(R.id.phone);
			holder.yixiang = (TextView) convertView.findViewById(R.id.yixiang);
			holder.huxing = (TextView) convertView.findViewById(R.id.huxing);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.laoDaiXin = (Button) convertView.findViewById(R.id.laoDaiXin);
			convertView.setTag(holder);
		}else
			holder = (ViewHolder) convertView.getTag();
		holder.name.setText(model.getCustomer_name());
		holder.phone.setText(model.getCustomer_phone());
		holder.yixiang.setText(model.getYixiang());
		holder.huxing.setText(model.getYixianghuxing());
		holder.time.setText(model.getDatatime());
		
		holder.laoDaiXin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(context,LaoDaiXin.class);
				it.putExtra("phone", model.getCustomer_phone());
				context.startActivity(it);
			}
		});
		
		return convertView;
	}
	class ViewHolder{
		TextView name,phone,yixiang,huxing,time;
		Button laoDaiXin;
	}
}
