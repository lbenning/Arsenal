/**
* Splay Tree - A self adjusting binary search tree
* that provides insert, find and delete operations in 
* amortized O(logn) time for a tree on n nodes. Every node
* is uniquely identified by a key (no duplicates in tree),
* and these keys map to corresponding values
*/
public class SplayTree<K extends Comparable<K>,V extends Comparable<V>> implements MambaTree<K,V> {

  private K key;
  private V value;

  private SplayTree<K,V> left;
  private SplayTree<K,V> right;
  private SplayTree<K,V> parent;

  public SplayTree() {}

  private SplayTree(K key, V value, SplayTree<K,V> parent) {
    this.key = key;
    this.value = value;
    this.parent = parent;
  }

  public void insert(K key, V value) {
    if (key == null || this.parent != null) { return; }
    if (this.key == null) {
      this.key = key;
      this.value = value;
      return;
    }
    SplayTree<K,V> curr = this;
    while (true) {
      if (curr.key.compareTo(key) < 0) {
        if (curr.right == null) {
          SplayTree<K,V> node = new SplayTree<K,V>(key, value, curr);
          curr.right = node;
          splay(node);
          break;
        }
        curr = curr.right;
      }
      else if (curr.key.compareTo(key) > 0) {
        if (curr.left == null) {
          SplayTree<K,V> node = new SplayTree<K,V>(key, value, curr);
          curr.left = node;
          splay(node);
          break;
        }
        curr = curr.left;
      }
      else {
        curr.value = value;
        splay(curr);
        verifyRoot();
        return;
      }
    }
    verifyRoot();
  }

  public V find(K key) {
    if (key == null || this.key == null || this.parent != null) { 
      return null; 
    }
    SplayTree<K,V> runner = findNode(key);
    if (runner != null) {
      splay(runner);
      V value = runner.value;
      verifyRoot();
      return value;
    }
    return null;
  }

  public void delete(K key) {
    if (key == null || this.key == null || this.parent != null) { return; }
    SplayTree<K,V> runner = findNode(key);
    if (runner != null) {
      if (isLeaf(runner)) {
        if (runner == this) { 
          this.key = null;
          this.value = null;
          return;
        }
        else {
          routeParent(runner,null);
        }
      }
      else {
        if (runner.left == null) {
          SplayTree<K,V> min = findMinimum(runner.right);
          swap(runner,min);
          runner = min;
          routeParent(runner,runner.right);
        }
        else {
          SplayTree<K,V> max = findMaximum(runner.left);
          swap(runner,max);
          runner = max;
          routeParent(runner,runner.left);
        }
      }
      verifyRoot();
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
  * If this has been rotated to be off the root, rewire such that
  * this becomes the root of the tree (since this is final, it cannot
  * be edited directly). If this has been rotated off, it is either
  * the left or right child of the root, hence two cases to consider
  */
  private void verifyRoot() {
    if (this.parent != null) {
      swap(this,this.parent);
      if (this.parent.left == this) {
        this.parent.left = this.left;
        if (this.left != null) {
          this.left.parent = this.parent;
        }
        this.left = this.parent;
        SplayTree<K,V> temp = this.right;
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
        SplayTree<K,V> temp = this.left;
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
  * Perform tree rotations to splay node to the root
  */
  private void splay(SplayTree<K,V> node) {
    while (node.parent != null) {
      if (node.parent.parent == null) {
        if (node.parent.left == node) {
         rotateRight(node.parent);
        }
        else {
         rotateLeft(node.parent);
        }
      }
      else {
        // Left-left case
        if (node.parent.parent.left == node.parent && node.parent.left == node) {
         rotateRight(node.parent);
         rotateRight(node.parent);
        }
        // Right-right case
        else if(node.parent.parent.right == node.parent && node.parent.right == node) {
         rotateLeft(node.parent);
         rotateLeft(node.parent);
        }
        // Right-left case
        else if(node.parent.parent.right == node.parent) {
         rotateRight(node.parent);
         rotateLeft(node.parent);
        }
        // Left-right case
        else {
         rotateLeft(node.parent);
         rotateRight(node.parent);
        }
      }
    }
  }
  
  /**
  * Perform a right rotation centered on node
  */
  private void rotateRight(SplayTree<K,V> node) {
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
  private void rotateLeft(SplayTree<K,V> node) {
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
  * Find the SplayNode whose key matches the given key
  */
  private SplayTree<K,V> findNode(K key) {
    SplayTree<K,V> runner = this;
    while (runner != null) {
      if (runner.key.compareTo(key) == 0) {
        break;
      }
      runner = runner.key.compareTo(key) < 0 ? runner.right : runner.left;
    }
    return runner;
  }

  /**
  * Retrieve SplayNode with maximum value key in
  * subtree rooted at node
  */
  private SplayTree<K,V> findMaximum(SplayTree<K,V> node) {
    SplayTree<K,V> runner = node;
    while (runner.right != null) {
      runner = runner.right;
    }
    return runner;
  }

  /**
  * Retrieve SplayNode with minimum value key in
  * subtree rooted at node
  */
  private SplayTree<K,V> findMinimum(SplayTree<K,V> node) {
    SplayTree<K,V> runner = node;
    while (runner.left != null) {
      runner = runner.left;
    }
    return runner;
  }

  /**
  * Determine whether node is a leaf or not
  */
  private boolean isLeaf(SplayTree<K,V> node) {
      return node.left == null && node.right == null;
  }

  /**
  * Swap keys and values between x and y
  */
  private void swap(SplayTree<K,V> x, SplayTree<K,V> y) {
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
  private void routeParent(SplayTree<K,V> base, SplayTree<K,V> target) {
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