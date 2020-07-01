package com.cu.gerbagecollection.model;

public class OrderItem {
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
            ContactNo;

    public OrderItem(String requestId, String userId, String lat, String aLong, String address, String garbageType, String totalAmount, String requestStatus, String createdDate, String name, String contactNo) {
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
}
