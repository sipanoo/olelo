package com.pro.st.Model;

import java.util.HashMap;
import java.util.Map;

public class ChatItem {
    private String id;
    private String user;
    private String image;
    private String lastmessage;

    ChatItem() {

    }


    public ChatItem(String id, String user, String image, String lastmessage) {
        this.id = id;
        this.user = user;
        this.image = image;
        this.lastmessage = lastmessage;
    }
    public ChatItem(String id, String user, String image) {
        this.id = id;
        this.user = user;
        this.image = image;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastmessege() {
        return lastmessage;
    }

    public void setLastmessege(String lastmessege) {
        this.lastmessage = lastmessege;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
