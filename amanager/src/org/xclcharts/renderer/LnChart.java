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

package org.xclcharts.renderer;

import java.util.List;

import org.xclcharts.common.CurveHelper;
import org.xclcharts.common.MathHelper;
import org.xclcharts.common.PointHelper;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.info.PlotAxisTick;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

/**
 * @ClassName XChart
 * @Description 所有线类，如折线，曲线等图表类的基类,在此主要用于Key及坐标系的绘制。
 * 
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com) 
 *         
 */

public class LnChart extends AxesChart {
	
	private static final String TAG = "LnChart";
	
	private PointF[] BezierControls ;		
				
	public LnChart() {
		
		//初始化图例
		if(null != plotLegend)
		{
			plotLegend.show();
			plotLegend.setType(XEnum.LegendType.ROW);
			plotLegend.setHorizontalAlign(XEnum.HorizontalAlign.LEFT);
			plotLegend.setVerticalAlign(XEnum.VerticalAlign.TOP);
			plotLegend.hideBox();
		} 
		
		categoryAxisDefaultSetting();
		dataAxisDefaultSetting();
		
		//setAxesClosed(true);		
	}
		
	
	/**
	 * 绘制底部标签轴
	 */
	@Override
	protected void drawClipDataAxisGridlines(Canvas canvas) 
	{						
		// 与柱形图不同，无须多弄一个
		float XSteps = 0.0f,YSteps = 0.0f;
		
		// 数据轴数据刻度总个数
		int tickCount = dataAxis.getAixTickCount();
		int labeltickCount = tickCount;
		
		if( 0 == tickCount)
		{
			Log.e(TAG,"数据库数据源为0!");
			return ;
		}else if (1 == tickCount)  //label仅一个时右移
			    labeltickCount = tickCount - 1 ;
					
		// 标签轴(X 轴)		
		float axisX = 0.0f,axisY = 0.0f,currentX = 0.0f,currentY = 0.0f;
		// 标签
		double currentTickLabel = 0d;
		// 轴位置
		XEnum.Location pos = getDataAxisLocation();
				
		//步长
		switch(pos)
		{			 
			case LEFT: //Y
			case RIGHT:			
				YSteps = getVerticalYSteps(labeltickCount) ;	
											
				if( XEnum.Location.RIGHT  == pos)
				{    //显示在右边
					currentX = axisX = plotArea.getRight();
				}else{ //显示在左边
					currentX = axisX = plotArea.getLeft();
				}			
				
				currentY = axisY = plotArea.getBottom();
				break;						
			case TOP: //X
			case BOTTOM:
				XSteps = getVerticalXSteps(labeltickCount);						
				if(XEnum.Location.TOP == pos)
				{
					currentY = axisY = plotArea.getTop();
				}else{
					currentY = axisY = plotArea.getBottom();
				}
				currentX = axisX = plotArea.getLeft();
				break;			
		}
		
		mLstDataTick.clear();			
		//绘制
		for (int i = 0; i < tickCount +1; i++)
		{					
			switch(pos)
			{				 
				case LEFT: //Y
				case RIGHT:								
					// 依起始数据坐标与数据刻度间距算出上移高度	
					currentY =  sub(plotArea.getBottom(), i * YSteps);
								
					// 从左到右的横向网格线
					drawHorizontalGridLines(canvas,plotArea.getLeft(),plotArea.getRight(),
																i,tickCount + 1,YSteps,currentY);
															
					//这个有点问题，要处理下，
					//  隐藏时应当不需要这个，但目前主明细模式下，会有问题，加 了一个都显示不出来
					//  先省略了
					//if(!dataAxis.isShowAxisLabels())continue;
					 
					// 标签					
					currentTickLabel = MathHelper.getInstance().add(
										dataAxis.getAxisMin(),i * dataAxis.getAxisSteps());	
					
					mLstDataTick.add(new PlotAxisTick(i,axisX , currentY, Double.toString(currentTickLabel)));
					break;							
				case TOP: //X
				case BOTTOM:	
					 // 依初超始X坐标与标签间距算出当前刻度的X坐标
					 currentX = add(plotArea.getLeft() , i * XSteps); 
								
					 //绘制竖向网格线
					 drawVerticalGridLines(canvas,plotArea.getTop(),plotArea.getBottom(),
													i ,tickCount + 1,XSteps,currentX);
				
					// if(!dataAxis.isShowAxisLabels())continue;
					 
					// 画上标签/刻度线	
					currentTickLabel = MathHelper.getInstance().add(
											dataAxis.getAxisMin(),i * dataAxis.getAxisSteps());	
										
					mLstDataTick.add(new PlotAxisTick(i,currentX, axisY, Double.toString(currentTickLabel)));
															
					break;	
			} //switch end						
		} //end for	
	}

	
	
//	@Override
	protected int getCategoryAxisCount()
	{
		int tickCount = categoryAxis.getDataSet().size();		
		int labeltickCount = 0;
		if( 0 == tickCount)
		{
			Log.e(TAG,"分类轴数据源为0!");
			return labeltickCount;
		}else if (1 == tickCount)  //label仅一个时右移
		{
			labeltickCount = tickCount ;
			//j++;
		}else{
			labeltickCount = tickCount - 1 ;
		}		
		return labeltickCount;
	}
	
	
	/**
	 * 绘制底部标签轴
	 */
	@Override
	protected void drawClipCategoryAxisGridlines(Canvas canvas) 
	{				
		// 得到标签轴数据集
		List<String> dataSet = categoryAxis.getDataSet();
		// 与柱形图不同，无须多弄一个
		float XSteps = 0.0f,YSteps = 0.0f;
		int j = 0;
	
		int tickCount = dataSet.size();		
		if( 0 == tickCount)
		{
			Log.e(TAG,"分类轴数据源为0!");
			return ;
		}
		else if (1 == tickCount)  //label仅一个时右移
		{			
			j = 1;
		}
		int labeltickCount = getCategoryAxisCount();
			 
							
		// 标签轴(X 轴)
		float axisX = 0.0f,axisY = 0.0f,currentX = 0.0f,currentY = 0.0f;
		
		XEnum.Location pos = getCategoryAxisLocation();
								
		if( XEnum.Location.LEFT == pos || 
				XEnum.Location.RIGHT == pos)
		{						
			YSteps = getVerticalYSteps( labeltickCount) ;
			switch(pos) //Y
			{				 
				case LEFT:
					currentX = axisX = plotArea.getLeft();
					break;
				case RIGHT:	
					currentX = axisX = plotArea.getRight();
					break;
			}
			currentY = axisY = plotArea.getBottom();										
		}else{ //TOP BOTTOM														
			
			XSteps = getVerticalXSteps(labeltickCount);
			switch(pos) //Y
			{				 
				case TOP:
					currentY = axisY = plotArea.getTop();
					break;
				case BOTTOM:	
					currentY = axisY = plotArea.getBottom();					
					break;
			}		
			currentX = axisX = plotArea.getLeft();
		}
					
		mLstCateTick.clear();	
		
		//绘制
		for (int i = 0; i < tickCount ; i++) 
		{			
			switch(pos)
			{				 
				case LEFT: //Y
				case RIGHT:			
					
					// 依起始数据坐标与数据刻度间距算出上移高度									
					currentY = sub(plotArea.getBottom(), j * YSteps);
																							
					// 从左到右的横向网格线
					drawHorizontalGridLines(canvas,plotArea.getLeft(),plotArea.getRight(),
																i,tickCount,YSteps,currentY);
					
					if(!categoryAxis.isShowAxisLabels()) continue;	
					mLstCateTick .add(new PlotAxisTick( axisX,currentY, dataSet.get(i)));
					
					break;							
				case TOP: //X
				case BOTTOM:			
					// 依初超始X坐标与标签间距算出当前刻度的X坐标			
					currentX = add(plotArea.getLeft() , (j) * XSteps); 
										
					 //绘制竖向网格线
					 drawVerticalGridLines(canvas,plotArea.getTop(),plotArea.getBottom(),
													i ,tickCount,XSteps,currentX);
				
					 if(!categoryAxis.isShowAxisLabels()) continue;	
					 									
					mLstCateTick .add(new PlotAxisTick( currentX,axisY, dataSet.get(i)));	
						
					break;			
			} //switch end
			j++;
		} //end for
	
	}
	
		
	@Override
	public boolean isPlotClickArea(float x,float y)
	{				
		if(!getListenItemClickStatus())return false;	
		
		if(Float.compare(x , getLeft()) == -1 ) return false;
		if(Float.compare(x,  getRight() ) == 1 ) return false;	
		
		if(Float.compare( y , getPlotArea().getTop() ) == -1  ) return false;
		if(Float.compare( y , getPlotArea().getBottom() ) == 1  ) return false;		
		return true;
	}
	
