/**
* AVL Tree - A self balancing binary search tree
* that provides insert, find and delete operations in 
* worst case O(logn) time for a tree on n nodes. Every node
* is uniquely identified by a key (no duplicates in tree),
* and these keys map to corresponding values
*/
public class AvlTree<K extends Comparable<K>,V extends Comparable<V>> implements MambaTree<K,V> {

  private K key;
  private V value;

  private AvlTree<K,V> left;
  private AvlTree<K,V> right;
  private AvlTree<K,V> parent;

  private int balance;

  public AvlTree() {}

  private AvlTree(K key, V value, AvlTree<K,V> parent, int balance) {
    this.key = key;
    this.value = value;
    this.parent = parent;
    this.balance = balance;
  }

  public void insert(K key, V value) {
      if (key == null || this.parent != null) { return; }
      if (this.key == null) {
        this.key = key;
        this.value = value;
        return;
      }
      AvlTree<K,V> curr = this;
      while (true) {
        if (curr.key.compareTo(key) < 0) {
          if (curr.right == null) {
              AvlTree<K,V> node = new AvlTree<K,V>(key, value, curr, 0);
              curr.right = node;
              curr = node;
              break;
          }
          curr = curr.right;
        }
        else if (curr.key.compareTo(key) > 0) {
          if (curr.left == null) {
              AvlTree<K,V> node = new AvlTree<K,V>(key, value, curr, 0);
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
      fixInsertion(curr);
      verifyRoot();
  }

  public V find(K key) {
      if (key == null || this.key == null || this.parent != null) { return null; }
      AvlTree<K,V> runner = findNode(key);
      if (runner != null) {
        return runner.value;
      }
      return null;
  }

  public void delete(K key) {
    if (key == null || this.key == null || this.parent != null) { return; }
    AvlTree<K,V> runner = findNode(key);
    if (runner != null) {
      if (isLeaf(runner) && runner == this) {
         this.key = null;
         return;
      }
      else if (runner.left != null || runner.right != null) {
        AvlTree<K,V> target = runner.left == null ? findMinimum(runner.right) : findMaximum(runner.left);
        swap(runner,target);
        runner = target;
      }
      fixDeletion(runner);
      verifyRoot();
    }
  }

  public boolean isEmpty() {
    return this.key == null;
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
      this.balance += this.parent.balance;
      this.parent.balance = this.balance - this.parent.balance;
      this.balance = this.balance - this.parent.balance;
      if (this.parent.left == this) {
        this.parent.left = this.left;
        if (this.left != null) {
          this.left.parent = this.parent;
        }
        this.left = this.parent;
        AvlTree<K,V> temp = this.right;
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
        AvlTree<K,V> temp = this.left;
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
  * After inserting target, walk up tree and correct balances as needed
  */
  private void fixInsertion(AvlTree<K,V> target) {
    // walk up the tree, correcting balances
    // initial walk while parent exists and has zero balance
    while (target.parent != null && target.parent.balance == 0) {
      updateBalance(target,1);
      target = target.parent;
    }
    // case 0 : root has been reached => target.parent == null => done
    if (target.parent == null) {
      return;
    }
    // case 1 : root has not been reached => target.parent.balance != 0
    // perform balance update
    updateBalance(target,1);
    // subcase 0 : the height becomes 0 => height change absorbed => done
    if (target.parent.balance == 0) {
      return;
    }
    // subcase 1 : the height was 1/-1 and did not become 0 => became 2/-2
    // perform a rebalance operation centered on target's parent
    rebalance(target.parent);
  }

  /**
  * After deleting target, walk up tree and correct balances as needed
  */
  private void fixDeletion(AvlTree<K,V> target) {
    // update balance of target's parent and sever target from tree
    updateBalance(target,-1);
    if (isLeaf(target)) {
      if (target.parent.right == target) {
        target.parent.right = null;
      }
      else {
        target.parent.left = null;
      }
    }
    else if (target.right == null) {
      target.parent.right = target.left;
      target.left.parent = target.parent;
      target = target.left;
    }
    else {
      target.parent.left = target.right;
      target.right.parent = target.parent;
      target = target.right;
    }
    // walk up the tree, updating balances and rebalancing as necessary
    while (target.parent != null) {
      target = target.parent;
      // case 1 : target's balance is 1/-1 => was 0 => 
      // height has not changed => balanced update terminates as the change
      // has been absorbed
      if (target.balance == 1 || target.balance == -1) {
        return;
      }
      else if (target.balance != 0) {
        // case 2 : target's balance is 2/-2 => require rebalance
        boolean choice = (target.balance == 2 && target.right.balance == 0 
          || target.balance == -2 && target.left.balance == 0);
        rebalance(target);
        if (choice) {
          return;
        }
        // after rebalance, target must now have a parent as rotation shifts
        // target to be left or right child of a node
        // shift target up again as this parent has been updated
        target = target.parent;
      }
      if (target.parent != null) {
        updateBalance(target,-1);
      }
    }
  }

  /**
  * Perform rebalancing operation centered on runner
  */
  private void rebalance(AvlTree<K,V> runner) {
    if (runner.balance == 2) {
      if (runner.right.balance == -1) {
        rotateRight(runner.right);
      }
      rotateLeft(runner);
    }
    else {
      if (runner.left.balance == 1) {
        rotateLeft(runner.left);
      }
      rotateRight(runner);
    }
  }

  /**
  * Perform a right rotation centered on node
  */
  private void rotateRight(AvlTree<K,V> node) {
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
    node.balance = node.balance+1+max(0,-node.parent.balance);
    node.parent.balance = node.parent.balance+1+max(0,node.balance);
  }

  /**
  * Perform a left rotation centered on node
  */
  private void rotateLeft(AvlTree<K,V> node) {
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
    node.balance = node.balance-1-max(0,node.parent.balance);
    node.parent.balance = node.parent.balance-1-max(0,-node.balance);
  }

  /**
  * Determine whether node is a leaf or not
  */
  private boolean isLeaf(AvlTree<K,V> node) {
      return node.left == null && node.right == null;
  }

   /**
  * Find the AvlTree<K,V> whose key matches the given key
  */
  private AvlTree<K,V> findNode(K key) {
    AvlTree<K,V> runner = this;
    while (runner != null) {
      if (runner.key.compareTo(key) == 0) {
        break;
      }
      runner = runner.key.compareTo(key) < 0 ? runner.right : runner.left;
    }
    return runner;
  }

  /**
  * Retrieve AvlTree<K,V> with maximum value key in
  * subtree rooted at node
  */
  private AvlTree<K,V> findMaximum(AvlTree<K,V> node) {
    AvlTree<K,V> runner = node;
    while (runner.right != null) {
      runner = runner.right;
    }
    return runner;
  }

  /**
  * Retrieve AvlTree<K,V> with minimum value key in
  * subtree rooted at node
  */
  private AvlTree<K,V> findMinimum(AvlTree<K,V> node) {
    AvlTree<K,V> runner = node;
    while (runner.left != null) {
      runner = runner.left;
    }
    return runner;
  }

  /**
  * Swap keys and values between x and y
  */
  private void swap(AvlTree<K,V> x, AvlTree<K,V> y) {
    K tempKey = x.key;
    x.key = y.key;
    y.key = tempKey;
    V tempValue = x.value;
    x.value = y.value;
    y.value = tempValue;
  }

  /**
  * Update the balance of base's parent after a balance change
  */
  private void updateBalance(AvlTree<K,V> base, int flip) {
    base.parent.balance += base.parent.left == base ? -flip : flip;
  }

  /**
  * Utility function for integer max
  */
  private int max(int x, int y) {
    return x > y ? x : y;
  }
    
}