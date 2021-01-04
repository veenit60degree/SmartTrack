package com.models;


public class NotificationModel {

    String keyName;
    String value;

    public NotificationModel(String keyName, String value) {
        this.keyName = keyName;
        this.value = value;
    }

    public String getKeyName() {
        return keyName;
    }

    public String getValue() {
        return value;
    }
}
