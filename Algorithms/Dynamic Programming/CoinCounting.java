/**
* Dynamic programming solutions to coin counting problem -
* both combination and permutation versions
*
*/
public class CoinCounting  {

  private CoinCounting() {}
  
  public static void main(String[] args) {
	  int[] input = {6,4,10,44,1,51};
	  int target = 5;
	  System.out.println(countCombinations(input,target));
	  System.out.println(countPermutations(input,target));
  }

  /**
  * Combinations - Count number of combinations to pick coins to
  * reach some value, duplicates not allowed.
  */
  public static int countCombinations(int[] coins, int target) {
  	if (coins == null || coins.length == 0 || target <= 0) {
  		return 0;
  	}

  	int[] memo = new int[target+1];
  	memo[0] = 1;
  	int runningSum = 0;
  	for (int c = 0; c < coins.length; c++) {
  		runningSum += coins[c];
  		for (int s = min(target,runningSum); s >= coins[c]; s--) {
  			memo[s] += memo[s-coins[c]];
  		}
  	}

  	return memo[target];
  }

  /**
  * Permutations - Count number of permutations to pick coins to
  * reach some value, duplicates allowed
  */
  public static int countPermutations(int[] coins, int target) {
  	if (coins == null || coins.length == 0 || target <= 0) {
      return 0;
    }

    int[] memo = new int[target+1];
    memo[0] = 1;
    for (int x = 1; x <= target; x++) {
      for (int c : coins) {
        if (x >= c) {
          memo[x] += memo[x-c];
        }
      }
    }
    return memo[target];
  }
  
  private static int min(int x, int y) {
		if (x < y) { return x; }
		return y;
	}
  
}
