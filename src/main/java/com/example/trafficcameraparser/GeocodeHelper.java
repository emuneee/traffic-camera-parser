package com.example.trafficcameraparser;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by evan on 5/27/15.
 */
public class GeocodeHelper {

    private static final String GEOCODE_BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String LATLNG_PARAM = "latlng";

    private static final float MIN_LATITUDE = -90f;
    private static final float MAX_LATITUDE = 90f;
    private static final float MIN_LONGITUDE = -180f;
    private static final float MAX_LONGITUDE = 180f;

    /**
     * Returns a formatted street address for the latitude and longitude
     * @param latitude
     * @param longitude
     * @return
     */
    public static String getFormattedAddress(float latitude, float longitude) {
        String formattedAddress = null;

        if (latitude > MAX_LATITUDE || latitude < MIN_LATITUDE) {
            throw new IllegalArgumentException(String.format("Latitude %f is out of range", latitude));
        }

        if (longitude > MAX_LONGITUDE || longitude < MIN_LONGITUDE) {
            throw new IllegalArgumentException(String.format("Longitude %f is out of range", longitude));
        }

        String url = new StringBuilder(GEOCODE_BASE_URL)
                .append("?")
                .append(LATLNG_PARAM)
                .append("=")
                .append(latitude)
                .append(",")
                .append(longitude).toString();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();

            if (data != null) {
                JSONObject jsonObject = new JSONObject(data);

                if (jsonObject.has("results") && jsonObject.getJSONArray("results").length() > 0 &&
                        jsonObject.getJSONArray("results").getJSONObject(0).has("formatted_address")) {
                    formattedAddress = jsonObject.getJSONArray("results")
                            .getJSONObject(0)
                            .getString("formatted_address");
                }
            }
        } catch (IOException e) {

        }
        return formattedAddress;
    }
}
