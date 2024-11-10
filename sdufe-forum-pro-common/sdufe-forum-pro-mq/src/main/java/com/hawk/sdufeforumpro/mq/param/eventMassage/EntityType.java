package com.hawk.sdufeforumpro.mq.param.eventMassage;

public enum EntityType {

    ARTICLE(1),

    COMMENT(2);

    private int value;

    EntityType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
