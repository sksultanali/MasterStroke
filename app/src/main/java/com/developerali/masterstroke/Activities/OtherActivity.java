package com.developerali.masterstroke.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.developerali.masterstroke.Adapters.myListAdapter;
import com.developerali.masterstroke.Helpers.Helper;
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

        getSupportActionBar().setTitle(R.string.other_options);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        arrayList.clear();
        arrayList.add(getString(R.string.party_workers)); //0
        arrayList.add(getString(R.string.headOfFamily)); //1
        arrayList.add(getString(R.string.ourPartySupporters)); //2
        arrayList.add(getString(R.string.upcomingVoters));  //3
        arrayList.add(getString(R.string.studen));  //4
        arrayList.add(getString(R.string.locatedVoters));  //5
        //arrayList.add(getString(R.string.whatsForward));  //6
        //arrayList.add(getString(R.string.tele_calling_facility));  //7
        arrayList.add(getString(R.string.today_birthday_all));  //8
        arrayList.add(getString(R.string.today_anniversary_all));  //9
        arrayList.add(getString(R.string.tomorrow_birthday_all));  //10
        arrayList.add(getString(R.string.tomorrow_anniversary_all));  //11
        arrayList.add(getString(R.string.today_birthday_part_wise));  //8
        arrayList.add(getString(R.string.today_anniversary_part_wise));  //9
        arrayList.add(getString(R.string.tomorrow_birthday_part_wise));  //10
        arrayList.add(getString(R.string.tomorrow_anniversary_part_wise));  //11
        arrayList.add(getString(R.string.party_surveys));  //12
        arrayList.add(getString(R.string.dead));  //12

        myListAdapter adapter = new myListAdapter(OtherActivity.this, arrayList, true);
        binding.toolsList.setAdapter(adapter);

        binding.toolsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((position)){
                    case 0:
                        Intent i0k = new Intent(OtherActivity.this, SearchActivity.class);
                        i0k.putExtra("keyword", "Yes");
                        i0k.putExtra("searchOn", "party_worker");
                        startActivity(i0k);
                        break;
                    case 1:
                        Intent mll1 = new Intent(OtherActivity.this, PartSectionActivity.class);
                        mll1.putExtra("name", "hof_Part");
                        mll1.putExtra("lan", "Yes");
                        startActivity(mll1);
//                        Intent ik = new Intent(OtherActivity.this, SearchActivity.class);
//                        ik.putExtra("keyword", "Yes");
//                        ik.putExtra("searchOn", "hof");
//                        startActivity(ik);
                        break;
                    case 2:
                        Intent lj = new Intent(OtherActivity.this, PartSectionActivity.class);
                        lj.putExtra("name", "intereset_party");
                        lj.putExtra("lan", "Our Party");
                        startActivity(lj);
                        break;
                    case 3:
                        Intent lij = new Intent(OtherActivity.this, ChildOrNewVoter.class);
                        lij.putExtra("type", "Upcoming Voter");
                        startActivity(lij);
                        break;
                    case 4:
                        Intent lik = new Intent(OtherActivity.this, ChildOrNewVoter.class);
                        lik.putExtra("type", "Student");
                        startActivity(lik);
                        break;
                    case 5:
                        Intent mll = new Intent(OtherActivity.this, PartSectionActivity.class);
                        mll.putExtra("name", "Relocated_Part");
                        mll.putExtra("lan", "Relocated");
                        startActivity(mll);
//                        Intent ixk = new Intent(OtherActivity.this, SearchActivity.class);
//                        ixk.putExtra("keyword", "Relocated");
//                        ixk.putExtra("searchOn", "status");
//                        startActivity(ixk);
                        break;
//                    case 6:
//                        if (Helper.isChromeCustomTabsSupported(OtherActivity.this)){
//                            Helper.openChromeTab("https://whatsapp2.bulksmsserver.in/", OtherActivity.this);
//                        }else {
//                            Intent i = new Intent(OtherActivity.this, WebView.class);
//                            i.putExtra("share" ,"https://whatsapp2.bulksmsserver.in/");
//                            startActivity(i);
//                        }
//                        break;
//                    case 7:
//                        if (Helper.isChromeCustomTabsSupported(OtherActivity.this)){
//                            Helper.openChromeTab("https://obd2.bulksmsserver.in/", OtherActivity.this);
//                        }else {
//                            Intent i = new Intent(OtherActivity.this, WebView.class);
//                            i.putExtra("share" ,"https://obd2.bulksmsserver.in/");
//                            startActivity(i);
//                        }
//                        break;
                    case 6:
                        Intent i = new Intent(OtherActivity.this, SearchActivity.class);
                        i.putExtra("keyword", Helper.getToday());
                        i.putExtra("searchOn", "dob");
                        startActivity(i);
                        break;
                    case 7:
                        Intent j = new Intent(OtherActivity.this, SearchActivity.class);
                        j.putExtra("keyword", Helper.getToday());
                        j.putExtra("searchOn", "doa");
                        startActivity(j);
                        break;
                    case 8:
                        Intent ij = new Intent(OtherActivity.this, SearchActivity.class);
                        ij.putExtra("keyword", Helper.getTomorrowDate());
                        ij.putExtra("searchOn", "dob");
                        startActivity(ij);
                        break;
                    case 9:
                        Intent jkl = new Intent(OtherActivity.this, SearchActivity.class);
                        jkl.putExtra("keyword", Helper.getTomorrowDate());
                        jkl.putExtra("searchOn", "doa");
                        startActivity(jkl);
                        break;
                    case 10:
                        Intent ml4 = new Intent(OtherActivity.this, PartSectionActivity.class);
                        ml4.putExtra("name", "Dob_Part");
                        ml4.putExtra("lan", Helper.getToday());
                        startActivity(ml4);
//                        Intent i = new Intent(OtherActivity.this, SearchActivity.class);
//                        i.putExtra("keyword", Helper.getToday());
//                        i.putExtra("searchOn", "dob");
//                        startActivity(i);
                        break;
                    case 11:
                        Intent ml5 = new Intent(OtherActivity.this, PartSectionActivity.class);
                        ml5.putExtra("name", "Doa_Part");
                        ml5.putExtra("lan", Helper.getToday());
                        startActivity(ml5);
//                        Intent j = new Intent(OtherActivity.this, SearchActivity.class);
//                        j.putExtra("keyword", Helper.getToday());
//                        j.putExtra("searchOn", "doa");
//                        startActivity(j);
                        break;
                    case 12:
                        Intent ml6 = new Intent(OtherActivity.this, PartSectionActivity.class);
                        ml6.putExtra("name", "Dob_Part");
                        ml6.putExtra("lan", Helper.getTomorrowDate());
                        startActivity(ml6);
//                        Intent ij = new Intent(OtherActivity.this, SearchActivity.class);
//                        ij.putExtra("keyword", Helper.getTomorrowDate());
//                        ij.putExtra("searchOn", "dob");
//                        startActivity(ij);
                        break;
                    case 13:
                        Intent ml7 = new Intent(OtherActivity.this, PartSectionActivity.class);
                        ml7.putExtra("name", "Doa_Part");
                        ml7.putExtra("lan", Helper.getTomorrowDate());
                        startActivity(ml7);
//                        Intent jkl = new Intent(OtherActivity.this, SearchActivity.class);
//                        jkl.putExtra("keyword", Helper.getTomorrowDate());
//                        jkl.putExtra("searchOn", "doa");
//                        startActivity(jkl);
                        break;
                    case 14:
                        startActivity(new Intent(OtherActivity.this, PartiesActivity.class));
                        break;
                    case 15:
                        Intent ml = new Intent(OtherActivity.this, PartSectionActivity.class);
                        ml.putExtra("name", "Dead_Part");
                        ml.putExtra("lan", "Dead");
                        startActivity(ml);
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