package com.assistant.fragment;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;

import com.assistant.adapter.SendMsgAdapter;
import com.assistant.adapter.SmsAdapter;
import com.assistant.model.ConsumerModel;
import com.assistant.model.SmsContent;
import com.assistant.model.SmsInfo;
import com.assistant.service.PhoneReceiver;
import com.assistant.utils.Constant;
import com.assistant.view.ContactsActivity;
import com.example.assistant.R;

public class FourFragment extends Fragment implements OnClickListener{
	 private boolean scrolling = false;
	 PopupWindow sendMsgPop;
	 Button sendMessage;
	 int arrId = 1;
	 ArrayList<ConsumerModel> contacts = new ArrayList<ConsumerModel>();
	 private ListView listview,contactList; 
	 private ArrayList<SmsInfo> infos;
	 private ArrayList<ConsumerModel> constactsList = new ArrayList<ConsumerModel>();
     private SmsAdapter sAdapter;
     private SendMsgAdapter smAdapter;
     private Button importContacts;
     private int sendSize = 0;
     private boolean isShow = false;
     String smsContent = "";
     TextView sendCount ;
     /* 自定义ACTION常数，作为广播的Intent Filter识别常数 */
     String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
     String RECIEVE_SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
     String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
     
     Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
				Uri uri = Uri.parse("content://sms/");
				SmsContent sc = new SmsContent(getActivity(), uri);
				infos = (ArrayList<SmsInfo>) sc.getSmsInfo();
				
