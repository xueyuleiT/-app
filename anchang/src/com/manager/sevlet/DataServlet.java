package com.manager.sevlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.manager.model.ConsultModel;
import com.manager.model.ConsumerAccept;
import com.manager.model.ConsumerModel;
import com.manager.model.MyModel;
import com.manager.model.TextMessage;
import com.manager.util.CurrentTime;
import com.manager.util.DataManager;
import com.manager.util.MessageUtil;
import com.mysql.jdbc.ResultSet;
import com.sina.sae.util.SaeUserInfo;

public class DataServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		if(req.getParameter("action").equals("updataconsumermodel")){
			Enumeration emRation = req.getParameterNames();
			ConsumerModel con = new ConsumerModel();
			while (emRation.hasMoreElements()) {
				String name = (String) emRation.nextElement();
				if (name.equals("customer_name")) {
					con.setCustomer_name(""+req.getParameter(name));
				} else if (name.equals("customer_phone")) {
					con.setCustomer_phone(""+req.getParameter(name));
				} else if (name.equals("datatime")) {
					con.setDatatime(""+req.getParameter(name));
				} else if (name.equals("consultantname")) {
					con.setConsultantname(""+req.getParameter(name));
				} else if (name.equals("consultantphone")) {
					con.setConsultantphone(""+req.getParameter(name));
				} else if (name.equals("kehuleixing")) {
					con.setKehuleixing(""+req.getParameter(name));
				} else if (name.equals("xingbie")) {
					con.setXingbie(""+req.getParameter(name));
				} else if (name.equals("yisuan")) {
					con.setYisuan(""+req.getParameter(name));
				} else if (name.equals("yixiang")) {
					con.setYixiang(""+req.getParameter(name));
				} else if (name.equals("yixiangyetai")) {
					con.setYixiangyetai(""+req.getParameter(name));
				} else if (name.equals("kehuzu")) {
					con.setKehuzu(""+req.getParameter(name));
				} else if (name.equals("yixiangmianji")) {
					con.setYixiangmianji(""+req.getParameter(name));
				} else if (name.equals("zijinshili")) {
					con.setZijinshili(""+req.getParameter(name));
				}else if (name.equals("yixianghuxing")) {
					con.setYixianghuxing(""+req.getParameter(name));
				}else if (name.equals("kehushuxing")) {
					con.setKehushuxing(""+req.getParameter(name));
				}else if (name.equals("renzhiqudao")) {
					con.setRenzhiqudao(""+req.getParameter(name));
				}else if (name.equals("dizhi")) {
					con.setDizhi(""+req.getParameter(name));
				}else if (name.equals("beizhu")) {
					con.setBeizhu(""+req.getParameter(name));
				}else if (name.equals("temp1")) {
					con.setTemp1(""+req.getParameter(name));
				}else if (name.equals("temp2")) {
					con.setTemp2(""+req.getParameter(name));
				}else if (name.equals("temp3")) {
					con.setTemp3(""+req.getParameter(name));
				}else if (name.equals("temp4")) {
					con.setTemp4(""+req.getParameter(name));
				}else if (name.equals("temp5")) {
					con.setTemp5(""+req.getParameter(name));
				}else if (name.equals("temp6")) {
					con.setTemp6(""+req.getParameter(name));
				}else if (name.equals("temp7")) {
					con.setTemp7(""+req.getParameter(name));
				}else if (name.equals("temp8")) {
					con.setTemp8(""+req.getParameter(name));
				}else if (name.equals("temp9")) {
					con.setTemp9(""+req.getParameter(name));
				}else if (name.equals("temp10")) {
					con.setTemp10(""+req.getParameter(name));
				}else if (name.equals("quyu")) {
					con.setQuyu(req.getParameter(name));
				}else if (name.equals("louhao")) {
					con.setLouhao(req.getParameter(name));
				}else if (name.equals("shenfenzheng")) {
					con.setShenfenzheng(req.getParameter(name));
				}else if(name.equals("chengjiaojine")){
					con.setChengjiaojine(req.getParameter(name));
				}else if(name.equals("laoyezhuphone")){
					con.setLaoyezhuphone(req.getParameter(name));
				}
			}
			if(req.getParameter("type").equals("insert")){
				int i = DataManager.insertConsumerModel(con,resp);
//				if(i > 1 && !con.getBeizhu().equals("")){
//					TextMessage t = new TextMessage();
//					t.setContent("更新成功");
//					resp.getWriter().print(MessageUtil.textMessageToXml(t));
//					resp.getWriter().flush();
//				}else if(con.getBeizhu().equals("") && i >0){
//					TextMessage t = new TextMessage();
//					t.setContent("更新成功");
//					resp.getWriter().print(MessageUtil.textMessageToXml(t));
//					resp.getWriter().flush();
//				}else{
//					TextMessage t = new TextMessage();
//					t.setContent("更新失败");
//					resp.getWriter().print(MessageUtil.textMessageToXml(t));
//					resp.getWriter().flush();
//				}
			}else if(DataManager.updataConsumerModel(con,false,resp) > 0){
//				TextMessage t = new TextMessage();
//				t.setContent("更新成功");
//				resp.getWriter().print(MessageUtil.textMessageToXml(t));
			}else{
//				TextMessage t = new TextMessage();
//				t.setContent("更新失败");
//				resp.getWriter().print(MessageUtil.textMessageToXml(t));
			}
		}else if (req.getParameter("action").equals("fastupdataconsumermodel")) {
			Enumeration emRation = req.getParameterNames();
			ConsumerModel con = new ConsumerModel();
			while (emRation.hasMoreElements()) {
				String name = (String) emRation.nextElement();
				if (name.equals("customer_name")) {
					con.setCustomer_name(req.getParameter(name));
				} else if (name.equals("customer_phone")) {
					con.setCustomer_phone(req.getParameter(name));
				} else if (name.equals("datatime")) {
					con.setDatatime(req.getParameter(name));
				} else if (name.equals("consultantname")) {
					con.setConsultantname(req.getParameter(name));
				} else if (name.equals("consultantphone")) {
					con.setConsultantphone(req.getParameter(name));
				}else if (name.equals("xingbie")) {
					con.setXingbie(req.getParameter(name));
				}else if (name.equals("quyu")) {
					con.setQuyu(req.getParameter(name));
				}else if(name.equals("beizhu")){
					con.setBeizhu(req.getParameter(name));
				}else if (name.equals("temp9")) {
					con.setTemp9(req.getParameter(name));
				}else if(name.equals("kehushuxing")){
					con.setKehushuxing(req.getParameter(name));
				}
			
			}
			if(req.getParameter("type").equals("insert")){
				if(DataManager.insertConsumerModel(con,resp) > 0){
//					TextMessage t = new TextMessage();
//					t.setContent("更新成功");
//					resp.getWriter().print(MessageUtil.textMessageToXml(t));
//					resp.getWriter().flush();
				}
			}else if(DataManager.updataConsumerModel(con,true,resp) > 0){
//				TextMessage t = new TextMessage();
//				t.setContent("更新成功");
//				resp.getWriter().print(MessageUtil.textMessageToXml(t));
			}
		}else if(req.getParameter("action").equals("getconsumermodel")){
			ArrayList<ConsumerModel> lst = DataManager.getConsumerModel(req.getParameter("quyu"),req.getParameter("phone"),resp);
			if(lst.size() > 0){
				Gson gson = new Gson();
				resp.getWriter().print(gson.toJson(lst));
				resp.getWriter().flush();
			}
		}else if(req.getParameter("action").equals("getoneconsumermodel")){
			ConsumerModel con = DataManager.getOneConsumerModel(req.getParameter("phone"));
			if(con != null){
				Gson gson = new Gson();
				resp.getWriter().print(gson.toJson(con));
				resp.getWriter().flush();
			}
		}else if(req.getParameter("action").equals("insertcall")){
			
			if(DataManager.insertCall(req.getParameter("start"), req.getParameter("stop"), req.getParameter("isCallIn"), req.getParameter("phone"), req.getParameter("myphone")) > 0){
				TextMessage t = new TextMessage();
				t.setContent("更新成功");
				resp.getWriter().print(MessageUtil.textMessageToXml(t));
				resp.getWriter().flush();
			}else{
				TextMessage t = new TextMessage();
				t.setContent("更新失败");
				resp.getWriter().print(MessageUtil.textMessageToXml(t));
				resp.getWriter().flush();
			}
		}
