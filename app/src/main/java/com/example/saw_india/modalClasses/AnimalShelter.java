package com.example.saw_india.modalClasses;

public class AnimalShelter implements Comparable<AnimalShelter>{
    String name;
    double lat;
    double lng;
    String address;
    String phoneNumber;
    int phoneNumberAvailableOrNot;

    public AnimalShelter(String name, double lat, double lng, String address) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public void setPhoneNumberAvailableOrNot(int phoneNumberAvailableOrNot) {
        this.phoneNumberAvailableOrNot = phoneNumberAvailableOrNot;
    }

    @Override
    public String toString() {
        return "Name : " + name + ", Phone Number : " + phoneNumber + ", Address : " + address + "\n\n" ;
    }

    public AnimalShelter(String name, double lat, double lng, String address, String phoneNumber, int phoneNumberAvailableOrNot) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.phoneNumberAvailableOrNot = phoneNumberAvailableOrNot;
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

    @Override
    public int compareTo(AnimalShelter o) {
        return Integer.compare(o.phoneNumberAvailableOrNot, this.phoneNumberAvailableOrNot);
    }
}
