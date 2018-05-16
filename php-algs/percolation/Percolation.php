<?php
declare(strict_types=1);

require '/var/code/WeightedQuickUnionPathCompressionUF.php';

class Percolation
{
    /** @var int */
    private $size;
    /** @var int */
    private $first;
    /** @var int */
    private $last;
    /** @var bool[] */
    private $blockStatuses = [];
    /** @var int */
    private $openSites;
    /** @var WeightedQuickUnionPathCompressionUF */
    private $blocks;

    public function __construct(int $n)
    {
        if ($n < 0) {
            throw new InvalidArgumentException('Size cannot be less than 1');
        }

        $this->size = $n;
        $this->first = 0;
        $this->last = $n * $n + 1;
        $this->openSites = 0;
        $this->blocks = new WeightedQuickUnionPathCompressionUF($n * $n + 2);

        for ($i = 1; $i <= $this->size; $i++) {
            $this->blockStatuses[$i] = true;
            $this->blockStatuses[$this->last - $i] = true;
            $this->blocks->union($this->first, $i);
            $this->blocks->union($this->last, $this->last - $i);
        }
    }

    public function open(int $row, int $col): void
    {
        if (!$this->isOpen($row, $col)) {
            $this->openSites++;

            $currentBlock = $this->xyTo1D($row, $col);
            $this->blockStatuses[$currentBlock] = true;

            $topRow = $row - 1;
            if (0 < $topRow) {
                if ($this->isOpen($topRow, $col)) {
                    $this->blocks->union($currentBlock, $this->xyTo1D($topRow, $col));
                }
            } else {
                $this->blocks->union($currentBlock, $this->first);
            }

            $leftCol = $col - 1;
            if (0 < $leftCol && $this->isOpen($row, $leftCol)) {
                $this->blocks->union($currentBlock, $this->xyTo1D($row, $leftCol));
            }

            $rightCol = $col + 1;
            if ($rightCol < $this->size && $this->isOpen($row, $rightCol)) {
                $this->blocks->union($currentBlock, $this->xyTo1D($row, $rightCol));
            }

            $bottomRow = $row + 1;
            if ($bottomRow < $this->size) {
                if ($this->isOpen($bottomRow, $col)) {
                    $this->blocks->union($currentBlock, $this->xyTo1D($bottomRow, $col));
                }
            } else {
                $this->blocks->union($currentBlock, $this->last);
            }
        }
    }

    public function isOpen(int $row, int $col): bool
    {
        $this->checkInput($row, $col);

        return !empty($this->blockStatuses[$this->xyTo1D($row, $col)]);
    }

    public function isFull(int $row, int $col): bool
    {
        $this->checkInput($row, $col);
        $blockNumber = $this->xyTo1D($row, $col);

        return !empty($this->blockStatuses[$blockNumber]) && $this->blocks->connected($this->first, $blockNumber);
    }

    public function numberOfOpenSites(): int
    {
        return $this->openSites;
    }

    public function percolates(): bool
    {
        return $this->blocks->connected($this->first, $this->last);
    }

    /**
     * @param int $row
     * @param int $col
     * @throws InvalidArgumentException
     */
    public function checkInput(int $row, int $col): void
    {
        if (!(0 < $row && $row <= $this->size && 0 < $col && $col <= $this->size)) {
            throw new InvalidArgumentException("Invalid input: row - $row; col - $col");
        }
    }

    private function xyTo1D(int $row, int $col): int
    {
        return ($row - 1) * $this->size + $col;
    }
}
