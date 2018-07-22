package com.smac.tushar.mylocation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class MapFragment extends Fragment implements OnMapReadyCallback {


    RequestQueue rq;
    View view;
    GoogleMap googleMap;
    MapView mapView;
    Context context;
    private  String GOOGLE_BROWSER_API_KEY="AIzaSyAowCbmyBSJtLUGn_FKoeGuVj4Sk_z5Rfw";




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_map,container,false);
        context=getActivity().getBaseContext();
        Log.d("Maps","ok");
        MainActivity activity= (MainActivity) getActivity();
        activity.updateFrag(false);
        return view;


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
        mapView=(MapView) view.findViewById(R.id.map2);

        rq= Volley.newRequestQueue(getContext());
        if(mapView!=null){

            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);

        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        MainActivity mainActivity = (MainActivity) getActivity();


        double startLat=mainActivity.getMyLat();
        double startLon=mainActivity.getMyLon();
        double endLat= Double.parseDouble(getArguments().getString("endlat"));
        double endtLon=Double.parseDouble(getArguments().getString("endlng"));
        String name=getArguments().getString("name");
        String address=getArguments().getString("address");
        String type=getArguments().getString("type");



        Fragment fragment2=new NearByPlaceFragment();
        Bundle b = new Bundle();
        b.putString("maptype",type);

        fragment2.setArguments(b);


        Log.d("Maps","ok");
        MapsInitializer.initialize(getContext());
        Log.d("Maps","ok");

       // String value = getArguments().getString("YourKey");
        //Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
        LatLng startLatLon = new LatLng(startLat, startLon);

        googleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLon));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        Marker marker= googleMap.addMarker(new MarkerOptions()
                .position(startLatLon)
                .title("Your Location")

                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.endmarker)));
        marker.showInfoWindow();

        Marker marker2=  googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(endLat,endtLon))
                .title(name)
                .snippet(address)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.startmarker)));
       // marker2.showInfoWindow();

        //Save cashe memory

        SharedPreferences preferences =  getActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();

        String a =type;

        prefsEditor.putString("ItemId",a).commit();



        buildUrl(googleMap,startLat,startLon,endLat,endtLon);
    }






    public void buildUrl(final GoogleMap map,double starLat,double startLon,double endLat,double endtLon) {

        MainActivity mainActivity = (MainActivity) getActivity();
        try {
        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googlePlacesUrl.append("origin=").append(starLat).append(",").append(startLon);
        googlePlacesUrl.append("&destination=").append(endLat).append(",").append(endtLon);

        googlePlacesUrl.append("&key=" + GOOGLE_BROWSER_API_KEY);

        String url = googlePlacesUrl.toString();
        Log.d("LocationUrl", url);


        //Maps direction step parces.....................


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                       // Log.d("points", "ok");
                        try {
                            Log.d("points", "ok");
                            JSONArray stepPath = response.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");

                           // Log.d("points", stepPath.toString());

                            int count = stepPath.length();
                            String[] polyins_array = new String[count];


                            for (int i = 0; i < count; i++) {
                                JSONObject list = new JSONObject(String.valueOf(stepPath.get(i)));

                                String polygon = list.getJSONObject("polyline").getString("points");
                                polyins_array[i] = polygon;

                                Log.d("points", polygon);
                            }

                            int count2 = polyins_array.length;

                            for (int i = 0; i < count2; i++) {
                                PolylineOptions polylineOptions = new PolylineOptions();
                                polylineOptions.color(Color.BLUE);
                                polylineOptions.width(10);
                                polylineOptions.addAll(PolyUtil.decode(polyins_array[i]));

                                map.addPolyline(polylineOptions);
                            }


                            // Log.d("Location",contacts.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("points", "error");
                    }
                });
        rq.add(jsonObjectRequest);
    }catch (Exception e){
        Log.d("points",e.toString());



    }

    }







}