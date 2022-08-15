package com.sam.termtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.sam.termtracker.R;

public class MainActivity extends AppCompatActivity {

    MaterialButton enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterButton = findViewById(R.id.enterButton);

    }

    public void enterApp(View view) {
        Intent intent = new Intent(this, TermViewActivity.class);
        startActivity(intent);
    }
}