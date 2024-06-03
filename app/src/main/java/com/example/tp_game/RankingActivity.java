package com.example.tp_game;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RankingActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        LinearLayout rankingContainer = findViewById(R.id.rankingContainer);

        DatabaseReference rankref = FirebaseDatabase.getInstance().getReference("ranking").child("mode1");

        // Read the rankings from Realtime Database
        rankref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Map.Entry<String, Long>> rankingList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String userId = childSnapshot.getKey();
                        Long score = childSnapshot.getValue(Long.class);
                        rankingList.add(new java.util.AbstractMap.SimpleEntry<>(userId, score));
                    }

                    // Sort the rankings by score (ascending order)
                    Collections.sort(rankingList, new Comparator<Map.Entry<String, Long>>() {
                        @Override
                        public int compare(Map.Entry<String, Long> entry1, Map.Entry<String, Long> entry2) {
                            return entry1.getValue().compareTo(entry2.getValue());
                        }
                    });
                    System.out.println(rankingList);

                    // Update the ranking container
                    rankingContainer.removeAllViews(); // Clear existing views
                    for (int i = 0; i < rankingList.size(); i++) {
                        Map.Entry<String, Long> entry = rankingList.get(i);
                        String userId = entry.getKey();
                        Long score = entry.getValue();
                        System.out.println(userId);
                        System.out.println(score);

                        TextView rankingTextView = new TextView(RankingActivity.this);
                        rankingTextView.setText((i + 1) + ". " + userId + " - " + score + "ms");
                        rankingTextView.setPadding(0, 8, 0, 8);
                        rankingTextView.setTextColor(Color.WHITE);
                        rankingContainer.addView(rankingTextView);
                    }
                } else {
                    Toast.makeText(RankingActivity.this, "순위 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RankingActivity.this, "순위 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
