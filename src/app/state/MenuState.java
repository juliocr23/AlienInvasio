package app.state;

import app.main.Framework;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class MenuState extends State {

    private BufferedImage buttonH;
    private BufferedImage buttonN;
    private BufferedImage background;
    private int counter = 0;
    private boolean enter = false;

    public MenuState(){
        try{
            background = ImageIO.read(new File("Resources/GUI/bg.png"));
            buttonN = ImageIO.read(new File("Resources/GUI/buttonN.png"));
            buttonH = ImageIO.read(new File("Resources/GUI/buttonH.png"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update() {

        if(enter) {
            if (counter == 0)
                State.setState(Framework.gameState);

            if (counter == 1)
                State.setState(Framework.aboutState);

            if (counter == 2)
                System.exit(0);
        }
    }

    @Override
    public void processInput() {
        boolean up    = Framework.keyboard.keyDownOnce(KeyEvent.VK_UP);
        boolean down  = Framework.keyboard.keyDownOnce(KeyEvent.VK_DOWN);
                enter = Framework.keyboard.keyDownOnce(KeyEvent.VK_ENTER);

        if(down && counter <2)
            counter++;

        if(up && counter>0)
            counter --;
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

        g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        drawStart(g2);
        drawAbout(g2);
        drawQuit(g2);
    }

    private void drawBG(Graphics2D g){
        g.drawImage(background,0,0,null);
        g.drawString("Alien Invasion",Framework.width/2-90,60);
    }

    private void drawStart(Graphics2D g){

        if(counter == 0)
            g.drawImage(buttonH,Framework.width/2-100,Framework.height/2-80,200,50,null);
        else
            g.drawImage(buttonN,Framework.width/2-100,Framework.height/2-80,200,50,null);

        g.drawString("Start Game",Framework.width/2-50,Framework.height/2-50);
    }

    private void drawAbout(Graphics2D g){

        if(counter == 1)
            g.drawImage(buttonH,Framework.width/2-100,Framework.height/2-10,200,50,null);
        else
            g.drawImage(buttonN,Framework.width/2-100,Framework.height/2-10,200,50,null);

        g.drawString("About",Framework.width/2-30,Framework.height/2+20);
    }

    private void drawQuit(Graphics2D g){

        if(counter == 2)
            g.drawImage(buttonH,Framework.width/2-100,Framework.height/2+60,200,50,null);
        else
            g.drawImage(buttonN,Framework.width/2-100,Framework.height/2+60,200,50,null);

        g.drawString("Quit",Framework.width/2-30,Framework.height/2+90);
    }
}
