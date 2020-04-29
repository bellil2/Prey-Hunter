package com.example.preyhunterfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameView extends SurfaceView implements Runnable {
    Context context;

    //boolean variable to track if the game is playing or not
    volatile boolean playing;
    MediaPlayer player;
    //the game thread
    private Thread gameThread = null;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private List<Enemy> enemies;
    private int enemyCount;
    private int totalEnemyCount;
    private int level;
    private int levelCompleted;

    int screenX;
    int screenY;

    int enemyHit;

    boolean flag ;

    private boolean isGameOver ;
    private boolean isFinished ;
    private boolean isReloading ;

    int score;

    int ammunition;
    Bitmap background;
    Bitmap button;

    int highScore[]=new int[4];
    SharedPreferences sharedPreferences;
    //Class constructor
    public GameView(Context context, int screenX, int screenY,int level, int enemiesOnScreen, int maximumNumberEnemies,int levelCompleted) {
        super(context);
        surfaceHolder = getHolder();
        paint = new Paint();
        this.level=level;
        this.levelCompleted=levelCompleted;
        enemyCount=enemiesOnScreen;
        totalEnemyCount=maximumNumberEnemies;
        enemies = new ArrayList<>();
        this.context=context;
        for(int i=0; i<enemyCount; i++){
            enemies.add(new Enemy(context, screenX, screenY,10));
        }
        this.screenX = screenX;
        this.screenY=screenY;

        enemyHit = 0;

        isGameOver = false;
        isFinished=false;
        isReloading=false;

        score = 0;
        ammunition=5;
        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME",Context.MODE_PRIVATE);

        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);
        highScore[3] = sharedPreferences.getInt("score4",0);

        background=BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        button=BitmapFactory.decodeResource(context.getResources(), R.drawable.button);

    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }


    private void update() {
        if (totalEnemyCount!=0){
            for (Enemy enemy: enemies){
                enemy.update(3);
                if(enemy.getX()==screenX){
                    flag = true;
                }
                if (flag){
                    if (enemy.getX()>=screenX){
                        ++enemyHit;
                        flag=false;
                        if (enemyHit >=3){
                            playing=false;
                            isGameOver=true;

                            for(int i=0;i<4;i++){
                                if(highScore[i]<score){

                                    final int finalI = i;
                                    highScore[i] = score;
                                    break;
                                }
                            }

                            //storing the scores through shared Preferences
                            SharedPreferences.Editor e = sharedPreferences.edit();
                            for(int i=0;i<4;i++){
                                int j = i+1;
                                e.putInt("score"+j,highScore[i]);
                            }
                            e.apply();
                        }

                    }
                }
            }
        } else {
            isFinished=true;
            playing=false;

            for(int i=0;i<4;i++){
                if(highScore[i]<score){

                    final int finalI = i;
                    highScore[i] = score;
                    break;
                }
            }

            //storing the scores through shared Preferences
            SharedPreferences.Editor e = sharedPreferences.edit();
            for(int i=0;i<4;i++){
                int j = i+1;
                e.putInt("score"+j,highScore[i]);
            }
            e.apply();
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {

            canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(background, 0, 0, paint);


            //ammunition
            ammunitionStatus();
            //Health bar
            healthBarStatus();
            // Enemy
            enemyDraw();

            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
                buttonMenuDraw();
                buttonRecommencerDraw();
            }
            if(isFinished){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Niveau terminé",canvas.getWidth()/2,yPos,paint);
                buttonMenuDraw();
            }



            surfaceHolder.unlockCanvasAndPost(canvas);

        }

    }

    private void buttonRecommencerDraw() {
        canvas.drawBitmap(button,screenX/2-260,screenY/2+250,paint);
        paint.setTextSize(70);
        canvas.drawText("Recommencer",screenX/2+20,screenY/2+375,paint);
    }

    private void buttonMenuDraw() {
        canvas.drawBitmap(button,screenX/2-260,screenY/2+60,paint);
        paint.setTextSize(70);
        canvas.drawText("Menu",screenX/2+20,screenY/2+185,paint);

    }

    private void enemyDraw() {
        for (ListIterator<Enemy> iterator = enemies.listIterator(); iterator.hasNext();){
            Enemy enemy=iterator.next();
            if (enemy.getPresent()) {
                canvas.drawBitmap(

                        enemy.getBitmap(),
                        enemy.getX(),
                        enemy.getY(),
                        paint
                );
            } else {
                enemy.setX(screenX+100);
            }
        }
    }

    private void ammunitionStatus() {
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        canvas.drawText("Score:"+score,100,50,paint);
        canvas.drawText(ammunition+"/5",150,screenY-50,paint);
        canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.reload),50,screenY-110,paint);

        if (ammunition==0){
            canvas.drawBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.warning),250,screenY-100,paint);
        }
    }

    private void healthBarStatus() {
        Bitmap heart= BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
        Bitmap missingHeart=BitmapFactory.decodeResource(context.getResources(), R.drawable.missingheart);
        if (enemyHit<=2){canvas.drawBitmap(heart,screenX-450,0,paint);}else{canvas.drawBitmap(missingHeart,screenX-450,0,paint);}
        if (enemyHit<=1){canvas.drawBitmap(heart,screenX-300,0,paint);}else{canvas.drawBitmap(missingHeart,screenX-300,0,paint);}
        if (enemyHit==0){canvas.drawBitmap(heart,screenX-150,0,paint);}else{canvas.drawBitmap(missingHeart,screenX-150,0,paint);}
    }


    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        //when the game is paused
        //setting the variable to false
        playing = false;
        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        //when the game is resumed
        //starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                TimerTask reload=new TimerTask() { //Reload timer with 1sec
                    @Override
                    public void run() {
                        ammunition=5;
                        isReloading=false;
                    }
                };
                //Reload button
                reloadButton(x, y, reload);

                if (ammunition>0 && !isFinished){
                    //Enemy detection
                    enemyTouchColision(x, y);
                    //Remove an ammo if we hit or miss
                    if (ammunition>0) {
                        player = MediaPlayer.create(context, R.raw.tir); //select music file
                        player.start();
                        ammunition -= 1;
                    }
                }else{
                    automaticReload(reload);
                }
                if (isFinished){
                    //Button Menu
                    buttonMenuDetection(x, y);
                }
                if (isGameOver){
                    //Button Menu
                    buttonMenuDetection(x, y);
                    //Button Recommencer
                    buttonRecommencerDetection(x, y);
                }

        }

        return false;
    }

    private void buttonRecommencerDetection(int x, int y) {
        int left=screenX/2-260;
        int top=screenY/2+250;
        Rect detectCollision=new Rect(left,top, left+button.getWidth(), top+button.getHeight());
        if (detectCollision.contains(x,y)){
            Intent intent = new Intent(context, GameActivity.class);
            context.startActivity(intent);

        }
    }

    private void buttonMenuDetection(int x, int y) {
        int left=screenX/2-260;
        int top=screenY/2+60;
        Rect detectCollision=new Rect(left,top, left+button.getWidth(), top+button.getHeight());
        if (detectCollision.contains(x,y)){
            Intent intent = new Intent(context, PageNiveau.class);
            intent.putExtra("ISFINISHED",isFinished);
            intent.putExtra("LEVEL",level);
            intent.putExtra("LEVEL_COMPLETED",levelCompleted);
            context.startActivity(intent);

        }
    }

    private void automaticReload(TimerTask reload) {
        if (!isReloading) {
            // if you don't have any ammo, when you touch the screen you have an automatic reload but it is 1 second longer
            LocalDateTime twoSecondLater = LocalDateTime.now().plusSeconds(2);
            Date twoSecondsLaterDate = Date.from(twoSecondLater.atZone(ZoneId.systemDefault()).toInstant());
            new Timer().schedule(reload, twoSecondsLaterDate);
            isReloading=true;
            player = MediaPlayer.create(context, R.raw.rechargement); //select music file
            player.start();
        }
    }

    private void enemyTouchColision(int x, int y) {
        for (ListIterator<Enemy> iterator = enemies.listIterator(); iterator.hasNext();){
            Enemy enemy=iterator.next();
            if (enemy.getDetectCollision().contains(x,y)){
                totalEnemyCount-=1;

                score += enemy.getPoints();
                if (totalEnemyCount>=enemyCount) {
                    Random generator = new Random();
                    Bitmap bitmap=enemy.getBitmap();
                    enemy.setX(screenX);
                    enemy.setY(Math.abs(generator.nextInt(screenY) - bitmap.getHeight()));

                }else{
                    enemy.setPresent(false);
                }
                break;
            }
        }
    }

    private void reloadButton(int x, int y, TimerTask reload) {
        Bitmap reloadBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.reload);
        Rect detectCollision=new Rect(50,screenY-110, 50+reloadBitmap.getWidth(), screenY-110+reloadBitmap.getHeight());
        if (detectCollision.contains(x,y) && !isReloading){
            ammunition=0;
            LocalDateTime twoSecondLater=LocalDateTime.now().plusSeconds(1);
            Date twoSecondsLaterDate=Date.from(twoSecondLater.atZone(ZoneId.systemDefault()).toInstant());
            new Timer().schedule(reload,twoSecondsLaterDate);
            isReloading=true;
            player = MediaPlayer.create(context, R.raw.rechargement); //select music file
            player.start();

        }
    }
}