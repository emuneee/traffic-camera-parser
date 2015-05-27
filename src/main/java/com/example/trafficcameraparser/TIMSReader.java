package com.example.trafficcameraparser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by evan on 5/26/15.
 */
public class TIMSReader {

    private static final String TIMS_URL = "http://tims.ncdot.gov/TIMS/RSS/CameraGeoRSS.aspx?" +
            "TLatitude=37.02886944696474&TLongitude=-87.82470703125&BLatitude=31.58789446407041&BLongitude=-69.774169921875";

    /**
     * Calls NC DOT TIMS traffic camera XML feed
     * @return list of traffic cameras
     */
    public static List<TrafficCamera> getTrafficCameras() {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        List<TrafficCamera> trafficCameras = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        XMLStreamReader streamReader = null;

        try {
            // Create HTTP URL connection to TIMS
            URL url = new URL(TIMS_URL);
            connection = (HttpURLConnection) url.openConnection();
            inputStream = connection.getInputStream();

            // create XML Stream Reader and have it parsed for traffic cameras
            streamReader = xmlInputFactory.createXMLStreamReader(inputStream);
            trafficCameras = processStreamReader(streamReader);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            // let's do some cleanup
            if (connection != null) {
                connection.disconnect();
            }

            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (streamReader != null) {
                    streamReader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("Error occurred while connecting to URL or closing resources");
            } catch (XMLStreamException e) {
                throw new RuntimeException("Error occurred while parsing XML or closing resources");
            }
        }
        return trafficCameras;
    }

    private static List<TrafficCamera> processStreamReader(XMLStreamReader streamReader) throws XMLStreamException {
        boolean readingItem = false;
        List<TrafficCamera> trafficCameras = new LinkedList<>();
        TrafficCamera trafficCamera = null;
        StringBuilder characters = new StringBuilder();

        while (streamReader.hasNext()) {
            int event = streamReader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    characters.setLength(0);

                    if (streamReader.getLocalName().equals("item")) {
                        readingItem = true;
                        trafficCamera = new TrafficCamera();
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (readingItem) {
                        characters.append(streamReader.getText().trim());
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (readingItem) {
                        String localName = streamReader.getLocalName();

                        if (localName.equals("item")) {
                            readingItem = false;

                            if (trafficCamera.getLatitude() != TrafficCamera.NO_VAL &&
                                    trafficCamera.getLongitude() != TrafficCamera.NO_VAL) {
                                String formattedAddress = GeocodeHelper.getFormattedAddress(
                                        trafficCamera.getLatitude(), trafficCamera.getLongitude());
                                trafficCameras.add(trafficCamera);
                            }
                            trafficCamera = null;
                        }

                        else if (localName.equals("title")) {
                            trafficCamera.setTitle(characters.toString());
                        }

                        else if (localName.equals("description")) {
                            String cameraImageUrl = parseCameraImageUrl(characters.toString());

                            if (cameraImageUrl != null) {
                                trafficCamera.setCameraUrl(cameraImageUrl);
                            }
                        }

                        else if (localName.equals("point")) {
                            float[] latLng = parseLatLng(characters.toString());

                            if (latLng != null) {
                                trafficCamera.setLatitude(latLng[0]);
                                trafficCamera.setLongitude(latLng[1]);
                            }
                        }
                    }
                    break;
            }
        }
        return trafficCameras;
    }

    private static float[] parseLatLng(String latLngStr) {

        if (latLngStr == null || latLngStr.length() == 0) {
            return null;
        }

        String[] latLngStrArr = latLngStr.split(" ");

        if (latLngStrArr.length != 2) {
            return null;
        }

        return new float[] { Float.parseFloat(latLngStrArr[0]), Float.parseFloat(latLngStrArr[1]) };
    }

    private static String parseCameraImageUrl(String descriptionStr) {

        if (descriptionStr == null || descriptionStr.length() == 0 ) {
            return null;
        }

        int start = descriptionStr.indexOf(".open('") + 7;
        int end = descriptionStr.indexOf("'", start);

        if (start == -1 || end == -1) {
            return null;
        }

        return descriptionStr.substring(start, end).toLowerCase();
    }
}