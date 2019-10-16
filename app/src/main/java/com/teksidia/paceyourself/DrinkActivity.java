package com.teksidia.paceyourself;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;


public class DrinkActivity extends AppCompatActivity {

    public static final String TARGET_AMOUNT = "com.teksidia.paceyourself.TARGET_AMOUNT";
    public static final String TARGET_HOUR = "com.teksidia.paceyourself.TARGET_HOUR";
    public static final String TARGET_MINUTE = "com.teksidia.paceyourself.TARGET_MINUTE";

    private TextView mCountdown;
    private TextView mFinishTimeLabel;
    private CountDownTimer mCountdownTimer;
    private LinearLayout mBeerContainer;
    private Button mNextButton;

    private int mTargetAmount;
    private long mSessionTimeInMillisecs;
    private int mDrinksConsumed = 0;
    private int mTargetHour;
    private int mTargetMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCountdown = findViewById(R.id.text_countdown);
        mBeerContainer = findViewById(R.id.linear_beers);
        mNextButton = findViewById(R.id.button_next_drink);
        mFinishTimeLabel = findViewById(R.id.text_finish_time);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextDrink(false);
            }
        });

        Intent intent = getIntent();
        mTargetAmount = intent.getIntExtra(TARGET_AMOUNT, 0);
        mTargetHour = intent.getIntExtra(TARGET_HOUR, 0);
        mTargetMinute = intent.getIntExtra(TARGET_MINUTE, 0);

        mFinishTimeLabel.setText(String.format("Finish time: %s:%s", String.format("%02d", mTargetHour), String.format("%02d", mTargetMinute)));

        nextDrink(true);

    }

    private long setSessionTime() {
        Calendar dateTimeNow = Calendar.getInstance();
        Calendar dateTimeTarget = Calendar.getInstance();
        if(mTargetHour < dateTimeNow.get(Calendar.HOUR_OF_DAY)) {
            dateTimeTarget.add(Calendar.DAY_OF_YEAR, 1);
        }
        dateTimeTarget.set(Calendar.HOUR_OF_DAY, mTargetHour);
        dateTimeTarget.set(Calendar.MINUTE, mTargetMinute);
        long now = dateTimeNow.getTimeInMillis();
        long target = dateTimeTarget.getTimeInMillis();
        long timeLeftInMillisecs = (target - now);
        Log.d("timing", "Minutes left:" + timeLeftInMillisecs / (1000 * 60));
        return timeLeftInMillisecs;
    }

    private void nextDrink(boolean isStart) {

        if(!isStart) {
            mDrinksConsumed += 1;
        }

        final int drinksRemaining = (mTargetAmount - mDrinksConsumed);
        mSessionTimeInMillisecs = setSessionTime();

        adjustBeerIndicator(drinksRemaining);

        if(drinksRemaining == 0) {
            Intent intent = new Intent(DrinkActivity.this, CompleteActivity.class);
            intent.putExtra(CompleteActivity.SESSION_EXPIRED, mSessionTimeInMillisecs <= 0);
            startActivity(intent);
            finish();
            return;
        }

        if(drinksRemaining == 1) {
            mNextButton.setText("Complete session");
        }

        long millisecsPerDrink = 0;
        if(mSessionTimeInMillisecs <= 0) {
            mCountdown.setText("Time up");
        } else {
            millisecsPerDrink = mSessionTimeInMillisecs / drinksRemaining;
        }

        if(mCountdownTimer != null) {
            mCountdownTimer.cancel();
        }

        mCountdownTimer = new CountDownTimer(millisecsPerDrink, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
                int hours   = (int) ((millisUntilFinished / (1000*60*60)) % 24);
                mCountdown.setText(String.format("%s:%s:%s",
                        String.format("%02d", hours), String.format("%02d", minutes), String.format("%02d", seconds)));
            }

            public void onFinish() {
                final int drinksRemaining = (mTargetAmount - mDrinksConsumed);

                if(drinksRemaining == 1) {
                    mCountdown.setText("Time up");
                    mNextButton.setText("Complete session");
                } else {
                    mCountdown.setText("Get next drink!");
                }

            }
        };

        mCountdownTimer.start();

    }

    private void adjustBeerIndicator(int drinksRemaining) {
        mBeerContainer.removeAllViews();
        int currentDrink = (mTargetAmount - drinksRemaining);
        LinearLayout beerRow = new LinearLayout(this);
        for(int i = 0; i < mTargetAmount; i++) {
            if (i % 5 == 0) {
                beerRow = new LinearLayout(this);
                beerRow.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mBeerContainer.addView(beerRow, layoutParams);
            }
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.beer_glass), (int) getResources().getDimension(R.dimen.beer_glass));
            ImageView ii = new ImageView(this);
            if(i > currentDrink) {
                ii.setBackgroundResource(R.drawable.beerfull);
            } else if(i == currentDrink) {
                ii.setBackgroundResource(R.drawable.beerhalf);
            } else {
                ii.setBackgroundResource(R.drawable.beerempty);
            }
            ii.setLayoutParams(layoutParams);
            beerRow.addView(ii);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drink, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.reset) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}