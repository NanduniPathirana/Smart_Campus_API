package com.smartcampus.store;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Central in-memory data store for the Smart Campus API.
 * Uses ConcurrentHashMap to ensure thread-safety across concurrent requests.
 * No database is used - all data is stored in memory using data structures.
 */
public class DataStore {

    // Stores all rooms - key is room ID
    public static final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    // Stores all sensors - key is sensor ID
    public static final ConcurrentHashMap<String, Sensor> sensors = new ConcurrentHashMap<>();

    // Stores sensor readings - key is sensor ID, value is list of readings
    public static final ConcurrentHashMap<String, CopyOnWriteArrayList<SensorReading>> readings
            = new ConcurrentHashMap<>();
}