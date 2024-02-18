/* Gjin Dhomi - 300291100
 * Nick Gingras - 300291269
 */

import java.util.*;
import java.io.File;
import java.io.IOException;

public class SimilaritySearch {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java SimilaritySearch <image_filename> <image_dataset_directory>");
            return;
        }
        
        String imageFilename = args[0];
        String imageDatasetDir = args[1];

        // Check if query image file exists, if so, initalize a variable for it 
        File queryImageFile = new File("queryImages", imageFilename);
        if (!queryImageFile.exists() || queryImageFile.isDirectory()) {
            System.out.println("Query image file does not exist.");
            return;
        }

        // Compute the histogram of the query image 
        ColorImage queryImage = new ColorImage(queryImageFile.getAbsolutePath());
        ColorHistogram queryHistogram = new ColorHistogram(3);
        queryHistogram.setImage(queryImage);

        // Load the pre-computed histograms of the image dataset 
        File datasetDir = new File(imageDatasetDir);
        File[] datasetFiles = datasetDir.listFiles();

        // Use a priority queue to keep track of the 5 most similar images 
        PriortyQueue<Pair> mostSimilarImages = new PriortyQueue<>(5, Comparator.comparingDouble(Pair::getSimilairty));

        for (File file : datasetFiles) {
            ColorHistogram datasetHistogram = new ColorHistogram(file.getAbsolutePath());

            // Compare the histogram of the query image with the histogram of the dataset image
            double similarity = queryHistogram.compare(datasetHistogram);

            // If the priority queue is full and the current similarity is greater than the smallest similarity in the queue 
            if (mostSimilarImages.size() == 5 && similarity > mostSimilarImages.peek().getSimilarity()) {
                mostSimilarImages.poll(); // Remove the image with the smallest similarity
            }

            if (mostSimilarImages.size() < 5) {
                mostSimilarImages.add(new Pair(file.getName(), similarity)); // Add the current image to the queue 
            }
        }

        // Print the names of the 5 most similar images 
        while (!mostSimilarImages.isEmpty()) {
            Pair pair = mostSimilarImages.poll();
            System.out.println(pair.getImageName() + ": " + pair.getSimilarity());
        }
    }

    private static class Pair {
        private final String imageName;
        private final double similarity;

        public Pair(String imageName, double similarity) {
            this.imageName = imageName;
            this.similarity = similarity;
        }

        public String getImageName() {
            return imageName;
        }

        public double getSimilarity() {
            return similarity
        }
    }
}
