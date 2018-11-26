package com.example.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Bitmap;

import com.example.model.ConsumerModel;

public class NetworkData {
	private int length;
	private static final int TIME_OUT = 10 * 1000; 
	private static final String CHARSET = "utf-8";
	private static MyClientRead read = new MyClientRead();
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	public static String getMyModel(String url){
		InputStream is = null;
		String result = null;
		StringBuilder sb = new StringBuilder();
		try {
			System.out.println("url = "+url);
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			
		}catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
    } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
    } catch (ConnectTimeoutException cte) {
            System.out.println("ConnectTimeoutException");
            cte.printStackTrace();
            return null;
    } catch (SocketTimeoutException ste) {
            System.out.println("SocketTimeoutException");
            ste.printStackTrace();
            return null;
    } catch (IOException e) {
            e.printStackTrace();
            return null;
    }

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"));
			String line = null;
			if(reader != null){
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			}else
				return null;
		} catch (Exception e) {
			System.out.println("result" + e.getMessage());
			return null;
		}

		return result;
	}
	
	
//	public static boolean sendContent(String urlStr,String msg){
//		URL url;
//		try {
//			url = new URL(urlStr+"&content="+URLEncoder.encode(msg, "UTF-8"));
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setConnectTimeout(5 * 1000);
//			conn.setRequestMethod("POST");
//			System.out.println("发送消息  = "+msg);
//			if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
//				return true;
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (ProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
	
//	public static Map<String,String> getContent(String urlStr){
//		Map<String,String> getStream = null;
//		try {
//			HttpClient httpclient = new DefaultHttpClient();
//			HttpPost httppost = new HttpPost(urlStr);
//			HttpResponse response = httpclient.execute(httppost);
//			HttpEntity entity = response.getEntity();
//			getStream = read.readStream(entity.getContent());
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (ProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return getStream;
//		
//	}
	
//	public static boolean heat(String urlStr){
//		URL url;
//		try {
//			url = new URL(urlStr);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setConnectTimeout(5 * 1000);
//			conn.setRequestMethod("POST");
//			if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
//				return true;
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (ProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
	
//	 public static String uploadFile(File file, String RequestURL,String keyString) {
//	        String result = null;
//	        String BOUNDARY = UUID.randomUUID().toString(); // �߽��ʶ ������
//	        String PREFIX = "--", LINE_END = "\r\n";
//	        String CONTENT_TYPE = "multipart/form-data"; // ��������
//
//
//	        try {
//	            URL url = new URL(RequestURL);
//	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//	            conn.setReadTimeout(TIME_OUT);
//	            conn.setConnectTimeout(TIME_OUT);
//	            conn.setDoInput(true); // ����������
//	            conn.setDoOutput(true); // ���������
//	            conn.setUseCaches(false); // ������ʹ�û���
//	            conn.setRequestMethod("POST"); // ����ʽ
//	            conn.setRequestProperty("Charset", CHARSET); // ���ñ���
//	            conn.setRequestProperty("connection", "keep-alive");
//	            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//
//
//	            if (file != null) {
//	                /**
//	                 * ���ļ���Ϊ�գ����ļ���װ�����ϴ�
//	                 */
//	                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
//	                StringBuffer sb = new StringBuffer();
//	                sb.append(PREFIX);
//	                sb.append(BOUNDARY);
//	                sb.append(LINE_END);
//
//	                sb.append("Content-Disposition: form-data; name=\""+keyString+"\"; filename=\""
//	                        + file.getName() + "\"" + LINE_END);
//	                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
//	                sb.append(LINE_END);
//	                dos.write(sb.toString().getBytes());
//	                InputStream is = new FileInputStream(file);
//	                
//	                byte[] bytes = new byte[1024];
//	                int len = 0;
//	                while ((len = is.read(bytes)) != -1) {
//	                    dos.write(bytes, 0, len);
//	                }
//	                is.close();
//	                dos.write(LINE_END.getBytes());
//	                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
//	                dos.write(end_data);
//	                dos.flush();
//	                /**
//	                 * 
//	                 */
//	                int res = conn.getResponseCode();
//	                System.out.println("res = "+res);
//	                InputStream input = conn.getInputStream();
//	                Map<String,String> getStream = read.readStream(input);
//	                System.out.println("result = "+getStream.get("Content"));
//	            }
//	        } catch (MalformedURLException e) {
//	            e.printStackTrace();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	        return result;
//	    }
//	 private static HttpGet getHttpGet(String url, Map<String, String> params,  
//	            String encode) {  
//	        StringBuffer buf = new StringBuffer(url);  
//	        if (params != null) {  
//	            // 地址增加?或者&  
//	            String flag = (url.indexOf('?') == -1) ? "?" : "&";  
//	            // 添加参数  
//	            for (String name : params.keySet()) {  
//	                buf.append(flag);  
//	                buf.append(name);  
//	                buf.append("=");  
//	                try {  
//	                    String param = params.get(name);  
//	                    if (param == null) {  
//	                        param = "";  
//	                    }  
//	                    buf.append(URLEncoder.encode(param, encode));  
//	                } catch (UnsupportedEncodingException e) {  
//	                    System.out.println("URLEncoder Error,encode=" + encode + ",param="  
//	                            + params.get(name));  
//	                }  
//	                flag = "&";  
//	            }  
//	        }  
//	        HttpGet httpGet = new HttpGet(buf.toString());  
//	        return httpGet;  
//	    }  
//	 
	 
//	 public static void inputstreamtofile(InputStream ins,File file) {
//		  try {
//		   OutputStream os = new FileOutputStream(file);
//		   int bytesRead = 0;
//		   byte[] buffer = new byte[8192];
//		   while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
//		    os.write(buffer, 0, bytesRead);
//		   }
//		   os.close();
//		   ins.close();
//		  } catch (Exception e) {
//		   e.printStackTrace();
//		  }
//		 }
	 
	
//	public static boolean connect(String urlStr){
//		URL url;
//		try {
//			url = new URL(urlStr);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setConnectTimeout(5 * 1000);
//			conn.setRequestMethod("POST");
//			if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
//				return true;
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (ProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return false;
//		
//	}
	
	public static TextMessage postFastConsumerDataUrl(String url,ConsumerModel con) throws ClientProtocolException, IOException{
		try{
		List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>(); 
		formParams.add(new BasicNameValuePair("customer_name", con.getCustomer_name())); 
		formParams.add(new BasicNameValuePair("customer_phone", con.getCustomer_phone())); 
		formParams.add(new BasicNameValuePair("datatime", con.getDatatime())); 
		formParams.add(new BasicNameValuePair("consultantname", con.getConsultantname())); 
		formParams.add(new BasicNameValuePair("consultantphone", con.getConsultantphone())); 
		formParams.add(new BasicNameValuePair("xingbie", con.getXingbie())); 
		formParams.add(new BasicNameValuePair("quyu", con.getQuyu())); 
		formParams.add(new BasicNameValuePair("beizhu", con.getBeizhu()));
		HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8"); 
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(entity);
		HttpResponse response = httpclient.execute(httppost);
		if(response.getStatusLine().getStatusCode() == 200){
			HttpEntity getEntity = response.getEntity();
			TextMessage msg = new TextMessage();
			Map<String,String> getStream = read.readStream(getEntity.getContent());
			if(getStream == null) 
				return null;
			System.out.println("得到返回 = "+getStream.get("Content"));
			msg.setContent(getStream.get("Content"));
			return msg;
		}else 
			return null;
		
	}catch (ClientProtocolException e) {
        e.printStackTrace();
        return null;
} catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        return null;
} catch (ConnectTimeoutException cte) {
        System.out.println("ConnectTimeoutException");
        cte.printStackTrace();
        return null;
} catch (SocketTimeoutException ste) {
        System.out.println("SocketTimeoutException");
        ste.printStackTrace();
        return null;
} catch (IOException e) {
        e.printStackTrace();
        return null;
}
	}
	
	public static TextMessage postConsumerDataUrl(String url,ConsumerModel con) throws ClientProtocolException, IOException{
	try{
		List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>(); 
		formParams.add(new BasicNameValuePair("customer_name", con.getCustomer_name())); 
		formParams.add(new BasicNameValuePair("customer_phone", con.getCustomer_phone())); 
		formParams.add(new BasicNameValuePair("datatime", con.getDatatime())); 
		formParams.add(new BasicNameValuePair("consultantname", con.getConsultantname())); 
		formParams.add(new BasicNameValuePair("consultantphone", con.getConsultantphone())); 
		formParams.add(new BasicNameValuePair("kehuleixing", con.getKehuleixing())); 
		formParams.add(new BasicNameValuePair("xingbie", con.getXingbie())); 
		formParams.add(new BasicNameValuePair("yisuan", con.getYisuan())); 
		formParams.add(new BasicNameValuePair("yixiang", con.getYixiang())); 
		formParams.add(new BasicNameValuePair("yixiangyetai", con.getYixiangyetai())); 
		formParams.add(new BasicNameValuePair("kehuzu", con.getKehuzu())); 
		formParams.add(new BasicNameValuePair("yixiangmianji", con.getYixiangmianji())); 
		formParams.add(new BasicNameValuePair("zijinshili", con.getZijinshili())); 
		formParams.add(new BasicNameValuePair("yixianghuxing", con.getYixianghuxing())); 
		formParams.add(new BasicNameValuePair("kehushuxing", con.getKehushuxing())); 
		formParams.add(new BasicNameValuePair("renzhiqudao", con.getRenzhiqudao()));
		formParams.add(new BasicNameValuePair("quyu", con.getQuyu()));
		formParams.add(new BasicNameValuePair("shenfenzheng", con.getShenfenzheng()));
		formParams.add(new BasicNameValuePair("chengjiaojine", con.getChengjiaojine()));
		formParams.add(new BasicNameValuePair("laoyezhuphone", con.getLaoyezhuphone()));
		formParams.add(new BasicNameValuePair("beizhu", con.getBeizhu()));
		formParams.add(new BasicNameValuePair("louhao", con.getLouhao()));
		formParams.add(new BasicNameValuePair("dizhi", con.getDizhi()));
		formParams.add(new BasicNameValuePair("temp1", con.getTemp1()));
		formParams.add(new BasicNameValuePair("temp2", con.getTemp2()));
		formParams.add(new BasicNameValuePair("temp3", con.getTemp3()));
		formParams.add(new BasicNameValuePair("temp4", con.getTemp4()));
		formParams.add(new BasicNameValuePair("temp5", con.getTemp5()));
		formParams.add(new BasicNameValuePair("temp6", con.getTemp6()));
		formParams.add(new BasicNameValuePair("temp7", con.getTemp7()));
		formParams.add(new BasicNameValuePair("temp8", con.getTemp8()));
		formParams.add(new BasicNameValuePair("temp9", con.getTemp9()));
		formParams.add(new BasicNameValuePair("temp10", con.getTemp10()));
		
		HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8"); 
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(entity);
		HttpResponse response = httpclient.execute(httppost);
		if(response.getStatusLine().getStatusCode() == 200){
			System.out.println("连接成功");
			HttpEntity getEntity = response.getEntity();
			TextMessage msg = new TextMessage();
			Map<String,String> getStream = read.readStream(getEntity.getContent());
			if(getStream == null) 
				return null;
			System.out.println("得到返回 = "+getStream.get("Content"));
			msg.setContent(getStream.get("Content"));
			return msg;
		}else 
			return null;
	}catch (ClientProtocolException e) {
        e.printStackTrace();
        return null;
} catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        return null;
} catch (ConnectTimeoutException cte) {
        cte.printStackTrace();
        return null;
} catch (SocketTimeoutException ste) {
        ste.printStackTrace();
        return null;
} catch (IOException e) {
        e.printStackTrace();
        return null;
}
	}
	
	public static TextMessage posturl(String url) throws ClientProtocolException, IOException {
		TextMessage msg = new TextMessage();
		try{
		Map<String,String> getStream;
			System.out.println("url = "+url);
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			getStream = read.readStream(entity.getContent());
			if(getStream == null) 
				return null;
		msg.setFromType(getStream.get("fromType"));
		msg.setContent(getStream.get("Content"));
		msg.setFromid(getStream.get("fromid"));
		msg.setName(getStream.get("name"));
		System.out.println("content = "+msg.getContent());
		
		}catch (ClientProtocolException e) {
	            e.printStackTrace();
	            return null;
	    } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	            return null;
	    } catch (ConnectTimeoutException cte) {
	            System.out.println("ConnectTimeoutException");
	            cte.printStackTrace();
	            return null;
	    } catch (SocketTimeoutException ste) {
	            System.out.println("SocketTimeoutException");
	            ste.printStackTrace();
	            return null;
	    } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	    }
		return msg;
	}

}
