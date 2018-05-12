package app.state;

import app.main.Framework;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class AboutState extends State {

    private String about;
    private BufferedImage bg;
    private BufferedImage buttonH;
    private BufferedImage buttonN;
    private boolean flag;
    private boolean enter;

    public AboutState(){
        about = "Author: Julio Rosario\n" +
                "Contact : rosariojulio40@gmail.com\n"+
                "Last Update: 5/11/2018";

        flag = false;
        enter = false;

        try{
            bg = ImageIO.read(new File("Resources/GUI/bg.png"));
            buttonN = ImageIO.read(new File("Resources/GUI/buttonN.png"));
            buttonH = ImageIO.read(new File("Resources/GUI/buttonH.png"));
        }catch (Exception e){

        }
    }

    @Override
    public void update() {
            if(enter){
                if(flag)
                    System.exit(0);
                else
                    State.setState(Framework.menuState);
            }
    }

    @Override
    public void processInput() {

        boolean up   = Framework.keyboard.keyDownOnce(KeyEvent.VK_UP);
        boolean down = Framework.keyboard.keyDownOnce(KeyEvent.VK_DOWN);
               enter = Framework.keyboard.keyDownOnce(KeyEvent.VK_ENTER);

        if(down)
            flag = true;

        if(up)
            flag = false;
    }

    @Override
    public void draw(Graphics g) {

        Graphics2D g2 = Framework.get2DGraphics(g);

        //Set the font and color
        g2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));

        g2.drawImage(bg,0,0,null);
        drawString(g2,about,100,150);

        drawGoBackToMenu(g2);
        drawQuit(g2);

    }

    private void drawGoBackToMenu(Graphics2D g2){

        if(!flag)
             g2.drawImage(buttonH,250,250,120,40,null);
        else
            g2.drawImage(buttonN,250,250,120,40,null);

        g2.drawString("Menu",285,275);
    }

    private void drawQuit(Graphics2D g2){

        if(flag)
            g2.drawImage(buttonH,250,310,120,40,null);
        else
            g2.drawImage(buttonN,250,310,120,40,null);

        g2.drawString("Quit",285,335);
    }


    private void drawString(Graphics2D g, String text, int x, int y) {
        for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }
}
