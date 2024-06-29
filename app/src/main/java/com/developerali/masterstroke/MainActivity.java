package com.developerali.masterstroke;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developerali.masterstroke.Activities.ListActivity;
import com.developerali.masterstroke.Activities.OtherActivity;
import com.developerali.masterstroke.Activities.SurveyActivity;
import com.developerali.masterstroke.Models.ToolsModel;
import com.developerali.masterstroke.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<ToolsModel> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        arrayList.clear();
        arrayList.add(new ToolsModel("Search", getDrawable(R.drawable.search)));
        arrayList.add(new ToolsModel("List", getDrawable(R.drawable.list)));
        arrayList.add(new ToolsModel("Survey", getDrawable(R.drawable.survey)));
        arrayList.add(new ToolsModel("Charts", getDrawable(R.drawable.charts)));
        arrayList.add(new ToolsModel("Others", getDrawable(R.drawable.more)));

        myListAdapter adapter = new myListAdapter();
        binding.toolsList.setAdapter(adapter);

        binding.toolsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, ListActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, SurveyActivity.class));
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "Charts", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, OtherActivity.class));
                        break;
                }
            }
        });




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
        if (itemId == R.id.notification) {
            Toast.makeText(this, "Notification...!", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.logout) {
            Toast.makeText(this, "Log Out...!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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