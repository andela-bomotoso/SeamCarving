import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private static final int BASE_ENERGY = 1000;
    private Picture picture;
    private double[][] energyArray;

    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();
        this.picture = new Picture(picture);
    }             // create a seam carver object based on the given originalPicture

    public Picture picture() {
        return new Picture(picture);
    }                        // current originalPicture

    public int width() {
        return picture.width();
    }                     // width of current originalPicture

    public int height() {
        return picture.height();

    }                     // height of current originalPicture

    public double energy(int x, int y) {
        if (!validRowCol(x, y))
            throw new IllegalArgumentException();
        if (x == 0 || y == 0 || (x == width() - 1) || (y == height() - 1))
            return BASE_ENERGY;
        else
            return computeCentralDifference(x, y);
    }           // energy of pixel at column x and row y

    public int[] findHorizontalSeam() {
        int[] horizontalSeams;
        picture = transposePicture(picture);
        horizontalSeams = findVerticalSeam();
        picture = transposePicture(picture);
        return horizontalSeams;
    }          // sequence of indices for horizontal seam

    public int[] findVerticalSeam() {
        int[] verticalSeams = new int[height()];
        energyArray = new double[height()][width()];
        for (int i = 1; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                energyArray[i][j] = energy(j, i);
            }
        }

        int[][] edgeTo = new int[height()][width()];
        double[][] distTo = new double[height()][width()];

        // initialize distTo to max_vals
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                if (row == 0)
                    distTo[row][col] = BASE_ENERGY;
                else
                    distTo[row][col] = Double.POSITIVE_INFINITY;
            }
        }

        for (int row = 0; row < height() - 1; row++) {
            for (int col = 0; col < width(); col++) {
                relax(row, col, edgeTo, distTo);
            }
        }

        // Backtrack from the last row to get a shortest path
        int minCol = 0;
        double minDist = Double.POSITIVE_INFINITY;
        for (int col = 0; col < width(); col++) {
            if (distTo[height() - 1][col] < minDist) {
                minDist = distTo[height() - 1][col];
                minCol = col;
            }
        }
        for (int row = height() - 1; row >= 0; row--) {
            verticalSeams[row] = minCol;
            minCol = minCol - edgeTo[row][minCol];
        }
        return verticalSeams;
    }         // sequence of indices for vertical seam

    private void relax(int row, int col, int[][] edgeTo, double[][] distTo) {
        int nextRow = row + 1;
        for (int i = -1; i <= 1; i++) {
            int nextCol = col + i;
            if (nextCol < 0 || nextCol >= width())
                continue;
            if (distTo[nextRow][nextCol] >= distTo[row][col] + energyArray[nextRow][nextCol]) {
                distTo[nextRow][nextCol] = distTo[row][col] + energyArray[nextRow][nextCol];
                edgeTo[nextRow][nextCol] = i;
            }
        }
    }

    public void removeHorizontalSeam(int[] seam) {
        Picture transposedPicture = transposePicture(picture);
        picture = transposedPicture;
        removeVerticalSeam(seam);
        picture = transposePicture(picture);
    } // remove horizontal seam from current originalPicture

    public void removeVerticalSeam(int[] seam) {

        Picture modifiedPicture = new Picture(picture.width() - 1, picture.height());

        int height = modifiedPicture.height();
        int width = modifiedPicture.width();

        if (seam == null || seam.length != picture.height())
            throw new IllegalArgumentException();

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= picture.width())
                throw new IllegalArgumentException();
        }
        if (isInvalidSeam(seam))
            throw new IllegalArgumentException();


        for (int row = 0; row < height; row++) {
            for (int col = 0; col < seam[row]; col++) {
                modifiedPicture.set(col, row, picture.get(col, row));
            }

            for (int col = seam[row]; col <= width - 1; col++) {
                modifiedPicture.set(col, row, picture.get(col + 1, row));
            }
        }
        this.picture = modifiedPicture;

    }  // remove vertical seam from current originalPicture

    private boolean validRowCol(int x, int y) {
        if (x < 0 || y < 0 || x > width() - 1 || y > height() - 1)
            return false;
        return true;
    }

    private double computeCentralDifference(int x, int y) {
        Color leftColor = picture.get(x - 1, y);
        Color rightColor = picture.get(x + 1, y);
        Color topColor = picture.get(x, y - 1);
        Color bottomColor = picture.get(x, y + 1);

        double redDiffH = Math.abs(leftColor.getRed() - rightColor.getRed());
        double greenDiffH = Math.abs(leftColor.getGreen() - rightColor.getGreen());
        double blueDiffH = Math.abs(leftColor.getBlue() - rightColor.getBlue());

        double redDiffV = Math.abs(topColor.getRed() - bottomColor.getRed());
        double greenDiffV = Math.abs(topColor.getGreen() - bottomColor.getGreen());
        double blueDiffV = Math.abs(topColor.getBlue() - bottomColor.getBlue());

        double valueH = Math.pow(redDiffH, 2) + Math.pow(greenDiffH, 2) + Math.pow(blueDiffH, 2);
        double valueV = Math.pow(redDiffV, 2) + Math.pow(greenDiffV, 2) + Math.pow(blueDiffV, 2);
        double valSum = valueH + valueV;

        return Math.sqrt(valSum);
    }

    private Picture transposePicture(Picture pictureToTranspose) {
        Picture transposedPicture = new Picture(pictureToTranspose.height(), pictureToTranspose.width());
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                transposedPicture.set(j, i, pictureToTranspose.get(i, j));
            }
        }
        return transposedPicture;
    }

    private boolean isInvalidSeam(int[] seam) {
        int currentSeam = seam[0];
        for (int i = 1; i < seam.length; i++) {
            if ((seam[i] != currentSeam) && (seam[i] != (currentSeam + 1)) && (seam[i] != (currentSeam - 1))) {
                return true;
            }
            currentSeam = seam[i];
        }
        return false;
    }

}
