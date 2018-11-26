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
 * @version 1.0
 */
package org.xclcharts.chart;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.LnChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.line.PlotCustomLine;
import org.xclcharts.renderer.line.PlotDot;
import org.xclcharts.renderer.line.PlotDotRender;
import org.xclcharts.renderer.line.PlotLine;

import android.graphics.Canvas;
import android.util.Log;

/**
 * @ClassName LineChart
 * @Description  线图基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class LineChart extends LnChart{
	
	private  static final  String TAG ="LineChart";
	
	//数据源
	protected List<LineData> mDataSet;

	//用于绘制定制线(分界线)
	private PlotCustomLine mCustomLine = null;
	//当线与轴交叉时是否不断开连接
	private boolean mLineAxisIntersectVisible = true;
	//图例
	private List<LnData> mLstKey = new ArrayList<LnData>();			
	
	public LineChart()
	{
		
	}
	
	@Override
	public XEnum.ChartType getType()
	{
		return XEnum.ChartType.LINE;
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
		 * @param dataSet 数据源
		 */
		public void setDataSource( LinkedList<LineData> dataSet)
		{										
			this.mDataSet = dataSet;		
		}			
						
		/**
		 * 设置定制线值
		 * @param customLineDataset 定制线数据集合
		 */
		public void setDesireLines( List<CustomLineData> customLineDataset)
		{
			if(null == mCustomLine) mCustomLine = new PlotCustomLine();
			mCustomLine.setCustomLines(customLineDataset);
		}
		
		
		/**
		 *  设置当值与底轴的最小值相等时，线是否与轴连结显示. 默认为False
		 * @param visible 是否连接
		 */
		public void setLineAxisIntersectVisible( boolean visible)
		{
			mLineAxisIntersectVisible = visible;
		}
		
		/**
		 * 返回当值与底轴的最小值相等时，线是否与轴连结的当前状态
		 * @return 状态
		 */
		public boolean getLineAxisIntersectVisible()
		{
			return mLineAxisIntersectVisible;
		}
		
		/**
		 * 绘制线
		 * @param canvas	画布
		 * @param bd		数据类
		 * @param type		处理类型
		 */
		private boolean renderLine(Canvas canvas, LineData bd,String type,int dataID)
		{
			if(null == categoryAxis || null == dataAxis) return false;
			
			if(null == bd)
			{
				Log.e(TAG,"传入的线的数据序列参数为空.");
				return false;
			}
			
			float initX =  plotArea.getLeft();
            float initY =  plotArea.getBottom();             
			float lineStartX = initX,lineStartY = initY;         
            float lineStopX = 0.0f,lineStopY = 0.0f;        
            													
			//得到分类轴数据集
			List<String> dataSet =  categoryAxis.getDataSet();
			if(null == dataSet ||dataSet.size() == 0){
				Log.e(TAG,"分类轴数据为空.");
				return false;
			}		
			//数据序列
			List<Double> chartValues = bd.getLinePoint();
			if(null == chartValues ||chartValues.size() == 0 )
			{
				Log.e(TAG,"当前分类的线数据序列值为空.");
				return false;
			}
				
			//步长
			float axisScreenHeight = getPlotScreenHeight();
			float axisDataHeight = (float) dataAxis.getAxisRange();	
			float XSteps = 0.0f;	
			int j = 0,childID = 0;	
			int tickCount = dataSet.size();		
			if( 0 == tickCount)
			{
				Log.e(TAG,"分类轴数据源为0!");
				return false;
			}else if (1 == tickCount)  //label仅一个时右移
			{
				j = 1;
			}			
			int labeltickCount = getCategoryAxisCount();
			XSteps = getVerticalXSteps(labeltickCount);
			
			
			float itemAngle = bd.getItemLabelRotateAngle();
					
		    //画线
			for(Double bv : chartValues)
            {																	
				//参数值与最大值的比例  照搬到 y轴高度与矩形高度的比例上来 	                                             	
            	float vaxlen = (float) MathHelper.getInstance().sub(bv, dataAxis.getAxisMin());				
				float fvper = div( vaxlen,axisDataHeight );
				float valuePostion = mul(axisScreenHeight, fvper);			    
            
            	lineStopX = add(initX , j * XSteps);        	
            	lineStopY = sub(initY , valuePostion);  
            	        	
            	if(0 == j)
            	{
            		lineStartX = lineStopX;
            		lineStartY = lineStopY;
            	}
                        	
            	if( getLineAxisIntersectVisible() == false &&
            			Double.compare(bv, dataAxis.getAxisMin()) == 0 )
            	{
            		//如果值与最小值相等，即到了轴上，则忽略掉  
            		lineStartX = lineStopX;
    				lineStartY = lineStopY;

    				j++;
            	}else{
	            	PlotLine pLine = bd.getPlotLine();           
	            	if(type.equalsIgnoreCase("LINE"))
	            	{
	            		
	            		if(getLineAxisIntersectVisible() == true ||
	            					Float.compare(lineStartY, initY) != 0 )	
	            		{	            		
	            			DrawHelper.getInstance().drawLine(bd.getLineStyle(), 
	            					lineStartX ,lineStartY ,lineStopX ,lineStopY,
	            					canvas,pLine.getLinePaint());		            			
	            		}
	            	}else if(type.equalsIgnoreCase("DOT2LABEL")){
	            		
	            		float radius = 0.0f;
	            		if(!pLine.getDotStyle().equals(XEnum.DotStyle.HIDE))
	                	{                		       	
	                		PlotDot pDot = pLine.getPlotDot();	        
	                		radius = pDot.getDotRadius();
	                		float rendEndX  = lineStopX  + radius;  
	                		
	                		PlotDotRender.getInstance().renderDot(canvas,pDot,
	                				lineStartX ,lineStartY ,
	                				lineStopX ,lineStopY,
	                				pLine.getDotPaint()); //标识图形            		
	                			                		
	                		savePointRecord(dataID,childID, lineStopX  + mMoveX, lineStopY  + mMoveY,
	                				lineStopX - radius + mMoveX,lineStopY - radius + mMoveY,
	                				lineStopX + radius + mMoveX,lineStopY + radius + mMoveY);
	                		
	                		childID++;
	                		
	            			lineStopX = rendEndX;	            			
	                	}
	            		
	            		if(bd.getLabelVisible()) //标签
	                	{	                	            
	            			bd.getPlotLabel().drawLabel(canvas, pLine.getDotLabelPaint(), 
	            					getFormatterItemLabel(bv), lineStopX - radius, lineStopY, itemAngle,bd.getLineColor());
	                	}
	            			            		
	            	}else{
	            		Log.e(TAG,"未知的参数标识。"); //我不认识你，我不认识你。
	            		return false;
	            	}      				
            	
					lineStartX = lineStopX;
					lineStartY = lineStopY;
	
					j++;
            	} //if(bv != dataAxis.getAxisMin())
            } 				
			return true;
		}
					
		/**
		 * 绘制图表
		 */
		private boolean renderVerticalPlot(Canvas canvas)
		{											
			if(null == mDataSet) 
			{
				Log.e(TAG,"数据轴数据为空.");
				return false;
			}			
			
			mLstKey.clear();
			String key = "";
			//开始处 X 轴 即分类轴                  
			int count = mDataSet.size();
			for(int i=0;i<count;i++)
			{								
				if(renderLine(canvas,mDataSet.get(i),"LINE",i) == false) 
					return false;;
				if(renderLine(canvas,mDataSet.get(i),"DOT2LABEL",i) == false) 
					return false;;
				key = mDataSet.get(i).getLineKey();				
				if("" != key && key.length() > 0)
					mLstKey.add(mDataSet.get(i));
			}			
			
			return true;
		}	

		/////////////////////////////////////////////
				
		@Override
		protected void drawClipPlot(Canvas canvas)
		{
			if(renderVerticalPlot(canvas) == true)
			{				
				if(null != mCustomLine) //画横向定制线
				{
					mCustomLine.setVerticalPlot(dataAxis, plotArea, getAxisScreenHeight());
					mCustomLine.renderVerticalCustomlinesDataAxis(canvas);	
				}
				
			}
		}
			
		@Override
		protected void drawClipLegend(Canvas canvas)
		{
			plotLegend.renderLineKey(canvas, mLstKey);
			mLstKey.clear();
		}
		/////////////////////////////////////////////

}