	/**
	 * 返回当前点击点的信息
	 * @param x 点击点X坐标
	 * @param y	点击点Y坐标
	 * @return 返回对应的位置记录
	 */
	public PointPosition getPositionRecord(float x,float y)
	{		
		return getPointRecord(x,y);
	}
			
	//遍历曲线
	protected void renderBezierCurveLine(Canvas canvas,Paint paint,Path bezierPath ,
										List<PointF> lstPoints )
	{		
		if(null == BezierControls ) BezierControls = new PointF[2];				
		paint.setStyle(Style.STROKE);
		
		int count = lstPoints.size();
		if( count <= 2  ) return; //没有或仅一个点就不需要了
				
		
		if(count == 3)
		{			
			if(null == bezierPath)bezierPath = new Path();
				bezierPath.moveTo(lstPoints.get(0).x, lstPoints.get(0).y);
				
				PointF ctl3 = PointHelper.percent(lstPoints.get(1),0.5f, lstPoints.get(2),0.8f) ;
				bezierPath.quadTo(ctl3.x, ctl3.y, lstPoints.get(2).x, lstPoints.get(2).y);		
			
			canvas.drawPath(bezierPath, paint);
			bezierPath.reset();					
			return;
		}
		
		
		float axisMinValue = plotArea.getBottom();
		
		for(int i = 0;i<count;i++)
		{					
			if(i<3) continue;
			
			//连续两个值都为0,控制点有可能会显示在轴以下，则此种情况下，将其处理为直线			
			if(lstPoints.get(i - 1).y >= axisMinValue && lstPoints.get(i).y >= axisMinValue)
			 {
				
				CurveHelper.curve3( lstPoints.get(i-2),  
									lstPoints.get(i-1), 
									lstPoints.get(i-3),
									lstPoints.get(i), 
									BezierControls);
				
				if(null == bezierPath)bezierPath = new Path();
				bezierPath.reset();					
				bezierPath.moveTo(lstPoints.get(i-2).x, lstPoints.get(i-2).y);
				
				bezierPath.quadTo(BezierControls[0].x, BezierControls[0].y,
						lstPoints.get(i-1).x, lstPoints.get(i-1).y);
				
				canvas.drawPath(bezierPath, paint);		
				bezierPath.reset();
				
				canvas.drawLine(lstPoints.get(i-1).x, lstPoints.get(i-1).y, 
						         lstPoints.get(i).x, lstPoints.get(i).y, paint);
					
				continue;
			 }
									
			CurveHelper.curve3( lstPoints.get(i-2),  
								lstPoints.get(i-1), 
								lstPoints.get(i-3),
								lstPoints.get(i), 
								BezierControls);
			
			if(lstPoints.get(i - 1).y >= axisMinValue )
			{
				continue; 
			}else{							 
				renderBezierCurvePath(canvas,paint,bezierPath,
							lstPoints.get(i-2), lstPoints.get(i -1 ), 
							BezierControls );
			}
							
		}			
	
	
		if(count> 3)
		{			
			PointF stop  = lstPoints.get(count-1);
			//PointF start = lstPoints.get(lstPoints.size()-2);						
			CurveHelper.curve3(lstPoints.get(count-2),  
										stop, 
										lstPoints.get(count-3),
										stop, 
										BezierControls);
			
			renderBezierCurvePath(canvas,paint,bezierPath,
								lstPoints.get(count-2), 
								lstPoints.get(count-1), 
								BezierControls );			 						
		}				
		
	}


	//绘制曲线
	private void renderBezierCurvePath(Canvas canvas,Paint paint,Path bezierPath,
					PointF start,PointF stop,PointF[] bezierControls)
	{		
		if(null == bezierPath)bezierPath = new Path();
		bezierPath.reset();					
		bezierPath.moveTo(start.x, start.y);
		bezierPath.cubicTo( bezierControls[0].x, bezierControls[0].y, 
				bezierControls[1].x, bezierControls[1].y, 
				stop.x, stop.y);	
						
		canvas.drawPath(bezierPath, paint);		
		bezierPath.reset();
	}
	
	/////////////////////////////////////////
	/////////////////////////////////////////	
}
