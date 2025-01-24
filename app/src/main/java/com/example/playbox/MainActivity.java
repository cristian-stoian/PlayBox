package com.example.playbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Match-3 Button
        Button match3Button = findViewById(R.id.btnMatch3);
        match3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Match3.class);
                startActivity(intent);
            }
        });

        // 2048 Button
        Button puzzleButton = findViewById(R.id._2048);
        puzzleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, _2048.class);
                startActivity(intent);
            }
        });

        // Shooter Button
        Button shooterButton = findViewById(R.id.clickMania);
        shooterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClickManiaActivity.class);
                startActivity(intent);
            }
        });

        // Tower Defense Button
        Button towerDefenseButton = findViewById(R.id.btnTowerDefense);
        towerDefenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, _2048.class);
                startActivity(intent);
            }
        });
    }
}
