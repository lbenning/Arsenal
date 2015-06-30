/**
* Implementation of the Union-Find data structure
* For a Union Find object of size x, x > 0, items
* are indexed from 0 to x-1
*/
public class UnionFind {

  private final int[] ptrs;
  private final boolean[] isRoots;
  private final int[] sizes;
  private int components;
	
	// makes a new union find set with all elements
	// by default in their own component
	private UnionFind(int n) {
    ptrs = new int[n];
    isRoots = new boolean[n];
    sizes = new int[n];
    for (int x = 0; x < ptrs.length; x++) {
      ptrs[x] = x;
      sizes[x] = x;
      isRoots[x] = true;
    }
    components = n;
	}

  // static factory method - only construct a
  // Union Find object if the size is valid
	public static UnionFind makeSet(int n) {
		if (n > 0) {
      UnionFind set = new UnionFind(n);
      return set;
    }
    return null;
	}

  // among two trees of x and y, merges the smaller
  // tree into the larger tree
	public void union(int x, int y) {
     if (x >= 0 && x < ptrs.length && y >= 0 &&
      y < ptrs.length && find(x) != find(y)) {
        // merge the roots
        while (!isRoots[x]) {
        	x = ptrs[x];
        }
        while (!isRoots[y]) {
        	y = ptrs[y];
        }
        if (sizes[x] >= sizes[y]) {
          ptrs[y] = x;
          isRoots[y] = false;
          sizes[x] += sizes[y];
        }
        else {
          ptrs[x] = y;
          isRoots[x] = false;
          sizes[y] += sizes[x];
        }
        // every union between two distinct components decrements
        // the total number of components by one
        components--;
      }
	}

  // returns root label of element x
	public int find(int x) {
    if (x >= 0 && x < ptrs.length) {
      if (isRoots[x]) {
      	return x;
      }
      ptrs[x] = find(ptrs[x]);
      return ptrs[x];
    }
    return -1;
	}

	// return the number of connected components
	public int getComponentCount() {
	  return this.components;
	}

}