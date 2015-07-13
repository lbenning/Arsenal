/**
* Red Black Tree - A self balancing binary search tree
* that provides insert, find and delete operations in 
* guaranteed O(logn) time for a tree on n nodes. Every node
* is uniquely identified by a key (no duplicates in tree),
* and these keys map to corresponding values
*/
public class RedBlackTree<K extends Comparable<K>,V extends Comparable<V>> implements MambaTree<K,V> {

	private K key;
  private V value;

  private RedBlackTree<K,V> left;
  private RedBlackTree<K,V> right;
  private RedBlackTree<K,V> parent;

  private int color;

	public RedBlackTree() {}

  private RedBlackTree(K key, V value, RedBlackTree<K,V> parent, int color) {
    this.key = key;
    this.value = value;
    this.parent = parent;
    this.color = color;
  }

	public void insert(K key, V value) {
    if (key == null || this.parent != null) { return; }
    if (this.key == null) {
      this.key = key;
      this.value = value;
      this.color = 1;
      return;
    }
    RedBlackTree<K,V> curr = this;
    while (true) {
      if (curr.key.compareTo(key) < 0) {
        if (curr.right == null) {
            RedBlackTree<K,V> node = new RedBlackTree<K,V>(key, value, curr, 0);
            curr.right = node;
            insertionCheck(node);
            break;
        }
        curr = curr.right;
      }
      else if (curr.key.compareTo(key) > 0) {
        if (curr.left == null) {
            RedBlackTree<K,V> node = new RedBlackTree<K,V>(key, value, curr, 0);
            curr.left = node;
            insertionCheck(node);
            break;
        }
        curr = curr.left;
      }
      else {
        curr.value = value;
        return;
      }
    }
    verifyRoot();
  }

	public V find(K key) {
    if (key == null || this.key == null || this.parent != null) { return null; }
    RedBlackTree<K,V> runner = findNode(key);
    return runner != null ? runner.value : null;
	}

