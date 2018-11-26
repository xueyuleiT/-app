package com.assistant.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.assistant.model.SmsContent;
import com.assistant.model.SmsInfo;
import com.assistant.utils.Constant;
import com.assistant.utils.CurrentTime;
import com.example.assistant.R;

public class SmsAdapter extends BaseAdapter{
	Context context;
	ArrayList<SmsInfo> info;
	LayoutInflater mInflater;
	PopupWindow sendMsgPop;
	String content = "";
	public SmsAdapter(Context context,ArrayList<SmsInfo> info){
		this.context = context;
		this.info = info;
		mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return info.size();
	}

	@Override
	public Object getItem(int arg0) {
		return info.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		final SmsInfo smsInfo = info.get(arg0);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.sms_item, null);
			holder = new ViewHolder();
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.phone = (TextView) convertView.findViewById(R.id.phone);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
			
		}else
			holder = (ViewHolder) convertView.getTag();
		
		holder.content.setText(smsInfo.getSmsbody());
		holder.time.setText(smsInfo.getDate());
		
		if(smsInfo.getName() == null){
			holder.phone.setText(smsInfo.getPhoneNumber());
		}else{
			holder.phone.setText(smsInfo.getName());
		}
		
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createSendMsgPop(smsInfo.getPhoneNumber(),smsInfo.getName());
			}
		});
		
		return convertView;
	}
	
	 private void createSendMsgPop(final String phone,final String name){
		 if(phone == null){
			 Toast.makeText(context, "号码为空", 0).show();
		 }else{
		 View popwindow = mInflater.inflate(R.layout.chat_message, null);
		 final EditText edtContent = (EditText) popwindow.findViewById(R.id.edtSend);
		 Button send = (Button) popwindow.findViewById(R.id.sendMsg);
		 final ListView infoList = (ListView) popwindow.findViewById(R.id.msgList);
		 edtContent.setText(content);
		 
		 SmsContent sms = new SmsContent(((Activity)context), Uri.parse("content://sms/"));
		 final ArrayList<SmsInfo> smsInfos = (ArrayList<SmsInfo>) sms.getOnePersonInfo(phone,name);
		 
		 final SmsInfoAdapter sAdapter = new SmsInfoAdapter(context, smsInfos);
		 infoList.setAdapter(sAdapter);
		 infoList.setSelection(smsInfos.size());
		 
		 sendMsgPop = new PopupWindow(popwindow, Constant.scWidth/2, Constant.scHeight*3/4);
		 send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!phone.equals("") && phone.length() == 11){
					String smsContent = edtContent.getText().toString();
					if(!smsContent.equals("")){
					SmsManager smsManager = SmsManager.getDefault(); 
					if(smsContent.length()>70){  
			             List<String> contents = smsManager.divideMessage(smsContent);  
			             for(String c:contents){  
			                 smsManager.sendTextMessage(phone, null, c, null, null);  
			             }  
			         }else{  
			             smsManager.sendTextMessage(phone, null, smsContent, null, null);  
			         } 
					edtContent.setText("");
					SmsInfo sms = new SmsInfo();
					sms.setDate(CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
					sms.setName(name);
					sms.setPhoneNumber(phone);
					sms.setType("0");
					sms.setSmsbody(smsContent);
					smsInfos.add(sms);
					sAdapter.notifyDataSetChanged();
					infoList.setSelector(smsInfos.size());
					
					context.sendBroadcast(new Intent("RECIEVE_SMS_ACTION"));
					}
				}else
					Toast.makeText(context, "号码不正确", 0).show();
			}
		});
		 
		 sendMsgPop.setFocusable(true);
		 sendMsgPop.setBackgroundDrawable(new BitmapDrawable());
		 int[] location = new int[2];
		 send.getLocationOnScreen(location); 
		 sendMsgPop.setAnimationStyle(R.style.PopupAnimation);
		 sendMsgPop.showAtLocation(send, Gravity.CENTER_HORIZONTAL, location[0], location[1]);
		 sendMsgPop.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				content =  edtContent.getText().toString();
			}
		});
	 }
	 }
	
	class ViewHolder{
		TextView phone,content,time;
	}
}
