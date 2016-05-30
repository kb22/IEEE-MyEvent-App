package com.rkapps.ieeemyeventapp;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sony on 5/26/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder>{

    List<IEEEEvents> events;

    static int current;

    RVAdapter(List<IEEEEvents> events){
        this.events = events;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView eventName;
        TextView eventDesc;
        ImageView eventImg;
        TextView date1;

        EventViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            eventName = (TextView)itemView.findViewById(R.id.event_name);
            eventDesc = (TextView)itemView.findViewById(R.id.event_desc);
            date1 = (TextView)itemView.findViewById(R.id.event_date1);
            //eventImg = (ImageView)itemView.findViewById(R.id.event_photo);


        }

    }

    @Override
    public int getItemCount(){
        return events.size();
    }

    @Override
    public EventViewHolder onCreateViewHolder (ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_main, viewGroup, false);// content main? or item?
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(final EventViewHolder personViewHolder, int i) {
        personViewHolder.eventName.setText(events.get(i).eventName);
        personViewHolder.eventDesc.setText(events.get(i).description);
        personViewHolder.date1.setText(events.get(i).date1);
        current = i;
        //personViewHolder.eventImg.setImageResource(events.get(i).eventID);

        personViewHolder.cv.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.d("Check123", "Clicked : " + current + "");
                Intent i = new Intent(v.getContext(), ShowEvent.class);
                v.getContext().startActivity(i.putExtra("Event_ID", events.get(personViewHolder.getAdapterPosition()).eventID));

            }
        });
    }




    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }






}
