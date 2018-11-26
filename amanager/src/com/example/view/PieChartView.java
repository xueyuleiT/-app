/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */

package com.example.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.xclcharts.chart.PieChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotLegend;

import com.example.model.ConsumerModel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @ClassName PieChart02View
 * @Description  平面饼图的例子
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */

public class PieChartView extends DemoView {

	int size = 0;
	 private String TAG = "PieChart02View";
	 private PieChart chart = new PieChart();	
	 private LinkedList<PieData> chartData = new LinkedList<PieData>();
	 Paint mPaintToolTip = new Paint(Paint.ANTI_ALIAS_FLAG);
	 HashMap<String,ArrayList<ConsumerModel>> hashMap;
	 private int mSelectedID = -1;
	
	 public PieChartView(Context context,HashMap<String,ArrayList<ConsumerModel>> hashMap) {
		super(context);
		this.hashMap = hashMap;
		initView();
	 }	
	
	 public PieChartView(Context context, AttributeSet attrs,HashMap<String,ArrayList<ConsumerModel>> hashMap){   
        super(context, attrs);   
        this.hashMap = hashMap;
        initView();
	 }
	 
	 public PieChartView(Context context, AttributeSet attrs, int defStyle,HashMap<String,ArrayList<ConsumerModel>> hashMap) {
		super(context, attrs, defStyle);
		this.hashMap = hashMap;
		initView();
	 }
	 
	 private void initView()
	 {
		 Iterator iter = hashMap.entrySet().iterator();
		 while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String) entry.getKey();
				size += ((ArrayList<ConsumerModel>) entry.getValue()).size();
		 }
		 chartDataSet(hashMap);	
		 chartRender();
	 }	 		 	
	
	@Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       //图所占范围大小
        chart.setChartRange(w,h);
    }  	
	

	private void chartRender()
	{
		try {										
			//标签显示(隐藏，显示在中间，显示在扇区外面,折线注释方式)
			chart.setLabelStyle(XEnum.SliceLabelStyle.BROKENLINE);		
			chart.getLabelBrokenLine().setLinePointStyle(XEnum.LabelLinePoint.END);
			chart.syncLabelColor();
			chart.syncLabelPointColor();
			
			
			//图的内边距
			//注释折线较长，缩进要多些
			int [] ltrb = new int[4];
			ltrb[0] = DensityUtil.dip2px(getContext(), 40); //left	
			ltrb[1] = DensityUtil.dip2px(getContext(), 45); //top	
			ltrb[2] = DensityUtil.dip2px(getContext(), 40); //right
			ltrb[3] = DensityUtil.dip2px(getContext(), 40); //bottom	
											
			chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
			
			//设定数据源
			chart.setDataSource(chartData);												
		
			//标题
			chart.setTitle("客户百分比");
			chart.addSubtitle("(案场管理系统)");
			//chart.setTitleVerticalAlign(XEnum.VerticalAlign.MIDDLE);
			chart.getLabelPaint().setTextSize(40);
			//隐藏渲染效果
//			chart.hideGradient();
			//显示边框
			chart.showRoundBorder();
			
			//激活点击监听
			chart.ActiveListenItemClick();
			chart.showClikedFocus();
			chart.disablePanMode();
			
			//显示图例
			PlotLegend legend = chart.getPlotLegend();	
			legend.show();
			legend.setHorizontalAlign(XEnum.HorizontalAlign.CENTER);
			legend.setVerticalAlign(XEnum.VerticalAlign.BOTTOM);
			legend.showBox();
			
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	public void chartDataSet(HashMap<String, ArrayList<ConsumerModel>> hashMap)
	{
		this.hashMap = hashMap;
		
		Random random = new Random();
		double pecent = 0;
		int count = 0;
		boolean flag = false;
		Iterator iter = hashMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			int val = ((ArrayList<ConsumerModel>) entry.getValue()).size();
			
			if(val != 0){
				count += val;
				double pecentOf =(Math.round(val*10000/size)/100.0); 
			//设置图表数据源				
			PieData pieData = new PieData(key,key+":"+pecentOf+"% ("+val+"人)",pecentOf,(int)Color.rgb(random.nextInt(256),
					random.nextInt(256), random.nextInt(256))) ;
			pecent += pecentOf;
			if(!flag){
				pieData.setSelected(true);
				pieData.setItemLabelRotateAngle(45.f);
				flag = true;
			}
			chartData.add(pieData);
			}
		}
		if(pecent < 100 && count < size){
			chartData.add(new PieData("其它:","其它"+(100-pecent)+"% ("+(size - count)+"人)",100-pecent,(int)Color.rgb(random.nextInt(256),
					random.nextInt(256), random.nextInt(256))));
		}
	}
	@Override
	public void render(Canvas canvas) {
		 try{
	            chart.render(canvas);
	        } catch (Exception e){
	        	Log.e(TAG, e.toString());
	        }
	}


	@Override
	public List<XChart> bindChart() {
		List<XChart> lst = new ArrayList<XChart>();
		lst.add(chart);		
		return lst;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);		
		if(event.getAction() == MotionEvent.ACTION_UP) 
		{						
			triggerClick(event.getX(),event.getY());
		}
		return true;
	}
	

	//触发监听
	private void triggerClick(float x,float y)
	{		
		if(!chart.getListenItemClickStatus())return;
		ArcPosition record = chart.getPositionRecord(x,y);			
		if( null == record) return;
		
		PieData pData = chartData.get(record.getDataID());		
		
		boolean isInvaldate = true;		
		for(int i=0;i < chartData.size();i++)
		{	
			PieData cData = chartData.get(i);
			if(i == record.getDataID())
			{
				if(cData.getSelected()) 
				{
					isInvaldate = false;
					break;
				}else{
					cData.setSelected(true);	
				}
			}else
				cData.setSelected(false);			
		}
		
		
		//显示选中框
		chart.showFocusArc(record,pData.getSelected());
		chart.getFocusPaint().setStyle(Style.STROKE);
		chart.getFocusPaint().setStrokeWidth(5);		
		chart.getFocusPaint().setColor(Color.GREEN);	
		chart.getFocusPaint().setAlpha(100);
		
		
		//在点击处显示tooltip
		mPaintToolTip.setColor(Color.RED);		
		mPaintToolTip.setTextSize(30);
		chart.getToolTip().setCurrentXY(x,y);		
		chart.getToolTip().addToolTip(pData.getLabel(),mPaintToolTip);	
											
		this.invalidate();						
	}
	 
}
