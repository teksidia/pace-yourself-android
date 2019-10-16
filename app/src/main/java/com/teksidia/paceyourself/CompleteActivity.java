package com.teksidia.paceyourself;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class CompleteActivity extends AppCompatActivity {

    public static final String SESSION_EXPIRED = "com.teksidia.paceyourself.SESSION_EXPIRED";
    private Button mCompleteButton;
    private boolean mSessionExpired;
    private TextView mCompleteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mSessionExpired = intent.getBooleanExtra(SESSION_EXPIRED, false);

        mCompleteText = findViewById(R.id.text_complete);
        mCompleteButton = findViewById(R.id.button_again);
        mCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompleteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if(mSessionExpired) {
            mCompleteText.setText("Session expired, time to go home!");
        }

    }

}
