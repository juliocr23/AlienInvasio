/*
     TODO Create aliens enemies, flies and Boss
     TODO If player falls he lose one third of his health, once it reach zero is game over.
     TODO when the player falls resetAnimation everything.
     TODO when the bullet hits the enemy, the bullet will disappear and an explosition animation should appear
     TODO when the player collides with the enemy the game is resetAnimation.

     Priority:
     Make the enemy walk forward and turn left if it
     is colliding with a tile or it is at the edge of
     the tile is walking on.
 */
package app.state;
import app.sprites.Player;
import app.sprites.World;

import java.awt.*;

public class GameState extends State {

    private Player player;
    private double playerVxi  = 1;                        //Player initial velocity in the x direction
    private double playerVyi  = 0;                        //Player initial velocity in the y direction

    private double playerVxf  = 4;                        //Player final   velocity in the x direction
    private double playerVyf  = 7;                        //Player final   velocity in the y direction

    private double playerAx   = 0.05;                     //Player acceleration in the x direction
    private double playerAy   = 0.4;                      //Player  acceleration in the y direction

    private int    playerXPos = 100;
    private int    playerYPos = 347;

    private String BGFile = "Resources\\TileMap1\\BG";    //The image files of the background
    private int BGLength = 7;                             //The number of images for background

    private World map;                                    //The tileMap of the game
    private String mapTxt ="Maps/map1.txt";               //The text file that contains the outline of  map
    private String mapImgs ="Resources/TileMap1/Tile";    //The image files of the map

    public GameState(){

        //Player
        player = new Player(playerXPos,playerYPos,64,100,playerVxi,playerVyi,playerAx,playerAy);
        player.setVxf(playerVxf);
        player.setVyf(playerVyf);

        //Tile map
        map = new World(mapTxt,mapImgs);
        map.setPlayer(player);
        map.setBackGround(BGFile,BGLength,"Resources/Sound/environment.wav");
    }

    @Override
    public void update() {

       player.update();

        //If player fall of the map take a third of his life off.
        /*if(player.playerOffScreen() || timer == 0 || World.isCollidingWithEnemy()){
            player.setX(playerXPos);
            player.setY(playerYPos);
            map.setX(0);
            map.setY(0);
            timer = 200;
        }

        //Game is Over
        if(player.getHealth() == 1){
            State.setState(null);
        }*/

        map.update();
    }

    @Override
    public void processInput() {
        player.getInput();
    }

    @Override
    public void draw(Graphics g) {
        map.draw(g);
        player.draw(g);
    }

   // public void getInput(){
     //   player.getInput();
   // }
}
