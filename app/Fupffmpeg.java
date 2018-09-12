package com.mysterman.FupConvert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Fupffmpeg {
    public Fupffmpeg(String[] inputPath, String outputPath, String[] sizes, String[] outTypes){
    	//检查输出路径
    	File outdir = new File(outputPath);
    	if(!outdir.isDirectory()){
    		outputPath = inputPath[0].substring(0, inputPath[0].lastIndexOf("\\"));
    	}
    	outputPath = outputPath+"\\";
    	//查检必须ffpmeg路径
    	File ffmpegFile = new File(FupConfig.PATH_FFMPEG);
    	if(!ffmpegFile.isFile()){
    		JOptionPane.showMessageDialog(null, FupConfig.config.get("noFfmpeg"), FupConfig.config.get("dialogTitle"), JOptionPane.ERROR_MESSAGE); 
    		return;
    	}
    	//质量
    	String quality = FupConfig.qualityField.getText().replaceAll(" ", "");
    	quality = quality.equals("") ? "28" : quality;

        //打开执行转换进程
        if(sizes != null && sizes.length > 0){
        	this.process(inputPath, outputPath, quality, sizes, outTypes, 0, 0);
        }else{
        	JOptionPane.showMessageDialog(null, FupConfig.config.get("emptyErr"), FupConfig.config.get("dialogTitle"), JOptionPane.ERROR_MESSAGE); 
        }
    }
    public void process(String[] inputPath, String outputPath, String quality, String[] sizes, String[] outTypes, int findex, int sindex) {
    	//用来给输出的文件命名
    	String newName =  "";
    	
    	List<String> command = new ArrayList<String>();
    	
    	if(FupConfig.inArray(inputPath[findex], FupConfig.imgTypes)){
    		//图片
    		newName = FupConfig.NEWFILE_PREFIX + sizes[sindex] + "_"  + FupConfig.getFileName(inputPath[findex],0) + "." + (outTypes[1] == null ? FupConfig.getFileName(inputPath[findex],1) : outTypes[1]);
    		
	        command.add(FupConfig.PATH_FFMPEG);
	        command.add("-threads");
	        command.add("2");
	        command.add("-i");
	        command.add(inputPath[findex]);
	        command.add("-crf");
	        command.add(quality);
	        command.add("-y");
	        command.add("-vf");
	        command.add("scale="+sizes[sindex]+":-2");
	        command.add(outputPath + newName);
    	}else if(FupConfig.inArray(inputPath[findex], FupConfig.videoTypes)){
    		//视频
    		newName = FupConfig.NEWFILE_PREFIX + sizes[sindex] + "_" + FupConfig.getFileName(inputPath[findex],0) + "." + outTypes[0];
			
	        command.add(FupConfig.PATH_FFMPEG);
	        command.add("-threads");
	        command.add("2");
	        command.add("-i");
	        command.add(inputPath[findex]);
	        command.add("-vcodec");
	        command.add("libx264");
	        command.add("-preset");
	        command.add("slow");
	        command.add("-crf");
	        command.add(quality);
	        command.add("-y");
	        command.add("-vf");
	        command.add("scale="+sizes[sindex]+":-2");
	        command.add(outputPath + newName);
    	}
    	
    	try {
            Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();
            new FupffmpegThread(videoProcess.getInputStream(), inputPath, outputPath, sizes, quality, outTypes, findex, sindex, this).start();
            FupConfig.statusMsg.setText(FupConfig.config.get("converting").replace("$0",FupConfig.getFileName(inputPath[findex],0)+"."+FupConfig.getFileName(inputPath[findex],1))+"...");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, FupConfig.config.get("fail"), FupConfig.config.get("dialogTitle"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
