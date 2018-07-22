package com.smac.tushar.mylocation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;


public class LocationReq implements LocationListener {
    Context context;

    private Geocoder mGeocoder;
    private List<Address> addressList;

    private double lat;
    private double lon;
    String adress;
    String city;
    String subCity;
    String postCode;
    String country;
    String division;
    String countrycode;

    public LocationReq() {

    }

    public void LocationReq() {

    }

    public void address(Geocoder mGeocoder){

       // Log.d("Location2",String.valueOf(getLat()));
        try {
            addressList = mGeocoder.getFromLocation(getLat(),getLon(),1);
            Address address = addressList.get(0);
            this.adress=address.getThoroughfare();
            this.city=String.valueOf(address.getLocality());
            this.subCity= address.getSubLocality();
            this.postCode=address.getPostalCode();
            this.division=address.getAdminArea();
            this.country=address.getCountryName();
            this.countrycode=address.getCountryCode();

            Log.d("Location2",division +" "+country);
        } catch (IOException e) {
            Log.d("Location2","Not ok");
        }

    }

    public LocationReq(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public void onLocationChanged(Location location) {





}
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubCity() {
        return subCity;
    }

    public void setSubCity(String subCity) {
        this.subCity = subCity;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    @Override
    public String toString() {
        return "LocationRequest{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", adress='" + adress + '\'' +
                ", city='" + city + '\'' +
                ", subCity='" + subCity + '\'' +
                ", postCode='" + postCode + '\'' +
                ", country='" + country + '\'' +
                ", division='" + division + '\'' +
                ", countrycode='" + countrycode + '\'' +
                '}';
    }
}
