package pubsubbro;

import java.awt.EventQueue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import UI.ServerUI;
import action.JoinAction;
import action.JoinAction.ClientType;
import action.ReplyJoinAction;

public class Broker extends ServerUI {	
	
	public static final ClientHandler[] clientHandlers = new ClientHandler[Config.MAX_CONNECTIONS];
	
	ServerSocket serverSocket = null;	
	
	List<String> locations = new ArrayList<String>();	
	HashMap<String, Set<subClientHandler>> subscribersTopicMap = new HashMap<String, Set<subClientHandler>>();

	private long id;
	
	public Broker() {		
		initialize();		
	}
	
	public void process() {
		try {
			this.serverSocket = new ServerSocket(Config.PORT);
			updateMessage("Init server at port " + serverSocket.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listen() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				updateMessage("Server get connection at port " + socket.getPort());				
			
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());		
				JoinAction joinAction = (JoinAction) in.readObject();			
			
				int state = checkConnect(joinAction);
				ClientType type = joinAction.getClientType();		
				
				ReplyJoinAction replyAction = new ReplyJoinAction(state);
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());	
				
				out.writeObject(replyAction);
				out.flush();					
				if (state != 0) {
					//reject, close connection
					socket.close();
					in.close();
					out.close();
				} else {						
					// new client handle, communicate
					if (type.equals(ClientType.PUBLISHER)) {
						pubAcceptedRequest(socket, in, out, joinAction);						
						updateMessage("Accept pub request at port " + socket.getPort());	
						
						for (String s : locations) {
							updateMessage(s);
						}					
						
						for (String name : subscribersTopicMap.keySet()) {
							String key = name.toString();
							String value = subscribersTopicMap.get(name).toString();
							updateMessage(key + " " + value);
						}	
				
						
					} else if (type.equals(ClientType.SUBSCRIBER)) {		
						subAcceptedRequest(socket, in, out, joinAction);
						updateMessage("Accept sub request at port " + socket.getPort());	
						
						for (String s : locations) {
							updateMessage(s);
						}
					
						for (String name : subscribersTopicMap.keySet()) {
							String key = name.toString();
							String value = subscribersTopicMap.get(name).toString();
							updateMessage(key + " " + value);
						}	
					}					
				}				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private long generaetID() {
		return id++;
	}
	
	public void pubAcceptedRequest(Socket socket, ObjectInputStream in, ObjectOutputStream out, JoinAction joinAction) {
		String location = joinAction.getLocation();	
		locations.add(location);		
		
		Set<subClientHandler> subscribers = new HashSet<subClientHandler>();		
		subscribersTopicMap.put(location, subscribers);			
		
		pubClientHandler clientHandler = new pubClientHandler(generaetID(), socket, in, out, subscribers, joinAction.getLocation(), clientHandlers);				
		
		for (int i = 0; i < Config.MAX_CONNECTIONS; i++) {
			if (clientHandlers[i] != null)
				continue;
			clientHandlers[i] = clientHandler;
			clientHandler.start();
			break;
		}		
	}
	
	public void subAcceptedRequest(Socket socket, ObjectInputStream in, ObjectOutputStream out, JoinAction joinAction) {
		String[] loca = joinAction.getLocation().split("/");
		
		subClientHandler clientHandler = new subClientHandler(generaetID(), socket, in, out, joinAction.getLocation(), clientHandlers);			
		
		if (loca.length == 2) {
			Set<subClientHandler> subscribers = subscribersTopicMap.get(joinAction.getLocation());						
			subscribers.add(clientHandler);			
			subscribersTopicMap.put(joinAction.getLocation(), subscribers);
			
			for (int i = 0; i < Config.MAX_CONNECTIONS; i++) {
				if (clientHandlers[i] instanceof pubClientHandler) {
					pubClientHandler pub = (pubClientHandler) clientHandlers[i];
					if (!(pub.getLocation().equals(joinAction.getLocation()))) {
						continue;
					}
					pub.setSub(subscribers);
					break;
				}
			}
		} else if (loca.length == 1) {			
			String location = loca[0];			
			for (int i = 0; i < Config.MAX_CONNECTIONS; i++) {				
				if (clientHandlers[i] instanceof pubClientHandler) {
					
					pubClientHandler pub = (pubClientHandler) clientHandlers[i];
					
					String location1 = pub.getLocation().split("/")[0];		
					
					if (location.equals(location1)) {
						Set<subClientHandler> subscribers = pub.getSub();
						subscribers.add(clientHandler);
						pub.setSub(subscribers);
					}
				}
			}			
		}
		
		for (int i = 0; i < Config.MAX_CONNECTIONS; i++) {
			if (clientHandlers[i] != null)
				continue;
			clientHandlers[i] = clientHandler;
			clientHandler.start();
			break;
		}
	}
	
	public int checkConnect(JoinAction act) {
		boolean check = true;		
		boolean check_sub = false;
		int state;
		int i;
		for (i = 0; i < clientHandlers.length; i++) {	
			if (clientHandlers[i] == null) 
				break;		
		}
		if (i == Config.MAX_CONNECTIONS) {
			return 1;
		}
		
		if (act.getClientType().equals(ClientType.PUBLISHER)) {
			String[] location = act.getLocation().split("/");
			if (!(location.length == 2)) {
				return 4;
			}
			for (ClientHandler cl : clientHandlers) {
				if (cl instanceof pubClientHandler) {
					pubClientHandler pub = (pubClientHandler) cl;
					if (pub.getLocation().equals(act.getLocation())) {
						return 2;
					}
				}			
			}
			
		} else if (act.getClientType().equals(ClientType.SUBSCRIBER)) {
			String[] location = act.getLocation().split("/");
			if (location.length > 2) {
				return 5;
			}
			if (location.length == 2) {			
				for (String s : locations) {
					if (s.equals(act.getLocation())) {
						check_sub = true;
						break;
					}			
				}
			} else if (location.length == 1) {
				String loca = location[0];
				for (ClientHandler cl : clientHandlers) {
					if (cl instanceof pubClientHandler) {
						pubClientHandler pub = (pubClientHandler) cl;
						if (pub.getLocation().split("/")[0].equals(loca)) {
							check_sub = true;
							break;
						}
					
					}			
				}
			}
		}
		if (act.getClientType().equals(ClientType.SUBSCRIBER)) {
			state = (check_sub)?0:3;
		} else {
			state = (check)?0:3;
		}
		return state;		
	}
	
	
	public void disconnectSub(subClientHandler sub) {
		Set<subClientHandler> subscribers = subscribersTopicMap.get(sub.getLocation());				
		
		for (subClientHandler s : subscribers) {
			if (s.equals(sub)) {
				subscribers.remove(sub);
				break;			
			}			
		}
		subscribersTopicMap.put(sub.getLocation(), subscribers);	
		for (int i = 0; i < Config.MAX_CONNECTIONS; i++) {
			if (clientHandlers[i] instanceof pubClientHandler && clientHandlers[i].getLocation().equals(sub.getLocation())) {
				pubClientHandler pub = (pubClientHandler) clientHandlers[i];
				pub.setSub(subscribers);
				clientHandlers[i] = pub;
				break;
			}
		}	

		for (int i = 0; i < Config.MAX_CONNECTIONS; i++) {
			if (clientHandlers[i] != null && clientHandlers[i].equals(sub)) {
				clientHandlers[i] = null;
				break;
			}
		}		
	}
	
	public void disconnectPub(pubClientHandler pub) {			
		locations.remove(pub.getLocation());				
		Set<subClientHandler> subscribers = pub.getSub();			
		for (subClientHandler sub : subscribers) {				
			for (int i = 0; i < Config.MAX_CONNECTIONS; i++) {					
				if (sub.equals(clientHandlers[i])) {						
					clientHandlers[i] = null;					
				}				
			}			
		}		
		for (int i = 0; i < Config.MAX_CONNECTIONS; i++) {
			if (clientHandlers[i] != null && clientHandlers[i].equals(pub)) {
				clientHandlers[i] = null;
				break;
			}
		}
	}
	
	public void quit() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Broker bro = new Broker();
	}
}
