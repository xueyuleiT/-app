package com.assistant.view;

import com.example.assistant.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class ShowBeizhu extends Activity{
	String beizhu = "";
	private TextView showBeizhu;
	private View root;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		setContentView(R.layout.showbeizhu);
		beizhu = getIntent().getExtras().getString("beizhu");
		root = findViewById(R.id.root);
		showBeizhu = (TextView) findViewById(R.id.showbeizhu);
		showBeizhu.setText(beizhu);
		root.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				finish();
				return false;
			}
		});
	}

}
