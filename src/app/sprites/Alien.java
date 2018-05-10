/**
 * Fix health bar, make the boss smart.
 * Create gameOver state.
 * Once the player defeat the boss, display Some GUI with congratulation and stuff.
 */

package app.sprites;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Alien extends Sprite {

    private enum Activity{
        RUN(0),
        EXPLOSION(1),
        FIRE(2);
        private final int num;
        Activity(int num) {
            this.num = num;
        }
        public int getValue(){
            return num;
        }
    }

    private Activity activity;
    private boolean isMovingRight;
    private Image img;
    private int numberOfHits;      //The number of times the alien need to be hit for exploding
    private int hitCounter;        //Record the number of time the alien has been hit

    public Alien(double x, double y,int w, int h,String file,double scale){
        super(x,y,w,h);

        addAction("run");
        addAction("explosion");

        addDuration(10);      //Duration for Run
        addDuration(5);      //Duration for explosion

        addLength(6);       //Length of images for run
        addLength(7);       //Length of images for explosion

        setImgFilesPath(file);
        loadAnimation();

        activity  = Activity.RUN;

        vxi = 1;
        vxf = 5;

        rectangle.height = rectangle.height-55;
        rectangle.width = rectangle.width-40;
        moveR = true;
        scaleBy = scale;
        numberOfHits = 1;
        hitCounter = 0;
    }

    public void update(int dx, int dy){
      super.update();

        if(animationOver(activity.getValue())) {
            reset(activity.getValue());
        }

        updateImage();

        rectangle.x =(int) (dx+x+4);
        rectangle.y = dy+19;
    }

    public void draw(Graphics g,int dx, int dy){

        int drawX =(int)(x+dx);
        int drawY = (int)(y+dy);

        g.drawImage(img,drawX,drawY,width,height,null);
    }

    public void moveRight(){

        if(moveR){
            if(isOnGround(rectangle.width,0) && !isFacingRTile()){
                x += vxi;
                rectangle.x += vxi;
            }else{
                moveR = false;
                moveL = true;
            }
            isMovingRight = true;
        }
    }

    public void moveLeft(){
        if(moveL){


            if(isOnGround(0,-rectangle.width) && !isFacingLTile() && rectangle.x > 0){
                x -= vxi;
                rectangle.x -= vxi;
            }else{
                moveL = false;
                moveR = true;
            }
            isMovingRight = false;
        }
    }

    public boolean isOnGround(int c1,int c2){

        //To prevent the Alien to go off the tile, the width was added to the x
        int tx1 =  getTx1(rectangle.x+Math.abs(World.getX())+c1,0);
        int tx2 =  getTx2(rectangle.x+Math.abs(World.getX())+c2,rectangle.width,0);
        int ty2 =  getTy2(rectangle.y,rectangle.height,vyi);


        return  isColliding(tx1,ty2) || isColliding(tx2,ty2);
    }

    public void updateImage(){

        img = getNextImg(activity.getValue());
        height = (int)(img.getHeight(null)*scaleBy);
        width =  (int)(img.getWidth(null)*scaleBy);

        if(!isMovingRight){ //Create a new image and flipped horizontally and
                            //swap pointers with img

            BufferedImage newImg = new BufferedImage(width, height, ((BufferedImage)img).getType());
            Graphics2D g2 = newImg.createGraphics();

            g2.drawImage(img,width,0,-width,height,null);
            g2.dispose();
            img = newImg;
        }
    }

    private boolean isFacingRTile(){

          int tx1   =  (int) Math.abs((rectangle.x+rectangle.width)/World.getTileSize());
          int ty1   = (int)Math.abs(rectangle.y/World.getTileSize());
          return World.isSolidTile(tx1,ty1);
    }

    private boolean isFacingLTile(){

        int tx1 = (int)Math.abs(rectangle.x/World.getTileSize());
        int ty1 = (int)Math.abs(rectangle.y/World.getTileSize());


        return World.isSolidTile(tx1,ty1);
    }

    public void setToDead(){
            activity = Activity.EXPLOSION;
    }

    public void setToHit(){
        hitCounter++;
    }

    public boolean isDead(){
        return  activity == Activity.EXPLOSION &&
                animationOver(activity.getValue());
    }

    public void dontMove(){
        moveR = false;
        moveL = false;
    }

    public void setNumberOfHits(int hits){
        numberOfHits = hits;
    }

    public boolean isExploading(){
        return activity == Activity.EXPLOSION;
    }

    public int getHitCounter(){
        return hitCounter;
    }

    public int getNumberOfHits(){
        return numberOfHits;
    }


}
