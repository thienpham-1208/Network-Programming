package action;

import pubsubbro.Broker;

public class PublishDataAction extends Action {
	
	private String data;
	private String location;
	
	public PublishDataAction(String data, String location) {
		this.data = data;
		this.location = location;
	}
	
	public String getData() {
		return this.data;
	}
	
	public String getLocation() {
		return this.location;
	}

	@Override
	public boolean matches(Action act) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
