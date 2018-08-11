package com.smac.tushar.mylocation;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class NearByPlaceFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<NearByPlaceModel> listItems;
    private Spinner typeSp;
    public String type2;

    private View view;
    //Nearbyplace using Api...........
    private String GOOGLE_BROWSER_API_KEY = "AIzaSyAowCbmyBSJtLUGn_FKoeGuVj4Sk_z5Rfw";

    private EditText rediusET;
    private TextView instruction;


    SupportPlaceAutocompleteFragment searcher;


    RequestQueue rq;


    public NearByPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_near_by_place, container, false);
        SharedPreferences addPreferences = this.getActivity().getSharedPreferences("CurrentAddress", MODE_PRIVATE);
        String address = addPreferences.getString("currentlocation", "");

        MainActivity activity= (MainActivity) getActivity();
        activity.updateFrag(true);
        activity.searchBox(false);

        return view;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Spinner...........


        rq = Volley.newRequestQueue(getContext());
        recyclerView = view.findViewById(R.id.recyclerV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listItems = new ArrayList<>();
        rediusET = view.findViewById(R.id.rediusET);
        instruction = view.findViewById(R.id.instruction);

        MaterialSpinner  spinner = (MaterialSpinner) view.findViewById(R.id.spinner);
        spinner.setItems("Select One","Airport","ATM","Bank","Bus Station","Cafe","Car Rental","Car Repair","Church",
                "Doctor","Embassy","Gas Station","Gym","Hindu Temple","Hospital","Mosque","Movie Theater","Museum"
                ,"Park","Police","Post Office","School","Stadium","Supermarket","Train Station","Travel Agency","Zoo");

        if (listItems.isEmpty()) {
            String click;
            String redius = "1";
            try {
                SharedPreferences preferencesearch = getActivity().getSharedPreferences("HomeClick", MODE_PRIVATE);
                click = preferencesearch.getString("type", "");

                redius=preferencesearch.getString("redius","");
                spinner.setText(click);
            } catch (Exception e) {
                click = "";
            }

            if(click!=""){
                rediusET.setText(redius);
                loadRecycleView(click);
               // Toast.makeText(getContext(), "done click", Toast.LENGTH_SHORT).show();
            }

            SharedPreferences preferencesearch = getActivity().getSharedPreferences("HomeClick", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencesearch.edit();
            editor.clear();
            editor.commit();


    }


        //Check Recycle view Set Fixed Data.............................................................

       /* for(int i=0;i<=10;i++){
            NearByPlaceModel listitem=new NearByPlaceModel(
                    "Heading",
                    "Hi tony How areyou"
            );
            listItems.add(listitem);

        }
        adapter=new MyAdapter(listItems,getContext());
        recyclerView.setAdapter(adapter);*/


       //Place suggession addd.................................................................................






        spinner.setDropdownMaxHeight(600);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();

                String type;


                //Save data........
                /*SharedPreferences preferences =  getActivity().getSharedPreferences("SinnerType", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = preferences.edit();*/

   SharedPreferences preferencesearch =getActivity().getSharedPreferences("SearchType",MODE_PRIVATE);
   SharedPreferences.Editor preferencesearchE=preferencesearch.edit();

                switch (position){

                    case 1:
                        listItems.clear();
                        type="airport";
                        type2=type;
                        preferencesearchE.putString("searctype",type).commit();
                        //String vlaaue=spinner.getItems().toString();
                       // prefsEditor.putString("spinnerType", vlaaue).commit();
                        loadRecycleView(type);
                        break;
                    case 2:

                        listItems.clear();
                        type="atm";
                        type2=type;
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 3:
                        listItems.clear();
                        type="bank";
                        type2=type;
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 4:
                        listItems.clear();
                        type="bus_station";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 5:
                        listItems.clear();
                        type="cafe";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 6:
                        listItems.clear();
                        type="car_rental";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 7:
                        listItems.clear();
                        type="car_repair";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;

                    case 8:
                        listItems.clear();

                        type="church";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 9:
                        listItems.clear();

                        type="doctor";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 10:
                        listItems.clear();

                        type="embassy";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 11:
                        listItems.clear();

                        type="gas_station";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 12:
                        listItems.clear();

                        type="gym";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 13:
                        listItems.clear();
                        type="hindu_temple";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 14:
                        listItems.clear();
                        type="hospital";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 15:
                        listItems.clear();
                        type="mosque";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 16:
                        listItems.clear();
                        type="movie_theater";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 17:
                        listItems.clear();
                        type="museum";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;

                    case 18:
                        listItems.clear();
                        type="park";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);

                        break;
                    case 19:
                        listItems.clear();
                        type="police";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 20:
                        listItems.clear();
                        type="post_office";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 21:
                        listItems.clear();
                        type="school";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 22:
                        listItems.clear();
                        type="stadium";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 23:
                        listItems.clear();
                        type="supermarket";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 24:
                        listItems.clear();
                        type="train_station";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 25:
                        listItems.clear();
                        type="travel_agency";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;
                    case 26:
                        listItems.clear();
                        type="zoo";
                        preferencesearchE.putString("searctype",type).commit();
                        loadRecycleView(type);
                        break;


                        default:
                            Toast.makeText(getContext(),"Please Select A Type",Toast.LENGTH_SHORT).show();
                            preferencesearchE.putString("searctype","").commit();
                            listItems.clear();
                            adapter=new MyAdapter(listItems,getContext());
                            recyclerView.setAdapter(adapter);

                            break;

                }
                SharedPreferences preferences2 =  getActivity().getSharedPreferences("lengthET", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = preferences2.edit();


                prefsEditor.putString("length",rediusET.getText().toString()).commit();


            }
        });



        try {



            String back = getArguments().getString("back");
            if(back=="back"){

//Set back use chas dadta
                SharedPreferences preferences =  this.getActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE);
                String type=preferences.getString("ItemId", "");

                spinner.setText(type);


                SharedPreferences preferences3 =  this.getActivity().getSharedPreferences("lengthET", MODE_PRIVATE);
               // rediusET.setText(preferences3.getString("length", ""));

                SharedPreferences preferencesearch =getActivity().getSharedPreferences("DistanceSeek",MODE_PRIVATE);
               rediusET.setText(preferencesearch.getString("distance",""));

                //MyAdapter adpt=new MyAdapter();
                Log.d("back","??"+type);

                getArguments().remove("back");

                loadRecycleView(type);

            }
            else {listItems.clear();


            /*spinner.setId(0);
            rediusET.setText("1");*/

                Log.d("back","clear");
            }


        }catch (Exception e){
            listItems.clear();


           // rediusET.setText("1");

            Log.d("back","Error not select");
        }



    }

    ProgressDialog progressDialog;
    public void loadRecycleView(final String type) {







        try {

            listItems.clear();
           // final ProgressDialog progressDialog;
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading.....");
        progressDialog.show();

        //Back Button Press Detect..........
        }catch (Exception e){

        }





        MainActivity mainActivity= (MainActivity) getActivity();

        try {
            //String type = "hospital";

            SharedPreferences preferences =  getActivity().getSharedPreferences("NearbyData", MODE_PRIVATE);
            final String lat=preferences.getString("lat", "");
            final String lon=preferences.getString("lon","");


           // String mailat=String.valueOf(mainActivity.getMyLat());
           // Log.d("locatt",String.valueOf(lat));

            StringBuilder googlePlacesUrl =
                    new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePlacesUrl.append("location=").append(lat).append(",").append(lon);
            googlePlacesUrl.append("&radius=").append(Double.parseDouble(rediusET.getText().toString())*1000);
            googlePlacesUrl.append("&types=").append(type);
            googlePlacesUrl.append("&sensor=true");
            googlePlacesUrl.append("&key=" + GOOGLE_BROWSER_API_KEY);


             //String url = "https://maps.googleapis.com/maps/api/directions/json?origin=Daffodil%20International%20University%20Main%20Campus%20(DIU)&destination=Mohammadpur%20Model%20School%20&%20College&key=AIzaSyCXacNxNEMAREbql4V1o5Rkh4VerZjztoE&callback";
            String url=googlePlacesUrl.toString();
            Log.d("Location",url);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            try {
                                JSONArray neadby = response.getJSONArray("results");

                                for(int i=0;i<neadby.length();i++){
                                    JSONObject list= new JSONObject(String.valueOf(neadby.get(i)) );

                                    JSONObject location=new JSONObject(list.getString("geometry"));
                                    JSONObject location2=new JSONObject(location.getString("location"));

                                    String distance=getDistanceKm(Float.valueOf(lat),Float.valueOf(lon),Float.valueOf(location2.getString("lat")),Float.valueOf(location2.getString("lng")));
                                    Log.d("LocationD",String.valueOf(distance));

                                    NearByPlaceModel listitem=new NearByPlaceModel(list.getString("name"),list.getString("vicinity"),location2.getString("lat"),location2.getString("lng"),type, distance,list.getString("place_id"));
                                    listItems.add(listitem);

                                  //  Log.d("Location",list.getString("name"));
                                }


                                progressDialog.dismiss();
                                adapter=new MyAdapter(listItems,getContext());
                                recyclerView.setAdapter(adapter);
                                if (listItems.size()<1){
                                    instruction.setText("No Data Found");
                                }

                                // Log.d("Location",contacts.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Location","Notok");

                        }
                    });
            rq.add(jsonObjectRequest);
        }catch (Exception e){
            Log.d("Location",e.toString());



        }




    }
    public String getDistanceKm( float lat_a, float lng_a, float lat_b, float lng_b) {

     /*   double R = 6371e3; // metres
        double φ1 = Math.toRadians(lat_a);
        double φ2 = Math.toRadians(lat_b);
        double Δφ = Math.toRadians(lat_b-lat_a);
        double Δλ = Math.toRadians(lng_b-lng_a);

        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d = R * c;

        double Δψ = Math.log(Math.tan(Math.PI/4+φ2/2)/Math.tan(Math.PI/4+φ1/2));
        double q = Math.abs(Δψ) > 10e-12 ? Δφ/Δψ : Math.cos(φ1);


        return d;*/

     Location location1=new Location("LocationA");
     location1.setLatitude(lat_a);
     location1.setLongitude(lng_a);
        Location location2=new Location("LocationB");
        location2.setLatitude(lat_b);
        location2.setLongitude(lng_b);
        double distance =location1.distanceTo(location2);



        DecimalFormat myFormatter = new DecimalFormat("0.00");
        return myFormatter.format(distance/1000);






    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
   /* public void loadRecycleView2(final String type) {
        listItems.clear();


        try {
            //String type = "hospital";

            SharedPreferences preferences5 =  getActivity().getSharedPreferences("NearbyData", MODE_PRIVATE);
            String lat=preferences5.getString("lat", "");
            String lon=preferences5.getString("lon","");

           // String mailat=String.valueOf(mainActivity.getMyLat());
           // Log.d("loca",String.valueOf(mainActivity.getMyLat()));

            StringBuilder googlePlacesUrl =
                    new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePlacesUrl.append("location=").append(lat).append(",").append(lon);
            googlePlacesUrl.append("&radius=").append(Double.parseDouble(rediusET.getText().toString())*1000);
            googlePlacesUrl.append("&types=").append(type);
            googlePlacesUrl.append("&sensor=true");
            googlePlacesUrl.append("&key=" + GOOGLE_BROWSER_API_KEY);


            //String url = "https://maps.googleapis.com/maps/api/directions/json?origin=Daffodil%20International%20University%20Main%20Campus%20(DIU)&destination=Mohammadpur%20Model%20School%20&%20College&key=AIzaSyCXacNxNEMAREbql4V1o5Rkh4VerZjztoE&callback";
            String url=googlePlacesUrl.toString();
            Log.d("Location",url);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            try {
                                JSONArray neadby = response.getJSONArray("results");

                                for(int i=0;i<neadby.length();i++){
                                    JSONObject list= new JSONObject(String.valueOf(neadby.get(i)) );

                                    JSONObject location=new JSONObject(list.getString("geometry"));
                                    JSONObject location2=new JSONObject(location.getString("location"));
                                    Log.d("Locationt",location2.getString("lat"));
                                    NearByPlaceModel listitem=new NearByPlaceModel(list.getString("name"),list.getString("vicinity"),location2.getString("lat"),location2.getString("lng"),type);
                                    listItems.add(listitem);

                                    //  Log.d("Location",list.getString("name"));
                                }



                                adapter=new MyAdapter(listItems,getContext());
                                recyclerView.setAdapter(adapter);

                                // Log.d("Location",contacts.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Location","Notok");

                        }
                    });
            rq.add(jsonObjectRequest);
        }catch (Exception e){
            Log.d("Location",e.toString());



        }

    }*/



}
