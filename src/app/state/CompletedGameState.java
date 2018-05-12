package app.state;

import app.main.Framework;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class CompletedGameState extends  State {

    private BufferedImage bg;
    private BufferedImage star;
    private BufferedImage money;
    private BufferedImage enemy;

    private int totalEnemy;
    private int totalMoney;
    private int enemyKilled;
    private int moneyCollected;

    public CompletedGameState(){

        totalMoney = 0;
        totalEnemy = 0;
        enemyKilled = 0;
        moneyCollected = 0;

        try{
            bg = ImageIO.read(new File("Resources/GUI/completebg.png"));
            star = ImageIO.read(new File("Resources/GUI/star.png"));
            money = ImageIO.read(new File("Resources/GUI/score2.png"));
            enemy = ImageIO.read(new File("Resources/GUI/defeated.png"));
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
        Graphics2D g2 = Framework.get2DGraphics(g);

        //Set the font and color
        g2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));

        //Draw Background
        g2.drawImage(bg,120,0,400,Framework.height,null);

        //Draw money collected
        g2.drawImage(money,280,310,35,35,null);
        g2.drawString("Money",220,330);
        g2.drawString(""+totalMoney +"/" +moneyCollected,320,333);

        //Draw enemy killed
        g2.drawImage(enemy,280,365,35,35,null);
        g2.drawString("Enemies",205,385);
        g2.drawString(""+totalEnemy +"/" +enemyKilled,320,385);


        g2.drawImage(star,180,200,100,100,null);
        g2.drawImage(star,270,180,100,100,null);
        g2.drawImage(star,355,200,100,100,null);
    }


    public int getTotalEnemy() {
        return totalEnemy;
    }

    public void setTotalEnemy(int totalEnemy) {
        this.totalEnemy = totalEnemy;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getEnemyKilled() {
        return enemyKilled;
    }

    public void setEnemyKilled(int enemyKilled) {
        this.enemyKilled = enemyKilled;
    }

    public int getMoneyCollected() {
        return moneyCollected;
    }

    public void setMoneyCollected(int moneyCollected) {
        this.moneyCollected = moneyCollected;
    }
}
