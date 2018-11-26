package com.assistant.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

public class FileUtils {

    private static int FILESIZE = 4 * 1024;   
    public static String getSDPATH(){  
        return Environment.getExternalStorageDirectory() + "/";  
    }  
    /**  
     * ��SD���ϴ����ļ�  
     * @param fileName  
     * @return  
     * @throws IOException  
     */  
    public static  File createSDFile(String fileName) throws IOException{  
        File file = new File(getSDPATH() + fileName);  
        file.createNewFile();  
        return file;  
    }  
    /**  
     * ��SD���ϴ���Ŀ¼  
     * @param dirName  
     * @return  
     */  
    public static  File createSDDir(String dirName){  
        File dir = new File(getSDPATH() + dirName);  
        if(!dir.exists())
        	dir.mkdirs();  
        return dir;  
    }  
    public static  void deleteSDDir(String dirName){  
        File dir = new File(getSDPATH() + dirName);  

		if (dir != null) {
			for (File file : dir.listFiles()) {
				file.delete();
			}
		} 
    }  
      
    /**  
     * �ж�SD���ϵ��ļ����Ƿ����  
     * @param fileName  
     * @return  
     */  
    public static boolean isFileExist(String fileName){  
        File file = new File(getSDPATH() + "zh/" + fileName);  
        return file.exists();  
    }  
      
    public static String save(byte[] data){  
        File dir = new File(Environment.getExternalStorageDirectory() + "/noproview/");
        if(!dir.exists())
        	dir.mkdirs();  
        FileOutputStream fos = null;
        Bitmap bitmap = null;
        String path = Environment.getExternalStorageDirectory() + "/noproview/"+System.currentTimeMillis()+".jpg"; 
        try {  
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  
                
                File file = new File(path);  
                if(!file.exists()){  
                    file.createNewFile();  
                }  
                BitmapFactory.Options options=new BitmapFactory.Options();  
    		    options.inJustDecodeBounds = false;  
    		    options.inSampleSize = 4; 
    		    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
    		    Matrix matrix = new Matrix();  
    		    matrix.preRotate(-90);
    		    bitmap = Bitmap.createBitmap(bitmap ,0,0, bitmap .getWidth(), bitmap .getHeight(),matrix,true);  
    		    fos = new FileOutputStream(file);   
    		    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (fos != null) {
					fos.flush();
					fos.close();
					fos = null;
				}
				if (!bitmap.isRecycled()) {
					bitmap.recycle(); 
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return path;  
    }  
  
    
    /**  
     * ��һ��InputStream��������д�뵽SD����  
     * @param path  
     * @param fileName  
     * @param input  
     * @return  
     */  
    public static File write2SDFromInput(String path,String fileName,InputStream input){  
        File file = null;  
        OutputStream output = null;  
        try {  
            createSDDir(path);
            if(input == null)
            	return null;
            file = createSDFile(path +"/"+ fileName);
            output = new FileOutputStream(file);  
            byte[] buffer = new byte[FILESIZE];
            int len ;
            while((len = input.read(buffer)) != -1){  
                output.write(buffer,0,len);  
            }  
            output.flush();  
        }   
        catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            try {  
                output.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return file;  
    }  
    
    public static String getDataAndType(Properties prop,String filePath){
    	if(prop == null)
    		throw new RuntimeException("û�ж��������ļ�");
    	String fileExtName = getFileExtName(filePath);
    	return prop.getProperty(fileExtName);
    }
    public static String getFileExt(String fileName){
    	return fileName.substring(fileName.lastIndexOf("."));
    }
    public static String getFileExtName(String fileName){
    	return fileName.substring(fileName.lastIndexOf("/")+1);
    }
    
    public static Bitmap convertToBitmap(String path, int w, int h) {
    	if(w == 0 || h == 0){
    		w = 150;
    		h = 200;
    	}
    	
        BitmapFactory.Options opts = new BitmapFactory.Options();
    // 设置为ture只获取图片大小
      opts.inJustDecodeBounds = true;
       opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
       // 返回为空
      BitmapFactory.decodeFile(path, opts);
     int width = opts.outWidth;
      int height = opts.outHeight;
      float scaleWidth = 0.f, scaleHeight = 0.f;
      if (width > w || height > h) {
         // 缩放
         scaleWidth = ((float) width) / w;
         scaleHeight = ((float) height) / h;
     }
      opts.inJustDecodeBounds = false;
      float scale = Math.max(scaleWidth, scaleHeight);
       opts.inSampleSize = (int)scale;
       Bitmap bmp = BitmapFactory.decodeFile(path, opts);
       if(bmp == null)
    	   return null;
      return Bitmap.createScaledBitmap(bmp , w, h, true);
 }
    public static String getDocFileName(Map<String,String> detail){
    	return detail.get("filesensorid")+"_"+detail.get("censorstate")+".doc";
    }
    public static String getAttFileName(Map<String,String> detail){
    	String fileExt = FileUtils.getFileExt(detail.get("annexid"));
    	return detail.get("filesensorid")+"_"+detail.get("censorstate")+"_att"+fileExt;
    }
    public void deleteFile(String filename) {
		System.out.println(filename);
		File file = new File(filename);
		if (file.exists()) {
			file.delete();
		}
	}
}