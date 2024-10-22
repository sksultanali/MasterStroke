package com.developerali.masterstroke;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.developerali.masterstroke.Activities.ChartsActivity;
import com.developerali.masterstroke.Activities.ListActivity;
import com.developerali.masterstroke.Activities.LoginActivity;
import com.developerali.masterstroke.Activities.OtherActivity;
import com.developerali.masterstroke.Activities.SearchActivity;
import com.developerali.masterstroke.Activities.SurveyActivity;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.Models.ToolsModel;
import com.developerali.masterstroke.databinding.ActivityMainBinding;
import com.developerali.masterstroke.databinding.DialogChooseLanguageBinding;
import com.mannan.translateapi.Language;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<ToolsModel> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);`
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String currentLanguage = Helper.getLanguagePreference(MainActivity.this);
        String defaultLanguage = getResources().getConfiguration().locale.getLanguage();
        if (!currentLanguage.equals(defaultLanguage)) {
            Helper.updateLocale(MainActivity.this, currentLanguage);
        }

        //make below type false if want worker app
        Helper.ADMIN_APPLICATION =  true;
        Helper.WB =  false;


        if (Helper.CANDIDATE != null){
            getSupportActionBar().setTitle(Helper.CANDIDATE);
        }

        arrayList.clear();
        arrayList.add(new ToolsModel(getString(R.string.search), getDrawable(R.drawable.search)));
        arrayList.add(new ToolsModel(getString(R.string.list), getDrawable(R.drawable.list)));
        arrayList.add(new ToolsModel(getString(R.string.languages), getDrawable(R.drawable.google_translate)));
        if (Helper.ADMIN_APPLICATION){
            binding.officerTag.setVisibility(View.GONE);
            binding.imView.setImageDrawable(getDrawable(R.drawable.pubimg_min));
            arrayList.add(new ToolsModel(getString(R.string.surveyss), getDrawable(R.drawable.survey)));
            arrayList.add(new ToolsModel(getString(R.string.chartsmao), getDrawable(R.drawable.charts)));
            arrayList.add(new ToolsModel(getString(R.string.erssss), getDrawable(R.drawable.more)));
        }else {
            binding.officerTag.setVisibility(View.VISIBLE);
            if (Helper.HOME_LINK.equalsIgnoreCase("NA")) {
                binding.imView.setImageDrawable(getDrawable(R.drawable.worker_image));
            }else {
                Glide.with(MainActivity.this)
                        .load(Helper.HOME_LINK)
                        .placeholder(getDrawable(R.drawable.worker_image))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(binding.imView);
            }
            binding.officerTag.setAnimation(AnimationUtils.loadAnimation(this, R.anim.blink_animation));
        }

        myListAdapter adapter = new myListAdapter();
        binding.toolsList.setAdapter(adapter);

        binding.toolsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(MainActivity.this, SearchActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, ListActivity.class));
                        break;
                    case 2:
                        showBottomDialog();
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, SurveyActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, ChartsActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, OtherActivity.class));
                        break;
                }
            }
        });

        ArrayList<String> arrayList = Helper.getPartyList(MainActivity.this);
        if (arrayList == null){
            arrayList = new ArrayList<>();
            arrayList.add("Our Party");
            arrayList.add("Opposition Party");
            arrayList.add("Doubtful");
            Helper.savePartyList(MainActivity.this, arrayList);
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
//        if (itemId == R.id.translate) {
//            showBottomDialog();
//        } else

        if (itemId == R.id.logout) {
            //Helper.saveLanguagePreference(MainActivity.this, "bn");
            Helper.clearSharedPreferences(MainActivity.this);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void showBottomDialog() {
        DialogChooseLanguageBinding chooseBinding = DialogChooseLanguageBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(this);
        dialog.setContentView(chooseBinding.getRoot());

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        if (Helper.getLanguagePreference(MainActivity.this).equalsIgnoreCase("hi")){
            chooseBinding.hindi.setChecked(true);
        } else if (Helper.getLanguagePreference(MainActivity.this).equalsIgnoreCase("bn")) {
            chooseBinding.bengali.setChecked(true);
        }else if (Helper.getLanguagePreference(MainActivity.this).equalsIgnoreCase("bn")){
            chooseBinding.english.setChecked(true);
        }else if (Helper.getLanguagePreference(MainActivity.this).equalsIgnoreCase("ur")){
            chooseBinding.urdu.setChecked(true);
        }

        chooseBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.english) {
                    Helper.saveLanguagePreference(MainActivity.this, "en");
                    Helper.updateLocale(MainActivity.this, Helper.getLanguagePreference(MainActivity.this));
                    dialog.dismiss();
                    recreate();
                } else if (checkedId == R.id.hindi) {
                    Helper.saveLanguagePreference(MainActivity.this, "hi");
                    Helper.updateLocale(MainActivity.this, Helper.getLanguagePreference(MainActivity.this));
                    dialog.dismiss();
                    recreate();
                }else if (checkedId == R.id.urdu) {
                    Helper.saveLanguagePreference(MainActivity.this, Language.URDU);
                    Helper.updateLocale(MainActivity.this, Helper.getLanguagePreference(MainActivity.this));
                    dialog.dismiss();
                    recreate();
                }else {
                    Helper.saveLanguagePreference(MainActivity.this, "bn");
                    Helper.updateLocale(MainActivity.this, Helper.getLanguagePreference(MainActivity.this));
                    dialog.dismiss();
                    recreate();
                }
            }
        });

        dialog.show();
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
            View view1 = obj.inflate(R.layout.sample_tools, null);
            ImageView imageView = view1.findViewById(R.id.toolImg);
            TextView textView = view1.findViewById(R.id.toolName);

            ToolsModel toolsModel = arrayList.get(i);
            textView.setText(toolsModel.getName());
            imageView.setImageDrawable(toolsModel.getDrawable());

            return view1;
        }
    }
}