package com.manager.server;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.manager.model.TextMessage;
import com.manager.util.MessageUtil;
  
public class ManagerServer implements Runnable  
{  HashMap<Integer,Socket> clientArr;
	public ManagerServer(HashMap<Integer,Socket> clientArr){
		this.clientArr = clientArr;
	}
    public void run()  
    {  
        try  
        {  
            //����ServerSocket  
            ServerSocket serverSocket = new ServerSocket(80);  
            
            while (true)  
            {  
                //���ܿͻ�������  
                Socket client = serverSocket.accept(); 
                System.out.println("accept");  
                try  
                {  
                    //���տͻ�����Ϣ  
                	Map<String,String> getStream = MessageUtil.parseXml(client.getInputStream());
//                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));  
//                    String str = in.readLine();  
//                    System.out.println("read:" + str);    
                    //�������������Ϣ  
                    PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(client.getOutputStream())),true);        
                    TextMessage textMsg = new TextMessage();
            		textMsg.setContent(URLEncoder.encode("server get = "+getStream.get("Content")));
            		out.print(MessageUtil.textMessageToXml(textMsg));  
                    //�ر���  
                    out.close();  
//                    in.close();  
                }  
                catch (Exception e)  
                {  
                    System.out.println(e.getMessage());  
                    e.printStackTrace();  
                }  
                finally  
                {  
                    //�ر�  
                    client.close();  
                    System.out.println("close");  
                }  
            }  
        }  
        catch (Exception e)  
        {  
            System.out.println(e.getMessage());  
        }  
    }  
}  
