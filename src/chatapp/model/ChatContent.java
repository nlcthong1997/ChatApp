package chatapp.model;

public class ChatContent {
	private String contentChat;
	private User sender;
	private User receiver;
	public String getContentChat() {
		return contentChat;
	}
	public void setContentChat(String contentChat) {
		this.contentChat = contentChat;
	}
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public User getReceiver() {
		return receiver;
	}
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	public ChatContent(String contentChat, User sender, User receiver) {
		super();
		this.contentChat = contentChat;
		this.sender = sender;
		this.receiver = receiver;
	}
	public ChatContent() {
		super();
	}
	
	
}
