package com.assistant.view;

import java.util.ArrayList;
import java.util.Collection;

import com.assistant.model.ConsumerModel;
import com.assistant.utils.Constant;
import com.assistant.utils.NetworkData;
import com.example.assistant.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LaoDaiXin extends Activity{
	ArrayList<Model> mArr = new ArrayList<LaoDaiXin.Model>();
	ListView mList;
	View show;
	String phone;
	Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
				show.setVisibility(View.GONE);
				mList.setAdapter(new Madapter());
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.laodaixin);
		mList = (ListView) findViewById(R.id.laoDaiXinList);
		show = findViewById(R.id.progress_show);
		
		phone = getIntent().getExtras().getString("phone");
	}
	
	
	
	@Override
	protected void onStart() {
		super.onStart();
		initData();
	}



	private void initData() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(!phone.equals("")){
					String arr = NetworkData.getMyModel(Constant.GETLAODAIXIN+"&phone="+phone);
					if(arr != null && !arr.equals("") && !arr.equals("error")){
						Gson gson = new Gson();
						mArr.addAll((Collection<? extends Model>) gson.fromJson(arr,
								new TypeToken<ArrayList<Model>>() {}.getType()));
					}
				}
				myHandler.sendEmptyMessage(1);
			}
		}).start();
	}

	class Madapter extends BaseAdapter{
		LayoutInflater mInflater;
		public Madapter(){
			mInflater = LayoutInflater.from(LaoDaiXin.this);
		}
		@Override
		public int getCount() {
			return mArr.size();
		}

		@Override
		public Object getItem(int position) {
			return mArr.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Model model = mArr.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.laoyezhu_item, null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.phone = (TextView) convertView.findViewById(R.id.phone);
			}else
				holder = (ViewHolder) convertView.getTag();
			holder.name.setText(model.getName());
			holder.phone.setText(model.getPhone());
			return convertView;
		}
		
		class ViewHolder{
			TextView name,phone;
		}
	}

	class Model{
		String time = "", phone = "" ,name = "";

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
}
