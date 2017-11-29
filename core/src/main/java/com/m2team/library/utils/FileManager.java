package com.m2team.library.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;


@SuppressWarnings("resource")
public class FileManager {

	
	//?????????????
	public static final String CACHE_DIRECTORY =  Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;  
	
	//??????
	public static final String CACHE_DIR ="";
	
	//????
	public static boolean CUT_FALG = false;
	
	//????
	public static boolean COPY_FALG = false;
	
	//????
	public static boolean DELETE_FALG = false;
	

	public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
            	if(file.isDirectory())  
            		deleteFilesByDirectory(file);
            		file.delete();
            }
            AppLogMessageMgr.i("FileManager-->>deleteFilesByDirectory", "This directory is not file, execute delete");
        }else{
        	AppLogMessageMgr.i("FileManager-->>deleteFilesByDirectory", "This directory is file, not execute delete");
        }
    }
	
    
    public static long getFileSize(File file) {
    	long size = 0;
    	if (file != null && file.exists() && file.isDirectory()) {
    		 File files[] = file.listFiles();
 	        for (int i = 0; i < files.length; i++) {
 	            if (files[i].isDirectory()){
 	                size = size + getFileSize(files[i]);
 	            }else {
 	                size = size + files[i].length();
 	            }
 	        }
    	}
    	AppLogMessageMgr.i("FileManager-->>getFileSize", "This file size: " + size);
        return size;
    }
    
    
    
    public static void saveFileToSdcard(Bitmap bitmap, String path){
		File file = new File(path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			FileOutputStream outputStream;
			byte[] array = null;
			try {
				outputStream = new FileOutputStream(file);
				if (null != bitmap) {
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
					array = os.toByteArray();
					os.close();
				}
				outputStream.write(array);
				outputStream.flush();
				outputStream.close();
				AppLogMessageMgr.i("FileManager-->>saveFileToSdcard-->>bitmap:", bitmap.toString());
				AppLogMessageMgr.i("FileManager-->>saveFileToSdcard-->>path:", path);
				AppLogMessageMgr.i("FileManager-->>saveFileToSdcard:", "?File???????????");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				AppLogMessageMgr.e("FileManager-->>saveFileToSdcard:", "?File???????????" + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
				AppLogMessageMgr.e("FileManager-->>saveFileToSdcard:", "?File???????????" + e1.getMessage());
			}
		}
	}
	
	public static boolean checkFileExists(String path, String timestamp){
		if(timestamp == null){
			if (new File(path).exists()) {
				return true;
			}
		}else{
			File file = new File(path);
			Date fileTime = new Date(file.lastModified());
			long fileTimestamp = fileTime.getTime();
			long newTimestamp = Long.valueOf(timestamp)*1000;
			long error = Long.valueOf(60000000);
			if (new File(path).exists() && fileTimestamp - error>= newTimestamp) {
				return true;
			}
		}
		return false;
	}
	
	 public static File getCacheFile(String imageUri) {
		File cacheFile = null;
		try {
			if (getSdCardIsEnable()) {
				File sdCardDir = Environment.getExternalStorageDirectory();
				String fileName = getFileName(imageUri);
				File dir = new File(sdCardDir.getCanonicalPath() + CACHE_DIR);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				cacheFile = new File(dir, fileName);
			}
		} catch (IOException e) {
			e.printStackTrace();
			AppLogMessageMgr.e("FileManager-->>getCacheFile:","??Sdcard????????????" + e.getMessage());
		}
			AppLogMessageMgr.i("FileManager-->>getCacheFile-->>imageUri:", imageUri);
			AppLogMessageMgr.i("FileManager-->>getCacheFile:", "??Sdcard????????????");
			return cacheFile;
	}
	 
	 
	
	public static String getFileName(String path) {
		int index = path.lastIndexOf("/");
		AppLogMessageMgr.i("FileManager-->>getFileName-->>path:", path);
		return path.substring(index + 1);
	}

	
	public static void writeFileToSdCard(String fileContent, String fileName) {
		//??SDCard??????????
		String sdCardFlag = Environment.getExternalStorageState();
		if (sdCardFlag != null && sdCardFlag.equals(Environment.MEDIA_MOUNTED)) {
			AppLogMessageMgr.i("FileManager-->>writeFileToSdCard-->>fileContent:",fileContent);
			AppLogMessageMgr.i("FileManager-->>writeFileToSdCard-->>fileName:",fileName);
			File file = new File(CACHE_DIRECTORY + fileName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			//??????????????
			if (file.exists()) {
				file.delete();
			}
			try {
				//????
				file.createNewFile();
				//?String??????input
				InputStream input = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));
				//????????????
				OutputStream output = new FileOutputStream(file);
				//???
				byte[] buffer = new byte[1024];
				int numread;
				while ((numread = input.read(buffer)) != -1) {
					output.write(buffer, 0, numread);
				}
				output.flush();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
				AppLogMessageMgr.e("FileManager-->>writeFileToSdCard:","?????SdCard?????????" + e.getMessage());
			}
		} else {
				AppLogMessageMgr.e("FileManager-->>writeFileToSdCard:","?SdCard???????????,?????");
		}
	}
	    
	
	public File writeInputStreamToSdCard(String path, String fileName, InputStream inputStream){
		File file = null;
		OutputStream output = null;
		try {
			AppLogMessageMgr.i("FileManager-->>writeInputStreamToSdCard-->>path:",path);
			AppLogMessageMgr.i("FileManager-->>writeInputStreamToSdCard-->>fileName:",fileName);
			AppLogMessageMgr.i("FileManager-->>writeInputStreamToSdCard-->>inputStream:",inputStream + "");
			//???????
			file = createSDFile( path + fileName);
			//????? 
			output = new FileOutputStream(file);
			//?????????
			byte buffer[] = new byte[4*1024];
			if((inputStream.read(buffer)) != -1){
				output.write(buffer);
			}
			output.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			AppLogMessageMgr.e("FileManager-->>writeInputStreamToSdCard:","?InputStream??SdCard????????" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			AppLogMessageMgr.e("FileManager-->>writeInputStreamToSdCard:","?InputStream??SdCard????????" + e.getMessage());
		}finally{
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
				AppLogMessageMgr.e("FileManager-->>writeInputStreamToSdCard:","?InputStream??SdCard????????" + e.getMessage());
			}
		}
		return file;
	}

	    
	public static String readFileFromSdCard(String cacheFileName) {
		FileReader fr = null;
		String fileContentStr = "";
		try {
			fr = new FileReader(CACHE_DIRECTORY + cacheFileName);
			// ????????????????
			BufferedReader br = new BufferedReader(fr);
			String str = "";
			while ((str = br.readLine()) != null) {
				fileContentStr += str;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			AppLogMessageMgr.e("FileManager-->>readFileFromSdCard:","?SdCard??????????" + e.getMessage());
		}
			AppLogMessageMgr.i("FileManager-->>readFileFromSdCard-->>cacheFileName:",cacheFileName);
			AppLogMessageMgr.i("FileManager-->>readFileFromSdCard:","?SdCard??????????");
			return fileContentStr;
	}

	    
	    
	public static String getRootFilePath(Context context, String path) {
		String file;
		//SdCard???
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			file = Environment.getExternalStorageDirectory().getPath() + path;
			AppLogMessageMgr.i("FileManager-->>getRootFilePath","SdCard???, ?SdCard???????");
			AppLogMessageMgr.i("FileManager-->>getRootFilePath-->>file", "????????" + file);
			File files = new File(file);
			if (files == null || !files.exists()) {
				files.mkdir();
			}
			return file;
		}
		//SdCard????
		else {
			file = Environment.getRootDirectory().getPath() + path;
			AppLogMessageMgr.i("FileManager-->>getRootFilePath","SdCard????, ??????????");
			AppLogMessageMgr.i("FileManager-->>getRootFilePath-->>file:","?????????" + file);
			File files = new File(file);
			if (files == null || !files.exists()) {
				files.mkdir();
			}
			return file;
		}
	}
    
	
	@SuppressWarnings("deprecation")
	public static long getSdCardSize() {
		String str = Environment.getExternalStorageDirectory().getPath();
		StatFs localStatFs = new StatFs(str);
		long blockSize = localStatFs.getBlockSize();
		return localStatFs.getAvailableBlocks() * blockSize;
	}
	
	
	
	public static String getSdCardAbsolutePath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;  
	}
	
	
	public static String getSdCardPath() {
		return Environment.getExternalStorageDirectory().getPath() +  File.separator;  
	}
	
	
	@SuppressWarnings("deprecation")
	public static long getSdCardEnableSize()  {  
		   //????SdCard????
	       if (getSdCardIsEnable())  {  
	            StatFs stat = new StatFs(getSdCardAbsolutePath());  
	            //???????????  
				long availableBlocks = (long) stat.getAvailableBlocks() - 4;  
	            //???????????byte?  
	            long freeBlocks = stat.getAvailableBlocks();  
	            return freeBlocks * availableBlocks;  
	        }  
	        	return 0;  
	}  
	
	@SuppressWarnings("deprecation")
	public long getSdCardAllSize() {  
	      if(getSdCardIsEnable()) {  
	          File path = Environment.getExternalStorageDirectory();  
	          StatFs stat = new StatFs(path.getPath());  
	          long blockSize = stat.getBlockSize();  
	          long totalBlocks = stat.getBlockCount();  
	          return totalBlocks * blockSize;  
	      } 
	      	  return 0;  
	} 

	
	public static String getRootAbsolutePath() {
       return Environment.getRootDirectory().getAbsolutePath() +  File.separator; 
    }  
    
    
    public static String getRootPath() {
        return Environment.getRootDirectory().getPath() +  File.separator; 
    }  
	
    
	public static String getDataPath(){
		return Environment.getDataDirectory().getPath() +  File.separator;  
	}
	
	
	
	public static String getDataAbsolutePath(){
		return Environment.getDataDirectory().getAbsolutePath() +  File.separator;  
	}
	
	
	public static boolean getSdCardIsEnable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	
	@SuppressWarnings("deprecation")
	public static long getMobileEnableRAM(){
		  StatFs stat = new StatFs(getDataPath());  
		  long blockSize = stat.getBlockSize();  
	      long availableBlocks = stat.getAvailableBlocks() - 4;;  
	      return availableBlocks * blockSize;  
	}
	
	
	
	@SuppressWarnings("deprecation")
	public static long getMobileAllRAM(){
	    StatFs stat = new StatFs(getDataPath());  
	    return stat.getBlockSize() * stat.getBlockCount();  
	}

	
	
	public static File createSDFile(String filename){
			File file = null;
			try {
				file = new File(getSdCardAbsolutePath() + filename);
				if(!file.exists()){
					file.createNewFile();
				}
			} catch (IOException e) {
				e.printStackTrace();
				AppLogMessageMgr.e("FileManager-->>createSDFile:","???????" + e.getMessage());
			}
				return file;
	}
	
	
	public static void createSDDirectory(String directory){
		File file = new File(getSdCardAbsolutePath() + directory);
		if(!file.exists()){
			file.mkdir();
		}
	}
	
	
	public static void createSDDirectorys(String directorys){
		File file = new File(getSdCardAbsolutePath() + directorys);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	
	public static boolean isFileExistSdCard(String filename){
		File file = new File(getSdCardPath() + filename);
		return file.exists();
	}
	
	
	public static void removeExpiredCache(String path) {
		if(checkFileExists(path)){
			File file = new File(path);  
			file.delete();  
	    }  
	}
	
	
	public static boolean checkFileExists(String path) {
		if (new File(path).exists()) {
			return true;
		}
			return false;
	}
	

	public static String readFileByLine(String filePath) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String line = "";
		String arrs = null;
		while ((line = br.readLine()) != null) {
			arrs = line;
		}
		br.close();
		isr.close();
		fis.close();
		return arrs;
	}

	
	public static void writeFile(String path, String value) throws IOException {
		FileOutputStream fos = new FileOutputStream(new File(path));
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(value);
		bw.close();
		osw.close();
		fos.close();
	}
	
	
	public InputStream getInputStreamByStringURL(String urlStr){
		InputStream inputStream = null;
		try {
			URL url = new URL(urlStr);
			URLConnection urlConnection = url.openConnection();
			inputStream = urlConnection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			AppLogMessageMgr.e("FileManager-->>getInputStreamByStringURL:","??????InputStream???" + e.getMessage());
		} catch (IOException e){
			e.printStackTrace();
			AppLogMessageMgr.e("FileManager-->>getInputStreamByStringURL:","??????InputStream???" + e.getMessage());
		}
		 	return inputStream;
	}
	

	
	public static boolean cutFile(File src, File desc, boolean boolCover, boolean boolCut){
		try {
			//1:???????
			if(src.isFile())    
			{
				if(!desc.isFile() || boolCover)
						//?????
						desc.createNewFile();
						//??????
						CUT_FALG = copyFile(src, desc);
						//???????
						if(boolCut){ 	src.delete();	}
			}
			//2????????
			else if(src.isDirectory()) {  
						desc.mkdirs();
						File[] list = src.listFiles();
						//???????????
						for(int i = 0; i < list.length; i++){
							String fileName = list[i].getAbsolutePath().substring(src.getAbsolutePath().length(), list[i].getAbsolutePath().length());
							File descFile = new File(desc.getAbsolutePath()+ fileName);
							cutFile(list[i],descFile, boolCover, boolCut);	
						}
						//???????
						if(boolCut)	{  	src.delete();	}
			}
		} catch (Exception e) {
				CUT_FALG = false;
				e.printStackTrace();
				AppLogMessageMgr.e("FileManager-->>cutFile:","???????????" + e.getMessage());
		}	
				return CUT_FALG;
	}
	
	
	public static boolean copyFile(File src, File desc){
		//???????(??,??)
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		//???????,?????
		FileInputStream srcInputStream  = null;
		FileOutputStream descOutputStream= null;
		//???????????
		int count = 0;
		//????????
		boolean boolCover = false;
		//?????????
		if(src.isFile()){
				try {
					  //???????????????
					  File[] list = desc.listFiles();
					  //???????     
					  String srcname = src.toString().substring(src.toString().lastIndexOf("\\")+1, src.toString().length()).trim();
					  if(null != list)
					  {
						  if(list.length > 0)
						  {
							    //?????????????????
								for(int i = 0; i < list.length; i++)
								{
									//??????????
									String descname = list[i].toString().substring(list[i].toString().lastIndexOf("\\")+1, list[i].toString().length()).trim();
									//??????????????????????1
									if(srcname.equals(descname)){
											count = count + 1;
											boolCover = true;
									}
									if(descname.indexOf("??") != -1 && descname.indexOf(srcname.substring(srcname.indexOf(")")+1, srcname.length())) != -1){
											count = count + 1;
									}
								}
						  }
					  }
					  //????????
					  if(boolCover)
					  {
						  if(count == 1)
						  {
							  if(desc.toString().indexOf(".") != -1)
							  {
								  //??????? ?? + ??????
								  descOutputStream = new FileOutputStream(desc.toString() + "\\?? " );
							  }else
							  {
								  //??????? ?? + ??????
								  descOutputStream = new FileOutputStream(desc.toString() + "\\?? " + srcname);
							  }
						  }else{
							  if(desc.toString().indexOf(".") != -1)
							  {
								  //??????? ??(???)+ ??????
								  descOutputStream = new FileOutputStream(desc.toString() + "\\?? ("+count+") ");
							  }else
							  {
								  //??????? ??(???)+ ??????
								  descOutputStream = new FileOutputStream(desc.toString() + "\\?? ("+count+") " + srcname);
							  }
						  }
					  }else{
						  	if(desc.toString().indexOf(".") != -1)
						  	{
						  		descOutputStream = new FileOutputStream(desc.toString() + "\\" );
						  	}else
						  	{
						  		descOutputStream = new FileOutputStream(desc.toString() + "\\" + srcname);
						  	}
					  }
					  byte[] buf = new byte[1];
					  srcInputStream = new FileInputStream(src);  
				      bis = new BufferedInputStream(srcInputStream);
				      bos = new BufferedOutputStream(descOutputStream);
				      while(bis.read(buf) != -1){
				        	  bos.write(buf);
				        	  bos.flush();
				       }
				      COPY_FALG = true;
				} catch (Exception e) {
					  COPY_FALG = false;
					  e.printStackTrace();
					  AppLogMessageMgr.e("FileManager-->>copyFile:","???????????" + e.getMessage());
				}finally{
						try {
							if(bis != null){ 	
								bis.close(); 	
							}
							if(bos != null){  	
								bos.close(); 	
							}	
						} catch (IOException e) {
							COPY_FALG = false;
							e.printStackTrace();
							AppLogMessageMgr.e("FileManager-->>copyFile:", "?????????????????" + e.getMessage());
						}
				}
			}else if(src.isDirectory()){
				//????
				desc.mkdir();
				File[] list = src.listFiles();
				//???????????
				for(int i = 0; i < list.length; i++){
					String fileName = list[i].getAbsolutePath().substring(src.getAbsolutePath().length(), list[i].getAbsolutePath().length());
					File descFile = new File(desc.getAbsolutePath()+ fileName);
					copyFile(list[i], descFile);	
				}
			}
				return COPY_FALG;
	}
	
	public static boolean renameFile(File file, String name){
		String path = file.getParent();
		if(!path.endsWith(File.separator)){
			path += File.separator;
		}
		AppLogMessageMgr.i("FileManager-->>renameFile:", "??????????");
		return file.renameTo(new File(path + name));
	}
	
	
	
	public static boolean deleteFile(File file){
		try {
			if(file.isFile())
			{
				file.delete();
				DELETE_FALG  = true;
			}
			else if(file.isDirectory())
			{
				File[] list = file.listFiles();
				for(int i=0;i<list.length;i++){
					deleteFile(list[i]);				
				}
				file.delete();
			}
		} catch (Exception e) {
			DELETE_FALG = false;
			e.printStackTrace();
			AppLogMessageMgr.i("FileManager-->>deleteFile", "?????????" + e.getMessage());
		}
			return DELETE_FALG;
	}
	
	
	
	
	
	
}
