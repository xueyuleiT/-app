package com.example.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amanager.R;
import com.example.fragment.home;
import com.example.util.Constant;

public class Setting extends Activity {
	String intentType;
	EditText userid, name, safeWorld;
	Button save;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		intentType = getIntent().getExtras().getString("intentType");
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		initUI();

	}

	private void initUI() {
		userid = (EditText) findViewById(R.id.userid);
		name = (EditText) findViewById(R.id.name);
		save = (Button) findViewById(R.id.save);
		safeWorld = (EditText) findViewById(R.id.safepwd);

		userid.setText(Constant.USERPHONE);
		name.setText(Constant.USERNAME);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				save();
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (intentType.equals("firstRun")) {
			if (name.getText().toString().equals(""))
				Toast.makeText(this, "请填写名字", 0).show();
		} else {
			if (name.getText().toString().equals(""))
				Toast.makeText(this, "请填写名字", 0).show();
			else
				super.onBackPressed();
		}
	}

	private void save() {
		if (safeWorld.getText().toString().length() < 6)
			Toast.makeText(this, "安全密码不得少于6位", 0).show();
		else {
			Constant.showLoad(getApplicationContext(), save);
			Editor editor = sp.edit();
			editor.putString("USER_NAME", name.getText().toString().trim());
			editor.putString("USER_PHONE", userid.getText().toString().trim());
			editor.putString("SAFE_PWD", safeWorld.getText().toString());
			editor.commit();

			Constant.USERNAME = name.getText().toString().trim();
			Constant.USERPHONE = userid.getText().toString().trim();
			Constant.SAFEPWD = safeWorld.getText().toString();
			System.out.println("safePwd = " + Constant.SAFEPWD);
			Toast.makeText(this, "已保存", 0).show();

			if (intentType.equals("firstRun")) {
				Intent it = new Intent(this, home.class);
				startActivity(it);
			}
			finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	@Override
	protected void onDestroy() {
		Constant.dimissLoad();
		super.onDestroy();
	}

}
