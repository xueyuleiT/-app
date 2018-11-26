package com.manager.sevlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.manager.model.BeizhuModel;
import com.manager.model.CallModel;
import com.manager.model.Model;
import com.manager.model.TextMessage;
import com.manager.util.DataManager;
import com.manager.util.MessageUtil;

public class DataServletTwo extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		if(req.getParameter("action").equals("getcallrecord")){
			ArrayList<CallModel> arr = DataManager.getCallRecord(req.getParameter("myphone"));
			if(arr.size() > 0){
				Gson gson = new Gson();
				resp.getWriter().print(gson.toJson(arr));
			}
		}else if(req.getParameter("action").equals("islaoyezhu")){
			if(DataManager.isLaoyezhu(req.getParameter("phone")).equals("true")){
				TextMessage tMsg = new TextMessage();
				tMsg.setContent("是");
				resp.getWriter().write(MessageUtil.textMessageToXml(tMsg));
				resp.getWriter().flush();
			}else{
				TextMessage tMsg = new TextMessage();
				tMsg.setContent("否");
				resp.getWriter().write(MessageUtil.textMessageToXml(tMsg));
				resp.getWriter().flush();
			}
		}else if(req.getParameter("action").equals("getcalldetail")){
			DataManager.getCallDetail(req.getParameter("phone"), resp);
		}else if(req.getParameter("action").equals("gettask")){
			DataManager.getTask(req.getParameter("phone"), resp);
		}else if(req.getParameter("action").equals("getnewadd")){
			DataManager.getNewAdd(req.getParameter("phone"), resp);
		}else if(req.getParameter("action").equals("getxiaoshoumodel")){
			DataManager.getXiaoShouModel(req.getParameter("id"), resp);
		}else if(req.getParameter("action").equals("guohu")){
			if(DataManager.guohu(req.getParameter("fromphone"), req.getParameter("tophone")))
			{
				TextMessage t = new TextMessage();
				t.setContent("更新成功");
				PrintWriter print= resp.getWriter();
				print.print(MessageUtil.textMessageToXml(t));
				print.flush();
			}else{
				TextMessage t = new TextMessage();
				t.setContent("更新失败");
				PrintWriter print= resp.getWriter();
				print.print(MessageUtil.textMessageToXml(t));
				print.flush();
			}
		}else if(req.getParameter("action").equals("registerxiaoshou")){
			if(DataManager.registerXiaoshou(req.getParameter("phone"), req.getParameter("name"), req.getParameter("pwd"), req.getParameter("managerid"),req.getParameter("task"),resp))
			{
//				TextMessage t = new TextMessage();
//				t.setContent("更新成功");
//				PrintWriter print= resp.getWriter();
//				print.print(MessageUtil.textMessageToXml(t));
//				print.flush();
//			}else{
//				TextMessage t = new TextMessage();
//				t.setContent("更新失败");
//				PrintWriter print= resp.getWriter();
//				print.print(MessageUtil.textMessageToXml(t));
//				print.flush();
			}
		}else if(req.getParameter("action").equals("getallbeizhu")){
			ArrayList<BeizhuModel> resList = DataManager.getAllBeizhu(req.getParameter("phone"));
				if(resList.size() > 0){
					Gson gson = new Gson();
					PrintWriter p = resp.getWriter();
					p.print(gson.toJson(resList));
					p.flush();
				}
		}else if(req.getParameter("action").equals("getlaodaixin")){
			ArrayList<Model> mArr = DataManager.getLaoDaiXin(req.getParameter("phone"));
			if(mArr.size() > 0){
				Gson gson = new Gson();
				PrintWriter p = resp.getWriter();
				p.print(gson.toJson(mArr));
				p.flush();
			}
		}else if(req.getParameter("action").equals("settask")){
			int count = DataManager.setTask(req.getParameter("num"),req.getParameter("day"));
			if(count > 0){
				PrintWriter p = resp.getWriter();
				TextMessage t = new TextMessage();
				t.setContent("更新成功");
				p.print(MessageUtil.textMessageToXml(t));
				p.flush();
			}else{
				PrintWriter p = resp.getWriter();
				TextMessage t = new TextMessage();
				t.setContent("更新失败");
				p.print(MessageUtil.textMessageToXml(t));
				p.flush();
			}
		}else if(req.getParameter("action").equals("getfinishtask")){
			String str = DataManager.getFinishCount(req.getParameter("phone"),resp);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			doPost(req, resp);
	}
}
