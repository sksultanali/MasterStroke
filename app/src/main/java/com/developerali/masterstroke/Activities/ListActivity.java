package com.developerali.masterstroke.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.developerali.masterstroke.Adapters.myListAdapter;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.databinding.ActivityListBinding;
import com.developerali.masterstroke.databinding.DialogListSearchBinding;
import com.developerali.masterstroke.databinding.DialogTextInputBinding;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ActivityListBinding binding;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayListChoose = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        Helper.WARD = sharedPreferences.getString("ward_id", "0");

        getSupportActionBar().setTitle("List Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        arrayList.clear();
        arrayList.add("Complete list"); //0
        arrayList.add("By Address"); //1
        arrayList.add("By Booth"); //2
        arrayList.add("By Name");  //3
        arrayList.add("By Voter Id No"); //4
        arrayList.add("By House No"); //4
        if (Helper.ADMIN_APPLICATION){
            arrayList.add("By Age"); //5
            arrayList.add("By Language"); //6
            arrayList.add("By Religion"); //7
            arrayList.add("By Last Name");  //8
            arrayList.add("By Sex");  //9
            arrayList.add("By Section");  //10
            arrayList.add("By Mobile No List"); //11
            arrayList.add("Dead");  //12
            arrayList.add("By Common Name"); //13
        }


        myListAdapter adapter = new myListAdapter(ListActivity.this, arrayList, true);
        binding.toolsList.setAdapter(adapter);

        binding.toolsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((position)){
                    case 0:
                        startActivity(new Intent(ListActivity.this, SearchActivity.class));
                        break;
                    case 1:
                        Intent l = new Intent(ListActivity.this, PartSectionActivity.class);
                        l.putExtra("name", "address");
                        startActivity(l);
                        break;
                    case 2:
                        Intent k = new Intent(ListActivity.this, PartSectionActivity.class);
                        k.putExtra("name", "part_no");
                        startActivity(k);
                    case 3:
                        searchDialog("name", keyword->{
                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
                            i.putExtra("keyword", keyword);
                            i.putExtra("searchOn", "name");
                            startActivity(i);
                        });
                        break;
                    case 4:
                        searchDialog("voter_id", keyword->{
                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
                            i.putExtra("keyword", keyword);
                            i.putExtra("searchOn", "voter_id");
                            startActivity(i);
                        });
                        break;
                    case 5:
                        searchDialog("house", keyword->{
                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
                            i.putExtra("keyword", keyword);
                            i.putExtra("searchOn", "house");
                            startActivity(i);
                        });
                        break;
                    case 6:
                        searchDialog("age", keyword->{
                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
                            i.putExtra("keyword", keyword);
                            i.putExtra("searchOn", "age");
                            startActivity(i);
                        });
                        break;
                    case 7:
                        //Religion
                        Intent q = new Intent(ListActivity.this, PartSectionActivity.class);
                        q.putExtra("name", "language");
                        Helper.RemoveMarked = true;
                        startActivity(q);
                        break;
                    case 8:
                        Intent m = new Intent(ListActivity.this, PartSectionActivity.class);
                        m.putExtra("name", "religion");
                        startActivity(m);
                        break;
                    case 9:
                        Intent mn = new Intent(ListActivity.this, PartSectionActivity.class);
                        Helper.MARKING_ENABLE = true;
                        Helper.RemoveMarked = false;
                        mn.putExtra("name", "lname");
                        startActivity(mn);
                        break;
                    case 10:
                        Intent s = new Intent(ListActivity.this, PartSectionActivity.class);
                        s.putExtra("name", "sex");
                        startActivity(s);
                        break;
                    case 11:
                        Intent j = new Intent(ListActivity.this, PartSectionActivity.class);
                        j.putExtra("name", "section");
                        startActivity(j);
                        break;
                    case 12:
                        Intent i = new Intent(ListActivity.this, SearchActivity.class);
                        i.putExtra("keyword", "");
                        i.putExtra("searchOn", "mobile");
                        startActivity(i);
                        break;
                    case 13:
                        Intent ml = new Intent(ListActivity.this, PartSectionActivity.class);
                        ml.putExtra("name", "Dead_Part");
                        ml.putExtra("lan", "Dead");
                        startActivity(ml);
                        break;
                    case 14:
                        Intent mno = new Intent(ListActivity.this, PartSectionActivity.class);
                        mno.putExtra("name", "lname");
                        mno.putExtra("merge", "cname");
                        startActivity(mno);
                        break;
                }
            }
        });

    }

    private void searchDialog(String searchOn, DialogCallback callback) {
        DialogTextInputBinding dialogBinding = DialogTextInputBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogBinding.getRoot());
        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialogBinding.btn.setText("Search");

        if (searchOn.equalsIgnoreCase("age")){
            dialogBinding.minMax.setVisibility(View.VISIBLE);
            dialogBinding.inputHint.setVisibility(View.GONE);

            dialogBinding.btn.setOnClickListener(v -> {
                String min = dialogBinding.minValue.getText().toString();
                String max = dialogBinding.maxValue.getText().toString();

                if (min.isEmpty()) {
                    dialogBinding.minValue.setError("*");
                } else if (max.isEmpty()){
                    dialogBinding.maxValue.setError("*");
                } else if (Integer.parseInt(min) > Integer.parseInt(max)){
                    Helper.showCustomMessage(ListActivity.this, "Error 402!",
                            "Minimum age must be lesser then maximum age. Recheck before search..!");
                }else {
                    dialog.dismiss();
                    callback.onResult(min + " AND " + max);
                }
            });

        }else {
            dialogBinding.minMax.setVisibility(View.GONE);
            dialogBinding.inputHint.setVisibility(View.VISIBLE);
            dialogBinding.inputHint.setHint(searchOn);

            dialogBinding.btn.setOnClickListener(v -> {
                String text = dialogBinding.textInputL.getText().toString();
                if (text.isEmpty()) {
                    dialogBinding.textInputL.setError("*");
                } else {
                    dialog.dismiss();
                    callback.onResult(text);
                }
            });
        }
        dialog.show();
    }

    private void searchDialog(String searchOn,ArrayList<String> arrayListChoose, DialogCallback callback) {
        DialogListSearchBinding dialogBinding = DialogListSearchBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogBinding.getRoot());
        // Create and show the dialog
        AlertDialog dialog = builder.create();

        dialogBinding.searchOn.setText(searchOn);

        myListAdapter adapter = new myListAdapter(ListActivity.this, arrayListChoose, false);
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

    @Override
    protected void onStart() {
        Helper.getUserLogin(ListActivity.this);
        super.onStart();
    }

    public interface DialogCallback {
        void onResult(String keyword);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


}