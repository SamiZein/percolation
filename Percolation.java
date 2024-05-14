import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int len;
    private int sitesOpen;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufnb;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Grid size must be greater than zero");
        ufnb = new WeightedQuickUnionUF((n * n) + 1);
        uf = new WeightedQuickUnionUF((n * n) + 2);
        len = n;
        grid = new boolean[n + 1][n + 1];
        sitesOpen = 0;
    }

    private int convertTo1D(int row, int col) {
        return ((row - 1) * len) + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isInRange(row, col))
            throw new IllegalArgumentException("Row or col argument outside of range");
        if (isOpen(row, col))
            return;
        grid[row][col] = true;
        int element = convertTo1D(row, col);
        if (row == 1) {
            uf.union(element, 0);
            ufnb.union(element, 0);
        } else if (isOpen(row - 1, col)) {
            uf.union(element, element - len);
            ufnb.union(element, element - len);
        }
        if (row == len) {
            uf.union(element, (len * len) + 1);
        } else if (isOpen(row + 1, col)) {
            uf.union(element, element + len);
            ufnb.union(element, element + len);
        }

        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(element, element - 1);
            ufnb.union(element, element - 1);
        }
        if (col < len && isOpen(row, col + 1)) {
            uf.union(element, element + 1);
            ufnb.union(element, element + 1);
        }
        sitesOpen += 1;
    }

    private boolean isInRange(int row, int col) {
        return row > 0 && row < len + 1 && col > 0 && col < len + 1;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isInRange(row, col))
            throw new IllegalArgumentException("Row or col argument outside of range");
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isInRange(row, col))
            throw new IllegalArgumentException("Row or col argument outside of range");
        return ufnb.find(0) == ufnb.find(((row - 1) * len) + col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return sitesOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(0) == uf.find((len * len) + 1);
    }
}
