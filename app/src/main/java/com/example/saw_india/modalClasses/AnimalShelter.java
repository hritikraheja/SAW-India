package com.example.saw_india.modalClasses;

public class AnimalShelter {
    String name;
    double lat;
    double lng;
    String address;
    String phoneNumber;

    public AnimalShelter(String name, double lat, double lng, String address) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Name : " + name + ", Phone Number : " + phoneNumber + ", Address : " + address + "\n\n" ;
    }

    public AnimalShelter(String name, double lat, double lng, String address, String phoneNumber) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
