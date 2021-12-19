package pubsubbro;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import action.Action;
import action.PublishDataAction;
import action.UnsubcriptionAction;
import action.JoinAction.ClientType;

import java.io.IOException;

public class subClientHandler extends ClientHandler {	
	
	boolean check = true;
	
	public subClientHandler(long id, Socket socket, ObjectInputStream in, ObjectOutputStream out, String location, ClientHandler[] clientHandlers) {
		super(id, socket, in, out, location, clientHandlers);
		// TODO Auto-generated constructor stub		
	}
	
	public ObjectOutputStream getOut() {
		return this.out;
	}
	
	@Override
	public void run() {
		while (check) {			
			try {
				Object act = this.in.readObject();
				if (act == null || !(act instanceof UnsubcriptionAction)) {
					continue;
				} else if (act instanceof UnsubcriptionAction) {
					for (int i = 0; i < Config.MAX_CONNECTIONS; i++) {
						if (clientHandlers[i] instanceof subClientHandler) {
							subClientHandler sub = (subClientHandler) clientHandlers[i];
							if (sub.equals(this)) {
								clientHandlers[i] = null;
								check = false;
								close();
							}
						}
					}
				}
				Thread.sleep(Config.SLEEP_TIME);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InterruptedException  e) {				
			}			
		}					
	}
	
	public void sendAction(Action act) {
		try {
			out.writeObject(act);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String location = "hello/hihi";
		Publisher pub = new Publisher(ClientType.PUBLISHER, location);
		pub.process();
	}


}
