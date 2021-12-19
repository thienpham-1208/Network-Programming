package pubsubbro;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextArea;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import action.Action;
import action.PublishDataAction;
import action.ReplyJoinAction;
import action.UnsubcriptionAction;
import action.JoinAction.ClientType;


public class Subscriber extends Client {
	
	JButton connectButton;	
	JButton quitButton;	
	JFrame frameClient;	
	TextArea txtMessage;
	boolean check = true;
	
	public Subscriber(ClientType clientType, String location) {
		super(clientType, location);		
	}	
	
	public Subscriber() {
		Subscriber sub = new Subscriber(ClientType.SUBSCRIBER, location);
        sub.process();
	}
	
	
	public void process() {
		Action act = receiveAction();		
		
		if (!(act instanceof ReplyJoinAction)) {
			return;
		}
		ReplyJoinAction reply = (ReplyJoinAction) act;
		
		if (!(reply.getState() == 0)) {
			System.out.println(Config.messageToClient.get(reply.getState()));
			close();
		} else {		
			System.out.println(Config.messageToClient.get(reply.getState()));
			Thread thread = new Thread() {
				public void run() {
					System.out.println("type quit");
					String es = scan.nextLine();
					if (es.equalsIgnoreCase("quit")) {
						quit();
						check = false;
					}
				}
			};
			thread.start();
			
			Thread thread1 = new Thread() {
				public void run() {
					while (check) {
						try {
							Action action = (Action) in.readObject();
							if (action instanceof PublishDataAction) {
								PublishDataAction updateData = (PublishDataAction) action;
								System.out.println(updateData.getData());			
							} else if (action instanceof UnsubcriptionAction) {
								UnsubcriptionAction mess = (UnsubcriptionAction) action;
								System.out.println(mess.getMessage());
								close();						
							}
						} catch(IOException e) {
							e.printStackTrace();
						} catch(ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			};
			thread1.start();
		}	
	}
	
	public void initialize() {
		frameClient = new JFrame("Subscriber");		  
		connectButton = new JButton("Connect");
		quitButton = new JButton("Quit");
		frameClient.setResizable(false);        
		frameClient.setBounds(0, 200, 500, 500);
		
		JPanel panel = new JPanel();
		// khoi tao JTextField
	        
		JTextField jTextField = new JTextField(10);	        
		panel.add(jTextField);	        
		panel.add(connectButton);    
		panel.add(quitButton);
	        
		frameClient.add(panel);
		frameClient.setVisible(true);
		connectButton.addActionListener(e -> {
	        String location = jTextField.getText();
	        Subscriber sub = new Subscriber(ClientType.SUBSCRIBER, location);
	        sub.process();
	    });				
		
		quitButton.addActionListener(e -> {
			quit();
		});
		
		txtMessage = new TextArea();
		txtMessage.setBackground(Color.WHITE);
		txtMessage.setForeground(Color.BLACK);
		txtMessage.setFont(new Font("Consolas", Font.PLAIN, 14));
		txtMessage.setEditable(false);
		txtMessage.setBounds(0, 50, 500, 500); ////// Vi tri textArea		
		frameClient.getContentPane().add(txtMessage);
	}
	
	public void updateMessage(String msg) {
		txtMessage.append(msg + "\n");
	}
	
	public void quit() {
		String msg = "quit action from sub " + socket.getLocalPort();	
		sendAction(new UnsubcriptionAction(msg));
		close();	
	}
	
	public static void main(String[] args) {
		String location = "hello/hi";
		Subscriber sub = new Subscriber(ClientType.SUBSCRIBER, location);
		sub.process();
	}	
}
