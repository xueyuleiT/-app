package com.manager.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.manager.model.BeizhuModel;
import com.manager.model.CallModel;
import com.manager.model.ConsultModel;
import com.manager.model.ConsumerAccept;
import com.manager.model.ConsumerModel;
import com.manager.model.Model;
import com.manager.model.MyModel;
import com.manager.model.ResponseModel;
import com.manager.model.TaskModel;
import com.manager.model.TextMessage;
import com.manager.model.TodayStatistics;
import com.manager.model.UserInfo;
import com.manager.model.XiaoshouDetail;
import com.manager.model.XiaoshouModel;
import com.manager.sevlet.CoreServlet;
import com.sina.sae.util.SaeUserInfo;

public class DataManager {
	public static String rURL="jdbc:mysql://r.rdc.sae.sina.com.cn:3307/app_anchang";
	public static String wURL="jdbc:mysql://w.rdc.sae.sina.com.cn:3307/app_anchang";
	private static byte[] lock = new byte[0];
	public  static byte[] lockWrite = new byte[0];
	public static int setTask(String num,String dayNum){
		CurrentTime.dayNum = dayNum;
		int count = 0;
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		
		try {
			Class.forName(Driver).newInstance();
			String sql = "update managerserver set managerserver.task='"+num+"'";
			Connection conW = DriverManager.getConnection(wURL,Username,Password);
			PreparedStatement p = conW.prepareStatement(sql);
			count = p.executeUpdate();
			p.close();
			conW.close();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	
	public static void login(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		
		if(ResponseModel.userArr == null){
			ResponseModel.userArr = new HashMap<Integer, UserInfo>();
		}
		
		
		//app_name为创建的应用名
		//在SAE上创建的studnt表
		String sql = "SELECT managerserver.username,managerid,managerserver.task FROM managerserver where managerserver.phone = '"+req.getParameter("id")+"' and password = '"+req.getParameter("password")+"'";
		// 通过SaeUserInfo提供的静态方法获取应用的access_key和secret_key  对应的SaeUserInfo是自己创建的，使用SAE提供的key
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		try{
		Class.forName(Driver).newInstance();
		Connection con=DriverManager.getConnection(rURL,Username,Password);	
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet rs = (ResultSet) pstmt.executeQuery();
		if(rs != null && rs.next()){
			TextMessage text = new TextMessage();
			text.setFromType("服务器");
			text.setContent(rs.getString("username"));
			text.setName(rs.getString("managerid"));
			text.setFromid(rs.getString("task"));
			resp.getWriter().write(MessageUtil.textMessageToXml(text));
			con.close();
			con = null;
			
//			ResponseModel model = new ResponseModel();
//			CoreServlet.responseArr.put(req.getParameter("phone"), model);
			
			String wSql = "UPDATE managerserver SET online = 'yes' WHERE managerserver.phone ='"+req.getParameter("id")+"'";
			Connection conW=DriverManager.getConnection(wURL,Username,Password);	
			PreparedStatement p = conW.prepareStatement(wSql);
			p.executeUpdate();
			conW.close();
			conW = null;
		}else{
			TextMessage text = new TextMessage();
			text.setFromType("服务器");
			text.setContent("登陆失败");
			resp.getWriter().write(MessageUtil.textMessageToXml(text));
		}
		}catch(Exception e){
			TextMessage text = new TextMessage();
			text.setFromType("服务器");
			text.setContent("登陆失败");
			resp.getWriter().write(MessageUtil.textMessageToXml(text));
			e.printStackTrace();
		}
		
	}
	private static long comparesTime(String t1,String t2,DateFormat dateFormat){
		 long days = 0;
		 long diff = 0;
		try
		{
		    Date d1 = dateFormat.parse(t1);
		    Date d2 = dateFormat.parse(t2);
		    diff = d1.getTime() - d2.getTime();
		}
		catch (Exception e)
		{
		}
		 return diff;
	}
	public static String getFinishCount(String phone,HttpServletResponse resp){
		String result = "";
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		int count = 0,count2 = 0,count3 = 0;//count还没做的任务
		int total = 0;
		String line = "";
		try {
			Class.forName(Driver).newInstance();
			Connection conS = DriverManager.getConnection(rURL,Username,Password);
			String str = "select fubiao.phone from fubiao where timestampdiff(second,'"+CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00',fubiao.calltime) > 0 and fubiao.out='true' and fubiao.myphone='"+phone+"' group by fubiao.phone";
			PreparedStatement p = conS.prepareStatement(str);
			ResultSet rsStr = p.executeQuery();
			
			
			if(rsStr != null){
				long m = 24*60*60*1000;
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				while(rsStr.next()){
					String temp = "select fubiao.calltime from fubiao where timestampdiff(second,'2000-01-01 01:01:01',fubiao.calltime) > 0" +
							" and timestampdiff(second,fubiao.calltime,'"+CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00') > 0 and fubiao.out = 'true' and fubiao.phone='"+rsStr.getString("phone")+"' order by id desc limit 0,1";
					p = conS.prepareStatement(temp);
					ResultSet rsTemp = p.executeQuery();
					if(rsTemp != null && rsTemp.next()){
						if(comparesTime(CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00",rsTemp.getString("calltime"),df)/m >= Integer.parseInt(CurrentTime.dayNum)){
							count ++;
						}
						rsTemp.close();
						rsTemp = null;
					}else{
						String aa = "select Register.time from Register where Register.userphone='"+rsStr.getString("phone")+"'";
						p = conS.prepareStatement(aa);
						ResultSet rsaa = p.executeQuery();
						if(rsaa != null && rsaa.next()){
							if(comparesTime(CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00",rsaa.getString("time"),df)/m >= Integer.parseInt(CurrentTime.dayNum)){
								count ++;
							}
							rsaa.close();
							rsaa = null;
							
						}
					}
					
				}
				rsStr.close();
				rsStr = null;
			}
			
//			
//			if(rsStr != null){
//				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				long m = 24*60*60*1000;
//				while(rsStr.next()){
//					boolean flag = false;
//					String temp = "select fubiao.calltime from fubiao where timestampdiff(second,'"+CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00',fubiao.calltime) > 0 and fubiao.phone='"+rsStr.getString("userphone")+"' group by fubiao.phone";
//					conS.prepareStatement(temp);
//					ResultSet rsTemp = p.executeQuery();
//					if(rsTemp != null && rsTemp.next()){
//						if(comparesTime(rsTemp.getString("calltime"),CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00",df) > 0){
//							 if(rsTemp.next() && (comparesTime(CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00",rsTemp.getString("calltime"),df)/m) >= Integer.parseInt(CurrentTime.dayNum)){
//								 flag = true;
//							 }else if(!rsTemp.next()){
//								 String aa = "select min(fubiao.id),fubiao.datatime from fubiao where fubiao.phone='"+rsStr.getString("userphone")+"'";
//								 conS.prepareStatement(aa);
//								 ResultSet rsaa = p.executeQuery();
//								 if(rsaa != null && rsaa.next()){
//									 if((comparesTime(CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00",rsaa.getString("datatime"),df)/m) >= Integer.parseInt(CurrentTime.dayNum)){
//										 flag = true;
//									 }
//								 }
//								 
//								 if(rsaa != null){
//									 rsaa.close();
//									 rsaa = null;
//								 }
//						}
//						}
//					}
//				}
//			}
			
			
//			String sql = "select count(*) from fubiao where fubiao.myphone='"+phone+"' and fubiao.out='true' and timestampdiff(second,'"+CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00"+"',fubiao.calltime) > 0 group by fubiao.phone";
//			p = conS.prepareStatement(sql);
//			ResultSet rs = p.executeQuery();
//			
//			if(rs != null && rs.next()){
//				count = rs.getInt("count(*)");
//			}
//			
//			if(rs != null)
//			{
//				rs.close();
//			}
			
			String sql = "select managerserver.task from managerserver where managerserver.phone='"+phone+"'";
			p = conS.prepareStatement(sql);
			ResultSet rs1 = p.executeQuery();
			if(rs1 != null && rs1.next()){
				total = Integer.parseInt(rs1.getString("task"));
				rs1.close();
			}
			
			sql = "select Register.userphone,Register.time from Register where Register.myphone='"+phone+"'";
			p = conS.prepareStatement(sql);
			ResultSet rs10 = p.executeQuery();
			if(rs10 != null){
				long m = 24*60*60*1000;
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				while(rs10.next()){
					p = conS.prepareStatement("select fubiao.calltime from fubiao where timestampdiff(second,'2000-01-01 01:01:01',fubiao.calltime) > 0 and fubiao.out='true' and fubiao.phone='"+rs10.getString("userphone")+"' order by id desc limit 0,1");
					ResultSet rs2 = p.executeQuery();
					if(rs2 != null && rs2.next()){
						if(comparesTime(CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00",rs2.getString("calltime"),df)/m > Integer.parseInt(CurrentTime.dayNum)){
							count2 ++;
						}
						rs2.close();
						rs2 = null;
					}else{
						if(comparesTime(CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00",rs10.getString("time"),df)/m > Integer.parseInt(CurrentTime.dayNum)){
							count2 ++;
						}
					}
				}
				rs10.close();
				rs10 = null;
			}
			
			
			if((count+count2) < total){
				sql = "select count(*) from Register where Register.myphone='"+phone+"' and timestampdiff(second,'"+CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00"+"',Register.time) > 0";
				p = conS.prepareStatement(sql);
				ResultSet rs3 = p.executeQuery();
				if(rs3 != null && rs3.next()){
					count3 = rs3.getInt("count(*)");
					rs3.close();
				}
			}
			
//			sql = "select fubiao.calltime,fubiao.datatime fubiao.phone from fubiao where fubiao.myphone='"+phone+"'  and  timestampdiff(day,fubiao.calltime,'"+CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:01') >= "+CurrentTime.dayNum +"group by fubiao.phone";
//			p = conS.prepareStatement(sql);
//			ResultSet rs2 = p.executeQuery();
//			if(rs2 != null && rs2.next()){
//				count2 = rs2.getInt("count(*)");
//				rs2.close();
//			}
//			
//			sql = "select count(*) from Register where Register.myphone='"+phone+"'";
//			p = conS.prepareStatement(sql);
//			ResultSet rs3 = p.executeQuery();
//			if(rs3 != null && rs3.next()){
//				count3 = rs3.getInt("count(*)");
//				rs3.close();
//			}
//			
//			if(count3 < total){
//				
//			}
			
			
//			if((count2 + count) < total){
//				sql = "select count(*) from Register where Register.myphone='"+phone+"' and timestampdiff(second,'"+CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00"+"',Register.time) > 0";
//				p = conS.prepareStatement(sql);
//				ResultSet rs3 = p.executeQuery();
//				if(rs3 != null && rs3.next()){
//					count3 = rs3.getInt("count(*)");
//					rs3.close();
//				}
//			}
			
			if((count+count2) >= total){
				result = "("+count+"/"+total+")";
			}else if(count+count2 < total){
				if(count+count3 < total)
					result = "("+(count+count3)+"/"+total+")";
				else
					result = "("+total+"/"+total+")";
			}
			p.close();
			conS.close();
			
			PrintWriter pw = resp.getWriter();
			TextMessage t = new TextMessage();
			t.setContent(result);
			t.setFromid(CurrentTime.dayNum);
			pw.print(MessageUtil.textMessageToXml(t));
			pw.flush();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			PrintWriter pw;
			try {
				pw = resp.getWriter();
				pw.print(e.toString() +"   line ="+line);
				pw.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			PrintWriter pw;
			try {
				pw = resp.getWriter();
				pw.print(e.toString() +"   line ="+line);
				pw.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			PrintWriter pw;
			try {
				pw = resp.getWriter();
				pw.print(e.toString() +"   line ="+line);
				pw.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			PrintWriter pw;
			try {
				pw = resp.getWriter();
				pw.print(e.toString() +"   line ="+line);
				pw.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
			PrintWriter pw;
			try {
				pw = resp.getWriter();
				pw.print(e.toString() +"   line ="+line);
				pw.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		return result;
		
	}
	
	public static boolean guohu(String fromPhone,String toPhone){
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		try {
			Class.forName(Driver).newInstance();
			String sql = "update Register set Register.myphone='"+toPhone+"' where Register.myphone='"+fromPhone+"'";
			Connection conS = DriverManager.getConnection(wURL,Username,Password);
			PreparedStatement p = conS.prepareStatement(sql);
			p.executeUpdate();
			
			String sql1 = "update fubiao set fubiao.myphone='"+toPhone+"' where fubiao.myphone='"+fromPhone+"'";
			p = conS.prepareStatement(sql1);
			p.executeUpdate();
			
			String sql2 = "update MyConsultant set MyConsultant.consultantphone='"+toPhone+"' where MyConsultant.consultantphone='"+fromPhone+"'";
			p = conS.prepareStatement(sql2);
			p.executeUpdate();
			
			p.close();
			conS.close();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static ArrayList<XiaoshouDetail> getCallDetail(String phone,HttpServletResponse resp){
		ArrayList<XiaoshouDetail>  xiaoshouList= new ArrayList<XiaoshouDetail>();
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		String today = CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00";
		try {
			Class.forName(Driver).newInstance();
			String sql = "select * from fubiao WHERE fubiao.myphone='"+phone+"'  and timestampdiff(second,'"+today+"',fubiao.calltime) > 0";
			Connection conS = DriverManager.getConnection(rURL,Username,Password);
			PreparedStatement p = conS.prepareStatement(sql);
			ResultSet rs = (ResultSet) p.executeQuery();
			if(rs != null){
				while(rs.next()){
					XiaoshouDetail detail = new XiaoshouDetail();
					String callIn = rs.getString("in");
					if(callIn.equals("true")){
						detail.setCallIn(callIn);
					}else
						detail.setCallIn("false");
					detail.setStartCallTime(rs.getString("calltime"));
					detail.setStopCallTime(rs.getString("stopcall"));
					detail.setPhone(rs.getString("phone"));
					xiaoshouList.add(detail);
				}
			}
			if(rs != null){
				rs.close();
			}
			p.close();
			conS.close();
			try {
				
				Gson gson = new Gson();
				PrintWriter print = resp.getWriter();
				print.print(gson.toJson(xiaoshouList));
				print.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return xiaoshouList;
	}
	
	
	public static ArrayList<String> getNewAdd(String phone,HttpServletResponse resp){
		ArrayList<String> addList = new ArrayList<String>();
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		
		try {
			Class.forName(Driver).newInstance();
			String sql = "select Register.userphone from Register WHERE Register.myphone='"+phone+"' and timestampdiff(hour,'"+CurrentTime.getCurrentTime("yyyy-MM-dd")+" 00:00:00',Register.time) > 0";
			Connection conS = DriverManager.getConnection(rURL,Username,Password);
			PreparedStatement p = conS.prepareStatement(sql);
			ResultSet rs = (ResultSet) p.executeQuery();
			if(rs != null){
				while(rs.next()){
					addList.add(rs.getString("userphone"));
				}
				rs.close();
				p.close();
				conS.close();
			}
			try {
				Gson gson = new Gson();
				PrintWriter print = resp.getWriter();
				print.print(gson.toJson(addList));
				print.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addList;
	}
	
	
	public static boolean registerXiaoshou(String phone,String name,String pwd,String managerid,String task,HttpServletResponse resp){
		boolean flag = true;
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		String sql1 = null;
		try {
			Class.forName(Driver).newInstance();
			String sql = "select managerserver.phone from managerserver where managerserver.phone ='"+phone+"'";
			Connection conS = DriverManager.getConnection(wURL,Username,Password);
			PreparedStatement p = conS.prepareStatement(sql);
			ResultSet rs = (ResultSet) p.executeQuery();
			if(rs != null && rs.next()){
				flag = false;
				rs.close();
			}else{
				if(flag){
					sql1 = "insert into managerserver(managerserver.username,managerserver.password,managerserver.phone,managerserver.state,managerserver.count,managerserver.online,managerserver.managerid" +
							",managerserver.onlineTime,managerserver.offerlineTime,managerserver.standOn,managerserver.standOff,managerserver.imgpath,managerserver.task) values('"+name+"','"+pwd+"','"+phone+"','空闲',0,'false','"+managerid+"','2014-01-01 00:00:00','2014-01-01 00:00:00','2014-01-01 00:00:00','2014-01-01 00:00:00','','"+task+"')";
					PreparedStatement p1 = conS.prepareStatement(sql1);
					if(p1.executeUpdate() == 0)
						flag = false;
					p1.close();
				}
			}
			p.close();
			conS.close();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(flag){
				TextMessage t = new TextMessage();
				t.setContent("更新成功");
				PrintWriter print;
				try {
					print = resp.getWriter();
					print.print(MessageUtil.textMessageToXml(t));
					print.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				TextMessage t = new TextMessage();
				t.setContent("更新失败");
				PrintWriter print;
				try {
					print = resp.getWriter();
					print.print(MessageUtil.textMessageToXml(t));
					print.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
		return flag;
		
	}
	public static void getXiaoShouModel(String id,HttpServletResponse resp){
		ArrayList<XiaoshouModel> modelList = new ArrayList<XiaoshouModel>();
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		try {
			Class.forName(Driver).newInstance();
			
			String sql = "select managerserver.phone,managerserver.username from managerserver where managerserver.managerid = '"+id+"'";
			Connection conS = DriverManager.getConnection(rURL,Username,Password);
			PreparedStatement p = conS.prepareStatement(sql);
			ResultSet rs = (ResultSet) p.executeQuery();
			if(rs != null){
				while(rs.next()){
					XiaoshouModel model = new XiaoshouModel();
					model.setName(rs.getString("username"));
					model.setPhone(rs.getString("phone"));
					modelList.add(model);
				}
				rs.close();
				p.close();
				conS.close();
			}
			
			Gson gson = new Gson();
			PrintWriter print = resp.getWriter();
			print.print(gson.toJson(modelList));
			print.flush();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<String> getTask(String phone,HttpServletResponse resp){
		ArrayList<String> taskList = new ArrayList<String>();
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			java.util.Date toDay=df.parse(CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			Class.forName(Driver).newInstance();
			
			String sql = "select Register.userphone from Register WHERE Register.myphone='"+phone+"'";
			Connection conS = DriverManager.getConnection(rURL,Username,Password);
			PreparedStatement p = conS.prepareStatement(sql);
			ResultSet rs = (ResultSet) p.executeQuery();
			int dayNum = Integer.parseInt(CurrentTime.dayNum);
			if(rs != null){
				while(rs.next()){
					String str = "select max(stopcall) from fubiao where fubiao.phone = '"+rs.getString("userphone")+"'";
					PreparedStatement p1 = conS.prepareStatement(str);
					ResultSet rs1 = (ResultSet) p1.executeQuery();
					if(rs1 != null && rs1.next() && rs1.getString("max(stopcall)") != null){
						Date data = df.parse(rs1.getString("max(stopcall)"));
						 long l=toDay.getTime()-data.getTime();
						 long magin =l/(24*60*60*1000);
						 if(magin >= dayNum){
							 taskList.add(rs.getString("userphone"));
						 }
						 rs1.close();
						 p1.close();
					}else{
						String str2 = "select MyConsultant.datatime from MyConsultant where MyConsultant.customer_phone = '"+rs.getString("userphone")+"'";
						PreparedStatement p2 = conS.prepareStatement(str2);
						ResultSet rs2 = (ResultSet) p2.executeQuery();
						if(rs2 != null && rs2.next()){
							Date data = df.parse(rs2.getString("datatime"));
							long l=toDay.getTime()-data.getTime();
							 long magin =l/(24*60*60*1000);
							 if(magin >= dayNum){
								 taskList.add(rs.getString("userphone"));
							 }
							 
							 rs2.close();
							 p2.close();
						}else{
							String str3 = "select Register.time from Register where Register.userphone = '"+rs.getString("userphone")+"'";
							PreparedStatement p3 = conS.prepareStatement(str3);
							ResultSet rs3 = (ResultSet) p3.executeQuery();
							if(rs3 != null && rs3.next()){
								Date data = df.parse(rs3.getString("time"));
								long l=toDay.getTime()-data.getTime();
								 long magin =l/(24*60*60*1000);
								 if(magin >= dayNum){
									 taskList.add(rs.getString("userphone"));
								 }
								 rs3.close();
								 p3.close();
							}
						}
					}
				}
				rs.close();
				p.close();
				conS.close();
			}
			try {
				Gson gson = new Gson();
				PrintWriter print = resp.getWriter();
				print.print(gson.toJson(taskList));
				print.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return taskList;
		
	}
	public static String isLaoyezhu(String phone){
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		
		try {
			Class.forName(Driver).newInstance();
			String sql = "select islaoyezhu from Register WHERE Register.userphone='"+phone+"'";
			Connection conS = DriverManager.getConnection(rURL,Username,Password);
			PreparedStatement p = conS.prepareStatement(sql);
			ResultSet rs = (ResultSet) p.executeQuery();
			phone = "";
			if(rs != null && rs.next()){
				phone = rs.getString("islaoyezhu");
			}
			if(rs != null){
				rs.close();
				p.close();
				conS.close();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return phone;
		
	}
//	public static String getConsumer(int serverid){
//		String Username=SaeUserInfo.getAccessKey();
//		String Password=SaeUserInfo.getSecretKey();
//		String Driver="com.mysql.jdbc.Driver";
//		
//		String gStr = null;
//		try {
//			Class.forName(Driver).newInstance();
//			String sql = "select *from customer WHERE customer_id="+serverid;
//			Connection conS=DriverManager.getConnection(rURL,Username,Password);
//			PreparedStatement p = conS.prepareStatement(sql);
//			ResultSet rs = (ResultSet) p.executeQuery();
//			if(rs != null && rs.next()){
//				Gson gson = new Gson();
//				ConsumerAccept con = new ConsumerAccept();
//				con.setConsultantname(rs.getString("consultantname"));
//				con.setCustomer_name(rs.getString("customer_name"));
//				con.setCustomer_phone(rs.getString("customer_phone"));
//				con.setAccept(rs.getString("accept"));
//				con.setConsultantphone(rs.getString("consultantphone"));
//				con.setIsaccept(rs.getString("isaccept"));
//				con.setServerid(rs.getInt("customer_id"));
//				con.setSaleid(rs.getString("saleid"));
//				con.setDatatime(rs.getString("datatime"));
//				con.setImgpath(rs.getString("imgpath"));
//				con.setIshelp(rs.getString("ishelp"));
//				con.setHelpacceptname(rs.getString("helpacceptname"));
//				gStr = gson.toJson(con);
//			}
//			if(rs != null){
//				rs.close();
//				rs = null;
//			}
//			p.close();
//			conS.close();
//			conS = null;
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		
//		return gStr;
//		
//	}
	
	public static ArrayList<MyModel> getSelectConsultants(){
		ArrayList<MyModel> arrList = new ArrayList<MyModel>();
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		
		try {
			Class.forName(Driver).newInstance();
			String sql = "select username,managerserver.phone from managerserver where online = 'yes'";
			Connection con = DriverManager.getConnection(rURL,Username,Password);
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = (ResultSet) pstmt.executeQuery();
			while (rs.next()) {
				MyModel model = new MyModel();
				model.setName(rs.getString("username"));
				model.setId(Integer.parseInt(rs.getString("phone")));
				arrList.add(model);
			}
			if(rs != null){
				rs.close();
				rs = null;
			}
			pstmt.close();
			con.close();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arrList;
		
	}
	
//	public static ArrayList<ConsultModel> getConsultants(){
//		ArrayList<ConsultModel> conLst = new ArrayList<ConsultModel>();
//		String Username=SaeUserInfo.getAccessKey();
//		String Password=SaeUserInfo.getSecretKey();
//		String Driver="com.mysql.jdbc.Driver";
//		try { 
//			String sql = "select *from managerserver where online = 'yes'";
//			Class.forName(Driver).newInstance();
//			Connection con = DriverManager.getConnection(rURL,Username,Password);	
//			Connection con1 = null;
//			PreparedStatement pstmt = con.prepareStatement(sql);
//			ResultSet rs = (ResultSet) pstmt.executeQuery();
//			while (rs.next()) {
//				ConsultModel model = new ConsultModel();
//				model.setName(rs.getString("username"));
//				model.setId(rs.getInt("id"));
//				model.setState(rs.getString("state"));
//				if(rs.getString("onlineTime") != null)
//					model.setOnlineTime(rs.getString("onlineTime"));
//				else 
//					model.setOnlineTime("");
//				if(rs.getString("offerlineTime") != null)
//					model.setOfferlineTime(rs.getString("offerlineTime"));
//				else
//					model.setOfferlineTime("");
//				if(rs.getString("standOn") != null)
//					model.setStandOn(rs.getString("standOn"));
//				else
//					model.setStandOn("");
//				if(rs.getString("standOff") != null)
//					model.setStandOff(rs.getString("standOff"));
//				else
//					model.setStandOff("");
//				conLst.add(model);
//			}
//			if(rs != null){
//				rs.close();
//				rs = null;
//			}
//			pstmt.close();
//			if(conLst.size() > 0){
//				con1 = DriverManager.getConnection(rURL,Username,Password);
//				int length = conLst.size();
//				ResultSet rs1 = null;
//				for(int i = 0 ; i < length ; i++){
//					String sql1 = "select count(*) from customer where saleid = '"+conLst.get(i).getId()+"' and saleid = accept";
//					PreparedStatement ps = con1.prepareStatement(sql1);
//					rs1 = (ResultSet) ps.executeQuery();
//					if(rs1.next()){
//						conLst.get(i).setCount(rs1.getInt("count(*)"));
//					}
//					sql1 = "select count(*) from customer where accept = '"+conLst.get(i).getId()+"'";
//					ps = con1.prepareStatement(sql1);
//					rs1 = (ResultSet) ps.executeQuery();
//					if(rs1.next()){
//						conLst.get(i).setAcceptCount(""+rs1.getInt("count(*)"));
//					}
//					conLst.get(i).setHelpAcceptCount(""+(Integer.parseInt(conLst.get(i).getAcceptCount()) - conLst.get(i).getCount()));
//				}
//				if(rs1 != null){
//					rs1.close();
//					rs1 = null;
//				}
//			}
//			if (con1 != null) {
//				con1.close();
//				con1 = null;
//			}
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return conLst;
//	}
	public static int unRegister(String phone,HttpServletResponse resp){
		int count = 0;
		if(!isRegister(phone).equals("")){
			String Username=SaeUserInfo.getAccessKey();
			String Password=SaeUserInfo.getSecretKey();
			String Driver="com.mysql.jdbc.Driver";
			
			try {
				String wSql = "DELETE FROM Register WHERE Register.userphone = '"+phone+"'";
				Class.forName(Driver).newInstance();
				Connection conW = DriverManager.getConnection(wURL,Username,Password);	
				PreparedStatement p = conW.prepareStatement(wSql);
				count += p.executeUpdate();
				
				String wSql1 = "DELETE FROM MyConsultant WHERE MyConsultant.customer_phone = '"+phone+"'";
				Connection conW1 = DriverManager.getConnection(wURL,Username,Password);	
				PreparedStatement p1 = conW1.prepareStatement(wSql1);
				count += p1.executeUpdate();
				
				String wSql2 = "DELETE FROM fubiao WHERE fubiao.phone = '"+phone+"'";
				Connection conW2 = DriverManager.getConnection(wURL,Username,Password);	
				PreparedStatement p2 = conW2.prepareStatement(wSql2);
				count += p2.executeUpdate();
				p2.close();
				p1.close();
				p.close();
				
				
				conW2.close();
				conW1.close();
				conW.close();
				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else
			count = -1;
		return count;
	}
	public static int Register(String phone,String userid,String myphone,String myname,String laoyezhuphone,String islaoyezhu,String name){
		int count = 0;
		synchronized (lockWrite) {
		if(isRegister(phone).equals("")){
			String Username=SaeUserInfo.getAccessKey();
			String Password=SaeUserInfo.getSecretKey();
			String Driver="com.mysql.jdbc.Driver";
			
			try {
				String wSql = "insert into Register(userphone,userid,time,myphone,myname,laoyezhuphone,islaoyezhu,username) values('"+
				phone+"','"+userid+"','"+CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss")+"','"+myphone+"','"+myname+"','"+laoyezhuphone+"','"+islaoyezhu+"','"+name+"')";
				Class.forName(Driver).newInstance();
				Connection conW=DriverManager.getConnection(wURL,Username,Password);	
				PreparedStatement p = conW.prepareStatement(wSql);
				count = p.executeUpdate();
				p.close();
				conW.close();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
		}
		return count;
		
	}
	
	public static String isRegister(String phone){
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		String str = "";
		try {
			
			String sql = "select Register.myname from Register where userphone = '"+phone+"'";
			Class.forName(Driver).newInstance();
			Connection conR=DriverManager.getConnection(rURL,Username,Password);
			PreparedStatement p = conR.prepareStatement(sql);
			ResultSet rs = p.executeQuery();
			
			if(rs != null && rs.next()){
				str = rs.getString("myname");
			}
			
			if(rs != null){
				rs.close();
				rs = null;
			}
			p.close();
			conR.close();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	
//	public static int insertConsumer(ConsumerAccept consumer){
//		String Username=SaeUserInfo.getAccessKey();
//		String Password=SaeUserInfo.getSecretKey();
//		String Driver="com.mysql.jdbc.Driver";
//		int id = -1;
//		try {
//			synchronized (lock) {
//			Class.forName(Driver).newInstance();
//				String wSql = "insert into customer(customer_name,customer_phone,isaccept,helpacceptname,accept,consultantname,consultantphone,saleid,datatime,imgpath,ishelp) values('"+consumer
//				.getCustomer_name()+"','"+consumer.getCustomer_phone()+"','"+consumer.getIsaccept()+"','"+consumer.getHelpacceptname()+"','"+consumer.getAccept()+"','"+consumer.getConsultantname()+"','"+consumer.getConsultantphone()
//				+"','"+consumer.getSaleid()+"','"+consumer.getDatatime()+"','"+consumer.getImgpath()+"','"+consumer.getIshelp()+"')";
//				Connection conW=DriverManager.getConnection(wURL,Username,Password);	
//				PreparedStatement p = conW.prepareStatement(wSql);
//				p.executeUpdate();
//				
//				
//				ResultSet rs = p.executeQuery("SELECT LAST_INSERT_ID()");
//				if(rs.next()){
//					id = rs.getInt(1);
//				}
////				String rSql = "select customer_id from Consultant where unix_timestamp(datatime) = unix_timestamp('"+consumer.getDatatime()+"')";
////				Connection conR=DriverManager.getConnection(rURL,Username,Password);
////				PreparedStatement pr = conR.prepareStatement(rSql);
////				ResultSet rs = pr.executeQuery();  
////				if (rs.next()) {  
////					id = rs.getInt("customer_id"); 
////					if(id == 0){
////						id = -1;
////					}
////	            } 
//				if(rs != null){
//					rs.close();
//					rs = null;
//				}
//				p.close();
//				conW.close();
////				conR = null;
//				
//			}
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return id;
//	}
	
	public static int insertConsumerModel(ConsumerModel consumer, HttpServletResponse resp){
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		int count = 0;
			String wSql = "insert into MyConsultant(MyConsultant.customer_name,MyConsultant.customer_phone,MyConsultant.consultantname,MyConsultant.consultantphone,MyConsultant.datatime,MyConsultant.kehuleixing,MyConsultant.xingbie,MyConsultant.yisuan,MyConsultant.yixiang,MyConsultant.yixiangyetai," +
					"MyConsultant.kehuzu,MyConsultant.yixiangmianji,MyConsultant.zijinshili,MyConsultant.yixianghuxing,MyConsultant.kehushuxing,MyConsultant.renzhiqudao,MyConsultant.louhao,MyConsultant.dizhi,MyConsultant.temp1,MyConsultant.temp2,MyConsultant.temp3,MyConsultant.temp4,MyConsultant.temp5,MyConsultant.temp6,MyConsultant.temp7,MyConsultant.temp8,MyConsultant.temp9,MyConsultant.temp10,MyConsultant.quyu,shenfenzheng,MyConsultant.chengjiaojine) values('"+
			consumer.getCustomer_name()+"','"+consumer.getCustomer_phone()+"','"+consumer.getConsultantname()+"','"+consumer.getConsultantphone()+"','"+consumer.getDatatime()
			+"','"+consumer.getKehuleixing()+"','"+consumer.getXingbie()+"','"+consumer.getYisuan()
			+"','"+consumer.getYixiang()+"','"+consumer.getYixiangyetai()+"','"+consumer.getKehuzu()
			+"','"+consumer.getYixiangmianji()+"','"+consumer.getZijinshili()+"','"+consumer.getYixianghuxing()
			+"','"+consumer.getKehushuxing()+"','"+consumer.getRenzhiqudao()+"','"+consumer.getLouhao()+"','"+consumer.getDizhi()
			+"','"+consumer.getTemp1()+"','"+consumer.getTemp2()+"','"+consumer.getTemp3()+"','"+consumer.getTemp4()+"','"+consumer.getTemp5()+"','"+consumer.getTemp6()
			+"','"+consumer.getTemp7()+"','','"+consumer.getTemp9()+"','"+consumer.getTemp10()+"','"+consumer.getQuyu()+"','"+consumer.getShenfenzheng()+"','"+consumer.getChengjiaojine()+"')";
			try {
				Class.forName(Driver).newInstance();
				Connection conW=DriverManager.getConnection(wURL,Username,Password);
				PreparedStatement p = conW.prepareStatement(wSql);
				count = p.executeUpdate();
				p.close();
				
					String wSql1 = "insert into fubiao(fubiao.stopcall,fubiao.calltime,fubiao.phone,fubiao.beizhu,fubiao.in,fubiao.out,fubiao.datatime,fubiao.myphone) values('2000-01-01 01:01:01','2000-01-01 01:01:01','"+consumer.getCustomer_phone()+"','"+consumer.getBeizhu()
					+"','-1','-1','"+consumer.getDatatime()+"','"+consumer.getConsultantphone()+"')";
					Connection conW1=DriverManager.getConnection(wURL,Username,Password);	
					PreparedStatement p1 = conW1.prepareStatement(wSql1);
					int i = p1.executeUpdate();
					count += i;
					p1.close();
				
				if(!consumer.getChengjiaojine().equals("")){
					String chengjiaoStr = "UPDATE Register set Register.islaoyezhu='true'  where Register.userphone='"+consumer.getCustomer_phone()+"'";
					PreparedStatement chengjiaoP = conW.prepareStatement(chengjiaoStr);
					chengjiaoP.executeUpdate();
					chengjiaoP.close();
				}
				
				if(!consumer.getLaoyezhuphone().equals("")){
					String laoyezhuStr = "UPDATE Register set Register.laoyezhuphone='"+consumer.getLaoyezhuphone()+"' where Register.userphone='"+consumer.getCustomer_phone()+"'";
					PreparedStatement laoyezhuP = conW.prepareStatement(laoyezhuStr);
					laoyezhuP.executeUpdate();
					laoyezhuP.close();
				}
				
				conW.close();
//				p.setString(1, consumer.getCustomer_name());
//				p.setString(2, consumer.getCustomer_phone());
//				p.setString(3, consumer.getConsultantname());
//				p.setString(4, consumer.getConsultantphone());
//				p.setString(5, consumer.getDatatime());
//				p.setString(6, consumer.getKehuleixing());
//				p.setString(7, consumer.getXingbie());
//				p.setString(8, consumer.getYisuan());
//				p.setString(9, consumer.getYixiang());
//				p.setString(10, consumer.getYixiangyetai());
//				p.setString(11, consumer.getKehuzu());
//				p.setString(12, consumer.getYixiangmianji());
//				p.setString(13, consumer.getZijinshili());
//				p.setString(14, consumer.getYixianghuxing());
//				p.setString(15, consumer.getKehushuxing());
//				p.setString(16, consumer.getRenzhiqudao());
//				p.setString(17, consumer.getDizhi());
//				p.setString(18, consumer.getTemp1());
//				p.setString(19, consumer.getTemp2());
//				p.setString(20, consumer.getTemp3());
//				p.setString(21, consumer.getTemp4());
//				p.setString(22, consumer.getTemp5());
//				p.setString(23, consumer.getTemp6());
//				p.setString(24, consumer.getTemp7());
//				p.setString(25, consumer.getTemp8());
//				p.setString(26, consumer.getTemp9());
//				p.setString(27, consumer.getTemp10());
				
				if(count > 1){
					TextMessage t = new TextMessage();
					t.setContent("更新成功");
					try {
						resp.getWriter().print(MessageUtil.textMessageToXml(t));
						resp.getWriter().flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					TextMessage t = new TextMessage();
					t.setContent("更新失败");
					try {
						resp.getWriter().print(MessageUtil.textMessageToXml(t));
						resp.getWriter().flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (InstantiationException e) {
				TextMessage t = new TextMessage();
				t.setContent("更新失败  = "+e.toString());
				try {
					resp.getWriter().print(MessageUtil.textMessageToXml(t));
					resp.getWriter().flush();
				} catch (IOException e1) {
					e.printStackTrace();
				}
			} catch (IllegalAccessException e) {
				TextMessage t = new TextMessage();
				t.setContent("更新失败  = "+e.toString());
				try {
					resp.getWriter().print(MessageUtil.textMessageToXml(t));
					resp.getWriter().flush();
				} catch (IOException e1) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				TextMessage t = new TextMessage();
				t.setContent("更新失败  = "+e.toString());
				try {
					resp.getWriter().print(MessageUtil.textMessageToXml(t));
					resp.getWriter().flush();
				} catch (IOException e1) {
					e.printStackTrace();
				}
			} catch (SQLException e) {
				TextMessage t = new TextMessage();
				t.setContent("更新失败  = "+e.toString());
				try {
					resp.getWriter().print(MessageUtil.textMessageToXml(t));
					resp.getWriter().flush();
				} catch (IOException e1) {
					e.printStackTrace();
				}
			}
			
		return count;
	}
	
	public static ArrayList<BeizhuModel> getAllBeizhu(String phone){
		ArrayList<BeizhuModel> bList = new ArrayList<BeizhuModel>();
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		
		try {
			Class.forName(Driver).newInstance();
			Connection conR=DriverManager.getConnection(rURL,Username,Password);
			PreparedStatement p = conR.prepareStatement("select fubiao.datatime,fubiao.calltime,fubiao.beizhu from fubiao where fubiao.beizhu!='' and fubiao.phone='"+phone+"'");
			ResultSet r = p.executeQuery();
			if(r != null){
				while(r.next()){
					BeizhuModel model = new BeizhuModel();
					model.setBeizhu(r.getString("beizhu"));
					if(!r.getString("datatime").substring(0, 4).equals("2000"))
						model.setTime(r.getString("datatime"));
					else
						model.setTime(r.getString("calltime"));
					bList.add(model);
				};
				r.close();
			}
			p.close();
			conR.close();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bList;
		
	}
	
	public static ConsumerModel getOneConsumerModel(String phone){
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		ConsumerModel model = null;
		try {
			Class.forName(Driver).newInstance();
			Connection conR=DriverManager.getConnection(rURL,Username,Password);
			PreparedStatement p = conR.prepareStatement("select *from MyConsultant where MyConsultant.customer_phone='"+phone+"'");
			ResultSet rs = p.executeQuery();
			if(rs != null && rs.next()){
				model = new ConsumerModel();
				
				model.setConsultantname(rs.getString("consultantname"));
				model.setConsultantphone(rs.getString("consultantphone"));
				model.setCustomer_name(rs.getString("customer_name"));
				model.setCustomer_phone(rs.getString("customer_phone"));
				p = conR.prepareStatement("select Register.time from Register where Register.userphone='"+phone+"'");
				ResultSet rs2 = p.executeQuery();
				if(rs2 != null && rs2.next()){
					model.setDatatime(rs2.getString("time"));
					rs2.close();
					rs2 = null;
				}
				
//				p = conR.prepareStatement("select fubiao.calltime from fubiao where timestampdiff(second,'2000-01-01 01:01:01',fubiao.calltime) > 0 and fubiao.phone='"+phone+"' order by id desc limit 0,1");
//				ResultSet rs20 = p.executeQuery();
//				
//				if(rs20 != null && rs20.next()){
//					model.setLaifangshijian(rs20.getString("calltime"));
//					rs20.close();
//					rs20 = null;
//				}
				
				PreparedStatement p1 = conR.prepareStatement("select fubiao.beizhu from fubiao where fubiao.beizhu!='' and fubiao.phone='"+phone+"' order by fubiao.datatime desc");
				ResultSet rs1 = p1.executeQuery();
				if(rs1 != null && rs1.next()){
					model.setBeizhu(rs1.getString("beizhu"));
				}
				if(rs1 != null){
					rs1.close();
					rs1 = null;
				}
				
				p1 = conR.prepareStatement("select fubiao.calltime from fubiao where fubiao.in='true' and fubiao.phone='"+phone+"' order by fubiao.datatime desc");
				ResultSet rs10 = p1.executeQuery();
				if(rs10 != null && rs10.next()){
					model.setLastCallTime(rs10.getString("calltime"));
					rs10.close();
					rs10 = null;
				}else{
					model.setLastCallTime(model.getDatatime());
				}
				
				model.setDizhi(rs.getString("dizhi"));
				model.setKehuleixing(rs.getString("kehuleixing"));
				model.setKehushuxing(rs.getString("kehushuxing"));
				model.setKehuzu(rs.getString("kehuzu"));
				model.setLouhao(rs.getString("louhao"));
				model.setQuyu(rs.getString("quyu"));
				model.setRenzhiqudao(rs.getString("renzhiqudao"));
				model.setTemp1(rs.getString("temp1"));
				model.setTemp2(rs.getString("temp2"));
				model.setTemp3(rs.getString("temp3"));
				model.setTemp4(rs.getString("temp4"));
				model.setTemp5(rs.getString("temp5"));
				model.setTemp6(rs.getString("temp6"));
				model.setTemp7(rs.getString("temp7"));
				model.setTemp8(rs.getString("temp8"));
				model.setTemp9(rs.getString("temp9"));
//				model.setTemp10(rs.getString("temp10"));
				model.setXingbie(rs.getString("xingbie"));
				model.setYisuan(rs.getString("yisuan"));
				model.setYixiang(rs.getString("yixiang"));
				model.setYixianghuxing(rs.getString("yixianghuxing"));
				model.setYixiangmianji(rs.getString("yixiangmianji"));
				model.setYixiangyetai(rs.getString("yixiangyetai"));
				model.setZijinshili(rs.getString("zijinshili"));
				model.setShenfenzheng(rs.getString("shenfenzheng"));
				model.setChengjiaojine(rs.getString("chengjiaojine"));
				if(p1 != null){
					p1.close();
				}
			}
			if(p != null){
				if(rs != null){
					rs.close();
				}
				p.close();
			}
			conR.close();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return model;
		
	}
	
	public static int insertCall(String start,String stop,String isCallIn,String phone,String myphone){
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		int count = 0;
		try {
			Class.forName(Driver).newInstance();
			Connection conW=DriverManager.getConnection(wURL,Username,Password);
			String callIn = "",callOut = "";
			if(isCallIn.equals("true")){
				callIn = "true";
			}else{
				callOut = "true";
			}
			PreparedStatement p = conW.prepareStatement("insert into fubiao(fubiao.stopcall,fubiao.calltime,fubiao.phone,fubiao.beizhu,fubiao.in,fubiao.out,fubiao.datatime,fubiao.myphone) values('"+stop+"','"+start+"','"+phone+"','','"+callIn+"','"+callOut+"','2000-01-01 01:01:01','"+myphone+"')");
			count = p.executeUpdate();
			if(p != null){
				p.close();
				conW.close();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	} 
	
	public static ArrayList<ConsumerModel> getConsumerModel(String quyu,String phone,HttpServletResponse response){
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		
		ArrayList<ConsumerModel> arr = new ArrayList<ConsumerModel>();
		try {
			Class.forName(Driver).newInstance();
			Connection conR=DriverManager.getConnection(rURL,Username,Password);
			String sql = "";
			if(!phone.equals("主管"))
				sql = "select *from MyConsultant where  quyu='"+quyu+"' AND MyConsultant.consultantphone='"+phone+"'";
			else
				sql = "select *from MyConsultant where  quyu='"+quyu+"'";
			PreparedStatement p = conR.prepareStatement(sql);
			ResultSet rs = p.executeQuery();
			if(rs != null){
				PreparedStatement p1 = null;
			while(rs.next()){
				ConsumerModel model = new ConsumerModel();
				
				
				model.setConsultantname(rs.getString("consultantname"));
				model.setConsultantphone(rs.getString("consultantphone"));
				model.setCustomer_name(rs.getString("customer_name"));
				model.setCustomer_phone(rs.getString("customer_phone"));
				model.setDatatime(rs.getString("datatime"));
				p1 = conR.prepareStatement("select fubiao.beizhu from fubiao where fubiao.beizhu!='' and fubiao.phone='"+model.getCustomer_phone()+"' order by fubiao.datatime desc");
				ResultSet rs1 = p1.executeQuery();
				if(rs1 != null && rs1.next()){
					model.setBeizhu(rs1.getString("beizhu"));
				}
				if(rs1 != null){
					rs1.close();
				}
				
				model.setDizhi(rs.getString("dizhi"));
				model.setKehuleixing(rs.getString("kehuleixing"));
				model.setKehushuxing(rs.getString("kehushuxing"));
				
				p1 = conR.prepareStatement("select *from Register where Register.userphone='"+model.getCustomer_phone()+"'");
				ResultSet rs2 = p1.executeQuery();
				if(rs2 != null && rs2.next()){
					if(!model.getKehushuxing().equals("新客户")){
						if(!rs2.getString("laoyezhuphone").equals("")){
							model.setLaoyezhuphone(rs2.getString("laoyezhuphone"));
						}
					}
					
					model.setTemp8(rs2.getString("time"));//注册时间
				}
				if(rs2 != null){
					rs2.close();
				}
				
				model.setKehuzu(rs.getString("kehuzu"));
				model.setLouhao(rs.getString("louhao"));
				model.setQuyu(quyu);
				model.setRenzhiqudao(rs.getString("renzhiqudao"));
				model.setTemp1(rs.getString("temp1"));
				model.setTemp2(rs.getString("temp2"));
				model.setTemp3(rs.getString("temp3"));
				model.setTemp4(rs.getString("temp4"));
				model.setTemp5(rs.getString("temp5"));
				model.setTemp6(rs.getString("temp6"));
				model.setTemp7(rs.getString("temp7"));
//				model.setTemp8(rs.getString("temp8"));
				model.setTemp9(rs.getString("temp9"));//成交时间
//				model.setTemp10(rs.getString("temp10"));
				model.setXingbie(rs.getString("xingbie"));
				model.setYisuan(rs.getString("yisuan"));
				model.setYixiang(rs.getString("yixiang"));
				model.setYixianghuxing(rs.getString("yixianghuxing"));
				model.setYixiangmianji(rs.getString("yixiangmianji"));
				model.setYixiangyetai(rs.getString("yixiangyetai"));
				model.setZijinshili(rs.getString("zijinshili"));
				model.setShenfenzheng(rs.getString("shenfenzheng"));
				model.setChengjiaojine(rs.getString("chengjiaojine"));
				
				arr.add(model);
			}
			if(p1 != null){
				p1.close();
			}
			}
			if(p != null){
				if(rs != null){
					rs.close();
				}
				p.close();
			}
			conR.close();
			
			Connection conR1 = DriverManager.getConnection(rURL,Username,Password);
			int size = arr.size();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i = 0; i < size; i++){
				ConsumerModel model = arr.get(i);
				PreparedStatement p2 = conR1.prepareStatement("select *from fubiao where fubiao.out='true' and timestampdiff(second,'2000-01-01 01:01:01',fubiao.calltime) > 0 and fubiao.phone='"+model.getCustomer_phone()+"'");
				ResultSet rs2 = p2.executeQuery();
				int grade = 0;
				while(rs2 != null && rs2.next()){
					
					String callIn = rs2.getString("in");
					String callOut = rs2.getString("out");
					if(callIn.equals("true") || callOut.equals("true")){
						
					String time = rs2.getString("stopcall");
					String time1 = rs2.getString("calltime");
					model.setLastCallTime(time1);
					int in = 10,out = 5;
					String time2 = CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss");
					   java.util.Date now = df.parse(time);
					   java.util.Date date=df.parse(time1);
					   java.util.Date toDay=df.parse(time2);
					   long l=now.getTime()-date.getTime();
					   long l1=toDay.getTime()-date.getTime();
					   long today=l1/(24*60*60*1000);
					   long day=l/(24*60*60*1000);
					   long hour=(l/(60*60*1000)-day*24);
					   long min=((l/(60*1000))-day*24*60-hour*60);
					   
					   if(today > 20 && today <30){
						   in = in - 4;
						   out = out -2;
					   }else if(today >= 30 && today < 50){
						   in -= 6;
						   out = out -3;
					   }else if(today >= 50){
						   in -= 8;
						   out = out -4;
					   }else if(today <= 20 && today > 7){
						   in -= 2;
						   out = out -1;
					   }else if(today <= 7 && today >= 0){
						   in -= 0;
						   out = out - 0;
					   }
					   
					   if(hour > 0 || min >= 10){
						   if(callIn.equals("true")){
							   grade = grade + in + 6;
						   }else  if(callOut.equals("true"))
							   grade = grade + out + 3;
					   }else if(hour == 0 && min < 10 && min >= 3){
						   if(callIn.equals("true")){
							   grade = grade + in + 4;
						   }else if(callOut.equals("true"))
							   grade = grade + out + 2;
					   }else if(hour == 0 && min < 3 && min >0){
						   if(callIn.equals("true")){
							   grade = grade + in + 2;
						   }else  if(callOut.equals("true"))
							   grade = grade + out + 1;
					   }else if(hour == 0 && min == 0){
						   if(callIn.equals("true")){
							   grade = grade + in + 1;
						   }else  if(callOut.equals("true"))
							   grade = grade + out + 1;
					   }
					}
					   
				}
				if(rs2 != null)
					rs2.close();
				if(p2 != null){
					p2.close();
				}
				model.setCallGrade(grade);
				if(model.getLastCallTime().equals("")){
					PreparedStatement p7 = conR1.prepareStatement("select Register.time from Register where Register.userphone='"+model.getCustomer_phone()+"'");
					ResultSet rs3 = p7.executeQuery();
					if(rs3 != null && rs3.next()){
						model.setLastCallTime(rs3.getString("time"));
					}else{
						arr.remove(model);
					}
					
					if(rs3 != null){
						rs3.close();
					}
					p7.close();
				}
			}
			conR1.close();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return arr;
		
	}
	
	public static ArrayList<Model> getLaoDaiXin(String phone){
		ArrayList<Model> mArr = new ArrayList<Model>();
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		
		try {
			Class.forName(Driver).newInstance();
			String sql = "select *from Register where Register.laoyezhuphone='"+phone+"'";
			Connection conR=DriverManager.getConnection(rURL,Username,Password);	
			PreparedStatement p = conR.prepareStatement(sql);
			ResultSet rs = p.executeQuery();
			if(rs != null ){
				while(rs.next()){
					Model model = new Model();
					model.setTime(rs.getString("time"));
					model.setPhone(rs.getString("userphone"));
					model.setName(rs.getString("username"));
					mArr.add(model);
				}
			}
			if(rs != null){
				rs.close();
				rs = null;
			}
			p.close();
			conR.close();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return mArr;
		
	}
	
	public static int updataConsumerModel(ConsumerModel consumer,boolean flag,HttpServletResponse response){
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		int count = 0;
		String wSql = "";
		try {
			Class.forName(Driver).newInstance();
			Connection conW=DriverManager.getConnection(wURL,Username,Password);
			if(!consumer.getChengjiaojine().equals("") && !consumer.getKehushuxing().equals("新客户")){
				
				if(!consumer.getLaoyezhuphone().equals("") && consumer.getKehushuxing().equals("老带新")){
					String laoyezhuStr = "UPDATE Register set Register.islaoyezhu='true',Register.laoyezhuphone='"+consumer.getLaoyezhuphone()+"' where Register.userphone='"+consumer.getCustomer_phone()+"'";
					PreparedStatement laoyezhuP = conW.prepareStatement(laoyezhuStr);
					laoyezhuP.executeUpdate();
					laoyezhuP.close();
				}else{
					consumer.setKehushuxing("老业主");
					String chengjiaoStr = "UPDATE Register set Register.laoyezhuphone='',Register.islaoyezhu='true' where Register.userphone='"+consumer.getCustomer_phone()+"'";
					PreparedStatement chengjiaoP = conW.prepareStatement(chengjiaoStr);
					chengjiaoP.executeUpdate();
					chengjiaoP.close();
				}
			}else{
				String chengjiaoStr = "UPDATE Register set Register.islaoyezhu='false' ,Register.laoyezhuphone='' where Register.userphone='"+consumer.getCustomer_phone()+"'";
				PreparedStatement chengjiaoP = conW.prepareStatement(chengjiaoStr);
				chengjiaoP.executeUpdate();
				chengjiaoP.close();
			}
			
//			if(!consumer.getLaoyezhuphone().equals("")){
//				String laoyezhuStr = "UPDATE Register set Register.laoyezhuphone='"+consumer.getLaoyezhuphone()+"' where Register.userphone='"+consumer.getCustomer_phone()+"'";
//				PreparedStatement laoyezhuP = conW.prepareStatement(laoyezhuStr);
//				laoyezhuP.executeUpdate();
//				laoyezhuP.close();
//			}else{
//				String laoyezhuStr = "UPDATE Register set Register.laoyezhuphone='' where Register.userphone='"+consumer.getCustomer_phone()+"'";
//				PreparedStatement laoyezhuP = conW.prepareStatement(laoyezhuStr);
//				laoyezhuP.executeUpdate();
//				laoyezhuP.close();
//			}
			
			if(consumer.getBeizhu() != null && !consumer.getBeizhu().equals("")){
				String wSql1 = "insert into fubiao(stopcall,calltime,fubiao.phone,fubiao.beizhu,fubiao.in,fubiao.out,fubiao.datatime,fubiao.myphone) values('2000-01-01 01:01:01','2000-01-01 01:01:01','"+consumer.getCustomer_phone()+"','"+consumer.getBeizhu()
				+"','-1','-1','"+CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss")+"','"+consumer.getConsultantphone()+"')";
				Connection conW1=DriverManager.getConnection(wURL,Username,Password);	
				PreparedStatement p1 = conW1.prepareStatement(wSql1);
				count += p1.executeUpdate();
				p1.close();
				conW1.close();
			}
			
			
			if(flag){
				wSql = "UPDATE MyConsultant SET MyConsultant.customer_name ='"+consumer.getCustomer_name()+"',MyConsultant.customer_phone='"+consumer.getCustomer_phone()+"',MyConsultant.consultantphone='"+consumer.getConsultantphone()+"',quyu='"+consumer.getQuyu()+"',MyConsultant.datatime='"+consumer.getDatatime()+"',MyConsultant.consultantname='"+consumer.getConsultantname()+"',xingbie='"+consumer.getXingbie()+
				"',temp9='"+consumer.getTemp9()+"'  where MyConsultant.customer_phone='"+consumer.getCustomer_phone()+"'";
			}else{
			wSql = "UPDATE MyConsultant SET MyConsultant.customer_name ='"+consumer.getCustomer_name()+"',MyConsultant.customer_phone='"+consumer.getCustomer_phone()+"',MyConsultant.consultantphone='"+consumer.getConsultantphone()+"',quyu='"+consumer.getQuyu()+"',louhao='"+consumer.getLouhao()+"',MyConsultant.datatime='"+consumer.getDatatime()
			+"',MyConsultant.consultantname='"+consumer.getConsultantname()+"',kehuleixing='"+consumer.getKehuleixing()+"',xingbie='"+consumer.getXingbie()+"',yisuan='"+consumer.getYisuan()
			+"',yixiang='"+consumer.getYixiang()+"',yixiangyetai='"+consumer.getYixiangyetai()+"',kehuzu='"+consumer.getKehuzu()+"',yixiangmianji='"+consumer.getYixiangmianji()+"',zijinshili='"
			+consumer.getZijinshili()+"',yixianghuxing='"+consumer.getYixianghuxing()+"',kehushuxing='"+consumer.getKehushuxing()+"',renzhiqudao='"+consumer.getRenzhiqudao()+"',dizhi='"+consumer.getDizhi()+"'," +
					"temp1='"+consumer.getTemp1()+"',temp2='"+consumer.getTemp2()+"',temp3='"+consumer.getTemp3()+"',temp4='"+consumer.getTemp4()+"',temp5='"+consumer.getTemp5()+"',temp6='"+consumer.getTemp6()+
					"',temp7='"+consumer.getTemp7()+"',temp9='"+consumer.getTemp9()+"',shenfenzheng='"+consumer.getShenfenzheng()+"',chengjiaojine='"+consumer.getChengjiaojine()+"'  where MyConsultant.customer_phone='"+consumer.getCustomer_phone()+"'";
			}
			//,temp10='"+consumer.getTemp10()+"'
			PreparedStatement p = conW.prepareStatement(wSql);
			count += p.executeUpdate();
			p.close();
			
			conW.close();
			if(count > 0){
				TextMessage t = new TextMessage();
				t.setContent("更新成功");
				response.getWriter().print(MessageUtil.textMessageToXml(t));
			}else{
				TextMessage t = new TextMessage();
				t.setContent("更新失败");
				response.getWriter().print(MessageUtil.textMessageToXml(t));
			}
		} catch (InstantiationException e) {
			TextMessage t = new TextMessage();
			t.setContent("更新失败  = "+e.toString());
			try {
				response.getWriter().print(MessageUtil.textMessageToXml(t));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			TextMessage t = new TextMessage();
			t.setContent("更新失败  = "+e.toString());
			try {
				response.getWriter().print(MessageUtil.textMessageToXml(t));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			TextMessage t = new TextMessage();
			t.setContent("更新失败  = "+e.toString());
			try {
				response.getWriter().print(MessageUtil.textMessageToXml(t));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (SQLException e) {
			TextMessage t = new TextMessage();
			t.setContent("更新失败  = "+e.toString());
			try {
				response.getWriter().print(MessageUtil.textMessageToXml(t));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			TextMessage t = new TextMessage();
			t.setContent("更新失败  = "+e.toString());
			try {
				response.getWriter().print(MessageUtil.textMessageToXml(t));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return count;
	}
	
	public static ArrayList<CallModel> getCallRecord(String phone){
		ArrayList<CallModel> callArr = new ArrayList<CallModel>();
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Class.forName(Driver).newInstance();
			String myDate = CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss");
			Date date1 = sdf.parse(myDate);
			String befroeDate = sdf.format(new Date(date1.getTime() - 3 * 24 * 60 * 60 * 1000));
			String sql = "select *from fubiao where fubiao.myphone = '"+phone+"' and timestampdiff(second,'"+myDate+"',calltime) < 0 and timestampdiff(second,'"+befroeDate+"',calltime) > 0";
			Connection conR = DriverManager.getConnection(rURL,Username,Password);	
			PreparedStatement p = conR.prepareStatement(sql);
			ResultSet rs = p.executeQuery();
			if(rs != null){
				while(rs.next()){
					CallModel call = new CallModel();
					String in = rs.getString("in");
					String out = rs.getString("out");
					if(in.equals("true")){
						call.setIn(true);
					}else if(out.equals("true")){
						call.setOut(true);
					}else 
						continue;
					call.setCallTime(rs.getString("calltime"));
					call.setPhone(rs.getString("phone"));
					String sql1 = "select MyConsultant.customer_name,MyConsultant.yixianghuxing,MyConsultant.yixiang,MyConsultant.yisuan from MyConsultant where MyConsultant.customer_phone ='"+call.getPhone()+"'";
					PreparedStatement p1 = conR.prepareStatement(sql1);
					ResultSet rs1 = p1.executeQuery();
					if(rs1 != null && rs1.next()){
						call.setYixiang(rs1.getString("MyConsultant.yixiang"));
						call.setName(rs1.getString("MyConsultant.customer_name"));
						call.setYixianghuxing(rs1.getString("MyConsultant.yixianghuxing"));
						call.setYusuan(rs1.getString("MyConsultant.yisuan"));
					}
					if(rs1 != null){
						rs1.close();
						p1.close();
					}
					
					callArr.add(call);
				}
				rs.close();
				p.close();
				conR.close();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return callArr;
	}
	
//	public static void updataConsumer(ConsumerAccept consumer){
//		synchronized(lockWrite){
//		String Username=SaeUserInfo.getAccessKey();
//		String Password=SaeUserInfo.getSecretKey();
//		String Driver="com.mysql.jdbc.Driver";
//		
//		try {
//			Class.forName(Driver).newInstance();
//			String wSql = "UPDATE customer SET customer_name ='"+consumer.getCustomer_name()+"',customer_phone='"+consumer.getCustomer_phone()+"',isaccept='"+consumer.getIsaccept()
//			+"',helpacceptname='"+consumer.getHelpacceptname()+"',accept='"+consumer.getAccept()+"',consultantname='"+consumer.getConsultantname()+"',consultantphone='"+consumer.getConsultantphone()
//			+"',saleid='"+consumer.getSaleid()+"',ishelp='"+consumer.getIshelp()+"'  where customer_id="+consumer.getServerid();
//			Connection conW=DriverManager.getConnection(wURL,Username,Password);	
//			PreparedStatement p = conW.prepareStatement(wSql);
//			p.executeUpdate();
//			p.close();
//			conW.close();
//			conW = null;
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		}
//		
//	}
//	
	
	public static void getDayDetail(HttpServletResponse resp,String myPhone){
		HashMap<String,TodayStatistics> hMap = new HashMap<String,TodayStatistics>();
		ArrayList<TodayStatistics> lst = null;
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		String Driver="com.mysql.jdbc.Driver";
		
		String myDate = CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cale = Calendar.getInstance();   
        cale.set(Calendar.DAY_OF_MONTH,0);
        String lastDay = sdf.format(cale.getTime());
        
        Date date;
		try {
			date = sdf.parse(lastDay);
			lastDay = sdf.format(new Date(date.getTime() + 1 * 24 * 60 * 60 * 1000)); 
			Date d1 = sdf.parse(lastDay);
			
			Date date1 = sdf.parse(myDate);
			myDate = sdf.format(new Date(date1.getTime() + 1 * 24 * 60 * 60 * 1000)); 
			Date d2 = sdf.parse(myDate);
			if(d1.before(d2)){
				Class.forName(Driver).newInstance();
				String str = "select *from fubiao where fubiao.myPhone='"+myPhone+"' and timestampdiff(second,'"+myDate+"',calltime) < 0 and  timestampdiff(second,'"+lastDay+"',calltime) > 0";
				Connection conR=DriverManager.getConnection(rURL,Username,Password);	
				PreparedStatement p = conR.prepareStatement(str);
				ResultSet rs = p.executeQuery();
				
				if(rs != null){
					lst = new ArrayList<TodayStatistics>();
					Connection conR1=DriverManager.getConnection(rURL,Username,Password);
					while(rs.next()){
						String phone = rs.getString("phone");
						String callTime = rs.getString("calltime").substring(0, 10);
						String key = callTime+phone;
						Date nowTime = sdf1.parse(callTime);
						PreparedStatement p3 = conR1.prepareStatement("select fubiao.in from fubiao where fubiao.in = 'true' and fubiao.phone='"+phone+"' and calltime='"+rs.getString("calltime")+"'");
						ResultSet rs3 = p3.executeQuery();
						int todayCount = 0;
						if(rs3 != null && rs3.next()){
							todayCount = 1;
						}
						if(rs3 != null){
							rs3.close();
							p3.close();
						}
						
						if(todayCount  == 0)
							continue;
						
						if(hMap.get(callTime) == null){
							hMap.put(key, new TodayStatistics());
						}
						hMap.get(key).setPhone(phone);
						hMap.get(key).setDay(callTime.substring(8, 10));
						hMap.get(key).setMonth(callTime);
						
						if(rs.getString("in").equals("true"))
							hMap.get(key).setDayLaiDian(1);
						String xinfang = "select count(fubiao.in),min(calltime) from fubiao where fubiao.in = 'true' and fubiao.phone='"+phone+"'";
						PreparedStatement p1 = conR1.prepareStatement(xinfang);
						ResultSet rs1 = p1.executeQuery();
						
						int count = 0;
						String time = "";
						if(rs1 != null && rs1.next()){
							count = rs1.getInt("count(fubiao.in)");
							time = rs1.getString("min(calltime)");
						}
						if(count > 1 && todayCount > 0){
							if(nowTime.after(sdf1.parse(time.substring(0, 10)))){
								hMap.get(key).setDayFuFang(1);
							}else{
								hMap.get(key).setDayXinFang(1);
							}
						}else if(todayCount > 0){
							hMap.get(key).setDayXinFang(1);
						}
						
						if(rs1 != null){
							rs1.close();
							p1.close();
						}
						
						String yixiang = "select yixiang,MyConsultant.customer_name from MyConsultant where MyConsultant.customer_phone='"+phone+"'";
						PreparedStatement p2 = conR1.prepareStatement(yixiang);
						ResultSet rs2 = p2.executeQuery();
						
						if(rs2 !=null && rs2.next()){
							String yStr = rs2.getString("yixiang");
							if(yStr.equals("强")){
								hMap.get(key).setDayYiXiangQiang(1);
							}else if(yStr.equals("中")){
								hMap.get(key).setDayYiXiang(1);
							}else if(yStr.equals("一般")){
								hMap.get(key).setDayYiXiangYiBan(1);
							}
							hMap.get(key).setName(rs2.getString("customer_name"));
						}
						String rengou = "select kehushuxing from MyConsultant where timestampdiff(second,MyConsultant.datatime,'"+callTime+" 00:00:00"+"') < 0 and timestampdiff(second,MyConsultant.datatime,'"+callTime+" 23:59:59"+"') > 0 and MyConsultant.customer_phone='"+phone+"'";
						p2 = conR1.prepareStatement(rengou);
						rs2 = p2.executeQuery();
						if(rs2 !=null && rs2.next()){
							String rStr = rs2.getString("kehushuxing");
							if(!rStr.equals("新客户")){
								hMap.get(key).setDayXinZengQianYue(1);
							}
						}
						if(rs2 != null){
							rs2.close();
							p2.close();
						}
					}
				}
				if(rs != null){
					rs.close();
				}
				
				  Iterator it =  hMap.entrySet().iterator();
	              while(it.hasNext()){          
	                      Entry  obj = (Entry) it.next();
	                     lst.add(hMap.get(obj.getKey()));
	              }
			}
			 Gson gson = new Gson();
			resp.getWriter().print(gson.toJson(lst));
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void exit(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException{
		
		String Username=SaeUserInfo.getAccessKey();
		String Password=SaeUserInfo.getSecretKey();
		
		String wSql = "UPDATE managerserver SET online = 'no' WHERE managerserver.phone ='"+req.getParameter("id")+"'";
		Connection conW=DriverManager.getConnection(wURL,Username,Password);	
		PreparedStatement p = conW.prepareStatement(wSql);
		p.executeUpdate();
		conW.close();
		conW = null;
		
//		CoreServlet.responseArr.get(req.getParameter("phone")).msgArr.clear();
//		CoreServlet.responseArr.remove(req.getParameter("phone"));
		
//		if(CoreServlet.responseArr.get(req.getParameter("phone")) != null){
//		TextMessage text = new TextMessage();
//		text.setContent(req.getParameter("phone"));
//		resp.getWriter().write(MessageUtil.textMessageToXml(text));
//		}else{
			TextMessage text = new TextMessage();
			text.setContent("退出");
			resp.getWriter().write(MessageUtil.textMessageToXml(text));
//		}
	}
	
//	public static void addUserToArr(String id) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
//		String Username=SaeUserInfo.getAccessKey();
//		String Password=SaeUserInfo.getSecretKey();
//		String Driver="com.mysql.jdbc.Driver";
//		Class.forName(Driver).newInstance();
//		
//		
//		String sql = "select * from managerserver WHERE online = 'yes' and managerserver.phone ='"+id+"'";
//		Connection conW=DriverManager.getConnection(rURL,Username,Password);
//		PreparedStatement p = conW.prepareStatement(sql);
//		ResultSet rs = (ResultSet) p.executeQuery();
//		if(rs.next()){
//			UserInfo user = new UserInfo();
//			user.setId(rs.getInt("managerserver.phone"));
//			user.setCount(rs.getInt("count"));
//			user.setOnline(rs.getString("online"));
//			user.setState(rs.getString("state"));
//			user.setUsername(rs.getString("username"));
//			
//			ResponseModel.userArr.put(rs.getInt("id"),user);
//		}
//		if(rs != null){
//			rs.close();
//			rs = null;
//		}
//		conW.close();
//		conW = null;
//	}
	
//	public static HashMap<Integer, UserInfo> getUsers(String id) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
//		String Username=SaeUserInfo.getAccessKey();
//		String Password=SaeUserInfo.getSecretKey();
//		String Driver="com.mysql.jdbc.Driver";
//		Class.forName(Driver).newInstance();
//		
//		String rSql = "select managerid from managerserver WHERE managerserver.phone ='"+id+"'";
//		Connection conW=DriverManager.getConnection(rURL,Username,Password);
//		PreparedStatement p = conW.prepareStatement(rSql);
//		ResultSet rs = (ResultSet) p.executeQuery();
//		
//		if(rs.next()){
//			int managerid = rs.getInt("managerid");
//			
//			String sql = "select * from managerserver WHERE online = 'yes' and managerid ='"+managerid+"'";
//			PreparedStatement p1 = conW.prepareStatement(sql);
//			ResultSet rs1 = (ResultSet) p1.executeQuery();
//			
//			if(rs1 != null){
//				ResponseModel.userArr.clear();
//				while(rs1.next()){
//					UserInfo user = new UserInfo();
//					user.setId(rs1.getString("phone"));
//					user.setCount(rs1.getInt("count"));
//					user.setManagerid(managerid);
//					user.setOnline(rs1.getString("online"));
//					user.setState(rs1.getString("state"));
//					user.setUsername(rs1.getString("username"));
//					
//					ResponseModel.userArr.put(rs1.getInt("id"),user);
//				}
//			}
//		}
//		if(rs != null){
//			rs.close();
//			rs = null;
//		}
//		conW.close();
//		conW = null;
//		
//		return ResponseModel.userArr;
//	}
}
