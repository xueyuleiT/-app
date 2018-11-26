package com.assistant.utils;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.assistant.model.TextMessage;


public class MyClientWriter {
     
	private TextMessage textMsg = new TextMessage();
	
     public boolean writeStream(final OutputStream dos,final String text){
    	 
    	 new Thread(new Runnable() {
			
			@Override
			public void run() {
				textMsg.setContent(text);
				 PrintWriter out = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(dos)), true);
				 out.println(MessageUtil.textMessageToXml(textMsg));
			}
		}).start();
    	 return true;
     }
     
 }
