package app.animation;

import java.awt.*;

public class Background {

    private ImageLayer[] background;
    private int width;
    private int height;

    public Background(int width, int height, String file,int length){
        this.width = width;
        this.height = height;
        setBackground(file,length);
    }

    private void setBackground(String imgs, int length){
        try {
            background = new ImageLayer[length];

            for(int i = 1; i<=length; i++) {
                background[i - 1] = new ImageLayer(imgs + i + ".png", 0, 0,1*i , width, height);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Back ground image not found");
        }
    }

    public void draw(Graphics g){
            for (int i = background.length-1; i >=0; i--)
                background[i].draw(g);
    }

    public void moveLeftBy(int dx){
        for(int i = 0; i<background.length; i++)
            background[i].moveLeftBy(dx);
    }

    public void moveRightBy(int dx){
        for(int i = 0; i<background.length; i++)
            background[i].moveRightBy(dx);
    }

    public void reset(){
        for(int i = 0; i<background.length; i++)
             background[i].reset();
    }

    public int getX(int i){
        return background[i].getX();
    }

    public int getY(int i){
        return background[i].getY();
    }

    public int getLength(){
        return background.length;
    }

    public boolean isNotLeftEnd(){
        boolean flag = true;

        for(int i = 0; i<background.length; i++) {
            if (background[i].getX() > 0){
                flag = false;
                break;
            }
        }
        return flag;
    }
}
