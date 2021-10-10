package com.example.saw_india.modalClasses;

public class Helper {
    private String helperName;
    private String helperMobileNumber;
    private String helperEmail;
    private String helperOrganisationName;
    private String placeId;

    public Helper() {
    }

    public Helper(String helperName, String helperMobileNumber, String helperEmail, String helperOrganisationName, String placeId) {
        this.helperName = helperName;
        this.helperMobileNumber = helperMobileNumber;
        this.helperEmail = helperEmail;
        this.helperOrganisationName = helperOrganisationName;
        this.placeId = placeId;
    }

    public String getHelperName() {
        return helperName;
    }

    public String getHelperMobileNumber() {
        return helperMobileNumber;
    }

    public String getHelperEmail() {
        return helperEmail;
    }

    public String getHelperOrganisationName() {
        return helperOrganisationName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setHelperName(String helperName) {
        this.helperName = helperName;
    }

    public void setHelperMobileNumber(String helperMobileNumber) {
        this.helperMobileNumber = helperMobileNumber;
    }

    public void setHelperEmail(String helperEmail) {
        this.helperEmail = helperEmail;
    }

    public void setHelperOrganisationName(String helperOrganisationName) {
        this.helperOrganisationName = helperOrganisationName;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    public String toString() {
        return "Helper{" +
                "helperName='" + helperName + '\'' +
                ", helperMobileNumber='" + helperMobileNumber + '\'' +
                ", helperEmail='" + helperEmail + '\'' +
                ", helperOrganisationName='" + helperOrganisationName + '\'' +
                ", placeId='" + placeId + '\'' +
                '}';
    }
}
