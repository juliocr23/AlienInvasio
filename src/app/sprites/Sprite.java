/**
 * Change velocity,
 * Fix background to parallel scrolling,
 * Add Sprite like coins and enemies, make
 * the map stop at the beginning of the map for
 * boss. Music to went the player collide with a coin
 * Music for when the player attack an enemy.
 *
 */

package app.sprites;


import app.animation.Animation;
import app.main.Framework;
import app.others.Rectangle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public  class Sprite {

    protected double x;                            //The x position of the sprite
    protected double y;                            //The y position of the sprite
    private double initialX;
    private double initialY;

    private ArrayList<Animation> animation;      //A list containing all the animation each action has

    private ArrayList<String> action;            //A list containing all the actions of the sprite
    private ArrayList<Integer> length;           //The number of images per action
    private ArrayList<Integer> duration;         //The duration of animation per action
    private String imgFilesPath;                 //The directory path where images will be found.

    protected boolean moveL;                       //Sprite is moving left
    protected boolean moveR;                       //Sprite is moving right
    protected boolean moveUp;                      //Sprite is moving up
    protected boolean moveDown;                    //Sprite is moving down
    protected boolean isInTheMiddle;               //Sprite is in the middle of the screen

    protected boolean isFacingLeftTile;           //Sprite is facing a left tile
    protected boolean isFacingRightTile;          //Sprite is facing a right tile
    protected boolean isOnGround;                  //Sprite is touching the ground tile
    protected boolean isTouchingRoof;              //Sprite is touching the roof tile

    protected double vxi;                         //Initial velocity of the sprite in the x direction
    protected double vyi;                         //Initial velocity of the sprite in the y direction

    protected double ax;                          //Acceleration of the sprite in the x direction
    protected double ay;                          //Acceleration of the sprite in the y direction

    protected   double vxf;                       //Final velocity of the sprite in the x direction
    protected   double vyf;                       //Final velocity of the sprite in the y direction

    protected double scaleBy;                     //The value used to scale the sprite/animation/image

    protected int width;                          //The width of the sprite
    protected int height;                         //The height of the sprite

    private String format;                        //The image format for the sprite

    private int leftWall;                         //The left wall the player shouldn't walk to
    private int rightWall;                        //The right wall the player shouldn't walk to

    protected app.others.Rectangle rectangle;                //The rectangle used for collision detection

    protected double prevVxi;                     //Previous initial x velocity
    protected double prevVyi;                     //Previously  initial y velocity

    protected double rectOffsetX;                 //The offset of the rectangle used for collision in the x direction
    protected double rectOffsetY;                 //The offset of the rectangle used  for collision in the y direction


    public Sprite(double x, double y, int w, int h,double vxi,double vyi, double ax,double ay){

      animation = new ArrayList<>();
      action = new ArrayList<>();
      length = new ArrayList<>();
      duration = new ArrayList<>();

      imgFilesPath = null;
      this.x = x;
      this.y = y;
      initialX = x;
      initialY = y;

      this.vxi = vxi;
      this.vyi = vyi;

      this.vxf = vxi;
      this.vyf = vyi;

      prevVxi = vxi;
      prevVyi = vyi;

      this.ax = ax;
      this.ay = ay;

      moveL = false;
      moveR = false;

      moveUp = false;
      moveDown = false;

      width = w;
      height = h;

      leftWall = 0;
      rightWall = 0;

      format = ".png";

      rectangle = new app.others.Rectangle((int)x,(int)y,width,height);

      rectOffsetX = 0;
      rectOffsetY = 0;

    }

    public Sprite(double x, double y,int w, int h){

        animation = new ArrayList<>();
        action = new ArrayList<>();
        length = new ArrayList<>();
        duration = new ArrayList<>();

        imgFilesPath = null;
        this.x = x;
        this.y = y;

        this.vxi = 0;
        this.vyi = 0;

        this.vxf = vxi;
        this.vyf = vyi;

        prevVxi = vxi;
        prevVyi = vyi;

        this.ax = 0;
        this.ay = 0;

        moveL = false;
        moveR = false;

        moveUp = false;
        moveDown = false;

        width = w;
        height = h;

        leftWall = 0;
        rightWall = 0;

        format = ".png";

        rectangle = new Rectangle((int)x,(int)y,width,height);

        rectOffsetX = 0;
        rectOffsetY = 0;
    }


    /**
     * The getInput method get the Sprite's input
     * for moving right, left,up and down.
     */
    public void getInput(){
        moveR    = Framework.keyboard.keyDown(KeyEvent.VK_RIGHT);
        moveL    = Framework.keyboard.keyDown(KeyEvent.VK_LEFT);
        moveUp   = Framework.keyboard.keyDown(KeyEvent.VK_UP);
        moveDown = Framework.keyboard.keyDown(KeyEvent.VK_DOWN);
    }

   public void update(){
        isFacingRightTile = isFacingRTile();
        isFacingLeftTile  = isFacingLTile();
        isOnGround        = isOnT();
        isTouchingRoof    = isTileAbove();
        isInTheMiddle     = isInTheMiddle();

        if(!isMoving())                //If is not doing anything reset velocity
            vxi = prevVxi;

        if(isRunning()) {             //If is running increase velocity in the x direction
            if ((vxi + ax) <= vxf)
                vxi += ax;
        }

        if(moveUp || moveDown) {     //If is going up or down increase velocity in the y direction
            if ((vyi + ay) <= vyf)
                vyi += ay;
        }

       moveRight();
       moveLeft();
       moveUp();
       moveDown();
   }

    public void draw(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect((int)(rectangle.x),
                   (int)(rectangle.y),
                         rectangle.width,
                         rectangle.height);
    }

    /**
     * The loadAnimation method load
     * the animation of the sprites
     */

    public void loadAnimation(){
        for(int i = 0; i<action.size(); i++){
            Animation an = new Animation(imgFilesPath + action.get(i),
                                          format, length.get(i), duration.get(i));
            animation.add(an);
        }
    }

    /**
     * The isMoving method
     * @return True if the Sprite is moving to the right,
     *         left,up or down.
     */

    public boolean isMoving(){
        return  moveL || moveR || moveDown || moveUp;
    }

    /**
     * The isInTheMiddle
     * @return True if the sprite is in the middle of
     * the screen. Otherwise return false.
     */

    private boolean isInTheMiddle(){
        double wall = (Framework.width/2)-(width);
        return x > wall-1;
    }


    /**
     * The isStart method
     * @return True if the world is at the start of the tile map.
     *         otherwise return false.
     */

    public boolean isStart(){
        return World.getX()+vxi < 0;
    }

    public void moveRight(){

        if(moveR){

            if(!isFacingRightTile && !isInTheMiddle){
                x += vxi;
                rectangle.x += vxi;
            }
        }
    }

    public void moveLeft(){

        if(moveL){

            if(x-vxi <= 0){
                x = 0;
                rectangle.x = (int)(x+20);
                return;
            }

            if(!isStart() || (!isFacingLeftTile && !isInTheMiddle)) {
                x -= vxi;
                rectangle.x -= vxi;
            }

        }
    }

    public void moveUp(){

        if(moveUp){

            if(!isTouchingRoof && (y-vyi) >0){
                y -= vyi;
                rectangle.y -= vyi;
            }else if((y-vyi) <= 0){
                y = 0;
                rectangle.y =(int) (y+rectOffsetY);
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
            }
        }
    }

    public boolean isColliding(int x, int y){
        return World.isSolidTile(x,y);
    }

    private boolean isFacingRTile(){

        int tx2 =  getTx2(rectangle.x+Math.abs(World.getX()),rectangle.width,vxi);
        int ty1 =  getTy1(rectangle.y,0);
        int ty2 =  getTy2(rectangle.y,rectangle.height,0);

        return isColliding(tx2,ty1) || isColliding(tx2,ty2);
    }
    private boolean isFacingLTile(){

        int tx1 =  getTx1(rectangle.x+Math.abs(World.getX()),-vxi);
        int ty1 =  getTy1(rectangle.y,0);
        int ty2 =  getTy2(rectangle.y,rectangle.height,0);

        return isColliding(tx1,ty1)  || isColliding(tx1,ty2);
    }

    private boolean isTileAbove(){
        int tx1 =   getTx1(rectangle.x+Math.abs(World.getX()),0);
        int tx2 =   getTx2(rectangle.x+Math.abs(World.getX()),rectangle.width,0);
        int ty1 =   getTy1(rectangle.y,-vyi);

        return isColliding(tx1,ty1) || isColliding(tx2,ty1);
    }

    private boolean isOnT(){
        int tx1 =  getTx1(rectangle.x+Math.abs(World.getX()),0);
        int tx2 =  getTx2(rectangle.x+Math.abs(World.getX()),rectangle.width,0);
        int ty2 =  getTy2(rectangle.y,rectangle.height,vyi);

        return  isColliding(tx1,ty2) || isColliding(tx2,ty2);
    }

    /**
     * The getTx1 method gets the x coordinate of a tile, in tiles
     * by using the left x coordinate of the rectangle.
     * @param c The distance the sprite is moving into
     * @return The x coordinate or column of the tile
     */

    public int getTx1(double x, double c){
        return  Math.abs((int)(x+c)/World.getTileSize());
    }

    /**
     * The getTx2 method gets the x coordinate of a tile, in tiles
     * by using the right (x+width) vertex of the rectangle.
     * @param c The distance the sprite is moving into
     * @return The x coordinate or column of the tile
     */

    public int getTx2(double x,double w,double c){
        return  Math.abs((int)(x+w+c)/World.getTileSize());
    }

    /**
     * The getTy1 method gets the y coordinate of a tile, in tiles
     * by using the left y coordinate of the rectangle.
     * @param c The distance the sprite is moving into
     * @return The y coordinate or column of the tile
     */

    public int getTy1(double y,double c){
      return   Math.abs((int)(y+c)/World.getTileSize());
    }

    /**
     * The getTy2 method gets the y coordinate of a tile, in tiles
     * by using the (y+height) vertex the rectangle.
     * @param c The distance the sprite is moving into
     * @return The y coordinate or column of the tile
     */

    public int getTy2(double y,double h,double c){
       return Math.abs((int)(y+h+c)/World.getTileSize());
    }

    /**
     * The isRunning method
     * @return True if the sprite is moving left or right.
     *         Otherwise return false.
     */

    public boolean isRunning(){
        return isMoveL() || isMoveR();
    }

    public boolean isEndOfWorld(){

        int tx2 =  getTx2(rectangle.x+Math.abs(World.getX()),rectangle.width,vxi);

        return  tx2  == (World.getWorldMaxX()-1);

    }

//--------------------------------------------------------------Getters And Setters------------------------------------------------------------------\\

    public int getLeftWall() {
        return leftWall;
    }

    public void setLeftWall(int leftWall) {
        this.leftWall = leftWall;
    }

    public int getRightWall() {
        return rightWall;
    }

    public void setRightWall(int rightWall) {
        this.rightWall = rightWall;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setAction(String[] obj){
        for(int i = 0; i<obj.length; i++)
            action.add(obj[i]);
    }

    public void setAction(ArrayList<String> obj){
        for(int i = 0; i<obj.size(); i++)
            action.add(obj.get(i));
    }

    public void addAction(String str){
        action.add(str);
    }

    public void setLength(int[] obj){
        for(int i = 0; i<obj.length; i++)
            length.add(obj[i]);
    }

    public void setLength(ArrayList<Integer> obj){
        for(int i = 0; i<obj.size(); i++)
            length.add(obj.get(i));
    }

    public void addLength(int num){
        length.add(num);
    }

    public void setDuration(int[] obj){
        for(int i = 0; i<obj.length; i++)
            duration.add(obj[i]);
    }

    public void setDuration(ArrayList<Integer> obj){
        for(int i = 0; i<obj.size(); i++)
            duration.add(obj.get(i));
    }

    public void addDuration(int num){
        duration.add(num);
    }

    public void setImgFilesPath(String str){
        imgFilesPath = str;
    }

    public void setX(double x) {
        this.x = x;
        rectangle.x = (int)(x+rectOffsetX);
    }
    public double getX() {
        return x;
    }

    public void setY(double y){
        this.y = y;
        rectangle.y = (int)(y + rectOffsetY);
    }

    public double getY() {
        return y;
    }

    public void setAx(double ax){
        this.ax = ax;
    }
    public double getAx(){
        return ax;
    }

    public void setAy(double ay){

        this.ay = ay;
    }
    public double getAy() {
        return ay;
    }

    public void setVxi(double vx){
        this.vxi = vx;
    }

    public double getVx(){

        return vxi;
    }

    public void setVyi(double vy){
        this.vyi = vy;
    }

    public double getVy(){
        return vyi;
    }

    public void setVxf(double vx){
        this.vxf = vx;
    }
    public double getVxf(){
        return vxf;
    }

    public void setVyf(double vy){
        this.vyf = vy;
    }

    public double getVyf(){
        return vyf;
    }

    public void setScaleBy(double d) {
        scaleBy = d;
    }

    public double getScaleBy(){
        return scaleBy;
    }

    public boolean isMoveL(){
        return moveL;
    }

    public boolean isMoveR(){
        return moveR;
    }

    public boolean isMoveUp() {
        return moveUp;
    }

    public boolean isMoveDown() {
        return moveDown;
    }

    public void add(String file, String format, int length, int delay){
        try {
            animation.add(new Animation(file,format,length,delay));
        }catch(Exception e){}
    }

    public Image getNextImg(int i){
       return animation.get(i).nextImage();
    }

    public boolean animationOver(int i){
        return animation.get(i).isAnimationOver();
    }

    public void reset(int i){
        animation.get(i).reset();
    }

    public boolean isInMiddleOfScreen(){
        return isInTheMiddle;
    }

    public boolean isFacingRightTile(){
        return isFacingRightTile;
    }

    public boolean isFacingLeftTile() {
        return isFacingLeftTile;
    }

    public boolean isOnTile() {
        return isOnGround;
    }

    public Rectangle getRectangle(){
        return rectangle;
    }

    public boolean overlap(Rectangle rect2){
        return rectangle.overlaps(rect2);
    }

    public void resetPosition(){
        x = initialX;
        y = initialY;
    }
}
