import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private double[] thresholds;
    private int t;
    private double confidence95 = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException("n and trial must be greater than zero");
        }
        t = trials;
        thresholds = new double[t];

        for (int i = 0; i < t; i++) {
            Percolation perc = new Percolation(n);
            int randX = StdRandom.uniformInt(1, n + 1);
            int randY = StdRandom.uniformInt(1, n + 1);

            while (!perc.percolates()) {
                while (perc.isOpen(randX, randY)) {
                    randX = StdRandom.uniformInt(1, n + 1);
                    randY = StdRandom.uniformInt(1, n + 1);
                }
                perc.open(randX, randY);
            }
            double area = n * n;
            double openSites = perc.numberOfOpenSites();
            thresholds[i] = openSites / area;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return StdStats.mean(thresholds) - (confidence95 / Math.sqrt(t));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return StdStats.mean(thresholds) + (confidence95 / Math.sqrt(t));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, t);

        StdOut.println(percStats.mean());
        StdOut.println(percStats.stddev());
        StdOut.println(percStats.confidenceHi());
        StdOut.println(percStats.confidenceLo());

    }

}
