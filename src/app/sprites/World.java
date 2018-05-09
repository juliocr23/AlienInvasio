package app.sprites;

import app.animation.Background;
import app.animation.Coin;
import app.main.Framework;
import app.others.DroppedItem;
import app.others.Sound;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class World {

    private static Object[][] object;                 //objects in the map
    private static int tileSize = 64;                 //The size of the tile
    private static Player player;                     //The player in the map
    private static double x;                          //The x offset of the map
    private static double y;                          //The y offset of the map
    private Background BG;                            //The background of the game
    private Sound backgroundSound;                    //Sound for the background

    public World(){
        x = 0;
        y = 0;
        tileSize = 64;
    }

    public void moveLeftBy(double dx){
        x -= dx;
    }
    public void moveRightBy(double dx){
        x += dx;
    }


    public void update(){

        for(int i = 0; i< object.length; i++) {
            for (int j = 0; j< object[0].length; j++) {
                if(object[i][j] != null) {
                    updateCoin(i,j);
                    updateEnemy(i,j,getDrawX(j),getDrawY(i));
                }
            }
        }

        if(player.isMoveR() &&
           !player.isEndOfWorld()){

            if(player.isInMiddleOfScreen() &&
              !player.isFacingRightTile()) {

                BG.moveLeftBy((int) player.getVx());
                moveLeftBy(player.getVx());
            }
        }

        if(player.isMoveL()){

            if(player.isInMiddleOfScreen() &&
              !player.isFacingLeftTile() &&
               player.isStart()) {

                BG.moveRightBy((int) player.getVx());
                moveRightBy(player.getVx());
            }
            else if(!player.isStart()){
                setX(0);
                BG.reset();
            }
        }
    }

    private void updateCoin(int x, int y){

        if (object[x][y] instanceof Coin){
            Coin coin = (Coin) object[x][y];
            coin.update();
        }
    }

    private void updateEnemy(int i, int j, int x, int y){

        if(object[i][j] instanceof  Alien){

            Alien alien =(Alien) object[i][j];
            Bullet[] bullet = player.getBullet();
            int c = player.getCounter();

            if(c >= 0 && bullet[c] != null){
                if(alien.overlap(bullet[c])) {
                    alien.dontMove();
                    alien.setToDead();

                    bullet[c].x = -1000;
                    bullet[c].y = -1000;
                }
            }

            if(alien.isDead())
                object[i][j] = null;

            alien.update(x,y);
        }
    }

    public void draw(Graphics g){

        //Draw background
        BG.draw(g);

        int startX =  getStartX();
        int endX   =  getEndX();
        int startY =  getStartY();
        int endY   =  getEndY();

        //Draw object and pick up items
        for(int i = startY; i<endY && getRow(i) < Framework.height; i++) {

            for (int j = startX ; j<endX && getCol(j)<Framework.width; j++) {

                if(object[i][j] != null) {
                    drawTiles(g,i,j);
                    drawCoin(g,i,j);
                    drawDroppedItem(g,i,j);
                }
            }
        }

        drawSprites(g);
    }

    private void drawTiles(Graphics g,int i,int j){

        if (object[i][j] instanceof Image) {

            g.drawImage((Image) object[i][j],
                         getDrawX(j), getDrawY(i),
                         null);
        }
    }


    private void drawCoin(Graphics g,int i, int j){

        if (object[i][j] instanceof Coin){

            Coin coin = (Coin) object[i][j];
            coin.draw(g,getDrawX(j),getDrawY(i));
        }
    }

    private void drawDroppedItem(Graphics g,int i, int j){

        if (object[i][j] instanceof DroppedItem){

            DroppedItem item = (DroppedItem) object[i][j];
            item.draw(g,getDrawX(j)+12,getDrawY(i));
        }
    }


    private void drawSprites(Graphics g){

        for(int i = 0; i< object.length; i++){

            for(int j = 0; j< object[0].length; j++){

                if(object[i][j] != null) {

                    if(object[i][j] instanceof Alien){

                        Alien alien = (Alien) object[i][j];

                        int offsetY = Math.abs(tileSize-alien.getHeight());

                        alien.draw(g,getDrawX(j),getDrawY(i)-offsetY);
                    }
                }
            }
        }
    }



    public static boolean isSolidTile(int x, int y){
        boolean flag = false;
        if((x >= 0 && x< object[0].length) && (y>=0 && y< object.length) ) {
            if (object[y][x] != null && object[y][x] instanceof Image)
                 flag = true;
        }
        return flag;
    }

    public static boolean isSolidCoin(int x, int y){
        boolean flag = false;
        if((x >= 0 && x< object[0].length) && (y>=0 && y< object.length) ) {
            if (object[y][x] != null && object[y][x] instanceof Coin)
                flag = true;
        }
        return flag;
    }

    public static boolean isSolidBullet(int x, int y){
        boolean flag = false;
        if((x >= 0 && x< object[0].length) && (y>=0 && y< object.length) ) {
            if (object[y][x] != null && object[y][x] instanceof DroppedItem)
                flag = true;
        }
        return flag;
    }

    public static void removeBullet(int x, int y){
        if((x >= 0 && x< object[0].length) && (y>=0 && y< object.length) ) {
            if (object[y][x] != null && object[y][x] instanceof DroppedItem)
                object[y][x] = null;
        }
    }


    public static boolean isCollidingWithEnemy(){

        boolean flag = false;
        for(int i = 0; i< object.length; i++){
            for(int j = 0; j< object[0].length; j++){
                   if(object[i][j] != null){

                       if(object[i][j] instanceof  Alien){
                           Alien alien = (Alien) object[i][j];
                           if(alien.overlap(player.getRectangle())){
                               flag = true;
                               break;
                           }
                       }
                   }
            }
        }
        return flag;
    }

    public static boolean isHittingEnemy(){

        boolean flag = false;

        for(int i = 0; i< object.length; i++){

            for(int j = 0; j< object[0].length; j++){

                if(object[i][j] != null){

                    if(object[i][j] instanceof  Alien){
                        Alien alien = (Alien) object[i][j];
                        Bullet[] bullet = player.getBullet();

                        for(int k = 0; k<bullet.length; k++){

                            if(bullet[k] != null) {
                                if (alien.overlap(bullet[k])) {
                                    flag = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return flag;
    }


    public static void removeCoin(int x, int y){
        if((x >= 0 && x< object[0].length) && (y>=0 && y< object.length) ) {
            if (object[y][x] != null && object[y][x] instanceof Coin)
                object[y][x] = null;
        }
    }

    public static double getX(){
        return x;
    }

    public void setX(double x){
        this.x = x;
    }

    public static double getY(){
        return y;
    }

    public static int getTileSize(){
        return tileSize;
    }


    /**
     * The getContext method read a file line by line ignoring #
     * and put it in an ArrayList
     * @param fileName A .txt file.
     * @return An ArrayList  containing the context read from the txt file
     */

    public ArrayList<String> getContext(String fileName){

        ArrayList<String> context = new ArrayList<>();
        File file = new File(fileName);

        try {
            Scanner read = new Scanner(file);
            String line;
            while (read.hasNext()){

                line = read.nextLine();
                if(!line.startsWith("#")){
                    context.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return context;
    }

    /**
     * The getLongestLine method find the longest line
     * in a ArrayList type string
     * @param context An ArrayList type string
     * @return The length of the longest line in the ArrayList
     */

    public int getLongestLine(ArrayList<String> context){
        int longest = 0;
        int temp;
        for(int i = 0; i<context.size(); i++){
            temp = context.get(i).length();

            if(longest < temp) longest = temp;
        }
        return longest;
    }

    public void loadTiles(String map, String imgName){

        ArrayList<String> code = getContext(map);
        int width = getLongestLine(code);
        int height = code.size();

        object = new Object[height][width];  //Create a grid to represent the world

        String line;                      //A line of code
        char  tile;                       //A tile

        for(int i = 0; i<height; i++) {

            line = code.get(i);   //Get a line of code

            for (int j = 0; j < line.length(); j++) {

                tile = line.charAt(j); //Get a tile from the code

                //If the char is A-Z then is a tile.
                if (Character.isAlphabetic(tile) && Character.isUpperCase(tile)) {
                    try {
                        //Read in the tile
                        Image img = ImageIO.read(new File(imgName + tile + ".png"));
                        object[i][j] = img;
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                //Store coins
                if(tile ==  'o'){
                    object[i][j] = new Coin();
                }

                if(tile == '*'){
                    object[i][j] = new DroppedItem();
                }

                //Store enemies
                if(tile == '1'){
                    object[i][j] = new Alien(0,0,64,100,"Resources/Enemies/BlueAlien/");
                }

                if(tile == '2'){
                    object[i][j] = new Alien(0,0,64,100,"Resources/Enemies/GreenAlien/");
                }
            }
        }
    }

    public static int getWorldMaxY(){
        return object.length;
    }

    public static int getWorldMaxX(){
        return object[0].length;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setPlayer(Player obj){
        player = obj;
    }

    private int getDrawX(int j){
        return (j * tileSize) + (int) x;
    }

    private int getDrawY(int i){
        return (i * tileSize) + (int) y;
    }

    private int getStartX(){
        return  (int)Math.max(0,Math.abs(x)/tileSize);
    }

    private int getEndX(){
        return object[0].length;
    }

    private int getStartY(){
       return  (int)Math.max(0,Math.abs(y)/tileSize);
    }

    private int getEndY(){
        return object.length;
    }

    private int getRow(int i){
        return (i*tileSize+(int)y);
    }

    private int getCol(int j){
        return (j*tileSize+(int)x);
    }

    public void setBackGround(String bgFile,int length,String soundFile){
        BG = new Background(Framework.width,Framework.height,bgFile,length);
        backgroundSound = new Sound(soundFile);
        backgroundSound.setVolume(-20.0f);
        backgroundSound.loop();
    }
}