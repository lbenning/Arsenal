/*
 * Class containing generic versions of the two popular sorting algorithms.
 * These versions return the index of the item being searched for, and -1 if 
 * not found.
 */
public class Searching {

	public static <T extends Comparable<T>> int linearSearch(T[] input, T item) {
		if (input == null) { return -1; }
		int s = -1;
		for (int a = 0; a < input.length; a++) {
			if (input[a].equals(item)) { s = a; break; }
		}
		return s;
	}
	
	public static <T extends Comparable<T>> int binarySearch(T[] input, T item) {
		if (input == null) { return -1; }
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
}
