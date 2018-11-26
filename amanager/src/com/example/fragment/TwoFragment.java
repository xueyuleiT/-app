package com.example.fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.amanager.R;
import com.example.util.Constant;
import com.example.util.NetworkData;
import com.example.util.TextMessage;
import com.example.view.AlertDialog;
import com.example.view.RegisterActivity;

public class TwoFragment extends Fragment implements OnClickListener{
	View register,guohu,setting;
	String taskNum = "0" ,dayNum = "0";
	public BroadcastReceiver m_BroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
			}

	 };
	 
	Handler myhandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
				Toast.makeText(getActivity(), "号码未注册", 0).show();
			}else if(msg.what == 3){
				Toast.makeText(getActivity(), "更新成功", 0).show();
			}else if(msg.what == 2){
				Toast.makeText(getActivity(), "更新失败", 0).show();
			}else if(msg.what == 4){
				Constant.showLoad(getActivity(), setting);
			}
			Constant.dimissLoad();
		}
		
	};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
		
	}


	private void initUI() {
		register = getView().findViewById(R.id.register);
		guohu = getView().findViewById(R.id.zhuanyizhanghu);
		setting = getView().findViewById(R.id.setting);
		
		setting.setOnClickListener(this);
		guohu.setOnClickListener(this);
		register.setOnClickListener(this);
	}



	public List deepCopy(List src){   
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();   
        ObjectOutputStream out;
        List dest = null;
		try {
			out = new ObjectOutputStream(byteOut);
			 out.writeObject(src);   
		       
		        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());   
		        ObjectInputStream in =new ObjectInputStream(byteIn);   
		        dest = (List)in.readObject(); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}   
        return dest;   
    }   
	

	
	@Override
	public void onDestroy() {
//		getActivity().unregisterReceiver(m_BroadcastReceiver);
		super.onDestroy();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.twofragment, container, false);
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
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.setting:
			String[] task = {" 0"," 1"," 2"," 3"," 4"," 5"," 6"," 7"," 8"," 9"," 10"," 11"," 12"," 13"," 14"," 15",
					" 16"," 17"," 18"," 19"," 20"," 21"," 22"," 23"," 24"," 25"," 26"," 27"," 28"," 29"," 30"," 31"};
			View viewSet = LayoutInflater.from(getActivity()).inflate(R.layout.settask, null);
			final Spinner taskSpinner = (Spinner) viewSet.findViewById(R.id.taskSpinner);
			final Spinner maginDaySpinner = (Spinner) viewSet.findViewById(R.id.maginDaySpinner);
			final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, task);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			taskSpinner.setAdapter(adapter);
			
			final ArrayAdapter<String> dayAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, task);
			dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			maginDaySpinner.setAdapter(dayAdapter);
			
			
			maginDaySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					dayNum = dayAdapter.getItem(position).toString().trim();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
				}
			});
			taskSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					taskNum = adapter.getItem(position).toString().trim();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
				}
			});
			
			
			final AlertDialog ad = new AlertDialog(getActivity());
			ad.setTitle("任务设置");
			ad.setView(viewSet);
			ad.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Constant.showLoad(getActivity(), setting);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try {
							TextMessage t = NetworkData.posturl(Constant.SETTASK+"&num="+taskNum+"&day="+dayNum);
								if(t != null && t.getContent().equals("更新成功")){
									Constant.TASK = taskNum;
									myhandler.sendEmptyMessage(3);
								}else
									myhandler.sendEmptyMessage(2);
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}).start();
					ad.dismiss();
				}
			});
			ad.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ad.dismiss();
				}
			});
			
//			new AlertDialog.Builder(getActivity())
//			.setView(viewSet)
//			.setPositiveButton("确定", new DialogInterface.OnClickListener(){
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					Constant.showLoad(getActivity(), setting);
//					new Thread(new Runnable() {
//						
//						@Override
//						public void run() {
//							try {
//							TextMessage t = NetworkData.posturl(Constant.SETTASK+"&num="+taskNum+"&day="+dayNum);
//								if(t != null && t.getContent().equals("更新成功")){
//									Constant.TASK = taskNum;
//									myhandler.sendEmptyMessage(3);
//								}else
//									myhandler.sendEmptyMessage(2);
//							} catch (ClientProtocolException e) {
//								e.printStackTrace();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//					}).start();
//					
//				}
//				
//			})
//			.setNegativeButton("取消", new DialogInterface.OnClickListener(){
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.cancel();
//				}
//				
//			}).show();
			break;
		case R.id.zhuanyizhanghu:
			View view  = LayoutInflater.from(getActivity()).inflate(R.layout.registerview, null);
			final EditText from = (EditText) view.findViewById(R.id.from);
			final EditText to = (EditText) view.findViewById(R.id.to);
			
			
			final AlertDialog ad1 = new AlertDialog(getActivity());
			ad1.setTitle("过户");
			ad1.setView(view);
			ad1.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(from.getText().toString().length() != 11 || to.getText().toString().length() != 11){
						Toast.makeText(getActivity(), "号码输入有误", 0).show();
					}else{
						Constant.showLoad(getActivity(), setting);
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								try {
									TextMessage t = NetworkData.posturl(Constant.ISREGISTER+"&phone="+to.getText().toString());
									if(t.getContent().equals("未注册")){
										myhandler.sendEmptyMessage(1);
									}else{
				            		myhandler.sendEmptyMessage(4);
										TextMessage t1 = NetworkData.posturl(Constant.GUOHU+"&fromphone="+from.getText().toString()+"&tophone="+to.getText().toString());
										if(t1 != null && t1.getContent().equals("更新成功")){
											myhandler.sendEmptyMessage(3);
										}else{
											myhandler.sendEmptyMessage(2);
										}
									}
								} catch (ClientProtocolException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}).start();
						ad1.dismiss();
					}
				}
			});
			ad1.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					ad1.dismiss();
				}
			});
			
	/*		new AlertDialog.Builder(getActivity())
			.setView(view)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(from.getText().toString().length() != 11 || to.getText().toString().length() != 11){
						Toast.makeText(getActivity(), "号码输入有误", 0).show();
					}else{
						Constant.showLoad(getActivity(), setting);
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								try {
									TextMessage t = NetworkData.posturl(Constant.ISREGISTER+"&phone="+to.getText().toString());
									if(t.getContent().equals("未注册")){
										myhandler.sendEmptyMessage(1);
									}else{
				            		myhandler.sendEmptyMessage(4);
										TextMessage t1 = NetworkData.posturl(Constant.GUOHU+"&fromphone="+from.getText().toString()+"&tophone="+to.getText().toString());
										if(t1 != null && t1.getContent().equals("更新成功")){
											myhandler.sendEmptyMessage(3);
										}else{
											myhandler.sendEmptyMessage(2);
										}
									}
								} catch (ClientProtocolException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			}).show();*/
			break;
		case R.id.register:
			Intent it = new Intent(getActivity(),RegisterActivity.class);
			getActivity().startActivity(it);
			break;
		}
	}


}
