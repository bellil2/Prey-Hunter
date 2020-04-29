package com.example.preyhunterfinal;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    //declaring gameview
    private GameView gameView;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Getting display object
        Display display = getWindowManager().getDefaultDisplay();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Getting the screen resolution into point object
        Point size = new Point();
        display.getSize(size);

        //Getting the data from the intent for the level difficulty
        Intent intent=getIntent();
        int level=intent.getIntExtra("LEVEL",1);
        int enemiesOnScreen=intent.getIntExtra("ENEMY_AMOUNT",10);
        int maximumNumberEnemies=intent.getIntExtra("ENEMY_ONSCREEN",3);
        int levelCompleted=intent.getIntExtra("LEVEL_COMPLETED",0);
        //Initializing game view object
        //this time we are also passing the screen size to the GameView constructor
        gameView = new GameView(this, size.x, size.y,level,maximumNumberEnemies,enemiesOnScreen,levelCompleted);


        //adding it to contentview
        setContentView(gameView);
    }

    //pausing the game when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    //running the game when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
        player = MediaPlayer.create(this, R.raw.musique); //select music file
        player.setLooping(true);
        player.start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        player.stop();
    }
}
