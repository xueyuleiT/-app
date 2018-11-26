package com.example.view;


import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amanager.R;
import com.example.util.Constant;
import com.example.util.NetworkData;
import com.example.util.TextMessage;

public class RegisterActivity extends Activity {
	private EditText mEdtRegUserName = null;
	private EditText mEdtRegPassword = null;
	private EditText mEdtRegPasswordConfirm = null,mEdtRegUserId;
	private Button btn_ok, btn_cancel;
	private String mStrUserName = null;
	private String mStrUserId= null;
	private String mStrPassword = null;
	private String mStrPasswordConfirm = null;
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(getApplicationContext(), "这个账号已经存在",  
                        Toast.LENGTH_SHORT).show();  
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "注册失败 账号已存在",  
                        Toast.LENGTH_SHORT).show();  
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "恭喜你 注册成功",  
                        Toast.LENGTH_SHORT).show();  
				finish();
				break;
			case 3:
				Toast.makeText(getApplicationContext(), "服务器没有返回结果",  
                        Toast.LENGTH_SHORT).show();  
				break;
			}
			Constant.dimissLoad();
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		initUI();
		initListener();
	}

	private void initListener() {
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mEdtRegUserId.getText().toString().length() > 0 && mEdtRegUserName.getText().toString().length() > 0
						&& mEdtRegPassword.getText().toString().length() >0 && mEdtRegUserName.getText().toString().length() > 0){
				mStrUserName = mEdtRegUserName.getText().toString();
				mStrPassword = mEdtRegPassword.getText().toString();
				mStrPasswordConfirm = mEdtRegPasswordConfirm.getText().toString();
				mStrUserId = mEdtRegUserId.getText().toString();
				if(mStrUserName.equals("") || mStrPassword.equals("") 
						|| mStrPasswordConfirm.equals("") || mStrUserId.equals("")){
					Toast.makeText(RegisterActivity.this, "您还需填入其他信息", 1).show();
				}else if(!mStrPassword.equals(mStrPasswordConfirm)){
					Toast.makeText(RegisterActivity.this, "密码不一致", 1).show();
				}else{
					Constant.showLoad(RegisterActivity.this, btn_ok);
					new Thread(new Runnable() {
						public void run() {
							try {
								TextMessage t = NetworkData.posturl(Constant.REGISTERXIAOSHOU+"&phone="+mStrUserId+"&name="+mStrUserName
										+"&pwd="+mStrPassword+"&managerid="+Constant.USERPHONE+"&task="+Constant.TASK);
						
								if(t != null && t.getContent().equals("更新成功")){
									handler.sendEmptyMessage(2);
								}else
									handler.sendEmptyMessage(1);
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
				}else{
					Toast.makeText(RegisterActivity.this, "有未填选项", 1).show();
				}
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout); 
	}
	private void initUI() {
		mEdtRegUserId = (EditText) findViewById(R.id.user_id);
		mEdtRegUserName = (EditText) findViewById(R.id.user_name);
		mEdtRegPassword = (EditText) findViewById(R.id.user_pwd);
		mEdtRegPasswordConfirm = (EditText) findViewById(R.id.user_secondpwd);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
	}

}
