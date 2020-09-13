/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.model;

import java.io.Serializable;

/**
 *
 * @author Dell
 */
public class User implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String username;
    public String password;
    private StringBuilder chatOfUser = new StringBuilder();

    public String getChatOfUser() {
		return chatOfUser.toString();
	}

	public void setChatOfUser(String chatOfUser) {
		this.chatOfUser.append(chatOfUser);
	}

	public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		User other = (User) obj;
		if(username==null || other.getUsername() ==null) {
			return false;
		}
		if (!username.equals(other.getUsername()))
			return false;
		return true;
	}

	public User(String username, String password, StringBuilder chatOfUser) {
		super();
		this.username = username;
		this.password = password;
		this.chatOfUser = chatOfUser;
	}

	public User() {
		super();
	}
    
    
}
