/* Gjin Dhomi - 300291100
 * Nick Gingras - 300291269
 */

import java.io.IOException;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class ColorHistogram {
    private double[] histogram;
    private int depth;

    public ColorHistogram (int d) {
        this.depth = d;
        this.histogram = new double[(int) Math.pow(2,d*3)]; //d-bit range
    }

    public ColorHistogram (String filename) throws IOException{
        this.histogram = new double[512]; //from txt files
        this.depth = 3; //3 bits per channel
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            String[] split = line.split("\\s+");

            for (int i=1; i<split.length; i++) { //skipping the first value (512)
                this.histogram[i-1] = Double.parseDouble(split[i]);
            }
        }
    }

    public void setImage(ColorImage image) {
        image.reduceColor(depth); //reduce color from ColorImage class
        Arrays.fill(histogram, 0); //resets the histogram

        for (int i=0; i<image.getWidth(); i++) {
            for (int j=0; j<image.getHeight(); j++) {
                int[] rgb = image.getPixel(i, j); //getPixel from ColorImage class

                int index = (rgb[0] << (2 * depth)) + (rgb[1] << depth) + (rgb[2]); //position values correctly for the histogram
                histogram[index]++;
            }
        }
        
        double total = Arrays.stream(histogram).sum();
        for (int i=0; i<histogram.length; i++) {
            histogram[i] /= total;
        }
    }

    private double[] getHistogram() {
        return histogram;
    }

    public double compare(ColorHistogram hist) {
        double intersection = 0.0;

        for (int i=0; i<this.histogram.length; i++) {
            intersection += Math.min(this.histogram[i], hist.histogram[i]);
        }

        return intersection;
    }

    public void save(String filename) throws IOException{
        try (FileWriter writer = new FileWriter(filename)) {
            for(double bin : histogram) {
                writer.write(bin + " ");
            }
        }
    }

}
