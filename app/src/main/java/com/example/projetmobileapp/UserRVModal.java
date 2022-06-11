package com.example.projetmobileapp;

import android.os.Parcel;
import android.os.Parcelable;
public class UserRVModal implements Parcelable {
    // creating variables.
    private String userName;
    private String userDescription;
    private String userAge;
    private String userRole;
    private String userLink;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Empty constructor.
    public UserRVModal() {

    }

    protected UserRVModal(Parcel in) {
        userName = in.readString();
        userId = in.readString();
        userDescription = in.readString();
        userAge = in.readString();
        userRole = in.readString();
        userLink = in.readString();
    }

    public static final Creator<UserRVModal> CREATOR = new Creator<UserRVModal>() {
        @Override
        public UserRVModal createFromParcel(Parcel in) {
            return new UserRVModal(in);
        }

        @Override
        public UserRVModal[] newArray(int size) {
            return new UserRVModal[size];
        }
    };

    // creating getter and setter methods.
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserLink() {
        return userLink;
    }

    public void setUserLink(String userLink) {
        this.userLink = userLink;
    }


    public UserRVModal(String userId, String userName, String userDescription, String userAge, String userRole, String userLink) {
        this.userName = userName;
        this.userId = userId;
        this.userDescription = userDescription;
        this.userAge = userAge;
        this.userRole = userRole;
        this.userLink = userLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userId);
        dest.writeString(userDescription);
        dest.writeString(userAge);
        dest.writeString(userRole);
        dest.writeString(userLink);
    }
}
