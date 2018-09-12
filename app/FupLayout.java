package com.mysterman.FupConvert;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;

public class FupLayout extends JFrame implements  ActionListener,KeyListener{
	private static final long serialVersionUID = 1L;
	public FupLayout(){
		//���ñ���
		setTitle(FupConfig.config.get("appName"));
		//����frame�ĳߴ��λ���Լ����ַ�ʽ
		setSize(FupConfig.frameWidth, FupConfig.frameHeight);
		setLocation((FupConfig.screenWidth - FupConfig.frameWidth) / 2, (FupConfig.screenHeight - FupConfig.frameHeight) / 2);
		setLayout(null);
		
		//��������
		ImageIcon icon = new ImageIcon(FupConfig.PATH_UI);
		JLabel bglabel = new JLabel(icon);
		bglabel.setBounds(0, 0, FupConfig.frameWidth, FupConfig.frameHeight);
		getLayeredPane().add(bglabel, new Integer(Integer.MIN_VALUE));
		((JPanel)(getContentPane())).setOpaque(false);
		((JRootPane)(getRootPane())).setOpaque(false);
		setUndecorated(true);
		
		//�رհ�ť���趨
		button(FupConfig.frameWidth-42, 11, 32, 32, null, "close"); 
		
		//����ָ��
		button(506, 18, 75, 25, null, "leading");
		
		//input��ť���趨
		button(48, 108, 80, 40, null, "input");
		
		//output��ť���趨
		button(48, 169, 80, 40, null, "output");

		//convert��ť���趨
		button(511, 323, 80, 40, null, "convert");
		
		//input field �ı�����趨
		FupConfig.inputField = input(162, 108, 420, 40, "");
		
		//output field �ı�����趨
		FupConfig.outputField = input(162, 170, 420, 40, "");
		
		//output field �ı�����趨
		FupConfig.qualityField = input(162, 233, 45, 40, "28");
		FupConfig.qualityField.addKeyListener(this);
		
		//size field �ı�����趨
		FupConfig.sizeField = input(355, 233, 228, 40, "1280");
		
		//type field �ı�����趨
		FupConfig.outTypeField = input(196, 288, 45, 32, "mp4");
		
		//status field ״̬�����趨
		FupConfig.statusIcon = label(FupConfig.PATH_READY, 0, 320, 120, 120, "center");
		FupConfig.statusMsg = label(FupConfig.config.get("waiting")+"...",140,330,450,100,"left");
		FupConfig.statusMsg.setFont(font(18));
		
		//��drag �¼������϶�����
		FupMouseEvent mymouse = new FupMouseEvent(this);
		addMouseMotionListener(mymouse);
		addMouseListener(mymouse);
		
		//������ʾ���������š��ر���Ϊ
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	//����������ť
	private JButton button(int x, int y, int width, int height, String imgurl,String command){
		JButton btn = new JButton();
		btn.setFocusable(false);
		btn.setBounds(x, y, width, height);
		btn.setActionCommand(command);
		btn.addActionListener(this);
		this.getContentPane().add(btn);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.setContentAreaFilled(false);
		if(imgurl != null){
			btn.setIcon(new ImageIcon(imgurl));
		}
		return btn;
	}
	//���������ı���
	private JTextField input(int x, int y, int width, int height, String text){
		JTextField tf = new JTextField(text);
		tf.setBounds(x, y, width, height);
		tf.setOpaque(false);
		tf.setBorder(null);
		tf.setForeground(FupConfig.gray100);
		tf.setFont(this.font(14));
		this.getContentPane().add(tf);
		return tf;
	}
	//���������ı���
	private JLabel label(String content,int x, int y, int width, int height, String align){
		JLabel lb = null;
		if(FupConfig.inArray(content, FupConfig.imgTypes )){
			lb = new JLabel(new ImageIcon(content));
		}else{
			lb = new JLabel(content);
		}
		int lg = 0;
		if(align.equals("center")){
			lg = JLabel.CENTER;
		}else if(align.equals("right")){		
			lg = JLabel.RIGHT;
		}else{	
			lg = JLabel.LEFT;
		}
		lb.setForeground(FupConfig.gray100);
		lb.setFont(this.font(14));
		lb.setHorizontalAlignment(lg);
		lb.setBounds(x, y, width, height);
		this.getContentPane().add(lb);
		return lb;
	}
	//������������
	private Font font(int size){
		Font f = new Font("Microsoft YaHei",Font.PLAIN, size);
		return f;
	}
	//����¼�����,���书��
	public void actionPerformed(ActionEvent e) {
		//��ȡ�¼�ʵ����
		String cmd = e.getActionCommand();
		String log = FupConfig.PATH_LOG;
		if(cmd.equals("input")){
			String savePath = this.readLog(log);
			this.inputHandle(savePath);
			if(log != null) this.writeLog(log);
		}else if(cmd.equals("output")){
			String savePath = this.readLog(log);
			this.outputHandle(savePath);
		}else if(cmd.equals("convert")){
			this.convertHandle();
		}else if(cmd.equals("close")){
			this.exitHandle();
		}else if(cmd.equals("leading")){
			this.leadingHandle();
		}
	}
	//��ȡlog.txt���ѱ����Ŀ¼
	private String readLog(String log){
		String savePath = null;
		//��ȡlog.txt
		try{
			FileReader filereader = new FileReader(log);
			BufferedReader bufferedReader=new BufferedReader(filereader);
			savePath = bufferedReader.readLine();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return savePath;
	}
	//�����һ�δ򿪵�Ŀ¼д��log.txt��
	private void writeLog(String log){
		String inputDir = FupConfig.inputField.getText().split(";")[0];
		if(new File(inputDir).isFile()){
			try{
				FileOutputStream fopen = new FileOutputStream(log);
				String content = inputDir.substring(0, inputDir.lastIndexOf("\\"));
				fopen.write(content.getBytes("GBK"));
				fopen.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	//�����롱��ť�¼��Ĳ���
	private void inputHandle(String savePath){
		JFileChooser chooser = new JFileChooser(savePath);
		chooser.setMultiSelectionEnabled(true);
		String path = null;
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File[] files = chooser.getSelectedFiles();
			String tmp = "";
			for(int i=0, len=files.length; i<len; i++){
				path = files[i].getAbsolutePath();
				if(FupConfig.inArray(path, FupConfig.videoTypes) || FupConfig.inArray(path, FupConfig.imgTypes)){
						if(i>0) tmp += ";";
						tmp += path;
				}else{
					String videoStr = FupConfig.arrayToStr(FupConfig.videoTypes, "��"),
							imgStr = FupConfig.arrayToStr(FupConfig.imgTypes, "��");	
					JOptionPane.showMessageDialog(null, FupConfig.config.get("typeErr").replace("$0", videoStr).replace("$1", imgStr), FupConfig.config.get("dialogTitle"), JOptionPane.ERROR_MESSAGE); 
				}
			}
			FupConfig.inputField.setText(tmp);
	  	}
	}
	//���������ť�¼��Ĳ���
	private void outputHandle(String savePath){
		JFileChooser chooser = new JFileChooser(savePath);
		String path = null;
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			path = chooser.getSelectedFile().getAbsolutePath();
			FupConfig.outputField.setText(path);
	  	}
	}
	//����ʼת�����¼��Ĳ���
	private void convertHandle(){
		String[] inputArr = FupConfig.inputField.getText().split(";");
		String[] sizes = FupConfig.sizeField.getText().split("[ /s]+");
		String[] outTypeTmp = FupConfig.outTypeField.getText().split("[ /s]+");
		String[] outTypes = new String[2];
		outTypes[0] = "mp4";
		outTypes[1] = null;
		if(outTypeTmp.length>1){
			if(FupConfig.inArray(outTypeTmp[0], FupConfig.videoTypes)){
				outTypes[0] = outTypeTmp[0];
			}else if(FupConfig.inArray(outTypeTmp[1], FupConfig.videoTypes)){
				outTypes[0] = outTypeTmp[1];
			}else{
				outTypes[0] = "mp4";
			}
			if(FupConfig.inArray(outTypeTmp[0], FupConfig.imgTypes)){
				outTypes[1] = outTypeTmp[0];
			}else if(FupConfig.inArray(outTypeTmp[1], FupConfig.imgTypes)){
				outTypes[1] = outTypeTmp[1];
			}else{
				outTypes[1] = null;
			}
		}else if(outTypeTmp.length==1){
			if(FupConfig.inArray(outTypeTmp[0], FupConfig.videoTypes)){
				outTypes[0] = outTypeTmp[0];
				outTypes[1] = null;
			}else if(FupConfig.inArray(outTypeTmp[0], FupConfig.imgTypes)){
				outTypes[0] = "mp4";
				outTypes[1] = outTypeTmp[0];
			}
		}
		outTypeTmp = null;

		if(inputArr.length > 0 && new File(inputArr[0]).isFile()){
			FupConfig.inConverting = true;
			FupConfig.statusMsg.setForeground(FupConfig.darkgreen);
			new Fupffmpeg(inputArr, FupConfig.outputField.getText(), sizes, outTypes);
		}else{
			JOptionPane.showMessageDialog(null, FupConfig.config.get("emptyErr"), FupConfig.config.get("dialogTitle"), JOptionPane.ERROR_MESSAGE);
		}
	}
	//�˳�����
	private void exitHandle(){
		if(FupConfig.inConverting){
			int n = JOptionPane.showConfirmDialog(null, FupConfig.config.get("delWarning"),FupConfig.config.get("dialogTitle"), JOptionPane.YES_NO_OPTION);
			if(n == JOptionPane.YES_OPTION){
				Runtime rt = Runtime.getRuntime();
				try{
					rt.exec("TASKKILL /F /IM ffmpeg.exe");
				}catch(Exception ex){
					ex.printStackTrace();
				}
				System.exit(0);
			}else if(n == JOptionPane.NO_OPTION){
				return;
			}
		}else{
			System.exit(0);
		}
	}
	//����ָ�ϵ���
	private void leadingHandle(){
		JOptionPane.showMessageDialog(null, FupConfig.config.get("leadingContent"), FupConfig.config.get("leadingTitle"), JOptionPane.PLAIN_MESSAGE);
	}
	//ֻ������������
	public void keyTyped(KeyEvent e){
		int keychar=e.getKeyChar();
		if(keychar<KeyEvent.VK_0 || keychar>KeyEvent.VK_9){
			e.consume();
		}
	}
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e){
		String str = FupConfig.qualityField.getText();
		if(!str.equals("")){
			int v = Integer.parseInt(str);
			if(v > 50){
				FupConfig.qualityField.setText("50");
			}
		}
	}
}
