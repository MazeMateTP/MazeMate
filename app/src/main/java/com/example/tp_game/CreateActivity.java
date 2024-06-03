package com.example.tp_game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CreateActivity extends AppCompatActivity {

    private EditText roomNameEditText;
    private Spinner roomModeSpinner;
    private Button createRoomButton;
    private FirebaseDatabase database;
    private String[] roomModes; // Array to store room mode options


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        roomNameEditText = findViewById(R.id.roomNameEditText);
        roomModeSpinner = findViewById(R.id.roomModeSpinner);
        createRoomButton = findViewById(R.id.createRoomButton);
        database = FirebaseDatabase.getInstance();

        // Define room mode options (replace with your desired modes)
        roomModes = new String[]{"모드 1", "모드 2", "모드 3"};

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomModes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomModeSpinner.setAdapter(adapter);

        createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomName = roomNameEditText.getText().toString();
                String selectedMode = roomModeSpinner.getSelectedItem().toString();

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();
                String userId = currentUser.getUid();

                // Generate a random room number
                int roomNumber = new Random().nextInt(1000) + 1;


                // Create a room data map
                HashMap<String, Object> roomData = new HashMap<>();
                roomData.put("roomNumber", roomNumber);
                roomData.put("roomName", roomName);
                roomData.put("roomMode", selectedMode);
                roomData.put("host",userId);
                roomData.put("guest","");
                roomData.put("hostStartPoint","1 0");
                roomData.put("guestStartPoint","0 0");
                roomData.put("maze","");
                roomData.put("endPoint","");
                roomData.put("hostPosition","");
                roomData.put("guestPosition","1 0");
                roomData.put("winner","");

                // Save room data to Realtime Database
                DatabaseReference roomsRef = database.getReference("rooms");
                roomsRef.child(String.valueOf(roomNumber)).setValue(roomData)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Start the appropriate activity based on mode (replace with your logic)
                                if(selectedMode=="모드 1"){
                                    Intent intent = new Intent(CreateActivity.this, Multimode1Activity.class); // Assuming GameMode1Activity for mode 1
                                    intent.putExtra("roomNumber", String.valueOf(roomNumber)); // Pass room number
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(CreateActivity.this, "방 생성에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}

