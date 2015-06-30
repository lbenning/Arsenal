/**
* Implementation of the Fenwick Tree data structure, also
* known as Binary Indexed Tree
*/
public class FenwickTree {

	private final int[] tree;

  // initialize tree values
	private FenwickTree(int size) {
		tree = new int[size+1];
	}

  // build a Fenwick tree where sequence is all zeros
	private static FenwickTree buildClearTree(int size) {
    if (size > 0) {
      FenwickTree tree = new FenwickTree(size);
      return tree;
    }
    return null;
	}

  // build a Fenwick tree from an initial array of integers
	private static FenwickTree buildBaseTree(int[] sequence) {
		if (sequence != null && sequence.length > 0) {
			FenwickTree tree = new FenwickTree(sequence.length);
			for (int x = 0; x < sequence.length; x++) {
        tree.updateValue(x,sequence[x]);
			}
			return tree;
		}
		return null;
	}

  // update all tree indices that cover the given index
  // with the given value
	private void update(int index, int value) {
		index += 1
    while (index < tree.length) {
    	tree[index] += value;
    	index += (index & -index)
    }
	}

  // return the prefix sum up to index in the sequence
	private int sum(int index) {
    int sum = 0;
    index += 1;
    while (index >= 0) {
    	sum += tree[index];
    	index -= (index & -index);
    }
    return sum;
	}

  
	private int rangeSum(int start, int end) {
    
	}


}