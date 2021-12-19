package pubsubbro;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

import action.Action;
import action.PublishDataAction;
import action.UnsubcriptionAction;
import action.JoinAction.ClientType;

public class pubClientHandler extends ClientHandler {

	private Set<subClientHandler> sub;
	boolean check = true;
	
	
	public pubClientHandler(long id, Socket socket, ObjectInputStream in, ObjectOutputStream out, Set<subClientHandler> setSub, String location, ClientHandler[] clientHandlers) {
		super(id, socket, in, out, location, clientHandlers);
		// TODO Auto-generated constructor stub
		this.sub = setSub;	
	}
	
	public void setSub(Set<subClientHandler> sub) {
		this.sub = sub;
	}
	
	public Set<subClientHandler> getSub() {
		return this.sub;
	}		
	
	@Override
	public void run() {
		while (check) {
			try {
				Action act = (Action) this.in.readObject();		
				// send data to other sub
				for (subClientHandler s : sub)	 {
					s.sendAction(act);
				}
				/*
				 * for (subClientHandler s : sub) { for (ClientHandler cl : clientHandlers) { if
				 * (cl instanceof subClientHandler) { subClientHandler sub = (subClientHandler)
				 * cl; if (sub.equals(cl)) { System.out.println(sub); s.sendAction(act); } } } }
				 */
				if (act instanceof UnsubcriptionAction) {
					for (subClientHandler s : sub) {
						for (int i = 0; i < Config.MAX_CONNECTIONS; i++) {
							if (clientHandlers[i] instanceof subClientHandler) {
								subClientHandler sub = (subClientHandler) clientHandlers[i];
								if (sub.equals(s)) {
									clientHandlers[i] = null;
									s.close();
								}
							}
						}
					}
					for (int i = 0; i < Config.MAX_CONNECTIONS; i++) {
						if (clientHandlers[i] instanceof pubClientHandler) {
							pubClientHandler pub = (pubClientHandler) clientHandlers[i];
							if (pub.equals(this)) {
								clientHandlers[i] = null;
								check = false;
								close();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}	
	
	public static void main(String[] args) {
		String location = "hello";
		Subscriber sub = new Subscriber(ClientType.SUBSCRIBER, location);
		sub.process();
	}	

}
