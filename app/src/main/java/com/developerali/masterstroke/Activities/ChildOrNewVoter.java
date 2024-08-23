package com.developerali.masterstroke.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.Helper;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ActivityChildOrNewVoterBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class ChildOrNewVoter extends AppCompatActivity {

    ActivityChildOrNewVoterBinding binding;
    PhoneAddressModel.Item  details;
    ProgressDialog progressDialog;
    String type, childClass, typeTxt;
    ArrayList<String> classes = new ArrayList<>();
    Calendar calendar;
    String[] types = {"Student", "New Voter"};
    private boolean isLoading = false;
    private boolean noMoreLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildOrNewVoterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Students or New Voters");

        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        Helper.WARD = sharedPreferences.getString("ward_id", "0");

        progressDialog = new ProgressDialog(ChildOrNewVoter.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("updating data...");

        Intent intent = getIntent();
        if (intent.hasExtra("details")){
            details = intent.getParcelableExtra("details");
        }

        Helper.getWardName(Helper.WARD, binding.wardName);

        ArrayAdapter<String> obj3 = new ArrayAdapter<String>(ChildOrNewVoter.this, R.layout.layout_spinner_item, types);
        binding.spinnerS.setAdapter(obj3);
        typeTxt = types[0];

        binding.spinnerS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeTxt = types[position];
                getSupportActionBar().setTitle("Add " + typeTxt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
















    }

    @Override
    protected void onStart() {
        Helper.getUserLogin(ChildOrNewVoter.this);
        super.onStart();
    }

    public void hideKeyboard() {
        Activity activity = ChildOrNewVoter.this;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        //binding.pageText.setCursorVisible(false);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(layoutManager);

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                    // Load more data here
                    if (!noMoreLoad){
                        isLoading = true;
                        binding.progressBar.setVisibility(View.VISIBLE);

//                        if (!normalNextPage) {
//                            getSearchVoters(nextPageToken, searchKeyword, searchOn);
//                        } else {
//                            getDetails(nextPageToken);
//                        }

                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addSomething){

            Intent i = new Intent(ChildOrNewVoter.this, ChildActivity.class);
            i.putExtra("details", details);
            startActivity(i);
        }else if (item.getItemId() == R.id.refreshSomething){
            Toast.makeText(this, "ref", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}