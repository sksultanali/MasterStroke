package com.developerali.masterstroke.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.masterstroke.Activities.SearchActivity;
import com.developerali.masterstroke.ApiModels.WardClass;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ChildVoterBinding;

import java.util.List;

public class PartSecAdapter  extends RecyclerView.Adapter<PartSecAdapter.ViewHolder>{

    Activity activity;
    List<WardClass.Item> arrayList;
    String type;

    public PartSecAdapter(Activity activity, List<WardClass.Item> arrayList, String type) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.child_voter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WardClass.Item details = arrayList.get(position);

        holder.binding.otherDetails.setText("Total - " + details.getTotal());
        holder.binding.voterName.setText( type.toUpperCase() + " - " + details.getTxt());

        holder.itemView.setOnClickListener(v->{
            Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
            i.putExtra("keyword", details.getTxt());
            i.putExtra("searchOn", type);
            activity.startActivity(i);
        });
    }

    public void addItems(List<WardClass.Item> newVoters) {
        this.arrayList.addAll(newVoters);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ChildVoterBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChildVoterBinding.bind(itemView);
        }
    }
}
