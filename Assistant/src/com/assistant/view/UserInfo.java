package com.assistant.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
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

import com.assistant.adapter.SmsInfoAdapter;
import com.assistant.model.BeizhuModel;
import com.assistant.model.ConsumerModel;
import com.assistant.model.SmsContent;
import com.assistant.model.SmsInfo;
import com.assistant.model.TextMessage;
import com.assistant.service.PhoneReceiver;
import com.assistant.utils.Constant;
import com.assistant.utils.CurrentTime;
import com.assistant.utils.NetworkData;
import com.example.assistant.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserInfo extends Activity implements OnClickListener{
	private TextView chengjiaojine,name,phone,xingbie,edtYisuan,edtYixiang,edtYixiangyetai,edtYonghuzu,edtKehushuxing,edtZijinshili
	,edtRenzhiqudao,edtYixiangmianji,edtYixianghuxing,shengfen,quyu,louhao,shenfenzheng,beizhu;
	private ImageButton imgCall,imgEdit,imgMessage,imgMore,delete;
	private ConsumerModel consumer;
	String content = "";
	PopupWindow sendMsgPop, beizhuPop;
	LayoutInflater mInflater;
	ArrayList<BeizhuModel> beizhuArr = new ArrayList<BeizhuModel>();
	View popwindow, showBar;
	private ProgressDialog pDia = null;
	ListView beizhuList;
	BeizhuAdapter bAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		setContentView(R.layout.userinfo);
		consumer = (ConsumerModel) getIntent().getExtras().get("consumer");
		initUI();
		initDate();
		initListener();
	}
	private void initListener() {
		imgCall.setOnClickListener(this);
		imgEdit.setOnClickListener(this);
		imgMessage.setOnClickListener(this);
		imgMore.setOnClickListener(this);
		delete.setOnClickListener(this);
	}
	private void initDate() {
		mInflater = LayoutInflater.from(this);
		name.setText(consumer.getCustomer_name());
		phone.setText(consumer.getCustomer_phone());
		edtYisuan.setText(consumer.getYisuan());
		xingbie.setText(consumer.getXingbie());
		edtYixiang.setText(consumer.getYixiang());
		edtYixiangyetai.setText(consumer.getYixiangyetai());
		edtYonghuzu.setText(consumer.getKehuzu());
		edtKehushuxing.setText(consumer.getKehushuxing());
		edtZijinshili.setText(consumer.getZijinshili());
		edtRenzhiqudao.setText(consumer.getRenzhiqudao());
		edtYixiangmianji.setText(consumer.getYixiangmianji());
		edtYixianghuxing.setText(consumer.getYixianghuxing());
		shengfen.setText(consumer.getDizhi());
		quyu.setText(consumer.getQuyu());
		louhao.setText(consumer.getLouhao());
		shenfenzheng.setText(consumer.getShenfenzheng());
		chengjiaojine.setText(consumer.getChengjiaojine());
		beizhu.setText(consumer.getBeizhu());
	}
	private void initUI() {
		name = (TextView) findViewById(R.id.name);
		phone = (TextView) findViewById(R.id.phone);
		xingbie = (TextView) findViewById(R.id.xingbie);
		edtYisuan = (TextView) findViewById(R.id.yisuan);
		edtYixiang = (TextView) findViewById(R.id.yixiang);
		edtYixiangyetai = (TextView) findViewById(R.id.yixiangyetai);
		edtYonghuzu = (TextView) findViewById(R.id.yonghuzu);
		edtKehushuxing = (TextView) findViewById(R.id.kehushuxing);
		edtZijinshili = (TextView) findViewById(R.id.zijinshili);
		edtRenzhiqudao = (TextView) findViewById(R.id.renzhiqudao);
		edtYixiangmianji = (TextView) findViewById(R.id.yixiangmianji);
		edtYixianghuxing = (TextView) findViewById(R.id.yixianghuxing);
		chengjiaojine = (TextView) findViewById(R.id.chengjiaojine);
		shengfen = (TextView) findViewById(R.id.shengfen);
		quyu = (TextView) findViewById(R.id.quyu);
		louhao = (TextView) findViewById(R.id.louhao);
		beizhu = (TextView) findViewById(R.id.beizhu);
		shenfenzheng = (TextView) findViewById(R.id.shenfenzheng);
		imgCall = (ImageButton) findViewById(R.id.call);
		imgEdit = (ImageButton) findViewById(R.id.edit);
		imgMessage = (ImageButton) findViewById(R.id.message);
		imgMore =  (ImageButton) findViewById(R.id.imgMore);
		delete = (ImageButton) findViewById(R.id.delete);
		
		pDia = new ProgressDialog(this);
		pDia.setMessage("正在删除。。。");
		pDia.setCancelable(false);
		pDia.setCanceledOnTouchOutside(false);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.call:
			if(!consumer.getCustomer_phone().equals("") && consumer.getCustomer_phone().length() == 11){
		        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri  
	            .parse("tel:" + consumer.getCustomer_phone()));  
		        startActivity(dialIntent); 
			}else{
				Toast.makeText(this, "号码不正确", 0).show();
			}
			break;
		case R.id.message:
			createSendMsgPop(consumer.getCustomer_phone(),consumer.getCustomer_name());
			break;
		case R.id.edit:
			Intent it = new Intent(this,Register.class);
			it.putExtra("type", "editer");
			it.putExtra("consumer", consumer);
			startActivity(it);
			finish();
			break;
		case R.id.imgMore:
			if(!consumer.getBeizhu().equals("")){
				createBeizhuPop();
			}else{
				Toast.makeText(UserInfo.this, "暂无备注", 0).show();
			}
			break;
		case R.id.delete:
			final EditText edt = new EditText(this);
			new AlertDialog.Builder(UserInfo.this)
			.setTitle("安全密码")
			.setView(edt)
			.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(edt.getText().toString().equals(Constant.SAFEPWD)){
						
						try {
							InputMethodManager im = (InputMethodManager) UserInfo.this
									.getSystemService(Activity.INPUT_METHOD_SERVICE);
							IBinder windowToken = edt.getWindowToken();
							if (windowToken != null) {
								im.hideSoftInputFromWindow(windowToken, 0);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						PhoneReceiver.showLoad(UserInfo.this, delete);
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								try {
									TextMessage t = NetworkData.posturl(Constant.UNREGISTER+"&phone="+consumer.getCustomer_phone());
									if(t != null && !t.getContent().equals("-1")){
										myHandler.sendEmptyMessage(2);
									}else{
										myHandler.sendEmptyMessage(3);
									}
								} catch (ClientProtocolException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}).start();
					}else
						myHandler.sendEmptyMessage(4);
					dialog.cancel();
					
				}
			}).show();
			break;
		}
	}
	
	Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				showBar.setVisibility(View.GONE);
				 if(beizhuArr.size() > 0){
					 bAdapter = new BeizhuAdapter(beizhuArr);
					 beizhuList.setAdapter(bAdapter);
				 }
				break;
			case 1:
				Toast.makeText(UserInfo.this, "", 0).show();
				break;
			case 2:
				Toast.makeText(UserInfo.this, "删除成功", 0).show();
				Home.getDate(UserInfo.this,chengjiaojine);
				PhoneReceiver.dimissLoad();
				finish();
				break;
			case 3:
				Toast.makeText(UserInfo.this, "删除失败", 0).show();
				PhoneReceiver.dimissLoad();
				break;
			case 4:
				Toast.makeText(UserInfo.this, "安全密码错误", 0).show();
				break;
			}
		}
		
	};
	private void getBiezhu(final ArrayList<BeizhuModel> beizhuArr, View showBar){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				String arr = NetworkData.getMyModel(Constant.GETALLBEIZHU+"&phone="+consumer.getCustomer_phone());
				System.out.println("arr = "+arr);
				if(arr != null && !arr.equals("") && !arr.equals("error")){
					Gson gson = new Gson();
					beizhuArr.clear();
					beizhuArr.addAll((Collection<? extends BeizhuModel>) gson.fromJson(arr,
							new TypeToken<ArrayList<BeizhuModel>>() {}.getType()));
				}
				myHandler.sendEmptyMessage(0);
			}
		});
		t.start();
		
	}
	
	private void createBeizhuPop(){
		if(popwindow == null){
		 popwindow = mInflater.inflate(R.layout.beizhupop, null);
		 showBar = popwindow.findViewById(R.id.gressBar);
		 beizhuList = (ListView) popwindow.findViewById(R.id.beizhuList);
		 beizhuPop = new PopupWindow(popwindow, Constant.scWidth/2, Constant.scHeight*3/4);
		 beizhuPop.setFocusable(true);
		 beizhuPop.setBackgroundDrawable(new BitmapDrawable());
		}
		 int[] location = new int[2];
		 beizhuList.getLocationOnScreen(location); 
		 beizhuPop.setAnimationStyle(R.style.PopupAnimation);
		 beizhuPop.showAtLocation(beizhuList, Gravity.CENTER_HORIZONTAL, location[0], location[1]);
		 getBiezhu(beizhuArr,showBar);
	}
	class BeizhuAdapter extends BaseAdapter{
		ArrayList<BeizhuModel> consuList;
		LayoutInflater mInflater;
		public BeizhuAdapter(ArrayList<BeizhuModel> consuList){
			this.consuList = consuList;
			mInflater = LayoutInflater.from(UserInfo.this);
		}
		
		@Override
		public int getCount() {
			return consuList.size();
		}

		@Override
		public Object getItem(int position) {
			return consuList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final BeizhuModel model = consuList.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.beizhu_item, null);
				holder.beizhu = (TextView) convertView.findViewById(R.id.beizhu);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				convertView.setTag(holder);
			}else
				holder = (ViewHolder) convertView.getTag();
			holder.beizhu.setText(model.getBeizhu());
			holder.time.setText(model.getTime());
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent it = new Intent(UserInfo.this,ShowBeizhu.class);
					it.putExtra("beizhu", model.getBeizhu());
					startActivity(it);
				}
			});
			return convertView;
		}
		class ViewHolder{
			TextView time,beizhu;
		}
	}
	 private void createSendMsgPop(final String phone,final String name){
		 if(phone == null){
			 Toast.makeText(UserInfo.this, "号码为空", 0).show();
		 }else{
		 View popwindow = mInflater.inflate(R.layout.chat_message, null);
		 final EditText edtContent = (EditText) popwindow.findViewById(R.id.edtSend);
		 Button send = (Button) popwindow.findViewById(R.id.sendMsg);
		 final ListView infoList = (ListView) popwindow.findViewById(R.id.msgList);
		 edtContent.setText(content);
		 
		 SmsContent sms = new SmsContent(this, Uri.parse("content://sms/"));
		 final ArrayList<SmsInfo> smsInfos = (ArrayList<SmsInfo>) sms.getOnePersonInfo(phone,name);
		 
		 final SmsInfoAdapter sAdapter = new SmsInfoAdapter(this, smsInfos);
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
					}
				}else
					Toast.makeText(UserInfo.this, "号码不正确", 0).show();
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
