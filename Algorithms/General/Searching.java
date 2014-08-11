/*
 * Class containing various sorting algorithms.
 * These versions return the index of the item being searched for, and -1 if 
 * not found.
 */
public class Searching {

	/*
	 * Generic Linear Search.
	 * Returns the index of the target item, -1 if not found.
	 * Time Complexity : O(n)
	 */
	public static <T extends Comparable<T>> int flinearSearch(T[] input, T item) {
		if (input == null || input.length == 0) { return -1; }
		int s = -1;
		for (int a = 0; a < input.length; a++) {
			if (input[a].equals(item)) { s = a; break; }
		}
		return s;
	}
	
	/*
	 * Generic Linear Search - starts at end of array. Use if suspect
	 * target elements to be towards end rather than front.
	 * Returns the index of the target item, -1 if not found.
	 * Time Complexity : O(n)
	 */
	public static <T extends Comparable<T>> int blinearSearch(T[] input, T item) {
		if (input == null || input.length == 0) { return -1; }
		int s = -1;
		for (int a = input.length-1; a >= 0; a--) {
			if (input[a].equals(item)) { s = a; break; }
		}
		return s;
	}
	
	/*
	 * Generic Binary Search.
	 * Returns the index of the target item, -1 if not found.
	 * Precondition: Input must be sorted.
	 * Time Complexity : O(logn)
	 */
	public static <T extends Comparable<T>> int binarySearch(T[] input, T item) {
		if (input == null || input.length == 0) { return -1; }
		int low = 0;
		int high = input.length-1;
		while (low <= high) {
			int mid = (low + high) / 2;
			if (input[mid].equals(item)) { return mid; }
			else if (input[mid].compareTo(item) < 0) { low = mid+1; }
			else { high = mid-1; }
		}
		return -1;
	}
	
	
	// Special Search Algorithms //
	
	/*
	 * Jump Search implementation. 
	 * Returns the index of the target item, -1 if not found.
	 * Precondition: Input must be sorted.
	 * Time Complexity : O(sqrt(n))
	 */
	public static <T extends Comparable<T>> int jumpSearch(T[] input, T item) {
		if (input == null || input.length == 0) { return -1; }
		int alpha = 0;
		int beta = (int)Math.sqrt(input.length);
		while (beta < input.length && input[beta-1].compareTo(item) < 0 || 
				input[input.length-1].compareTo(item) < 0) {
			alpha = beta;
			beta += (int)Math.sqrt(input.length);
			if (alpha >= input.length) { return -1; }
		}
		while (input[alpha].compareTo(item) < 0) {
			alpha += 1;
			if (alpha >= beta || alpha >= input.length) {
				return -1; }
			}
		if (input[alpha].compareTo(item) == 0) {
			return alpha;
		}
		return -1;
	}
	
	/*
	 * Interpolation Search implementation for integer arrays.
	 * Returns the index of the target integer, -1 if not found.
	 * Precondition: Input must be sorted.
	 * Worst Case Time O(n), in practice faster.
	 */
	public static int interpolationSearch(int[] input, int target) {
		if (input == null || input.length == 0) { return -1; }
		int alpha = 0;
		int beta = input.length - 1;
		
		int lower = input[alpha];
		int upper = input[beta];
		
		if (target < lower || target > upper) { return -1; }
		
		while (alpha < beta) {
			int mid = (int)Math.floor((alpha + 
					(double)((beta-alpha)*(target-lower)) / ((double)(upper-lower))));
			int midtarg = input[mid];
			if (target > midtarg) {
				alpha = mid + 1;
				lower = midtarg;
			}
			else if (target < midtarg) {
				beta = mid - 1;
				upper = midtarg;
			}
			else {
				return mid;
			}		
		}
		if (target != lower) { return -1; }
		return alpha;	
	}
}
