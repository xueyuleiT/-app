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
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.5
 */
package org.xclcharts.chart;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.LnChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.line.PlotDot;
import org.xclcharts.renderer.line.PlotDotRender;
import org.xclcharts.renderer.plot.PlotQuadrant;
import org.xclcharts.renderer.plot.PlotQuadrantRender;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.Log;

/**
 * @ClassName BubbleChart
 * @Description  气泡图基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class BubbleChart extends LnChart{

	private static  String TAG="BubbleChart";
	
	//数据源
	private List<BubbleData> mDataset;
	
	//分类轴的最大，最小值
	private double mMaxValue = 0d;
	private double mMinValue = 0d;
		
	//用于格式化标签的回调接口
	private IFormatterTextCallBack mLabelFormatter;
	
	//指定气泡半径的最大大小
	private float mBubbleMaxSize = 0.0f;
	private float mBubbleMinSize = 0.0f;
	
	//指定最大气泡大小所表示的实际值，最大气泡大小由 BubbleMaxSize 设置。
	private float mBubbleScaleMax = 0.0f;
	private float mBubbleScaleMin = 0.0f;
	
	private Paint mPaintPoint = null;	
	private PlotDot mPlotDot = new PlotDot();
	
	private Paint mPaintBorderPoint = null;	
	
	//四象限类
	private PlotQuadrantRender mPlotQuadrant = null;

	public BubbleChart()
	{
		initChart();
	}
	
	@Override
	public XEnum.ChartType getType()
	{
		return XEnum.ChartType.BUBBLE;
	}
	
	private void initChart()
	{
		if(null != mPlotDot)
			mPlotDot.setDotStyle(XEnum.DotStyle.DOT);
		
		categoryAxisDefaultSetting();
		dataAxisDefaultSetting();
		
		this.setAxesClosed(true);
	}
	
	protected void categoryAxisDefaultSetting()
	{		
		if(null != categoryAxis)
			categoryAxis.setHorizontalTickAlign(Align.CENTER);
	}
	
	protected void dataAxisDefaultSetting()
	{		
		if(null != dataAxis)
			dataAxis.setHorizontalTickAlign(Align.LEFT);
	}
	
	
	/**
	 * 指定气泡半径的最大大小
	 * @param maxSize 最大气泡半径(px)
	 */
	public void setBubbleMaxSize(float maxSize)
	{
		mBubbleMaxSize = maxSize;
	}
		
	/**
	 * 指定气泡半径的最小大小
	 * @param minSize 最小气泡半径(px)
	 */
	public void setBubbleMinSize(float minSize)
	{
		mBubbleMinSize = minSize;
	}

	
	/**
	 * 指定最大气泡大小所表示的实际值
	 * @param scaleMax 最大气泡实际值
	 */
	public void setBubbleScaleMax(float scaleMax)
	{
		mBubbleScaleMax = scaleMax;
	}
	
	/**
	 * 指定最小气泡大小所表示的实际值
	 * @param scaleMin  最小气泡实际值
	 */
	public void setBubbleScaleMin(float scaleMin)
	{
		mBubbleScaleMin = scaleMin;
	}				
	
	/**
	 * 分类轴的数据源
	 * @param categories 标签集
	 */
	public void setCategories( List<String> categories)
	{
		if(null != categoryAxis)categoryAxis.setDataBuilding(categories);
	}
	
	/**
	 *  设置数据轴的数据源
	 * @param dataSeries 数据序列
	 */
	public void setDataSource( List<BubbleData> dataSeries)
	{		
		this.mDataset = dataSeries;		
	}	
	
	/**
	 *  显示数据的数据轴最大值
	 * @param value 数据轴最大值
	 */
	public void setCategoryAxisMax( double value)
	{
		mMaxValue = value;
	}	
	
	/**
	 * 设置分类轴最小值
	 * @param value 最小值
	 */
	public void setCategoryAxisMin( double value)
	{
		mMinValue = value;
	}	
	
	/**
	 * 设置标签的显示格式
	 * @param callBack 回调函数
	 */
	public void setDotLabelFormatter(IFormatterTextCallBack callBack) {
		this.mLabelFormatter = callBack;
	}
	
	/**
	 * 返回标签显示格式
	 * 
	 * @param value 传入当前值
	 * @return 显示格式
	 */
	protected String getFormatterDotLabel(String text) {
		String itemLabel = "";
		try {
			itemLabel = mLabelFormatter.textFormatter(text);
		} catch (Exception ex) {
			itemLabel = text;
		}
		return itemLabel;
	}
	

	private float calcRaidus(float scale,float scaleTotalSize,float bubbleRadius)
	{
		 return  ( bubbleRadius  * (scale / scaleTotalSize) );		
	}
	
	
	/**
	 * 返回四象限绘制类
	 * @return
	 */
	public PlotQuadrant getPlotQuadrant()
	{
		if(null == mPlotQuadrant) mPlotQuadrant = new PlotQuadrantRender();
		return mPlotQuadrant;
	}
	
	
	
	/**
	 * 绘制点的画笔
	 * @return
	 */
	public Paint getPointPaint()
	{
		if(null == mPaintPoint)
		{
			mPaintPoint = new Paint(Paint.ANTI_ALIAS_FLAG);			
		}
		return mPaintPoint;
	}
		
	public Paint getPointBorderPaint()
	{
		if(null == mPaintBorderPoint)
		{
			mPaintBorderPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaintBorderPoint.setStyle(Style.STROKE);
			mPaintBorderPoint.setStrokeWidth(2);
		}
		return mPaintBorderPoint;
	}
	
	/**
	 * 绘制象限
	 * @param canvas 画布
	 */
	private void drawQuadrant(Canvas canvas )
	{
		if(!getPlotQuadrant().isShow())return;
		
		float axisScreenWidth = getPlotScreenWidth(); 
    	float axisScreenHeight = getPlotScreenHeight();
		float axisDataHeight = (float) dataAxis.getAxisRange(); 	
						
		Double xValue = getPlotQuadrant().getQuadrantXValue();
	    Double yValue = getPlotQuadrant().getQuadrantYValue();	    
	    	   
	    //对应的Y坐标
	    float centerY = (float) (axisScreenHeight * ( (yValue - dataAxis.getAxisMin() ) / axisDataHeight)) ;  
	                	
    	//对应的X坐标	  	  
	    float centerX = (float) (axisScreenWidth * ( (xValue - mMinValue ) / (mMaxValue - mMinValue))) ;  
    	    	   
	    mPlotQuadrant.drawQuadrant(canvas, centerX, centerY, 
			   plotArea.getLeft(), plotArea.getPlotTop(), plotArea.getPlotRight(), plotArea.getBottom());	    
	}
			
				
	private void renderPoints( Canvas canvas, BubbleData bd ,int dataID)
	{			
		float initX =  plotArea.getLeft();
        float initY =  plotArea.getBottom();
		float lineStartX = initX;
        float lineStartY = initY;
        float lineStopX = 0.0f;
        float lineStopY = 0.0f;        
    	
    	float axisScreenWidth = getPlotScreenWidth(); 
    	float axisScreenHeight = getPlotScreenHeight();
		float axisDataHeight = (float) dataAxis.getAxisRange(); 	
		
		//得到标签对应的值数据集		
		LinkedHashMap<Double,Double> chartValues = bd.getDataSet();	
		if(null == chartValues) return ;
															
	    //画出数据集对应的线条				
		int j = 0;
		int childID = 0;
		float YvaluePostion = 0.0f,XvaluePostion = 0.0f;

		if(Float.compare(mBubbleScaleMax, mBubbleScaleMin)  == 0  ) 
		{
			Log.e(TAG,"没有指定用于决定气泡大小的最大最小实际数据值。");
			return;
		}
		
		if( Float.compare(mBubbleMaxSize, mBubbleMinSize)  == 0 ) 
		{
			Log.e(TAG,"没有指定气泡本身，最大最小半径。");
			return;
		}
		
		float scale =  mBubbleScaleMax - mBubbleScaleMin;
		float size = mBubbleMaxSize - mBubbleMinSize;
		
		List<Double> lstBubble = bd.getBubble();
		int bubbleSize =  lstBubble.size() ;
		double bubble = 0;
		float curRadius = 0.0f;		
		//汽泡颜色
		getPointPaint().setColor(bd.getColor());	
		//边框颜色
		if(bd.getBorderColor() != -1) getPointBorderPaint().setColor(bd.getBorderColor());	
		
		float itemAngle = bd.getItemLabelRotateAngle();
				
		Iterator iter = chartValues.entrySet().iterator();
		while(iter.hasNext()){
			    Entry  entry=(Entry)iter.next();
			
			    Double xValue =(Double) entry.getKey();
			    Double yValue =(Double) entry.getValue();	
			    			    
			    //对应的Y坐标
			    YvaluePostion = (float) (axisScreenHeight * ( (yValue - dataAxis.getAxisMin() ) / axisDataHeight)) ;  
			                	
            	//对应的X坐标	  	  
			    XvaluePostion = (float) (axisScreenWidth * ( (xValue - mMinValue ) / (mMaxValue - mMinValue))) ;  
            
            	if(j == 0 )
				{	                		
            		lineStartX = add(initX , XvaluePostion);
					lineStartY = sub(initY , YvaluePostion);
					
					lineStopX = lineStartX ;
					lineStopY = lineStartY;														
				}else{
					lineStopX =  add(initX , XvaluePostion);  
					lineStopY =  sub(initY , YvaluePostion);
				}            
            	            	
        		if(j >= bubbleSize )
        		{
        			continue;
        		}else{
        			bubble = lstBubble.get(j);
        		}
        		
        		curRadius = calcRaidus(scale, size, (float) bubble);
        		
        		if(Float.compare(curRadius, 0.0f) == 0 
        				|| Float.compare(curRadius, 0.0f) == -1) 
        		{
        			//Log.e(TAG,"当前气泡半径小于或等于0。");
        			continue;
        		}

        		mPlotDot.setDotRadius( curRadius); 
        		
            	PlotDotRender.getInstance().renderDot(
            			canvas,mPlotDot,
            			lineStartX,lineStartY,lineStopX,lineStopY,
            			getPointPaint());
            	                        	
            	savePointRecord(dataID,childID, lineStopX + mMoveX ,lineStopY + mMoveY,
				            	lineStopX  - 2*curRadius + mMoveX , lineStopY - curRadius + mMoveY,
				            	lineStopX  + curRadius + mMoveX, lineStopY + curRadius + mMoveY);
            	            	
            	if(bd.getBorderColor() != -1)
            	{
            		canvas.drawCircle(lineStopX,lineStopY, curRadius, getPointBorderPaint());
            	}
            	            	            
    			childID++;
             	    			            	
            	if(bd.getLabelVisible())
            	{            			
            		//请自行在回调函数中处理显示格式
                    DrawHelper.getInstance().drawRotateText(getFormatterDotLabel(
                            Double.toString(xValue)+","+ Double.toString(yValue)
                            +" : "+Double.toString(bubble)),
                            lineStopX,lineStopY, itemAngle, canvas, bd.getDotLabelPaint());
            	}  
            	            	            	
				lineStartX = lineStopX;
				lineStartY = lineStopY;

				j++;	              								
		}								
	}
		

	/**
	 * 绘制图
	 */
	private boolean renderPlot(Canvas canvas)
	{
		//检查是否有设置分类轴的最大最小值		
		if(mMaxValue == mMinValue && 0 == mMaxValue)
		{
			Log.e(TAG,"请检查是否有设置分类轴的最大最小值。");
			return false;
		}
		if(null == mDataset)
		{
			Log.e(TAG,"数据源为空.");
			return false;
		}		
		
		//绘制四象限
		drawQuadrant(canvas);
				
		//开始处 X 轴 即分类轴              	
		int size = mDataset.size();
		for(int i=0;i<size;i++)
		{																	
			BubbleData bd =  mDataset.get(i);							
			renderPoints( canvas, bd,i);	
		}	
		
		return true;
	}	

	/////////////////////////////////////////////
	
	protected void drawClipPlot(Canvas canvas)
	{
		renderPlot(canvas);
	}
	
	protected void drawClipLegend(Canvas canvas)
	{
		plotLegend.renderBubbleKey(canvas,mDataset);		
	}
	/////////////////////////////////////////////

}
