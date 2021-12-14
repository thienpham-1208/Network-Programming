package action;


import action.Action;

public class ReplyJoinAction extends Action {
	
	
	private int state;
	
	public ReplyJoinAction(int state) {
		this.state = state;
	}
	
	public int getState() {
		return this.state;
	}

	@Override
	public boolean matches(Action act) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
