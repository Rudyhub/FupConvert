package com.mysterman.FupConvert;

import javax.swing.UIManager;
public class FupConvert{
	public static void main(String[] args){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		new FupLayout();
	}
}
