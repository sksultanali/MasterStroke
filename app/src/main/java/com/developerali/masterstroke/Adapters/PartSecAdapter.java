package com.developerali.masterstroke.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.masterstroke.Activities.ListActivity;
import com.developerali.masterstroke.Activities.PartSectionActivity;
import com.developerali.masterstroke.Activities.SearchActivity;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiModels.UpdateModel;
import com.developerali.masterstroke.ApiModels.WardClass;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.Helpers.MarkedItemsPreference;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.SelectionListner;
import com.developerali.masterstroke.databinding.ChildVoterBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartSecAdapter  extends RecyclerView.Adapter<PartSecAdapter.ViewHolder>{

    Activity activity;
    List<WardClass.Item> arrayList;
    String type, adType, text, grossTotal;
    int total, slNo;
    ArrayList<String> arrayListChoose;
    ProgressDialog progressDialog;
    Set<String> markedItems;
    SelectionListner selectionListner;
    private final List<WardClass.Item> itemsToRemove = new ArrayList<>();
    final int lastItem;

    public PartSecAdapter(Activity activity, List<WardClass.Item> arrayList,
                          String type, String adType, String grossTotal, SelectionListner selectionListner) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.type = type;
        this.adType = adType;
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


        if (type.equalsIgnoreCase("lname")){
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    arrayListChoose.clear();
//                    arrayListChoose.add("Bengali");
//                    arrayListChoose.add("Hindi");
//                    arrayListChoose.add("Urdu");
//
//                    Helper.searchDialog(activity, "Set Language", arrayListChoose, keyword->{
//                        updateAllData(details.getTxt(), keyword, position, holder);
//                        MarkedItemsPreference.saveMarkedItem(activity, "Id"+position);
//                    });
//
//                    return true;
//                }
//            });

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
//                if (Helper.RemoveMarked){
//                    //itemsToRemove.add(details);
//                    arrayList.remove(position);
//                    //notifyItemRemoved(position);
//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            arrayList.remove(position);
//                            //notifyItemRemoved(position);
//                            notifyDataSetChanged();
//                        }
//                    });
//                }else {
//
//                }

                holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_dark_yellow));
                //holder.binding.voterName.setTextColor(activity.getColor(R.color.white));
                holder.binding.otherDetails.setTextColor(activity.getColor(R.color.red));
            }else {
                holder.binding.backLayout.setBackground(activity.getDrawable(R.drawable.bg_white_color_corner8));
                holder.binding.voterName.setTextColor(activity.getColor(R.color.black));
                holder.binding.otherDetails.setTextColor(activity.getColor(R.color.icon_color));
            }
        }


        if (adType != null){
            if (position >= 1){
                double similarity = Helper.getSimilarityPercentage(details.getTxt(), arrayList.get((position-1)).getTxt());
                if (similarity > 80 ){
                    text = details.getTxt();
                    total += Integer.parseInt(details.getTotal());
                }else {
                    if (text != null){
                        holder.binding.otherDetails.setText("Total - " + total + " / " + grossTotal);
                        holder.binding.voterName.setText( type.toUpperCase() + " - " + text);
                        holder.binding.slNo.setVisibility(View.VISIBLE);
                        holder.binding.slNo.setText("#"+(position+1));

                        holder.itemView.setOnClickListener(v->{
                            Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                            i.putExtra("keyword", details.getTxt());
                            i.putExtra("searchOn", type);
                            activity.startActivity(i);
                        });
                        text = null;
                    }else {
                        holder.binding.otherDetails.setText("Total - " + details.getTotal()+ " / " + grossTotal);
                        if (details.getTxt() == null || details.getTxt().isEmpty()){
                            holder.binding.voterName.setText("UNDEFINED");
                        }else {
                            holder.binding.voterName.setText( type.toUpperCase() + " - " + details.getTxt());
                        }

                        holder.binding.slNo.setVisibility(View.VISIBLE);
                        holder.binding.slNo.setText("#"+(position+1));

                        holder.itemView.setOnClickListener(v->{
                            Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                            i.putExtra("keyword", details.getTxt());
                            i.putExtra("searchOn", type);
                            activity.startActivity(i);
                        });
                    }
                }

            }else {
                holder.itemView.setOnClickListener(v->{
//                    Toast.makeText(activity, "cli", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                    i.putExtra("keyword", details.getTxt());
                    i.putExtra("searchOn", type);
                    activity.startActivity(i);
                });
            }
        }else {
            holder.binding.otherDetails.setText("Total - " + details.getTotal()+ " / " + grossTotal);
            if (details.getTxt() == null || details.getTxt().isEmpty()){
                holder.binding.voterName.setText("UNDEFINED");
            }else {
                holder.binding.voterName.setText( type.toUpperCase() + " - " + details.getTxt());
            }
            holder.binding.slNo.setVisibility(View.VISIBLE);
            holder.binding.slNo.setText("#"+(position+1));

            if (type.equalsIgnoreCase("language")){
                if (holder.binding.voterName.getText().toString().equalsIgnoreCase("UNDEFINED")){
                    holder.itemView.setOnClickListener(v->{
                        Helper.LANGUAGE = "";
                        Intent mn = new Intent(activity.getApplicationContext(), PartSectionActivity.class);
                        mn.putExtra("name", "language_Part");
                        //mn.putExtra("lan", details.getTxt());
                        mn.putExtra("lan", "");
                        activity.startActivity(mn);
                    });
                }else {
                    if (details.getTxt().equalsIgnoreCase("Hindi") || details.getTxt().equalsIgnoreCase("Bengali")){
                        holder.binding.voterName.setText( type.toUpperCase() + " - " + details.getTxt()+"vashi + Hindu");
                    }else {
                        holder.binding.voterName.setText( type.toUpperCase() + " - " + details.getTxt()+"vashi + Muslim");
                    }

                    holder.itemView.setOnClickListener(v->{
                        Intent q = new Intent(activity, PartSectionActivity.class);
                        q.putExtra("name", "language_Part");
                        q.putExtra("lan", details.getTxt());
                        activity.startActivity(q);
                    });
                }
            }else if (type.equalsIgnoreCase("language_part")){
                holder.binding.voterName.setText( "PART_NO - " + details.getTxt());
                holder.binding.otherDetails.setText("Total - " + details.getTotal()+
                        (details.getTotal_count() == null ? grossTotal : " / " + details.getTotal_count() + "   | "
                                + Helper.calculatePercentage(details.getTotal(), details.getTotal_count()) + "%"));

                holder.itemView.setOnClickListener(v->{
                    Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                    i.putExtra("keyword", Helper.LANGUAGE);
                    i.putExtra("dualSearch", details.getTxt());
                    i.putExtra("searchOn", "language");
                    activity.startActivity(i);
                });
            }else if (type.equalsIgnoreCase("ageDual")){
                holder.binding.voterName.setText( "PART_NO - " + details.getTxt());
                holder.binding.otherDetails.setText("Total - " + details.getTotal()+
                        (details.getTotal_count() == null ? grossTotal : " / " + details.getTotal_count() + "   | "
                                + Helper.calculatePercentage(details.getTotal(), details.getTotal_count()) + "%"));

                holder.itemView.setOnClickListener(v->{
                    Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                    i.putExtra("keyword", Helper.MIN_AGE + " AND " + Helper.MAX_AGE);
                    i.putExtra("dualSearch", details.getTxt());
                    i.putExtra("searchOn", "age");
                    activity.startActivity(i);
                });
            }else if (type.equalsIgnoreCase("religion")){
                holder.itemView.setOnClickListener(v->{
                    Intent q = new Intent(activity, PartSectionActivity.class);
                    q.putExtra("name", "religion_Part");
                    q.putExtra("lan", details.getTxt());
                    activity.startActivity(q);
                });
            }else if (type.equalsIgnoreCase("religion_part")){
                holder.binding.voterName.setText( "PART_NO - " + details.getTxt());
                holder.binding.otherDetails.setText("Total - " + details.getTotal()+
                        (details.getTotal_count() == null ? grossTotal : " / " + details.getTotal_count() + "   | "
                                + Helper.calculatePercentage(details.getTotal(), details.getTotal_count()) + "%"));
                holder.itemView.setOnClickListener(v->{
                    Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                    i.putExtra("keyword", Helper.LANGUAGE);
                    i.putExtra("dualSearch", details.getTxt());
                    i.putExtra("searchOn", "religion");
                    activity.startActivity(i);
                });
            }else if (type.equalsIgnoreCase("family_Part")){
                holder.binding.voterName.setText( "PART_NO - " + details.getTxt());
                holder.binding.otherDetails.setText("Total - " + details.getTotal());
                Helper.LANGUAGE = "F";


                holder.itemView.setOnClickListener(v->{
                    Intent mn = new Intent(activity.getApplicationContext(), PartSectionActivity.class);
                    mn.putExtra("name", "family_Part_Part");
                    mn.putExtra("lan", details.getTxt());
                    activity.startActivity(mn);
                });
            }else if (type.equalsIgnoreCase("family_Part_Part")){
                if (details.getTxt() == null || details.getTxt().isEmpty()){
                    holder.binding.voterName.setText("UNDEFINED");
                }else{
                    holder.binding.voterName.setText(details.getHof_name() + " - " + details.getTxt());
                }
                holder.binding.otherDetails.setText("Total - " + details.getTotal());

                holder.itemView.setOnClickListener(v->{
                    Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                    i.putExtra("keyword", details.getTxt());
                    i.putExtra("searchOn", "family");
                    activity.startActivity(i);
                });
            }else if (type.equalsIgnoreCase("Dead_part") || type.equalsIgnoreCase("Relocated_part")){
                holder.binding.voterName.setText( "PART_NO - " + details.getTxt());
                holder.binding.otherDetails.setText("Total - " + details.getTotal()+
                        (details.getTotal_count() == null ? grossTotal : " / " + details.getTotal_count() + "   | "
                                + Helper.calculatePercentage(details.getTotal(), details.getTotal_count()) + "%"));
                holder.itemView.setOnClickListener(v->{
                    Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                    i.putExtra("keyword", Helper.LANGUAGE);
                    i.putExtra("dualSearch", details.getTxt());
                    i.putExtra("searchOn", "status");
                    activity.startActivity(i);
                });
            } else if (type.equalsIgnoreCase("intereset_party")){
                holder.binding.voterName.setText( "PART_NO - " + details.getTxt());
                holder.binding.otherDetails.setText("Total - " + details.getTotal()+
                        (details.getTotal_count() == null ? grossTotal : " / " + details.getTotal_count() + "   | "
                                + Helper.calculatePercentage(details.getTotal(), details.getTotal_count()) + "%"));
                holder.itemView.setOnClickListener(v->{
                    Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                    i.putExtra("keyword", Helper.LANGUAGE);
                    i.putExtra("dualSearch", details.getTxt());
                    i.putExtra("searchOn", "intereset_party");
                    activity.startActivity(i);
                });
            }else if (type.equalsIgnoreCase("hof_part")){
                holder.binding.voterName.setText( "PART_NO - " + details.getTxt());
                holder.binding.otherDetails.setText("Total - " + details.getTotal()+
                        (details.getTotal_count() == null ? grossTotal : " / " + details.getTotal_count() + "   | "
                                + Helper.calculatePercentage(details.getTotal(), details.getTotal_count()) + "%"));
                holder.itemView.setOnClickListener(v->{
                    Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                    i.putExtra("keyword", Helper.LANGUAGE);
                    i.putExtra("dualSearch", details.getTxt());
                    i.putExtra("searchOn", "hof");
                    activity.startActivity(i);
                });
            }else if (type.equalsIgnoreCase("doa_part")){
                holder.binding.voterName.setText( "PART_NO - " + details.getTxt());
                holder.binding.otherDetails.setText("Total - " + details.getTotal()+
                        (details.getTotal_count() == null ? grossTotal : " / " + details.getTotal_count() + "   | "
                                + Helper.calculatePercentage(details.getTotal(), details.getTotal_count()) + "%"));
                holder.itemView.setOnClickListener(v->{
                    Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                    i.putExtra("keyword", Helper.LANGUAGE);
                    i.putExtra("dualSearch", details.getTxt());
                    i.putExtra("searchOn", "doa");
                    activity.startActivity(i);
                });
            }else if (type.equalsIgnoreCase("dob_part")){
                holder.binding.voterName.setText( "PART_NO - " + details.getTxt());
                holder.binding.otherDetails.setText("Total - " + details.getTotal()+
                        (details.getTotal_count() == null ? grossTotal : " / " + details.getTotal_count() + "   | "
                                + Helper.calculatePercentage(details.getTotal(), details.getTotal_count()) + "%"));
                holder.itemView.setOnClickListener(v->{
                    Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                    i.putExtra("keyword", Helper.LANGUAGE);
                    i.putExtra("dualSearch", details.getTxt());
                    i.putExtra("searchOn", "dob");
                    activity.startActivity(i);
                });
            }else if (type.equalsIgnoreCase("part_no")) {
                holder.binding.otherDetails.setText("Total - " + details.getTotal());
                Helper.PART_NO = details.getTxt();

                holder.itemView.setOnClickListener(v->{
                    Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                    i.putExtra("keyword", details.getTxt());
                    i.putExtra("searchOn", type);
                    activity.startActivity(i);
                });
            } else if (type.equalsIgnoreCase("address")) {

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
                    Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                    i.putExtra("keyword", details.getTxt());
                    i.putExtra("searchOn", type);
                    activity.startActivity(i);
                });
            }else {
                holder.itemView.setOnClickListener(v->{
//                    Toast.makeText(activity, "cli2", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(activity.getApplicationContext(), SearchActivity.class);
                    i.putExtra("keyword", details.getTxt());
                    i.putExtra("searchOn", type);
                    activity.startActivity(i);
                });
            }

        }


        //Toast.makeText(activity, ""+ lastItem, Toast.LENGTH_SHORT).show();
//        if (lastItem != 0 && lastItem == (position+1)){
//            //handleItemRemoval();
//            if (itemsToRemove!= null && !itemsToRemove.isEmpty()){
//                removeMatchedList(arrayList, itemsToRemove);
//            }
//        }



    }

    private void handleItemRemoval() {
        if (itemsToRemove.isEmpty()) return;

        for (WardClass.Item item : itemsToRemove) {
            int position = arrayList.indexOf(item);
            if (position >= 0) {
                arrayList.remove(position);
                notifyItemRemoved(position);
            }
        }
        itemsToRemove.clear();
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
                              int position, ViewHolder holder){
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
