package com.grupa2.przewodnikturystyczny;

/**
 * Created by KPC on 2017-02-02.
 */
public class Attraction {
    private String mName;
    private String mAdress;
    private String mShortDesctription;
    private String mFullDescription;
    private String mPhoto1;
    private String mPhoto2;
    private String mPhoto3;
    private Integer[] mPhotos;

    public Attraction(String name, String adress, String shortDesctription, String fullDescription,  Integer[] photos) {
        this.mName = name;
        this.mAdress = adress;
        this.mShortDesctription = shortDesctription;
        this.mFullDescription = fullDescription;
        this.mPhotos = photos;
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

    public Integer[] getPhotos() { return mPhotos;}

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

    public void setmShortDesctription(String mShortDesctription) {
        this.mShortDesctription = mShortDesctription;
    }

    public void setmPhoto(int position, int value){
        mPhotos[position] = value;
    }

}
