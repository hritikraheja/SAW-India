package com.example.saw_india.modalClasses;

public class User implements Comparable<User>{
    private String name;
    private String mobileNumber;
    private String email;

    public User(){

    }

    public User(String name, String mobileNumber, String email) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public int compareTo(User o) {
        return this.mobileNumber.compareTo(o.mobileNumber);
    }
}
