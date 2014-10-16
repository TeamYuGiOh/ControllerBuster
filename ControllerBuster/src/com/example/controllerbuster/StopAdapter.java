package com.example.controllerbuster;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StopAdapter extends ArrayAdapter<Stop>{

    Context context; 
    int layoutResourceId;    
    List<Stop> data = null;
    int[] imgResources;
    
    public StopAdapter(Context context, int layoutResourceId, List<Stop> data, int[] imgResources) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.imgResources = imgResources;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
    	StopHolder holder = null;
        
        if(view == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new StopHolder();
            holder.icon = (ImageView) view.findViewById(R.id.icon);
            holder.stopName = (TextView) view.findViewById(R.id.stopName);
            
            view.setTag(holder);
        }
        else
        {
            holder = (StopHolder)view.getTag();
        }
        
        Stop stop = data.get(position); 
        holder.icon.setImageResource(this.imgResources[0]);
        holder.stopName.setText(stop.getStopName());
        return view;
    }
    
    static class StopHolder
    {
    	ImageView icon;
        TextView stopName;
    }
}

