package com.wupipi.coedit.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Room {
    private String name;
    private Map<String, ChatUser> users = new ConcurrentHashMap<String, ChatUser>();
    private List<ChatMessage> messageHistory = new CopyOnWriteArrayList<ChatMessage>();
    private Paper paper = new Paper();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<ChatUser> getUsers() {
        return users.values();
    }

    public void setUsers(List<ChatUser> users) {
        Map<String, ChatUser> map = new ConcurrentHashMap<String, ChatUser>();
        for (ChatUser user : users) {
            map.put(user.getUid(), user);
        }

        this.users = map;
    }

    public ChatUser addUser(ChatUser user) {
        return this.users.put(user.getUid(), user);
    }

    public ChatUser removeUser(String uid) {
        return this.users.remove(uid);
    }

    public List<ChatMessage> getMessageHistory() {
        return messageHistory;
    }

    public void setMessageHistory(List<ChatMessage> messageHistory) {
        this.messageHistory = messageHistory;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }
}
