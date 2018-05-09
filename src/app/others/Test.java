package app.others;



import java.io.IOException;

public class Test  {

    public static void main(String[] args) throws IOException {
       Sound sound = new Sound("Sound/coin.wav");
       sound.play();

      // while(true);

    }



    /*public static void flipHorizontal(String fileName) throws IOException {

        for (int i = 0; i < 32; i++) {

            BufferedImage original = ImageIO.read(new File(fileName + i + ".png"));

            //Get width and height
            int width = original.getWidth();
            int height = original.getHeight();

           //Create the copy image
            BufferedImage copy = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);


            for (int row = 0; row < height; row++) {

                for (int col = width - 1, j = 0; col >= 0; col--, j++) {

                    //J gets the original pixal value starting from the right
                    //Then the copy takes that pixal and draws it starting from the left
                    int pixel = original.getRGB(j, row);
                    copy.setRGB(col, row, pixel);
                }
            }
            ImageIO.write(copy, "png", new File("DefendingLeft_" + i + ".png"));
            System.out.println("Done copying image");
        }
    }

    public Image flipVertical(String fileName) {
        return null;
    }*/
}
