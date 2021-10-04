package com.example.saw_india.modalClasses;

public class Donation implements Comparable<Donation>{
    private String donorName;
    private String donorMobileNumber;
    private String donorEmail;
    private String dateAndTime;
    private String donationAmount;
    private String transactionID;
    private String status;

    public Donation() {
    }

    public Donation(String donorName, String donorMobileNumber, String donorEmail, String dateAndTime,String amount, String transactionID, String status) {
        this.donorName = donorName;
        this.donorMobileNumber = donorMobileNumber;
        this.donorEmail = donorEmail;
        this.dateAndTime = dateAndTime;
        this.donationAmount = amount;
        this.transactionID = transactionID;
        this.status = status;
    }

    public String getDonorName() {
        return donorName;
    }

    public String getDonorMobileNumber() {
        return donorMobileNumber;
    }

    public String getDonorEmail() {
        return donorEmail;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getDonationAmount() {
        return donationAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getTransactionID() {
        return transactionID;
    }

    @Override
    public String toString() {
        return "Donation{" +
                "donorName='" + donorName + '\'' +
                ", donorMobileNumber='" + donorMobileNumber + '\'' +
                ", donorEmail='" + donorEmail + '\'' +
                ", dateAndTime='" + dateAndTime + '\'' +
                ", amount='" + donationAmount + '\'' +
                ", transactionID='" + transactionID + '\'' +
                '}';
    }

    @Override
    public int compareTo(Donation o) {
        return this.dateAndTime.compareTo(o.dateAndTime);
    }
}
