public class LongIncSubseq {

	private LongIncSubseq() {}
	
	public static void main(String[] args) {
		int[] tst = {1,-5,2,11,3,7,4,-2,5,6,7,-10,8,4,9,0,10};
		int[] res = computeLongIncSubseq(tst);
		for (int x = 0; x < res.length; x++) {
			System.out.println("The " + x + "th value is " + res[x] + ".");
		}
	}

	public static int[] computeLongIncSubseq(int[] input) {
		if (input == null || input.length == 0) {
			return null;
		}

		int[] backRefs = new int[input.length];
		int[] minSeqIndices = new int[input.length+1];
		int record = 0;

		for (int x = 0; x < input.length; x++) {
			int low = 1;
			int high = record;
			while (low <= high) {
				int mid = low + (high-low)/2;
				if (input[minSeqIndices[mid]] < input[x]) {
					low = mid+1;
				}
				else {
					high = mid-1;
				}
			}
			
			backRefs[x] = minSeqIndices[low-1];

			if (record < low) {
				minSeqIndices[low] = x;
				record = low;
			}
			else if (input[minSeqIndices[low]] > input[x]) {
				minSeqIndices[low] = x;
			}
		}

		int[] sequence = new int[record];
		int k = minSeqIndices[record];
		for (int j = record-1; j >= 0; j--) {
			sequence[j] = input[k];
			k = backRefs[k];
		}

		return sequence;
	}
}
