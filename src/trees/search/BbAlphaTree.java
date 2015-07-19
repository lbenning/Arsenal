/**
* BB Alpha Tree - A weight balanced binary
* search tree that achieves amortized complexity
* of O(mlogn) for a sequence of m find,insert and delete
* operations on at most n items. Items are key,value pairs,
* duplicate keys are not allowed
*/
public class BbAlphaTree<K extends Comparable<K>, V extends Comparable<V>> implements MambaTree<K,V> {

	private BbAlphaTree<K,V> parent;
	private BbAlphaTree<K,V> left;
	private BbAlphaTree<K,V> right;

	private K key;
	private V value;

	private int weight;
	private double alpha;

	public BbAlphaTree() {
		this.alpha = 0.25;
	}
	
	public BbAlphaTree(double alpha) {
		if (alpha < 0.30 && alpha > 0.10) {
			this.alpha = alpha;
		}
	}

	private BbAlphaTree(K key, V value, BbAlphaTree<K,V> parent, int weight) {
		this.key = key;
		this.value = value;
		this.parent = parent;
		this.weight = weight;
	}

	 public void insert(K key, V value) {
		if (key == null || this.parent != null) { return; }
		if (this.key == null) {
			this.key = key;
			this.value = value;
			this.weight = 1;
			return;
		}
		BbAlphaTree<K,V> curr = this;
		while (true) {
			if (curr.key.compareTo(key) < 0) {
				if (curr.right == null) {
					BbAlphaTree<K,V> node = new BbAlphaTree<K,V>(key, value, curr, 1);
					curr.right = node;
					break;
				}
				curr = curr.right;
			}
			else if (curr.key.compareTo(key) > 0) {
				if (curr.left == null) {
					BbAlphaTree<K,V> node = new BbAlphaTree<K,V>(key, value, curr, 1);
					curr.left = node;
					break;
				}
				curr = curr.left;
			}
			else {
				curr.value = value;
				return;
			}
		}
		BbAlphaTree<K,V> target = balancer(curr,1);
		if (target != null) {
			fixTree(target);
		}
	}

	public V find(K key) {
		if (key == null || this.key == null || this.parent != null) { return null; }
		BbAlphaTree<K,V> runner = findNode(key);
		return runner != null ? runner.value : null;
	}

	public void delete(K key) {
		if (key == null || this.key == null || this.parent != null) { return; }
		BbAlphaTree<K,V> runner = findNode(key);
		if (runner != null) {
			if (isLeaf(runner) && runner == this) { 
				this.key = null;
				this.weight = 0;
				return;
			}
			else if (runner.left != null && runner.right != null) {
				BbAlphaTree<K,V> swapTarget = runner.left == null 
					? findMinimum(runner.right) : findMaximum(runner.left);
				swap(swapTarget,runner);
				runner = swapTarget;
			}
			if (runner == this) {
				swapRoot();
				return;
			}
			if (runner.left != null) {
				routeParent(runner,runner.left);
			}
			else {
				routeParent(runner,runner.right);
			}
			runner = balancer(runner.parent,-1);
			if (runner != null) {
				fixTree(runner);
			}
		}
	}

