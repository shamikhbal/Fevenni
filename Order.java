package com.example.pickuplaundry;

public class Order {
    private  String pickupLocation;
    private String deliveryLocation;
    private String kolej;
    private String pDay;
    private String pTime;
    private String dDay;
    private String dTime;

    private int tShirt;
    private int pants;
    private int scarf;
    private int skirt;
    private int jacket;
    private int others;
    private int washingMachine;
    private int dryers;

    private double totalPrice;


    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
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

    public String getdTime() {
        return dTime;
    }

    public void setdTime(String dTime) {
        this.dTime = dTime;
    }

    public String getdDay() {
        return dDay;
    }

    public void setdDay(String dDay) {
        this.dDay = dDay;
    }


    public int getWashingMachine() {
        return washingMachine;
    }

    public void setWashingMachine(int washingMachine) {
        this.washingMachine = washingMachine;
    }

    public int getDryers() {
        return dryers;
    }

    public void setDryers(int dryers) {
        this.dryers = dryers;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTshirt() {
        return tShirt;
    }

    public void setTshirt(int tshirt) {
        this.tShirt = tshirt;
    }

    public int getPants() {
        return pants;
    }

    public void setPants(int pants) {
        this.pants = pants;
    }

    public int getScarf() {
        return scarf;
    }

    public void setScarf(int scarf) {
        this.scarf = scarf;
    }

    public int getSkirt() {
        return skirt;
    }

    public void setSkirt(int skirt) {
        this.skirt = skirt;
    }

    public int getJacket() {
        return jacket;
    }

    public void setJacket(int jacket) {
        this.jacket = jacket;
    }

    public int getOthers() {
        return others;
    }

    public void setOthers(int others) {
        this.others = others;
    }
}
