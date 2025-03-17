package com.pro.st.Model;

public class MessageItem {


    private String sender;
    private String textMessege;
    private String recover;


    public MessageItem() {
    }

    public MessageItem(String sender, String recover , String textMessege ) {
        this.sender = sender;
        this.recover = recover;
        this.textMessege=textMessege;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTextMessege() {
        return textMessege;
    }

    public void setTextMessege(String textMessege) {
        this.textMessege = textMessege;
    }

    public String getRecover() {
        return recover;
    }

    public void setRecover(String recover) {
        this.recover = recover;
    }

    @Override
    public String toString() {
        return "(" + sender + ":" + textMessege+ ")";
    }

}
