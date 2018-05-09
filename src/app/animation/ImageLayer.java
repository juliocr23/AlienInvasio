package app.animation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class ImageLayer
{
  private Image image;
  private int x;
  private int y;
  private int z;

  private int prevX;
  private int prevY;
  private int prevZ;

  private int screenWidth;
  private int screenHeight;


   public ImageLayer(String file, int x, int y, int z,int w, int h)
   {
      try {
         image = ImageIO.read(new File(file));
      }catch (Exception e){
         System.out.println(e.getMessage());
      }

      this.x = x;
      this.y = y;
      this.z = z;

      prevX = x;
      prevY = y;
      prevZ = z;

      screenHeight = h;
      screenWidth = w;

   }

   public void moveLeftBy(int dx)
   {
       x -= dx;
   }
   public void moveRightBy(int dx)
   {
      x += dx;
   }

   public void moveUpBy(int dy){ y += dy;}
   public void moveDownBy(int dy){ y-= dy;}

   public void setX(int x){this.x = x;}
   public void setY(int y){this.y = y;}

   public void draw(Graphics g) {
      for(int i = 0; i < 10; i++)
         g.drawImage(image, x/z + screenWidth*i, y,screenWidth,screenHeight, null);
   }

   public int getX(){
      return x;
   }

   public int getY(){
       return y;
   }

   public void setScreenWidth(int w){
       screenWidth = w;
   }

   public void setScreenHeight(int h){
       screenHeight = h;
   }

   public void reset(){
       x = prevX;
       y = prevY;
       z = prevZ;
   }

}