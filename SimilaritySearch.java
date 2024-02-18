/* Gjin Dhomi - 300291100
 * Nick Gingras - 300291269
 */

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimilaritySearch {
    private static final Logger LOGGER = Logger.getLogger(SimilaritySearch.class.getName());

    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                throw new IllegalArgumentException("Usage: java SimilaritySearch <image_filename> <image_dataset_directory>");
            }
            
            String imageFilename = args[0];
            String imageDatasetDir = args[1];

            // Check if query image file exists, if so, initialize a variable for it
            File queryImageFile = new File("queryImages", imageFilename);
            if (!queryImageFile.exists() || queryImageFile.isDirectory()) {
                throw new IOException("Query image file does not exist.");
            }
	
            // Compute the histogram of the query image
            ColorImage queryImage = new ColorImage(queryImageFile.getAbsolutePath());
            ColorHistogram queryHistogram = new ColorHistogram(3);
            queryHistogram.setImage(queryImage);
	
            // Print the depth of the query image 
            int depth = queryImage.getDepth();
            System.out.println("The depth of the query image is: " + depth);
	
            // Load the pre-computed histograms of the image dataset 
            File[] datasetFiles = getDatasetFiles(imageDatasetDir);

            // Use a priority queue to keep track of the 5 most similar images 
            PriorityQueue<Pair> mostSimilarImages = new PriorityQueue<>(5, Comparator.comparingDouble(Pair::similarity));
	
            for (File file : datasetFiles) {
                ColorHistogram datasetHistogram = new ColorHistogram(file.getAbsolutePath());

                // Print the histogram of the dataset image for debugging
//                System.out.println("Histogram of " + file.getName() + ": " + Arrays.toString(datasetHistogram.getHistogram()));

                // Compare the histogram of the query image with the histogram of the dataset image
                double similarity = queryHistogram.compare(datasetHistogram);

                // Print the similarity score for debugging
//                System.out.println("Similarity with " + file.getName() + ": " + similarity);

                // If the priority queue is full and the current similarity is greater than the smallest similarity in the queue 
                if (mostSimilarImages.size() == 5 && similarity > mostSimilarImages.peek().similarity()) {
                  	mostSimilarImages.poll(); // Remove the image with the smallest similarity
                }
	
                if (mostSimilarImages.size() < 5) {
                  	mostSimilarImages.add(new Pair(file.getName(), similarity)); // Add the current image to the queue 
                }
            }
	
            // Print the names of the 5 most similar images 
            while (!mostSimilarImages.isEmpty()) {
                Pair pair = mostSimilarImages.poll();
                System.out.println(pair.imageName() + ": " + pair.similarity());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred: ", e);
        }
    }

    private static File[] getDatasetFiles(String imageDatasetDir) throws IOException {
        File datasetDir = new File(imageDatasetDir);
        if (!datasetDir.exists() || !datasetDir.isDirectory()) {
            throw new IOException("Dataset directory does not exist or is not a directory.");
        }

        File[] datasetFiles = datasetDir.listFiles((dir, name) -> name.endsWith(".txt"));
        if (datasetFiles == null || datasetFiles.length == 0) {
            throw new IOException("No .txt files found in the dataset directory.");
        }

        return datasetFiles;
    }

    private record Pair(String imageName, double similarity) { }
}
