package com.developerali.masterstroke.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developerali.masterstroke.Adapters.myListAdapter;
import com.developerali.masterstroke.Helper;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ActivityPartiesBinding;

import java.util.ArrayList;

public class PartiesActivity extends AppCompatActivity {

    ActivityPartiesBinding binding;
    ArrayList<String>arrayList;
    myListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPartiesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Set Parties");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        arrayList = Helper.getPartyList(PartiesActivity.this);
        if (arrayList == null){
            arrayList = new ArrayList<>();
        }
        adapter = new myListAdapter(PartiesActivity.this, arrayList);
        binding.listItems.setAdapter(adapter);

        binding.btnSave.setOnClickListener(v->{
            String partyName = binding.partyName.getText().toString();
            if (partyName.isEmpty()){
                binding.partyName.setError("*");
            }else{
                arrayList.add(partyName);
                Helper.savePartyList(PartiesActivity.this, arrayList);
                binding.partyName.setText("");
                adapter.notifyDataSetChanged();
            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteMenu){
            showAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PartiesActivity.this);
        builder.setMessage("Are sure want to delete all parties?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Helper.clearPartyList(PartiesActivity.this);
                arrayList.clear();
                adapter.notifyDataSetChanged();
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}