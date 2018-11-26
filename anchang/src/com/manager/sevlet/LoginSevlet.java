package com.manager.sevlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manager.model.TextMessage;
import com.manager.util.DataManager;
import com.manager.util.MessageUtil;
import com.sina.sae.util.SaeUserInfo;
/*
 * 
w.rdc.sae.sina.com.cn 读写
r.rdc.sae.sina.com.cn 只读
 */
public class LoginSevlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		//这里做你需要的操作

		
		//我写的登录操作  你要改成你自己的事件
		DataManager.login(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	doGet(req,resp);
	}
}
