package com.assistant.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.assistant.utils.Constant;
import com.example.assistant.R;

public class HandDownSave extends Activity{

	Button btnSave,btnFastSave;
	TextView phone;
	String flag = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hand_down_save);
		
		btnSave = (Button) findViewById(R.id.save);
		btnFastSave = (Button) findViewById(R.id.fastSave);
		phone = (TextView) findViewById(R.id.phone);
		
		phone.setText(getIntent().getExtras().getString("phone"));
		sendBroadcast(new Intent("dismiss"));
		
		
		flag = getIntent().getExtras().getString("flag");
		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!Constant.USERPHONE.equals("")){
					Intent it = new Intent(HandDownSave.this,Register.class);
					Bundle bd = new Bundle();
					if(getIntent().getExtras().getString("type").equals("callSave")){
						bd.putString("type", "saveAndInsertCall");
						bd.putString("phone", phone.getText().toString());
						bd.putString("flag", flag);
						System.out.println("flag = "+flag );
						bd.putLong("stopTime", getIntent().getExtras().getLong("stopTime"));
						bd.putLong("callTime", getIntent().getExtras().getLong("callTime"));
					}else{
						bd.putString("type", "save");
						bd.putString("phone", phone.getText().toString());
					}
					it.putExtras(bd);
					startActivity(it);
					finish();
				}else
					Toast.makeText(HandDownSave.this, "请先开通帐号", 0).show();
			}
		});
		btnFastSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!Constant.USERPHONE.equals("")){
					Intent it = new Intent(HandDownSave.this,Register.class);
					Bundle bd = new Bundle();
					if(getIntent().getExtras().getString("type").equals("callSave")){
						bd.putString("type", "fastsaveAndInsertCall");
						bd.putString("phone", phone.getText().toString());
						bd.putString("flag", flag);
						bd.putLong("stopTime", getIntent().getExtras().getLong("stopTime"));
						bd.putLong("callTime", getIntent().getExtras().getLong("callTime"));
					}else{
						bd.putString("type", "fastsave");
						bd.putString("phone", phone.getText().toString());
					}
					it.putExtras(bd);
					startActivity(it);
					finish();
				}else
					Toast.makeText(HandDownSave.this, "请先开通帐号", 0).show();
			}
		});
		sendBroadcast(new Intent("dismiss"));
		
	}
	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);  
	}
}
