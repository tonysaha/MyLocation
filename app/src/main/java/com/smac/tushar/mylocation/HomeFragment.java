package com.smac.tushar.mylocation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements View.OnClickListener {
    View view;
    SeekBar seekBar;
    TextView distance;

    NearByPlaceFragment nearByPlaceFragment;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        seekBar=view.findViewById(R.id.seekBar);
        distance=view.findViewById(R.id.distance_id);

        try{
            SharedPreferences preferencesearch =getActivity().getSharedPreferences("DistanceSeek",MODE_PRIVATE);
            distance.setText(preferencesearch.getString("distance","1"));
            seekBar.setProgress(Integer.valueOf(String.valueOf(distance.getText())));
        }catch (Exception e){

        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance.setText(String.valueOf(progress/2));

                SharedPreferences preferencesearch =getActivity().getSharedPreferences("DistanceSeek",MODE_PRIVATE);
                SharedPreferences.Editor preferencesearchE=preferencesearch.edit();
                preferencesearchE.putString("distance",distance.getText().toString()).commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        nearByPlaceFragment=new NearByPlaceFragment();
MainActivity activity=new MainActivity();


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        ImageView atm=(ImageView) view.findViewById(R.id.atm_id);
        atm.setOnClickListener(this);

        ImageView bank_id=(ImageView) view.findViewById(R.id.bank_id);
        bank_id.setOnClickListener(this);

        ImageView texi_id=(ImageView) view.findViewById(R.id.texi_id);
        texi_id.setOnClickListener(this);

        ImageView bas_id=(ImageView) view.findViewById(R.id.bas_id);
        bas_id.setOnClickListener(this);

        ImageView salon_id=(ImageView) view.findViewById(R.id.salon_id);
        salon_id.setOnClickListener(this);

        ImageView hospital_id=(ImageView) view.findViewById(R.id.hospital_id);
        hospital_id.setOnClickListener(this);

        ImageView hotel_id=(ImageView) view.findViewById(R.id.hotel_id);
        hotel_id.setOnClickListener(this);

        ImageView restaurant_id=(ImageView) view.findViewById(R.id.restaurant_id);
        restaurant_id.setOnClickListener(this);

        ImageView park_id=(ImageView) view.findViewById(R.id.park_id);
        park_id.setOnClickListener(this);

        ImageView zoo_id=(ImageView) view.findViewById(R.id.zoo_id);
        zoo_id.setOnClickListener(this);

        ImageView cinema_id=(ImageView) view.findViewById(R.id.cinema_id);
        cinema_id.setOnClickListener(this);

        ImageView shopping_id=(ImageView) view.findViewById(R.id.shopping_id);
        shopping_id.setOnClickListener(this);

        ImageView cafa_id=(ImageView) view.findViewById(R.id.cafa_id);
        cafa_id.setOnClickListener(this);

        ImageView grocery_id=(ImageView) view.findViewById(R.id.grocery_id);
        grocery_id.setOnClickListener(this);
        ImageView bar_id=(ImageView) view.findViewById(R.id.bar_id);
        bar_id.setOnClickListener(this);

        ImageView bakery_id=(ImageView) view.findViewById(R.id.bakery_id);
        bakery_id.setOnClickListener(this);

        ImageView dentist_id=(ImageView) view.findViewById(R.id.dentist_id);
        dentist_id.setOnClickListener(this);

        ImageView doctor_id=(ImageView) view.findViewById(R.id.doctor_id);
        doctor_id.setOnClickListener(this);

        ImageView police_id=(ImageView) view.findViewById(R.id.police_id);
        police_id.setOnClickListener(this);

        ImageView university=(ImageView) view.findViewById(R.id.university);
        university.setOnClickListener(this);

        ImageView gas_id=(ImageView) view.findViewById(R.id.gas_id);
        gas_id.setOnClickListener(this);

        ImageView temple_id=(ImageView) view.findViewById(R.id.temple_id);
        temple_id.setOnClickListener(this);

        ImageView church=(ImageView) view.findViewById(R.id.church);
        church.setOnClickListener(this);


        ImageView mosque_id=(ImageView) view.findViewById(R.id.mosque_id);
        mosque_id.setOnClickListener(this);

        ImageView carwash_id=(ImageView) view.findViewById(R.id.carwash_id);
        carwash_id.setOnClickListener(this);

        ImageView gym_id=(ImageView) view.findViewById(R.id.gym_id);
        gym_id.setOnClickListener(this);

        ImageView school_id=(ImageView) view.findViewById(R.id.school_id);
        school_id.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        SharedPreferences preferencesearch;
        FragmentManager fragmentManager;
        preferencesearch =getActivity().getSharedPreferences("HomeClick",MODE_PRIVATE);
        SharedPreferences.Editor preferencesearchE= preferencesearchE=preferencesearch.edit();
        switch (v.getId()){


            case R.id.atm_id:

                preferencesearchE.putString("type","atm");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;

            case R.id.bank_id:

                preferencesearchE.putString("type","bank");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;

            case R.id.texi_id:

                preferencesearchE.putString("type","taxi_stand");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.bas_id:

                preferencesearchE.putString("type","bus_station");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.salon_id:

                preferencesearchE.putString("type","hair_care");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.hospital_id:

                preferencesearchE.putString("type","hospital");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.hotel_id:

                preferencesearchE.putString("type","hotel");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.restaurant_id:

                preferencesearchE.putString("type","restaurant");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.park_id:

                preferencesearchE.putString("type","park");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.zoo_id:

                preferencesearchE.putString("type","zoo");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.cinema_id:

                preferencesearchE.putString("type","movie_theater");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.shopping_id:

                preferencesearchE.putString("type","shopping_mall");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.cafa_id:

                preferencesearchE.putString("type","cafe");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.grocery_id:

                preferencesearchE.putString("type","grocery");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.bar_id:

                preferencesearchE.putString("type","bar");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.bakery_id:

                preferencesearchE.putString("type","bakery");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.dentist_id:

                preferencesearchE.putString("type","dentist");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.doctor_id:

                preferencesearchE.putString("type","doctor");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.police_id:

                preferencesearchE.putString("type","police");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.university:

                preferencesearchE.putString("type","university");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.gas_id:

                preferencesearchE.putString("type","gas_station");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.temple_id:

                preferencesearchE.putString("type","hindu_temple");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.church:

                preferencesearchE.putString("type","church");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.mosque_id:

                preferencesearchE.putString("type","mosque");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.carwash_id:

                preferencesearchE.putString("type","car_wash");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.gym_id:

                preferencesearchE.putString("type","gym");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;
             case R.id.school_id:

                preferencesearchE.putString("type","school");
                preferencesearchE.putString("redius",distance.getText().toString()).commit();
                break;




                default:
                Toast.makeText(getContext(),"another",Toast.LENGTH_SHORT).show();

        }

        fragmentManager=getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.screen_area,nearByPlaceFragment).commit();
    }
}
