import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    /**
     * Size of the matrix
     */
    private int size;
    /**
     * Virtual first block index
     */
    private int first;
    /**
     * Virtual last block index
     */
    private int last;
    /**
     * All blocks
     */
    private WeightedQuickUnionUF blocks;
    /**
     * Open blocks number
     */
    private int openBlocks;
    /**
     * Key value pairs of block statuses, here key is a block index and value is status of this block: true - open,
     * false - closed
     */
    private boolean[] blockStatuses;

    /**
     * Initialize block and connect necessary blocks
     *
     * @param n size of the matrix
     * @throws IllegalArgumentException when size is less then zero
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Size can not be less than 1");
        }
        size = n;
        first = 0;
        last = n * n + 1;
        openBlocks = 0;
        blockStatuses = new boolean[last + 1];

        blocks = new WeightedQuickUnionUF(n * n + 2);

        // Connect first row to first virtual block and last row to last virtual row
        for (int i = 1; i <= n; i++) {
            blocks.union(first, i);
            blocks.union(last, last - i);
        }
    }

    /**
     * Make block open if it is not open already
     *
     * @param row x coordinate
     * @param col y coordinate
     * @throws IllegalArgumentException when block coordinates are out of range
     */
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            openBlocks++;

            int currentBloc = xyTo1D(row, col);

            blockStatuses[currentBloc] = true;

            // Find top neighbour and connect to it
            int topRow = row - 1;
            if (topRow > 0) {
                if (isOpen(topRow, col)) {
                    blocks.union(currentBloc, xyTo1D(topRow, col));
                }
            } else {
                blocks.union(currentBloc, first);
            }

            // Find left neighbour and connect to it
            int leftCol = col - 1;
            if (leftCol > 0 && isOpen(row, leftCol)) {
                blocks.union(currentBloc, xyTo1D(row, leftCol));
            }

            // Find right neighbour and connect to it
            int rightCol = col + 1;
            if (rightCol <= size && isOpen(row, rightCol)) {
                blocks.union(currentBloc, xyTo1D(row, rightCol));
            }

            // Find bottom neighbour and connect to it
            int botRow = row + 1;
            if (botRow <= size) {
                if (isOpen(botRow, col)) {
                    blocks.union(currentBloc, xyTo1D(botRow, col));
                }
            } else {
                blocks.union(currentBloc, last);
            }
        }
    }

    /**
     * Returns status of the system
     *
     * @return true if first and last block are connected
     */
    public boolean percolates() {
        return blocks.connected(first, last);
    }

    /**
     * Returns fill status of the block
     *
     * @param row x coordinate
     * @param col y coordinate
     * @return true if block is connected to first row, false otherwise
     * @throws IllegalArgumentException when block coordinated are out of range
     */
    public boolean isFull(int row, int col) {
        validate(row, col);
        int blockNumber = xyTo1D(row, col);

        return blockStatuses[blockNumber] && blocks.connected(first, blockNumber);
    }

    /**
     * Check if block is open
     *
     * @param row x coordinate
     * @param col y coordinate
     * @return status of the block
     * @throws IllegalArgumentException when coordinates are out range
     */
    public boolean isOpen(int row, int col) {
        validate(row, col);

        return blockStatuses[xyTo1D(row, col)];
    }

    /**
     * Returns number if open blocks
     *
     * @return number of open blocks
     */
    public int numberOfOpenSites() {
        return openBlocks;
    }

    /**
     * Validate coordinates of the block
     *
     * @param row x coordinate
     * @param col y coordinate
     * @throws IllegalArgumentException when coordinates are out range
     */
    private void validate(int row, int col) {
        if (!((0 < row && row <= size) && (0 < col && col <= size))) {
            throw new IllegalArgumentException("Size can not be less than 1");
        }
    }

    /**
     * Transform 2D coordinates to it 1D equivalent
     *
     * @param row x coordinate
     * @param col y coordinate
     * @return index of the block
     */
    private int xyTo1D(int row, int col) {
        return (row - 1) * size + col;
    }
}
