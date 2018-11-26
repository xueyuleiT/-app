package com.manager.sevlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manager.model.TextMessage;
import com.manager.util.DataManager;
import com.manager.util.MessageUtil;

public class Register extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
		if(req.getParameter("action").equals("isRegister")){
			String str = DataManager.isRegister(req.getParameter("phone"));
			TextMessage tMsg = new TextMessage();
			if(str.equals("")){
				str = "Î´×¢²á";
			}
			tMsg.setContent(str);
			resp.getWriter().println(MessageUtil.textMessageToXml(tMsg));
		}else if(req.getParameter("action").equals("Register")){
			int count = DataManager.Register(req.getParameter("phone"), req.getParameter("userid"),req.getParameter("myphone"),req.getParameter("myname")
					,req.getParameter("laoyezhuphone"),req.getParameter("islaoyezhu"),req.getParameter("username"));
			TextMessage tMsg = new TextMessage();
			tMsg.setContent(""+count);
			resp.getWriter().println(MessageUtil.textMessageToXml(tMsg));
		}else if(req.getParameter("action").equals("unRegister")){
			int count = DataManager.unRegister(req.getParameter("phone"),resp);
			TextMessage tMsg = new TextMessage();
			tMsg.setContent(""+count);
			resp.getWriter().println(MessageUtil.textMessageToXml(tMsg));
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	doGet(req,resp);
	}
}