//		else if (req.getParameter("action").equals("updata")) {
//			Enumeration emRation = req.getParameterNames();
//			ConsumerAccept con = new ConsumerAccept();
//			while (emRation.hasMoreElements()) {
//				String name = (String) emRation.nextElement();
//				if (name.equals("customer_name")) {
//					con.setCustomer_name(req.getParameter(name));
//				} else if (name.equals("customer_phone")) {
//					con.setCustomer_phone(req.getParameter(name));
//				} else if (name.equals("isaccept")) {
//					con.setIsaccept(req.getParameter(name));
//				} else if (name.equals("ishelp")) {
//					con.setIshelp(req.getParameter(name));
//				} else if (name.equals("accept")) {
//					con.setAccept(req.getParameter(name));
//				} else if (name.equals("datatime")) {
//					con.setDatatime(req.getParameter(name));
//				} else if (name.equals("imgpath")) {
//					con.setImgpath(req.getParameter(name));
//				} else if (name.equals("saleid")) {
//					con.setSaleid(req.getParameter(name));
//				} else if (name.equals("consultantname")) {
//					con.setConsultantname(req.getParameter(name));
//				} else if (name.equals("consultantphone")) {
//					con.setConsultantphone(req.getParameter(name));
//				} else if (name.equals("helpacceptname")) {
//					con.setHelpacceptname(req.getParameter(name));
//				} else if (name.equals("serverid")) {
//					con.setServerid(Integer.parseInt(req.getParameter(name)));
//				}
//			}
//			DataManager.updataConsumer(con);
//			if(con.getIshelp().equals("true") && !req.getParameter("acceptid").equals("-1")){
//				TextMessage tMsg = new TextMessage();
//				tMsg.setFromid(con.getSaleid());
//				tMsg.setServerId(con.getServerid());
//				tMsg.setFromType("HELPACCEPT");
//				tMsg.setName(con.getConsultantname());
//				tMsg.setContent(con.getImgpath());
//				CoreServlet.responseArr.get(req.getParameter("acceptid")).msgArr.add(tMsg);
//			}
//			TextMessage t = new TextMessage();
//			t.setContent("更新成功");
//			resp.getWriter().print(MessageUtil.textMessageToXml(t));
//		} 
		else if(req.getParameter("action").equals("unRegister")){
			int i = DataManager.unRegister(req.getParameter("phone"),resp);
			if(i > 0){
				TextMessage t = new TextMessage();
				t.setContent("更新成功");
				resp.getWriter().print(MessageUtil.textMessageToXml(t));
				resp.getWriter().flush();
			}else if(i == -1){
				TextMessage t = new TextMessage();
				t.setContent("未注册");
				resp.getWriter().print(MessageUtil.textMessageToXml(t));
				resp.getWriter().flush();
			}
		}else if(req.getParameter("action").equals("getdaydetail")){
			DataManager.getDayDetail(resp,req.getParameter("myphone"));
		}
