package com.example.tp_game.single;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.tp_game.R;

public class SingleActivity extends AppCompatActivity {

    private SingleMazePanel SinglemazePanel;
    private PopupWindow levelMenuPopup;
    private PopupWindow gameMenuPopup;
    private int level=11; // 레벨 변수 추가
    public static boolean Isautoclicked = true;
    public boolean getautoboolean() {
        return Isautoclicked;
    }

    // Setter method for private variable
    public static void setautoboolean(boolean value) {
        Isautoclicked = value;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        // MazePanel 초기화
        initializeMazePanel();

        // Find menu items
        TextView gameMenu = findViewById(R.id.gameMenu);
        TextView levelMenu = findViewById(R.id.levelMenu);
        TextView computerMenu = findViewById(R.id.computerMenu);
        TextView helpMenu = findViewById(R.id.helpMenu);



        // Computer Menu와 Help Menu의 OnClickListener 설정
        computerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Isautoclicked ==true){
                    SinglemazePanel.autoMove(); // autoMove 메소드 호출
                }
                Isautoclicked=false;
            }
        });

        helpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle help menu click
            }
        });

        // Find direction buttons
        Button upButton = findViewById(R.id.upButton);
        Button rightButton = findViewById(R.id.rightButton);
        Button downButton = findViewById(R.id.downButton);
        Button leftButton = findViewById(R.id.leftButton);

        // Direction buttons의 OnClickListener 설정
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinglemazePanel.movePlayerUp(); // 위쪽으로 이동
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinglemazePanel.movePlayerRight(); // 오른쪽으로 이동
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinglemazePanel.movePlayerDown(); // 아래쪽으로 이동
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinglemazePanel.movePlayerLeft(); // 왼쪽으로 이동
            }
        });

        // 레벨 메뉴를 표시하는 메서드
        levelMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLevelMenuPopup(v);
            }
        });

        // 게임 메뉴를 표시하는 메서드
        gameMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGameMenuPopup(v);
            }
        });
    }



    // 레벨 메뉴를 표시하는 메서드
    private void showLevelMenuPopup(View anchorView) {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_level_menu, null);

        // 팝업 윈도우 생성
        levelMenuPopup = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        levelMenuPopup.setFocusable(true);
        levelMenuPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        levelMenuPopup.showAsDropDown(anchorView);

        // 각 레벨 버튼에 클릭 리스너 설정
        popupView.findViewById(R.id.level1Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinglemazePanel.setMazeSize(11); // 레벨 1에 해당하는 미로 크기 설정
                levelMenuPopup.dismiss(); // 팝업 닫기
            }
        });

        popupView.findViewById(R.id.level2Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinglemazePanel.setMazeSize(15); // 레벨 2에 해당하는 미로 크기 설정
                levelMenuPopup.dismiss(); // 팝업 닫기
            }
        });

        popupView.findViewById(R.id.level3Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinglemazePanel.setMazeSize(18); // 레벨 3에 해당하는 미로 크기 설정
                levelMenuPopup.dismiss(); // 팝업 닫기
            }
        });

        popupView.findViewById(R.id.level4Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinglemazePanel.setMazeSize(21); // 레벨 4에 해당하는 미로 크기 설정
                levelMenuPopup.dismiss(); // 팝업 닫기
            }
        });

        popupView.findViewById(R.id.level5Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinglemazePanel.setMazeSize(26); // 레벨 5에 해당하는 미로 크기 설정
                levelMenuPopup.dismiss(); // 팝업 닫기
            }
        });

        // 추가적인 레벨 버튼들에 대한 설정
    }

    private void showGameMenuPopup(View anchorView) {
        View GamepopupView = LayoutInflater.from(this).inflate(R.layout.new_game_restart_menu, null);

        // 팝업 윈도우 생성
        gameMenuPopup = new PopupWindow(GamepopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        gameMenuPopup.setFocusable(true);
        gameMenuPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameMenuPopup.showAsDropDown(anchorView);

        // 각 레벨 버튼에 클릭 리스너 설정
        GamepopupView.findViewById(R.id.newgameButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinglemazePanel.setMazeSize(level); // 레벨 1에 해당하는 미로 크기 설정
                gameMenuPopup.dismiss(); // 팝업 닫기
            }
        });

        GamepopupView.findViewById(R.id.restartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinglemazePanel.restart();
                gameMenuPopup.dismiss(); // 팝업 닫기
            }
        });
        GamepopupView.findViewById(R.id.goHomeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // 미로 패널 초기화 메서드
    private void initializeMazePanel() {
        FrameLayout mazePanelLayout = findViewById(R.id.mazePanel);
        SinglemazePanel = new SingleMazePanel(this);
        mazePanelLayout.addView(SinglemazePanel);
    }
}