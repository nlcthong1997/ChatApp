package chatapp.enumApp;

public enum ActionEnum {
	
	UPDATELISTUSER("list"),
	UPDATECHAT("chat");
	
	private String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	private ActionEnum(String action) {
		this.action = action;
	}
	
	 
}