	public void delete(K key) {
    if (key == null || this.key == null || this.parent != null) { return; }
    RedBlackTree<K,V> runner = findNode(key);
    if (runner != null) {
      if (isLeaf(runner) && runner == this) { 
        this.key = null;
        return;
      }
      else if (!isLeaf(runner)) {
        RedBlackTree<K,V> target = runner.left == null 
          ? findMinimum(runner.right) : findMaximum(runner.left);
        swap(runner,target);
        runner = target;
      }
      deletionCheck(runner);
    }
    verifyRoot();
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
  * If this has been rotated to be off the root, rewire such that
  * this becomes the root of the tree (since this is final, it cannot
  * be edited directly). If this has been rotated off, it is either
  * the left or right child of the root, hence two cases to consider
  */
  private void verifyRoot() {
    if (this.parent != null) {
      swap(this,this.parent);
      int t = this.color;
      this.color = this.parent.color;
      this.parent.color = t;
      if (this.parent.left == this) {
        this.parent.left = this.left;
        if (this.left != null) {
          this.left.parent = this.parent;
        }
        this.left = this.parent;
        RedBlackTree<K,V> temp = this.right;
        this.right = this.parent.right;
        if (this.parent.right != null) {
          this.right.parent = this;
        }
        this.parent.right = temp;
        if (temp != null) {
          temp.parent = this.parent;
        }
      }
      else {
        this.parent.right = this.right;
        if (this.right != null) {
          this.right.parent = this.parent;
        }
        this.right = this.parent;
        RedBlackTree<K,V> temp = this.left;
        this.left = this.parent.left;
        if (this.left != null) {
          this.left.parent = this;
        }
        this.parent.left = temp;
        if (temp != null) {
          temp.parent = this.parent;
        }
      }
      this.parent.parent = this;
      this.parent = null;
    }
  }

  /**
  * Find the RedBlackTree<K,V> whose key matches the given key
  */
	private RedBlackTree<K,V> findNode(K key) {
    RedBlackTree<K,V> runner = this;
    while (runner != null) {
      if (runner.key.compareTo(key) == 0) {
        break;
      }
      runner = runner.key.compareTo(key) < 0 ? runner.right : runner.left;
    }
    return runner;
  }

  /**
  * Verify red-black tree properties hold after an
  * insertion operation, correcting as needed
  */
  private void insertionCheck(RedBlackTree<K,V> node) {
    // case 0 : node is root, make black
    if (node.parent == null) {
      node.color = 1;
      return;
    }
    // case 1 : parent is black
    else if (node.parent.color == 1) {
      return;
    }
    // case 2 : parent is red => grandparent exists since
    // root is black, check uncle
    RedBlackTree<K,V> uncle = getUncle(node);
    RedBlackTree<K,V> grandparent = getGrandparent(node);
    if (uncle != null && uncle.color == 0) {
      uncle.color = 1;
      node.parent.color = 1;
      grandparent.color = 0;
      insertionCheck(grandparent);
      return;
    }
    // case 3 : parent is red, uncle is black, grandparent exists,
    // node is right child of parent, parent is left of grandparent,
    // or mirror version
    if (node.parent.right == node && grandparent.left == node.parent) {
      rotateLeft(node.parent);
      node = node.left;
    }
    else if (node.parent.left == node && grandparent.right == node.parent) {
      rotateRight(node.parent);
      node = node.right;
    }
    // case 4 : parent is red, uncle is black, grandparent exists,
    // node is left child of parent, and parent is left child of
    // grandparent, or mirror version
    grandparent = getGrandparent(node);
    node.parent.color = 1;
    grandparent.color = 0;
    if (node.parent.left == node) {
      rotateRight(grandparent);
    }
    else {
      rotateLeft(grandparent);
    }
  }

  /**
  * Verify red-black tree properties hold after a
  * deletion operation, correcting as needed
  */
  private void deletionCheck(RedBlackTree<K,V> node) {
    // case 0 : node is red => child is black
    RedBlackTree<K,V> child = getChild(node);
    if (node.color == 0) {
      routeParent(node,child);
      return;
    }
    // case 1 : node is black and child is red
    else if (child != null && child.color == 0) {
      child.color = 1;
      routeParent(node,child);
      return;
    }
    // case 2 : node is black and child is black =>
    // node is a black leaf
    chainDeletion(node);
    // node remains a leaf after reparations, now
    // it can be deleted
    // subcase 0 : root deleted, given node is leaf,
    // tree is empty
    if (node.parent == null) {
      this.key = null;
    }
    // subcase 1 : delete leaf
    else {
      routeParent(node,null);
    }
  }

  /**
  * Perform deletion on phantom node
  */
  private void chainDeletion(RedBlackTree<K,V> node) {
    // case 0 : node is root
    if (node.parent == null) {
      return;
    }
    // case 1 : sibling is red (the sibling must exist)
    RedBlackTree<K,V> sibling = getSibling(node);
    if (sibling.color == 0) {
      sibling.color = 1;
      node.parent.color = 0;
      if (node.parent.left == node) {
        rotateLeft(node.parent);
      }
      else {
        rotateRight(node.parent);
      }
    }
    // case 2 : parent, sibling and sibling children are black
    sibling = getSibling(node);
    if (node.parent.color == 1 && sibling.color == 1 
      && (sibling.left == null || sibling.left.color == 1)
      && (sibling.right == null || sibling.right.color == 1)) {
      sibling.color = 0;
      chainDeletion(node.parent);
      return;
    }
    // case 3 : parent is red, sibling is black and sibling
    // children are both black
    sibling = getSibling(node);
    if (sibling != null && sibling.color == 1
      && (sibling.left == null || sibling.left.color == 1)
      && (sibling.right == null || sibling.right.color == 1)
      && node.parent.color == 0) {
      node.parent.color = 1;
      sibling.color = 0;
      return;
    }
    // case 4 : sibling black, sibling left red, sibling right black,
    // node is left child of parent
    sibling = getSibling(node);
    if (sibling != null && sibling.color == 1) {
      if (node.parent.left == node 
        && (sibling.left != null && sibling.left.color == 0) 
        && (sibling.right == null || sibling.right.color == 1)) {
        sibling.color = 0;
        sibling.left.color = 1;
        rotateRight(sibling);
      }
      else if (node.parent.right == node
        && (sibling.left == null || sibling.left.color == 1)
        && (sibling.right != null && sibling.right.color == 0)) {
        sibling.color = 0;
        sibling.right.color = 1;
        rotateLeft(sibling);
      }
    }
    // case 5 : sibling is black, sibling right is red, node
    // is left child of parent (otherwise mirrored)
    sibling = getSibling(node);
    sibling.color = node.parent.color;
    node.parent.color = 1;
    if (node.parent.left == node) {
      if (sibling.right != null) {
        sibling.right.color = 1;
      }
      rotateLeft(node.parent);
    }
    else {
      if (sibling.left != null) {
        sibling.left.color = 1;
      }
      rotateRight(node.parent);
    }
  }

  /**
  * Delete base with replacement target
  */
  private void routeParent(RedBlackTree<K,V> base, RedBlackTree<K,V> target) {
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
  * Retrieve the sibling of node
  */
  private RedBlackTree<K,V> getSibling(RedBlackTree<K,V> node) {
    return node.parent != null ? node.parent.left == node 
      ? node.parent.right : node.parent.left : null;
  }

  /**
  * Retrieve the single child of node
  */
  private RedBlackTree<K,V> getChild(RedBlackTree<K,V> node) {
    return node.left == null ? node.right : node.left;
  }

  /**
  * Retrieve the grandparent of node
  */
  private RedBlackTree<K,V> getGrandparent(RedBlackTree<K,V> node) {
    return node.parent == null || node.parent.parent == null 
      ? null : node.parent.parent;
  }

  /**
  * Retrieve the uncle of node
  */
  private RedBlackTree<K,V> getUncle(RedBlackTree<K,V> node) {
    if (node.parent == null || node.parent.parent == null) {
      return null;
    }
    return node.parent.parent.left == node.parent 
      ? node.parent.parent.right : node.parent.parent.left;
  }

  /**
  * Perform a right rotation centered on node
  */
  private void rotateRight(RedBlackTree<K,V> node) {
    if (node.parent != null) {
      if (node == node.parent.right) {
        node.parent.right = node.left;
      }
      else {
        node.parent.left = node.left;
      }
    }
    node.left.parent = node.parent;
    node.parent = node.left;
    node.left = node.left.right;
    if (node.left != null) {
      node.left.parent = node;
    }
    node.parent.right = node;
  }

  /**
  * Perform a left rotation centered on node
  */
  private void rotateLeft(RedBlackTree<K,V> node) {
    if (node.parent != null) {
      if (node == node.parent.right) {
        node.parent.right = node.right;
      }
      else {
        node.parent.left = node.right;
      }
    }
    node.right.parent = node.parent;
    node.parent = node.right;
    node.right = node.right.left;
    if (node.right != null) {
      node.right.parent = node;
    }
    node.parent.left = node;
  }

  /**
  * Retrieve RedBlackTree<K,V> with maximum value key in
  * subtree rooted at node
  */
  private RedBlackTree<K,V> findMaximum(RedBlackTree<K,V> node) {
    RedBlackTree<K,V> runner = node;
    while (runner.right != null) {
      runner = runner.right;
    }
    return runner;
  }

  /**
  * Retrieve RedBlackTree<K,V> with minimum value key in
  * subtree rooted at node
  */
  private RedBlackTree<K,V> findMinimum(RedBlackTree<K,V> node) {
    RedBlackTree<K,V> runner = node;
    while (runner.left != null) {
      runner = runner.left;
    }
    return runner;
  }

  /**
  * Determine whether node is a leaf or not
  */
  private boolean isLeaf(RedBlackTree<K,V> node) {
      return node.left == null && node.right == null;
  }

  /**
  * Swap keys and values between x and y
  */
  private void swap(RedBlackTree<K,V> x, RedBlackTree<K,V> y) {
    K tempKey = x.key;
    x.key = y.key;
    y.key = tempKey;
    V tempValue = x.value;
    x.value = y.value;
    y.value = tempValue;
  }

}