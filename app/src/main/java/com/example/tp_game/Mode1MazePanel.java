package com.example.tp_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tp_game.single.SingleActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Mode1MazePanel extends View {
    private Mode1Maze maze;
    private int[][] mazeArray;
    private Paint paint;
    private int cellSize;
    private Position current;
    private Position guestPlayerPosition;

    private Position start; // 시작 위치
    private Position end; // 목표 위치
    private Mode1GuestMazePanel mode1GuestMazePanel;

    public String roomNumber;

    private enum Direction {
        // 플레이어의 방향을 나타내는 변수 추가
        UP, DOWN, LEFT, RIGHT
    }

    private Direction playerDirection = Direction.RIGHT; // 초기 방향 설정

    private int delay = 100; // 이동 간격(ms)

    private boolean[][] visitted_way; // 방문한 위치 배열
    private Handler handler; // 핸들러 추가
    private boolean isAutoMoving = false; // autoMove() 실행 여부를 추적하는 플래그


    public Mode1MazePanel(Context context) {
        super(context);
        init();
    }

    public Mode1MazePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public Mode1MazePanel(Context context, AttributeSet attrs, int size) {
        super(context, attrs);
        init(size);
    }

    private void init() {
        maze = new Mode1Maze(11); // 11은 미로 크기, 필요에 따라 조정 가능
        mazeArray = maze.getArray();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        current = new Position(1, 0); // 시작 위치 설정
        roomNumber = Multimode1Activity.getRoomnumber();
        // 시작 위치와 목표 위치 초기화
        start = new Position(1, 0); // 시작 위치
        end = new Position(mazeArray.length - 2, mazeArray.length - 1); // 목표 위치 설정
        guestPlayerPosition = maze.getGueststart();
        String endPoint1 = String.valueOf(mazeArray.length-2);
        String endPoint2 = String.valueOf(mazeArray.length-1);
        String endPoint = endPoint1+" "+endPoint2;
        String startPoint = "1 0";
        String currentPoint = "1 0";


        DatabaseReference endreference = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("endPoint");
        endreference.setValue(endPoint);
        DatabaseReference startreference = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("startPoint");
        startreference.setValue(startPoint);
        DatabaseReference hostPreference = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("hostPosition");
        hostPreference.setValue(currentPoint);
        DatabaseReference guestPreference = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("guestPosition");
        guestPreference.setValue(maze.getGueststart().toString());
        // 방문한 위치 배열 초기화
        visitted_way = new boolean[mazeArray.length][mazeArray.length];

        GameData.getInstance().setGameStarted(true);


    }
    private void init(int size) {
        maze = new Mode1Maze(size); // 11은 미로 크기, 필요에 따라 조정 가능
        mazeArray = maze.getArray();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        current = new Position(1, 0); // 시작 위치 설정
        guestPlayerPosition = maze.getGueststart();

        // 시작 위치와 목표 위치 초기화
        start = new Position(1, 0); // 시작 위치
        end = new Position(mazeArray.length - 2, mazeArray.length - 1); // 목표 위치 설정
        DatabaseReference guestPreference = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("guestPosition");
        guestPreference.setValue(maze.getGueststart().toString());


        // 방문한 위치 배열 초기화
        visitted_way = new boolean[mazeArray.length][mazeArray.length];

        handler = new Handler(); // 핸들러 초기화
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMaze(canvas);
        drawPlayer(canvas);
    }

    public void restart() {
        current = new Position(1, 0);
        invalidate();
    }

    private void drawMaze(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        cellSize = Math.min(width, height) / (mazeArray.length - 2); // 미로의 크기에 맞게 셀 크기 조정
        cellSize *= 0.9;

        for (int i = 0; i < mazeArray.length; i++) {
            for (int j = 0; j < mazeArray[0].length; j++) {
                if (mazeArray[i][j] == 0) {
                    paint.setColor(Color.WHITE); // 길을 나타내는 색상
                } else if (mazeArray[i][j] == 2) {
                    paint.setColor(Color.YELLOW); // 최단 경로를 나타내는 색상 (초록색)
                } else {
                    paint.setColor(Color.BLACK); // 벽을 나타내는 색상
                }
                canvas.drawRect(j * cellSize, i * cellSize, (j + 1) * cellSize, (i + 1) * cellSize, paint);
            }
        }
    }
    public int[][] getmaze(){
        return this.maze.getArray();
    }
    // drawPlayer 메서드에서 이미지 그리기 전에 방향에 따라 이미지 선택하는 코드 추가
    private void drawPlayer(Canvas canvas) {
        // Santa 이미지를 가져옴
        Bitmap santaBitmap;

        // 플레이어 방향에 따라 적절한 이미지 선택
        switch (playerDirection) {
            case UP:
                santaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.santa_up);
                break;
            case DOWN:
                santaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.santa_down);
                break;
            case LEFT:
                santaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.santa_left);
                break;
            case RIGHT:
                santaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.santa_right);
                break;
            default:
                santaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.santa_right); // 기본값은 오른쪽
        }

        // 이미지 크기 조정
        int width = (int) (cellSize * 0.8f);  // 예시로 크기 조정
        int height = (int) (cellSize * 0.8f);
        santaBitmap = Bitmap.createScaledBitmap(santaBitmap, width, height, true);

        // 이미지가 정중앙에 위치하도록 좌표 계산
        float left = (current.getY() + 0.5f) * cellSize - santaBitmap.getWidth() / 2f;
        float top = (current.getX() + 0.5f) * cellSize - santaBitmap.getHeight() / 2f;

       loadGuestPlayerPosition();
        if (guestPlayerPosition != null) { // Check if position is valid
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLUE);
            System.out.println(guestPlayerPosition.getX());
            System.out.println(guestPlayerPosition.getY());
            System.out.println(current.getX());
            System.out.println(current.getY());
            float centerX = (guestPlayerPosition.getX() + 0.5f) * cellSize;
            float centerY = (guestPlayerPosition.getY() + 0.5f) * cellSize;
            canvas.drawCircle(centerX, centerY, cellSize * 0.2f, paint);
        }


        canvas.drawBitmap(santaBitmap, left, top, paint);
        invalidate();
    }
    private void loadGuestPlayerPosition() {
        DatabaseReference mazeRef = FirebaseDatabase.getInstance().getReference("rooms").child(Multimode1Activity.getRoomnumber()).child("guestPosition");
        mazeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Parse the maze data from Firebase (JSON string, 2D array, etc.)
                    String guestPosition = dataSnapshot.getValue(String.class);
                    String[] splitStrings = guestPosition.split(" ");
                    System.out.println(splitStrings);
                    int number1 = Integer.parseInt(splitStrings[0]);
                    int number2 = Integer.parseInt(splitStrings[1]);
                    System.out.println("current");
                    guestPlayerPosition.setX(number2);
                    guestPlayerPosition.setY(number1);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors
            }
        });
    }

    public void setMazeSize(int size) {
        SingleActivity.setautoboolean(true);
        maze = new Mode1Maze(size);
        mazeArray = maze.getArray();
        current = new Position(1, 0); // 시작 위치 초기화
        start = current; // 시작 위치 설정
        end = new Position(size , size+1 ); // 목표 위치 설정
        invalidate(); // 뷰를 다시 그려서 변경된 미로를 표시
    }

    private boolean passed() {
        return current.equals(end);
    }

    private void move(Position current) {
        invalidate(); //repaint 기능


        if (passed()) {

            DatabaseReference winnerRef = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("winner");
            winnerRef.setValue(Multimode1Activity.getPlayerId()); // Replace with your method to get the player ID

            winnerRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String winner = dataSnapshot.getValue(String.class);
                        if(winner == Multimode1Activity.getPlayerId()){
                            Toast.makeText(getContext(), "Congratulations! You have cleared the maze!", Toast.LENGTH_SHORT).show();
                            // Refresh the maze after a 2-second delay
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    GameData.getInstance().setGameFinished(true);
                                }
                            }, 2000);
                        }else{
                            Toast.makeText(getContext(), "Game over!", Toast.LENGTH_SHORT).show();
                            // Refresh the maze after a 2-second delay
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    GameData.getInstance().setGameFinished(true);
                                    //setMazeSize(maze.getSize());
                                    //invalidate();
                                }
                            }, 3000);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database errors
                }
            });

            handler = new Handler(); // 핸들러 초기화

        }

    }

    // up 버튼 눌렀을 때 호출되는 메서드 수정
    public void movePlayerUp() {
        if (maze.canMoveUp(current)) {
            current.setX(current.getX() - 1);
            move(current);
            String hostPosition = String.valueOf(current.getX())+" "+String.valueOf(current.getY());
            DatabaseReference posreference = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("hostPosition");
            posreference.setValue(hostPosition);
            playerDirection = Direction.UP;
        }
    }

    // down 버튼 눌렀을 때 호출되는 메서드 수정
    public void movePlayerDown() {
        if (maze.canMoveDown(current)) {
            current.setX(current.getX() + 1);
            move(current);
            String hostPosition = String.valueOf(current.getX())+" "+String.valueOf(current.getY());
            DatabaseReference posreference = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("hostPosition");
            posreference.setValue(hostPosition);
            playerDirection = Direction.DOWN;
        }
    }

    // left 버튼 눌렀을 때 호출되는 메서드 수정
    public void movePlayerLeft() {
        if (maze.canMoveLeft(current)) {
            current.setY(current.getY() - 1);
            move(current);
            String hostPosition = String.valueOf(current.getX())+" "+String.valueOf(current.getY());
            DatabaseReference posreference = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("hostPosition");
            posreference.setValue(hostPosition);
            playerDirection = Direction.LEFT;
        }
    }

    // right 버튼 눌렀을 때 호출되는 메서드 수정
    public void movePlayerRight() {
        if (maze.canMoveRight(current)) {
            current.setY(current.getY() + 1);
            move(current);
            String hostPosition = String.valueOf(current.getX())+" "+String.valueOf(current.getY());
            DatabaseReference posreference = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("hostPosition");
            posreference.setValue(hostPosition);
            playerDirection = Direction.RIGHT;
        }
    }



}