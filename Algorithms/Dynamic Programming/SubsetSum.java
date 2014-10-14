public class SubsetSum {
	
	private SubsetSum() {}
	
	public static void main(String[] args) {
		int[] input = {1,2,3,4,5,6,7,8,9,10};
		System.out.println(subsetSum(input,55));
		System.out.println(subsetSum(input,56));
	}

	/**
	* Decision version of subset sum.
	*/
	public static boolean subsetSum(int[] input, int target) {
		if (target < 0 || input == null || input.length == 0) {
			return false;
		}

		boolean[] memo = new boolean[target+1];
		memo[0] = true;
		int high = 0;

		for (int x = 0; x < input.length; x++) {
			high += input[x];
			int bound = min(high,target);
		    for (int j = bound; j >= input[x]; j--) {
			  memo[j] = memo[j] || memo[j-input[x]];
		    }
		}

		return memo[target];

	}

	private static int min(int x, int y) {
		if (x < y) { return x; }
		return y;
	}
	
}
