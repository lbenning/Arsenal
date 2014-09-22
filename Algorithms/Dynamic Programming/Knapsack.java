public class KnapSack {

	private KnapSack() {}

	public static void main(String[] args) {
		int[] v = {1,2,3,4,5};
		int[] w = {3,3,2,5,1};
		int limit = 3;
		int target = 9;
		System.out.println(knapsack(v,w,limit,target));
	}

	/**
	* Decision version of knapsack - solved via dynamic programming.
	*/
	public static boolean knapsack(int[] values, int[] weights, int limit, int target) {
		if (weights == null || values == null || weights.length == 0 ||
			values.length != weights.length || limit <= 0) {
			return false;
		}

		// Entry [i,j] is max value achievable using items up to i
		// and staying under weight limit j.
		int[][] memo = new int[values.length][limit+1];

		for(int x = 0; x < values.length; x++) {
			for (int y = 1; y <= limit; y++) {
				int picked = 0;
				int left = 0;
				if (y - weights[x] >= 0) {
					picked += values[x];
					if (x > 0) {
						picked += memo[x-1][y-weights[x]];
					}
				}
				if (x > 0) { left += memo[x-1][y]; }
				memo[x][y] = max(picked,left);
			}
		}

		if (memo[memo.length-1][limit] >= target) { return true; }
		return false;
	}

	private static int max(int x, int y) {
		if (x > y) { return x; }
		return y;
	}

}
