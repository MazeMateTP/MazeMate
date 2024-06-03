package com.example.tp_game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Room extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Button createButton = findViewById(R.id.btncreateroom);
        Button joinButton = findViewById(R.id.btnjoinroom);
        Button rankingButton = findViewById(R.id.btnranking);



        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Room.this,CreateActivity.class);
                startActivity(intent);

            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Room.this,JoinActivity.class);
                startActivity(intent);

            }
        });
       rankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Room.this,RankingActivity.class);
                startActivity(intent);

            }
        });
    }
}
