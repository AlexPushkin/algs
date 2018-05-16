<?php
declare(strict_types=1);

class WeightedQuickUnionPathCompressionUF
{
    /**
     * @var int[]
     */
    private $parent;
    /**
     * @var int[]
     */
    private $size;

    /**
     * @var int
     */
    private $count;

    public function __construct(int $n)
    {
        $this->count = $n;

        foreach (range(0, $this->count) as $item) {
            $this->parent[$item] = $item;
            $this->size[$item] = 1;
        }
    }

    /**
     * @param int $p
     * @param int $q
     * @return bool
     * @throws InvalidArgumentException
     */
    public function connected(int $p, int $q): bool
    {
        $this->checkInput($p);
        $this->checkInput($q);

        return $this->find($p) === $this->find($q);
    }

    public function count(): int
    {
        return $this->count;
    }

    /**
     * @param int $p
     * @return int
     * @throws InvalidArgumentException
     */
    public function find(int $p): int
    {
        $this->checkInput($p);
        $root = $p;

        while ($root !== $this->parent[$root]) {
            $root = $this->parent[$root];
        }

        while ($p !== $root) {
            $tempP = $this->parent[$p];
            $this->parent[$p] = $root;
            $p = $tempP;
        }

        return $p;
    }

    /**
     * @param int $p
     * @param int $q
     * @throws InvalidArgumentException
     */
    public function union(int $p, int $q): void
    {
        $this->checkInput($p);
        $this->checkInput($q);

        $rootP = $this->find($p);
        $rootQ = $this->find($q);
        if ($rootP === $rootQ) {
            return;
        }

        if ($this->size[$rootP] < $this->size[$rootQ]) {
            $this->parent[$rootP] = $rootQ;
            $this->size[$rootQ] += $this->size[$rootP];
        } else {
            $this->parent[$rootQ] = $rootP;
            $this->size[$rootP] += $this->size[$rootQ];
        }

        $this->count--;
    }

    /**
     * @param int $n
     * @throws InvalidArgumentException
     */
    private function checkInput(int $n): void
    {
        if ($n < 0 || $n >= \count($this->parent)) {
            throw new InvalidArgumentException('Index out of range');
        }
    }
}