package com.models;


public class ContainerModel {

    String ChessieName;
    int    ContainerId;
    String ContainerNo;
    String ContainerType;
    String DriverName;
    String GrossWeight;
    String SealNo;
    String Size;
    int    Status;
    String TareWeight;
    String TrcukName;
    boolean  IsRead ;

    String  NextLocation;
    int NextLocationType;
    String PickDate;
    String LocationLongitude;
    String LocationLatitude;
    boolean IsLoadingAppointmentRequired;
    boolean IsPickUpAppointmentRequired;
    boolean IsReturnAppointmentRequired;
    String RefNumber;
    String PONumber;

    int    OrignTypeID;
    String OrignAddress;
    String OrignLatitude;
    String OrignLogitude;
    int    DestinationTypeID;
    String DestinationAddress;
    String DestinationLatitude;
    String DestinationLogitude;

    String LocationTypeID;
    String ContainerLoadTypeID;
    String BillTO;
    String BillToContact;
    String ReservationNo;
    String DropOffReservationNo;
    String ShipingLineAddress;
    boolean IsStreetReturned;
    String TruckId;
    String ChessieId;
    String LoadingLocationId;
    String LegType;

    String CompanyId;
    String MessageText;
    String JobAcceptance;
    String OrderType;
    String JobType;
    String PickUpResDate;
    String PickUpResTime;

    String ContainerTypeDetail;
    boolean isDriverJobSequence;
    boolean isTitleShown;


    public ContainerModel(String chessieName, int containerId, String truckId, String chessieId, String companyId, String containerNo,
                          String containerType, String driverName, String grossWeight,
                          String sealNo, String size, int status, String tareWeight,
                          String trcukName, boolean isRead,
                          String  NextLocation, int NextLocationType,
                          String PickDate, String LocationLongitude,
                          String LocationLatitude, boolean IsLoadingAppointmentRequired,
                          boolean IsPickUpAppointmentRequired, boolean IsReturnAppointmentRequired,
                          String RefNumber, String PONumber, int OrignTypeID,
                          String OrignAddress, String OrignLatitude,
                          String OrignLogitude, int DestinationTypeID,
                          String DestinationAddress, String DestinationLatitude,
                          String DestinationLogitude, String LocationTypeID, String ContainerLoadTypeID,
                          String BillTO, String BillToContact,
                          String ReservationNo, String DropOffReservationNo, String ShipingLineAddress,
                          boolean IsStreetReturned,  String LoadingLocationId,
                          String LegType,String messageText,String jobAcceptance, String orderType,
                          String jobType, String pickUpResDate, String pickUpResTime,
                          String containerTypeDetail, boolean isDriverSequence, boolean istitleShown ) {


        this.ChessieId= chessieId;
        this.TruckId= truckId;
        this.CompanyId= companyId;
        this.ChessieName = chessieName;
        this.ContainerId = containerId;
        this.ContainerNo = containerNo;
        this.ContainerType = containerType;
        this.DriverName = driverName;
        this.GrossWeight = grossWeight;
        this.SealNo = sealNo;
        this.Size = size;
        this.Status = status;
        this.TareWeight = tareWeight;
        this.TrcukName = trcukName;
        this.IsRead = isRead;

        this.NextLocation = NextLocation;
        this.NextLocationType = NextLocationType;
        this.PickDate = PickDate;
        this.LocationLongitude = LocationLongitude;
        this.LocationLatitude = LocationLatitude;
        this.IsLoadingAppointmentRequired = IsLoadingAppointmentRequired;
        this.IsPickUpAppointmentRequired = IsPickUpAppointmentRequired;
        this.IsReturnAppointmentRequired = IsReturnAppointmentRequired;

        this.RefNumber = RefNumber;
        this.PONumber = PONumber;

        this.OrignTypeID = OrignTypeID;
        this.OrignAddress = OrignAddress;
        this.OrignLatitude = OrignLatitude;
        this.OrignLogitude = OrignLogitude;
        this.DestinationTypeID = DestinationTypeID;
        this.DestinationAddress = DestinationAddress;
        this.DestinationLatitude = DestinationLatitude;
        this.DestinationLogitude = DestinationLogitude;

        this.LocationTypeID = LocationTypeID;
        this.ContainerLoadTypeID = ContainerLoadTypeID;
        this.BillTO = BillTO;
        this.BillToContact = BillToContact;
        this.ReservationNo = ReservationNo;
        this.DropOffReservationNo = DropOffReservationNo;
        this.ShipingLineAddress = ShipingLineAddress;
        this.IsStreetReturned = IsStreetReturned;

        this.LoadingLocationId = LoadingLocationId;
        this.LegType = LegType;
        this.MessageText = messageText;
        this.JobAcceptance = jobAcceptance;
        this.OrderType = orderType;
        this.JobType = jobType;
        this.PickUpResDate = pickUpResDate;
        this.PickUpResTime = pickUpResTime;
        this.ContainerTypeDetail = containerTypeDetail;
        this.isDriverJobSequence = isDriverSequence;
        this.isTitleShown           = istitleShown;

    }

