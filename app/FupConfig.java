package com.mysterman.FupConvert;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class FupConfig {
	private FupConfig(){}
	
	//source path
	public final static String CURRENT_PATH = new File("").getAbsolutePath();
	public final static String PATH_LOG = CURRENT_PATH+"\\install\\support\\log.txt";
	public final static String PATH_FFMPEG = CURRENT_PATH+"\\install\\support\\ffmpeg.exe";
	public final static String PATH_UI = CURRENT_PATH+"\\install\\source\\ui-simple.png";
	public final static String PATH_READY = CURRENT_PATH+"\\install\\source\\wwpcv-ready.png";
	public final static String PATH_DOING = CURRENT_PATH+"\\install\\source\\wwpcv-doing.gif";
	public final static String PATH_CONFIG = CURRENT_PATH+"\\install\\source\\config.txt";
	
	//用于配置弹窗消息的map对象，来自于配置文件config.txt
	public final static Map<String,String> config = jsonParser(getFileContent(PATH_CONFIG));
	
	//settings
	public final static String NEWFILE_PREFIX = "fup_";
	public final static String[] videoTypes = {"mp4","mov","avi","flv","3gp","asx","asf","mpg","wmv"};
	public final static String[] imgTypes =  {"gif","jpg","jpeg","png"};
	
	//UI
	public static JTextField
		inputField = null,
		outputField = null,
		sizeField = null,
		qualityField = null,
		outTypeField = null;
	public static int
		screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width,
		screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height,
		frameWidth = 640,
		frameHeight = 420;
	public static JLabel
		statusIcon = null,
		statusMsg = null;
	
	public final static Color
		gray100 = new Color(100,100,100),
		darkgreen = new Color(45,135,50);
	
	//status
	public static boolean inConverting = false;
	
	/*-----------------
	 * common functions
	------------------- */
	//传入文件路径或文件扩展名，判断文件类型是否在数组中
	public final static boolean inArray(String path, String[] arr){
		String type = null;
		if(path.indexOf(".") == -1){
			type = path;
		}else{
			type = path.substring(path.lastIndexOf(".") + 1, path.length()).toLowerCase();
		}
		for(int i=0,len=arr.length; i<len; i++){
			if(arr[i].toLowerCase().equals(type)) return true;
		}
		return false;
	}
	//二维判断，如：判断批量选择中的图片是否包含视频
	public final static boolean inArray(String[] arr1, String[] arr2){
		for(int i=0,len=arr1.length; i<len; i++){
			if(FupConfig.inArray(arr1[i], arr2)) return true;
		}
		return false;
	}
	//传入文件路径，返回文件名。
	public final static String getFileName(String path){
		return path.substring(path.lastIndexOf("\\") + 1, path.length()).toLowerCase();
	}
	public final static String getFileName(String path, int type){
		String str = path.substring(path.lastIndexOf("\\") + 1, path.length()).toLowerCase();
		return str.split("\\.")[type];
	}
	//数组拼字符串
	public final static String arrayToStr(String[] arr, String sign){
		String str = null;
		for(int i=0, len=arr.length; i<len; i++){
			if(i>0) str += sign;
			str += arr[i];
		}
		return str;
	}
	//传入文件路径，把它读取为字符串，并返回。
	public final static String getFileContent(String path){
		File file = new File(path);
		StringBuilder result = new StringBuilder();
		try{
			FileReader fr = new FileReader(file);
	        try{
	            BufferedReader br = new BufferedReader(fr);
	            String s = null;
	            while((s = br.readLine())!=null){
	                result.append(s);
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
		}catch(IOException io){
			io.printStackTrace();
		}
		return result.toString();
	}
	//把json字符串转成map
	public final static Map<String, String> jsonParser(String str){
		Map<String,String> map = new HashMap<String,String>();
		str = str.replaceAll("[{\"}]", "");
		String[] arr = str.split(",");
		for(int i=0,len=arr.length; i<len; i++){
			String[] tmparr = arr[i].split(":");
			map.put(tmparr[0], tmparr[1].replaceAll("<p>", "\n"));
		}
		return map;
	}
	
}
