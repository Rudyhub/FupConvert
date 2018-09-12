package com.mysterman.FupConvert;

import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class FupffmpegThread extends Thread{
	InputStream instream = null;
	String[] inputPath, sizes, outTypes;
	String outputPath, quality;
	int findex=0, sindex=0;
	Fupffmpeg fupffmpeg = null;
    public FupffmpegThread(InputStream instream, String[] inputPath, String outputPath, String[] sizes, String quality, String[] outTypes, int findex, int sindex, Fupffmpeg fupffmpeg){
        this.instream = instream;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.sizes = sizes;
        this.quality = quality;
        this.outTypes = outTypes;
        this.findex = findex;
        this.sindex = sindex;
        this.fupffmpeg = fupffmpeg;
    } 
    public void run(){
        try{
            while(this != null){
                int _ch = this.instream.read();
                if(_ch != -1){
                	//正在处理
                	FupConfig.statusIcon.setIcon(new ImageIcon(FupConfig.PATH_DOING));
                	//System.out.print((char)_ch);
                }else{
                	//单个处理完成
                	FupConfig.statusIcon.setIcon(new ImageIcon(FupConfig.PATH_READY));
                	break;
                }
            }
            //进行回调
            if(this.findex < this.inputPath.length-1){
            	this.sindex++;
        		if(this.sindex == this.sizes.length){
        			this.findex++;
        			this.sindex = 0;
        			if(this.findex == this.inputPath.length) return;
        		}
        		this.fupffmpeg.process(this.inputPath, this.outputPath, this.quality, this.sizes, this.outTypes, this.findex, this.sindex);
        	}else{
        		//全部处理完成
        		FupConfig.inConverting = false;
        		FupConfig.statusMsg.setForeground(FupConfig.gray100);
        		FupConfig.statusMsg.setText(FupConfig.config.get("complete")+"...");
        		JOptionPane.showMessageDialog(null, FupConfig.config.get("complete"), FupConfig.config.get("dialogTitle"), JOptionPane.PLAIN_MESSAGE);
        	}
        }catch (Exception e) {
            e.printStackTrace();
        } 
    }
}

