import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

// Count effective lines of code inside a file by choosing the file in the dialogue window, 
// Or you can put lots of files in a folder and count them together.
// Will generate different comments respective to the ratio of Effective codes, 
// White lines and Comments in the codes.

public class CodeCounter implements ActionListener {
	// Using JFrame to generate the window.
	public JFrame frame = new JFrame("Code Counter");
	public JTabbedPane tabPane=new JTabbedPane();
	public JFileChooser fileChooser = new JFileChooser();
	public JButton button1 = new JButton("Open File"); 
	public JButton button2 = new JButton("Open Folder");
	public JButton button3 = new JButton("Run");
	public JLabel l1 = new JLabel("Select File");
	public JLabel l2 = new JLabel("Select Folder");
	public Container c1 = new Container();
	public JTextField text1 = new JTextField();
	public JTextField text2 = new JTextField();
	public JTextArea text3 = new JTextArea();
	public File f1 = new File("C:\\null");
	
	public String str = "";
	public boolean isDiretory = false;
	
	
	public static long commentLine;
	public static long emptyLine;
	public static long codeLine;
	
	
	public CodeCounter (){
		//Initialize the class -- the dialogue window.
		
		fileChooser.setCurrentDirectory(new File("C:\\Users\\Desktop"));
		frame.setLocation(1500, 30);
		frame.setSize(400,300);
		frame.setContentPane(c1);
		frame.setBackground(Color.LIGHT_GRAY);
		text1.setBounds(80,10,120,20);
        text2.setBounds(80,30,120,20);
        l1.setBounds(10,10,70,20);
        l2.setBounds(10,30,100,20);
        text3.setBounds(20,80,200,150);
		button1.setBounds(210,10,120,20);
        button2.setBounds(210,30,120,20);
        button3.setBounds(250,180,80,20);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		
		c1.add(button1);
		c1.add(button2);
		c1.add(button3);
		c1.add(l1);
		c1.add(l2);
		c1.add(text1);
		c1.add(text2);
		c1.add(text3);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(button1)){
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int resultVal = fileChooser.showOpenDialog(null);
			if(resultVal == JFileChooser.CANCEL_OPTION){
				return;
			} else if (resultVal == JFileChooser.APPROVE_OPTION){
				f1 = fileChooser.getSelectedFile();
				text1.setText(f1.getAbsolutePath());
			}
		} else if (e.getSource().equals(button2)){
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			int resultVal = fileChooser.showOpenDialog(null);
			if(resultVal == JFileChooser.APPROVE_OPTION){
				f1 = fileChooser.getSelectedFile();
				text2.setText(f1.getAbsolutePath());
			}
		}
		
		if(e.getSource().equals(button3)){
			if(f1.getAbsolutePath() == "C:\\null"){
				text3.setText("Please choose a file");
				return;
			} else if(f1.isDirectory() == true){
				isDiretory = true;
				File [] childFiles = f1.listFiles();
				for(File child : childFiles){
					countNow(child);
				}
				reset();
			} else{
				countNow(f1);
			}
		}
	}
	
	
	public void countNow(File f){
		BufferedReader br = null;
		boolean comment = false;
		
		// Simple Regular expression to count effective lines and ignore white lines.
		try {
			br = new BufferedReader(new FileReader(f));
			String line = "";
			while((line = br.readLine() )!= null){
				line = line.trim();
				if(line.matches("^[\\s&&[^\\n]]*$")){
					emptyLine++;
				} else if(line.matches("^\\n")){
					emptyLine++;
				} else if(line.startsWith("/*") && line.endsWith("*/")){
					commentLine++;
				} else if(line.startsWith("/*") && !line.endsWith("*/")){
					commentLine ++;
					comment = true;
				} else if(true == comment){
					commentLine ++;
					if(line.endsWith("*/")){
						comment = false;
					}
				} else if(line.startsWith("//")){
					commentLine++;
					
				} else {
					codeLine ++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null){
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		result();
	}
	
	public void result(){
		
		str = "";
		str += " Effective Lines£º" + codeLine;
		str += "\n Comments£º" + commentLine;
		str += "\n Empty Lines£º" + emptyLine;
		str += "\n";
		
		
		if((codeLine + commentLine + emptyLine) > 100){
			if(codeLine> (commentLine + emptyLine)){
				str += "\n Nice Job! Quite productive!";
			} else if(commentLine > codeLine){
				str += "\n That many comment lines? Are you writing a novel or what?";
			} else if (emptyLine > codeLine){
				str += "\n Seriously£¬you must fall asleep on your Enter key.....";
			} else{
				str += "\n It's alright, keep going";
			}
		} else if((codeLine + commentLine + emptyLine) < 100){
			if(codeLine> (commentLine + emptyLine)){
				str += "\n Need practice more probably";
			} else if(commentLine > codeLine){
				str += "\n That many comment lines? Are you writing a novel or what?";
			} else if (emptyLine > codeLine){
				str += "\n Seriously£¬you must fall asleep on your Enter key.....";
			} else{
				str += "\n No comments otherwise you will delete me";
			}
		} else if((codeLine + commentLine + emptyLine) < 30){
			if(codeLine> (commentLine + emptyLine)){
				str += "\n You are counting a single Class right?";
			} else if(commentLine > codeLine){
				str += "\n The usage of this Class is to communicate with users...";
			} else if (emptyLine > codeLine){
				str += "\n You boss will fire you";
			} else{
				str += "\n No comments otherwise you will delete me";
			}
		}
		
		text3.setLineWrap(true);
		text3.setText(str);
		if(!isDiretory){
			reset();
		}
	}
	
	public void reset(){
		commentLine = 0;
		emptyLine = 0;
		codeLine = 0;
	}
	
	
	public static void main(String[] args) {
		new CodeCounter();
	}
}
