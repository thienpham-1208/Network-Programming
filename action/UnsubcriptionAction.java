package action;

public class UnsubcriptionAction extends Action{
	
	private String message;

	public UnsubcriptionAction(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
	
	@Override
	public boolean matches(Action act) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
