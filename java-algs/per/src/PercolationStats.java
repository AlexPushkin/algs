import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    /**
     * Number of trials
     */
    private int trialsCount;
    /**
     * List of percolation thresholds from the trials
     */
    private double[] percolationThresholds;
    /**
     * Mean from the group of trials
     */
    private double mean;
    /**
     * Deviation from the group of trials
     */
    private double deviation;

    /**
     * Performs trials for requested size of matrix
     *
     * @param n      size of the matrix
     * @param trials number of trials to perform
     * @throws IllegalArgumentException when matrix size if less then zero or trials count is less the zero
     */
    public PercolationStats(int n, int trials) {
        if (n < 0) {
            throw new IllegalArgumentException("Size can not be less then zero");
        }
        trialsCount = trials;
        percolationThresholds = new double[trials];
        int hiEnd = n + 1;
        double matrixSize = n * n;

        // perform trials and save results
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(1, hiEnd), StdRandom.uniform(1, hiEnd));
            }
            percolationThresholds[i] = p.numberOfOpenSites() / matrixSize;
        }

        mean = StdStats.mean(percolationThresholds);

        deviation = StdStats.stddev(percolationThresholds);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return deviation;
    }

    public double confidenceLo() {
        return mean - 1.96 * deviation / Math.sqrt(trialsCount);
    }

    public double confidenceHi() {
        return mean + 1.96 * deviation / Math.sqrt(trialsCount);
    }

    public static void main(String[] args) {
        PercolationStats percolationStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.println("mean                    = " + percolationStats.mean());
        StdOut.println("stddev                  = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }
}
