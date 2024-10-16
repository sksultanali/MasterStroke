package com.developerali.masterstroke.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.developerali.masterstroke.Adapters.myListAdapter;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ActivitySurveyBinding;

import java.util.ArrayList;

public class SurveyActivity extends AppCompatActivity {

    ActivitySurveyBinding binding;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        binding = ActivitySurveyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Survey Activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        arrayList.clear();
        arrayList.add(getString(R.string.surveyss));
        arrayList.add(getString(R.string.export_survey));
        arrayList.add(getString(R.string.import_surveyss));
        arrayList.add(getString(R.string.upcomingVoters));
        arrayList.add(getString(R.string.viewVoted));
        arrayList.add(getString(R.string.clear_voted_marking));

        myListAdapter adapter = new myListAdapter(SurveyActivity.this, arrayList, true);
        binding.toolsList.setAdapter(adapter);

        binding.toolsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
//                        Intent l = new Intent(SurveyActivity.this, PartSectionActivity.class);
//                        l.putExtra("name", "intereset_party");
//                        startActivity(l);
                        startActivity(new Intent(SurveyActivity.this, PartiesActivity.class));
                        break;
                    case 1:
                        //startActivity(new Intent(MainActivity.this, HistoryBooks.class));
                        break;
                    case 2:
                        //startActivity(new Intent(MainActivity.this, PaymentActivity.class));
                        break;
                    case 3:
                        //startActivity(new Intent(MainActivity.this, DonationActivity.class));
                        break;
                    case 4:
                        //Toast.makeText(MainActivity.this, "not available...!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}