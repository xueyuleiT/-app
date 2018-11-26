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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.assistant.model.CallModel;
import com.assistant.model.SmsContent;
import com.assistant.model.SmsInfo;
import com.assistant.utils.Constant;
import com.assistant.utils.CurrentTime;
import com.assistant.view.UserInfo;
import com.example.assistant.R;

public class CallAdapter extends BaseAdapter {
	ArrayList<CallModel> callArr;
	Context context;
	PopupWindow sendMsgPop;
	LayoutInflater mInflater;
	String content = "";
	
	public CallAdapter(Context context,ArrayList<CallModel> callArr){
		this.callArr = callArr;
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return callArr.size();
	}

	@Override
	public Object getItem(int arg0) {
		return callArr.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View converView, ViewGroup arg2) {
		final CallModel model = callArr.get(position);
		
		ViewHolder holder = null;
		if(converView == null){
			holder = new ViewHolder();
			converView = mInflater.inflate(R.layout.call_item, null);
			holder.name = (TextView) converView.findViewById(R.id.name);
			holder.phone = (TextView) converView.findViewById(R.id.phone);
			holder.yixiang = (TextView) converView.findViewById(R.id.yixiang);
			holder.huxing = (TextView) converView.findViewById(R.id.huxing);
			holder.time = (TextView) converView.findViewById(R.id.time);
			holder.callIn = (ImageButton) converView.findViewById(R.id.call);
			holder.message = (ImageButton) converView.findViewById(R.id.message);
			converView.setTag(holder);
		}else
			holder = (ViewHolder) converView.getTag();
		if(model.isOut()){
			holder.callIn.setBackgroundResource(R.drawable.oneselect);
		}else{
			holder.callIn.setBackgroundResource(R.drawable.phone_callin);
		}
		holder.name.setText(model.getName());
		holder.phone.setText(model.getPhone());
		holder.yixiang.setText(model.getYixiang());
		holder.huxing.setText(model.getYixianghuxing());
		holder.time.setText(model.getCallTime());
		
		
		
		converView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(context,UserInfo.class);
				int size = Constant.conLst.size();
				for (int i = 0; i < size; i++) {
					if(Constant.conLst.get(i).getCustomer_phone().equals(model.getPhone())){
						it.putExtra("consumer", Constant.conLst.get(i));
						context.startActivity(it);
						break;
					}
				}
			}
		});
		
		holder.callIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!model.getPhone().equals("") && model.getPhone().length() == 11){
					if(Constant.isCanUseSim(context)){
			        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri  
		            .parse("tel:" + model.getPhone()));  
			        context.startActivity(dialIntent); 
					}
				}else{
					Toast.makeText(context, "号码不正确", 0).show();
				}
			}
		});
		holder.message.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!model.getPhone().equals("") && model.getPhone().length() == 11){
					if(Constant.isCanUseSim(context))
						createSendMsgPop(model.getPhone(),model.getName());
				}else{
					Toast.makeText(context, "号码不正确", 0).show();
				}
			}
		});
		
		return converView;
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
				infoList.setSelection(smsInfos.size());
				
				context.sendBroadcast(new Intent("RECIEVE_SMS_ACTION"));
			}
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
	TextView name,phone,yixiang,huxing,time;
	ImageButton callIn,message;
	}
}
