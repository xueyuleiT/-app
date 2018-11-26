package com.assistant.fragment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
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
import android.widget.BaseAdapter;
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

import com.assistant.adapter.CallAdapter;
import com.assistant.adapter.GroupAdapter;
import com.assistant.adapter.LaoDaiXinAdapter;
import com.assistant.adapter.SmsInfoAdapter;
import com.assistant.model.CallModel;
import com.assistant.model.ConsumerModel;
import com.assistant.model.SmsContent;
import com.assistant.model.SmsInfo;
import com.assistant.model.TextMessage;
import com.assistant.service.PhoneReceiver;
import com.assistant.utils.Constant;
import com.assistant.utils.CurrentTime;
import com.assistant.utils.NetworkData;
import com.assistant.view.Home;
import com.assistant.view.Register;
import com.assistant.view.UserInfo;
import com.example.assistant.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OneFragment extends Fragment {

	ListView orderList;
	Button register;
	private boolean scrolling = false;
	int arrId = 0;
	TextView title;
	EditText phone;
	Button btnAdd,fastAdd;
	TaskAdapter tAdapter,orderAdapter;
	CallAdapter cAdapter;
	public static ArrayList<ConsumerModel> taskArr = new ArrayList<ConsumerModel>();
	ArrayList<ConsumerModel> orderArr = new ArrayList<ConsumerModel>();
	ArrayList<ConsumerModel> heimingdanArr = new ArrayList<ConsumerModel>();
	ArrayList<ConsumerModel> laoyezhuArr = new ArrayList<ConsumerModel>();
	ArrayList<CallModel> callArr = new ArrayList<CallModel>();;
//	private ProgressDialog pDia = null;
	
	AbstractWheel group;
	String Group[] = new String[]{"今日新增","客户排名", "今日任务","近期电话记录","新增待完善","黑名单客户","老业主"};
	
	private Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
				Toast.makeText(getActivity(), "该用户已被--"+((String)msg.obj)+"--注册", 0).show();
			}else if(msg.what == 2){
				phone.setText("");
			}else if(msg.what == 3){
				PhoneReceiver.dimissLoad();
				Constant.isNeedRefresh = false;
				cAdapter = new CallAdapter(getActivity(), callArr);
				orderList.setAdapter(cAdapter);
			}else if(msg.what == 4){
				title.setText("今日任务(网络异常)");
        		orderAdapter = new TaskAdapter(taskArr,getActivity());
    			orderList.setAdapter(orderAdapter);
    			PhoneReceiver.dimissLoad();
			}else if(msg.what == 5){
				title.setText("今日任务"+Constant.finishTask);
        		orderAdapter = new TaskAdapter(taskArr,getActivity());
    			orderList.setAdapter(orderAdapter);
    			PhoneReceiver.dimissLoad();
			}else if(msg.what == 6){
				initTaskData();
				title.setText("今日任务"+Constant.finishTask);
        		orderAdapter = new TaskAdapter(taskArr,getActivity());
    			orderList.setAdapter(orderAdapter);
    			PhoneReceiver.dimissLoad();
			}
		}
		
	};
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
		register_BroadCast();
	}
    public BroadcastReceiver m_BroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals("update")){
					initTaskData();
				}else if(intent.getAction().equals("updatetask")){
					orderAdapter = new TaskAdapter(taskArr,getActivity());
        			orderList.setAdapter(orderAdapter);
				}else if(intent.getAction().equals("updateall")){
					Home.getDate(getActivity(), fastAdd);
				}
		}
	};
	 private void register_BroadCast() {
			IntentFilter filter = new IntentFilter();
			filter.addAction("update");
			filter.addAction("updatetask");
			filter.addAction("updateall");
			filter.setPriority(Integer.MAX_VALUE);
			getActivity().registerReceiver(m_BroadcastReceiver, filter);
		}
	private void initTaskData() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 try {
			java.util.Date toDay = df.parse(CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:01");
			
			taskArr.clear();
			heimingdanArr.clear();
			laoyezhuArr.clear();
			long m = (24*60*60*1000);
			int dayNum = Integer.parseInt(Constant.dayNum);
			for (int i = 0; i < Constant.conLst.size(); i++) {
				System.out.println("lastCall = "+Constant.conLst.get(i).getLastCallTime());
				java.util.Date data = df.parse(Constant.conLst.get(i).getLastCallTime());
				 long l = toDay.getTime()-data.getTime();
				 long magin =l/m;
				 
				 
				 if(Constant.conLst.get(i).getKehuzu().equals("黑名单客户")){
					 heimingdanArr.add(Constant.conLst.get(i));
					 Constant.conLst.remove(i);
					 i--;
					 continue;
				 }
				 if(magin >= dayNum && l >= 0){
					 taskArr.add(Constant.conLst.get(i));
				 }
				 if(!Constant.conLst.get(i).getKehushuxing().equals("") && !Constant.conLst.get(i).getKehushuxing().equals("新客户")){
					 laoyezhuArr.add(Constant.conLst.get(i));
				 }
			}
			orderArr.clear();
			orderArr.addAll(Constant.conLst);
			Collections.sort(orderArr,comparator);
			orderAdapter = new TaskAdapter(orderArr,getActivity());
			orderList.setAdapter(orderAdapter);
			group.setBackgroundResource(R.drawable.rect);
			group.setCurrentItem(1);
		 } catch (ParseException e) {
			e.printStackTrace();
		}
		
	}

	
	Comparator<ConsumerModel> comparator = new Comparator<ConsumerModel>() {

		@Override
		public int compare(ConsumerModel con1, ConsumerModel con2) {

			return con2.getCallGrade() - con1.getCallGrade();
		}
	};
	
	public boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	private void initUI() {
		orderList = (ListView) getView().findViewById(R.id.orderList);
		group = (AbstractWheel) getView().findViewById(R.id.group);
		title = (TextView) getView().findViewById(R.id.title);
		btnAdd = (Button) getView().findViewById(R.id.addOne);
		phone = (EditText) getView().findViewById(R.id.phone);
		fastAdd = (Button) getView().findViewById(R.id.fastAddOne);
		
		group.setCyclic(false);
		group.setVisibleItems(5);
		group.setViewAdapter(new GroupAdapter(getActivity(),Group));
		
		
		fastAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String str = phone.getText().toString().trim();
				   if(str.length() == 11 && isNumeric(str)){
				    	  new Thread(new Runnable() {
							
							@Override
							public void run() {
								try {
						    		  TextMessage tMsg = NetworkData.posturl(Constant.ISREGISTER+"&phone="+str);
									if(tMsg != null && tMsg.getContent().equals("未注册")){
										  Intent it = new Intent(getActivity(),Register.class);
									      it.putExtra("type", "fastsave");
									      it.putExtra("phone", str);
									      startActivity(it);
									      myHandler.sendEmptyMessage(2);
									}else{
										Message msg = new Message();
										msg.what = 1;
										msg.obj = tMsg.getContent();
										myHandler.sendMessage(msg);
									}
								} catch (ClientProtocolException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}).start();
				    	  
				      }else{
				    	  Toast.makeText(getActivity(), "请输入11位电话", 0).show();
				      }
			}
		});
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String str = phone.getText().toString().trim();
				   if(str.length() == 11 && isNumeric(str)){
				    	  new Thread(new Runnable() {
							
							@Override
							public void run() {
								try {
						    		  TextMessage tMsg = NetworkData.posturl(Constant.ISREGISTER+"&phone="+str);
									if(tMsg != null && tMsg.getContent().equals("未注册")){
										  Intent it = new Intent(getActivity(),Register.class);
									      it.putExtra("type", "save");
									      it.putExtra("phone", str);
									      startActivity(it);
									      myHandler.sendEmptyMessage(2);
									}else{
										Message msg = new Message();
										msg.what = 1;
										msg.obj = tMsg.getContent();
										myHandler.sendMessage(msg);
									}
								} catch (ClientProtocolException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}).start();
				    	  
				      }else{
				    	  Toast.makeText(getActivity(), "请输入11位电话", 0).show();
				      }
			}
		});
		group.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
            }
        });
		group.addScrollingListener( new OnWheelScrollListener() {
            public void onScrollingStarted(AbstractWheel wheel) {
                scrolling = true;
                
            }
            public void onScrollingFinished(AbstractWheel wheel) {
                scrolling = false;
                switch(group.getCurrentItem()){
            	case 1:
            		title.setText("客户排名");
            		orderAdapter = new TaskAdapter(orderArr,getActivity());
        			orderList.setAdapter(orderAdapter);
            		break;
            	case 0:
            		title.setText("今日新增");
            		ArrayList<ConsumerModel> tempList = new ArrayList<ConsumerModel>();
            		int length = Constant.conLst.size();
            		String toDay = CurrentTime.getCurrentTime("yyyy-MM-dd");
            		for (int i = 0; i < length; i++) {
						if(!Constant.conLst.get(i).getTemp8().equals("") && Constant.conLst.get(i).getTemp8().substring(0, 10).equals(toDay)){
							tempList.add(Constant.conLst.get(i));
						}
					}
            		orderAdapter = new TaskAdapter(tempList,getActivity());
        			orderList.setAdapter(orderAdapter);
	            	break;
            	case 2:
            		PhoneReceiver.showLoad(getActivity(), fastAdd);
            		new Thread(new Runnable() {
						
						@Override
						public void run() {
							try {
								TextMessage t = NetworkData.posturl(Constant.GETFINISHTASK+"&phone="+Constant.USERPHONE);
								if(t != null && !t.getContent().equals("")){
									Constant.finishTask = t.getContent();
									if(!t.getFromid().equals(Constant.dayNum)){
										Constant.dayNum = t.getFromid();
										myHandler.sendEmptyMessage(6);
									}else
										myHandler.sendEmptyMessage(5);
								}else{
									myHandler.sendEmptyMessage(4);
								}
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}	
						}
					}).start();
	            	break;
            	case 3:
            		title.setText("近期电话记录");
            		if(Constant.isNeedRefresh){
            			PhoneReceiver.showLoad(getActivity(),fastAdd);
            			callArr.clear();
            			new Thread(new Runnable() {
							
							@Override
							public void run() {
								String arr = NetworkData.getMyModel(Constant.GETCALLRECORD+"&myphone="+Constant.USERPHONE);
								if(arr != null && !arr.equals("") && !arr.equals("error")){ 
									Gson gson = new Gson();
									callArr.addAll((Collection<? extends CallModel>) gson.fromJson(arr,
											new TypeToken<ArrayList<CallModel>>() {}.getType()));
								}
								myHandler.sendEmptyMessage(3);
							}
						}).start();
            		}else{
            			cAdapter = new CallAdapter(getActivity(), callArr);
        				orderList.setAdapter(cAdapter);
            		}
	            	break;
            	case 4:
            		title.setText("新增待完善");
            		ArrayList<ConsumerModel> tempList1 = new ArrayList<ConsumerModel>();
            		int length1 = Constant.conLst.size();
            		for (int i = 0; i < length1; i++) {
						if(!Constant.conLst.get(i).getCustomer_phone().equals("") && Constant.conLst.get(i).getKehuzu().equals("")){
							tempList1.add(Constant.conLst.get(i));
						}
					}
            		orderAdapter = new TaskAdapter(tempList1,getActivity());
        			orderList.setAdapter(orderAdapter);
            		break;
            	case 5:
            		title.setText("黑名单客户");
            		orderAdapter = new TaskAdapter(heimingdanArr,getActivity());
        			orderList.setAdapter(orderAdapter);
            		break;
            	case 6:
            		title.setText("老业主");
            		LaoDaiXinAdapter ldxAdapter = new LaoDaiXinAdapter(getActivity(),laoyezhuArr);
        			orderList.setAdapter(ldxAdapter);
            		break;
                }
            }
        });
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.onefragment, container, false);
	}

	class TaskAdapter extends BaseAdapter{
		String content = "";
		ArrayList<ConsumerModel> conArr;
		LayoutInflater mInflater;
		 PopupWindow sendMsgPop;
		public TaskAdapter(ArrayList<ConsumerModel> conArr,Context context){
			this.conArr = conArr;
			mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return conArr.size();
		}

		@Override
		public Object getItem(int position) {
			return conArr.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			final ConsumerModel con = conArr.get(position);
			if(convertView == null){
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.task_item, null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.phone = (TextView) convertView.findViewById(R.id.phone);
				holder.yusuan = (TextView) convertView.findViewById(R.id.yusuan);
				holder.yixiang = (TextView) convertView.findViewById(R.id.yixiang);
				holder.call = (ImageView) convertView.findViewById(R.id.call);
				holder.message = (ImageView) convertView.findViewById(R.id.message);
				holder.beizhu = (TextView) convertView.findViewById(R.id.beizhu);
				convertView.setTag(holder);
				
			}else
				holder = (ViewHolder) convertView.getTag();
			
			holder.name.setText(con.getCustomer_name());
			holder.phone.setText(con.getCustomer_phone());
			holder.yixiang.setText(con.getYixiang());
			holder.yusuan.setText(con.getYisuan());
			holder.beizhu.setText(con.getBeizhu());
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 Intent it = new Intent(getActivity(),UserInfo.class);
				      Bundle bd = new Bundle();
				      bd.putSerializable("consumer", con);
				      it.putExtras(bd);
				      startActivity(it);
				}
			});
			holder.call.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!con.getCustomer_phone().equals("") && con.getCustomer_phone().length() == 11){
						if(Constant.isCanUseSim(getActivity())){
							Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri  
				            .parse("tel:" + con.getCustomer_phone()));  
					        startActivity(dialIntent); 
						}
					}else{
						Toast.makeText(getActivity(), "号码不正确", 0).show();
					}
				}
			});
			holder.message.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!con.getCustomer_phone().equals("") && con.getCustomer_phone().length() == 11){
						if(Constant.isCanUseSim(getActivity())){
							ArrayList<String> phoneArr = new ArrayList<String>();
							phoneArr.add(con.getCustomer_phone());
							createSendMsgPop(con.getCustomer_phone(), con.getCustomer_name());
						}
					}else
						Toast.makeText(getActivity(), "号码不正确", 0).show();
				}
			});
			return convertView;
		}
		class ViewHolder{
			TextView name,phone,yusuan,yixiang,beizhu;
			ImageView call,message;
		}
		
			private void createSendMsgPop(final String phone,final String name){
				 if(phone == null){
					 Toast.makeText(getActivity(), "号码为空", 0).show();
				 }else{
				 View popwindow = mInflater.inflate(R.layout.chat_message, null);
				 final EditText edtContent = (EditText) popwindow.findViewById(R.id.edtSend);
				 Button send = (Button) popwindow.findViewById(R.id.sendMsg);
				 final ListView infoList = (ListView) popwindow.findViewById(R.id.msgList);
				 edtContent.setText(content);
				 
				 SmsContent sms = new SmsContent(getActivity(), Uri.parse("content://sms/"));
				 final ArrayList<SmsInfo> smsInfos = (ArrayList<SmsInfo>) sms.getOnePersonInfo(phone,name);
				 
				 final SmsInfoAdapter sAdapter = new SmsInfoAdapter(getActivity(), smsInfos);
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
		 
	}
	 @Override
	public void onDestroy() {
		 getActivity().unregisterReceiver(m_BroadcastReceiver);
		super.onDestroy();
	}
}
