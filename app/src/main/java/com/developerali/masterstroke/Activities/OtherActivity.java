package com.developerali.masterstroke.Activities;

import android.content.Intent;
import android.net.Uri;
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

import com.developerali.masterstroke.Adapters.myListAdapter;
import com.developerali.masterstroke.Helper;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ActivityOtherBinding;

import java.util.ArrayList;

public class OtherActivity extends AppCompatActivity {

    ActivityOtherBinding binding;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        binding = ActivityOtherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Other Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        arrayList.clear();
        //arrayList.add("Survey"); //0
        //arrayList.add("Export Survey"); //1
        arrayList.add("Survey"); //2
        arrayList.add("New Voters");  //3
        arrayList.add("Child Voters");  //4
        arrayList.add("Relocated Voters");  //5
        arrayList.add("Whatsapp Message Forward");  //6
        arrayList.add("Tele Calling Facility");  //7
        arrayList.add("Today Birthday");  //8
        arrayList.add("Today Anniversary");  //9
        arrayList.add("Set Parties");  //10

        myListAdapter adapter = new myListAdapter(OtherActivity.this, arrayList);
        binding.toolsList.setAdapter(adapter);

        binding.toolsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((position+2)){
                    case 2:
                        Intent lj = new Intent(OtherActivity.this, PartSectionActivity.class);
                        lj.putExtra("name", "intereset_party");
                        startActivity(lj);
                        break;
                    case 3:
                        Intent lij = new Intent(OtherActivity.this, ChildOrNewVoter.class);
                        lij.putExtra("type", "New Voter");
                        startActivity(lij);
                        break;
                    case 4:
                        Intent lik = new Intent(OtherActivity.this, ChildOrNewVoter.class);
                        lik.putExtra("type", "Student");
                        startActivity(lik);
                        break;
                    case 5:
                        Intent ik = new Intent(OtherActivity.this, SearchActivity.class);
                        ik.putExtra("keyword", "Relocated");
                        ik.putExtra("searchOn", "status");
                        startActivity(ik);
                        break;
                    case 6:
                        if (Helper.isChromeCustomTabsSupported(OtherActivity.this)){
                            Helper.openChromeTab("https://whatsapp2.bulksmsserver.in/", OtherActivity.this);
                        }else {
                            Intent i = new Intent(OtherActivity.this, WebView.class);
                            i.putExtra("share" ,"https://whatsapp2.bulksmsserver.in/");
                            startActivity(i);
                        }
                        break;
                    case 7:
                        if (Helper.isChromeCustomTabsSupported(OtherActivity.this)){
                            Helper.openChromeTab("http://obd2.bulksmsserver.in/", OtherActivity.this);
                        }else {
                            Intent i = new Intent(OtherActivity.this, WebView.class);
                            i.putExtra("share" ,"http://obd2.bulksmsserver.in/");
                            startActivity(i);
                        }
                        break;
                    case 8:
                        Intent i = new Intent(OtherActivity.this, SearchActivity.class);
                        i.putExtra("keyword", Helper.getToday());
                        i.putExtra("searchOn", "dob");
                        startActivity(i);
                        break;
                    case 9:
                        Intent j = new Intent(OtherActivity.this, SearchActivity.class);
                        j.putExtra("keyword", Helper.getToday());
                        j.putExtra("searchOn", "doa");
                        startActivity(j);
                        break;
                    case 10:
                        startActivity(new Intent(OtherActivity.this, PartiesActivity.class));
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