    public String getContainerTypeDetail() {
        return ContainerTypeDetail;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getJobAcceptance() {
        return JobAcceptance;
    }

    public String getMessageText() {
        return MessageText;
    }

    public void setMessageText(String messageText) {
        MessageText = messageText;
    }

    public String getTruckId() {
        return TruckId;
    }

    public String getChessieId() {
        return ChessieId;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public String getChessieName() {
        return ChessieName;
    }

    public int getContainerId() {
        return ContainerId;
    }

    public String getContainerNo() {
        return ContainerNo;
    }

    public String getContainerType() {
        return ContainerType;
    }

    public String getDriverName() {
        return DriverName;
    }

    public String getGrossWeight() {
        return GrossWeight;
    }

    public String getSealNo() {
        return SealNo;
    }

    public String getSize() {
        return Size;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getStatus() {
        return Status;
    }

    public String getTareWeight() {
        return TareWeight;
    }

    public String getTrcukName() {
        return TrcukName;
    }

    public boolean isRead() {
        return IsRead;
    }

    public String getNextLocation() {
        return NextLocation;
    }

    public int getNextLocationType() {
        return NextLocationType;
    }

    public String getPickDate() {
        return PickDate;
    }

    public String getLocationLongitude() {
        return LocationLongitude;
    }

    public String getLocationLatitude() {
        return LocationLatitude;
    }

    public boolean isLoadingAppointmentRequired() {
        return IsLoadingAppointmentRequired;
    }

    public boolean isPickUpAppointmentRequired() {
        return IsPickUpAppointmentRequired;
    }

    public boolean isReturnAppointmentRequired() {
        return IsReturnAppointmentRequired;
    }

    public String getRefNumber() {
        return RefNumber;
    }

    public String getPONumber() {
        return PONumber;
    }


    public int getOrignTypeID() {
        return OrignTypeID;
    }

    public String getOrignAddress() {
        return OrignAddress;
    }

    public String getOrignLatitude() {
        return OrignLatitude;
    }

    public String getOrignLogitude() {
        return OrignLogitude;
    }

    public int getDestinationTypeID() {
        return DestinationTypeID;
    }

    public String getDestinationAddress() {
        return DestinationAddress;
    }

    public String getDestinationLatitude() {
        return DestinationLatitude;
    }

    public String getDestinationLogitude() {
        return DestinationLogitude;
    }


    public String getLocationTypeID() {
        return LocationTypeID;
    }

    public String getContainerLoadTypeID() {
        return ContainerLoadTypeID;
    }

    public String getBillTO() {
        return BillTO;
    }

    public String getBillToContact() {
        return BillToContact;
    }

    public String getReservationNo() {
        return ReservationNo;
    }

    public String getDropOffReservationNo() {
        return DropOffReservationNo;
    }

    public String getShipingLineAddress() {
        return ShipingLineAddress;
    }

    public boolean isStreetReturned() {
        return IsStreetReturned;
    }

    public String getLoadingLocationId() {
        return LoadingLocationId;
    }

    public String getLegType() {
        return LegType;
    }

    public void setJobAcceptance(String jobAcceptance) {
        JobAcceptance = jobAcceptance;
    }

    public String getOrderType() {
        return OrderType;
    }

    public String getJobType() {
        return JobType;
    }

    public String getPickUpResDate() {
        return PickUpResDate;
    }

    public String getPickUpResTime() {
        return PickUpResTime;
    }

    public boolean isDriverJobSequence() {
        return isDriverJobSequence;
    }

    public boolean isTitleShown() {
        return isTitleShown;
    }



}
