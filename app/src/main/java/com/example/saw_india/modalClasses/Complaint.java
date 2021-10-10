package com.example.saw_india.modalClasses;

import java.util.Calendar;

public class Complaint {
    String complaineeName;
    String complaineeMobileNumber;
    String complaineeEmail;
    double animalLocationLat;
    double animalLocationLng;
    String imageUrl;
    String complaintDescription;
    String status;
    String dateAndTime;
    Helper allocatedHelper;

    public Complaint() {
    }

    public Complaint(String complaineeName, String complaineeMobileNumber, String complaineeEmail, double animalLocationLat, double animalLocationLng, String imageUrl, String complaintDescription) {
        this.complaineeName = complaineeName;
        this.complaineeMobileNumber = complaineeMobileNumber;
        this.complaineeEmail = complaineeEmail;
        this.animalLocationLat = animalLocationLat;
        this.animalLocationLng = animalLocationLng;
        this.imageUrl = imageUrl;
        this.complaintDescription = complaintDescription;
        this.status = "Received";
        dateAndTime = Calendar.getInstance().getTime().toString();
        allocatedHelper = new Helper();
        allocatedHelper.setHelperName("Not Assigned Yet");
        allocatedHelper.setHelperMobileNumber("null");
        allocatedHelper.setHelperEmail("null");
        allocatedHelper.setHelperOrganisationName("null");
        allocatedHelper.setPlaceId("null");
    }

    public String getComplaineeName() {
        return complaineeName;
    }

    public String getComplaineeMobileNumber() {
        return complaineeMobileNumber;
    }

    public String getComplaineeEmail() {
        return complaineeEmail;
    }

    public double getAnimalLocationLat() {
        return animalLocationLat;
    }

    public double getAnimalLocationLng() {
        return animalLocationLng;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getComplaintDescription() {
        return complaintDescription;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getStatus() {
        return status;
    }

    public Helper getAllocatedHelper() {
        return allocatedHelper;
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "complaineeName='" + complaineeName + '\'' +
                ", complaineeMobileNumber='" + complaineeMobileNumber + '\'' +
                ", complaineeEmail='" + complaineeEmail + '\'' +
                ", animalLocationLat=" + animalLocationLat +
                ", animalLocationLng=" + animalLocationLng +
                ", imageUrl='" + imageUrl + '\'' +
                ", complaintDescription='" + complaintDescription + '\'' +
                ", status='" + status + '\'' +
                ", allocatedHelper=" + allocatedHelper +
                '}';
    }
}
