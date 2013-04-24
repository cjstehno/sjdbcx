package com.stehno.fixture;

/**
 * Simple object used to test nested property extraction.
 */
public class Region {

    private GeoCoordinates geoCoordinates;

    public Region( GeoCoordinates geoCoordinates ){
        this.geoCoordinates = geoCoordinates;
    }

    public GeoCoordinates getGeoCoordinates(){
        return geoCoordinates;
    }

    public void setGeoCoordinates( final GeoCoordinates geoCoordinates ){
        this.geoCoordinates = geoCoordinates;
    }
}
