package com.example.saw_india.modalClasses;

public class AnimalShelter implements Comparable<AnimalShelter>{
    String name;
    double lat;
    double lng;
    String address;
    String phoneNumber;
    int phoneNumberAvailableOrNot;
    String placeId;

    public AnimalShelter(String name, double lat, double lng, String address, String placeId) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.placeId = placeId;
    }

    public void setPhoneNumberAvailableOrNot(int phoneNumberAvailableOrNot) {
        this.phoneNumberAvailableOrNot = phoneNumberAvailableOrNot;
    }

    @Override
    public String toString() {
        return "Name : " + name + ", Phone Number : " + phoneNumber + ", Address : " + address + "\n\n" ;
    }

    public AnimalShelter(String name, double lat, double lng, String address, String phoneNumber, String placeId, int phoneNumberAvailableOrNot) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.phoneNumberAvailableOrNot = phoneNumberAvailableOrNot;
        this.placeId = placeId;
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

    public String getPlaceId() {
        return placeId;
    }

    @Override
    public int compareTo(AnimalShelter o) {
        return Integer.compare(o.phoneNumberAvailableOrNot, this.phoneNumberAvailableOrNot);
    }
}
