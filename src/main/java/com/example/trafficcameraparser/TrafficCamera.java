package com.example.trafficcameraparser;

/**
 * Created by evan on 5/26/15.
 */
public class TrafficCamera {

    public static final float NO_VAL = 1000f;

    private String mTitle;
    private String mAddress;
    private String mCameraUrl;
    private float mLatitude = NO_VAL;
    private float mLongitude = NO_VAL;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public float getLatitude() {
        return mLatitude;
    }

    public void setLatitude(float latitude) {
        mLatitude = latitude;
    }

    public float getLongitude() {
        return mLongitude;
    }

    public void setLongitude(float longitude) {
        mLongitude = longitude;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getCameraUrl() {
        return mCameraUrl;
    }

    public void setCameraUrl(String cameraUrl) {
        mCameraUrl = cameraUrl;
    }

    @Override
    public String toString() {
        return "TrafficCamera{" +
                "mAddress='" + mAddress + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mCameraUrl='" + mCameraUrl + '\'' +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                '}';
    }
}
