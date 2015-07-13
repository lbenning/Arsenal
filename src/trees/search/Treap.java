 import java.util.Random;

/**
* Treap - A randomized binary search tree
* that provides insert, find and delete operations in 
* expected O(logn) time for a tree on n nodes. Every node
* is uniquely identified by a key (no duplicates in tree),
* and these keys map to corresponding values
*/
public class Treap<K extends Comparable<K>, V extends Comparable<V>> implements MambaTree<K,V> {

  private K key;
  private V value;

  private Treap<K,V> left;
  private Treap<K,V> right;
  private Treap<K,V> parent;

  private static Random random = new Random();
  private int priority;

  public Treap() {}

  private Treap(K key, V value, Treap<K,V> parent) {
    this.key = key;
    this.value = value;
    this.parent = parent;
    priority = random.nextInt();
  }

  public void insert(K key, V value) {
    if (key == null || this.parent != null) { return; }
    if (this.key == null) {
      this.key = key;
      this.value = value;
      this.priority = random.nextInt();
      return;
    }
    Treap<K,V> curr = this;
    while (true) {
      if (curr.key.compareTo(key) < 0) {
        if (curr.right == null) {
          Treap<K,V> node = new Treap<K,V>(key, value, curr);
          curr.right = node;
          prioritize(node);
          break;
        }
        curr = curr.right;
      }
      else if (curr.key.compareTo(key) > 0) {
        if (curr.left == null) {
          Treap<K,V> node = new Treap<K,V>(key, value, curr);
          curr.left = node;
          prioritize(node);
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
    Treap<K,V> runner = findNode(key);
    return runner != null ? runner.value : null;
  }

  public void delete(K key) {
    if (key == null || this.key == null || this.parent != null) { return; }
    Treap<K,V> runner = findNode(key);
    if (runner != null) {
      if (isLeaf(runner) && runner == this) { 
        this.key = null;
        return;
      }
      else {
        if (runner == this) {
          deprioritize(runner);
          if (runner.parent.right == runner) {
            verifyRoot();
            runner = runner.right;
          }
          else {
            verifyRoot();
            runner = runner.left;
          }
        }
        while (!isLeaf(runner)) {
          deprioritize(runner);
        }
        routeParent(runner,null);
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
        Treap<K,V> temp = this.right;
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
        Treap<K,V> temp = this.left;
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
  * Perform a right rotation centered on node
  */
  private void rotateRight(Treap<K,V> node) {
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
  private void rotateLeft(Treap<K,V> node) {
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
  * Determine whether node is a leaf or not
  */
  private boolean isLeaf(Treap<K,V> node) {
      return node.left == null && node.right == null;
  }

   /**
  * Find the Treap<K,V> whose key matches the given key
  */
  private Treap<K,V> findNode(K key) {
    Treap<K,V> runner = this;
    while (runner != null) {
      if (runner.key.compareTo(key) == 0) {
        break;
      }
      runner = runner.key.compareTo(key) < 0 ? runner.right : runner.left;
    }
    return runner;
  }

  /**
  * Retrieve Treap<K,V> with maximum value key in
  * subtree rooted at node
  */
  private Treap<K,V> findMaximum(Treap<K,V> node) {
    Treap<K,V> runner = node;
    while (runner.right != null) {
      runner = runner.right;
    }
    return runner;
  }

  /**
  * Retrieve Treap<K,V> with minimum value key in
  * subtree rooted at node
  */
  private Treap<K,V> findMinimum(Treap<K,V> node) {
    Treap<K,V> runner = node;
    while (runner.left != null) {
      runner = runner.left;
    }
    return runner;
  }

  /**
  * Swap keys, values and priorities between x and y
  */
  private void swap(Treap<K,V> x, Treap<K,V> y) {
    K tempKey = x.key;
    x.key = y.key;
    y.key = tempKey;
    V tempValue = x.value;
    x.value = y.value;
    y.value = tempValue;
    int tempPriority = x.priority;
    x.priority = y.priority;
    y.priority = tempPriority;
  }

  /**
  * Delete base with replacement target
  */
  private void routeParent(Treap<K,V> base, Treap<K,V> target) {
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
  * Restore priority invariant for insertion
  */
  private void prioritize(Treap<K,V> node) {
    while (node.parent != null && node.parent.priority < node.priority) {
      if (node.parent.left == node) {
        rotateRight(node.parent);
      }
      else {
        rotateLeft(node.parent);
      }
    }
  }

  /**
  * Restore priority invariant for deletion
  */
  private void deprioritize(Treap<K,V> node) {
    if (node.left == null || node.right != null 
      && node.left.priority < node.right.priority) {
      rotateLeft(node);
    }
    else {
      rotateRight(node);
    }
  }

}