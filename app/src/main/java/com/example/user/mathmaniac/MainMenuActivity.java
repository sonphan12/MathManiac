package com.example.user.mathmaniac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.MobileAds;

public class MainMenuActivity extends AppCompatActivity {
    private ImageView btnPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        MobileAds.initialize(this, "ca-app-pub-5618073725839874~4344928149");
        addControls();
        addEvents();

    }

    private void addEvents() {
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        btnPlay = (ImageView) findViewById(R.id.btnPlay);
    }
}
