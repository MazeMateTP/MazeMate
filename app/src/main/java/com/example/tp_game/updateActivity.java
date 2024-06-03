/*
package com.example.tp_game;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import android.os.Handler;

public class updateActivity extends Worker {

*/
/*    private final double user1Latitude=0;
    private final double user1Longitude;
    private final double user2Latitude;
    private final double user2Longitude;*//*

    private Handler handler;

    public updateActivity(Context context, WorkerParameters workRequest) {
        super(context,workRequest);

        // 두 사용자의 위치 정보 추출
*/
/*        user1Latitude = workRequest.getArguments().getDouble("user1Latitude", 0.0);
        user1Longitude = workRequest.getArguments().getDouble("user1Longitude", 0.0);
        user2Latitude = workRequest.getArguments().getDouble("user2Latitude", 0.0);
        user2Longitude = workRequest.getArguments().getDouble("user2Longitude", 0.0);*//*


    }



    @NonNull
    @Override
    public Result doWork() {
        // 데이터베이스에 위치 정보 업데이트
        updateLocationInDatabase(user1Latitude, user1Longitude, user2Latitude, user2Longitude);

        // UI 업데이트 (메인 UI 스레드에서 실행)
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateUserLocationUI(user1Latitude, user1Longitude, user2Latitude, user2Longitude);
            }
        });

        return Result.success();
    }

    private void updateLocationInDatabase(double user1Latitude, double user1Longitude, double user2Latitude, double user2Longitude) {
        // 실제 데이터베이스 업데이트 코드를 작성합니다.
    }

    private void updateUserLocationUI(double user1Latitude, double user1Longitude, double user2Latitude, double user2Longitude) {
        // UI를 업데이트하여 사용자 위치를 다시 그리는 코드를 작성합니다.
    }
}
*/
