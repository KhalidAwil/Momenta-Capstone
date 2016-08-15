package com.momenta;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class AddNewTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText activityName;
    TextView activityGoal;
    TextView activityDeadline;
    TextView activityTimeSpent;
    Spinner spinner;

    //Calendar instance used to store the deadline date
    private final Calendar deadlineCalendar = Calendar.getInstance();

    //Global variables to hold goal and timespent hours, minutes and priority
    private  Integer goalHours = 2;
    private  Integer goalMins = 30;
    private  Integer timespentHours = 0;
    private  Integer timespentMins = 0;
    private  Task.Priority PRIORITY = Task.Priority.VERY_LOW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtask);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.new_task_title);
        }

        //Initialize UI objects
        activityName = (EditText)findViewById(R.id.newtask_name_edit_text);
        activityGoal = (TextView)findViewById(R.id.newtask_goal_value);
        activityDeadline = (TextView) findViewById(R.id.newtask_deadline_value);
        activityTimeSpent = (TextView) findViewById(R.id.newtask_timespent_value);

        deadlineCalendar.setTime(new Date());
        activityDeadline.setText(Task.getDateFormat(deadlineCalendar));

        spinner = (Spinner)findViewById(R.id.newtask_priority_spinner);

        assert spinner != null;
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(spinnerPosition(Task.Priority.MEDIUM));

        activityGoal.setText(timeSetText(goalHours,goalMins));
        activityTimeSpent.setText(timeSetText(timespentHours,timespentMins));
    }

    /**
     * Convenience method for setting the time related values for the goal and time spent fields
     * @param hours hour value that is being set
     * @param minutes minute value that is being set
     * @return the string containing the minute and hour values that will be set in the TextView
     */
    private String timeSetText(int hours, int minutes) {
        if (minutes > 1 && hours > 1) {
            return hours + " " + getResources().getString(R.string.timeentry_dialog_hours_label) + " & " +
                    minutes + " " +  getResources().getString(R.string.timeentry_dialog_minutes_label) ;
        } else if (minutes == 0 && hours > 1) {
            return hours + " " + getResources().getString(R.string.timeentry_dialog_hours_label);
        }
        else if (minutes == 1 && hours > 1) {
            return hours + " " + getResources().getString(R.string.timeentry_dialog_hours_label) + " & " +
                    minutes + " " + getResources().getString(R.string.timeentry_dialog_minute_label);
        }
        else if (hours == 1 && minutes > 1) {
            return hours + " " + getResources().getString(R.string.timeentry_dialog_hour_label) + " & " +
                    minutes + " " + getResources().getString(R.string.timeentry_dialog_minutes_label);
        }
        else if (hours == 1 && minutes == 1) {
            return hours + " " + getResources().getString(R.string.timeentry_dialog_hour_label) + " & " +
                    minutes + " " + getResources().getString(R.string.timeentry_dialog_minute_label);
        }
        else if (hours == 1 && minutes == 0) {
            return hours + " " + getResources().getString(R.string.timeentry_dialog_hour_label);
        }
        else if (minutes == 1 && hours == 0) {
            return minutes + " " + getResources().getString(R.string.timeentry_dialog_minute_label);
        }
        else {
            return minutes + " " + getResources().getString(R.string.timeentry_dialog_minutes_label);
        }
    }
    /**
     * Event handler for when the Goal TextView is clicked
     * @param view view being clicked
     */
    public void goalOnClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        LayoutInflater inflater = this.getLayoutInflater();

        final View alertView = inflater.inflate(R.layout.dialog_timeentry, null);
        final EditText editTextHours  = (EditText) alertView.findViewById(R.id.dialog_hour_textview);
        editTextHours.setText(Integer.toString(goalHours));

        final EditText editTextMinutes = (EditText) alertView.findViewById(R.id.dialog_minute_textview);
        editTextMinutes.setText(Integer.toString(goalMins));

        final TextView dialogTitle = (TextView) alertView.findViewById(R.id.dialog_title);
        dialogTitle.setText(R.string.timeentry_dialog_goal_title);
        builder.setView(alertView);


        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goalHours = Integer.valueOf(editTextHours.getText().toString());
                        goalMins = Integer.valueOf(editTextMinutes.getText().toString());
                        activityGoal.setText(timeSetText(goalHours, goalMins));
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    /**
     * Event handler for when the Time Spent TextView is clicked
     * @param view view being clicked
     */
    public void timespentOnClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        LayoutInflater inflater = this.getLayoutInflater();

        final View alertView = inflater.inflate(R.layout.dialog_timeentry, null);
        final EditText editTextHours  = (EditText) alertView.findViewById(R.id.dialog_hour_textview);
        editTextHours.setText(Integer.toString(timespentHours));

        final EditText editTextMinutes = (EditText) alertView.findViewById(R.id.dialog_minute_textview);
        editTextMinutes.setText(Integer.toString(timespentMins));

        final TextView dialogTitle = (TextView) alertView.findViewById(R.id.dialog_title);
        dialogTitle.setText(R.string.timeentry_dialog_timespent_title);

        builder.setView(alertView);


        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timespentHours = Integer.valueOf(editTextHours.getText().toString());
                timespentMins = Integer.valueOf(editTextMinutes.getText().toString());
                activityTimeSpent.setText(timeSetText(timespentHours, timespentMins) );
            }
        })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.show();
    }
    /**
     * Event handler for when clicking the Deadline TextView
     * @param view view being clicked
     */
    public void deadlineOnClick(View view){
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                deadlineCalendar.set(year, monthOfYear, dayOfMonth);
                activityDeadline.setText(Task.getDateFormat(deadlineCalendar));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        dialog.show();
        }

    /**
     * Event handler for when the done button is clicked
     * @param view view being clicked
     */
    public void doneOnClick(View view){
        save();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(position) {
            case 0:
                PRIORITY = Task.Priority.VERY_LOW;
                break;
            case 1:
                PRIORITY = Task.Priority.LOW;
                break;
            case 2:
                PRIORITY = Task.Priority.MEDIUM;
                break;
            case 3:
                PRIORITY = Task.Priority.HIGH;
                break;
            case 4:
                PRIORITY = Task.Priority.VERY_HIGH;
                break;
        }
    }

    private int spinnerPosition(Task.Priority priority) {
        switch (priority) {
            case VERY_LOW:
                return 0;
            case LOW:
                return 1;
            case MEDIUM:
                return 2;
            case HIGH:
                return 3;
            case VERY_HIGH:
                return 4;
            default:
                return 2;
        }
    }

    /**
     * Method for saving a task into the database upon entering the fields.
     */
    private void save(){
        String name = activityName.getText().toString();
        Long totalGoalMinutes = TimeUnit.MINUTES.convert(goalHours.longValue(), TimeUnit.HOURS) + goalMins.longValue();
        Long totalTimeSpentMinutes = TimeUnit.MINUTES.convert(timespentHours.longValue(), TimeUnit.HOURS) + timespentMins.longValue();

        if (name.isEmpty()) {
            activityName.setError(getResources().getString(R.string.toast_no_name_activity_added));
            return;
        }

        Task task = new Task(name, totalGoalMinutes.intValue(),
                deadlineCalendar, Calendar.getInstance().getTimeInMillis(), Calendar.getInstance());
        task.addTimeInMinutes(totalTimeSpentMinutes.intValue());
        task.setPriority(PRIORITY);
        DBHelper.getInstance(this).insertTask(task);
        finish();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