	public boolean isEmpty() {
		return key == null;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	public MambaTree getParent() {
		return parent;
	}
	public MambaTree getRight() {
		return right;
	}
	public MambaTree getLeft() {
		return left;
	} 

	// ---------------------- Tree Specific Operations --------------------- \\

	/**
	* When deleting root with only a single subtree, correct this
	* by swapping root of subtree with this
	*/
	private void swapRoot() {
		BbAlphaTree<K,V> target = this.left != null ? this.left : this.right;
		swap(target,this);
		this.weight = target.weight;
		this.left = target.left;
		this.right = target.right;
		if (this.left != null) {
			this.left.parent = this;
		}
		if (this.right != null) {
			this.right.parent = this;
		}
	}

	/**
	* Swap this with target to restore this to root
	*/
	private void adjustRoot(BbAlphaTree<K,V> target) {
		// swap data + weights
		swap(this,target);
		int tempWeight = this.weight;
		this.weight = target.weight;
		target.weight = tempWeight;
		// swap lefts
		BbAlphaTree<K,V> swapTarg = target.left;
		target.left = this.left;
		this.left = swapTarg;
		if (this.left != null) {
			this.left.parent = this;
		}
		if (target.left != null) {
			target.left.parent = target;
		}
		// swap rights
		swapTarg = target.right;
		target.right = this.right;
		this.right = swapTarg;
		if (this.right != null) {
			this.right.parent = this;
		}
		if (target.right != null) {
			target.right.parent = target;
		}
		// swap parents
		target.parent = this.parent;
		this.parent = null;
		if (target.parent != null) {
			if (target.parent.left == this) {
				target.parent.left = target;
			}
			else {
				target.parent.right = target;
			}
		}
	}

	/**
	* Walk up tree and update tree weights
	*/
	private BbAlphaTree<K,V> balancer(BbAlphaTree<K,V> target, int flag) {
		BbAlphaTree<K,V> unbalanced = null;
		while (target != null) {
			target.weight += flag;
			unbalanced = checkBalance(target);
			target = target.parent;
		}
		return unbalanced;
	}

	/**
	* Check if subtree rooted at target is balanced, returning target if
	* it is not otherwise null
	*/
	private BbAlphaTree<K,V> checkBalance(BbAlphaTree<K,V> target) {
		BbAlphaTree<K,V> check = target.left != null ? target.left : target.right;
		if (check != null) {
			double frac = ((double) check.weight)/target.weight;
			return frac < alpha || frac > 1-alpha ? target : null;
		}
		return null;
	}

	/**
	* Rebuilds the tree rooted at target and connects with parent if
	* it exists, otherwise adjusts root and this to continue pointing
	* to the root of the tree
	*/
	private void fixTree(BbAlphaTree<K,V> target) {
		// if parent exists, must record it and store direction to child
		BbAlphaTree<K,V> parent = target.parent;
		boolean flag = false;
		if (parent != null && parent.left == target) {
			flag = true;
		}
		// insertion has triggered a rebalance
		BbAlphaTree<K,V> genRoot = rebuildTree(target);
		// case 0 : entire tree has been rebalanced (this has been shifted)
		if (parent == null) {
			adjustRoot(genRoot);
		}
		// case 1 : subtree has been rebalanced
		else {
			genRoot.parent = parent;
			if (flag) {
				parent.left = genRoot;
			}
			else {
				parent.right = genRoot;
			}
		}
	}

	/**
	* Rebuild binary search tree rooted at root to a balanced tree
	*/
	private BbAlphaTree<K,V> rebuildTree(BbAlphaTree<K,V> root) {
		int weight = 1;
		weight += root.left != null ? root.left.weight : 0;
		weight += root.right != null ? root.right.weight : 0;
		BbAlphaTree<K,V> listHead = bstToLl(root);
		return llToBst(listHead,1,weight);
	}

	/**
	* Convert a bst to a sorted linked list
	*/
	private BbAlphaTree<K,V> bstToLl(BbAlphaTree<K,V> node) {
		BbAlphaTree<K,V> right = recLl(node);
		BbAlphaTree<K,V> next = right.right;
		right.right = null;
		return next;
	}

	/**
	* Recursive method for bst to linked list transform
	*/
	private BbAlphaTree<K,V> recLl(BbAlphaTree<K,V> node) {
		if (node == null) {
			return null;
		}
		BbAlphaTree<K,V> left = recLl(node.left);
		BbAlphaTree<K,V> right = recLl(node.right);
		if (left != null) {
			node.right = left.right;
			left.right = node;
		}
		else {
			node.right = node;
		}
		if (right != null) {
			BbAlphaTree<K,V> save = right.right;
			right.right = node.right;
			node.right = save;
		}
		if (left == null && right == null) {
			node.right = node;
		}
		return right != null ? right : node;
	}

	/**
	* Convert a sorted linked list to a balanced binary search tree
	*/
	private BbAlphaTree<K,V> llToBst(BbAlphaTree<K,V> curr, int low, int high) {
		if (low > high) {
			return null;
		}
		int mid = low+(high-low)/2;
		BbAlphaTree<K,V> left = llToBst(curr,low,mid-1);
		BbAlphaTree<K,V> save = new BbAlphaTree<K,V>(curr.key, curr.value, null, (high-low)+1);
		if (curr.right != null) {
			curr.key = curr.right.key;
			curr.value = curr.right.value;
			curr.right = curr.right.right;
		}
		BbAlphaTree<K,V> right = llToBst(curr,mid+1,high);
		save.left = left;
		save.right = right;
		if (left != null) { 
			left.parent = save;
		}
		if (right != null) {
			right.parent = save;
		}
		return save;
	}

	/**
	* Determine whether node is a leaf or not
	*/
	private boolean isLeaf(BbAlphaTree<K,V> node) {
			return node.left == null && node.right == null;
	}

	 /**
	* Find the BbAlphaTree<K,V> whose key matches the given key
	*/
	private BbAlphaTree<K,V> findNode(K key) {
		BbAlphaTree<K,V> runner = this;
		while (runner != null) {
			if (runner.key.compareTo(key) == 0) {
				break;
			}
			runner = runner.key.compareTo(key) < 0 ? runner.right : runner.left;
		}
		return runner;
	}

	/**
	* Retrieve BbAlphaTree<K,V> with maximum value key in
	* subtree rooted at node
	*/
	private BbAlphaTree<K,V> findMaximum(BbAlphaTree<K,V> node) {
		BbAlphaTree<K,V> runner = node;
		while (runner.right != null) {
			runner = runner.right;
		}
		return runner;
	}

	/**
	* Retrieve BbAlphaTree<K,V> with minimum value key in
	* subtree rooted at node
	*/
	private BbAlphaTree<K,V> findMinimum(BbAlphaTree<K,V> node) {
		BbAlphaTree<K,V> runner = node;
		while (runner.left != null) {
			runner = runner.left;
		}
		return runner;
	}

	/**
	* Swap keys, values and priorities between x and y
	*/
	private void swap(BbAlphaTree<K,V> x, BbAlphaTree<K,V> y) {
		K tempKey = x.key;
		x.key = y.key;
		y.key = tempKey;
		V tempValue = x.value;
		x.value = y.value;
		y.value = tempValue;
	}

	/**
	* Delete base with replacement target
	*/
	private void routeParent(BbAlphaTree<K,V> base, BbAlphaTree<K,V> target) {
		if (base.parent.left == base) {
			base.parent.left = target;
		}
		else {
			base.parent.right = target;
		}
		if (target != null) {
			target.parent = base.parent;
		}
	}
	
}