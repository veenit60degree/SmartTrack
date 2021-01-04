package com.tracking.constants;

import android.content.Context;

public class APIs {

    Context context;
    public static String API_BASE_URL = "";
    public String LOCAL_SERVER = "http://182.73.78.171:8266/api/integrate/";
    public String PRODUCTION_SERVER = "http://api.alscontainers.com/api/integrate/";


    public String LOGIN;
    public String Validate_OTP;
    public String GET_CONTAINER_LIST;
    public String GET_CONTAINER_DETAIL;
    public String UPDATE_READ_STATUS;
    public String UPDATE_PICKEDUP_CONTAINER;
    public String UPDATE_CONTAINER_STATUS;
    public String GET_NEXT_LOCATION;
    public String UPDATE_CONTAINER_LOADTYPE;
    public String UPLOAD_DELIVERY_DOCUMENT;
    public String UPDATE_CONTNR_STATUS_IMPORT ;
    public String UPDATE_CONTNR_LOADTYPE_IMPORT ;
    public String UPDATE_PICK_CONTNR_IMPORT ;
    public String UPLOAD_DELIVERY_DOC_IMPORT;
    public String UPDATE_CHASSIS_NO ;
    public String UPDATE_CONTAINER_NO ;
    public String GET_UNASSIGN_CONTAINER ;
    public String GET_DRIVER_TRUCK_CHASSI ;
    public String UPDATE_SELFASSIGN;
    public String UPDATE_REMARKS;
    public String LOGOUT ;
    public String JOB_Acceptance ;
    public String Get_Company_Setting ;
    public String GET_ASSIGNED_JOBS  ;
    public String GET_SUB_LOCATIONS ;
    public String GET_DRIVER_DETAIL ;
    public String UPLOAD_DRIVER_IMAGE;
    public String SET_DEFAULT_TRUCK;
    public String SET_DRIVER_READY_STATUS ;

    public String UPDATE_BACK_TO_YARD_EVENT;
    public String GET_BACK_TO_YARD_EVENT ;
    public String SET_DRIVER_LOGOUT_TIME;

    public APIs(Context context){
        this.context = context;

        if(Constants.IsApiUrlLocal(context)){
            API_BASE_URL = LOCAL_SERVER;
        }else{
            API_BASE_URL = PRODUCTION_SERVER;
        }

       // API_BASE_URL = LOCAL_SERVER;

        LOGIN                          = API_BASE_URL + "ValidateUser";
        Validate_OTP                   = API_BASE_URL + "ValidateOTP";
        GET_CONTAINER_LIST             = API_BASE_URL + "GetContainerList";
        GET_CONTAINER_DETAIL           = API_BASE_URL + "GetContainerDetail";
        UPDATE_READ_STATUS             = API_BASE_URL + "UpdateReadStausContainer";
        UPDATE_PICKEDUP_CONTAINER      = API_BASE_URL + "UpdatePickedContainer";
        UPDATE_CONTAINER_STATUS        = API_BASE_URL + "UpdateContainerStatus";
        GET_NEXT_LOCATION              = API_BASE_URL + "GetNextLocation";
        UPDATE_CONTAINER_LOADTYPE      = API_BASE_URL + "UpdateContainerLoadType";
        UPLOAD_DELIVERY_DOCUMENT       = API_BASE_URL + "UploadDeliveryDocument";
        UPDATE_CONTNR_STATUS_IMPORT    = API_BASE_URL + "UpdateContainerStatusForImport";
        UPDATE_CONTNR_LOADTYPE_IMPORT  = API_BASE_URL + "UpdateContainerLoadTypeForImport";
        UPDATE_PICK_CONTNR_IMPORT      = API_BASE_URL + "UpdatePickedContainerForImport";
        UPLOAD_DELIVERY_DOC_IMPORT     = API_BASE_URL + "UploadDeliveryDocumentForImport";
        UPDATE_CHASSIS_NO              = API_BASE_URL + "UpdateChassisNo";
        UPDATE_CONTAINER_NO            = API_BASE_URL + "UpdateContainerNo";
        GET_UNASSIGN_CONTAINER         = API_BASE_URL + "GetContainersByContainerNo";
        GET_DRIVER_TRUCK_CHASSI        = API_BASE_URL + "GetDriverTruckChassiYardByCompany";
        UPDATE_SELFASSIGN              = API_BASE_URL + "SelfAssign";
        UPDATE_REMARKS                 = API_BASE_URL + "GetAndUpdateDriverRemarks";
        LOGOUT                         = API_BASE_URL + "Logout";
        JOB_Acceptance                 = API_BASE_URL + "AcceptJob";
        Get_Company_Setting            = API_BASE_URL + "GetCompanySetting";
        GET_ASSIGNED_JOBS              = API_BASE_URL + "GetAssignedJobs";
        GET_SUB_LOCATIONS              = API_BASE_URL + "GetSubLocations";
        GET_DRIVER_DETAIL              = API_BASE_URL + "GetDriverDetail";
        UPLOAD_DRIVER_IMAGE            = API_BASE_URL + "UploadDriverImage";
        SET_DEFAULT_TRUCK              = API_BASE_URL + "SetDefaulttruck";
        SET_DRIVER_READY_STATUS        = API_BASE_URL + "SetDriverReadyStatus";

        UPDATE_BACK_TO_YARD_EVENT      = API_BASE_URL + "UpdteBackToYardEvent";
        GET_BACK_TO_YARD_EVENT         = API_BASE_URL + "GetBackToYardEvent";
        SET_DRIVER_LOGOUT_TIME         = API_BASE_URL + "SetDriverLogoutTime";



    }


}
