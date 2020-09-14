/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp.actionEnum;

/**
 *
 * @author hd161
 */
public enum ActionEnum {
    EXITCHAT("exit"),
    FIRSTCALL("firstcall"),
    SENDMESSAGE("sendmessage"),
    UPDATEACTIVES("updateactives");
    
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
