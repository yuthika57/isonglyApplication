import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * This class extends RedBlackTree into a tree that supports iterating over the values it
 * stores in sorted, ascending order.
 */
public class IterableRedBlackTree<T extends Comparable<T>>
                extends RedBlackTree<T> implements IterableSortedCollection<T> {

    private Comparable<T> min = null; // Stores the minimum bound for iteration
    private Comparable<T> max = null; // Stores the maximum bound for iteration

    /**
     * Allows setting the start (minimum) value of the iterator. When this method is called,
     * every iterator created after it will use the minimum set by this method until this method
     * is called again to set a new minimum value.
     * @param min the minimum for iterators created for this tree, or null for no minimum
     */
    @Override
    public void setIteratorMin(Comparable<T> min) {
        this.min = min;
        System.out.println("Iterator min set to: " + min);
    }

    /**
     * Allows setting the stop (maximum) value of the iterator. When this method is called,
     * every iterator created after it will use the maximum set by this method until this method
     * is called again to set a new maximum value.
     * @param max the maximum for iterators created for this tree, or null for no maximum
     */
    @Override
    public void setIteratorMax(Comparable<T> max) {
        this.max = max;
        System.out.println("Iterator max set to: " + max);
    }

    /**
     * Returns an iterator over the values stored in this tree. The iterator uses the
     * start (minimum) value set by a previous call to setIteratorMin, and the stop (maximum)
     * value set by a previous call to setIteratorMax. If setIteratorMin has not been called
     * before, or if it was called with a null argument, the iterator uses no minimum value
     * and starts with the lowest value that exists in the tree. If setIteratorMax has not been
     * called before, or if it was called with a null argument, the iterator uses no maximum
     * value and finishes with the highest value that exists in the tree.
     */
    @Override
    public Iterator<T> iterator() {
        return new RBTIterator<>((BinaryTreeNode<T>) root, min, max);
    }

    /**
     * Nested class for Iterator objects created for this tree and returned by the iterator method.
     * This iterator follows an in-order traversal of the tree and returns the values in sorted,
     * ascending order.
     */
    protected static class RBTIterator<R extends Comparable<R>> implements Iterator<R> {

        private final Stack<BinaryTreeNode<R>> stack = new Stack<>(); // Stack for in-order traversal
        private final Comparable<R> min; // Minimum bound for iteration
        private final Comparable<R> max; // Maximum bound for iteration

        /**
         * Constructor for a new iterator if the tree with root as its root node, and
         * min as the start (minimum) value (or null if no start value) and max as the
         * stop (maximum) value (or null if no stop value) of the new iterator.
         * @param root root node of the tree to traverse
         * @param min the minimum value that the iterator will return
         * @param max the maximum value that the iterator will return 
         */
        public RBTIterator(BinaryTreeNode<R> root, Comparable<R> min, Comparable<R> max) {
            this.min = min;
            this.max = max;
            buildStackHelper(root); // Initialize the stack with valid nodes
        }

        /**
         * Helper method for initializing and updating the stack.
         * This method both:
         * - Finds the next data value stored in the tree (or subtree) that is 
         *   between start (minimum) and stop (maximum) point (including start and stop points themselves), and
         * - Builds up the stack of ancestor nodes that contain values between start (minimum) and stop (maximum) values 
         *   (including start and stop values themselves) so that those nodes can be visited in the future.
         * @param node the root node of the subtree to process
         */
        private void buildStackHelper(BinaryTreeNode<R> node) {
          // Base case: stop recursion if node is null
          if (node == null) {
              return;
          }

          if (max != null && node.getData().compareTo((R) max) > 0) {
              // If the current node's value exceeds max, recurse on left subtree
              System.out.println("Ignoring node (exceeds max): " + node.getData());
              buildStackHelper(node.childLeft());
          } else if (min == null || node.getData().compareTo((R) min) >= 0) {
              // If node is within range, push to stack and recurse on left subtree first
              System.out.println("Adding node to stack: " + node.getData());
              stack.push(node);
              buildStackHelper(node.childLeft());
          } else {
              // If node is below min, recurse on right subtree
              System.out.println("Ignoring node (below min): " + node.getData());
              buildStackHelper(node.childRight());
          }
      }


        /**
         * Returns true if the iterator has another value to return, and false otherwise.
         */
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        /**
         * Returns the next value of the iterator.
         * @throws NoSuchElementException if the iterator has no more values to return
         */
        @Override
        public R next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            // Get the next node in in-order traversal
            BinaryTreeNode<R> nextNode = stack.pop();
            R nextValue = nextNode.getData();
            System.out.println("Returning next value: " + nextValue);

            // Process right subtree of the popped node
            buildStackHelper(nextNode.childRight());
            return nextValue;
        }
    }

    /**
     * Tests iteration over the entire tree with no minimum or maximum constraints.
     * Ensures elements are returned in sorted order.
     */
    @Test
    public void testIterationNoMinNoMax() {
        System.out.println("Running testIterationNoMinNoMax");
        IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
        
        // Insert elements into the tree
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(40);
        tree.insert(50);
        
        // Create an iterator for the tree
        Iterator<Integer> iter = tree.iterator();
        
        // Verify iteration order
        assertEquals(10, iter.next());
        assertEquals(20, iter.next());
        assertEquals(30, iter.next());
        assertEquals(40, iter.next());
        assertEquals(50, iter.next());
        
        // Ensure no more elements exist
        assertFalse(iter.hasNext());
    }

    /**
     * Tests iteration with a minimum bound set.
     * Ensures that only elements greater than or equal to the min are returned.
     */
    @Test
    public void testIterationWithMinOnly() {
        System.out.println("Running testIterationWithMinOnly");
        IterableRedBlackTree<String> tree = new IterableRedBlackTree<>();
        
        // Insert elements into the tree
        tree.insert("apple");
        tree.insert("banana");
        tree.insert("cherry");
        tree.insert("date");
        tree.insert("elderberry");
        
        // Set minimum iteration bound
        tree.setIteratorMin("cherry");
        
        // Create an iterator
        Iterator<String> iter = tree.iterator();
        
        // Verify iteration order starting from the minimum
        assertEquals("cherry", iter.next());
        assertEquals("date", iter.next());
        assertEquals("elderberry", iter.next());
        
        // Ensure no more elements exist
        assertFalse(iter.hasNext());
    }

    /**
     * Tests iteration with a maximum bound set.
     * Ensures that only elements less than or equal to the max are returned.
     */
    @Test
    public void testIterationWithMaxOnly() {
        System.out.println("Running testIterationWithMaxOnly");
        IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
        
        // Insert elements into the tree
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(40);
        tree.insert(50);
        
        // Set maximum iteration bound
        tree.setIteratorMax(30);
        
        // Create an iterator
        Iterator<Integer> iter = tree.iterator();
        
        // Verify iteration order up to the maximum
        assertEquals(10, iter.next());
        assertEquals(20, iter.next());
        assertEquals(30, iter.next());
        
        // Ensure no more elements exist
        assertFalse(iter.hasNext());
    }

    /**
     * Tests iteration with duplicate elements.
     * Ensures that duplicate values are correctly handled.
     */
    @Test
    public void testIterationWithDuplicates() {
        System.out.println("Running testIterationWithDuplicates");
        IterableRedBlackTree<String> tree = new IterableRedBlackTree<>();
        
        // Insert elements, including duplicates
        tree.insert("apple");
        tree.insert("banana");
        tree.insert("banana");
        tree.insert("cherry");
        tree.insert("date");
        
        // Create an iterator
        Iterator<String> iter = tree.iterator();
        
        // Verify that duplicates are correctly handled
        assertEquals("apple", iter.next());
        assertEquals("banana", iter.next());
        assertEquals("banana", iter.next());
        assertEquals("cherry", iter.next());
        assertEquals("date", iter.next());
        
        // Ensure no more elements exist
        assertFalse(iter.hasNext());
    }

    /**
     * Tests iteration with both minimum and maximum bounds set.
     * Ensures only elements within the range are returned.
     */
    @Test
    public void testIterationWithMinAndMax() {
        System.out.println("Running testIterationWithMinAndMax");
        IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
        
        // Insert elements into the tree
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(40);
        tree.insert(50);
        
        // Set both minimum and maximum iteration bounds
        tree.setIteratorMin(20);
        tree.setIteratorMax(40);
        
        // Create an iterator
        Iterator<Integer> iter = tree.iterator();
        
        // Verify iteration order within the range
        assertEquals(20, iter.next());
        assertEquals(30, iter.next());
        assertEquals(40, iter.next());
        
        // Ensure no more elements exist
        assertFalse(iter.hasNext());
    }

}