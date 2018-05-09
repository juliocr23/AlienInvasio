package app.main;

import java.awt.*;

public class GameOver extends State {

    private boolean isQuit;
    private boolean isRetry;

    public GameOver(){


    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        //Use anti-aliasing to make the text look sharper
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //Set the font and color
        g2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));
        g2.setColor(Color.WHITE);
        g2.drawString("GAME OVER",(Framework.width/2)-50,Framework.height/2);
    }

    public boolean isQuit() {
        return isQuit;
    }

    public boolean isRetry() {
        return isRetry;
    }
}
