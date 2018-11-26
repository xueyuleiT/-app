package com.example.view;

import com.example.amanager.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlertDialog extends Dialog{
	Context context;
	Dialog ad;
	TextView titleView;
	AbsoluteLayout messageView;
	LinearLayout buttonLayout;
	Button ok,cancel;
	View view;
	public AlertDialog(Context context) {
		super(context);
		this.context = context;
		ad = new Dialog(context);  
		ad.show();
		ad.getWindow().setBackgroundDrawable(new ColorDrawable(0));  
		view = LayoutInflater.from(context).inflate(R.layout.alertdialog, null);
		titleView = (TextView) view.findViewById(R.id.title);
		messageView = (AbsoluteLayout) view.findViewById(R.id.message);
		buttonLayout = (LinearLayout) view.findViewById(R.id.buttonLayout);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		ad.setContentView(view);
		// 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
	}

	public void setTitle(int resId) {
		titleView.setText(resId);
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}

	public void setView(View v) {
		messageView.addView(v);
	}

	/**
	 * 设置按钮
	 * 
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(String text,
			final View.OnClickListener listener) {
		ok.setText(text);
		ok.setOnClickListener(listener);
	}

	/**
	 * 设置按钮
	 * 
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(String text,
			final View.OnClickListener listener) {
		cancel.setText(text);
		cancel.setOnClickListener(listener);
	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		ad.dismiss();
	}
}
