package com.grupa2.przewodnikturystyczny;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KPC on 2017-02-02.
 */
public class Attraction implements Parcelable {
    String mName;
    String mAdress;
    String mShortDesctription;
    String mFullDescription;
    String mPhoto1;
    String mPhoto2;
    String mPhoto3;
    double mLongitude;
    double mLatitude;
    double mDistance;

    public Attraction(String name, String adress, String shortDesctription, String fullDescription,  String photo1, String photo2, String photo3, double longitude, double latitude, double distance) {
        this.mName = name;
        this.mAdress = adress;
        this.mShortDesctription = shortDesctription;
        this.mFullDescription = fullDescription;
        this.mPhoto1 = photo1;
        this.mPhoto2 = photo2;
        this.mPhoto3 = photo3;
        this.mLongitude = longitude;
        this.mLatitude = latitude;
        this.mDistance = distance;
    }

    public String getAdress() {
        return mAdress;
    }

    public String getFullDescription() {
        return mFullDescription;
    }

    public String getName() {
        return mName;
    }

    public String getPhoto1() {
        return mPhoto1;
    }

    public String getPhoto3() {
        return mPhoto3;
    }

    public String getPhoto2() {
        return mPhoto2;
    }

    public String getShortDesctription() {
        return mShortDesctription;
    }

    public void setAdress(String mAdress) {
        this.mAdress = mAdress;
    }

    public void setFullDescription(String mFullDescription) {
        this.mFullDescription = mFullDescription;
    }

    public void setPhoto1(String mPhoto1) {
        this.mPhoto1 = mPhoto1;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setPhoto2(String mPhoto2) {
        this.mPhoto2 = mPhoto2;
    }

    public void setPhoto3(String mPhoto3) {
        this.mPhoto3 = mPhoto3;
    }

    public void setShortDesctription(String mShortDesctription) {
        this.mShortDesctription = mShortDesctription;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getDistance() {
        return this.mDistance;
    }

    public void setDistance(double distance) {
        this.mDistance = distance;
    }

    protected Attraction(Parcel in) {
        mName = in.readString();
        mAdress = in.readString();
        mShortDesctription = in.readString();
        mFullDescription = in.readString();
        mPhoto1 = in.readString();
        mPhoto2 = in.readString();
        mPhoto3 = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mDistance = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mAdress);
        dest.writeString(mShortDesctription);
        dest.writeString(mFullDescription);
        dest.writeString(mPhoto1);
        dest.writeString(mPhoto2);
        dest.writeString(mPhoto3);
        dest.writeDouble(mLongitude);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mDistance);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Attraction> CREATOR = new Parcelable.Creator<Attraction>() {
        @Override
        public Attraction createFromParcel(Parcel in) {
            return new Attraction(in);
        }

        @Override
        public Attraction[] newArray(int size) {
            return new Attraction[size];
        }
    };
}