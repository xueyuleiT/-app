package com.assistant.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.assistant.utils.CurrentTime;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;

public class SmsContent {
	private Activity activity;//这里有个activity对象，不知道为啥以前好像不要，现在就要了。自己试试吧。 
    private Uri uri; 
 
    public SmsContent(Activity activity, Uri uri) { 
        this.activity = activity; 
        this.uri = uri; 
    } 
 
    public List<SmsInfo> getOnePersonInfo(String phone,String name){
    	 List<SmsInfo> infos = new ArrayList<SmsInfo>(); 
    	 SimpleDateFormat dateForamt = new SimpleDateFormat("yyyy-MM-dd");
         String[] projection = new String[] {"person", 
                 "body", "date", "type" }; 
         Cursor cusor = activity.managedQuery(uri, projection, "address=?", new String[]{phone}, "date asc");
         int smsbodyColumn = cusor.getColumnIndex("body"); 
         int dateColumn = cusor.getColumnIndex("date"); 
         int typeColumn = cusor.getColumnIndex("type"); 
         
         if (cusor != null) { 
             while (cusor.moveToNext()) { 
                 SmsInfo smsinfo = new SmsInfo(); 
                 smsinfo.setName(name); 
                 smsinfo.setDate(dateForamt.format(Long.valueOf(cusor.getString(dateColumn)).longValue())); 
                 smsinfo.setPhoneNumber(phone); 
                 smsinfo.setSmsbody(cusor.getString(smsbodyColumn)); 
                 smsinfo.setType(cusor.getString(typeColumn)); 
                 infos.add(smsinfo); 
             } 
             try  
             {  
                 //4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)  
                 if(Integer.parseInt(Build.VERSION.SDK) < 14)  
                 {  
                 	cusor.close();  
                 }  
             }catch(Exception e)  
             {  
             } 
         } 
         return infos; 
         
    }
    
    /**
     * Role:获取短信的各种信息 <BR>
     * 
     */ 
    public List<SmsInfo> getSmsInfo() { 
    	 List<SmsInfo> infos = new ArrayList<SmsInfo>(); 
    	 SimpleDateFormat dateForamt = new SimpleDateFormat("yyyy-MM-dd");
        String[] projection = new String[] { "_id", "address", "person", 
                "body", "date", "type" }; 
        @SuppressWarnings("deprecation")
		Cursor cusor = activity.managedQuery(uri, projection,  "1=1) group by (address", null, "date desc"); 
        int phoneNumberColumn = cusor.getColumnIndex("address"); 
        int smsbodyColumn = cusor.getColumnIndex("body"); 
        int dateColumn = cusor.getColumnIndex("date"); 
        int typeColumn = cusor.getColumnIndex("type"); 
        if (cusor != null) { 
            while (cusor.moveToNext()) { 
                SmsInfo smsinfo = new SmsInfo(); 
                smsinfo.setDate(dateForamt.format(Long.valueOf(cusor.getString(dateColumn)).longValue())); 
                smsinfo.setPhoneNumber(cusor.getString(phoneNumberColumn)); 
                smsinfo.setSmsbody(cusor.getString(smsbodyColumn)); 
                smsinfo.setType(cusor.getString(typeColumn)); 
                try {
					smsinfo.setName(testContactNameByNumber(smsinfo.getPhoneNumber()));
				} catch (Exception e) {
				} 
                
                infos.add(smsinfo); 
            } 
            try  
            {  
                //4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)  
                if(Integer.parseInt(Build.VERSION.SDK) < 14)  
                {  
                	cusor.close();  
                }  
            }catch(Exception e)  
            {  
            } 
            
            
            
        } 
        return infos; 
    } 
    
    //查询指定电话的联系人姓名，邮箱
    public String testContactNameByNumber(String number) throws Exception {
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + number);
        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"display_name"}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
        	number = cursor.getString(0);
        }
        if(cursor != null){
        	 cursor.close();
        	 return number;
        }
        
        return null;
    }
}
