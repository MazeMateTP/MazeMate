package com.example.tp_game;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class Mode1Maze {
    private int size;
    private Random random;
    private int[][] array;
    private boolean[][] visitted;
    private boolean[][] visitted_way;
    private int paths = 0;
    private int steps = 0;
    private Position Hoststart;
    private Position Gueststart;
    public String roomNumber;

    public int getSize() {
        return size; // 미로 배열의 외곽 벽을 제외한 크기 반환
    }
    public Position getHoststart(){
        return Hoststart;
    }
    public Position getGueststart(){
        return Gueststart;
    }
    public Mode1Maze(int size) {
        if(size % 2 == 0) size++;
        this.size = size;
        random = new Random();
        array = new int[size + 2][size + 2];
        visitted = new boolean[size + 2][size + 2];
        visitted_way = new boolean[size + 2][size + 2];

        generate();
    }
    public Mode1Maze(int[][] array1){
        this.array= array1;
        this.size = (array1.length-2);


    }

    public int[][] getArray() {
        return this.array;
    }

    private boolean canUp(Position position) {
        return position.getX() - 2 > 0;
    }

    private boolean canRight(Position position) {
        return position.getY() + 2 <= size;
    }

    private boolean canDown(Position position) {
        return position.getX() + 2 <= size;
    }

    private boolean canLeft(Position position) {
        return position.getY() - 2 > 0;
    }

    private Position randNext(Position current) {
        ArrayList<Integer> nexts = new ArrayList<Integer>();
        if (canUp(current))
            nexts.add(0);
        if (canRight(current))
            nexts.add(1);
        if (canDown(current))
            nexts.add(2);
        if (canLeft(current))
            nexts.add(3);

        int value = nexts.get(random.nextInt(nexts.size()));
        switch (value) {
            case 0: {
                return new Position(current.getX() - 2, current.getY());
            }
            case 1: {
                return new Position(current.getX(), current.getY() + 2);
            }
            case 2: {
                return new Position(current.getX() + 2, current.getY());
            }
            case 3: {
                return new Position(current.getX(), current.getY() - 2);
            }
            default:
                throw new IllegalArgumentException("Unexpected value: " + value);
        }
    }

    private void creatPath(Position current, Position next) {
        int x = (current.getX() + next.getX()) / 2;
        int y = (current.getY() + next.getY()) / 2;
        array[x][y] = 0;
    }

    public boolean canMoveUp(Position position) {
        return position.getX() - 1 > 0 && array[position.getX() - 1][position.getY()] == 0;
    }

    public boolean canMoveRight(Position position) {
        return position.getY() + 1 <= size + 1 && array[position.getX()][position.getY() + 1] == 0;
    }

    public boolean canMoveDown(Position position) {
        return position.getX() + 1 <= size && array[position.getX() + 1][position.getY()] == 0;
    }

    public boolean canMoveLeft(Position position) {
        return position.getY() - 1 > 0 && array[position.getX()][position.getY() - 1] == 0;
    }

    public void generate() {
        // First settup
        for (int i = 0; i < size + 2; i++) {
            array[0][i] = 1;
            array[size + 1][i] = 1;
            array[i][0] = 1;
            array[i][size + 1] = 1;
        }

        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                if (i % 2 == 1 && j % 2 == 1) {
                    array[i][j] = 0;
                    visitted[i][j] = false;
                    paths++;
                    visitted_way[i][j] = false;
                } else {
                    array[i][j] = 1;
                }
            }
        }

        array[1][0] = 0;

        array[size][size + 1] = 0;

        // Draw matrix
        int x_current = 1, y_current = 1;
        Position current = new Position(x_current, y_current);
        int visitedCell = 1;
        visitted[x_current][y_current] = true;
        while (visitedCell < paths) {
            steps++;
            Position next = randNext(current);
            int x_next = next.getX();
            int y_next = next.getY();

            if (visitted[x_next][y_next] == false) {
                visitted[x_next][y_next] = true;
                visitedCell++;
                creatPath(current, next);
            }

            // Update current position
            current.setX(next.getX());
            current.setY(next.getY());
        }
        ArrayList<Integer> second = new ArrayList<Integer>();
        for (int i = 1; i < size + 1; i++) {
            if (array[i][1] == 0) {
                second.add(i);

            }
        }
        int values = second.get(random.nextInt(second.size()));
        System.out.println(values);
        array[values][0]=0;
        Hoststart = new Position(1,0);
        Gueststart = new Position(values,0);

        roomNumber = Multimode1Activity.getRoomnumber();
        DatabaseReference HSreference = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("hostStartPoint");
        HSreference.setValue(Hoststart.toString());
        DatabaseReference GSreference = FirebaseDatabase.getInstance().getReference("rooms").child(roomNumber).child("guestStartPoint");
        GSreference.setValue(Gueststart.toString());

    }
    public static void main(String[] args) {
        int size = 11;
        Position start = new Position(1, 0);
        Position end = new Position(size, size + 1);
        Maze maze = new Maze(size);

        maze.print();

        maze = new Maze(size);
        System.out.println("-----------------");
        maze.print();
    }
}