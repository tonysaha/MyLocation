package com.smac.tushar.mylocation;

import android.Manifest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;


import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.karan.churi.PermissionManager.PermissionManager;

import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ICommunicate, GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks,LocationListener{

    private final int RC_VIDEO_APP_PERM=124;
    private boolean exit=false;
    private InterstitialAd mInterstitialAd;
    public int addCount=0;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private Location location;
    private PlaceAutocompleteFragment autocompleteFragment;

    private FloatingActionButton fabbButton;
    private NearByPlaceFragment nearByPlaceFragment;
    private MapFragment mapFragment;
    private PlaceFragment placeFragment;
    private double myLat;
    private double myLon;
    String poins;
    private  PermissionManager permission;


    private  String GOOGLE_BROWSER_API_KEY="AIzaSyAowCbmyBSJtLUGn_FKoeGuVj4Sk_z5Rfw";

    CountDownTimer countDownTimer;
    private  boolean gpsready=false;
    private LinearLayout linearLayout;

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        permission.checkResult(requestCode,permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        permission=new PermissionManager() {};
        permission.checkAndRequestPermissions(this);

        nearByPlaceFragment= new NearByPlaceFragment();
        mapFragment=new MapFragment();

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        buildGoogleApiClient();

        //Check Gps Enable Or Disable..........................................................................
        try {

            CheckStatus checkStatus=new CheckStatus();
            checkStatus.CheckGpsStatus(MainActivity.this);
            if(checkStatus.GpsStatus == true)
            {

                redyGps();
            }else {

                final AlertDialog.Builder dBuilder=new AlertDialog.Builder(this);
                dBuilder.setMessage("Do you want to enable gps")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                                //Wait for get gps location...................................
                                redyGps();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                              //  redyGps();
                                /*AlertDialog alertDialog=dBuilder.create();
                                alertDialog.setTitle("Please Gps Enable !");
                                alertDialog.show();*/

                            }
                        });

                AlertDialog alertDialog=dBuilder.create();
                alertDialog.setTitle("Gps Enable !");
                alertDialog.show();







            }

        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Please Try Agin", Toast.LENGTH_LONG).show();

        }
        //Toast.makeText(MainActivity.this, String.valueOf(myLat+"  "+myLon), Toast.LENGTH_LONG).show();
    //Floating Action bar...............................
        fabbButton=(FloatingActionButton) findViewById(R.id.fab) ;
        fabbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Change Default Location",Toast.LENGTH_SHORT).show();
                SharedPreferences preferences2 =  getSharedPreferences("NearbyData", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = preferences2.edit();

                String nearbyLat =String.valueOf(myLat);
                String nearbyLon=String.valueOf(myLon);

                prefsEditor.putString("lat",nearbyLat);
                prefsEditor.putString("lon",nearbyLon).commit();
                fabbButton.setBackgroundTintList(ColorStateList.valueOf(Color
                        .parseColor("#3A84DF")));
                SharedPreferences addPreferences=getSharedPreferences("CurrentAddress",MODE_PRIVATE);
                String address=addPreferences.getString("currentlocation","");
                autocompleteFragment.setText(addPreferences.getString("currentlocation",""));
                SharedPreferences preferencesearch =getSharedPreferences("SearchType",MODE_PRIVATE);
                String searchtype=preferencesearch.getString("searctype","");
                nearByPlaceFragment.loadRecycleView(searchtype);

            }
        });

   //....Google sugettion box.......................

        linearLayout=findViewById(R.id.place_sugetion);

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Search New Location");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
               // Log.i(TAG, "Place: " + place.getName());

                if (place != null) {
                    LatLng latLng = place.getLatLng();
                    double slat = (latLng.latitude);
                    double slan = (latLng.longitude);

                    SharedPreferences preferencesearch =getSharedPreferences("SearchType",MODE_PRIVATE);
                    String searchtype=preferencesearch.getString("searctype","");

                    SharedPreferences preferences =  getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    String type=preferences.getString("ItemId", "");

                    Toast.makeText(MainActivity.this,"Your Location set "+place.getAddress(),Toast.LENGTH_LONG).show();
                  /*  NearByPlaceFragment nearByPlaceFragment=new NearByPlaceFragment();
                    nearByPlaceFragment.onStart();*/



                  //Data save for nearby Data
                    SharedPreferences preferences2 =  getSharedPreferences("NearbyData", MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = preferences2.edit();

                    String nearbyLat =String.valueOf(slat);
                    String nearbyLon=String.valueOf(slan);

                    prefsEditor.putString("lat",nearbyLat);
                    prefsEditor.putString("lon",nearbyLon).commit();

                    //autocompleteFragment.setHint(place.getName());
                    fabbButton.setBackgroundTintList(ColorStateList.valueOf(Color
                            .parseColor("#fdfefe")));

                    nearByPlaceFragment.loadRecycleView(searchtype);




                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
               // Log.i(TAG, "An error occurred: " + status);
            }
        });




        addview();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();


    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    public void redyGps(){

        //Wait for get gps location...................................


        final ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait ! Your GPS ready....");
        progressDialog.show();
        progressDialog.closeOptionsMenu();

        final int milis=1000000;

        countDownTimer = new CountDownTimer(milis, 100) {


            @Override
            public void onTick(long millisUntilFinished) {



                // Log.d("time",String.valueOf(millisUntilFinished));
                try {
                    if (getMyLat() != 0.0) {

                        fabbButton.setBackgroundTintList(ColorStateList.valueOf(Color
                                .parseColor("#3A84DF")));
                        countDownTimer.onFinish();
                        countDownTimer.cancel();
                    } else {
                        // Log.d("time",String.valueOf(milis));
                        Log.d("start","0.0");
                    }
                }catch (Exception e){

                    progressDialog.dismiss();
                    countDownTimer.start();
                    Log.d("start","Exeption0.0");
                    System.exit(0);
                }



            }


            @Override
            public void onFinish() {


                try {

                    Log.d("start","Finished");

                    Fragment fragment = null;
                    Toolbar toolbar = findViewById(R.id.toolbar);


                    toolbar.setBackgroundColor(Color.parseColor("#4DC0B5"));
                    fragment = new PlaceFragment();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.screen_area, fragment);
                        ft.commit();

                        progressDialog.dismiss();


                        SharedPreferences preferences = getSharedPreferences("NearbyData", MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = preferences.edit();

                        String nearbyLat = String.valueOf(myLat);
                        String nearbyLon = String.valueOf(myLon);

                        prefsEditor.putString("lat", nearbyLat);
                        prefsEditor.putString("lon", nearbyLon).commit();

                    }

                }catch (Exception E){
                 //countDownTimer.onFinish();

                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Please Try Again...",Toast.LENGTH_SHORT).show();
                }
            }

        }.start();

    }
    //All Fragment detect and get visible fragment.......................................

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {



            //..Put arg for Back And data not clear...............Fragment check for back presss....................


            Fragment fragment = getVisibleFragment();
            if (fragment instanceof  MapFragment){
              //  Log.d("back",fragment.toString());


                //Get Cash memory data
                /*SharedPreferences preferences =  getSharedPreferences("AppPrefs", MODE_PRIVATE);
                String type=preferences.getString("ItemId", "");*/

               // Fragment fragment2=new NearByPlaceFragment();

                FragmentManager manager = getSupportFragmentManager();
                //For pass data...if back press ...back status


                Bundle args = new Bundle();
                args.putString("back","back");
               //args.putString("type",type);


                nearByPlaceFragment.setArguments(args);


                FragmentTransaction ft=manager.beginTransaction();
                ft.replace(R.id.screen_area, nearByPlaceFragment).commit();


            }else {


                if (exit) {
                    // finish activity

                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                } else {
                    Toast.makeText(this, "Press again to exit.",
                            Toast.LENGTH_SHORT).show();
                    exit = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exit = false;
                        }
                    }, 1 * 1000);

                }
            }



        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here...
        Fragment fragment=null;
        int id = item.getItemId();
        Toolbar toolbar=findViewById(R.id.toolbar);

      if (id == R.id.nav_place) {

          if (mInterstitialAd.isLoaded()) {
              mInterstitialAd.show();
          }
            linearLayout.setVisibility(View.GONE);

            toolbar.setBackgroundColor(Color.parseColor("#4DC0B5"));
          FragmentManager fragmentManager=getSupportFragmentManager();
          fragmentManager.beginTransaction().replace(R.id.screen_area,new PlaceFragment()).commit();

        } else if (id == R.id.nav_nearbyplace) {
          if (mInterstitialAd.isLoaded()) {
              mInterstitialAd.show();
          } else {

          }
            SharedPreferences preferences =  getSharedPreferences("NearbyData", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = preferences.edit();

            String nearbyLat =String.valueOf(myLat);
            String nearbyLon=String.valueOf(myLon);

            prefsEditor.putString("lat",nearbyLat);
            prefsEditor.putString("lon",nearbyLon).commit();



            //Floating Button....................color change.........
          fabbButton.setBackgroundTintList(ColorStateList.valueOf(Color
                  .parseColor("#3498db")));


            linearLayout.setVisibility(View.VISIBLE);
           // toolbar.setBackgroundColor(Color.parseColor("#3F51B5"));

          SharedPreferences addPreferences=getSharedPreferences("CurrentAddress",MODE_PRIVATE);
          String address=addPreferences.getString("currentlocation","");
          autocompleteFragment.setText(addPreferences.getString("currentlocation",""));
         // autocompleteFragment.setText("");

          MyAdapter myAdapter=new MyAdapter();
          myAdapter.setHasStableIds(false);
          myAdapter.notifyDataSetChanged();


            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.screen_area,nearByPlaceFragment).commit();
            Toast.makeText(MainActivity.this,"Set Your Current Location",Toast.LENGTH_LONG).show();



           // toolbar.setBackgroundColor(Color.parseColor("#3F51B5"));
        } else if (id == R.id.nav_send) {
          if (mInterstitialAd.isLoaded()) {
              mInterstitialAd.show();
          } else {

          }
            //linearLayout.setVisibility(View.GONE);
           // toolbar.setBackgroundColor(Color.parseColor("#3F51B5"));
          /*toolbar.setBackgroundColor(Color.parseColor("#4DC0B5"));
          fragment=new PlaceFragment();*/

     /*     Toolbar tb=findViewById(R.id.toolbar);
          setSupportActionBar(tb);
          ActionBar actionBar=getSupportActionBar();
          actionBar.setDisplayHomeAsUpEnabled(true);
          actionBar.setTitle("Settings");

          try {
              startActivity(new Intent(Intent.ACTION_VIEW,
                      Uri.parse("market://details?id=faisal.com.bdcashquiz")));
          }catch (ActivityNotFoundException e){

              startActivity(new Intent(Intent.ACTION_VIEW,
                      Uri.parse("https://play.google.com/store/apps/details?id=faisal.com.bdcashquiz")));
          }
          toolbar.setBackgroundColor(Color.parseColor("#4DC0B5"));*/
          Intent intent=new Intent(Intent.ACTION_SEND);
          intent.setType("text/plain");
          //LatLng point=new LatLng(myLat,myLon);
          String uri = "http://maps.google.com/maps/search/?api=1&query=" + myLat + ","
                  +myLon;

          Uri point=Uri.parse(uri);
          Log.d("sendl",point.toString());


          String sharebody=("SMAC TECHNOLOGY"+"\n"+point+"\nThanks for using \n http://smactechnology.net ");

                  String shareSub=getString(R.string.app_name);

          intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
          intent.putExtra(Intent.EXTRA_TEXT,sharebody);
          startActivity(Intent.createChooser(intent,"Share Using"));


          fragment=null;


        }


