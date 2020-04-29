package com.example.preyhunterfinal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;


public class PageNiveau extends AppCompatActivity {

    final String EXTRA_TEXT= "text_to_display";
    MediaPlayer player;
    int levelCompleted;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_niveau);
        player = MediaPlayer.create(this, R.raw.musique); //select music file
        player.start();
        if (getIntent()!=null){
            Intent intent=getIntent();
            boolean levelIsCompleted=intent.getBooleanExtra("ISFINISHED",false);
            int level=intent.getIntExtra("LEVEL",1);
            int levelPreviouslyCompleted=intent.getIntExtra("LEVEL_COMPLETED",0);
            levelCompleted=levelPreviouslyCompleted;
            if (levelIsCompleted){
                if (level>levelPreviouslyCompleted) {
                    levelCompleted = level;
                }
            }
        }else {
            levelCompleted=0;
        }
        levelColorUpdate();


    }

    private void levelColorUpdate() {
        if (levelCompleted>=1){
            Button level1= (Button) findViewById(R.id.button2);
            Button level2= (Button) findViewById(R.id.button3);
            ViewCompat.setBackgroundTintList(level1, ContextCompat.getColorStateList(this, android.R.color.holo_green_light));
            ViewCompat.setBackgroundTintList(level2, ContextCompat.getColorStateList(this, android.R.color.white));
            if (levelCompleted>=2){
                Button level3=(Button) findViewById(R.id.button4);
                ViewCompat.setBackgroundTintList(level2, ContextCompat.getColorStateList(this, android.R.color.holo_green_light));
                ViewCompat.setBackgroundTintList(level3, ContextCompat.getColorStateList(this, android.R.color.white));
                if (levelCompleted>=3){
                    Button level4=(Button) findViewById(R.id.button5);
                    ViewCompat.setBackgroundTintList(level3, ContextCompat.getColorStateList(this, android.R.color.holo_green_light));
                    ViewCompat.setBackgroundTintList(level4, ContextCompat.getColorStateList(this, android.R.color.white));
                    if (levelCompleted>=4){
                        Button level5=(Button) findViewById(R.id.button6);
                        ViewCompat.setBackgroundTintList(level4, ContextCompat.getColorStateList(this, android.R.color.holo_green_light));
                        ViewCompat.setBackgroundTintList(level5, ContextCompat.getColorStateList(this, android.R.color.white));
                        if (levelCompleted>=5){
                            Button level6=(Button) findViewById(R.id.button7);
                            ViewCompat.setBackgroundTintList(level5, ContextCompat.getColorStateList(this, android.R.color.holo_green_light));
                            ViewCompat.setBackgroundTintList(level6, ContextCompat.getColorStateList(this, android.R.color.white));
                            if (levelCompleted>=6){
                                ViewCompat.setBackgroundTintList(level6, ContextCompat.getColorStateList(this, android.R.color.holo_green_light));
                            }
                        }
                    }
                }
            }

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        player.stop();

        player = MediaPlayer.create(this, R.raw.musique); //select music file
        player.stop();
    }

    @Override
    public void onResume(){
        super.onResume();
        player.stop();

    }

    public void laFinDuDebut(android.view.View view){
        Intent intent = new Intent(PageNiveau.this, GameActivity.class);
        intent.putExtra("LEVEL",1);
        intent.putExtra("ENEMY_AMOUNT",10);
        intent.putExtra("ENEMY_ONSCREEN",2);
        intent.putExtra("LEVEL_COMPLETED",levelCompleted);
        System.out.println(intent.getIntExtra("LEVEL",4));
        player = MediaPlayer.create(this, R.raw.tir); //select music file
        player.start();
        startActivity(intent);
    }

    private void mustCompleteOtherLevelsToast() {
        Context context = getApplicationContext();
        CharSequence text = "Veuillez finir les niveaux précédents!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }



    public void laSuiteDesEnnuis(android.view.View view){
        if (levelCompleted>=1) {
            Intent intent = new Intent(PageNiveau.this, GameActivity.class);
            intent.putExtra("LEVEL", 2);
            intent.putExtra("ENEMY_AMOUNT", 20);
            intent.putExtra("ENEMY_ONSCREEN", 3);
            intent.putExtra("LEVEL_COMPLETED",levelCompleted);
            player = MediaPlayer.create(this, R.raw.tir); //select music file
            player.start();
            startActivity(intent);
        }else{
            mustCompleteOtherLevelsToast();
        }
    }



    public void etCEstPasFini(android.view.View view){
        if (levelCompleted>=2) {
            Intent intent = new Intent(PageNiveau.this, GameActivity.class);
            intent.putExtra("LEVEL", 3);
            intent.putExtra("ENEMY_AMOUNT", 20);
            intent.putExtra("ENEMY_ONSCREEN", 4);
            intent.putExtra("LEVEL_COMPLETED",levelCompleted);
            player = MediaPlayer.create(this, R.raw.tir); //select music file
            player.start();
            startActivity(intent);
        }else{
            mustCompleteOtherLevelsToast();
        }
    }

    public void cours(android.view.View view){
        if (levelCompleted>=3) {
            Intent intent = new Intent(PageNiveau.this, GameActivity.class);
            intent.putExtra("LEVEL", 4);
            intent.putExtra("ENEMY_AMOUNT", 20);
            intent.putExtra("ENEMY_ONSCREEN", 6);
            intent.putExtra("LEVEL_COMPLETED",levelCompleted);
            player = MediaPlayer.create(this, R.raw.tir); //select music file
            player.start();
            startActivity(intent);
        }else{
            mustCompleteOtherLevelsToast();
        }
    }

    public void operationElimination(android.view.View view){
        if (levelCompleted>=4) {
            Intent intent = new Intent(PageNiveau.this, GameActivity.class);
            intent.putExtra("LEVEL", 5);
            intent.putExtra("ENEMY_AMOUNT", 30);
            intent.putExtra("ENEMY_ONSCREEN", 4);
            intent.putExtra("LEVEL_COMPLETED",levelCompleted);
            player = MediaPlayer.create(this, R.raw.tir); //select music file
            player.start();
            startActivity(intent);
        }else{
            mustCompleteOtherLevelsToast();
        }
    }

    public void debutDeLaFin(android.view.View view){
        if (levelCompleted>=5) {
            Intent intent = new Intent(PageNiveau.this, GameActivity.class);
            intent.putExtra("LEVEL", 6);
            intent.putExtra("ENEMY_AMOUNT", 45);
            intent.putExtra("ENEMY_ONSCREEN", 5);
            intent.putExtra("LEVEL_COMPLETED",levelCompleted);
            player = MediaPlayer.create(this, R.raw.tir); //select music file
            player.start();
            startActivity(intent);
        }else{
            mustCompleteOtherLevelsToast();
        }
    }


}
