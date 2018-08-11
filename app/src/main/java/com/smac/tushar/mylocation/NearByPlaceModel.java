package com.smac.tushar.mylocation;

public class NearByPlaceModel {
    private String name;
    private String address;
    private String phone;
    private String lat;
    private String lan;
    private String type;
    private String distance;
    private String placeId;


    public NearByPlaceModel(String name, String address, String lat, String lan,String type,String distance,String place_id) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lan = lan;
        this.type=type;
        this.distance=distance;
        this.placeId=place_id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    @Override
    public String toString() {
        return "NearByPlaceModel{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lan + '\'' +
                '}';
    }
}
