import java.util.Arrays;
import java.util.PriorityQueue;

/*
 * Class for providing implementations of sorting algorithms.
 * 
 * Complexity Overview:
 * insertionSort : O(n^2) expected time, O(n) best time. O(1) space.
 * selectionSort : O(n^2) all cases time. O(1) space.
 * gnomeSort : O(n^2) expected time, O(n) best time. O(1) space.
 * bubbleSort : O(n^2) expected time, O(n) best time. O(1) space.
 * quickSort : O(nlogn) expected/best time, O(n^2) worst time. O(1) space.
 * mergeSort : O(nlogn) expected/best/worst time, approx. O(n) space.
 * heapSort : O(nlogn) expected/best/worst time, approx. O(n) space.
 * combSort : O(n^2) expected time, O(n) best time. O(1) space.
 * introSort : O(nlogn) time, O(logn) space. 
 * special_bin : O(n) time. O(n) space. Returns new array.
 * 
 * Standards:
 * Sorting algorithms operate on input array between low and high bounds,
 * inclusive of low but exclusive of high. 
 * Implementations are generic to allow for flexibility.
 * 
 * There are no restrictions on using this class in your projects.
 * 
 * Author : Luke Benning
 * Last Edited : 12/22/2013
 */
public class Sorting {
	
	/*
	 * Insertion Sort algorithm.
	 * Edits the input array.
	 * Very fast if input nearly sorted.
	 * Time Complexity: O(n^2) expected, O(n) best.
	 * Space Complexity: O(1).
	 */
	public static <T extends Comparable<T>> void insertionSort(T[] input, int low, int high) {
		if (input == null || input.length == 0 || high - low <= 0 ||
				low < 0 || high > input.length) { return; }
		for (int j = low; j < high; j++) {
			for (int i = j; i > low; i--) {
				if (input[i].compareTo(input[i-1]) < 0) {
					swap(input,i,i-1);
				}
			}
		}
	}
	
	/*
	 * Selection Sort algorithm.
	 * Edits the input array.
	 * Time Complexity: O(n^2) expected, O(n^2) best.
	 * Space Complexity: O(1).
	 */
	public static <T extends Comparable<T>> void selectionSort(T[] input, int low, int high) {
		if (input == null || input.length == 0 || high - low <= 0 ||
				low < 0 || high > input.length) { return; }
		for (int j = 0; j < high-1; j++) {
			int min_index = j;
			for (int i = j; i < high; i++) {
				if (input[i].compareTo(input[min_index]) < 0) {
					min_index = i;
				}
			}
			if (j != min_index) {
			  swap(input,j,min_index);
			}
		}
	}
	
	/*
	 * Gnome sort implementation without teleportation optimization
	 * (no index stored where swapping begins like insertion sort).
	 * Time Complexity: O(n^2) expected, O(n) best.
	 * Space Complexity: O(1).
	 */
	public static <T extends Comparable<T>> void gnomeSort(T[] input,int low, int high) {
		if (input == null || input.length == 0 || high - low <= 0 ||
				low < 0 || high > input.length) { return; }
		int k = low+1;
		while (k < high) {
			if (input[k].compareTo(input[k-1]) < 0) {
				swap(input,k,k-1);
				if (k > low+1) { k--; }
			}
			else {
			  k++;
			}
		}
	}
	
	/* Bubble sort implementation with optimization to avoid passing
	 * through sorted partitions. Edits the input array.
	 * Time Complexity: O(n^2) expected, O(n) best.
	 * Space Complexity: O(1).
	 */
	public static <T extends Comparable<T>> void bubbleSort(T[] input, int low, int high) {
		if (input == null || input.length == 0 || high - low <= 0 ||
				low < 0 || high > input.length) { return; }
		int t = high;
		while (t > low) {
			// It is used, but to remove warnings must suppress.
			@SuppressWarnings("unused") 
			boolean swapcheck = false;
			int r = 0;
			for (int k = low+1; k < t; k++) {
		      if (input[k].compareTo(input[k-1]) < 0) {
		    	  swapcheck = true;
		    	  swap(input,k,k-1);
		    	  r = k;
		      }
			}
			t = r;
		}
	}
	
