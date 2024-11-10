package com.hawk.sdufeforumpro.api.message.constant;

public enum MessageType {

    LIKE(1),

    COMMENT(2),

    CHAT(3);

    private int value;

    MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
