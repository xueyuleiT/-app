package com.manager.sevlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manager.model.ResponseModel;
import com.manager.model.TextMessage;
import com.manager.util.DataManager;
import com.manager.util.MessageUtil;




/**
 * 核心请求处理类
 * 
 * @author liufeng
 * @date 2013-05-18
 */
public class CoreServlet extends HttpServlet {
	public static HashMap<String,ResponseModel> responseArr = new HashMap<String,ResponseModel>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 4440739483644821986L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		TextMessage textMsg = new TextMessage();
		if(request.getParameter("action").equals("comsumersend")){
			TextMessage text = new TextMessage();
			text.setContent(request.getParameter("content"));
			textMsg.setFromType("客户");
			responseArr.get(request.getParameter("touser")).msgArr.add(text);
		}else if (request.getParameter("action").equals("connect")) {
			textMsg.setContent("正确");
			textMsg.setFromType("服务器");
			ResponseModel model = new ResponseModel();
			responseArr.put(request.getParameter("id"), model);
			out.print(MessageUtil.textMessageToXml(textMsg));
		}else if(request.getParameter("action").equals("send")){
			String id = request.getParameter("id");
			TextMessage text = new TextMessage();
			text.setContent(request.getParameter("content"));
			text.setFromType("聊天");
			text.setFromid(id);
			text.setName(request.getParameter("name"));
			
			  Iterator it =  responseArr.entrySet().iterator();
              while(it.hasNext()){          
                      Entry  obj = (Entry) it.next();
                      if(!id.equals(String.valueOf(obj.getKey()))){
                    	  if(responseArr.get(obj.getKey()).msgArr.size() > 20){
                    		  responseArr.get(obj.getKey()).msgArr.add(text);
                    		  responseArr.get(obj.getKey()).msgArr.remove(21);
                    	  }else{
                    		  responseArr.get(obj.getKey()).msgArr.add(text);
                    	  }
                      }
              }
		}else if(request.getParameter("action").equals("get")){
			ResponseModel model = responseArr.get(request.getParameter("id"));
			if(model != null && model.msgArr.size() != 0){
				PrintWriter writer = response.getWriter();
				TextMessage textmsg = new TextMessage();
				textmsg = model.msgArr.get(0);
				model.msgArr.remove(0);
				writer.print(MessageUtil.textMessageToXml(textmsg));
			}else{
				TextMessage textmsg = new TextMessage();
				textmsg.setFromType("心跳");
				response.getWriter().println(MessageUtil.textMessageToXml(textmsg));
			}
			
		}else if(request.getParameter("action").equals("exit")){
			try {
				DataManager.exit(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			textMsg.setContent("wu xiao");
			textMsg.setFromType("服务器");
			out.print(MessageUtil.textMessageToXml(textMsg));
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		TextMessage textMsg = new TextMessage();
		if(request.getParameter("action").equals("comsumersend")){
			TextMessage text = new TextMessage();
			text.setContent(request.getParameter("content"));
			textMsg.setFromType("客户");
			responseArr.get(request.getParameter("touser")).msgArr.add(text);
		}else if (request.getParameter("action").equals("connect")) {
			textMsg.setContent("正确");
			textMsg.setFromType("服务器");
			ResponseModel model = new ResponseModel();
			responseArr.put(request.getParameter("id"), model);
			out.print(MessageUtil.textMessageToXml(textMsg));
		}else if(request.getParameter("action").equals("send")){
			String id = request.getParameter("id");
			TextMessage text = new TextMessage();
			text.setContent(request.getParameter("content"));
			text.setFromType("聊天");
			text.setFromid(id);
			text.setName(request.getParameter("name"));
			
			  Iterator it =  responseArr.entrySet().iterator();
			    while(it.hasNext()){          
                    Entry  obj = (Entry) it.next();
                    if(!id.equals(String.valueOf(obj.getKey()))){
                  	  if(responseArr.get(obj.getKey()).msgArr.size() > 20){
                  		  responseArr.get(obj.getKey()).msgArr.add(text);
                  		  responseArr.get(obj.getKey()).msgArr.remove(21);
                  	  }else{
                  		  responseArr.get(obj.getKey()).msgArr.add(text);
                  	  }
                    }
            }
		}else if(request.getParameter("action").equals("get")){
			ResponseModel model = responseArr.get(request.getParameter("id"));
			if(model != null && model.msgArr.size() != 0){
				PrintWriter writer = response.getWriter();
				TextMessage textmsg = new TextMessage();
				textmsg = model.msgArr.get(0);
				model.msgArr.remove(0);
				writer.print(MessageUtil.textMessageToXml(textmsg));
			}
		}else if(request.getParameter("action").equals("exit")){
			try {
				DataManager.exit(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			textMsg.setContent("wu xiao");
			textMsg.setFromType("服务器");
			out.print(MessageUtil.textMessageToXml(textMsg));
		}
	}

}