package com.example.tcchecker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class custom_adapter extends ArrayAdapter<String> {
    private int resourceId;
    private int resourceId2;
    private String[] cs = new String[19];
    private int size;

    public custom_adapter(Context context, int resourceId, String[] items, String[] colors, int s) {
        super(context, resourceId,items);
        this.resourceId = resourceId;
        this.resourceId2 = resourceId;
        this.size = s;
        for(int i = 0; i < colors.length; i++){
            cs[i] = colors[i];
        }
    }
    public void setDropDownViewResource(int resourceId2){
        this.resourceId2 = resourceId2;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resourceId2, null);
        }
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        String list=this.getItem(position);
        String color = cs[position];
        tv.setTextSize(size);
        tv.setText(list);
        tv.setTextColor(Color.parseColor(color));//ここでリストの文字色を指定
        return convertView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resourceId, null);
        }
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        String list=this.getItem(position);
        String color = cs[position];
        tv.setText(list);
        tv.setTextSize(size);
        tv.setTextColor(Color.WHITE);//ここでスピナ-の文字色を指定
        return convertView;
    }
}
