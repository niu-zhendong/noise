package com.cetc.noise.model;

import lombok.Data;

@Data
public class Noise {
    private long dataTime;
    private float volume;
    private String point_id;
    private String point_name;
    private String location_id;
    private String location_name;
    private double longs;
    private double lat;
}