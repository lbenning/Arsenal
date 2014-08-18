/**
 * QuickSelect algorithm implementation. Finds the kth largest element
 * in an unsorted array of size n in expected O(n) time, worst case
 * O(n^2). Warning : This algorithm is susceptible to the median of 3 killer
 * array, which will force the alg. to run in O(n^2) time.
 * 
 * Implemented by Luke Benning
 * Last Updated : August 18th, 2014
 */ 
public class Select {
	
	private Select() {}
	
	/**
	 * Tester 
	 */ 
	public static void main(String[] args) {
		int[] x = {-5,4,3,-3,-4,5,0};
		for (int y = 0; y < x.length; y++) {
			System.out.println("The y elmt. is " + quickSelect(x,y) + ".");
		}
	}
	
	/**
	 * Algorithm  entrypoint, checks edge cases & base case
	 * @return targ largest elmt. in input, targ is index
	 */ 
	public static int quickSelect(int[] input, int targ) {
		if (input == null || input.length == 0 || targ <
		0 || targ >= input.length) {
			return -1;
		}
		if (input.length == 1) { return input[0]; }
		return findValue(input, targ, 0, input.length-1);
	}
	
	/**
	 * Recursive QuickSelect
	 * @return targ largest elmt. in input, targ is index
	 */ 
	private static int findValue(int [] input, int targ, int low, int high) {
		int pivot = selectPivot(input, low, high);
		int bound = partition(input, low, high, pivot);
		if (bound == targ) { return input[targ]; }
		else if (bound < targ) {
			return findValue(input, targ, bound+1, high);
		}
		return findValue(input, targ, low, bound-1);
	}

	/**
	 * @return index of median of low, mid and high indexed numbers in input
	 */ 
	private static int selectPivot(int[] input, int low, int high) {
		int mid = (low+high)/2;
		if ((input[low]-input[mid])*(input[high]-input[low]) >= 0) { return low; }
		if ((input[mid]-input[low])*(input[high]-input[mid]) >= 0) { return mid; }
		return high;
	}
	
	/**
	 * Partitions input between low and high around elmt. at initial index pivot.
	 * @return index of pivot after partitioning
	 */ 
	private static int partition(int[] input, int low, int high, int pivot) {
		int bound = low;
		swap(input, pivot, high);
		for (int x = low; x < high; x++) {
			if (input[x] < input[high]) {
				swap(input, x, bound++);
			}
		}
		swap(input,bound,high);
		return bound;
	}

	/**
	 * Performs an in-place swap on the input array between the 
	 * 2 given indices
	 */
	private static void swap(int[] input, int i, int j) {
		if (i != j) {
			input[i] = input[i]+input[j];
			input[j] = input[i]-input[j];
			input[i] = input[i]-input[j];
		}
	}

}
