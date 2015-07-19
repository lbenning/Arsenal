import java.lang.Math.*;

/**
* Scapegoat Tree - A height balanced binary
* search tree that achieves amortized complexity
* of O(mlogn) for a sequence of m insert and delete
* operations and O(logn) worst case complexity for find operations
* on at most n items. Items are key,value pairs,
* duplicate keys are not allowed
*/
public class ScapegoatTree<K extends Comparable<K>, V extends Comparable<V>> implements MambaTree<K,V> {

  private K key;
  private V value;

  private ScapegoatTree<K,V> parent;
  private ScapegoatTree<K,V> left;
  private ScapegoatTree<K,V> right;

  private double alpha;
  private int nodeCount;
  private int maxNodeCount;

  public ScapegoatTree() {
    this.alpha = 0.75;
  }

  public ScapegoatTree(double alpha) {
    this.alpha = (0.5 < alpha && alpha < 1.0) ? alpha : 0.75;
  }

  private ScapegoatTree(K key, V value, ScapegoatTree<K,V> parent) {
    this.key = key;
    this.value = value;
    this.parent = parent;
  }

   public void insert(K key, V value) {
    if (key == null || this.parent != null) { return; }
    if (this.key == null) {
      this.key = key;
      this.value = value;
      nodeCount = 1;
      maxNodeCount = 1;
      return;
    }
    ScapegoatTree<K,V> curr = this;
    int depth = 0;
    while (true) {
      depth += 1;
      if (curr.key.compareTo(key) < 0) {
        if (curr.right == null) {
          ScapegoatTree<K,V> node = new ScapegoatTree<K,V>(key, value, curr);
          curr.right = node;
          curr = node;
          break;
        }
        curr = curr.right;
      }
      else if (curr.key.compareTo(key) > 0) {
        if (curr.left == null) {
          ScapegoatTree<K,V> node = new ScapegoatTree<K,V>(key, value, curr);
          curr.left = node;
          curr = node;
          break;
        }
        curr = curr.left;
      }
      else {
        curr.value = value;
        return;
      }
    }
    nodeCount += 1;
    maxNodeCount = Math.max(nodeCount, maxNodeCount);
    if (depth > Math.floor(Math.log(nodeCount)/Math.log(1.0/alpha))) {
      insertionFix(curr);
    }
  }

  public V find(K key) {
    if (key == null || this.key == null || this.parent != null) { return null; }
    ScapegoatTree<K,V> runner = findNode(key);
    return runner != null ? runner.value : null;
  }

