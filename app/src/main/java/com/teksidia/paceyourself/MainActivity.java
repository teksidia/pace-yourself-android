package com.teksidia.paceyourself;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private int mTargetDrinks = 4;
    private int mTargetHour = 0;
    private int mTargetMinute = 0;
    private Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView finishTime = findViewById(R.id.txt_finish_time);

        Calendar currentTime = Calendar.getInstance();
        mTargetHour = currentTime.get(Calendar.HOUR_OF_DAY) + 4;
        mTargetMinute = currentTime.get(Calendar.MINUTE);

        finishTime.setText(String.format("%02d", mTargetHour) + ":" + String.format("%02d", mTargetMinute));

        NumberPicker np = findViewById(R.id.np);
        np.setMinValue(2);
        np.setMaxValue(10);
        np.setValue(mTargetDrinks);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                mTargetDrinks = newVal;
            }
        });

        mStartButton = findViewById(R.id.button_start);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DrinkActivity.class);
                intent.putExtra(DrinkActivity.TARGET_AMOUNT, mTargetDrinks);
                intent.putExtra(DrinkActivity.TARGET_HOUR, mTargetHour);
                intent.putExtra(DrinkActivity.TARGET_MINUTE, mTargetMinute);
                startActivity(intent);
            }
        });

        finishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mTargetHour = selectedHour;
                        mTargetMinute = selectedMinute;
                        finishTime.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute));
                    }
                }, mTargetHour, mTargetMinute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }
}
