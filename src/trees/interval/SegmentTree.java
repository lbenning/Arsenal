import java.lang.Math.*;

/**
* Segment Tree - Defined over a sequence of n elmts.
* Allows updating a value, summing a range, max over a
* range or min over a range to be computed in O(logn) time
*/
public class SegmentTree {

	private SegmentNode[] tree;
	
	private SegmentTree(double[] values) {
		int size = (int)Math.ceil(Math.log(values.length)/Math.log(2));
		tree = new SegmentNode[(int)(Math.pow(2,size+1))];
		recursiveBuilder(values,0,values.length-1,1);
	}

	/**
	* Restrict construction of segment trees to non-empty sequences only
	*/
	public static SegmentTree build(double[] values) {
		if (values == null || values.length == 0) {
			return null;
		}
		return new SegmentTree(values);
	}

	/**
	* Replace value in sequence at index with delta
	*/
	public void update(int index, double delta) {
		recursiveUpdate(index,delta,1);
	}

	/**
	* Retrieve sum of elements in sequence interval [low,high]
	*/
	public double rangeSum(int low, int high) {
		if (high < low) { return 0; }
		return recursiveRange(low,high,1);
	}

	/**
	* Retrieve max. element in sequence interval [low,high]
	*/
	public double rangeMax(int low, int high) {
		if (high < low) { return 0; }
		return recursiveStat(low,high,1,true);
	}

	/**
	* Retrieve min. element in sequence interval [low,high]
	*/
	public double rangeMin(int low, int high) {
		if (high < low) { return 0; }
		return recursiveStat(low,high,1,false);
	}

	private void recursiveBuilder(double[] values, int low, int high, int curr) {
		if (low == high) {
			tree[curr] = new SegmentNode(low,high,values[low],values[low],values[low]);
			return;
		}
		int mid = low+(high-low)/2;
		recursiveBuilder(values,low,mid,2*curr);
		recursiveBuilder(values,mid+1,high,2*curr+1);
		double max = Math.max(tree[2*curr].max,tree[2*curr+1].max);
		double min = Math.min(tree[2*curr].min,tree[2*curr+1].min);
		tree[curr] = new SegmentNode(low,high,tree[2*curr].value+tree[2*curr+1].value,max,min);
	}

	private void recursiveUpdate(int index, double delta, int curr) {
		if (tree[curr].low == index && tree[curr].high == index) {
			tree[curr].value = delta;
			tree[curr].max = tree[curr].value;
			tree[curr].min = tree[curr].value;
			return;
		}
		if (index <= tree[2*curr].high) {
			recursiveUpdate(index,delta,2*curr);
		}
		else {
			recursiveUpdate(index,delta,2*curr+1);
		}
		tree[curr].value = tree[2*curr].value + tree[2*curr+1].value;
		tree[curr].max = Math.max(tree[2*curr].max,tree[2*curr+1].max);
		tree[curr].min = Math.min(tree[2*curr].min,tree[2*curr+1].min);
	}

	private double recursiveRange(int low, int high, int curr) {
		double value = 0.0;
		if (low <= tree[curr].low && high >= tree[curr].high) {
			return tree[curr].value;
		}
		else if (low > tree[curr].high || high < tree[curr].low) {
			return value;
		}
		if (2*curr < tree.length) {
			value += recursiveRange(low,high,2*curr);
		}
		if (2*curr+1 < tree.length) {
			value += recursiveRange(low,high,2*curr+1);
		}
		return value;
	}

	private double recursiveStat(int low, int high, int curr, boolean op) {
		if (low <= tree[curr].low && high >= tree[curr].high) {
			return op ? tree[curr].max : tree[curr].min;
		}
		else if (low > tree[curr].high || high < tree[curr].low) {
			return op ? -Double.MAX_VALUE : Double.MAX_VALUE;
		}
		double left = recursiveStat(low,high,2*curr,op);
		double right = recursiveStat(low,high,2*curr+1,op);
		return op ? Math.max(left,right) : Math.min(left,right);
	}

	/**
	* Canonical unit used to construct segment tree
	*/
	private class SegmentNode {
		private int low;
		private int high;
		private double value;
		private double max;
		private double min;

		public SegmentNode(int low, int high, double value, double max, double min) {
			this.low = low;
			this.high = high;
			this.value = value;
			this.max = max;
			this.min = min;
		}
	}

}