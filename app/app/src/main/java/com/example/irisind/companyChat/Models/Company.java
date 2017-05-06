package com.example.irisind.companyChat.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by irisind on 28/4/17.
 */

public class Company {
    @SerializedName("_id")
    private String id;
    private String companyName;
    private String subAccountName;
    private String profilePic;
    private String[] followers;
    private String phoneNumber;
    private String userType;
    private String place;
    private String city;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSubAccountName() {
        return subAccountName;
    }

    public void setSubAccountName(String subAccountName) {
        this.subAccountName = subAccountName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String[] getFollowers() {
        return followers;
    }

    public void setFollowers(String[] followers) {
        this.followers = followers;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isProfileComplete() {
        if (companyName.length() > 0 && subAccountName.length() > 0 && place.length() > 0) {
            return true;
        }
        return false;
    }
}
