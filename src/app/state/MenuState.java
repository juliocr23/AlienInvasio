package app.state;

import app.main.Framework;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MenuState extends State {

    private BufferedImage background1;
    private BufferedImage background2;

    public MenuState(){
        try{
            background1 = ImageIO.read(new File("Resources/GUI/menuBG.png"));
            background2 = ImageIO.read(new File("Resources/GUI/GameOverBG.png"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void processInput() {

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(background1,0,0,Framework.width,Framework.height,null);
        g.drawImage(background2,Framework.width/2-200,Framework.height/2-150,400,280,null);
    }
}
