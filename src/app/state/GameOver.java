package app.state;

import app.main.Framework;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class GameOver extends State {

    private boolean isQuit;
    private boolean isRetry;
    private BufferedImage background;
    private BufferedImage buttonN;
    private BufferedImage buttonH;
    private boolean flag;
    private boolean enter;

    public GameOver(){

        try{
            background = ImageIO.read(new File("Resources/GUI/GameOverBG.png"));
            buttonN = ImageIO.read(new File("Resources/GUI/buttonN.png"));
            buttonH = ImageIO.read(new File("Resources/GUI/buttonH.png"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        flag = false;
        enter = false;

    }

    @Override
    public void update() {

        if(enter){
            if(flag){
                System.exit(0);
            }
            else{
               State.setState(Framework.gameState);
            }
        }
    }

    public void processInput(){
      boolean down =   Framework.keyboard.keyDownOnce(KeyEvent.VK_DOWN);
      boolean up   =   Framework.keyboard.keyDownOnce(KeyEvent.VK_UP);
      enter        =   Framework.keyboard.keyDownOnce(KeyEvent.VK_ENTER);

      if(down){
          flag = true;
      }

      if(up){
          flag = false;
      }


    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        //Use anti-aliasing to make the text look sharper
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.drawImage(background,0,0,null);

        //Set the font and color
        g2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));
        g2.setColor(Color.BLACK);
        g2.drawString("GAME OVER",(Framework.width/2)-50,Framework.height/2-200);

        drawRetryButton(g2);
        drawQuitButton(g2);
    }

    private void drawRetryButton(Graphics2D g){

        if(!flag)
            g.drawImage(buttonH,(Framework.width/2)-50,Framework.height/2-80,100,50,null);
        else
            g.drawImage(buttonN,(Framework.width/2)-50,Framework.height/2-80,100,50,null);

        g.drawString("Retry",(Framework.width/2)-20,Framework.height/2-50);
    }

    private void drawQuitButton(Graphics2D g){

        if(flag)
            g.drawImage(buttonH,(Framework.width/2)-50,Framework.height/2-25,100,50,null);
        else
            g.drawImage(buttonN,(Framework.width/2)-50,Framework.height/2-25,100,50,null);

        g.drawString("Quit",(Framework.width/2)-20,Framework.height/2+5);
    }

    public boolean isQuit() {
        return isQuit;
    }

    public boolean isRetry() {
        return isRetry;
    }
}
