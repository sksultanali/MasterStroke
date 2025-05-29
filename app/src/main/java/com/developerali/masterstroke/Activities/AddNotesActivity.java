package com.developerali.masterstroke.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developerali.masterstroke.Helpers.DB_Helper;
import com.developerali.masterstroke.MainActivity;
import com.developerali.masterstroke.Models.NotesModel;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ActivityAddNotesBinding;

import java.util.Calendar;
import java.util.Locale;

public class AddNotesActivity extends AppCompatActivity {

    ActivityAddNotesBinding binding;
    DB_Helper dbHelper;
    int year, month, day, hour, minute;
    String dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Add New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        dbHelper = new DB_Helper(AddNotesActivity.this);


        if (getIntent().hasExtra("title")){
            String title = getIntent().getStringExtra("title");
            String des = getIntent().getStringExtra("des");

            binding.title.setText(title);
            binding.description.setText(des);
            getSupportActionBar().setTitle("Previously Added Note");
        }


        binding.saveBtn.setOnClickListener(v->{
            String title = binding.title.getText().toString();
            String des = binding.description.getText().toString();
            String date = binding.dateTime.getText().toString();

            if (title.isEmpty()){
                binding.title.setError("*");
            } else if (des.isEmpty()) {
                binding.description.setError("*");
            }else {
                NotesModel recentSearchModel = new NotesModel();
                recentSearchModel.setTitle(title);
                recentSearchModel.setDescription(des);
                recentSearchModel.setDate(date);

                dbHelper.addSearchQuery(recentSearchModel);

                Toast.makeText(this, "Note Added...!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddNotesActivity.this, NotesActivity.class));
                finish();
            }

        });

        binding.dateTime.setOnClickListener(v->showDateTimePicker());





    }

    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddNotesActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    year = selectedYear;
                    month = selectedMonth + 1;
                    day = selectedDay;

                    // Now show Time Picker
                    TimePickerDialog timePickerDialog = new TimePickerDialog(AddNotesActivity.this,
                            (view1, selectedHour, selectedMinute) -> {
                                hour = selectedHour;
                                minute = selectedMinute;

                                String result = String.format(Locale.getDefault(),
                                        "%02d/%02d/%04d %02d:%02d", day, month, year, hour, minute);
                                binding.dateTime.setText(result);

                            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true);

                    timePickerDialog.show();

                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}