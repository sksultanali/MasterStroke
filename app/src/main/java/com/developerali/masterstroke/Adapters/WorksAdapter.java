package com.developerali.masterstroke.Adapters;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiModels.WorksModel;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ChildVoterBinding;

import java.util.List;

public class WorksAdapter extends RecyclerView.Adapter<WorksAdapter.ViewHolder>{

    Activity activity;
    List<WorksModel.Item> models;

    public WorksAdapter(Activity activity, List<WorksModel.Item> models) {
        this.activity = activity;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.child_voter, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorksModel.Item details = models.get(position);

        holder.binding.voterName.setText(details.toString());
        holder.binding.otherDetails.setText("\n"+details.getDate()+"\n"+details.getDistance());
        holder.binding.voterName.setGravity(Gravity.START);
        holder.binding.otherDetails.setGravity(Gravity.START);

    }

    public void addItems(List<WorksModel.Item> newVoters) {
        this.models.addAll(newVoters);
        notifyDataSetChanged();
    }

    public void removeItems() {
        this.models.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ChildVoterBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChildVoterBinding.bind(itemView);
        }
    }
}
