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

        holder.binding.voterName.setText(details.getName());
        holder.binding.otherDetails.setText(details.getAddress().substring(0, 22) + " | Age - " + details.getAge());

        holder.itemView.setOnClickListener(v->{
            activity.startActivity(new Intent(activity.getApplicationContext(), VoterDetails.class));
        });
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
