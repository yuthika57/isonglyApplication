/**
 * A generic Binary Search Tree (BST) implementation that stores Comparable elements.
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {
    
    // The root node of the BST
    protected BinaryTreeNode<T> root;
    
    /**
     * Constructs an empty Binary Search Tree.
     */
    public BinarySearchTree() {
        this.root = null;
    }

    /**
     * Inserts a new element into the BST.
     * 
     * @param data The element to insert
     * @throws NullPointerException if the data is null
     */
    @Override
    public void insert(T data) throws NullPointerException {
        if (data == null) throw new NullPointerException("Cannot insert null into BST");
        
        BinaryTreeNode<T> newNode = new BinaryTreeNode<>(data);
        
        // If the tree is empty, set the new node as root
        if (root == null) {
            root = newNode;
            return;
        }
        
        // Helper method to place the node at the correct position
        insertHelper(newNode, root);
    }

    /**
     * Helper method for recursive insertion.
     * 
     * @param newNode The node to insert
     * @param subtree The current subtree being examined
     */
    protected void insertHelper(BinaryTreeNode<T> newNode, BinaryTreeNode<T> subtree) {
        int comparison = newNode.getData().compareTo(subtree.getData());
        
        // If new value is less than or equal to current node's value, go left
        if (comparison <= 0) {
            if (subtree.childLeft() == null) {
                subtree.setChildLeft(newNode);
                newNode.setParent(subtree);
            } else {
                insertHelper(newNode, subtree.childLeft());
            }
        }
        // If new value is greater than current node's value, go right
        else {
            if (subtree.childRight() == null) {
                subtree.setChildRight(newNode);
                newNode.setParent(subtree);
            } else {
                insertHelper(newNode, subtree.childRight());
            }
        }
    }
    /**
     * Checks whether the BST contains a specific element.
     * 
     * @param data The element to search for
     * @return true if the element is found, false otherwise
     */
    @Override
    public boolean contains(Comparable<T> data) {
        if (data == null || root == null) return false;
        return containsHelper(data, root);
    }

    /**
     * Helper method for recursive search.
     * 
     * @param data The element to search for
     * @param current The current subtree being examined
     * @return true if found, false otherwise
     */
    private boolean containsHelper(Comparable<T> data, BinaryTreeNode<T> current) {
        if (current == null) return false;
        
        int comparison = data.compareTo(current.getData());
        if (comparison == 0) return true;
        if (comparison < 0) return containsHelper(data, current.childLeft());
        return containsHelper(data, current.childRight());
    }

    /**
     * Returns the number of elements in the BST.
     * 
     * @return The size of the tree
     */
    @Override
    public int size() {
        return sizeHelper(root);
    }

    /**
     * Helper method to calculate the size of the BST.
     * 
     * @param node The current subtree being examined
     * @return The number of nodes in the subtree
     */
    private int sizeHelper(BinaryTreeNode<T> node) {
        if (node == null) return 0;
        return 1 + sizeHelper(node.childLeft()) + sizeHelper(node.childRight());
    }

    
 // Checks if the tree is empty
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    // Clears the tree by setting root to null
    @Override
    public void clear() {
        root = null;
    }

    // Test inserting integers and checking basic operations
    public boolean test1() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert(10);
        bst.insert(3);
        bst.insert(7);
        bst.insert(1);
        bst.insert(9);
        
        // Validate size and search operations
        boolean test = bst.size() == 5 && bst.contains(10) && bst.contains(1) && bst.contains(9) && !bst.contains(6);
        
        // Clear tree and check emptiness
        bst.clear();
        return test && bst.isEmpty();
    }

    // Test inserting strings and handling duplicates
    public boolean test2() {
        BinarySearchTree<String> bst = new BinarySearchTree<>();
        bst.insert("bunny");
        bst.insert("alligator");
        bst.insert("cat");
        bst.insert("bunny"); // Duplicate insertion
        
        return bst.size() == 4 && bst.contains("bunny") && bst.contains("alligator") && bst.contains("cat") && !bst.contains("deer");
    }

    // Test different insertion orders creating different tree shapes
    public boolean test3() {
        BinarySearchTree<Integer> bst1 = new BinarySearchTree<>();
        bst1.insert(1);
        bst1.insert(2);
        bst1.insert(3); // Right-heavy tree
        
        BinarySearchTree<Integer> bst2 = new BinarySearchTree<>();
        bst2.insert(3);
        bst2.insert(2);
        bst2.insert(1); // Left-heavy tree
        
        return bst1.size() == 3 && bst2.size() == 3 && bst1.contains(2) && bst2.contains(2);
    }

    // Test edge cases including empty tree and single-node operations
    public boolean test4() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        
        // Validate operations on an empty tree
        boolean emptyTests = bst.isEmpty() && bst.size() == 0 && !bst.contains(5);
        
        // Insert a single node and validate
        bst.insert(10);
        boolean singleNodeTests = !bst.isEmpty() && bst.size() == 1 && bst.contains(10) && !bst.contains(5);
        
        // Clear tree and check again
        bst.clear();
        boolean clearTest = bst.isEmpty() && bst.size() == 0 && !bst.contains(10);
        
        return emptyTests && singleNodeTests && clearTest;
    }

    // Test balanced vs unbalanced tree scenarios
    public boolean test5() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        
        // Create a balanced tree
        bst.insert(50);
        bst.insert(25);
        bst.insert(75);
        bst.insert(12);
        bst.insert(37);
        bst.insert(62);
        bst.insert(87);
        
        boolean balancedTest = bst.size() == 7 && bst.contains(12) && bst.contains(87);
        bst.clear();
        
        // Create an unbalanced tree (right-heavy)
        bst.insert(10);
        bst.insert(20);
        bst.insert(30);
        bst.insert(40);
        bst.insert(50);
        
        boolean unbalancedTest = bst.size() == 5 && bst.contains(10) && bst.contains(50);
        
        return balancedTest && unbalancedTest;
    }

    // Test handling of negative numbers and duplicate values
    public boolean test6() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        
        // Insert negative numbers and a duplicate
        bst.insert(-5);
        bst.insert(-3);
        bst.insert(-7);
        bst.insert(-5);
        
        boolean negativeTest = bst.size() == 4 && bst.contains(-5) && bst.contains(-7);
        
        // Insert zero and check presence
        bst.insert(0);
        boolean zeroTest = bst.contains(0) && bst.size() == 5;
        
        return negativeTest && zeroTest;
    }

    // Main method to execute all tests
    public static void main(String[] args) {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        System.out.println("Test 1 (Integers and basic operations): " + (bst.test1() ? "PASSED" : "FAILED"));
        System.out.println("Test 2 (Strings and duplicates): " + (bst.test2() ? "PASSED" : "FAILED"));
        System.out.println("Test 3 (Different tree shapes): " + (bst.test3() ? "PASSED" : "FAILED"));
        System.out.println("Test 4 (Edge cases and empty tree): " + (bst.test4() ? "PASSED" : "FAILED"));
        System.out.println("Test 5 (Balanced vs unbalanced trees): " + (bst.test5() ? "PASSED" : "FAILED"));
        System.out.println("Test 6 (Negative numbers and duplicates): " + (bst.test6() ? "PASSED" : "FAILED"));
    }
}
