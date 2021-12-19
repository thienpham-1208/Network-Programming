package pubsubbro;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import action.JoinAction;
import action.JoinAction.ClientType;
import action.Action;


public abstract class Client {
	
	protected Socket socket = null;
	protected ObjectInputStream in = null;
	protected ObjectOutputStream out = null;
	protected String location;
	ClientType clientType;
	Scanner scan = new Scanner(System.in);
	
	public Client() {		
	}
	
	public Client(ClientType clientType, String location) {
		try {		
			this.socket = new Socket("localhost", Config.PORT);
		
			this.clientType = clientType;
	
			this.out = new ObjectOutputStream(this.socket.getOutputStream());
			
			sendAction(new JoinAction(clientType, location));		
			this.in = new ObjectInputStream(this.socket.getInputStream());	
			this.location = location;	
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public String getLocation() {
		return this.location;
	}
	
	public void close() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendAction(Action act) {
		try {
			out.writeObject(act);
			out.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Action receiveAction() {
		Action act = null;
		try {
			act = (Action) this.in.readObject();			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return act;
	}	
	
	public static void main(String[] args) {
		String location = "hi";
		Publisher pub = new Publisher(ClientType.PUBLISHER, location);
		pub.process();
	}

}
