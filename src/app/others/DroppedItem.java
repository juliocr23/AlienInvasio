package app.others;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class DroppedItem {
    private BufferedImage rocket;

    public DroppedItem(){
        try {
            rocket = ImageIO.read(new File("Resources/DropItems/rocket0.png"));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void draw(Graphics g,int x, int y){
        g.drawImage(rocket,x,y,null);
    }
}
