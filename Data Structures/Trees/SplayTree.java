/**
* Generic Splay Tree - A self adjusting binary search tree
* that provides insert, find and delete operations in 
* amortized O(logn) time for a tree on n nodes. Every node
* is uniquely identified by a key (no duplicates in tree),
* and these keys map to corresponding values.
*/
public class SplayTree<K extends Comparable<K>,V extends Comparable<V>> implements Tree {

 private SplayNode root;
 private int size;

 public SplayTree() {}

 public SplayTree(K key, V value) {
  root = new SplayNode(key,value);
  size += 1;
 }

 public void insert(K key, V value) {
  if (key != null) {
    if (root == null) {
     size += 1;
     root = new SplayNode(key,value);
    }
    else {
     recursiveInsert(key, value, root);
    }
   }
 }

 public V find(K key) {
  if (key == null) { return null; }
  SplayNode found = recursiveSearch(key, root);
  if (found == null) {
   return null;
  }
  splay(found);
  return found.value;
 }

 public void delete(K key) {
  if (key == null) { return; }
  recursiveRemoval(key, root);
 }

 public boolean isEmpty() {
  return root == null;
 }

 public int size() {
  return size;
 }

 // ---------------------- Tree Specific Operations --------------------- \\

 private void splay(SplayNode s) {
  // Null & Root Cases
  if (s == null || s.parent == null) { 
      root = s;
  }
  // Zig Cases
  else if (s.parent.parent == null) {
   if (s.parent.left == s) {
    rotateRight(s);
   }
   else {
    rotateLeft(s);
   }
   root = s;
  }
  // Zig-Zig and Zig-Zag Cases
  else {
   if (s.parent.parent.left == s.parent && s.parent.left == s) {
    rotateRight(s);
    rotateRight(s);
   }
   else if(s.parent.parent.right == s.parent && s.parent.right == s) {
    rotateLeft(s);
    rotateLeft(s);
   }
   else if(s.parent.parent.right == s.parent) {
    rotateRight(s);
    rotateLeft(s);
   }
   else {
    rotateLeft(s);
    rotateRight(s);
   }
   splay(s);
  }
 }
 
 /**
 * Rotates node s to the right, maintaining
 * BST invariants
 */
 private void rotateLeft(SplayNode s) {
  SplayNode p = s.parent;
  if (p.parent != null) {
      if (p.parent.left == p) {
          p.parent.left = s;
      }
      else {
          p.parent.right = s;
      }
  }
  s.parent = p.parent;
  p.parent = s;
  p.right = s.left;
  if (p.right != null) {
    p.right.parent = p;
  }
  s.left = p;
 }
 
 /**
 * Rotates node s to the right, maintaining
 * BST invariants
 */
 private void rotateRight(SplayNode s) {
  SplayNode parent = s.parent;
  SplayNode grandparent = parent.parent;
  if (grandparent != null) {
      if (grandparent.left == parent) {
          grandparent.left = s;
      }
      else {
          grandparent.right = s;
      }
  }
  s.parent = grandparent;
  
  parent.parent = s;
  parent.left = s.right;
  if (parent.left != null) {
    parent.left.parent = parent;
  }
  s.right = parent;
 }

 /**
  * Recursively search for the specified key in the Splay
  * Tree rooted at current
  */
 private SplayNode recursiveSearch(K key, SplayNode current) {
  if (current == null) { return null; }
  if (key.equals(current.key)) { return current; }
  else if (key.compareTo(current.key) < 0) {
   return recursiveSearch(key, current.left);
  }
  else {
   return recursiveSearch(key, current.right);
  }
 }

 /**
  * Recursively insert a key,value pair into the Splay Tree
  * rooted at current
  */
 private void recursiveInsert(K key, V value, SplayNode current) {
  if (key.compareTo(current.key) < 0) {
   if (current.left == null) {
    current.left = new SplayNode(key, value);
    size += 1;
    current.left.parent = current;
   }
   else {
    recursiveInsert(key, value, current.left);
   }
  }
  else if (key.compareTo(current.key) > 0) {
   if (current.right == null) {
    current.right = new SplayNode(key, value);
    size += 1;
    current.right.parent = current;
   }
   else {
    recursiveInsert(key, value, current.right);
   }
  }
 }

 /**
  * Recursively remove the SplayNode with the specified 
  * key in the Splay Tree rooted at current
  */
 private void recursiveRemoval(K key, SplayNode current) {
  if (current == null) { return; }
  if (key.equals(current.key)) {
   deleteNode(current);
   size -= 1;
  }
  else if (key.compareTo(current.key) < 0) {
   recursiveRemoval(key, current.left);
  }
  else {
   recursiveRemoval(key, current.right);
  }
 }

 /**
 * Removes SplayNode s from the Splay Tree
 */
 private void deleteNode(SplayNode s) {
   if (s == null) { return; }
   if (isLeaf(s)) {
     nullifyParent(s);
     if (s == root) {
         root = null;
     }
   }
   else if (s.left == null || isLeaf(s.right)) {
    SplayNode z = findMinimum(s.right);
    copyValue(z,s);
    deleteNode(z);
   }
   else if (s.right == null || isLeaf(s.left)) {
    SplayNode z = findMaximum(s.left);
    copyValue(z,s);
    deleteNode(z);
   }
   else {
    SplayNode z = findMinimum(s.right);
    copyValue(z,s);
    deleteNode(z);
   }
 }



 /**
 * Copies the key value data from SplayNode z into
 * SplayNode s
 */
 private void copyValue(SplayNode z, SplayNode s) {
   s.key = z.key;
   s.value = z.value;
 }
 
 /**
  * If target has parent, set parent reference that was
  * pointing to target to null
  */
 private void nullifyParent(SplayNode target) {
  if (target.parent != null) {
    if (target == target.parent.left) { target.parent.left = null; }
    else { target.parent.right = null; }
   }
 }

 /**
 * Returns whether or not the specified SplayNode is
 * a leaf in the Splay Tree
 */
 private boolean isLeaf(SplayNode s) {
   if (s == null || s.left != null || 
  s.right != null) { return false; }
   return true;
 }

 /**
 * Returns the minimum element in the subtree rooted at start
 */
 private SplayNode findMinimum(SplayNode start) {
  SplayNode min = start;
  while (min.left != null) {
   min = min.left;
  }
  return min;
 }
 
 /**
 * Returns the minimum element in the subtree rooted at start
 */
 private SplayNode findMaximum(SplayNode start) {
  SplayNode max = start;
  while (max.right != null) {
   max = max.right;
  }
  return max;
 }

 /**
 * SplayNode - building block used to build a Splay Tree.
 * Each SplayNode has a key that acts as an identifier,
 * and the SplayTree is sorted according to the keys. Each
 * of these keys is associated with a value, used to store
 * the data that the key represents
 */
 private class SplayNode {
  private K key;
  private V value;
  private SplayNode left;
  private SplayNode right;
  private SplayNode parent;

  public SplayNode(K key, V value) {
   this.key = key;
   this.value = value;
  }
 }
}
