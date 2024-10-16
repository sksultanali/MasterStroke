package com.developerali.masterstroke.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.masterstroke.Activities.VoterDetails;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.SelectionListner;
import com.developerali.masterstroke.databinding.ChildVoterBinding;

import java.util.ArrayList;
import java.util.List;

public class VoterAdapter extends RecyclerView.Adapter<VoterAdapter.ViewHolder>{

    Activity activity;
    List<PhoneAddressModel.Item> arrayList;
    SelectionListner selectionListner;

    public VoterAdapter(Activity activity, List<PhoneAddressModel.Item> arrayList, SelectionListner selectionListner) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.selectionListner = selectionListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.child_voter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        PhoneAddressModel.Item details = arrayList.get(position);
        details.isSelected = false;
        defaultColors(holder, details, position);

        holder.binding.voterName.setText(details.getSl_no() + ". " + details.getName());
        String someString = details.getAddress();


        if (someString.length() >= 22) {
            String substring = someString.substring(0, 22);
            holder.binding.otherDetails.setText(details.getHouse() + ", "+ substring+ " | Age - " + details.getAge());
        } else {
            holder.binding.otherDetails.setText(someString+ " | Age - " + details.getAge());
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (details.isSelected){
                    defaultColors(holder, details, position);
                    details.isSelected = false;
                    if (getSelectedRows().size() == 0){
                        selectionListner.onShowAction(false);
                    }
                }else {
                    holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_green_corner));
                    holder.binding.voterName.setTextColor(activity.getColor(R.color.white));
                    holder.binding.otherDetails.setTextColor(activity.getColor(R.color.gray));
                    details.isSelected = true;
                    selectionListner.onShowAction(true);
                }
                return true;
            }
        });

        Helper.translateText(activity, holder.binding.voterName.getText().toString(), new Helper.TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                holder.binding.voterName.setText(translatedText);
            }

            @Override
            public void onTranslationFailure(String errorText) {

            }
        });

        Helper.translateText(activity, holder.binding.otherDetails.getText().toString(), new Helper.TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                holder.binding.otherDetails.setText(translatedText);
            }

            @Override
            public void onTranslationFailure(String errorText) {

            }
        });


        holder.itemView.setOnClickListener(v->{
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

    void defaultColors(ViewHolder holder, PhoneAddressModel.Item details, int position){
        if (details.getStat() != null && details.getStat().equalsIgnoreCase("edited")){
            holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_dark_yellow));
            //holder.binding.voterName.setTextColor(activity.getColor(R.color.white));
            holder.binding.otherDetails.setTextColor(activity.getColor(R.color.red));
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
    }

    public ArrayList<PhoneAddressModel.Item> getSelectedRows(){
        ArrayList<PhoneAddressModel.Item> selectedRows = new ArrayList();
        for (PhoneAddressModel.Item item : arrayList){
            if (item.isSelected){
                selectedRows.add(item);
            }
        }
        return selectedRows;
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

        void setBindData(final PhoneAddressModel.Item details){


        }
    }
}
