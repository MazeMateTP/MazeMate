package com.example.tp_game.single;



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

import com.example.tp_game.Maze;
import com.example.tp_game.Position;
import com.example.tp_game.R;
import com.example.tp_game.single.SingleActivity;

import java.util.Stack;

public class SingleMazePanel extends View {
    private Maze maze;
    private int[][] mazeArray;
    private Paint paint;
    private int cellSize;
    private Position current;

    private Position start; // 시작 위치
    private Position end; // 목표 위치

    private enum Direction {
        // 플레이어의 방향을 나타내는 변수 추가
        UP, DOWN, LEFT, RIGHT
    }

    private Direction playerDirection = Direction.RIGHT; // 초기 방향 설정

    private int delay = 100; // 이동 간격(ms)

    private boolean[][] visitted_way; // 방문한 위치 배열
    private Handler handler; // 핸들러 추가
    private boolean isAutoMoving = false; // autoMove() 실행 여부를 추적하는 플래그


    public SingleMazePanel(Context context) {
        super(context);
        init();
    }

    public SingleMazePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        maze = new Maze(11); // 11은 미로 크기, 필요에 따라 조정 가능
        mazeArray = maze.getArray();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        current = new Position(1, 0); // 시작 위치 설정

        // 시작 위치와 목표 위치 초기화
        start = new Position(1, 0); // 시작 위치
        end = new Position(mazeArray.length - 2, mazeArray.length - 1); // 목표 위치 설정

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

        // 이미지 그리기
        canvas.drawBitmap(santaBitmap, left, top, paint);
    }

    public void setMazeSize(int size) {
        SingleActivity.setautoboolean(true);
        maze = new Maze(size);
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
            Toast.makeText(getContext(), "Congratulations! You have cleared the maze!", Toast.LENGTH_SHORT).show();
            // Refresh the maze after a 3-second delay
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setMazeSize(maze.getSize());
                    invalidate();
                }
            }, 3000);
        }

    }

    // up 버튼 눌렀을 때 호출되는 메서드 수정
    public void movePlayerUp() {
        if (maze.canMoveUp(current)) {
            current.setX(current.getX() - 1);
            move(current);
            // 방향 설정
            playerDirection = Direction.UP;
        }
    }

    // down 버튼 눌렀을 때 호출되는 메서드 수정
    public void movePlayerDown() {
        if (maze.canMoveDown(current)) {
            current.setX(current.getX() + 1);
            move(current);
            // 방향 설정
            playerDirection = Direction.DOWN;
        }
    }

    // left 버튼 눌렀을 때 호출되는 메서드 수정
    public void movePlayerLeft() {
        if (maze.canMoveLeft(current)) {
            current.setY(current.getY() - 1);
            move(current);
            // 방향 설정
            playerDirection = Direction.LEFT;
        }
    }

    // right 버튼 눌렀을 때 호출되는 메서드 수정
    public void movePlayerRight() {
        if (maze.canMoveRight(current)) {
            current.setY(current.getY() + 1);
            move(current);
            // 방향 설정
            playerDirection = Direction.RIGHT;
        }
    }

    public void autoMove() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println();
                Stack<Position> way = maze.getDirectWay(start, end);
                Paint paint = new Paint();
                paint.setColor(Color.YELLOW);
                while (!way.empty()) {
                    Position next = way.pop();
                    int x = next.getX();
                    int y = next.getY();
                    mazeArray[x][y] = 2; // 최단 경로로 표시
                    postInvalidate(); // UI 스레드에서 invalidate() 호출
                    try {
                        Thread.sleep(delay); // 지연 시간 적용
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}