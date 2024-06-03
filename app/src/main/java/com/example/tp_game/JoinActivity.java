package com.example.tp_game;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JoinActivity extends AppCompatActivity {

    private TextView roomCountTextView;
    private LinearLayout roomsContainer;
    private FirebaseDatabase database;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        roomCountTextView = findViewById(R.id.roomCountTextView);
        roomsContainer = findViewById(R.id.roomsContainer); // Use the correct ID from join.xml
        database = FirebaseDatabase.getInstance();
        userId = getIntent().getStringExtra("userId");
        // Read the number of rooms from Realtime Database
        database.getReference("rooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int roomCount = 0;
                List<String> roomNumberLi = new ArrayList<String>();
                if (dataSnapshot.exists()) {
                    roomCount = (int) dataSnapshot.getChildrenCount();

                    // Iterate through child nodes and extract room numbers (child keys)
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        roomNumberLi.add((String) childSnapshot.getKey().toString());
                        // Do something with the room number (e.g., add to a list, display it)
                    }
                }
                System.out.println(roomNumberLi);
                // Update room count TextView
                roomCountTextView.setText("방 개수: " + roomCount);

                // Create and add room buttons
                roomsContainer.removeAllViews(); // Clear existing buttons
                for (int i = 0; i < roomCount; i++) {






                    String roomName = "방 " + roomNumberLi.get(i); // Replace with actual room name retrieval
                    String roomNumber = roomNumberLi.get(i);
                    Button roomButton = new Button(JoinActivity.this);
                    roomButton.setText(roomName);
                    roomButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(JoinActivity.this, Multimode1Activity.class);
                            intent.putExtra("roomNumber", String.valueOf(roomNumber)); // Pass room number

                            startActivity(intent);

                            Toast.makeText(JoinActivity.this,   roomName + "에 참여합니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    roomsContainer.addView(roomButton);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(JoinActivity.this, "방 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}