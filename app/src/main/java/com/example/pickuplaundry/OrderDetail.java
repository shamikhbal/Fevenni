package com.example.pickuplaundry;

public class OrderDetail {

//    private String orderId;

    private String pDay;
    private String pTime;
    private String pickupLocation;
    private String dDay;
    private String dTime;
    private String deliveryLocation;
    private String kolej;

    public OrderDetail() {
    }

    public OrderDetail(String pDay, String pTime, String pickupLocation, String dDay, String dTime, String deliveryLocation, String kolej) {
        this.pDay = pDay;
        this.pTime = pTime;
        this.pickupLocation = pickupLocation;
        this.dDay = dDay;
        this.dTime = dTime;
        this.deliveryLocation = deliveryLocation;
        this.kolej = kolej;
    }

//    public String getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(String orderId) {
//        this.orderId = orderId;
//    }

    public String getpDay() {
        return pDay;
    }

    public void setpDay(String pDay) {
        this.pDay = pDay;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getdDay() {
        return dDay;
    }

    public void setdDay(String dDay) {
        this.dDay = dDay;
    }

    public String getdTime() {
        return dTime;
    }

    public void setdTime(String dTime) {
        this.dTime = dTime;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public String getKolej() {
        return kolej;
    }

    public void setKolej(String kolej) {
        this.kolej = kolej;
    }
}