package app.sprites;

import app.others.Rectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Bullet extends app.others.Rectangle {

    private BufferedImage bullet;
    private double velocity = 2;
    private double acceleration  = 0.1;
    private boolean launchRight;


    public Bullet(int dx, int dy,boolean f){
        super(dx,dy,0,0);
        launchRight = f;

        try {
            bullet = ImageIO.read(new File("Resources/player/Bullet1.png"));
            width =  (int)(bullet.getWidth()*0.04);
            height = (int)(bullet.getHeight()*0.2);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        if(!launchRight){
                BufferedImage newImg = new BufferedImage(bullet.getWidth(), bullet.getHeight(), bullet.getType());
                Graphics2D g2 = newImg.createGraphics();
                g2.drawImage(bullet,bullet.getWidth(),0,-bullet.getWidth(),bullet.getHeight(),null);
                g2.dispose();
                bullet = newImg;
        }
    }

    public void draw(Graphics g){
        g.drawImage(bullet,(int)x,(int)y,width,height, null);
    }

    public void launch(){

        if(velocity <= 8)
            velocity += acceleration;

        if(launchRight){
            x += velocity;
        }else{
            x -= velocity;
        }
    }
}
