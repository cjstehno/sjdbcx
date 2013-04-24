package com.stehno.fixture;

/**
 * Represents a geographical location.
 */
public class GeoCoordinates {

    private double latitude, longitude;

    public GeoCoordinates( double latitude, double longitude ){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLatitude( final double latitude ){
        this.latitude = latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLongitude( final double longitude ){
        this.longitude = longitude;
    }
}
