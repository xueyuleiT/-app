package com.assistant.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.assistant.model.ConsumerModel;
import com.example.assistant.R;

public class SendMsgAdapter extends BaseAdapter {

	ArrayList<ConsumerModel> list = new ArrayList<ConsumerModel>();
	Context context;
	LayoutInflater mInflater;
	ArrayList<String> phoneArr;
	public SendMsgAdapter(Context context, ArrayList<ConsumerModel> list,ArrayList<String> phoneArr) {
		this.context = context;
		this.list = list;
		this.phoneArr = phoneArr;
		mInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final ConsumerModel con = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.contacts_item1, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.yixiang = (TextView) convertView.findViewById(R.id.yixiang);
			holder.zijinshili = (TextView) convertView.findViewById(R.id.zijinshili);
			holder.check = (RadioButton) convertView.findViewById(R.id.check);
			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();
		if (con.getTemp10().equals("1")) {
			holder.check.setChecked(true);
		} else
			holder.check.setChecked(false);
		
		holder.name.setText("姓名:"+con.getCustomer_name());
		holder.time.setText(con.getDatatime());
		holder.yixiang.setText("意向:"+con.getYixiang());
		holder.zijinshili.setText("预算:"+con.getZijinshili());
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!con.getCustomer_phone().equals("") && con.getCustomer_phone().length() == 11){
					if(con.getTemp10().equals("1")){
						con.setTemp10("");
						phoneArr.remove(con.getCustomer_phone());
					}
					else{
						con.setTemp10("1");
						phoneArr.add(con.getCustomer_phone());
					}
					SendMsgAdapter.this.notifyDataSetChanged();
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView name, yixiang, zijinshili, time;
		RadioButton check;
	}
}
