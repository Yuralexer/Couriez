package com.yuralexer.couriez.model;

public class ChatMessage {
    private String text;
    private boolean isSentByUser;

    public ChatMessage(String text, boolean isSentByUser) {
        this.text = text;
        this.isSentByUser = isSentByUser;
    }

    public String getText() { return text; }
    public boolean isSentByUser() { return isSentByUser; }
}
