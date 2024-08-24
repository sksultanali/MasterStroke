package com.developerali.masterstroke.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiModels.WardWiseChildVoters;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.SelectionListner;
import com.developerali.masterstroke.databinding.ChildNewVotersAndChilddBinding;
import com.developerali.masterstroke.databinding.ChildVoterBinding;

import java.util.List;

public class ChildVotersAdapter extends RecyclerView.Adapter<ChildVotersAdapter.ViewHolder>{

    Activity activity;
    List<WardWiseChildVoters> arrayList;

    public ChildVotersAdapter(Activity activity, List<WardWiseChildVoters> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.child_new_voters_and_childd, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (position % 2 == 0){
            holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_white_color_corner8));
            holder.binding.voterName.setTextColor(activity.getColor(R.color.black));
            holder.binding.otherDetails.setTextColor(activity.getColor(R.color.icon_color));
        }else {
            holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_dark_gray_corner8));
            holder.binding.voterName.setTextColor(activity.getColor(R.color.black));
            holder.binding.otherDetails.setTextColor(activity.getColor(R.color.icon_color));
        }


        WardWiseChildVoters details = arrayList.get(position);

        holder.binding.voterName.setText(details.getName() + " " + details.getLname());

        if (!details.getsClass().isEmpty()){
            holder.binding.detailsClass.setVisibility(View.VISIBLE);
            holder.binding.detailsClass.setText("Class - " + details.getsClass());
        }else {
            holder.binding.detailsClass.setVisibility(View.GONE);
        }

        if (!details.getMobile().isEmpty()){
            holder.binding.detailsMobile.setVisibility(View.VISIBLE);
            holder.binding.detailsMobile.setText("Mobile - " +details.getMobile());
        }else {
            holder.binding.detailsMobile.setVisibility(View.GONE);
        }

        holder.binding.voterType.setText(details.getType());
        String someString = details.getAddress();

        if (someString.length() >= 22) {
            String substring = someString.substring(0, 22);
            holder.binding.otherDetails.setText(substring+ " | DOB - " + details.getDob());
        } else {
            holder.binding.otherDetails.setText(someString+ " | DOB - " + details.getDob());
        }



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addItems(List<WardWiseChildVoters> newVoters) {
        this.arrayList.addAll(newVoters);
        notifyDataSetChanged();
    }

    public void removeItems() {
        if (arrayList != null && !arrayList.isEmpty()){
            this.arrayList.clear();
            if (this != null) {
                notifyDataSetChanged();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ChildNewVotersAndChilddBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChildNewVotersAndChilddBinding.bind(itemView);
        }
    }
}
