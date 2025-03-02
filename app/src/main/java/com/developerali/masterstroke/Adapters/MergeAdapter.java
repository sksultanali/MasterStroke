package com.developerali.masterstroke.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.masterstroke.Activities.ListActivity;
import com.developerali.masterstroke.Activities.SearchActivity;
import com.developerali.masterstroke.ApiModels.UpdateModel;
import com.developerali.masterstroke.ApiModels.WardClass;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.Helpers.MarkedItemsPreference;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.SelectionListner;
import com.developerali.masterstroke.databinding.ChildVoterBinding;
import com.developerali.masterstroke.databinding.DialogListSearchBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MergeAdapter extends RecyclerView.Adapter<MergeAdapter.ViewHolder>{

    Activity activity;
    List<WardClass.Item> arrayList;
    String grossTotal;
    ArrayList<String> arrayListChoose;
    ProgressDialog progressDialog;
    Set<String> markedItems;
    SelectionListner selectionListner;
    private final List<WardClass.Item> itemsToRemove = new ArrayList<>();
    final int lastItem;

    public MergeAdapter(Activity activity, List<WardClass.Item> arrayList, String grossTotal, SelectionListner selectionListner) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.grossTotal = grossTotal;
        this.selectionListner = selectionListner;
        arrayListChoose = new ArrayList<>();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("updating data...");
        markedItems = MarkedItemsPreference.getMarkedItems(activity);
        if (arrayList != null && !arrayList.isEmpty()){
            lastItem = arrayList.size();
        }else {
            lastItem = 0;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.child_voter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        WardClass.Item details = arrayList.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (details.isSelected){
                    defaultColors(holder, details, position);
                    details.isSelected = false;
                    try{
                        if (Helper.MARKING_ENABLE){
                            MarkedItemsPreference.removeMarkedItem(activity, "Id"+position+details.getTxt());
                        }
                    }catch (Exception e){

                    }
                    if (getSelectedRows().size() == 0){
                        selectionListner.onShowAction(false);
                    }
                }else {
                    holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_green_corner));
                    holder.binding.voterName.setTextColor(activity.getColor(R.color.white));
                    holder.binding.otherDetails.setTextColor(activity.getColor(R.color.gray));
                    MarkedItemsPreference.saveMarkedItem(activity, "Id"+position+details.getTxt());
                    details.isSelected = true;
                    selectionListner.onShowAction(true);
                }
                return true;
            }
        });

        if (markedItems.contains("Id"+position+details.getTxt())){
            holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_dark_yellow));
            holder.binding.otherDetails.setTextColor(activity.getColor(R.color.red));
        }else {
            holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_white_color_corner8));
            holder.binding.voterName.setTextColor(activity.getColor(R.color.black));
            holder.binding.otherDetails.setTextColor(activity.getColor(R.color.icon_color));
        }

        holder.itemView.setOnClickListener(v->{
            if (details.getSubnames().size() > 1){
                searchDialog(details.getSubnames(), new ListActivity.DialogCallback() {
                    @Override
                    public void onResult(String keyword) {
                        //Toast.makeText(activity, keyword, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                        i.putExtra("keyword", keyword);
                        i.putExtra("searchOn", "lname");
                        activity.startActivity(i);
                    }
                });
            }else {
                Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                i.putExtra("keyword", details.getSubnames().get(0));
                i.putExtra("searchOn", "lname");
                activity.startActivity(i);
            }
        });


        holder.binding.otherDetails.setText("Total - " + details.getTotal()+ " / " + grossTotal);
        if (details.getTxt() == null || details.getTxt().isEmpty()){
            holder.binding.voterName.setText("UNDEFINED");
        }else {
            if (details.getSubnames().size() > 1){
                holder.binding.voterName.setText( "Last Name" + " - " + details.getTxt() + " + ");
            }else {
                holder.binding.voterName.setText( "Last Name" + " - " + details.getTxt());
            }

        }
        holder.binding.slNo.setVisibility(View.VISIBLE);
        holder.binding.slNo.setText("#"+(position+1));





    }

    private void searchDialog(ArrayList<String> arrayListChoose, ListActivity.DialogCallback callback) {
        DialogListSearchBinding dialogBinding = DialogListSearchBinding.inflate(activity.getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogBinding.getRoot());
        // Create and show the dialog
        AlertDialog dialog = builder.create();

        dialogBinding.searchOn.setText("Merged Last Names");

        myListAdapter adapter = new myListAdapter(activity, arrayListChoose, false);
        dialogBinding.chooseList.setAdapter(adapter);

        dialogBinding.chooseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                callback.onResult(arrayListChoose.get(i));
            }
        });

        dialog.show();
    }

    public ArrayList<WardClass.Item> getSelectedRows(){
        ArrayList<WardClass.Item> selectedRows = new ArrayList();
        for (WardClass.Item item : arrayList){
            if (item.isSelected){
                selectedRows.add(item);
            }
        }
        return selectedRows;
    }

    void defaultColors(ViewHolder holder, WardClass.Item details, int position){
        holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_white_color_corner8));
        holder.binding.voterName.setTextColor(activity.getColor(R.color.black));
        holder.binding.otherDetails.setTextColor(activity.getColor(R.color.icon_color));
    }

    public void updateAllData(String queryText, String changeValue,
                              int position, PartSecAdapter.ViewHolder holder){
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<UpdateModel> call = apiService.updateAllData(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                "lname",
                queryText,
                "language",
                changeValue,
                Integer.parseInt(Helper.WARD)
        );

        progressDialog.show();
        call.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UpdateModel apiResponse = response.body();
                    Toast.makeText(activity, apiResponse.getStatus()
                            + " : " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_dark_yellow));
                    //holder.binding.voterName.setTextColor(activity.getColor(R.color.white));
                    holder.binding.otherDetails.setTextColor(activity.getColor(R.color.red));

                } else {
                    Toast.makeText(activity, "Failed here...!", Toast.LENGTH_SHORT).show();
                }
                Log.d("PartSecAd.this", "URL: " + call.request().url());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("PartSecAd.this", "URL: " + call.request().url());
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addItems(List<WardClass.Item> newVoters) {
        this.arrayList.addAll(newVoters);
        notifyDataSetChanged();
    }

    public void removeMatchedList
            (List<WardClass.Item> completeList, List<WardClass.Item> removeList) {
        Set<WardClass.Item> removeSet = new HashSet<>(removeList);
        List<WardClass.Item> unmatchedItems = new ArrayList<>();
        for (WardClass.Item item : completeList) {
            if (!removeSet.contains(item)) {
                unmatchedItems.add(item);
            }
        }
        this.arrayList.clear();
        addItems(unmatchedItems);
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
