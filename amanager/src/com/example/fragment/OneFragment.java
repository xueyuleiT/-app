package com.example.fragment;

import com.example.adapter.XiaoshouAdapter;
import com.example.amanager.R;
import com.example.model.ConsumerModel;
import com.example.util.Constant;
import com.example.util.CurrentTime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class OneFragment extends Fragment {

	private ListView lst ;
	private XiaoshouAdapter xAdapter;
	private Button refresh;
	private TextView chengjiao,xinzeng;
    public BroadcastReceiver m_BroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if(Constant.xiaoshouLst.size() > 0){
					xAdapter = new XiaoshouAdapter(getActivity(), Constant.xiaoshouLst);
					lst.setAdapter(xAdapter);
				}
				if(Constant.conLst.size() > 0){
					int size = Constant.conLst.size();
					int cjCount = 0,xzCount = 0;
					String today = 	CurrentTime.getCurrentTime("yyyy-MM-dd");
					for (int i = 0; i < size; i++) {
						ConsumerModel model = Constant.conLst.get(i);
						if(model.getTemp8().substring(0, 10).equals(today)){
							xzCount ++;
						}
						if(!model.getKehushuxing().equals("新客户") && !model.getChengjiaojine().equals("") 
								&& model.getTemp9().substring(0, 10).equals(today)){
							cjCount ++;
						}
					}
					
					chengjiao.setText("今日成交:  "+cjCount);
					xinzeng.setText("今日新增:  "+xzCount);
				}
		}
	};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		register_BroadCast();
		initUI();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(Constant.xiaoshouLst.size() > 0){
				xAdapter = new XiaoshouAdapter(getActivity(), Constant.xiaoshouLst);
				lst.setAdapter(xAdapter);
			}
		}
		
	};
	Thread t;
	private void initUI() {
		lst = (ListView) getView().findViewById(R.id.detailList);
		refresh = (Button) getView().findViewById(R.id.refresh);
		chengjiao = (TextView) getView().findViewById(R.id.chengjiao);
		xinzeng = (TextView) getView().findViewById(R.id.xinzeng);
		t =	new Thread(new Runnable() {
			
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				while(!Constant.isLoading){
					try {
						t.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				myHandler.sendEmptyMessage(1);
			}
		});
		t.start();
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				home.initData(getActivity(), refresh);
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.onefragment, container, false);
	}
	 private void register_BroadCast() {
			IntentFilter filter = new IntentFilter();
			filter.addAction("update");
			filter.setPriority(Integer.MAX_VALUE);
			getActivity().registerReceiver(m_BroadcastReceiver, filter);
		}
	public boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(m_BroadcastReceiver);
		super.onDestroy();
	}
}
