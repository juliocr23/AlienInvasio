/*
     TODO Create aliens enemies, flies and Boss
     TODO If player falls he lose one third of his health, once it reach zero is game over.
     TODO when the player falls reset everything.
     TODO when the bullet hits the enemy, the bullet will disappear and an explosition animation should appear
     TODO when the player collides with the enemy the game is reset.

     Priority:
     Make the enemy walk forward and turn left if it
     is colliding with a tile or it is at the edge of
     the tile is walking on.
 */
package app.main;
import app.sprites.Player;
import app.sprites.World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class GameState extends State {

    private Player player;
    private double playerVxi  = 1;                   //Player initial velocity in the x direction
    private double playerVyi  = 0;                   //Player initial velocity in the y direction
    private double playerVxf  = 4;                   //Player final   velocity in the x direction
    private double playerVyf  = 7;                   //Player final   velocity in the y direction
    private double playerAx   = 0.05;                //Player acceleration in the x direction
    private double playerAy   = 0.4;                 //Player  acceleration in the y direction
    private int    playerXPos = 100;
    private int    playerYPos = 347;

    private String BGFile = "Resources\\TileMap1\\BG";  //The image files of the background
    private int BGLength = 7;                        //The number of images for background

    private World map;                               //The tileMap of the game
    private String mapTxt ="Maps/map1.txt";          //The text file that contains the outline of  map
    private String mapImgs ="Resources/TileMap1/Tile";  //The image files of the map

    //GUI Components of the game
    private Image clockUI1;
    private Image clockUI2;

    private int timer = 200;                         //Timer for player to complete map
    private volatile double delta = 0;               //Delta used to calculate timer

    public GameState(){

        //Player
        player = new Player(playerXPos,playerYPos,64,100,playerVxi,playerVyi,playerAx,playerAy);
        player.setVxf(playerVxf);
        player.setVyf(playerVyf);

        //Tile map
        map = new World();
        map.setPlayer(player);
        map.loadTiles(mapTxt,mapImgs);
        map.setBackGround(BGFile,BGLength,"Resources/Sound/environment.wav");

        try {
            clockUI1 = ImageIO.read(new File("Resources/GUI/clockUI1.png"));
            clockUI2 = ImageIO.read(new File("Resources/GUI/clockUI2.png"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void update() {

       updateTimer();
       player.update();

        //If player fall of the map take a third of his life off.
        if(player.playerOffScreen() || timer == 0 || World.isCollidingWithEnemy()){
            player.setX(playerXPos);
            player.setY(playerYPos);
            map.setX(0);
            map.setY(0);
            timer = 200;
        }

        //Game is Over
        if(player.getHealth() == 1){
            State.setState(null);
        }

        map.update();
    }

    @Override
    public void draw(Graphics g) {
      //  BG.draw(g);
        map.draw(g);
        drawUI(g);
        player.draw(g);
    }

    public void getInput(){
        player.getInput();
    }

    private void drawUI(Graphics g){

        Graphics2D g2 = (Graphics2D) g;

        //Use anti-aliasing to make the text look sharper
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //Set the font and color
        g2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));
        g2.setColor(Color.WHITE);

        //Draw timer UI
        g2.drawImage(clockUI1,450,0,140,38,null);
        g2.drawImage(clockUI2,460,7,35,25,null);
        g2.drawString(""+timer,520,25);
    }

    private void updateTimer(){
        delta += 0.02;
        if(delta >= 1){
            delta = 0;
            timer--;
        }
    }
}
