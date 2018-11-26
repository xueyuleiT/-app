package com.manager.sevlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.manager.model.ConsumerAccept;
import com.manager.model.TextMessage;
import com.manager.util.DataManager;
import com.manager.util.MessageUtil;
import com.sina.sae.storage.SaeStorage;
import com.sina.sae.util.SaeUserInfo;
import com.manager.util.CurrentTime;

public class UploadFile extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
//		TextMessage textMsg = new TextMessage();
//		
//		PrintWriter out = response.getWriter();
//		String toUser = request.getParameter("touser");
//		String realPath = SaeUserInfo.getSaeTmpPath() + "/";
//		if(request.getParameter("action").equals("upload")){
//		if(CoreServlet.responseArr.get(toUser) != null){
//		// ʹ��SaeUserInfo�õ��������д��·��
//		try {
//			// ʹ��common-upload�ϴ��ļ������·����
//			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//			if (!isMultipart)
//				return;
//			DiskFileItemFactory factory = new DiskFileItemFactory();
//			ServletFileUpload upload = new ServletFileUpload(factory);
//			upload.setFileSizeMax(1024 * 1024);
//			String file = "";
//			List<FileItem> items = null;
//			items = upload.parseRequest(request);
//			for (FileItem item : items) {
//				if (!item.isFormField()) {
//					File fullFile = new File(item.getName());
//					file = fullFile.getName();
//					File uploadFile = new File(realPath, file);
//					item.write(uploadFile);
//					// �ϴ���Ϻ� ʹ��SaeStorage��storage����д
//					SaeStorage ss = new SaeStorage();
//					// ʹ��upload�����ϴ�����Ϊpicture��
//					ss.upload("picture", realPath + file, "image/"+file);
//					
//				}
//			}
//			ConsumerAccept con = new ConsumerAccept();
//			con.setSaleid(request.getParameter("touser"));
//			con.setImgpath(file);
//			con.setDatatime(CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
//			
//			TextMessage textMessage = new TextMessage();
//			textMessage.setServerId(DataManager.insertConsumer(con));
//			textMessage.setFromType(MessageUtil.MESSAGE_TYPE_IMAGE);
//			textMessage.setContent(file);
//			CoreServlet.responseArr.get(toUser).msgArr.add(textMessage);
//			textMsg.setContent("�ϴ��ɹ�");
//		} catch (Exception e) {
//			textMsg.setContent("�ϴ�ʧ��");
//		} finally {
//			out.print(MessageUtil.textMessageToXml(textMsg));
//			out.flush();
//			out.close();
//		}
//		}else{
//			textMsg.setContent("�û�������");
//			out.print(MessageUtil.textMessageToXml(textMsg));
//			out.flush();
//			out.close();
//		}
//		}else if(request.getParameter("action").equals("delete")){
//			SaeStorage ss = new SaeStorage();
//			ss.delete("picture", "image/"+request.getParameter("filename"));
//		}
	}
}
