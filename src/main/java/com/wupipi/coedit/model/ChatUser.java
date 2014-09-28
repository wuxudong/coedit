package com.wupipi.coedit.model;

public class ChatUser implements Comparable<ChatUser>{
	private String uid;
	private String name;

	
	public ChatUser(String uid, String name) {
		this.uid = uid;
		this.name = name;
	}

    public ChatUser() {
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
		return uid;
	}
	
	public String getName() {
		return name;
	}

    @Override
    public int compareTo(ChatUser o) {
        return name.compareTo(o.getName());
    }
}
