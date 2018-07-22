package com.smac.tushar.mylocation;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class CheckStatus extends AppCompatActivity {

    boolean GpsStatus;

    public void CheckGpsStatus(Context context){
        LocationManager manager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE );
        GpsStatus = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);



    }





}
