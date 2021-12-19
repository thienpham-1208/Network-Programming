package UI;

import java.awt.Font;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.TextArea;
import javax.swing.UIManager;

import pubsubbro.Publisher;

public class ServerUI {

	JFrame frmServerMangement;

	TextArea txtMessage;

	JButton btnStart;

	public void updateMessage(String msg) {	       
		txtMessage.append(msg + "\n");
	}

	public void initialize() {
		frmServerMangement = new JFrame();
		frmServerMangement.setForeground(UIManager.getColor("RadioButtonMenuItem.foreground"));
		frmServerMangement.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 13));
		frmServerMangement.getContentPane()
				.setForeground(UIManager.getColor("RadioButtonMenuItem.acceleratorSelectionForeground"));
		frmServerMangement.setTitle("Server Mangement");
		frmServerMangement.setResizable(false);
		frmServerMangement.setBounds(200, 200, 500, 500);
		frmServerMangement.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmServerMangement.getContentPane().setLayout(null);
		frmServerMangement.setBackground(Color.ORANGE);
		frmServerMangement.setVisible(true);
	
		btnStart = new JButton("START"); 
		btnStart.setBackground(UIManager.getColor("RadioButtonMenuItem.selectionBackground")); 
		btnStart.setFont(new Font("Segoe UI", Font.PLAIN, 13));		  
		btnStart.setBounds(200, 30, 80, 50); /////// Vi tri button START
		frmServerMangement.getContentPane().add(btnStart);		 
		btnStart.addActionListener(e -> { process(); listen();});		

		txtMessage = new TextArea();
		txtMessage.setBackground(Color.WHITE);
		txtMessage.setForeground(Color.BLACK);
		txtMessage.setFont(new Font("Consolas", Font.PLAIN, 14));
		txtMessage.setEditable(false);
		txtMessage.setBounds(0, 100, 500, 358); ////// Vi tri textArea
		frmServerMangement.getContentPane().add(txtMessage);
	}
	
	public void process() {
	}
	
	public void listen() {
	}
	
	public static void main(String[] args) {
		String location = "b";
		String[] parts = location.split("/");
		System.out.println(parts.length);
		for (int i = 0; i < parts.length; i++) {
			System.out.println(parts[i]);
		}
	}

}
