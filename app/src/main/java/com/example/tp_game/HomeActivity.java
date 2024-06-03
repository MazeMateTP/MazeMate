package com.example.tp_game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tp_game.single.SingleActivity;

public class HomeActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button singleButton = findViewById(R.id.btnSingle);
        Button multiButton = findViewById(R.id.btnMulti);
        Button settingButton = findViewById(R.id.btnSetting);

        singleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,SingleActivity.class);
                startActivity(intent);
            }
        });
        multiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,Room.class);
                startActivity(intent);

            }
        });
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,SettingActivity.class);
                startActivity(intent);

            }
        });
    }
}
