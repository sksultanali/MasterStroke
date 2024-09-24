package com.developerali.masterstroke.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.developerali.masterstroke.R;

import java.util.ArrayList;

public class myListAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<String> arrayList;
    boolean singleColor;

    public myListAdapter(Activity activity, ArrayList<String> arrayList, boolean singleColor) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.singleColor = singleColor;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater obj = activity.getLayoutInflater();
        View view1 = obj.inflate(R.layout.sample_tools_vertical, null);

        TextView textView = view1.findViewById(R.id.toolName);
        textView.setText(arrayList.get(i));

        if (singleColor){
            if (i % 2 == 0) {
                view1.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
            } else {
                view1.setBackgroundColor(ContextCompat.getColor(activity, R.color.darkGray));
            }
        }else {
            if (i % 3 == 0) {
                view1.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_green_color2));
            } else if (i % 2 == 0){
                view1.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_yellow));
            }else {
                view1.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_red));
            }
        }

        return view1;
    }
}
