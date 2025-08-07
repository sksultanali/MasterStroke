package com.developerali.masterstroke.Activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developerali.masterstroke.Helpers.AlarmReceiver;
import com.developerali.masterstroke.Helpers.DB_Helper;
import com.developerali.masterstroke.Models.NotesModel;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ActivityAddNotesBinding;

import java.util.Calendar;
import java.util.Locale;

public class AddNotesActivity extends AppCompatActivity {

    private ActivityAddNotesBinding binding;
    private DB_Helper dbHelper;
    private int year, month, day, hour, minute;
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private static final String CHANNEL_ID = "meeting_reminder_channel";
    private long noteId = -1; // For editing existing notes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up toolbar
//        setSupportActionBar(binding.toolbar); // Assuming you have a Toolbar in your layout
//        getSupportActionBar().setTitle("Add New Note");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DB_Helper(this);
        createNotificationChannel();

        // Check for edit mode
        if (getIntent().hasExtra("id")) {
            noteId = getIntent().getLongExtra("id", -1);
            String title = getIntent().getStringExtra("title");
            String des = getIntent().getStringExtra("description");
            String date = getIntent().getStringExtra("date");
            binding.title.setText(title);
            binding.description.setText(des);
            binding.dateTime.setText(date);
            getSupportActionBar().setTitle("Edit Note");
        }

        binding.saveBtn.setOnClickListener(v -> saveNote());
        binding.dateTime.setOnClickListener(v -> showDateTimePicker());

        // Request permissions for alarms and notifications
        requestPermissions();
    }

    private void saveNote() {
        String title = binding.title.getText().toString().trim();
        String description = binding.description.getText().toString().trim();
        String date = binding.dateTime.getText().toString().trim();

        if (title.isEmpty()) {
            binding.title.setError("Title is required");
            return;
        }
        if (description.isEmpty()) {
            binding.description.setError("Description is required");
            return;
        }
        if (date.isEmpty()) {
            Toast.makeText(this, "Please select a date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        NotesModel note = new NotesModel();
        note.setTitle(title);
        note.setDescription(description);
        note.setDate(date);

        boolean success;
//        if (noteId != -1) {
//            note.setId(noteId);
//            success = dbHelper.updateNote(note); // Assuming DB_Helper has an updateNote method
//        } else {
//            success = dbHelper.addSearchQuery(note);
//        }
//
//        if (success) {
//            Toast.makeText(this, noteId != -1 ? "Note Updated!" : "Note Added!", Toast.LENGTH_SHORT).show();
//            setAlarm(title, description); // Schedule alarm 30 minutes before
//            startActivity(new Intent(this, NotesActivity.class)
//                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
//            finish();
//        } else {
//            Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show();
//        }
    }

    private void showDateTimePicker() {
        Calendar currentDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    year = selectedYear;
                    month = selectedMonth;
                    day = selectedDay;

                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            (view1, selectedHour, selectedMinute) -> {
                                hour = selectedHour;
                                minute = selectedMinute;

                                // Format date and time in 12-hour format with AM/PM
                                Calendar selectedDateTime = Calendar.getInstance();
                                selectedDateTime.set(year, month, day, hour, minute);
                                String result = String.format(Locale.getDefault(),
                                        "%02d/%02d/%04d %02d:%02d %s",
                                        day, month + 1, year,
                                        hour % 12 == 0 ? 12 : hour % 12, minute,
                                        hour >= 12 ? "PM" : "AM");
                                binding.dateTime.setText(result);

                            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false); // 12-hour format

                    timePickerDialog.show();
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis()); // Prevent past dates
        datePickerDialog.show();
    }

    private void setAlarm(String title, String description) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            Toast.makeText(this, "Alarm service unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate alarm time (30 minutes before the selected time)
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(year, month, day, hour, minute);
        alarmTime.add(Calendar.MINUTE, -30);

        // Create intent for the BroadcastReceiver
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        int requestCode = (int) (noteId != -1 ? noteId : System.currentTimeMillis() % Integer.MAX_VALUE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Set exact alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(this, "Exact alarm permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime.getTimeInMillis(),
                pendingIntent
        );
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Meeting Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for meeting reminders");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_CODE_PERMISSIONS);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!getSystemService(AlarmManager.class).canScheduleExactAlarms()) {
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}