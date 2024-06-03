package com.example.tp_game;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class Multimode1Activity extends AppCompatActivity {

    public static String roomNumber;
    private Mode1MazePanel mode1MazePanel;
    private Mode1GuestMazePanel mode1GuestMazePanel;
    private static String userId;
    private int level=11; // 레벨 변수 추가
    public static boolean Isautoclicked = true;
    private boolean isHost = false;
    private boolean userLeftGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        userId = currentUser.getUid();
        roomNumber = getIntent().getStringExtra("roomNumber");
        DataFetchTask dataFetchTask = new DataFetchTask();
        GameData gameData = GameData.getInstance();
        dataFetchTask.fetchDataAndExecuteTask(new DataFetchCallback() {
            @Override
            public void onDataFetched(String data) {


                if (data.equals(userId)) {
                    isHost = true;
                }

                if(isHost) {
                    setContentView(R.layout.activity_gamemode1);
                    // MazePanel 초기화

                    initializeMazePanel();
                    gameData.setGameStatusListener(new GameData.GameStatusListener() {
                        @Override
                        public void onGameFinished() {
                            long duration = gameData.getGameDuration();
                            System.out.println("The game has finished. Duration: " + duration + " milliseconds");
                            DatabaseReference timereference = FirebaseDatabase.getInstance().getReference("ranking").child("mode1").child(userId);
                            timereference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // 데이터 읽기 성공
                                    Long origin = dataSnapshot.getValue(Long.class);
                                    if (origin == null || origin < duration) {
                                        // origin이 null이거나 duration보다 작을 경우 업데이트
                                        timereference.setValue(duration);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // 데이터 읽기 실패
                                    System.out.println("Failed to read value.");
                                }
                            });

                            makeDialog();
                        }
                        public void onGameStarted() {
                            // 게임 시작 시 수행할 작업
                            System.out.println("The game has started.");
                        }
                    });



                    int[][] mazeArray = mode1MazePanel.getmaze();
                    List<List<Integer>> nestedList = new ArrayList<>();
                    for (int[] row : mazeArray) {
                        List<Integer> innerList = new ArrayList<>();
                        for (int element : row) {
                            innerList.add(element);
                        }
                        nestedList.add(innerList);
                    }


                    DatabaseReference mapreference = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("maze");
                    mapreference.setValue(nestedList);

                    Button upButton = findViewById(R.id.upButton);
                    Button rightButton = findViewById(R.id.rightButton);
                    Button downButton = findViewById(R.id.downButton);
                    Button leftButton = findViewById(R.id.leftButton);

                    //Find Guest direction button

                    // Direction buttons의 OnClickListener 설정
                    upButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mode1MazePanel.movePlayerUp(); // 위쪽으로 이동
                        }
                    });

                    rightButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mode1MazePanel.movePlayerRight(); // 오른쪽으로 이동
                        }
                    });

                    downButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mode1MazePanel.movePlayerDown(); // 아래쪽으로 이동
                        }
                    });

                    leftButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mode1MazePanel.movePlayerLeft(); // 왼쪽으로 이동
                        }
                    });




                }else{
                    setContentView(R.layout.activity_gamemode1guest);
                    DatabaseReference guestreference = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("guest");
                    guestreference.setValue(userId);

                    DatabaseReference mazeRef = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("maze");
                    mazeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Parse the maze data from Firebase (nested list)
                                GenericTypeIndicator<List<List<Integer>>> typeIndicator = new GenericTypeIndicator<List<List<Integer>>>() {};
                                List<List<Integer>> nestedList = dataSnapshot.getValue(typeIndicator);

                                // Convert nested list to int[][] array (optional)
                                int[][] mazeArray = new int[nestedList.size()][]; // Initialize 2D array with row size
                                for (int i = 0; i < nestedList.size(); i++) {
                                    mazeArray[i] = new int[nestedList.get(i).size()]; // Initialize each row with column size
                                    for (int j = 0; j < nestedList.get(i).size(); j++) {
                                        mazeArray[i][j] = nestedList.get(i).get(j);
                                        System.out.print(mazeArray[i][j]);
                                        System.out.print(" ");// Assign each element
                                    }
                                    System.out.println();
                                }

                                initializeguestMazePanel(mazeArray);

                                GameData.getInstance().setGameStatusListener(new GameData.GameStatusListener() {
                                    @Override
                                    public void onGameFinished() {
                                        makeDialog();
                                    }
                                    public void onGameStarted() {
                                        System.out.println("The game has started.");
                                    }
                                });


                                /*if (GameData.getInstance().isGameFinished()) {
                                    performAction();
                                }*/

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle database errors
                        }
                    });
                    //database get value of map
                    Button upButtonguest = findViewById(R.id.upButtonguest);
                    Button rightButtonguest = findViewById(R.id.rightButtonguest);
                    Button downButtonguest = findViewById(R.id.downButtonguest);
                    Button leftButtonguest = findViewById(R.id.leftButtonguest);



                    upButtonguest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mode1GuestMazePanel.movePlayerUp(); // 위쪽으로 이동
                        }
                    });

                    rightButtonguest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mode1GuestMazePanel.movePlayerRight(); // 오른쪽으로 이동
                        }
                    });

                    downButtonguest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mode1GuestMazePanel.movePlayerDown(); // 아래쪽으로 이동
                        }
                    });

                    leftButtonguest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mode1GuestMazePanel.movePlayerLeft(); // 왼쪽으로 이동
                        }
                    });
                }
            }
        });


        System.out.println(isHost);

    }
    public static class DataFetchTask {

        private FirebaseDatabase database;
        private DatabaseReference reference;

        public DataFetchTask() {
            database = FirebaseDatabase.getInstance();
            reference = database.getReference("rooms").child(roomNumber).child("host");
            System.out.println(reference);
        }

            public void fetchDataAndExecuteTask(DataFetchCallback callback) {
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String data = dataSnapshot.getValue().toString();
                        System.out.println(data);
                        callback.onDataFetched(data);
                        reference.removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            };

            reference.addValueEventListener(listener);
        }
    }

    // 미로 패널 초기화 메서드
    private void initializeMazePanel() {
        FrameLayout mazePanelLayout = findViewById(R.id.mazePanel);
        mode1MazePanel = new Mode1MazePanel(this);
        mazePanelLayout.addView(mode1MazePanel);
    }
    private void initializeguestMazePanel(int[][] mazearray) {
        FrameLayout mazePanelLayout = findViewById(R.id.mazePanel);
        mode1GuestMazePanel = new Mode1GuestMazePanel(this ,mazearray);
        System.out.println(mode1GuestMazePanel.getmaze());
        mazePanelLayout.addView(mode1GuestMazePanel);
    }

    // ... (Implement your game logic methods here)
    private void deleteRoomData(String roomNumber) {
        // Access Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference roomsRef = database.getReference("rooms");
        DatabaseReference roomRef = roomsRef.child(roomNumber);

        // Delete room data
        roomRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("GameMode1Activity", "Room data deleted: " + roomNumber);
            } else {
                Log.e("GameMode1Activity", "Failed to delete room data: " + task.getException().getMessage());
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();

        // **Placeholders for user leave detection and room data deletion**
        // 1. Check if user left the game (implement logic based on your needs)
        if(isHost==true){
            userLeftGame = true;
        }


        // 2. If user left the game, retrieve room number and delete room data
        if (userLeftGame) {
            if (getIntent().hasExtra("roomNumber")) {
                roomNumber = Multimode1Activity.getRoomnumber(); // Get the room number
                deleteRoomData(roomNumber);
            }
        }

    }
    public static String getRoomnumber(){
        return roomNumber;
    }
    public static String getPlayerId(){return userId;}

    private void makeDialog() {
        Dialog dialog = new Dialog(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_gameover, null);
        dialog.setContentView(dialogView);
        dialog.show();

        Button restartgamebutton = dialogView.findViewById(R.id.restart_game_button);
        Button exitgamebutton = dialogView.findViewById(R.id.exit_game_button);
        restartgamebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeMazePanel();  // 또는 원하는 다른 동작
                dialog.dismiss();
            }
        });

        exitgamebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRoomData(roomNumber);
                Intent intent = new Intent(Multimode1Activity.this, Room.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }
}