	/*
	 * Quicksort implementation with optimization to use insertion sort when
	 * the sub arrays get to a length less than or equal to 10.
	 * Very fast in practice.
	 * Time Complexity: O(nlogn) expected & best, O(n^2) worst.
	 * Space Complexity: O(1).
	 */
	public static <T extends Comparable<T>> void quickSort(T[] input,int low,int high) {
		if (input == null || input.length == 0 || high-low <= 1 ||
				low < 0 || high > input.length) { return; }
		if (high - low <= 10) { 
			insertionSort(input,low,high);
		}
		else {
			int k = partition(input,low,high);		
			quickSort(input,low,k-1);
			quickSort(input,k,high); }
	} 
	
	/*
	 * Mergesort implementation. Edits the input array.
	 * Time Complexity: O(nlogn) best, expected & worst cases.
	 * Space Complexity : O(n) expected.
	*/ 
	public static <T extends Comparable<T>> void mergeSort(T[] input, int low, int high) {
		if (input == null || input.length <= 0 || high-low <= 1 ||
				low < 0 || high > input.length) { return; }
		T[] sorted = rangeMerge(Arrays.copyOfRange(input, low, high));
		System.arraycopy(sorted, 0, input, low, sorted.length);
	} 
	
	/*
	 * Heapsort implementation. Edits the input array.
	 * Time Complexity: O(nlogn) best, expected & worst cases.
	 * Space Complexity : O(n) expected.
	*/ 
	public static <T extends Comparable<T>> void heapSort(T[] input, int low, int high) {
		if (input == null || input.length <= 0 || high-low <= 1 ||
				low < 0 || high > input.length) { return; }
		PriorityQueue<T> store = new PriorityQueue<T>();
		for (int i = low; i < high; i++) {
			store.add(input[i]);
		}
		for (int i = low; i < high; i++) {
			input[i] = store.poll();
		}
	}
	
	/*
	 * Combsort Implementation.
	 * Improves upon bubble sort.
	 * Edits the input array.
	 * Time Complexity: O(n^2) expected , Ω(n^2) worst, O(n) best.
	 * Space Complexity: O(1).
	 */
	public static <T extends Comparable<T>> void combSort(T[] input, int low, int high) {
		if (input == null || input.length <= 0 || high-low <= 1 ||
				low < 0 || high > input.length) { return; }
		int gap_size = input.length;
		boolean swapped = true;
		while (gap_size > 1 || swapped == true) {
			gap_size = (int)(gap_size / 1.3);
			if (gap_size < 1) { gap_size = 1; }
			swapped = false;
			for (int x = 0; x + gap_size < input.length; x++) {
				if (input[x+gap_size].compareTo(input[x]) < 0) {
					swap(input,x,x+gap_size);
					swapped = true;
				}
			}
		}
	}
	
	/*
	 * Introsort implementation.
	 * Hybrid of quicksort and heapsort.
	 * Edits the input array.
	 * Time Complexity: O(nlogn) all cases.
	 * Space Complexity : O(logn).
	 */
	public static <T extends Comparable<T>> void introSort(T[] input, int low, int high) {
		if (input == null || input.length <= 0 || high-low <= 1 ||
				low < 0 || high > input.length) { return; }
		int depth = (int)(2.0 * (Math.log(input.length) / Math.log(2)));
		introRunner(input,low,high,depth);
	}
	
	//******************************Special Algorithms****************************************//
	
