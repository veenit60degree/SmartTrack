package com.models;

public class CompanySetingModel {
    int Id;
    int    CompanyId;
    String SettingId;
    String Value;
    String Type ;
    String SettingName;
    String OrderType;

    public CompanySetingModel(int id, int companyId, String settingId, String value, String type, String settingName, String orderType) {
        Id = id;
        CompanyId = companyId;
        SettingId = settingId;
        Value = value;
        Type = type;
        SettingName = settingName;
        OrderType = orderType;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int companyId) {
        CompanyId = companyId;
    }

    public String getSettingId() {
        return SettingId;
    }

    public void setSettingId(String settingId) {
        SettingId = settingId;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSettingName() {
        return SettingName;
    }

    public void setSettingName(String settingName) {
        SettingName = settingName;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }
}

