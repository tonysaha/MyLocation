package com.smac.tushar.mylocation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<NearByPlaceModel> modelList;
    private Context context;

    public MyAdapter() {
    }

    public MyAdapter(List<NearByPlaceModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nearby,parent,false);
        return new ViewHolder(v);
    }
private String mapNme;
    String mapType;

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final NearByPlaceModel listitem=modelList.get(position);
        holder.headerTv.setText(listitem.getName());
        holder.detailTv.setText(listitem.getAddress());
        holder.distaceTv.setText("Distance : "+listitem.getDistance()+" km");
        holder.ratingBar.setRating(Float.valueOf(listitem.getRating()));
        holder.ratingTv.setText(listitem.getRating());
        Glide.with(context)
                .load(listitem.getImageUrl())
                .into(holder.iconEv);


        holder.recView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Send data to NearbypleaceFragement for back operation
                mapType=listitem.getType();




                //Open Map and send data to MapFragment
                Fragment fragment=new MapFragment();
               if(fragment!=null){

                   mapNme=listitem.getName();

                   Log.d("back",mapType);

                   FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                   //For pass data
                   Bundle args = new Bundle();
                   args.putString("endlat", listitem.getLat());
                   args.putString("endlng", listitem.getLan());
                   args.putString("name", listitem.getName());
                   args.putString("address", listitem.getAddress());
                   args.putString("type",mapType);
                   args.putString("palce_id",listitem.getPlaceId());

                   fragment.setArguments(args);


                   FragmentTransaction ft=manager.beginTransaction();
                   ft.replace(R.id.screen_area,fragment);
                   ft.commit();

               }
            }
        });
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
              if(direction==ItemTouchHelper.LEFT){
                  Toast.makeText(context,"ok left",Toast.LENGTH_LONG).show();
              }
            }

            };




        }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView headerTv,detailTv,distaceTv,ratingTv;
        private LinearLayout recView;
        private ImageView iconEv;
        private RatingBar ratingBar;


        public ViewHolder(View itemView) {
            super(itemView);
            headerTv=(TextView) itemView.findViewById(R.id.headTv);
            detailTv=(TextView) itemView.findViewById(R.id.detailTv);
            distaceTv=itemView.findViewById(R.id.distanceTv);
            iconEv=itemView.findViewById(R.id.icon);
            recView=(LinearLayout) itemView.findViewById(R.id.recView);
            ratingBar=itemView.findViewById(R.id.MyRating);
            ratingTv=itemView.findViewById(R.id.ratingTv);

        }
    }
}
