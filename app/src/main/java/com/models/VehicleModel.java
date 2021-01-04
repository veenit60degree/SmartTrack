package com.models;


public class VehicleModel {

    String VehicleId;
    String EquipmentNumber;
    String PlateNumber;
    String VIN;
    String PreviousDeviceMappingId;
    String DeviceMappingId;
    String CompanyId;


    public VehicleModel(String vehicleId, String equipmentNumber, String plateNumber, String VIN,
                        String previousDeviceMappingId, String deviceMappingId, String companyId) {
        VehicleId = vehicleId;
        EquipmentNumber = equipmentNumber;
        PlateNumber = plateNumber;
        this.VIN = VIN;
        PreviousDeviceMappingId = previousDeviceMappingId;
        DeviceMappingId     = deviceMappingId;
        CompanyId = companyId;
    }

    public String getDeviceMappingId() {
        return DeviceMappingId;
    }


    public String getVehicleId() {
        return VehicleId;
    }

    public String getEquipmentNumber() {
        return EquipmentNumber;
    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public String getVIN() {
        return VIN;
    }

    public String getPreviousDeviceMappingId() {
        return PreviousDeviceMappingId;
    }

    public String getCompanyId() {
        return CompanyId;
    }
}