//        if(fragment!=null){
//            FragmentManager fragmentManager=getSupportFragmentManager();
//            FragmentTransaction ft=fragmentManager.beginTransaction();
//            ft.replace(R.id.screen_area,fragment);
//            ft.commit();
//
//
//        }
//        else{
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
//            return true;
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        try {
            locationRequest = LocationRequest.create()
                    .setInterval(1000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {






                    Toast.makeText(MainActivity.this, "Set Allow For Permission", Toast.LENGTH_LONG).show();

                return;
            }



            //location=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    locationRequest, this);





        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Location not found", Toast.LENGTH_LONG).show();
        }
    }


    public double getMyLat() {
        return myLat;
    }

    public double getMyLon() {
        return myLon;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        myLat=location.getLatitude();
        myLon=  location.getLongitude();

       /* LocationReq locationRequest=new LocationReq();
        locationRequest.setLat(Double.valueOf(myLat));
        locationRequest.setLon(Double.valueOf(myLon));*/
       // Toast.makeText(MainActivity.this, String.valueOf(myLat+"   "+myLon), Toast.LENGTH_LONG).show();


        //Toast.makeText(MainActivity.this, String.valueOf(myLat+"  "+myLon), Toast.LENGTH_LONG).show();





    }

    private InterstitialAd addview(){
        MobileAds.initialize(MainActivity.this, getString(R.string.admob_id));


        mInterstitialAd = new InterstitialAd(MainActivity.this);

        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712" +
                "");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                // Log.d("Add", "Not Ok.");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                // Log.d("Add", "Append.");
                addCount++;
            }

            @Override
            public void onAdClicked() {


                addCount++;
                Log.d("count", String.valueOf(addCount));


            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // Log.d("Add", "Load.");
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if(addCount<1){
                mInterstitialAd.loadAd(new AdRequest.Builder().build());

                }
            }

        });

        return mInterstitialAd;

    }


    @Override
    public void updateFrag(Boolean flag) {
        if(flag==false)
            fabbButton.setVisibility(View.GONE);
        else
            fabbButton.setVisibility(View.VISIBLE);
    }
}