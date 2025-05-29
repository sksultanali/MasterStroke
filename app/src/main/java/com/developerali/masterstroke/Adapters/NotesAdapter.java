package com.developerali.masterstroke.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.masterstroke.Activities.AddNotesActivity;
import com.developerali.masterstroke.Helpers.DB_Helper;
import com.developerali.masterstroke.Models.NotesModel;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ChildVoterBinding;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{

    Activity activity;
    ArrayList<NotesModel> arrayList;
    DB_Helper dbHelper;

    public NotesAdapter(Activity activity, ArrayList<NotesModel> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
        dbHelper = new DB_Helper(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.child_voter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NotesModel notesModel = arrayList.get(position);

        holder.binding.voterName.setText(notesModel.getTitle()+"\n"+notesModel.getDate());
        if (notesModel.getDescription().length() > 150) {
            holder.binding.otherDetails.setText(notesModel.getDescription().substring(0, 150));
        }else {
            holder.binding.otherDetails.setText(notesModel.getDescription());
        }

        holder.itemView.setOnClickListener(v->{
            Intent i = new Intent(activity.getApplicationContext(), AddNotesActivity.class);
            i.putExtra("title", notesModel.getTitle());
            i.putExtra("des", notesModel.getDescription());
            activity.startActivity(i);
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(activity, view);
                popupMenu.getMenu().add("Remove");
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        dbHelper.deleteSearchQuery(notesModel.getTitle());
                        //removing from list onBindViewHolder
                        arrayList.remove(position);
                        notifyDataSetChanged();
                        return true;
                    }
                });
                return true;
            }
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
