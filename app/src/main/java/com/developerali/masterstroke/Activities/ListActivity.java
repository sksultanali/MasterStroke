package com.developerali.masterstroke.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developerali.masterstroke.Adapters.myListAdapter;
import com.developerali.masterstroke.Helper;
import com.developerali.masterstroke.MainActivity;
import com.developerali.masterstroke.Models.ToolsModel;
import com.developerali.masterstroke.R;
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
        arrayList.add("Advance Search"); //0
        arrayList.add("By Ward"); //1
        arrayList.add("By Address"); //2
        arrayList.add("By Booth"); //3
        arrayList.add("By Age"); //4
        arrayList.add("By Language"); //5
        arrayList.add("By Religion"); //6
        arrayList.add("By Village");  //7
        arrayList.add("By Sex");  //8
        arrayList.add("By Name");  //9
        arrayList.add("By Section");  //10
        arrayList.add("By Voter Id No"); //11
        arrayList.add("By Mobile No List"); //12
        arrayList.add("Dead");  //13
        arrayList.add("Important Voters"); //14

        myListAdapter adapter = new myListAdapter(ListActivity.this, arrayList);
        binding.toolsList.setAdapter(adapter);

        binding.toolsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //startActivity(new Intent(MainActivity.this, MainActivity.class));
                        break;
                    case 1:
                        searchDialog("ward", keyword->{
                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
                            i.putExtra("keyword", keyword);
                            i.putExtra("searchOn", "ward");
                            startActivity(i);
                        });
                        break;
                    case 2:
                        Intent l = new Intent(ListActivity.this, PartSectionActivity.class);
                        l.putExtra("name", "address");
                        startActivity(l);
//                        searchDialog("address", keyword->{
//                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
//                            i.putExtra("keyword", keyword);
//                            i.putExtra("searchOn", "address");
//                            startActivity(i);
//                        });
                        break;
                    case 3:
                        Intent k = new Intent(ListActivity.this, PartSectionActivity.class);
                        k.putExtra("name", "part_no");
                        startActivity(k);
//                        searchDialog("part_no", keyword->{
//                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
//                            i.putExtra("keyword", keyword);
//                            i.putExtra("searchOn", "part_no");
//                            startActivity(i);
//                        });
                        break;
                    case 4:
                        searchDialog("age", keyword->{
                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
                            i.putExtra("keyword", keyword);
                            i.putExtra("searchOn", "age");
                            startActivity(i);
                        });
                        break;
                    case 5:
                        //Language
                        Intent q = new Intent(ListActivity.this, PartSectionActivity.class);
                        q.putExtra("name", "language");
                        startActivity(q);
//                        arrayListChoose.clear();
//                        arrayListChoose.add("Bengali");
//                        arrayListChoose.add("Hindi");
//                        arrayListChoose.add("Urdu");
//
//                        searchDialog("Language", arrayListChoose, keyword->{
//                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
//                            i.putExtra("keyword", keyword);
//                            i.putExtra("searchOn", "language");
//                            startActivity(i);
//                        });
                        break;
                    case 6:
                        //Religion
//                        arrayListChoose.clear();
//                        arrayListChoose.add("Hindu");
//                        arrayListChoose.add("Muslim");
//
//                        searchDialog("Religion", arrayListChoose, keyword->{
//                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
//                            i.putExtra("keyword", keyword);
//                            i.putExtra("searchOn", "religion");
//                            startActivity(i);
//                        });
                        Intent m = new Intent(ListActivity.this, PartSectionActivity.class);
                        m.putExtra("name", "religion");
                        startActivity(m);
                        break;
                    case 7:
                        searchDialog("village", keyword->{
                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
                            i.putExtra("keyword", keyword);
                            i.putExtra("searchOn", "polling_station");
                            startActivity(i);
                        });
                        break;
                    case 8:
//                        arrayListChoose.clear();
//                        arrayListChoose.add("M");
//                        arrayListChoose.add("F");
//                        arrayListChoose.add("O");
//
//                        searchDialog("Sex", arrayListChoose, keyword->{
//                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
//                            i.putExtra("keyword", keyword);
//                            i.putExtra("searchOn", "sex");
//                            startActivity(i);
//                        });
                        Intent s = new Intent(ListActivity.this, PartSectionActivity.class);
                        s.putExtra("name", "sex");
                        startActivity(s);
                        break;
                    case 9:
                        searchDialog("name", keyword->{
                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
                            i.putExtra("keyword", keyword);
                            i.putExtra("searchOn", "name");
                            startActivity(i);
                        });
                        break;
                    case 10:
                        Intent j = new Intent(ListActivity.this, PartSectionActivity.class);
                        j.putExtra("name", "section");
                        startActivity(j);
//                        searchDialog("section", keyword->{
//                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
//                            i.putExtra("keyword", keyword);
//                            i.putExtra("searchOn", "section");
//                            startActivity(i);
//                        });
                        break;
                    case 11:
                        searchDialog("voter_id", keyword->{
                            Intent i = new Intent(ListActivity.this, SearchActivity.class);
                            i.putExtra("keyword", keyword);
                            i.putExtra("searchOn", "voter_id");
                            startActivity(i);
                        });
                        break;
                    case 12:
                        Intent i = new Intent(ListActivity.this, SearchActivity.class);
                        i.putExtra("keyword", "");
                        i.putExtra("searchOn", "mobile");
                        startActivity(i);
                        break;
                    case 13:

                        break;
                    case 14:

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

        dialogBinding.inputHint.setHint(searchOn);
        dialogBinding.btn.setText("Search");

        dialogBinding.btn.setOnClickListener(v -> {
            String text = dialogBinding.textInputL.getText().toString();
            if (text.isEmpty()) {
                dialogBinding.textInputL.setError("*");
            } else {
                dialog.dismiss();
                callback.onResult(text);
            }
        });

        dialog.show();
    }

    private void searchDialog(String searchOn,ArrayList<String> arrayListChoose, DialogCallback callback) {
        DialogListSearchBinding dialogBinding = DialogListSearchBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogBinding.getRoot());
        // Create and show the dialog
        AlertDialog dialog = builder.create();

        dialogBinding.searchOn.setText(searchOn);

        myListAdapter adapter = new myListAdapter(ListActivity.this, arrayListChoose);
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