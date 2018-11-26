package com.assistant.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.assistant.model.ConsumerModel;
import com.assistant.utils.Constant;
import com.example.assistant.R;
  
/** 
 * 悬浮窗Service 该服务会在后台一直运行一个悬浮的透明的窗体。 
 *  
 * @author 
 *  
 */  
public class FxService extends Service {  
  
    private int statusBarHeight;// 状态栏高度  
    private View view;
    private boolean viewAdded = false,isSmall = false;// 透明窗体是否已经显示  
    private WindowManager windowManager;  
    private WindowManager.LayoutParams layoutParams;  
    private Button smallOrBig;
    private ConsumerModel consumer;
    private LinearLayout rootview;
    private TextView tv_jingjiren,jingjiren,tv_name,name,tv_phone,phone,tv_huxing,huxing,tv_fangyuan,fangyuan,tv_jine,jine,tv_createTime,createTime,
    tv_laifangshijian,laifangshijian,tv_beizhu,beizhu,warring;
    
    
	 public BroadcastReceiver m_BroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("dismiss")){
				System.out.println("dismiss");
				stopSelf();
			}
		}
	};
    @Override  
    public IBinder onBind(Intent intent) {  
        return null;  
    }  
    private void register_BroadCast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("dismiss");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(m_BroadcastReceiver, filter);
	}
	@Override  
    public void onCreate() {  
        super.onCreate();  
        view = LayoutInflater.from(this).inflate(R.layout.floatting, null);  
    }  
  
    private void initDtae() {
         
    	if(!consumer.getConsultantphone().equals(Constant.USERPHONE)){
    		warring.setVisibility(View.VISIBLE);
    		rootview.setVisibility(View.GONE);
    		warring.setText("此用户已经被"+consumer.getConsultantname()+"注册!");
    	}else{
    		 warring.setVisibility(View.GONE);
    		 rootview.setVisibility(View.VISIBLE);
	         name.setText(consumer.getCustomer_name());
	         phone.setText(consumer.getCustomer_phone());
	         huxing.setText(consumer.getYixianghuxing());
	         fangyuan.setText(consumer.getLouhao());
	         jine.setText(consumer.getYisuan());
	         createTime.setText(consumer.getDatatime());
	         laifangshijian.setText(consumer.getLastCallTime());
	         beizhu.setText(consumer.getBeizhu());
	         jingjiren.setText(consumer.getConsultantname());
    	}
	}

	/** 
     * 刷新悬浮窗 
     *  
     * @param x 
     *            拖动后的X轴坐标 
     * @param y 
     *            拖动后的Y轴坐标 
     */  
    public void refreshView(int x, int y) {  
        //状态栏高度不能立即取，不然得到的值是0  
        if(statusBarHeight == 0){  
            View rootView  = view.getRootView();  
            Rect r = new Rect();  
            rootView.getWindowVisibleDisplayFrame(r);  
            statusBarHeight = r.top;  
        }  
          
        layoutParams.x = x;  
        // y轴减去状态栏的高度，因为状态栏不是用户可以绘制的区域，不然拖动的时候会有跳动  
        layoutParams.y = y - statusBarHeight;//STATUS_HEIGHT;  
        refresh();  
    }  
  
    /** 
     * 添加悬浮窗或者更新悬浮窗 如果悬浮窗还没添加则添加 如果已经添加则更新其位置 
     */  
    private void refresh() {  
        if (viewAdded) {  
            windowManager.updateViewLayout(view, layoutParams);  
        } else {  
            windowManager.addView(view, layoutParams);  
            viewAdded = true;  
        }  
    }  
  
    @SuppressWarnings("deprecation")
	@Override  
    public void onStart(Intent intent, int startId) {  
        super.onStart(intent, startId);  
        System.out.println("显示信息");
		if (intent != null){
			consumer = (ConsumerModel) intent.getExtras().get("consumermodel");
			initUI();
	        initDtae();
	        register_BroadCast();
	        refresh();  
		}
    }  
  
    private void initUI(){
        rootview = (LinearLayout) view.findViewById(R.id.view);
        warring = (TextView) view.findViewById(R.id.warring);
        smallOrBig = (Button) view.findViewById(R.id.smallOrBig);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_huxing = (TextView) view.findViewById(R.id.tv_huxing);
        tv_fangyuan = (TextView) view.findViewById(R.id.tv_fangyuan);
        tv_jine = (TextView) view.findViewById(R.id.tv_jine);
        tv_jingjiren = (TextView) view.findViewById(R.id.tv_jingjiren);
        
        name = (TextView) view.findViewById(R.id.name);
        phone = (TextView) view.findViewById(R.id.phone);
        huxing = (TextView) view.findViewById(R.id.huxing);
        fangyuan = (TextView) view.findViewById(R.id.fangyuan);
        jine = (TextView) view.findViewById(R.id.jine);
        jingjiren = (TextView) view.findViewById(R.id.jingjiren);
        
        tv_createTime =  (TextView) view.findViewById(R.id.tv_createTime);
        tv_laifangshijian =  (TextView) view.findViewById(R.id.tv_laifangshijian);
        tv_beizhu =  (TextView) view.findViewById(R.id.tv_beizhu);
        
        createTime =  (TextView) view.findViewById(R.id.createTime);
        laifangshijian =  (TextView) view.findViewById(R.id.laifangshijian);
        beizhu =  (TextView) view.findViewById(R.id.beizhu);
        
        
        windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);  
        /* 
         * LayoutParams.TYPE_SYSTEM_ERROR：保证该悬浮窗所有View的最上层 
         * LayoutParams.FLAG_NOT_FOCUSABLE:该浮动窗不会获得焦点，但可以获得拖动 
         * PixelFormat.TRANSPARENT：悬浮窗透明 
         */  
        
        if(Constant.scHeight > Constant.scWidth)
        	layoutParams = new LayoutParams(Constant.scWidth,  
             		Constant.scHeight*2/3, LayoutParams.TYPE_SYSTEM_ALERT,  
                     LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);  
        else
        	layoutParams = new LayoutParams(Constant.scHeight,  
        			Constant.scWidth*2/3, LayoutParams.TYPE_SYSTEM_ALERT,  
        			LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);  
        // layoutParams.gravity = Gravity.RIGHT|Gravity.BOTTOM; //悬浮窗开始在右下角显示  
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;  
        
        
        smallOrBig.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isSmall){
					smallOrBig.setBackgroundResource(R.drawable.fangda);
					if(Constant.scHeight > Constant.scWidth)
			        	layoutParams = new LayoutParams(Constant.scWidth*2/3,  
			             		Constant.scHeight/2, LayoutParams.TYPE_SYSTEM_ALERT,  
			                     LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);  
			        else
			        	layoutParams = new LayoutParams(Constant.scHeight*2/3,  
			        			Constant.scWidth/2, LayoutParams.TYPE_SYSTEM_ALERT,  
			        			LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT); 
					
			        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;  
			        
			        tv_fangyuan.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_10));
			        tv_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_10));
			        tv_phone.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_10));
			        tv_huxing.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_10));
			        tv_jine.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_10));
			        tv_jingjiren.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_10));
			        
			        fangyuan.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_10));
			        name.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_10));
			        phone.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_10));
			        huxing.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_10));
			        jine.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_10));
			        jingjiren.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_10));
			        
			        tv_createTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_8));
			        tv_laifangshijian.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_8));
			        tv_beizhu.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_8));
			        
			        createTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_8));
			        laifangshijian.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_8));
			        beizhu.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
			                getResources().getDimensionPixelSize(R.dimen.font_size_8));
			        
				}
				else{
					smallOrBig.setBackgroundResource(R.drawable.suoxiao);
					if(Constant.scHeight > Constant.scWidth)
			        	layoutParams = new LayoutParams(Constant.scWidth,  
			             		Constant.scHeight*2/3, LayoutParams.TYPE_SYSTEM_ALERT,  
			                     LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);  
			        else
			        	layoutParams = new LayoutParams(Constant.scHeight,  
			        			Constant.scWidth*2/3, LayoutParams.TYPE_SYSTEM_ALERT,  
			        			LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);   
				        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;  
				        tv_fangyuan.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        tv_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        tv_phone.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        tv_huxing.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        tv_jine.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        tv_jingjiren.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        
				        fangyuan.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        name.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        phone.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        huxing.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        jine.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        jingjiren.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        
				        tv_createTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        tv_laifangshijian.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        tv_beizhu.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        
				        createTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        laifangshijian.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        beizhu.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
				                getResources().getDimensionPixelSize(R.dimen.font_size_14));
				        
				}
				isSmall = !isSmall;
				 windowManager.updateViewLayout(view, layoutParams);
			}
		});
        view.setOnTouchListener(new OnTouchListener() {  
            float[] temp = new float[] { 0f, 0f };  
  
            public boolean onTouch(View v, MotionEvent event) {  
                layoutParams.gravity = Gravity.LEFT | Gravity.TOP;  
                int eventaction = event.getAction();  
                switch (eventaction) {  
                case MotionEvent.ACTION_DOWN: // 按下事件，记录按下时手指在悬浮窗的XY坐标值  
                    temp[0] = event.getX();  
                    temp[1] = event.getY();  
                    break;  
  
                case MotionEvent.ACTION_MOVE:  
                    refreshView((int) (event.getRawX() - temp[0]), (int) (event  
                            .getRawY() - temp[1]));  
                    break;  
  
                }  
                return true;  
            }  
        });  
    }
    
    /** 
     * 关闭悬浮窗 
     */  
    public void removeView() {  
        if (viewAdded) {  
            windowManager.removeView(view);  
            viewAdded = false;  
        }  
    }  
  
    @Override  
    public void onDestroy() {  
    	removeView();  
    	unregisterReceiver(m_BroadcastReceiver);
        super.onDestroy();  
    }  
}  