	/*
	 * Integer version for binsort. Can sort an array of integers
	 * in O(n) time & O(n) space provided the 
	 * integers form a unit sequence between
	 * 2 known integers inclusive (lower and upper bounds). If this
	 * knowledge is not known, do not use this method. 
	 * Ex : 694578 is bounded between 4 & 9. Becomes 456789.
	 */
	public static int[] special_bin(int[] input, int lower) {
		if (input == null) { return input;}
		int[] edit = new int[input.length];
		for (int j = 0; j < input.length; j++) {
			int k = input[j];
			edit[k-lower] = k;
		}
		return edit;
	}
	
	
	//******************************Helper Methods****************************************//
	
	
	/*
	 * Partitions the input array around its first, middle and last elements.
	*/ 
	private static <T extends Comparable<T>> int partition(T[] input,int low,int high) {
		if (input == null || input.length == 0 || high - low <= 1 ||
		low < 0 || high > input.length) { return 0; }
		swap(input,findMedian(input,low,high-1),high-1); // Put median at end of array
		T median = input[high-1];
		int divider = low;
		for (int k = low; k < high; k++) {
		  if (input[k].compareTo(median) <= 0)  {
			  swap(input,k,divider);
			  divider++;
		  }
		}
		swap(input,divider,high-1);
		return divider;
	} 
	
	
	
	/*
	 * Finds the index of the median of the first,
	 * middle and last elements of the input array.
	 */
	private static <T extends Comparable<T>> int findMedian(T[] input,int low,int high) {
		if (input == null) { return 0; }
		else if (high - low <= 1) { return low; }
		else {
			T a = input[low];
			T b = input[(high+low)/2];
			T c = input[high];
			if (a.compareTo(b) > 0) {
				if (b.compareTo(c) > 0) { return (high+low)/2; }
				else if (c.compareTo(a) > 0) { return low;}
				else {return high;}
			}
			else {
				if (a.compareTo(c) > 0) { return low; }
				else if (b.compareTo(c) > 0) { return high; }
				else {return (high+low)/2; }
			}
		}
	}
	
	/*
	 * Applies mergeSort algorithm to the input array and returns the 
	 * sorted array.
	 */
	private static <T extends Comparable<T>> T[] rangeMerge(T[] input) {
		if (input.length <= 1) { return input; }
		T[] left = Arrays.copyOfRange(input, 0, (input.length)/2);
		T[] right = Arrays.copyOfRange(input,(input.length)/2,input.length);
		left = rangeMerge(left);
		right = rangeMerge(right);
		return merge(left,right);	
	}
	
	/*
	 * Merges the two input arrays into a single sorted array and returns it.
	 * Preinvariant: input arrays must be sorted.
	 */
	private static <T extends Comparable<T>> T[] merge(T[] left, T[] right) {
		if (left == null && right == null) { return null; }
		else if (left == null) { return right; }
		else if (right == null) { return left; }	
		T[] merger = Arrays.copyOf(left,left.length + right.length);
		System.arraycopy(right, 0, merger, left.length, right.length);
		int lowCurr = 0;
		int highCurr = 0;
		while (left.length > lowCurr || right.length > highCurr) {
			if (left.length > lowCurr && right.length > highCurr) {
				if (left[lowCurr].compareTo(right[highCurr]) > 0) {
					merger[lowCurr+highCurr] = right[highCurr];
					highCurr++;
				}
				else {
					merger[lowCurr+highCurr] = left[lowCurr];
					lowCurr++;
				}
			}
			else if (left.length > lowCurr) {
				System.arraycopy(left, lowCurr, merger, 
						lowCurr+highCurr, left.length - lowCurr);
				lowCurr = left.length;
			}
			else {
				System.arraycopy(right, highCurr, merger, 
						lowCurr+highCurr, right.length - highCurr);
				highCurr = right.length;
			}
		}
		return merger;
	} 
	
	/*
	 * Helper function for introsort. Switches over to heapsort when depth = 0.
	 */
	private static <T extends Comparable<T>> void introRunner(T[] input,int low,int high, int depth) {
		if (input == null || input.length == 0 || high-low <= 1 ||
				low < 0 || high > input.length) { return; }		
		if (depth <= 0) { 
			heapSort(input,low,high);
		} 
		else {
			int k = partition(input,low,high);
			introRunner(input,low,k-1,depth-1);
			introRunner(input,k,high,depth-1); }
	}
	
	/*
	 * Swaps two elements in the array at the given indices.
	 */
	private static <T extends Comparable<T>> void swap(T[] input, int indexa, int indexb) {
		if (input == null || input.length == 1 || indexa == indexb) { return; }
		T temp = input[indexa];
		input[indexa] = input[indexb];
		input[indexb] = temp;
	}

}
