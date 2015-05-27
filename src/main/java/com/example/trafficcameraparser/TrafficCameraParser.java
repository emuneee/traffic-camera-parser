package com.example.trafficcameraparser;

import java.util.List;

/**
 * Created by evan on 5/26/15.
 */
public class TrafficCameraParser {

    private static final long SLEEP_TIME_MS = 200;

    public static void main(String[] args) throws InterruptedException {

        // get traffic camera objects from the TIMS feed
        List<TrafficCamera> trafficCameras = TIMSReader.getTrafficCameras();

        // for each camera, using it's lat/long to get it's address
        for (TrafficCamera trafficCamera : trafficCameras) {
            String formattedAddress = GeocodeHelper.getFormattedAddress(
                    trafficCamera.getLatitude(), trafficCamera.getLongitude());

            if (formattedAddress != null) {
                trafficCamera.setAddress(formattedAddress);
            }

            // sleep after each request so we respect usage limits of the API
            Thread.sleep(SLEEP_TIME_MS);
        }

        // persist our traffic cameras in a database

    }

}
