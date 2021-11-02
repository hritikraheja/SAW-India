package com.example.saw_india.modalClasses;

import androidx.annotation.NonNull;

public class ShelterRequest {
    private String personName;
    private String personMobileNumber;
    private String personEmail;
    private double latitude;
    private double longitude;

    public ShelterRequest() {
    }

    public ShelterRequest(String personName, String personMobileNumber, String personEmail, double latitude, double longitude) {
        this.personName = personName;
        this.personMobileNumber = personMobileNumber;
        this.personEmail = personEmail;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPersonName() {
        return personName;
    }

    public String getPersonMobileNumber() {
        return personMobileNumber;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
