/* Gjin Dhomi - 300291100
 * Nick Gingras - 300291269
 */

import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Arrays;

public class ColorImage {
    private final BufferedImage image;
    private int width;
    private int height;
    private int depth = 8;

    public ColorImage(String filename) throws IOException {
        //create image from file here
        image = ImageIO.read(new File(filename));
        if (image != null) {
            width = image.getWidth();
            height = image.getHeight();
        }

        // Print out the width and height for debugging
        System.out.println("Width: " + width + ", Height: " + height + ", Depth: " + depth);
    }

    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public int[] getPixel(int i, int j) {
        int colour = image.getRGB(i, j);

        // {(Red), (Green), (Blue)} in the range 0-255
        return new int[] {(colour >> 16) & 0xff, (colour >> 8) & 0xff, colour & 0xff};
    }

    public void reduceColor(int d) {
        int shift = 8 - d;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int[] rgb = getPixel(i, j);

                rgb[0] >>= shift;
                rgb[1] >>= shift;
                rgb[2] >>= shift;

                int newPixel = (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];

                image.setRGB(i, j, newPixel); // update the image with the reduced colours
            }
        }
        depth = d; // update depth
    }
}