  public void delete(K key) {
    if (key == null || this.key == null || this.parent != null) { return; }
    ScapegoatTree<K,V> runner = findNode(key);
    if (runner != null) {
      if (isLeaf(runner) && runner == this) { 
        this.key = null;
        nodeCount = 0;
        maxNodeCount = 0;
        return;
      }
      else if (runner.left != null && runner.right != null) {
        ScapegoatTree<K,V> swapTarget = runner.left == null 
          ? findMinimum(runner.right) : findMaximum(runner.left);
        swap(swapTarget,runner);
        runner = swapTarget;
      }
      if (runner == this) {
        swapRoot();
      }
      else if (runner.left != null) {
        routeParent(runner,runner.left);
      }
      else {
        routeParent(runner,runner.right);
      }
      nodeCount -= 1;
      if (nodeCount < alpha*maxNodeCount) {
        fixTree(this, nodeCount);
        maxNodeCount = nodeCount;
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
  * Swap this with target to restore this to root
  */
  private void adjustRoot(ScapegoatTree<K,V> target) {
    // swap data + weights
    swap(this,target);
    // swap lefts
    ScapegoatTree<K,V> swapTarg = target.left;
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
  * When deleting root with only a single subtree, correct this
  * by swapping root of subtree with this
  */
  private void swapRoot() {
    ScapegoatTree<K,V> target = this.left != null ? this.left : this.right;
    swap(target,this);
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
  * Fix the scapegoat tree after insertion of a deep node
  */
  private void insertionFix(ScapegoatTree<K,V> target) {
    int treeSize = 1;
    while (target.parent != null) {
      int siblingSize = getSize(getSibling(target));
      int parentSize = treeSize + siblingSize + 1;
      if (treeSize > alpha*parentSize) {
        treeSize = parentSize;
        break;
      }
      treeSize = parentSize;
      target = target.parent;
    }
    fixTree(target.parent, treeSize);
  }

  /**
  * Rebuild the entire tree rooted at target
  */
  private void fixTree(ScapegoatTree<K,V> target, int size) {
    boolean parentDir = false;
    ScapegoatTree<K,V> parent = target.parent;
    if (parent != null && parent.right == target) {
      parentDir = true;
    }
    ScapegoatTree<K,V> treeRoot = bstToLl(target);
    treeRoot = llToBst(treeRoot,1,size);
    if (parent != null) {
      if (parentDir) {
        parent.right = treeRoot;
      }
      else {
        parent.left = treeRoot;
      }
      treeRoot.parent = parent;
    }
    else {
      adjustRoot(treeRoot);
    }
  }

  /**
  * Rebuild binary search tree rooted at root to a balanced tree
  */
  private ScapegoatTree<K,V> rebuildTree(ScapegoatTree<K,V> root, int size) {
    ScapegoatTree<K,V> listHead = bstToLl(root);
    return llToBst(listHead,1,size);
  }

  /**
  * Convert a bst to a sorted linked list
  */
  private ScapegoatTree<K,V> bstToLl(ScapegoatTree<K,V> node) {
    ScapegoatTree<K,V> right = recLl(node);
    ScapegoatTree<K,V> next = right.right;
    right.right = null;
    return next;
  }

  /**
  * Recursive method for bst to linked list transform
  */
  private ScapegoatTree<K,V> recLl(ScapegoatTree<K,V> node) {
    if (node == null) {
      return null;
    }
    ScapegoatTree<K,V> left = recLl(node.left);
    ScapegoatTree<K,V> right = recLl(node.right);
    if (left != null) {
      node.right = left.right;
      left.right = node;
    }
    else {
      node.right = node;
    }
    if (right != null) {
      ScapegoatTree<K,V> save = right.right;
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
  private ScapegoatTree<K,V> llToBst(ScapegoatTree<K,V> curr, int low, int high) {
    if (low > high) {
      return null;
    }
    int mid = low+(high-low)/2;
    ScapegoatTree<K,V> left = llToBst(curr,low,mid-1);
    ScapegoatTree<K,V> save = new ScapegoatTree<K,V>(curr.key, curr.value, null);
    if (curr.right != null) {
      curr.key = curr.right.key;
      curr.value = curr.right.value;
      curr.right = curr.right.right;
    }
    ScapegoatTree<K,V> right = llToBst(curr,mid+1,high);
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
  private boolean isLeaf(ScapegoatTree<K,V> node) {
      return node.left == null && node.right == null;
  }

   /**
  * Find the ScapegoatTree<K,V> whose key matches the given key
  */
  private ScapegoatTree<K,V> findNode(K key) {
    ScapegoatTree<K,V> runner = this;
    while (runner != null) {
      if (runner.key.compareTo(key) == 0) {
        break;
      }
      runner = runner.key.compareTo(key) < 0 ? runner.right : runner.left;
    }
    return runner;
  }

  /**
  * Retrieve ScapegoatTree<K,V> with maximum value key in
  * subtree rooted at node
  */
  private ScapegoatTree<K,V> findMaximum(ScapegoatTree<K,V> node) {
    ScapegoatTree<K,V> runner = node;
    while (runner.right != null) {
      runner = runner.right;
    }
    return runner;
  }

  /**
  * Retrieve ScapegoatTree<K,V> with minimum value key in
  * subtree rooted at node
  */
  private ScapegoatTree<K,V> findMinimum(ScapegoatTree<K,V> node) {
    ScapegoatTree<K,V> runner = node;
    while (runner.left != null) {
      runner = runner.left;
    }
    return runner;
  }

  /**
  * Swap keys, values and priorities between x and y
  */
  private void swap(ScapegoatTree<K,V> x, ScapegoatTree<K,V> y) {
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
  private void routeParent(ScapegoatTree<K,V> base, ScapegoatTree<K,V> target) {
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

  /**
  * Get the size of the subtree rooted at the given node
  */
  private int getSize(ScapegoatTree<K,V> node) {
    return node == null ? 0 : 1 + getSize(node.left) + getSize(node.right);
  }

  /**
  * Get the sibling of the targeted node
  */
  private ScapegoatTree<K,V> getSibling(ScapegoatTree<K,V> target) {
    return target.parent.right == target ? target.parent.left : target.parent.right;
  }

}