				sAdapter = new SmsAdapter(getActivity(), infos);
				listview.setAdapter(sAdapter);
			}
		}
    	 
     };
     
	public BroadcastReceiver m_BroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("update")) {// 已接受
					
				smAdapter = new SendMsgAdapter(getActivity(), Constant.conLst,
						phoneArr);
				if(Constant.conLst.size() > 0 && !Constant.conLst.get(0).getCustomer_phone().equals("")){
					contactList.setAdapter(smAdapter);
				}
			}
			if (intent.getAction().equals(DELIVERED_SMS_ACTION)) {
				// 判断短信是否成功
				switch (getResultCode()) {
				case Activity.RESULT_OK:
//					sendSize ++;
//					sendCount.setText("已发送:    "+(sendSize)+"/"+phoneArr.size());
					break;
				default:
					break;
				}
			}else if(intent.getAction().equals(SMS_SEND_ACTIOIN)){
				 switch(getResultCode())
		          {
		          case Activity.RESULT_OK:
						sendSize ++;
						sendCount.setText("已发送:    "+(sendSize)+"/"+phoneArr.size());
						break;
		          case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
		              /* 发送短信失败 */
		              break;
		          }
			}else if(intent.getAction().equals(RECIEVE_SMS_ACTION)){
				 initData();
			}
		}
	};
	String Group[] = new String[]{
			"按意向筛选","我的客户", "按业态筛选","按户型筛选","按面积筛选","按认知渠道筛选","按资金实力筛选","按客户属性筛选","按客户组筛选"
			 ,"按预算筛选"};
	 ArrayList<String> phoneArr = new ArrayList<String>();
	 AbstractWheel group;
	 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initUI();
		register_BroadCast();
		initData();
		initListener();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void initData() {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						myHandler.sendEmptyMessage(1);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				}
			}).start();
		
		
	}
	 private void register_BroadCast() {
			IntentFilter filter = new IntentFilter();
			filter.addAction("update");
			filter.addAction(RECIEVE_SMS_ACTION);
			filter.addAction(DELIVERED_SMS_ACTION);
			filter.addAction(SMS_SEND_ACTIOIN);
			filter.setPriority(Integer.MAX_VALUE);
			getActivity().registerReceiver(m_BroadcastReceiver, filter);
		}
	private void initListener() {
		sendMessage.setOnClickListener(this);
		importContacts.setOnClickListener(this);
		group.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				if (!scrolling) {
					switch (group.getCurrentItem()) {
					case 1:
						arrId = 1;
						break;
					}
					smAdapter = new SendMsgAdapter(getActivity(),
							Constant.conLst, phoneArr);

					if (Constant.conLst.size() > 0
							&& !Constant.conLst.get(0).getCustomer_phone()
									.equals("")) {
						contactList.setAdapter(smAdapter);
					}

				}
			}
        });
		group.addScrollingListener( new OnWheelScrollListener() {
            public void onScrollingStarted(AbstractWheel wheel) {
                scrolling = true;
            }
            public void onScrollingFinished(AbstractWheel wheel) {
                scrolling = false;
                if(!isShow){
            	switch(group.getCurrentItem()){ 
            	case 1:
            		arrId = 1;
            		break;
            	case 0:
            		arrId = R.array.yixiang;
	            	break;
            	case 2:
            		arrId = R.array.yixiangyetai;
	            	break;
            	case 3:
            		arrId = R.array.yixianghuxing;
	            	break;
            	case 4:
            		arrId = R.array.yixiangmianji;
	            	break;
            	case 5:
            		arrId = R.array.renzhiqudao;
	            	break;
             	case 6:
             		arrId = R.array.zijinshili;
	            	break;
            	case 7:
            		arrId = R.array.kehushuxing;
	            	break;
            	case 8:
            		arrId = R.array.kehuzu;
	            	break;
            	case 9:
            		arrId = R.array.yusuan;
	            	break;
            	}
                if(arrId != 1){
                	isShow = true;
                new AlertDialog.Builder(getActivity()) // build AlertDialog  
    	        .setTitle("选择意向")
    	        .setItems(arrId, new DialogInterface.OnClickListener() { //content  
    	            @Override  
    	            public void onClick(DialogInterface dialog, int which) {  
    	            	PhoneReceiver.showLoad(getActivity(), sendMessage);
    	           	String item = getResources().getStringArray(arrId)[which];
    	           	 int length = Constant.conLst.size();
    	           	 int caseId = group.getCurrentItem();
    	           	 contacts.clear();
    	          for (int i = 0; i < length; i++) {
    	        	  if(caseId == 0){
    	        		  System.out.println(" yetai = "+Constant.conLst.get(i).getYixiang());
							if(item.equals(Constant.conLst.get(i).getYixiang())){
								contacts.add(Constant.conLst.get(i));
							}
						}else if(caseId == 2){
							System.out.println(" yetai = "+Constant.conLst.get(i).getYixiangyetai());
							if(item.equals(Constant.conLst.get(i).getYixiangyetai())){
								contacts.add(Constant.conLst.get(i));
							}
						}else if(caseId == 3){
							System.out.println(" yetai = "+Constant.conLst.get(i).getYixianghuxing());
							if(item.equals(Constant.conLst.get(i).getYixianghuxing())){
								contacts.add(Constant.conLst.get(i));
							}
						}else if(caseId == 4){
							System.out.println(" yetai = "+Constant.conLst.get(i).getYixiangmianji());
							if(item.equals(Constant.conLst.get(i).getYixiangmianji())){
								contacts.add(Constant.conLst.get(i));
							}
						}else if(caseId == 5){
							System.out.println(" yetai = "+Constant.conLst.get(i).getRenzhiqudao());
							if(item.equals(Constant.conLst.get(i).getRenzhiqudao())){
								contacts.add(Constant.conLst.get(i));
							}
						}else if(caseId == 6){
							System.out.println(" yetai = "+Constant.conLst.get(i).getZijinshili());
							if(item.equals(Constant.conLst.get(i).getZijinshili())){
								contacts.add(Constant.conLst.get(i));
							}
						}else if(caseId == 7){
							System.out.println(" yetai = "+Constant.conLst.get(i).getKehushuxing());
							if(item.equals(Constant.conLst.get(i).getKehushuxing())){
								contacts.add(Constant.conLst.get(i));
							}
						}else if(caseId == 8){
							System.out.println(" yetai = "+Constant.conLst.get(i).getKehuzu());
							if(item.equals(Constant.conLst.get(i).getKehuzu())){
								contacts.add(Constant.conLst.get(i));
							}
						}else if(caseId == 9){
							System.out.println(" yetai = "+Constant.conLst.get(i).getYisuan());
							if(item.equals(Constant.conLst.get(i).getYisuan())){
								contacts.add(Constant.conLst.get(i));
							}
						}
				}
    	        	smAdapter = new SendMsgAdapter(getActivity(), contacts,phoneArr);
    	        	if(contacts.size() > 0 && !contacts.get(0).getCustomer_phone().equals("")){
    					contactList.setAdapter(smAdapter);
    				}else
    					contactList.setAdapter(smAdapter);
  					isShow = false;
  					PhoneReceiver.dimissLoad();
    	            }  
    	        })  
    	        .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
    	              
    	            @Override  
    	            public void onClick(DialogInterface dialog, int which) {  
    	            	isShow = false;
    	                dialog.dismiss(); //关闭alertDialog  
    	            }  
    	        }).show();
                }else{
                	smAdapter = new SendMsgAdapter(getActivity(), Constant.conLst,phoneArr);
                	if(Constant.conLst.size() > 0 && !Constant.conLst.get(0).getCustomer_phone().equals("")){
    					contactList.setAdapter(smAdapter);
    				}else
    					contactList.setAdapter(smAdapter);
                }
            }
            }
        });
	
		group.setCurrentItem(1);
	}

	private void initUI() {
		group = (AbstractWheel) getView().findViewById(R.id.group);
		sendMessage = (Button) getView().findViewById(R.id.sendMessage);
		listview = (ListView) getView().findViewById(R.id.smsList);
		importContacts = (Button) getView().findViewById(R.id.importContacts);
		contactList = (ListView) getView().findViewById(R.id.contactList);
		
		group.setCyclic(false);
		group.setVisibleItems(5);
		group.setViewAdapter(new ContactsAdapter(getActivity(),Group));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fourfragment, container, false);
	}
	
	
	 private class ContactsAdapter extends AbstractWheelTextAdapter {
	        // Countries names
	        private String contactsGroup[];
	        // Countries flags
	        
	        /**
	         * Constructor
	         */
	        protected ContactsAdapter(Context context,String contactsGroup[]) {
	            super(context, R.layout.contacts_item, NO_RESOURCE);
	            setItemTextResource(R.id.contacts_name);
	            this.contactsGroup = contactsGroup;
	        }

	        @Override
	        public View getItem(int index, View cachedView, ViewGroup parent) {
	            View view = super.getItem(index, cachedView, parent);
	            ImageView img = (ImageView) view.findViewById(R.id.imgHead);
	            if(index == 0){
	            	img.setVisibility(View.VISIBLE);
	            }else{
	            	img.setVisibility(View.GONE);
	            }
	            TextView textView = (TextView) view.findViewById(R.id.contacts_name);
	            textView.setText(contactsGroup[index]);
	            return view;
	        }
	        
	        @Override
	        public int getItemsCount() {
	            return contactsGroup.length;
	        }
	        
	        @Override
	        protected CharSequence getItemText(int index) {
	            return contactsGroup[index];
	        }
	    }
	 
	// 把短信写入数据库
	 public void writeMsg(String content,String phone,ContentValues values,Uri SMS_URI){
	     
	   try{
	     // 发送时间
	     values.put("date", System.currentTimeMillis());
	     // 阅读状态      
	     values.put("read", 0);
	     // 类型：1为收，2为发      
	     values.put("type", 2);
	     // 发送号码      
	     values.put("address",phone);
	     // 发送内容     
	     values.put("body", content);
	     // 插入短信库 
	     getActivity().getContentResolver().insert(SMS_URI, values);      
	   }catch (Exception e) { 
	   }
	 }
	 
	 private void sendMessage(EditText edtContent,ArrayList<String> numberArr,TextView sendCount){
		 SmsManager smsManager = SmsManager.getDefault();
		 int size = numberArr.size();
		 Uri SMS_URI = Uri.parse("content://sms/");
		 smsContent = edtContent.getText().toString();
		 // 发送结果广播 
		 PendingIntent mSendPI = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SMS_SEND_ACTIOIN), 0);
         PendingIntent deliverPI = PendingIntent.getBroadcast(getActivity(), 0, new Intent(DELIVERED_SMS_ACTION), 0);
       //将数据插入数据库
         ContentValues cv = new ContentValues();
         sendSize = 0;
         for (int i = 0; i < size; i++) {
//			 if(smsContent.length()>70){  
//	             List<String> contents = smsManager.divideMessage(smsContent);  
//	             for(String c:contents){  
//	                 smsManager.sendTextMessage(numberArr.get(i), null, c, null, deliverPI);  
//	             }  
//	         }else{  
	         smsManager.sendTextMessage(numberArr.get(i), null, smsContent, mSendPI, deliverPI);  
//	         }  
			 writeMsg(smsContent, numberArr.get(i), cv ,SMS_URI);
		}
         smsContent = "";
		 edtContent.setText("");
		 numberArr.clear();
		 
		 initData();
		 
		 int size1 = Constant.conLst.size();
		 
		 for (int i = 0; i < size1; i++) {
			if(Constant.conLst.get(i).getTemp10().equals("1")){
				Constant.conLst.get(i).setTemp10("");
			}
		}
		 smAdapter.notifyDataSetChanged();
	 }

	 @Override
	public void onDestroy() {
		 getActivity().unregisterReceiver(m_BroadcastReceiver);
		super.onDestroy();
	}

	private void createSendMsgPop(){
		 LayoutInflater inflater = LayoutInflater.from(getActivity());
		 View popwindow = inflater.inflate(R.layout.sendmessagepop, null);
		 final EditText edtContent = (EditText) popwindow.findViewById(R.id.edtContent);
		 ImageButton send = (ImageButton) popwindow.findViewById(R.id.send);
		 sendCount = (TextView) popwindow.findViewById(R.id.sendCount);
		 edtContent.setText(smsContent);
		 sendMsgPop = new PopupWindow(popwindow, Constant.scWidth/2, Constant.scHeight*3/4);
		 send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendCount.setText("已发送:    0/"+phoneArr.size());
				sendMessage(edtContent, phoneArr,sendCount);
			}
		});
		 sendMsgPop.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				smsContent = edtContent.getText().toString();
			}
		});
		 sendMsgPop.setFocusable(true);
		 sendMsgPop.setBackgroundDrawable(new BitmapDrawable());
		 int[] location = new int[2];
		 sendCount.getLocationOnScreen(location); 
		 sendMsgPop.setAnimationStyle(R.style.PopupAnimation);//
		 sendMsgPop.showAtLocation(sendCount, Gravity.CENTER_HORIZONTAL, location[0], location[1]);
	 }
	 
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.sendMessage:
			if(phoneArr.size() != 0)
				if(Constant.isCanUseSim(getActivity()))
					createSendMsgPop();
			else
				Toast.makeText(getActivity(), "请选择客户", 0).show();
			break;
		case R.id.importContacts:
			Intent it = new Intent(getActivity(),ContactsActivity.class);
			getActivity().startActivity(it);
			break;
		}
	}
}
