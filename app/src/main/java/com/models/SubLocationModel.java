package com.models;

import java.util.List;

public class SubLocationModel {

    String SubLocationID;
    String MasterLoadingLocationId;
    String LocationName;
    String LocationAddress;
    boolean IsActive;


    public SubLocationModel(String subLocationID, String masterLoadingLocationId,
                            String locationName, String locationAddress, boolean isActive) {
        SubLocationID = subLocationID;
        MasterLoadingLocationId = masterLoadingLocationId;
        LocationName = locationName;
        LocationAddress = locationAddress;
        IsActive = isActive;
    }

    public String getSubLocationID() {
        return SubLocationID;
    }

    public String getMasterLoadingLocationId() {
        return MasterLoadingLocationId;
    }

    public String getLocationName() {
        return LocationName;
    }

    public String getLocationAddress() {
        return LocationAddress;
    }

    public boolean isActive() {
        return IsActive;
    }
}
