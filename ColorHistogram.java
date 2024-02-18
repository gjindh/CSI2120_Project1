/* Gjin Dhomi - 300291100
 * Nick Gingras - 300291269
 */

import java.io.IOException;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class ColorHistogram {
    private final double[] histogram;
    private final int depth;

    public ColorHistogram(int d) {
        this.depth = d;
        this.histogram = new double[(int) Math.pow(2,d*3)]; //d-bit range
    }

    public ColorHistogram(String filename) throws IOException {
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

        for (int i=0; i < image.getWidth(); i++) {
            for (int j=0; j < image.getHeight(); j++) {
                int[] pixel = image.getPixel(i, j); //getPixel from ColorImage class
                int index = (pixel[0] << (2 * depth)) + (pixel[1] << depth) + pixel[2]; //position values correctly for the histogram
                histogram[index]++;
            }
        }

        int totalPixels = image.getWidth() * image.getHeight();
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] /= totalPixels;
        }

        // Print out the histogram values for debugging
        System.out.println(Arrays.toString(histogram));
    }

    public double[] getHistogram() {
        return histogram;
    }

    public double compare(ColorHistogram other) {
        double[] thisHistogram = this.getHistogram();
        double[] otherHistogram = other.getHistogram();

        // Print the histograms for debugging
        System.out.println("This histogram: " + Arrays.toString(thisHistogram));
        System.out.println("Other histogram: " + Arrays.toString(otherHistogram));

        double similarity = 0.0;

        for (int i = 0; i < thisHistogram.length; i++) {
            // Compute the similarity for this bin
            similarity += Math.min(thisHistogram[i], otherHistogram[i]);
        }

        return similarity;
    }

    public void save(String filename) throws IOException{
        try (FileWriter writer = new FileWriter(filename)) {
            for(double bin : histogram) {
                writer.write(bin + " ");
            }
        }
    }
}
