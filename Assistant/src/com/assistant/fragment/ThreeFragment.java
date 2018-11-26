package com.assistant.fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import com.assistant.adapter.GroupAdapter;
import com.assistant.adapter.SmsInfoAdapter;
import com.assistant.model.ConsumerModel;
import com.assistant.model.SmsContent;
import com.assistant.model.SmsInfo;
import com.assistant.service.PhoneReceiver;
import com.assistant.utils.Constant;
import com.assistant.utils.CurrentTime;
import com.assistant.utils.Sort;
import com.assistant.view.Register;
import com.example.assistant.R;
import com.example.timeselect.JudgeDate;
import com.example.timeselect.ScreenInfo;
import com.example.timeselect.WheelMain;

public class ThreeFragment extends Fragment{
	 private boolean scrolling = false,tellPhoneScrolling = false;
	 private TextView name,phone,kehushuxing,yixiangyetai,yixiang,laoyezhuPhone;
	 private ImageView call,message,editer;
	 private String searchStr,content = "";
	 private EditText et_search;
	 boolean isSearch = false,isShow = false;
	 Builder dialog;
	 PopupWindow sendMsgPop;
	 AbstractWheel group,tellPhone;
	 ConsumerModel consumer;
	 ArrayList<ConsumerModel> contacts = new ArrayList<ConsumerModel>();
	 ArrayList<ConsumerModel> checkList = new ArrayList<ConsumerModel>();
	 int arrId = 1;
	 String Group[] = new String[]{
			"按意向筛选","我的客户", "按业态筛选","按户型筛选","按面积筛选","按认知渠道筛选","按资金实力筛选","按客户属性筛选","按客户组筛选"
			 ,"按预算筛选","带新老业主"};
	 private int mActiveContacts[] = new int[] {
	            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
	    };
	 public BroadcastReceiver m_BroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("update")) {// 已接受
				initListener();
			}
		}
	};
	 private int mActiveContact;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI(); 
		register_BroadCast();
	}
	 private void sendMessage(EditText edtContent,ArrayList<String> numberArr,TextView sendCount){
		 SmsManager smsManager = SmsManager.getDefault();
		 int size = numberArr.size();
		 String smsContent = edtContent.getText().toString();
		 for (int i = 0; i < size; i++) {
			 if(smsContent.length()>70){  
	             List<String> contents = smsManager.divideMessage(smsContent);  
	             for(String c:contents){  
	                 smsManager.sendTextMessage(numberArr.get(i), null, c, null, null);  
	             }  
	         }else{  
	             smsManager.sendTextMessage(numberArr.get(i), null, smsContent, null, null);  
	         }  
			 sendCount.setText("已发送:    "+(i+1)+"/"+size);
		}
		 edtContent.setText("");
	 }
	 
	 private void createSendMsgPop(final String phone,final String name){
		 LayoutInflater inflater = LayoutInflater.from(getActivity());
		 if(phone == null){
			 Toast.makeText(getActivity(), "号码为空", 0).show();
		 }else{
		 View popwindow = inflater.inflate(R.layout.chat_message, null);
		 final EditText edtContent = (EditText) popwindow.findViewById(R.id.edtSend);
		 Button send = (Button) popwindow.findViewById(R.id.sendMsg);
		 final ListView infoList = (ListView) popwindow.findViewById(R.id.msgList);
		 edtContent.setText(content);
		 
		 SmsContent sms = new SmsContent((getActivity()), Uri.parse("content://sms/"));
		 final ArrayList<SmsInfo> smsInfos = (ArrayList<SmsInfo>) sms.getOnePersonInfo(phone,name);
		 
		 final SmsInfoAdapter sAdapter = new SmsInfoAdapter(getActivity(), smsInfos);
		 infoList.setAdapter(sAdapter);
		 infoList.setSelection(smsInfos.size());
		 
		 sendMsgPop = new PopupWindow(popwindow, Constant.scWidth/2, Constant.scHeight*3/4);
		 send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!phone.equals("") && phone.length() == 11){
					if(Constant.isCanUseSim(getActivity())){
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
//					writeMsg(smsContent, phone, new ContentValues(), Uri.parse("content://sms/"));
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
					
					
					getActivity().sendBroadcast(new Intent("RECIEVE_SMS_ACTION"));
					}
					}
				}else
					Toast.makeText(getActivity(), "号码不正确", 0).show();
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
	 
	 
	// 把短信写入数据库
	public void writeMsg(String content, String phone, ContentValues values,
			Uri SMS_URI) {

		try {
			// 发送时间
			values.put("date", System.currentTimeMillis());
			// 阅读状态
			values.put("read", 0);
			// 类型：1为收，2为发
			values.put("type", 2);
			// 发送号码
			values.put("address", phone);
			// 发送内容
			values.put("body", content);
			// 插入短信库
			getActivity().getContentResolver().insert(SMS_URI, values);
		} catch (Exception e) {
		}
	}
	 
	private void initListener() {
		et_search.addTextChangedListener(new EdtTextWatch());
		call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!phone.getText().toString().equals("") && phone.getText().length() == 11){
					if(Constant.isCanUseSim(getActivity())){
					Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri  
		            .parse("tel:" + phone.getText().toString()));  
			        startActivity(dialIntent); 
					}
				}else{
					Toast.makeText(getActivity(), "号码不正确", 0).show();
				}
			}
		});
		message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!phone.getText().toString().equals("") && phone.getText().length() == 11){
					if(Constant.isCanUseSim(getActivity()))
						createSendMsgPop(phone.getText().toString(),name.getText().toString());
				}else
					Toast.makeText(getActivity(), "号码不正确", 0).show();
			}
		});
		editer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(!phone.getText().toString().equals("") && phone.getText().length() == 11){
					Intent it = new Intent(getActivity(), Register.class);
					Bundle bd = new Bundle();
					bd.putString("type", "editer");
					bd.putSerializable("consumer", consumer);
					it.putExtras(bd);
					startActivity(it);
				}else
					Toast.makeText(getActivity(), "号码不正确", 0).show();
			}
		});
		group.addChangingListener(new OnWheelChangedListener() {
	            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
	            	if (!scrolling) {
	            	switch(group.getCurrentItem()){
	            	case 1:
	            		arrId = 1;
	            		break;
		            	
	            	}
//	            		if(arrId != 1)
//	            		 new AlertDialog.Builder(getActivity()) // build AlertDialog  
//	            	        .setTitle("选择意向")
//	            	        .setItems(arrId, new DialogInterface.OnClickListener() { //content  
//	            	            @Override  
//	            	            public void onClick(DialogInterface dialog, int which) {  
//	            	           	String item = getResources().getStringArray(arrId)[which];
//	            	           	 int length = Constant.conLst.size();
//	            	           	 int caseId = group.getCurrentItem();
//	            	           	contacts.clear();
//	            	          for (int i = 0; i < length; i++) {
//								if(caseId == 0){
//									if(item.equals(Constant.conLst.get(i).getYixiang())){
//										contacts.add(Constant.conLst.get(i));
//									}
//								}else if(caseId == 2){
//									if(item.equals(Constant.conLst.get(i).getYixiangyetai())){
//										contacts.add(Constant.conLst.get(i));
//									}
//								}else if(caseId == 3){
//									if(item.equals(Constant.conLst.get(i).getYixianghuxing())){
//										contacts.add(Constant.conLst.get(i));
//									}
//								}else if(caseId == 4){
//									if(item.equals(Constant.conLst.get(i).getYixiangmianji())){
//										contacts.add(Constant.conLst.get(i));
//									}
//								}else if(caseId == 5){
//									if(item.equals(Constant.conLst.get(i).getRenzhiqudao())){
//										contacts.add(Constant.conLst.get(i));
//									}
//								}else if(caseId == 6){
//									if(item.equals(Constant.conLst.get(i).getZijinshili())){
//										contacts.add(Constant.conLst.get(i));
//									}
//								}else if(caseId == 7){
//									if(item.equals(Constant.conLst.get(i).getKehushuxing())){
//										contacts.add(Constant.conLst.get(i));
//									}
//								}else if(caseId == 8){
//									if(item.equals(Constant.conLst.get(i).getKehuzu())){
//										contacts.add(Constant.conLst.get(i));
//									}
//								}else if(caseId == 9){
//									if(item.equals(Constant.conLst.get(i).getYisuan())){
//										contacts.add(Constant.conLst.get(i));
//									}
//								}
//							}
//	            	          if(!isSearch){
//	            	          if(contacts.size() < 5){
//	            	        	  if(contacts.size() == 0){
//	            	        		  ConsumerModel con = new ConsumerModel();
//	            	        		  con.setBeizhu("空");
//	            	        		  con.setCustomer_name("空");
//	            	        		  contacts.add(con);
//	            	        	  }
//	            	        	  tellPhone.setVisibleItems(contacts.size()); 
//	            	          }else
//	            	        	  tellPhone.setVisibleItems(5); 
//	            	          updateContacts(tellPhone, contacts, caseId);
//	            	          consumer = contacts.get(0);
//	            	          initConsumer(consumer);
//	            	            }  
//	            	            }
//	            	        })  
//	            	        .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
//	            	              
//	            	            @Override  
//	            	            public void onClick(DialogInterface dialog, int which) {  
//	            	                dialog.dismiss(); //关闭alertDialog  
//	            	            }  
//	            	        }).show();
//	            		else{
	            			if(Constant.conLst.size() < 5){
	            	        	  if(Constant.conLst.size() == 0){
	            	        		  ConsumerModel con = new ConsumerModel();
	            	        		  con.setCustomer_name("空");
	            	        		  Constant.conLst.add(con);
	            	        	  }
	            	        	  tellPhone.setVisibleItems(Constant.conLst.size()); 
	            	          }else
	            	        	  tellPhone.setVisibleItems(5); 
	  	                	 updateContacts(tellPhone, Constant.conLst, 1);
	  	                	 consumer = Constant.conLst.get(0);
	  	        	         initConsumer(consumer);
//		                }
	            		
	                }
	            }
	        });
	        
		tellPhone.addScrollingListener(new OnWheelScrollListener() {
			
			@Override
			public void onScrollingStarted(AbstractWheel wheel) {
				tellPhoneScrolling = true;
			}
			
			@Override
			public void onScrollingFinished(AbstractWheel wheel) {
				tellPhoneScrolling = false;
				if (!isSearch) {
					if (mActiveContact == 1) {
						consumer = Constant.conLst.get(tellPhone.getCurrentItem());
						initConsumer(consumer);
					} else {
						consumer = contacts.get(tellPhone.getCurrentItem());
						initConsumer(consumer);
					}
				} else {
					consumer = checkList.get(tellPhone.getCurrentItem());
					initConsumer(consumer);
				}
			}
		});
		tellPhone.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				 if (!tellPhoneScrolling) {
					 if(!isSearch){
					 if(mActiveContact == 1){
						 consumer = Constant.conLst.get(tellPhone.getCurrentItem());
			             initConsumer(consumer);
					}else{
		                consumer = contacts.get(tellPhone.getCurrentItem());
		                initConsumer(consumer);
					}
					 }else{
			                consumer = checkList.get(tellPhone.getCurrentItem());
			                initConsumer(consumer);
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
	            	case 10:
	            		arrId = 10;
		            	break;
	            	}
	            	if(arrId == 10){
	            		isShow = true;
	            		showTimeSelect();
	            	}
	            	else if(arrId != 1){
	                	isShow = true;
	               dialog.setTitle("选择")
        	        .setItems(arrId, new DialogInterface.OnClickListener() { //content  
        	            @Override  
        	            public void onClick(DialogInterface dialog, int which) {
        	            PhoneReceiver.showLoad(getActivity(), call);
        	           	String item = getResources().getStringArray(arrId)[which];
        	           	 int length = Constant.conLst.size();
        	           	 int caseId = group.getCurrentItem();
        	           	 contacts.clear();
        	          for (int i = 0; i < length; i++) {
        	        	  if(caseId == 0){
								if(item.equals(Constant.conLst.get(i).getYixiang())){
									contacts.add(Constant.conLst.get(i));
								}
							}else if(caseId == 2){
								if(item.equals(Constant.conLst.get(i).getYixiangyetai())){
									contacts.add(Constant.conLst.get(i));
								}
							}else if(caseId == 3){
								if(item.equals(Constant.conLst.get(i).getYixianghuxing())){
									contacts.add(Constant.conLst.get(i));
								}
							}else if(caseId == 4){
								if(item.equals(Constant.conLst.get(i).getYixiangmianji())){
									contacts.add(Constant.conLst.get(i));
								}
							}else if(caseId == 5){
								if(item.equals(Constant.conLst.get(i).getRenzhiqudao())){
									contacts.add(Constant.conLst.get(i));
								}
							}else if(caseId == 6){
								if(item.equals(Constant.conLst.get(i).getZijinshili())){
									contacts.add(Constant.conLst.get(i));
								}
							}else if(caseId == 7){
								if(item.equals(Constant.conLst.get(i).getKehushuxing())){
									contacts.add(Constant.conLst.get(i));
								}
							}else if(caseId == 8){
								if(item.equals(Constant.conLst.get(i).getKehuzu())){
									contacts.add(Constant.conLst.get(i));
								}
							}else if(caseId == 9){
								if(item.equals(Constant.conLst.get(i).getYisuan())){
									contacts.add(Constant.conLst.get(i));
								}
							}
					}
        	          if(!isSearch){
        	          if(contacts.size() < 5){
        	        	  if(contacts.size() == 0){
        	        		  ConsumerModel con = new ConsumerModel();
        	        		  con.setCustomer_name("空");
        	        		  contacts.add(con);
        	        	  }
        	        	  tellPhone.setVisibleItems(contacts.size()); 
        	          }else
        	        	  tellPhone.setVisibleItems(5); 
        	          updateContacts(tellPhone, contacts, caseId);
        	          consumer = contacts.get(0);
        	          initConsumer(consumer);
        	          
        	            }  
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
	                }
	                else{
	                	if(Constant.conLst.size() < 5){
          	        	  if(Constant.conLst.size() == 0){
          	        		  ConsumerModel con = new ConsumerModel();
          	        		  con.setCustomer_name("空");
          	        		  Constant.conLst.add(con);
          	        	  }
          	        	  tellPhone.setVisibleItems(Constant.conLst.size()); 
          	          }else
          	        	  tellPhone.setVisibleItems(5); 
	                	 updateContacts(tellPhone, Constant.conLst, 1);
	                	 consumer = Constant.conLst.get(0);
	        	         initConsumer(consumer);
	                }
	            }
	            }
	        });

		group.setCurrentItem(1);
	}
	

	private void showTimeSelect(){
		
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		
		LayoutInflater inflater=LayoutInflater.from(getActivity());
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		
		ScreenInfo screenInfo = new ScreenInfo(getActivity());
		final WheelMain wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		Calendar calendar = Calendar.getInstance();
		if(JudgeDate.isDate(CurrentTime.getCurrentTime("yyyy-MM"), "yyyy-MM")){
			try {
				calendar.setTime(dateFormat.parse(CurrentTime.getCurrentTime("yyyy-MM")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year,month,day);
		new AlertDialog.Builder(getActivity())
		.setTitle("选择日期")
		.setView(timepickerview)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PhoneReceiver.showLoad(getActivity(), call);
				contacts.clear();
				String time = wheelMain.getTime();
				String[] arr = time.split("-");
				if(arr[2].length() == 1){
					arr[2] = "0"+arr[2];
				}
				
				if(arr[1].length() == 1){
					arr[1] = "0"+arr[1];
				}
				time = arr[0]+"-"+arr[1]+"-"+arr[2];
				
				int size = Constant.conLst.size();
				for (int i = 0; i < size; i++) {
					ConsumerModel model = Constant.conLst.get(i);
					System.out.println("laoyezhuphone = "+model.getLaoyezhuphone() +"    time = "+time +"  date = "+model.getDatatime());
					if(!model.getLaoyezhuphone().equals("") && comparesTime(model.getDatatime(), time, dateFormat)){
						for (int j = 0; j < size; j++) {
							if(Constant.conLst.get(j).getCustomer_phone().equals(model.getLaoyezhuphone()) && !contacts.contains(Constant.conLst.get(j))){
								contacts.add(Constant.conLst.get(j));
								break;
							}
						}
					}
				}
				if(contacts.size() == 0){
					ConsumerModel model = new ConsumerModel();
					model.setCustomer_name("空");
					contacts.add(model);
				}
				updateContacts(tellPhone, contacts, 10);
				initConsumer(contacts.get(0));
				isShow = false;
				PhoneReceiver.dimissLoad();
			}
		})
		.setNegativeButton("所有", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PhoneReceiver.showLoad(getActivity(), call);
				contacts.clear();
				int size = Constant.conLst.size();
				for (int i = 0; i < size; i++) {
					ConsumerModel model = Constant.conLst.get(i);
					if(!model.getLaoyezhuphone().equals("")){
						for (int j = 0; j < size; j++) {
							if(Constant.conLst.get(j).getCustomer_phone().equals(model.getLaoyezhuphone())
									&& !contacts.contains(Constant.conLst.get(j))){
								contacts.add(Constant.conLst.get(j));
								break;
							}
						}
					}
				}
				
				if(contacts.size() == 0){
					ConsumerModel model = new ConsumerModel();
					model.setCustomer_name("空");
					contacts.add(model);
				}
				updateContacts(tellPhone, contacts, 10);
				initConsumer(contacts.get(0));
				isShow = false;
				PhoneReceiver.dimissLoad();
			}
		})
		.show();
	}
	
	private boolean comparesTime(String t1,String t2,DateFormat dateFormat){
		 long days = 0;
		 long diff = 0;
		try
		{
		    Date d1 = dateFormat.parse(t1);
		    Date d2 = dateFormat.parse(t2);
		    diff = d1.getTime() - d2.getTime();
		    System.out.println("diff = "+diff +"  d1.getTime() = "+d1.getTime()+"  d2.getTime() = "+d2.getTime());
		    days = diff / (1000 * 60 * 60 * 24);
		}
		catch (Exception e)
		{
		}
		 return days >= 0?true:false;
	}
	private void initConsumer(ConsumerModel con){
		name.setText(con.getCustomer_name());
		phone.setText(con.getCustomer_phone());
		kehushuxing.setText(con.getKehushuxing());
		yixiangyetai.setText(con.getYixiangyetai());
		kehushuxing.setText(con.getKehushuxing());
		yixiang.setText(con.getYixiang());
		laoyezhuPhone.setText(con.getLaoyezhuphone());
	}
	
	 private void register_BroadCast() {
			IntentFilter filter = new IntentFilter();
			filter.addAction("update");
			filter.setPriority(Integer.MAX_VALUE);
			getActivity().registerReceiver(m_BroadcastReceiver, filter);
		}
	private void initUI(){
		group = (AbstractWheel) getView().findViewById(R.id.group);
		tellPhone = (AbstractWheel) getView().findViewById(R.id.tellPhone);
		name = (TextView) getView().findViewById(R.id.name);
		phone = (TextView) getView().findViewById(R.id.phone);
		kehushuxing = (TextView) getView().findViewById(R.id.kehushuxing);
		yixiangyetai = (TextView) getView().findViewById(R.id.yixiangyetai);
		yixiang = (TextView) getView().findViewById(R.id.yixiang);
		call = (ImageView) getView().findViewById(R.id.call);
		message = (ImageView) getView().findViewById(R.id.message);
		editer = (ImageView) getView().findViewById(R.id.editer);
		et_search = (EditText) getView().findViewById(R.id.edtSearch);
		laoyezhuPhone = (TextView) getView().findViewById(R.id.laoyezhuPhone);
		
		group.setCyclic(false);
		tellPhone.setCyclic(false);
		group.setVisibleItems(5);
		group.setViewAdapter(new GroupAdapter(getActivity(),Group));
		tellPhone.setVisibleItems(5);
		
		
		dialog = new AlertDialog.Builder(getActivity());
	}

    private void updateContacts(AbstractWheel tellPhone, ArrayList<ConsumerModel> contacts, int index) {
    	mActiveContact = index;
    	ContactsAdapter adapter = new ContactsAdapter(getActivity(), contacts);
        adapter.setTextSize(22);
        tellPhone.setViewAdapter(adapter);
        tellPhone.setCurrentItem(0);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.threefragment, container, false);
	}
	
	 private class ContactsAdapter extends AbstractWheelTextAdapter {
	        // Countries names
		 ArrayList<ConsumerModel> contactsList;
	        // Countries flags
	        
	        /**
	         * Constructor
	         */
	        protected ContactsAdapter(Context context,ArrayList<ConsumerModel> contactsList) {
	            super(context, R.layout.contacts_item, NO_RESOURCE);
	            setItemTextResource(R.id.contacts_name);
	            this.contactsList = contactsList;
	        }

	        @Override
	        public View getItem(int index, View cachedView, ViewGroup parent) {
	            View view = super.getItem(index, cachedView, parent);
	            ConsumerModel con= contactsList.get(index);
	            ImageView img = (ImageView) view.findViewById(R.id.imgHead);
	            if(index == 0){
	            	img.setVisibility(View.VISIBLE);
	            }else{
	            	img.setVisibility(View.GONE);
	            }
	            TextView textView = (TextView) view.findViewById(R.id.contacts_name);
	            textView.setText(con.getCustomer_name()+"-"+con.getCustomer_phone());
	            return view;
	        }
	        
	        @Override
	        public int getItemsCount() {
	            return contactsList.size();
	        }
	        
	        @Override
	        protected CharSequence getItemText(int index) {
	            return contactsList.get(index).getCustomer_name();
	        }
	    }
		class EdtTextWatch implements TextWatcher {

			@Override
			public void afterTextChanged(Editable s) {
				searchStr = et_search.getText().toString();
				if(searchStr.length() != 0 && isNumeric(searchStr)){
					isSearch = true;
					checkList.clear();
					int size = Constant.conLst.size();
					for (int i = 0; i < size; i++) {
						if(Constant.conLst.get(i).getCustomer_phone().contains(searchStr)){
							checkList.add(Constant.conLst.get(i));
						}
					}
					if(checkList.size() < 5){
	      	        	  if(checkList.size() == 0){
	      	        		  ConsumerModel con = new ConsumerModel();
	      	        		  con.setCustomer_name("空");
	      	        		checkList.add(con);
	      	        	  }
	      	        	  tellPhone.setVisibleItems(checkList.size()); 
	      	          }else
	      	        	  tellPhone.setVisibleItems(5); 
	      	          updateContacts(tellPhone, checkList, mActiveContact);
	      	          consumer = checkList.get(0);
	  	              initConsumer(consumer);
				}
				else if (searchStr.length() != 0) {
					isSearch = true;
					checkList.clear();
					checkList.addAll(Constant.conLst);
					checkSearchStr(searchStr);
					if(checkList.size() < 5){
      	        	  if(checkList.size() == 0){
      	        		  ConsumerModel con = new ConsumerModel();
      	        		  con.setCustomer_name("空");
      	        		checkList.add(con);
      	        	  }
      	        	  tellPhone.setVisibleItems(checkList.size()); 
      	          }else
      	        	  tellPhone.setVisibleItems(5); 
      	          updateContacts(tellPhone, checkList, mActiveContact);
      	          consumer = checkList.get(0);
  	              initConsumer(consumer);
			} else if(searchStr.length() == 0){
				isSearch = false;
				if(mActiveContact == 1){
					if(Constant.conLst.size() < 5){
        	        	  if(Constant.conLst.size() == 0){
        	        		  ConsumerModel con = new ConsumerModel();
        	        		  con.setCustomer_name("空");
        	        		  Constant.conLst.add(con);
        	        	  }
        	        	  tellPhone.setVisibleItems(Constant.conLst.size()); 
        	          }else
        	        	  tellPhone.setVisibleItems(5); 
	                	 updateContacts(tellPhone, Constant.conLst, 1);
	                	 consumer = Constant.conLst.get(0);
	        	         initConsumer(consumer);
	                }else{
	                	if(contacts.size() < 5){
	        	        	  if(contacts.size() == 0){
	        	        		  ConsumerModel con = new ConsumerModel();
	        	        		  con.setCustomer_name("空");
	        	        		  contacts.add(con);
	        	        	  }
	        	        	  tellPhone.setVisibleItems(contacts.size()); 
	        	          }else
	        	        	  tellPhone.setVisibleItems(5); 
	        	          updateContacts(tellPhone, contacts, mActiveContact);
	        	          consumer = contacts.get(0);
	        	          initConsumer(consumer);
	                }
				
				}
				}
				
				

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

		}
		public boolean isNumeric(String str) {
			for (int i = 0; i < str.length(); i++) {
				if (!Character.isDigit(str.charAt(i))) {
					return false;
				}
			}
			return true;
		}
		public void checkSearchStr(String search) {

			Sort mSort = new Sort();
			String tempSearch;
			String tempList;
			String newDataChar = null;
			String checkArrayListItem = null;
			if (search.matches("[a-zA-Z]+")) {
				int length = search.length();
				int size = checkList.size();
				for (int i = 0; i < length; i++) {
					for (int j = 0; j < size; j++) {
						checkArrayListItem = checkList.get(j).getCustomer_name();
						if (!checkArrayListItem.matches("[a-zA-Z]+")) {
							newDataChar = mSort
									.getAllPinYinHeadChar(checkArrayListItem);
						} else {
							newDataChar = checkArrayListItem;
						}
						tempSearch = String.valueOf(search.charAt(i)).toUpperCase();
						tempList = String.valueOf(newDataChar.charAt(i)).toUpperCase();
					
						if (!(tempSearch.equals(tempList))) {
							checkList.remove(j);
							size--;
							newDataChar = null;
							j--;
						}
					}
				}
			} else if (search.matches("[\u4e00-\u9fa5]+")) {

				for (int j = 0; j < checkList.size(); j++) {
					if (!checkList.get(j).getCustomer_name().contains(search)) {
						checkList.remove(j);
						j--;
					}
				}
			} else {
				search = String.valueOf(search.charAt(0));
				for (int j = 0; j < checkList.size(); j++) {
					if (!checkList.get(j).getCustomer_name().contains(search)){
						checkList.remove(j);
						j--;
					}
				}
			}
		}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(m_BroadcastReceiver);
		super.onDestroy();
	}
}
