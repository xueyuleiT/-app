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

import java.util.List;

import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.XEnum;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

/**
 * @ClassName RoseChart
 * @Description  南丁格尔图基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class RoseChart extends PieChart{
	
	private  static final  String TAG="PieChart";
	
	private Paint mPaintInner = null;

	public RoseChart() {
		// TODO Auto-generated constructor stub
			
		initChart();
	}
	
	@Override
	public XEnum.ChartType getType()
	{
		return XEnum.ChartType.ROSE;
	}
	
	
	private void initChart()
	{										
		//深色内环
		if(null == mPaintInner)
		{
			mPaintInner = new Paint();
			//mPaintInner.setColor(Color.DKGRAY);
			mPaintInner.setColor(Color.rgb(68, 68, 68));
			mPaintInner.setStyle(Style.FILL);		
			mPaintInner.setAntiAlias(true);		
		}
				
		//白色标签
		if(null != getLabelPaint())
		{
			getLabelPaint().setColor(Color.WHITE);
			getLabelPaint().setTextSize(22);
			getLabelPaint().setTextAlign(Align.CENTER);	
		}
	}
	
	/**
	 * 开放内部背景画笔
	 * @return 画笔
	 */
	public Paint getInnerPaint()
	{
		return mPaintInner;
	}
	
	@Override 
	protected boolean validateParams()
	{
		return true;
	}
	
	/**
	 * 绘制图
	 */
	protected boolean renderPlot(Canvas canvas)
	{			 							
			//计算中心点坐标
			float cirX = plotArea.getCenterX();
		    float cirY = plotArea.getCenterY();
	        float radius = getRadius();
	    	        
	        //外环
			canvas.drawCircle(cirX,cirY,radius,mPaintInner); 
	    
	        float percentage = 0.0f;		 		
	 		float newRaidus = 0.0f;		
	 		
	 		//数据源
	 		List<PieData> chartDataSource = this.getDataSource();
	 		if(null == chartDataSource || chartDataSource.size() == 0)
	 		{
	 			Log.e(TAG,"数据源为空.");
	 			return false;
	 		}
			
			//依参数个数，算出总个要算多少个扇区的角度
			percentage = 360 / chartDataSource.size();
			//percentage = (float)(Math.round(percentage *100))/100; 		
			percentage = div(mul(percentage,100),100);
			
	        for(PieData cData : chartDataSource)
			{
	        	geArcPaint().setColor(cData.getSliceColor());	
				
				//将百分比转换为新扇区的半径    
				double p = cData.getPercentage()/ 100;					
				newRaidus = mul(radius,dtof(p));				
				newRaidus = div(mul(newRaidus,100),100);
				
				//newRaidus = (float) (radius * (cData.getPercentage()/ 100));  
	            //newRaidus = (float)(Math.round(newRaidus *100))/100;    
	            
	            //在饼图中显示所占比例   
	            RectF nRF = new RectF(sub(cirX , newRaidus),sub(cirY , newRaidus),
	            					  add(cirX , newRaidus),add(cirY , newRaidus));  
	            canvas.drawArc(nRF, mOffsetAngle, percentage, true, geArcPaint());       
					       	            
	            //标识  
	            String label = cData.getLabel();	            
	            if("" != label)
            	{            			            
	            	//计算百分比标签  
		            PointF point = MathHelper.getInstance().calcArcEndPointXY(
		            			cirX, cirY, radius - radius/2/2, mOffsetAngle + percentage/2); 
		            
            		//请自行在回调函数中处理显示格式
                    DrawHelper.getInstance().drawRotateText( label,
                    		point.x, point.y, cData.getItemLabelRotateAngle(), 
                    		canvas, getLabelPaint());
            	}               
	         
	          //下次的起始角度  
	            mOffsetAngle = add(mOffsetAngle,percentage);
			}			
	        return true;
	}
		

}
