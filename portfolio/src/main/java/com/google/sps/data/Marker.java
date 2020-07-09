package com.google.sps.data;

/** Represents a marker on the map. */
public class Marker {
  final String name;
  final String description;
  final double lat;
  final double lng;

  public Marker(String name, String description, double lat, double lng) {
    this.name = name;
    this.description = description;
    this.lat = lat;
    this.lng = lng;
  }

  public double getLat() {
    return lat;
  }

  public double getLng() {
    return lng;
  }

  public String getDescription() {
    return description;
  }

  public String getName() {
      return name;
  }
}