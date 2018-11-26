package com.example.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amanager.R;
import com.example.fragment.home;
import com.example.util.Constant;
import com.example.util.NetworkData;
import com.example.util.TextMessage;

public class Login extends Activity implements OnFocusChangeListener {
	// private GifView login_gif;
	EditText edtName, edtPwd;
	Button login, chkRember;
	InputStream is = null;
	private EditText leibie;
	private PopMenu popMenu;
	PopupWindow settlementPop;
	private String type = "0";
	int count = 0;
	private boolean isLogin = false;
	private SharedPreferences sp;
	private View remeber;
	private TextView tv_remeber, line1, line2;
	private boolean isCheck = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// if(!isFileExist("login.gif")){
		//
		// createSDDir();
		// FileOutputStream fos=null;
		// BufferedInputStream bis=null;
		// BufferedOutputStream bos=null;
		//
		// try{
		// File file = new File(Environment.getExternalStorageDirectory() +
		// "/anchang/"+"login.gif");
		// file.createNewFile();
		// bis=new
		// BufferedInputStream(getResources().openRawResource(R.drawable.login));
		// fos=new FileOutputStream(Environment.getExternalStorageDirectory() +
		// "/anchang/"+"login.gif");
		// bos=new BufferedOutputStream(fos);
		// int n = 0;
		// while((n=bis.read())!=-1){
		// bos.write(n);
		// }
		//
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// if (bis != null) {
		// try {
		// bis.close(); // 关闭bis
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// if (bos != null) {
		// try {
		// bos.close(); // 关闭bos
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// }
		// try {
		// is = new FileInputStream(Environment.getExternalStorageDirectory() +
		// "/anchang/"+"login.gif");
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }

		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);

		initUI();
	}

	OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			if (position == 2) {
				leibie.setText("金桥花园");
			} else if (position == 0) {
				leibie.setText("博士后");
			} else if (position == 1) {
				leibie.setText("金山");
			}

			popMenu.dismiss();
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		popMenu = new PopMenu(this, getResources().getDimensionPixelSize(R.dimen.popmenu_x));
		popMenu.setOnItemClickListener(popmenuItemClickListener);
		popMenu.addItems(new String[] { "博士后", "金山", "金桥花园" });

	}

	// public void showLoad(){
	// LayoutInflater mInflater = LayoutInflater.from(this);
	// View settlementView = mInflater.inflate(R.layout.loading_pop, null);
	// LinearLayout loadView = (LinearLayout)
	// settlementView.findViewById(R.id.loadView);
	// TextView text = (TextView) settlementView.findViewById(R.id.text);
	// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
	// ViewGroup.LayoutParams.WRAP_CONTENT,
	// ViewGroup.LayoutParams.WRAP_CONTENT);
	// lp.gravity = Gravity.CENTER_VERTICAL;
	// ProgressBar progressBar = new ProgressBar(this);
	// progressBar.setLayoutParams(lp);
	// progressBar.setIndeterminateDrawable(getResources().getDrawable(R.anim.loading));
	// loadView.addView(progressBar);
	// loadView.setVisibility(View.VISIBLE);
	// settlementPop = new PopupWindow(settlementView,
	// ViewGroup.LayoutParams.FILL_PARENT,
	// ViewGroup.LayoutParams.FILL_PARENT);
	//
	// settlementPop.setFocusable(true);
	// int[] location = new int[2];
	// settlementView.findViewById(R.id.text).getLocationOnScreen(location);
	// settlementPop.showAtLocation(settlementView.findViewById(R.id.text),
	// Gravity.CENTER_HORIZONTAL, location[0], location[1]);
	// }
	private void initUI() {
		// login_gif = (GifView) findViewById(R.id.login_gif);
		edtName = (EditText) findViewById(R.id.userName);
		edtPwd = (EditText) findViewById(R.id.userPWD);
		login = (Button) findViewById(R.id.btnLogin);
		leibie = (EditText) findViewById(R.id.leibie);
		tv_remeber = (TextView) findViewById(R.id.tv_remeber);
		remeber = findViewById(R.id.remeber);
		chkRember = (Button) findViewById(R.id.chkbox_remember);
		line1 = (TextView) findViewById(R.id.line1);
		line2 = (TextView) findViewById(R.id.line2);

		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		// login_gif.setGifImage(is);
		// login_gif.setGifImageType(GifImageType.WAIT_FINISH);
		// login_gif.setShowDimension(mDisplayMetrics.widthPixels,
		// mDisplayMetrics.heightPixels);
		// login_gif.destroyDrawingCache();
		edtName.setText(sp.getString("USER_ID", ""));
		edtPwd.setText(sp.getString("USER_PWD", ""));
		leibie.setText(sp.getString("USER_QUYU", "博士后"));

		if (sp.getBoolean("PWD_REMIND", true))
			chkRember.setBackgroundResource(R.drawable.checkbox_checked);
		else
			chkRember.setBackgroundResource(R.drawable.checkbox_normal);

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!leibie.getText().toString().equals("")) {
					Constant.showLoad(Login.this, login);
					Thread t = new Thread(new Runnable() {
						public void run() {
							login();
						}
					});
					t.start();
				} else {
					Toast.makeText(Login.this, "请选择区域", 0).show();
				}
			}
		});
		remeber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isCheck) {
					isCheck = false;
					chkRember.setBackgroundResource(R.drawable.checkbox_normal);
					tv_remeber.setTextColor(Color.parseColor("#25c2fc"));
				} else {
					isCheck = true;
					chkRember
							.setBackgroundResource(R.drawable.checkbox_checked);
					tv_remeber.setTextColor(Color.parseColor("#ffffff"));
				}
			}
		});
		edtName.setOnFocusChangeListener(this);
		edtPwd.setOnFocusChangeListener(this);
		leibie.setFocusable(false);
		leibie.setLongClickable(false);
		leibie.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popMenu.showAsDropDown(v);
			}
		});

	}

	public boolean isFileExist(String fileName) {
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/anchang/" + fileName);
		return file.exists();
	}

	public File createSDDir() {
		File dir = new File(Environment.getExternalStorageDirectory()
				+ "/anchang");
		if (!dir.exists())
			dir.mkdirs();
		return dir;
	}

	private boolean login() {
		isLogin = false;
		Editor editor = sp.edit();
		boolean isFirstRun = sp.getBoolean("isFirstRun", true);

		try {
			TextMessage textMsg = NetworkData.posturl(Constant.LOGIN + "?id="
					+ edtName.getText().toString().trim() + "&password="
					+ edtPwd.getText().toString().trim());
			if (textMsg != null && !textMsg.getContent().equals("主管")) {
				finish();
				return false;
			} else if (textMsg != null) {
				Constant.QUYU = leibie.getText().toString().trim();
				Constant.USERPHONE = edtName.getText().toString().trim();
				Constant.USERNAME = textMsg.getContent();
				Constant.MANAGER = textMsg.getName();
				Constant.TASK = textMsg.getFromid();

				if (isCheck) {
					editor.putString("USER_PWD", edtPwd.getText().toString());
				} else
					editor.putString("USER_PWD", "");
				editor.putBoolean("PWD_REMIND", isCheck);
				if (isFirstRun) {
					editor.putBoolean("isFirstRun", false);
					editor.putString("USER_ID", edtName.getText().toString());
					editor.putString("USER_QUYU", Constant.QUYU);
					editor.commit();

					// ImportDB iDB = new ImportDB(this);
					// iDB.copyDatabase();

					Intent it = new Intent(this, Setting.class);
					it.putExtra("intentType", "firstRun");
					startActivity(it);
					finish();
					return true;

				} else {
					// Constant.USERNAME = sp.getString("USER_NAME", "");
					editor.putString("USER_ID", edtName.getText().toString());
					editor.putString("USER_QUYU", Constant.QUYU);
					editor.commit();
				}

				isLogin = true;
			}
		} catch (Exception e) {
			System.out.println("error = " + e.toString());
			count++;
			if (count < 3) {
				login();
			} else
				finish();
			return false;
		}

		Intent intent = new Intent();
		intent.setClass(Login.this, home.class);
		Login.this.startActivity(intent);
		finish();
		return isLogin;
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.userName:
			if (hasFocus) {
				line1.setBackgroundColor(Color.parseColor("#ffffff"));
				edtName.setHintTextColor(Color.parseColor("#ffffff"));
			} else {
				line1.setBackgroundColor(Color.parseColor("#25c2fc"));
				edtName.setHintTextColor(Color.parseColor("#25c2fc"));
			}
			break;
		case R.id.userPWD:
			if (hasFocus) {
				line2.setBackgroundColor(Color.parseColor("#ffffff"));
				edtPwd.setHintTextColor(Color.parseColor("#ffffff"));
			} else {
				line2.setBackgroundColor(Color.parseColor("#25c2fc"));
				edtPwd.setHintTextColor(Color.parseColor("#25c2fc"));
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {

		try {
			if (is != null)
				is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.gc();
		if (isLogin) {
			Toast.makeText(Login.this, "登陆成功", 0).show();
		} else {
			Toast.makeText(Login.this, "登陆失败", 0).show();
		}
		Constant.dimissLoad();
		super.onDestroy();
	}

}