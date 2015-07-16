/**
* Interface detailing common operations on binary search trees
*/
public interface MambaTree<K extends Comparable<K>,V extends Comparable<V>> {

  /**
  * Insert key,value pair into the tree, replacing old value
  * with given value if key already exists
  */
  public void insert(K key, V value);

  /**
  * Retrieve value associated with given key in tree
  */
  public V find(K key);

  /**
  * Delete key,value pair from tree whose key matches given key
  */
  public void delete(K key);

  /**
  * Determine if tree is empty
  */
  public boolean isEmpty();

  /* Retrieval methods */

  public K getKey();
  public V getValue();

  public MambaTree getParent();
  public MambaTree getRight();
  public MambaTree getLeft(); 

}