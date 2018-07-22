package com.smac.tushar.mylocation;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceFragment extends Fragment {
    private AdView mAdView;


    private Geocoder mGeocoder;

    private TextView addressTV,cityTv,subCTv,postcodeTv,divisionTv,countryTv,countrycodeTv;


    public PlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity activity= (MainActivity) getActivity();
        activity.updateFrag(false);
        return inflater.inflate(R.layout.fragment_place, container, false);



    }

    @Override
    public void onDetach() {
        super.onDetach();
        MainActivity activity= (MainActivity) getActivity();
        activity.updateFrag(true);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MobileAds.initialize(getActivity(), getString(R.string.admob_id));

        addressTV=view.findViewById(R.id.addressTV);
        cityTv=view.findViewById(R.id.cityTv);
        subCTv=view.findViewById(R.id.subcityTv);
        postcodeTv=view.findViewById(R.id.postcodeTv);
        divisionTv=view.findViewById(R.id.divisionTv);
        countryTv=view.findViewById(R.id.countryTv);
        countrycodeTv=view.findViewById(R.id.countycodeTv);
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        try {


    MainActivity activity = (MainActivity) getActivity();
    activity.getMyLat();

    LocationReq locatio = new LocationReq();
    locatio.setLat(Double.valueOf(activity.getMyLat()));
    locatio.setLon(Double.valueOf(activity.getMyLon()));
    Toast.makeText(getContext(), String.valueOf(locatio.getLat()) + " " + String.valueOf(locatio.getLon()), Toast.LENGTH_SHORT).show();

    mGeocoder = new Geocoder(getContext());
    locatio.address(mGeocoder);

    addressTV.setText(": "+locatio.getAdress());
    cityTv.setText(": "+locatio.getCity());
    subCTv.setText(": "+locatio.getSubCity());
    postcodeTv.setText(": "+locatio.getPostCode());
    divisionTv.setText(": "+locatio.getDivision());
    countryTv.setText(": "+locatio.getCountry());
    countrycodeTv.setText(": "+locatio.getCountrycode());

    SharedPreferences addPreferences=this.getActivity().getSharedPreferences("CurrentAddress", Context.MODE_PRIVATE);
    SharedPreferences.Editor addpreEditor=addPreferences.edit();
    addpreEditor.putString("currentlocation",locatio.getAdress()).commit();
}catch (Exception e){
    Toast.makeText(getActivity(),"Adrees not found try again ",Toast.LENGTH_SHORT).show();

}


    }
}
