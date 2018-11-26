package com.assistant.view;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.assistant.model.ConsumerModel;
import com.assistant.model.TextMessage;
import com.assistant.utils.Constant;
import com.assistant.utils.CurrentTime;
import com.assistant.utils.NetworkData;
import com.example.assistant.R;
public class ContactsActivity extends Activity {  
	 
    Context mContext = null;  
 
    /**获取库Phon表字段**/  
    private static final String[] PHONES_PROJECTION = new String[] {  
        Phone.DISPLAY_NAME, Phone.NUMBER};  
     
    /**联系人显示名称**/  
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;  
      
    /**电话号码**/  
    private static final int PHONES_NUMBER_INDEX = 1;  
      
    /**联系人名称**/  
    private ArrayList<MyModel> mContacts = new ArrayList<MyModel>();  
      
	private LinearLayout showBar;
	private Button back, rigster;
	private CheckBox cbAll;
	private int count = 0;
	ListView mListView = null;  
    MyListAdapter myAdapter = null;  
    private ProgressDialog pDia; 
    private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
				if(mContacts.size() > 0 ){
					 myAdapter = new MyListAdapter(ContactsActivity.this);  
				     mListView.setAdapter(myAdapter); 
					showBar.setVisibility(View.GONE);
				}else{
					showBar.setVisibility(View.INVISIBLE);
				}
			}else{
				pDia.setMessage("已完成   "+msg.arg1+"/"+count);
			}
		}
		
	};
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
    mContext = this;  
    super.onCreate(savedInstanceState);  
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
    setContentView(R.layout.importcontacts);
    initUI();
    initListener();
 
 
