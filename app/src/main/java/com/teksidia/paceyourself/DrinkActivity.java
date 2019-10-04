package com.teksidia.paceyourself;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DrinkActivity extends AppCompatActivity {

    public static final String TARGET_AMOUNT = "com.teksidia.paceyourself.TARGET_AMOUNT";
    public static final String TARGET_HOUR = "com.teksidia.paceyourself.TARGET_HOUR";
    public static final String TARGET_MINUTE = "com.teksidia.paceyourself.TARGET_MINUTE";

    private TextView mSettings;
    private TextView mCountdown;

    private int mDrinksConsumed = 0;
    private int mTargetAmount;
    private Calendar mDateTimeTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nextDrink();

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSettings = findViewById(R.id.text_settings);
        mCountdown = findViewById(R.id.text_countdown);
        Intent intent = getIntent();
        mTargetAmount = intent.getIntExtra(TARGET_AMOUNT, 0);
        int targetHour = intent.getIntExtra(TARGET_HOUR, 0);
        int targetMinute = intent.getIntExtra(TARGET_HOUR, 0);

        Calendar dateTimeNow = Calendar.getInstance();
        mDateTimeTarget = Calendar.getInstance();
        if(targetHour < dateTimeNow.get(Calendar.HOUR_OF_DAY)) {
            mDateTimeTarget.add(Calendar.DAY_OF_YEAR, 1);
        }
        mDateTimeTarget.set(Calendar.HOUR_OF_DAY, targetHour);
        mDateTimeTarget.set(Calendar.MINUTE, targetMinute);

        nextDrink();

    }

    private void nextDrink() {

        CountDownTimer timer;

        mDrinksConsumed += 1;

        mSettings.setText(Integer.toString(mDrinksConsumed));

        final int drinksRemaining = (mTargetAmount - mDrinksConsumed);

        if(drinksRemaining == 0) {
            mCountdown.setText("Go home");
            return;
        }

        if(drinksRemaining == 1) {
            // last drink!
        }

        Calendar dateTimeNow = Calendar.getInstance();
        long now = dateTimeNow.getTimeInMillis();
        long target = mDateTimeTarget.getTimeInMillis();
        long sessionTimeInMillisecs = (target - now);
        long timePerDrink = sessionTimeInMillisecs / drinksRemaining;

        timer = new CountDownTimer(timePerDrink, 1000) {

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();

            public void onTick(long millisUntilFinished) {
                date.setTime(millisUntilFinished);
                mCountdown.setText(sdf.format(date));
             }

            public void onFinish() {
                mCountdown.setText("You are good to go!");
            }
        };

        timer.start();

    }


}
