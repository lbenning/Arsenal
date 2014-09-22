/**
* Decision version of subset sum.
*
* Returns whether or not there exists a subset of the
* input that sums to target.
*/
public boolean subsetSum(int[] input, int target) {
	if (target < 0 || input == null || input.length == 0) {
		return false;
	}

	boolean[] memo = new boolean[target+1];
	memo[0] = true;
	int high = 0;

	for (int x = 0; x < input.length; x++) {
		high += input[x];
		bound = min(high,target);
	  for (int j = bound; j >= input[x]; j--) {
	  	memo[j] = memo[j] || memo[j-input[x]];
	  }
	}

	return memo[target+1];

}

private int min(int x, int y) {
	if (x < y) { return x; }
	return y;
}