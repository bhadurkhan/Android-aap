package com.cu.gerbagecollection.model;

public class OrderDetailItem {
    private String RequestId,
            UserId,
            Lat,
            Long,
            Address,
            GarbageType,
            TotalAmount,
            RequestStatus,
            CreatedDate,
            Name,
            ContactNo,
            Date,
            RequestDetailId,
            RequestDetailStatus,
            Time;

    public OrderDetailItem(String requestId, String userId, String lat, String aLong, String address, String garbageType, String totalAmount, String requestStatus, String createdDate, String name, String contactNo, String date, String requestDetailId, String requestDetailStatus, String time) {
        RequestId = requestId;
        UserId = userId;
        Lat = lat;
        Long = aLong;
        Address = address;
        GarbageType = garbageType;
        TotalAmount = totalAmount;
        RequestStatus = requestStatus;
        CreatedDate = createdDate;
        Name = name;
        ContactNo = contactNo;
        Date = date;
        RequestDetailId = requestDetailId;
        RequestDetailStatus = requestDetailStatus;
        Time = time;
    }

    public String getRequestId() {
        return RequestId;
    }

    public String getUserId() {
        return UserId;
    }

    public String getLat() {
        return Lat;
    }

    public String getLong() {
        return Long;
    }

    public String getAddress() {
        return Address;
    }

    public String getGarbageType() {
        return GarbageType;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public String getRequestStatus() {
        return RequestStatus;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public String getName() {
        return Name;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public String getDate() {
        return Date;
    }

    public String getRequestDetailId() {
        return RequestDetailId;
    }

    public String getRequestDetailStatus() {
        return RequestDetailStatus;
    }

    public String getTime() {
        return Time;
    }
}
