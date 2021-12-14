package action;

public class JoinAction extends Action {
	
	public static enum ClientType {
		PUBLISHER, SUBSCRIBER
	};	

	private String location;	
	private ClientType clientType;
	
	public JoinAction(ClientType clientType, String location) {
	
		this.clientType = clientType;
		this.location = location;
	}
	
	public ClientType getClientType() {
		return clientType;
	}	
	
	public String getLocation() {
		return this.location;
	}
	
	public boolean matches(Action act) {
		return false;
	}

	
}
