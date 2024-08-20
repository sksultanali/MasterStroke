package com.developerali.masterstroke.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.masterstroke.Activities.VoterDetails;
import com.developerali.masterstroke.ApiModels.BoothReportModel;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ChildVoterBinding;

import java.util.ArrayList;
import java.util.List;

public class VoterAdapter extends RecyclerView.Adapter<VoterAdapter.ViewHolder>{

    Activity activity;
    List<PhoneAddressModel.Item> arrayList;

    public VoterAdapter(Activity activity, List<PhoneAddressModel.Item> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.child_voter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PhoneAddressModel.Item details = arrayList.get(position);

        holder.binding.voterName.setText(details.getName() + " " + details.getLname());
        String someString = details.getAddress();

        if (details.getStat() != null && details.getStat().equalsIgnoreCase("edited")){
            holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_dark_yellow));
            holder.binding.voterName.setTextColor(activity.getColor(R.color.white));
            holder.binding.otherDetails.setTextColor(activity.getColor(R.color.gray));
        }else {

            if (position % 2 == 0){
                holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_white_color_corner8));
                holder.binding.voterName.setTextColor(activity.getColor(R.color.black));
                holder.binding.otherDetails.setTextColor(activity.getColor(R.color.icon_color));
            }else {
                holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_dark_gray_corner8));
                holder.binding.voterName.setTextColor(activity.getColor(R.color.black));
                holder.binding.otherDetails.setTextColor(activity.getColor(R.color.icon_color));
            }
        }

        if (someString.length() >= 22) {
            String substring = someString.substring(0, 22);  // Make sure the index is within bounds
            holder.binding.otherDetails.setText(substring+ " | Age - " + details.getAge());
        } else {
            holder.binding.otherDetails.setText(someString+ " | Age - " + details.getAge());  // Handle the case where the string is too short
        }

        holder.itemView.setOnClickListener(v->{
//            holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_green_corner));
//            holder.binding.voterName.setTextColor(activity.getColor(R.color.white));
//            holder.binding.otherDetails.setTextColor(activity.getColor(R.color.gray));
//            details.setStat("edited");

            Intent i = new Intent(activity.getApplicationContext(), VoterDetails.class);
            i.putExtra("details", details);
            activity.startActivity(i);
        });
    }

    public void addItems(List<PhoneAddressModel.Item> newVoters) {
        this.arrayList.addAll(newVoters);
        notifyDataSetChanged();
    }

    public void removeItems() {
        this.arrayList.clear();
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