//    mListView.setOnItemClickListener(new OnItemClickListener() {  
// 
//        @Override  
//        public void onItemClick(AdapterView<?> adapterView, View view,  
//            int position, long id) {  
//        //调用系统方法拨打电话  
//        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri  
//            .parse("tel:" + mContactsNumber.get(position)));  
//        startActivity(dialIntent);  
//        }  
//    });  
 
    }  
 
    private void initListener() {
    	back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ContactsActivity.this.finish();
			}
		});
    	cbAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				cbAllCheck(isChecked);
				if(isChecked)
					rigster.setEnabled(true);
			}
		});
    	mListView.setOnItemClickListener(new OnItemClickListener() {

    		@Override
    		public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
    			itemClick(parent, position);
    		}
    	});
    	rigster.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pDia = new ProgressDialog(ContactsActivity.this);
				pDia.setMessage("正在注册。。。");
				pDia.setCancelable(false);
				pDia.setCanceledOnTouchOutside(false);
				pDia.show();
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						int size = mContacts.size();
						int num = 0;
						for (int i = 0; i < size; i++) {
							try {
								if(mContacts.get(i).isCheck()){
									TextMessage tMsg = NetworkData.posturl(Constant.REGISTER+"&userid="+Constant.USERPHONE+"&phone="+mContacts.get(i).getPhoneNumber()
											+"&myphone="+Constant.USERPHONE+"&myname="+Constant.USERNAME+"&laoyezhuphone=&islaoyezhu=false&username="+mContacts.get(i).getName());
									ConsumerModel conModel = new ConsumerModel();
									conModel.setCustomer_phone(mContacts.get(i).getPhoneNumber());
									conModel.setCustomer_name(mContacts.get(i).getName());
									conModel.setConsultantname(Constant.USERNAME);
									conModel.setConsultantphone(Constant.USERPHONE);
									conModel.setXingbie("男");
									conModel.setQuyu(Constant.QUYU);
									conModel.setDatatime(CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
									if(tMsg != null && Integer.parseInt(tMsg.getContent()) > 0 ){
										TextMessage t = NetworkData.postFastConsumerDataUrl(Constant.FASTUPDATACONSUMERMODEL+"&type=insert", conModel);
										if(t != null && t.getContent().equals("更新成功")){
											num ++;
											Message msg = new Message();
											msg.what = 2;
											msg.arg1 = num;
											handler.sendMessage(msg);
										}
										
									}else{
										
									}
								}
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						sendBroadcast(new Intent("updateall"));
						pDia.cancel();
						finish();
					}
				}).start();
			}
		});
	}
    private void itemClick(AdapterView<?> parent,int position){

		if (((MyModel) parent.getItemAtPosition(position)).isCheck()) {
			mContacts.get(position).setCheck(false);
		} else {
			mContacts.get(position).setCheck(true);
		}
		
		int size = mContacts.size();
		count = 0;
		for (int i = 0; i < size; i++) {
			if(mContacts.get(i).isCheck())
				count++;
		}
		if (count > 0) {
			rigster.setEnabled(true);
		} else {
			rigster.setEnabled(false);
		}
		
		if (count == size) {
			cbAll.setChecked(true);
		} else {
			cbAll.setChecked(false);
		}
		myAdapter.notifyDataSetChanged();
	}
	private void cbAllCheck(boolean isChecked) {
		if (isChecked) {
			int size = mContacts.size();
			for (int i = 0; i < size; i++) {
				mContacts.get(i).setCheck(true);
			}
		} else {
			int size = mContacts.size();
			count = 0;
			for (int i = 0; i < size; i++) {
				if(count < 1){
				if(!mContacts.get(i).isCheck())
					count ++;
				}else
					break;
			}
			if(count == 0){
				for (int i = 0; i < size; i++) {
					mContacts.get(i).setCheck(false);
				}
				rigster.setEnabled(false);
			}
		}
		myAdapter.notifyDataSetChanged();
	}
	private void initUI() {
        mListView = (ListView) findViewById(R.id.lvContact);  
        back = (Button) findViewById(R.id.back);
        rigster = (Button) findViewById(R.id.btnRigster);
        cbAll = (CheckBox) findViewById(R.id.cbAll);
        showBar = (LinearLayout) findViewById(R.id.progress_show);
        /**得到手机通讯录联系人信息**/  
        getPhoneContacts();  
        getSIMContacts();
        handler.sendEmptyMessage(1);
     
	}

	/**得到手机通讯录联系人信息**/  
    private void getPhoneContacts() {  
    ContentResolver resolver = mContext.getContentResolver();  
 
    // 获取手机联系人  
    Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);  
 
 
    if (phoneCursor != null) {  
        while (phoneCursor.moveToNext()) {  
        	MyModel model = new MyModel();
        //得到手机号码  
        String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);  
        //当手机号码为空的或者为空字段 跳过当前循环  
        if (TextUtils.isEmpty(phoneNumber))  
            continue;  
        //得到联系人名称  
        String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);  
         if(contactName == null)
        	 model.setName("未知"); 
         else
        	 model.setName(contactName);  
         model.setPhoneNumber(phoneNumber);  
         mContacts.add(model);
        }  
 
        phoneCursor.close();  
    }  
    }  
      
    /**得到手机SIM卡联系人人信息**/  
    private void getSIMContacts() {  
    ContentResolver resolver = mContext.getContentResolver();  
    // 获取Sims卡联系人  
    Uri uri = Uri.parse("content://icc/adn");  
    Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,  
        null);  
 
    if (phoneCursor != null) {  
        while (phoneCursor.moveToNext()) {  
        	MyModel model = new MyModel();
        // 得到手机号码  
        String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);  
        // 当手机号码为空的或者为空字段 跳过当前循环  
        if (TextUtils.isEmpty(phoneNumber))  
            continue;  
        // 得到联系人名称  
        String contactName = phoneCursor 
            .getString(PHONES_DISPLAY_NAME_INDEX);  
        if(contactName == null){
        	model.setName("未知");
        }else
        	model.setName(contactName);
        //Sim卡中没有联系人头像  
        model.setPhoneNumber(phoneNumber);
        mContacts.add(model);  
        }  
 
        phoneCursor.close();  
    }  
    }  
      
    class MyListAdapter extends BaseAdapter {  
    public MyListAdapter(Context context) {  
        mContext = context;  
    }  
 
    public int getCount() {  
        //设置绘制数量  
        return mContacts.size();  
    }  
 
    @Override  
    public boolean areAllItemsEnabled() {  
        return false;  
    }  
 
    public Object getItem(int position) {  
        return mContacts.get(position);  
    }  
 
    public long getItemId(int position) {  
        return position;  
    }  
 
    public View getView(final int position, View convertView, ViewGroup parent) {  
    	ViewHolder holder = null;
        if (convertView == null) {  
        	holder = new ViewHolder();
	        convertView = LayoutInflater.from(mContext).inflate(R.layout.contacts_list, null);  
	        holder.title = (TextView) convertView.findViewById(R.id.color_title);  
	        holder.text = (TextView) convertView.findViewById(R.id.color_text);  
	        holder.check = (RadioButton) convertView.findViewById(R.id.check);
	        convertView.setTag(holder);
        }else
        	holder = (ViewHolder) convertView.getTag();
        //绘制联系人名称  
        holder.title.setText(mContacts.get(position).getName());  
        //绘制联系人号码  
        holder.text.setText(mContacts.get(position).getPhoneNumber()); 
        if(mContacts.get(position).isCheck)
        	holder.check.setChecked(true);
        else
        	holder.check.setChecked(false);
//        holder.check.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(mContacts.get(position).isCheck()){
//					mContacts.get(position).setCheck(false);
//				}
//				else{
//					mContacts.get(position).setCheck(true);
//				}
//				MyListAdapter.this.notifyDataSetChanged();
//			}
//		});
        return convertView;  
    }
    class ViewHolder{
    	 TextView title;  
         TextView text; 
         RadioButton check;
    }
 
    }  
    class MyModel{
    	String phoneNumber = "",name = "";
    	boolean isCheck = false;
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isCheck() {
			return isCheck;
		}
		public void setCheck(boolean isCheck) {
			this.isCheck = isCheck;
		}
    	
    }
} 
