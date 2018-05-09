package app.sprites;


/**
 * Important NOTE: You don't need booleans, you can do if(activity == Activity.Run) etc.
 * Last update was making the plaer go dizzy when is hit by an enemy.
 */



import app.main.Framework;
import app.others.Sound;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class Player extends Sprite {

    private enum Activity{
        IDLE(0),
        RUN(1),
        JUMP(2),
        RUN_WITH_GUN(3),
        SHOT(4),
        DEAD(5),
        DIZZY(6);

        private final int num;
        Activity(int num) {
            this.num = num;
        }
        public int getValue(){
            return num;
        }
    }

    private Activity activity;         //The activities of the player
    private boolean shot;              //Determine if player shot
    private boolean runWithGun;
    private boolean isMovingRight;
    private boolean isShooting;
    private boolean isDizzy;

    private Image img;                //The image to be drawn
    private double lastY;             //Last recorded y value
    private int score = 0;            //Player's score

    private Sound coinSound;          //Sound for coin
    private Sound jumpSound;          //Sound for jump
    private Sound shotSound;          //Sound for shot
    private boolean soundJmp;

    private Bullet[] bullet;          //Player's bullet
    private int counter;              //Bullet counter

    private int health  = 100;       //Player's health
    private int ammunition = 95;      //Player's ammunition

    private Image scoreUI1;
    private Image scoreUI2;
    private Image amUI1;
    private Image amUI2;
    private Image healthUI1;
    private Image healthUI2;


    public Player(double x, double y,int w, int h, double vxi, double vyi, double ax, double ay){
        super(x,y,w,h,vxi,vyi,ax,ay);

        addAction("Idle");
        addAction("Run");
        addAction("Jump");
        addAction("RunWithGun");
        addAction("Shot");
        addAction("Die");
        addAction("Dizzy");

        addDuration(20);                  //Duration for Idle
        addDuration(5);                  //Duration for Run
        addDuration(2);                  //Duration for jump
        addDuration(5);                  //Duration for running with gun
        addDuration(5);                  //Duration for shooting while standing
        addDuration(5);                  //Duration for dying
        addDuration(5);                  //Duration for being dizzy

        addLength(12);                   //Number of images for Idle
        addLength(10);                   //Number of images for Run
        addLength(6);                    //Number of images for jump
        addLength(10);                   //Number of images for running with gun
        addLength(5);                    //Number of images for shooting while standing
        addLength(12);                   //Number of images for dying
        addLength(4);                    //Number of images for being dizzy

        setImgFilesPath("Resources\\player\\");   //Set the path to where player's images are
        loadAnimation();                       //Load the animation

        activity = Activity.IDLE;              //Start standing still

        rectOffsetX = 10;
        rectOffsetY = 50;

        rectangle.height = rectangle.height-50;
        rectangle.y = rectangle.y+50;
        rectangle.width = rectangle.width-30;
        rectangle.x = rectangle.x+10;

        isMovingRight = true;
        soundJmp = false;
        lastY = y;
        isShooting = false;

        bullet = new Bullet[3];
        counter = -1;

        coinSound = new Sound("Resources/Sound/coin.wav");
        coinSound.setVolume(-25.0f);

        jumpSound = new Sound("Resources/Sound/jump.wav");
        jumpSound.setVolume(-30.0f);

        shotSound = new Sound("Resources/Sound/shot.wav");

        loadImgs();
    }

    @Override
    public void draw(Graphics g) {

        //Draw the player
        g.drawImage(img,(int)x,(int)y,width,height,null);

        //Draw health and bullet UI
        drawUI(g);

        //Draw the bullet
        for(int i = 0; i<= counter; i++){
           bullet[i].draw(g);
        }
    }

    public void update(){

       tryToGetCoin();
       tryToGetBullet();

       super.update();

       moveRightUpdate();
       moveLeftUpdate();

        //if(playerOffScreen() || World.isCollidingWithEnemy()){
        //    health -= 33;
       // }
       updateActivity();
       updateBullets();
       updateImg();
    }

    public void getInput(){

        moveR      =  Framework.keyboard.keyDown(KeyEvent.VK_RIGHT);
        moveL      =  Framework.keyboard.keyDown(KeyEvent.VK_LEFT);

      if(Framework.keyboard.keyDownOnce(KeyEvent.VK_S))
          runWithGun = !runWithGun;

        setShot(Framework.keyboard.keyDownOnce(KeyEvent.VK_A));

        if(isOnGround)
            setMoveUp(Framework.keyboard.keyDownOnce(KeyEvent.VK_UP));

        if(!isMoveUp())  moveDown = true;

        else             moveDown = false;

    }

    public void updateActivity(){

        standStill();       //Stand still if it is not moving

       // if(isDizzy)
        run();             //Run to the left or right if they were pressed

        shoot();           //Shoot enemies if A button was pressed

        jump();            //Jump if the up button was pressed

        reset();          //Reset the animation if animation is over
    }

    public void moveRight() {
        super.moveRight();
    }

    public void moveLeft() {
        super.moveLeft();
    }

    public void moveRightUpdate(){
        if(isMoveR()) {
            if(!isMovingRight)    //Adjust the y for rectangle
                rectangle.x -= 8;

            isMovingRight = true;
        }
    }

    public void moveLeftUpdate(){
        if(isMoveL()) {

            if(isMovingRight)     //Adjust the x for rectangle
                rectangle.x += 8;

            isMovingRight = false;
        }
    }

    public void updateImg(){

        img = getNextImg(activity.getValue());
        height = (int)(img.getHeight(null)*0.2);
        width =  (int)(img.getWidth(null)*0.2);

        if(!isMovingRight){ //Create a new image and flipped horizontally and
                            // swap pointers with img

            BufferedImage newImg = new BufferedImage(width, height, ((BufferedImage)img).getType());
            Graphics2D g2 = newImg.createGraphics();

            g2.drawImage(img,width,0,-width,height,null);
            g2.dispose();
            img = newImg;
        }
    }

    public void updateBullets(){

        for(int i = 0; i<= counter && i < bullet.length; i++){
                bullet[i].launch();
        }
    }

    public void moveUp() {
        if(moveUp){

            //As long as it is not touching a tile on top
            //and it is not touching the top of the screen
            if((y-vyi) >0 && y >(lastY-135)){
                y -= vyi;
                rectangle.y -= vyi;

                if(!soundJmp){
                    jumpSound.play();
                    soundJmp = true;
                }

            }else if((y-vyi) <= 0){
                y = 0;
                rectangle.y = (int)(y+rectOffsetY);
                vyi = prevVyi;

                moveUp = false;
                soundJmp = false;
            }else if(y <=(lastY-135)){
                vyi = prevVyi;

                moveUp = false;
                soundJmp = false;
            }
        }
    }

    public void moveDown(){

        if(moveDown){
            if(!isOnGround){
                y += vyi;
                rectangle.y += vyi;
            }else if(isOnGround){
                rectangle.y = (getTy2(rectangle.y, rectangle.height, vyi) * World.getTileSize()-rectangle.height)-1;
                y = rectangle.y -rectOffsetY;
                lastY = y;
            }
        }
    }

    public void tryToGetBullet(){
        int tx2 =  getTx2(rectangle.x+Math.abs(World.getX()),rectangle.width,vxi);
        int tx1 =  getTx1(rectangle.x+Math.abs(World.getX()),0);
        int ty1 =  getTy1(rectangle.y,0);
        int ty2 =  getTy2(rectangle.y,rectangle.height,0);

        if(World.isSolidBullet(tx2,ty1) || World.isSolidBullet(tx2,ty2) ||
           World.isSolidBullet(tx1,ty2) || World.isSolidBullet(tx1,ty1))
        {
            counter = -1;
            ammunition  = 95;
            World.removeBullet(tx2,ty1);
            World.removeBullet(tx2,ty2);
            World.removeBullet(tx1,ty2);
            World.removeBullet(tx1,ty1);

        }
    }

    public void tryToGetCoin(){

        int tx2 =  getTx2(rectangle.x+Math.abs(World.getX()),rectangle.width,vxi);
        int tx1 =  getTx1(rectangle.x+Math.abs(World.getX()),0);
        int ty1 =  getTy1(rectangle.y,0);
        int ty2 =  getTy2(rectangle.y,rectangle.height,0);

        if(World.isSolidCoin(tx2,ty1) || World.isSolidCoin(tx2,ty2) ||
           World.isSolidCoin(tx1,ty2) || World.isSolidCoin(tx1,ty1))
        {
            coinSound.play();
            score += 100;
            World.removeCoin(tx2,ty1);
            World.removeCoin(tx2,ty2);
            World.removeCoin(tx1,ty2);
            World.removeCoin(tx1,ty1);

        }
    }

    public boolean isMoving(){
        return shot || moveUp || moveL || moveR || isShooting  || isDizzy;
    }

    public boolean playerOffScreen(){
        return y >= Framework.height;
    }

  //----------------------------------------------------Getters and Setters-------------------------------------------------------\\

    public void setMoveUp(boolean flag){
        if(!moveUp) moveUp = flag;
    }

    public void setShot(boolean flag){
         shot = flag;
    }

    public boolean isShot(){
        return shot;
    }

    public int getScore(){
        return score;
    }

    public int getHealth(){
        return health;
    }

    public void setHealth(int h){
        health += h;
    }

    private void drawUI(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        //Use anti-aliasing to make the text look sharper
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //Set the font and color
        g2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));
        g2.setColor(Color.WHITE);

        //Draw score UI
        g2.drawImage(scoreUI1,10,0,120,40,null);
        g2.drawImage(scoreUI2,12,3,40,30,null);
        g2.drawString(""+score,70,25);

        //Draw ammunition UI
        g2.drawImage(amUI1,150,0,140,38,null);
        g2.drawImage(amUI2,184,5,ammunition,25,null);

        //Draw health UI
        g2.drawImage(healthUI1,300,0,140,38,null);
        g2.drawImage(healthUI2,330,5,health,25,null);

    }

    private void loadImgs(){
        try {
            scoreUI1 = ImageIO.read(new File("Resources/GUI/score1.png"));
            scoreUI2 = ImageIO.read(new File("Resources/GUI/score2.png"));

            amUI1 = ImageIO.read(new File("Resources/GUI/ammunition1.png"));
            amUI2 = ImageIO.read(new File("Resources/GUI/ammunition2.png"));

            healthUI1 = ImageIO.read(new File("Resources/GUI/healthUI1.png"));
            healthUI2 = ImageIO.read(new File("Resources/GUI/healthUI2.png"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public Bullet[] getBullet(){
        return bullet;
    }

    public int getCounter(){
        return counter;
    }

    public void standStill(){
        if(!isMoving())
            activity = Activity.IDLE;
    }

    public void run(){

        if(isRunning() && isOnGround) {
            if(runWithGun)
                activity = Activity.RUN_WITH_GUN;
            else
                activity = Activity.RUN;
        }
    }

    public void shoot(){

        if(shot && runWithGun){

            if(!moveR || !moveL) {
                activity = Activity.SHOT;
                isShooting = true;
            }
            else
                activity = Activity.RUN_WITH_GUN;

            if(counter < bullet.length-1) {
                counter++;
                ammunition -= 32;

                if(isMovingRight)
                    bullet[counter] = new Bullet((int) x+50, (int) y+35,true);
                else
                    bullet[counter] = new Bullet((int) x, (int) y+35,false);
            }

            shotSound.play();
        }
    }

    public void jump(){
        if(isMoveUp())
            activity = Activity.JUMP;
    }

    public void reset(){

        if(animationOver(activity.getValue())) {
            reset(activity.getValue());
            isShooting = false;
            isDizzy = false;
        }
    }


    public void setToDizzy(){
        activity = Activity.DIZZY;
        isDizzy = true;
    }

    public void setToDie(){
        activity = Activity.DEAD;
    }
}
