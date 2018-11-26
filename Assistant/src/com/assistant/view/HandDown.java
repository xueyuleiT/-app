package com.assistant.view;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.assistant.model.ConsumerModel;
import com.assistant.model.TextMessage;
import com.assistant.service.PhoneReceiver;
import com.assistant.utils.Constant;
import com.assistant.utils.CurrentTime;
import com.assistant.utils.NetworkData;
import com.example.assistant.R;

public class HandDown extends Activity implements OnClickListener{

	
	Button edit,fastSave;
	EditText beizhu;
	TextView name,phone,quyu,fangyuan,jine,huxing;
	ConsumerModel conModel;
	String stoptime = "",callTime = "";
//	private ProgressDialog pDia = null;
	private Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			PhoneReceiver.dimissLoad();
			if(msg.what == 1){
				Toast.makeText(HandDown.this, "更新失败 请检查网络", 0).show();
			}else if(msg.what == 2){
				Toast.makeText(HandDown.this, "更新成功", 0).show();
				Home.getDate(HandDown.this,fastSave);
				finish();
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hand_down);
		conModel = (ConsumerModel) getIntent().getExtras().get("consumer");
		initUI();
		initListener();
		sendBroadcast(new Intent("dismiss"));
	}
	private void initListener() {
		edit.setOnClickListener(this);
		fastSave.setOnClickListener(this);
	}
//	private void showDialog(){
//		pDia = new ProgressDialog(HandDown.this);
//		pDia.setMessage("正在努力更新 请稍后。。。。");
//		pDia.setCancelable(false);
//		pDia.setCanceledOnTouchOutside(false);
//		pDia.show();
//	}
	private void initUI() {
		edit = (Button) findViewById(R.id.edit);
		fastSave = (Button) findViewById(R.id.fastSave);
		name = (TextView) findViewById(R.id.name);
		phone = (TextView) findViewById(R.id.phone);
		quyu = (TextView) findViewById(R.id.quyu);
		fangyuan = (TextView) findViewById(R.id.fangyuan);
		jine = (TextView) findViewById(R.id.jine);
		huxing = (TextView) findViewById(R.id.huxing);
		beizhu = (EditText) findViewById(R.id.edtBeizhu);
		
		
		name.setText(conModel.getCustomer_name());
		phone.setText(conModel.getCustomer_phone());
		quyu.setText(conModel.getQuyu());
		fangyuan.setText(conModel.getLouhao());
		jine.setText(conModel.getChengjiaojine());
		huxing.setText(conModel.getYixianghuxing());
	}
	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);  
	}
	@Override
	public void onClick(View v) {
		if(!Constant.USERPHONE.equals("")){
		switch(v.getId()){
		case R.id.edit:
			Intent it = new Intent(HandDown.this,Register.class);
			it.putExtra("type", "editer");
			Bundle bd = new Bundle();
			bd.putSerializable("consumer", conModel);
			it.putExtras(bd);
			startActivity(it);
			finish();
			break;
		case R.id.fastSave:
			if(beizhu.getText().equals("") || beizhu.getText().length() == 0){
				Toast.makeText(getApplicationContext(), "请输入您的备注", 0).show();
			}else{
//				showDialog();
				PhoneReceiver.showLoad(HandDown.this,fastSave);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							conModel.setBeizhu(beizhu.getText().toString());
							conModel.setDatatime(CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
//							TextMessage tMsg = NetworkData.posturl(Constant.REGISTER+"&phone="+phone.getText().toString()+"&userid="
//						+Constant.USERID+"&myphone="+Constant.USERPHONE+"&myname="+Constant.USERNAME);
//							if(tMsg != null){
								TextMessage t = NetworkData.postFastConsumerDataUrl(Constant.FASTUPDATACONSUMERMODEL+"&type=update", conModel);
								if(t != null && t.getContent().equals("更新成功")){
									myHandler.sendEmptyMessage(2);
								}else{
									myHandler.sendEmptyMessage(1);
								}
//							}
//						else{
//								myHandler.sendEmptyMessage(1);
//							}
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
			break;
		}
		}else{
			Toast.makeText(HandDown.this, "请先开通帐号", 0).show();
		}
	}
}
