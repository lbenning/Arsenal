/**
 * An implementation of the median of medians algorithm, which can
 * find the kth largest element in an unsorted array of size n in
 * worst case O(n) time.
 * 
 * Implemented by Luke Benning
 * Last Updated : August 18th, 2014
 */
public class Medians {

	private Medians() {}
	
	/**
	 * Sample tester
	 */ 
	public static void main(String[] args) {
		int[] tst = {-5,5,4,-4,3,-3,2,-2,1,-1,0};
		for (int x = 0; x < tst.length; x++) {
			int y = medianOfMedians(tst,x);
			System.out.println("The " + x + " elmt. is " + y);
		}
	}
	
	/**
	 * Initiates algorithm, checks edge cases.
	 * @return The targ largest elmt. in input.
	 */
	public static int medianOfMedians(int[] input, int targ) {
		if (input == null || input.length == 0 || targ <
		0 || targ >= input.length) {
			return -1;
		}
		if (input.length == 1) { return input[0]; }
		return findValue(input, targ, 0, input.length-1);
	}

	/**
	 * @return The target largest element in input
	 */ 
	private static int findValue(int[] input, int target, int low, int high) {
		int pivot = selectPivot(input, low, high);
		int bound = partition(input, low, high, pivot);
		if (bound == target) { return input[target]; }
		else if (bound < target) {
			return findValue(input, target, bound+1, high);
		}
		return findValue(input, target, low, bound-1);
	}
	
	/**
	 * Computes a pivot guaranteed to be between the 30th and 70th percentiles
	 * of the sorted input between low and high, inclusive of both ends
	 * @return A good pivot
	 */ 
	private static int selectPivot(int[] input, int low, int high) {
		int[] medians = new int[(high-low)/5+1];
		int y = low;
		int p = 0;
		while (y+4 <= high) {
			medians[p++] = fifthMedian(input, y, y+4);
			y += 5;
		}
		if (y <= high) {
			medians[p] = fracMedian(input, y, high);
		}
		if (medians.length == 1) { return medians[0]; }
		return medianOfMedians(medians, medians.length/2);
	}

	/**
	 * Partitions the input array region around pivot between low and 
	 * high, inclusive of both ends
	 * @return The index of pivot in input after partitioning
	 */
	private static int partition(int[] input, int low, int high, int pivot) {
		int l = low;
		int m = low;
		int h = high;
		while (m <= h) {
			if (input[m] < pivot) { swap(input, l++, m++); }
			else if (input[m] == pivot) { m++; }
			else { swap(input,m,h--); }
		}
		return l;
	}

	/**
	 * Finds the median of 5 numbers using at most 6 comparisons.
	 * @return The median of the region in input between low & high
	 */
	private static int fifthMedian(int[] input, int low, int high) {
		if (input[low] > input[low+1]) { swap(input,low,low+1); }
		if (input[high] < input[high-1]) { swap(input,high,high-1); }
		if (input[low] < input[high-1]) {
			swap(input,low,high-1);
			swap(input,low+1,high);
		}
		if (input[low+1] < input[low+2]) {
			if (input[low+1] < input[high-1]) {
				return min(input[low+2],input[high-1]);
			}
			return min(input[low+1],input[high]);
		}
		if (input[high-1] < input[low+2]) { 
			return min(input[low+2],input[high]); 
		}
		return min(input[low+1],input[high-1]);
	}
	
	/**
	 * Finds the median of 4 or less numbers, used to handle the last
	 * region in the partition range. Since range size is not necessarily
	 * a multiple of 5, the end region might be smaller than 5. Moreover if the
	 * size is even we pick one middle number, since partition requires
	 * a value actually in the input.
	 * @return The median of the region in input between low & high
	 */
	private static int fracMedian(int[] input, int low, int high) {
		if (high-low < 2) { return input[low]; }
		if ((input[low]-input[low+1])*(input[high]-input[low]) >= 0) { return input[low]; }
		if ((input[low+1]-input[low])*(input[high]-input[low+1]) >= 0) { return input[low+1]; }
		return input[high];
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

	/**
	 * @return Min of x & y
	 */
	private static int min(int x, int y) {
		if (x < y) { return x; }
		return y;
	}

}
