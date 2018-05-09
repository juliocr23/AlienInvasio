package app.animation;

import java.awt.*;

public class Coin extends Animation {

    private static String  file = "Resources/Coins/Coin";
    private static String  format = ".png";
    private static int length = 4;
    private static int duration = 30;
    private static int width = 40;
    private static int height = 40;


    public Coin(){
      super(file,format,length,duration);
    }

    public void update(){
        if(isAnimationOver())
            reset();
    }

    public void draw(Graphics g,int x, int y){
        g.drawImage(nextImage(),x, y,width,height, null);
    }
}
