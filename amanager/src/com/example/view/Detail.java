package com.example.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.DetailAdapter;
import com.example.amanager.R;
import com.example.model.ConsumerModel;
import com.example.model.XiaoshouDetail;
import com.example.util.Constant;
import com.example.util.NetworkData;
import com.example.util.TextMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Detail extends Activity{

	
	private ListView dList;
	private TextView task,newAdd,title,callIn,callOut;
	private DetailAdapter dAdapter;
	private LinearLayout showTask;
	View show;
	String taskStr = "";
	ArrayList<String> taskList = new ArrayList<String>();
	ArrayList<String> addList = new ArrayList<String>();
	private ArrayList<XiaoshouDetail> xList = new ArrayList<XiaoshouDetail>();
	String phone = "",name = "";
	Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
				if(xList.size() > 0){
					dAdapter = new DetailAdapter(Detail.this, xList);
					dList.setAdapter(dAdapter);
				}
				task.setText(taskStr);
				show.setVisibility(View.GONE);
				showTask.setVisibility(View.VISIBLE);
				newAdd.setText(""+addList.size());
				
				int size = xList.size();
				int in = 0,out = 0;
				for (int i = 0; i < size; i++) {
					if(xList.get(i).getCallIn().equals("true")){
						in ++;
					}else
						out ++;
				}
				
				callIn.setText("共打进:"+in);
				callOut.setText("共打出:"+out);
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		phone = getIntent().getExtras().getString("phone");
		name = getIntent().getExtras().getString("name");
		initUI();
		initData();
	}

	private void initData() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String arr = NetworkData.getMyModel(Constant.GETCALLDETAIL+"&phone="+phone);
				if(arr != null && !arr.equals("")){
					Gson gson = new Gson();
					xList.addAll((Collection<? extends XiaoshouDetail>) gson
							.fromJson(arr,new TypeToken<ArrayList<XiaoshouDetail>>() {}.getType()));
				}
				try {
					TextMessage t = NetworkData.posturl(Constant.GETFINISHTASK+"&phone="+phone);
					if(t != null && !t.getContent().equals("")){
						taskStr = t.getContent();
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				String strArr1 = NetworkData.getMyModel(Constant.GETNEWADD+"&phone="+phone);
				if(strArr1 != null && !strArr1.equals("")){
					Gson gson = new Gson();
					addList.addAll((Collection<? extends String>) gson
							.fromJson(strArr1,new TypeToken<ArrayList<String>>() {}.getType()));
				}
				myHandler.sendEmptyMessage(1);
			}
		}).start();
	}
	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	private void initUI() {
		show = findViewById(R.id.show);
		callIn = (TextView) findViewById(R.id.callInNum);
		callOut = (TextView) findViewById(R.id.callOutNum);
		showTask = (LinearLayout) findViewById(R.id.showTask);
		dList = (ListView) findViewById(R.id.callList);
		task = (TextView) findViewById(R.id.task);
		newAdd = (TextView) findViewById(R.id.newAdd);
		title = (TextView) findViewById(R.id.title);
		title.setText(name);
	}
	
}
