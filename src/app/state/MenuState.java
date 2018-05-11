package app.state;

import app.main.Framework;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MenuState extends State {

    private BufferedImage buttonH;
    private BufferedImage buttonN;
    private BufferedImage background;

    public MenuState(){
        try{
            background = ImageIO.read(new File("Resources/GUI/GameOverBG.png"));
            buttonN = ImageIO.read(new File("Resources/GUI/buttonN.png"));
            buttonH = ImageIO.read(new File("Resources/GUI/buttonH.png"));
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

        Graphics2D g2 = (Graphics2D) g;

        //Use anti-aliasing to make the text look sharper
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //Set the font and color
        g2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,28));

        drawBG(g2);
        drawStart(g2);
        drawAbout(g2);
        drawQuit(g2);
    }

    private void drawBG(Graphics2D g){
        g.drawImage(background,0,0,null);
        g.drawString("Alien Invasion",Framework.width/2-90,60);
    }

    private void drawStart(Graphics2D g){
        g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        g.drawImage(buttonH,Framework.width/2-100,Framework.height/2-80,200,50,null);
        g.drawString("Start Game",Framework.width/2-50,Framework.height/2-50);
    }

    private void drawAbout(Graphics2D g){
        g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        g.drawImage(buttonN,Framework.width/2-100,Framework.height/2-10,200,50,null);
        g.drawString("About",Framework.width/2-30,Framework.height/2+20);
    }

    private void drawQuit(Graphics2D g){
        g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        g.drawImage(buttonN,Framework.width/2-100,Framework.height/2+60,200,50,null);
        g.drawString("Quit",Framework.width/2-30,Framework.height/2+90);
    }




}
