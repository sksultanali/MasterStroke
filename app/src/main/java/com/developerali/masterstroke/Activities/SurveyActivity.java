package com.developerali.masterstroke.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        arrayList.add("Survey");
        arrayList.add("Export Survey");
        arrayList.add("Import Survey");
        arrayList.add("New Voters");
        arrayList.add("View Voted");
        arrayList.add("Clear Voted Marking");

        myListAdapter adapter = new myListAdapter();
        binding.toolsList.setAdapter(adapter);

        binding.toolsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //startActivity(new Intent(MainActivity.this, MainActivity.class));
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

    public class myListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater obj = getLayoutInflater();
            View view1 = obj.inflate(R.layout.sample_tools_vertical, null);

            TextView textView = view1.findViewById(R.id.toolName);
            textView.setText(arrayList.get(i));

            return view1;
        }
    }
}