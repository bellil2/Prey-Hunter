package com.example.preyhunterfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

public class PageAccueil extends AppCompatActivity {

    ConstraintLayout layoutPrincipal;
    final String EXTRA_TEXT= "text_to_display";
    TextView saisie;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
    }

    @Override
    public void onResume(){
        super.onResume();
        player = MediaPlayer.create(this, R.raw.musique); //select music file
        player.setLooping(true);
        player.start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        player.stop();
    }


    public void pageNiveau(android.view.View view){
        Intent intent = new Intent(PageAccueil.this, PageNiveau.class);
        player = MediaPlayer.create(this, R.raw.rechargement); //select music file
        player.start();
        startActivity(intent);
    }

    public void couperMusique(android.view.View view){
        player.stop();
    }

    public void reprendreMusique(android.view.View view){
        player = MediaPlayer.create(this, R.raw.musique);
        player.start();
    }


}

