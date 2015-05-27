package com.example.trafficcameraparser;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

/**
 * Created by evan on 5/27/15.
 */
public class DatabaseHelper {

    private static final String MONGO_CONN_URI = "mongodb://localhost:27017";
    private static final String DATABASE = "traffic_camera_parser";
    private static final String COLLECTION = "camera";

    public static void insertTrafficCameras(List<TrafficCamera> trafficCameras) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(MONGO_CONN_URI));
        MongoDatabase database = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> collection = database.getCollection(COLLECTION);

        // TODO insert document
    }

}
