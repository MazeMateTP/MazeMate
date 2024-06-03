package com.example.tp_game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Button logoutButton = findViewById(R.id.btnlogout);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                logout();
                Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
    private void logout() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Logout successful
                            // Perform post-logout events here (Toast message, screen finish)
                            Toast.makeText(SettingActivity.this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // Logout failed
                            Toast.makeText(SettingActivity.this, "로그아웃에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}