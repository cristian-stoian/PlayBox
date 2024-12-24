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
        setContentView(R.layout.activity_main); // Asigură-te că folosești layout-ul corect

        // Găsește butonul din layout
        Button goToMatch3Button = findViewById(R.id.GoTo);

        // Setează un listener pentru apăsarea butonului
        goToMatch3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creează un Intent pentru a porni Match3
                Intent intent = new Intent(MainActivity.this, Match3.class);
                startActivity(intent);
            }
        });
    }
}