//		else if(req.getParameter("action").equals("getdata")){
//			int serverid = Integer.parseInt(req.getParameter("serverid"));
//			String gStr = DataManager.getConsumer(serverid);
//			resp.getWriter().print(gStr);
//			resp.getWriter().flush();
//		}
//		else if(req.getParameter("action").equals("getconsultants")){
//			ArrayList<ConsultModel> lst = DataManager.getConsultants();
//			if(lst.size() > 0){
//				Gson gson = new Gson();
//				resp.getWriter().print(gson.toJson(lst));
//				resp.getWriter().flush();
//			}
//		}
	   else if(req.getParameter("action").equals("getmymodel")){
			ArrayList<MyModel> lst = DataManager.getSelectConsultants();
			if(lst.size() > 0){
				Gson gson = new Gson();
				resp.getWriter().print(gson.toJson(lst));
				resp.getWriter().flush();
			}
		}else if(req.getParameter("action").equals("requestaccept")){
			TextMessage tMsg = new TextMessage();
			tMsg.setServerId(Integer.parseInt(req.getParameter("serverid")));
			tMsg.setFromType("HELPACCEPT");
			tMsg.setContent(req.getParameter("imgpath"));
			CoreServlet.responseArr.get(req.getParameter("acceptid")).msgArr.add(tMsg);
			
		}else if(req.getParameter("action").equals("helpother")){
			synchronized(DataManager.lockWrite){
				
				TextMessage tMsg = new TextMessage();
				tMsg.setServerId(Integer.parseInt(req.getParameter("serverid")));
				String Username=SaeUserInfo.getAccessKey();
				String Password=SaeUserInfo.getSecretKey();
				String Driver="com.mysql.jdbc.Driver";
				Connection conW;
				Connection conR;
				try {
					Class.forName(Driver).newInstance();
					conR = DriverManager.getConnection(DataManager.rURL,Username,Password);
					String rSql = "select ishelp from Consultant where accept='' and ishelp = 'true' and customer_id="+tMsg.getServerId();
					PreparedStatement pr = conR.prepareStatement(rSql);
					ResultSet rs = (ResultSet) pr.executeQuery();
					if(rs.next()){
						conW = DriverManager.getConnection(DataManager.wURL,Username,Password);
						String wSql = "UPDATE Consultant SET isaccept = 'true',accept='"+req.getParameter("userid")+"' ,imgpath = '' ,helpacceptname ='"+req.getParameter("helpacceptname")+"'   where customer_id="+tMsg.getServerId();
						PreparedStatement pw = conW.prepareStatement(wSql);
						pw.executeUpdate();
						pw.close();
						conW.close();
						conW = null;
						
						tMsg.setFromType("HELPACCEPT_RETURN");
						tMsg.setFromid(req.getParameter("userid"));
						tMsg.setName(req.getParameter("helpacceptname"));
						CoreServlet.responseArr.get(req.getParameter("touser")).msgArr.add(tMsg);
						
					}else{
						tMsg.setFromType("HELPACCEPT");
						tMsg.setContent("已经被接");
						resp.getWriter().print(MessageUtil.textMessageToXml(tMsg));
						resp.getWriter().flush();
					}
					if(rs != null){
						rs.close();
						rs = null;
					}
					pr.close();
					
					
				} catch (SQLException e) {
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}	
			}
		}else if(req.getParameter("action").equals("helpaccept")){
				
			TextMessage tMsg = new TextMessage();
			tMsg.setServerId(Integer.parseInt(req.getParameter("serverid")));
			tMsg.setContent(req.getParameter("imgpath"));
			tMsg.setFromType("HELPACCEPT");
			String Username=SaeUserInfo.getAccessKey();
			String Password=SaeUserInfo.getSecretKey();
			String Driver="com.mysql.jdbc.Driver";
			Connection conW;
			Connection conR;
			try {
				Class.forName(Driver).newInstance();
				conR = DriverManager.getConnection(DataManager.rURL,Username,Password);
				String rSql = "select ishelp from Consultant where ishelp!='true' and customer_id="+tMsg.getServerId();
				PreparedStatement pr = conR.prepareStatement(rSql);
				ResultSet rs = (ResultSet) pr.executeQuery();
				if(rs.next()){
					conW = DriverManager.getConnection(DataManager.wURL,Username,Password);
					String wSql = "UPDATE Consultant SET ishelp='true'  where customer_id="+tMsg.getServerId();
					PreparedStatement pw = conW.prepareStatement(wSql);
					pw.executeUpdate();
					pw.close();
					conW.close();
					conW = null;
					CoreServlet.responseArr.get(req.getParameter("acceptid")).msgArr.add(tMsg);
				}else{
					tMsg.setContent("已经被接");
					resp.getWriter().print(MessageUtil.textMessageToXml(tMsg));
					resp.getWriter().flush();
				}
				if(rs != null){
					rs.close();
					rs = null;
				}
				pr.close();
				
				
			} catch (SQLException e) {
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
		}
		}else if(req.getParameter("action").equals("accept")){
			String Username=SaeUserInfo.getAccessKey();
			String Password=SaeUserInfo.getSecretKey();
			String Driver="com.mysql.jdbc.Driver";
			Connection conW;
			try {
				Class.forName(Driver).newInstance();
				conW = DriverManager.getConnection(DataManager.wURL,Username,Password);
				String wSql = "UPDATE Consultant SET isaccept='true',imgpath = '' ,accept='"+req.getParameter("userid")+"' ,helpacceptname='"+req.getParameter("helpacceptname")+"'  where customer_id="+req.getParameter("serverid");
				PreparedStatement pw = conW.prepareStatement(wSql);
				pw.executeUpdate();
				pw.close();
				conW.close();
				conW = null;
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

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			doPost(req, resp);
	}
	
	
}
