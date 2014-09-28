package com.wupipi.coedit.model;

import java.util.List;

/**
 * User: xudong
 * Date: 9/14/14
 * Time: 10:20 AM
 */
public class ResyncResult {
    private String content;

    private List<ChatMessage> messageHistory;

    private int nextRevision;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNextRevision() {
        return nextRevision;
    }

    public void setNextRevision(int nextRevision) {
        this.nextRevision = nextRevision;
    }

    public List<ChatMessage> getMessageHistory() {
        return messageHistory;
    }

    public void setMessageHistory(List<ChatMessage> messageHistory) {
        this.messageHistory = messageHistory;
    }
}
