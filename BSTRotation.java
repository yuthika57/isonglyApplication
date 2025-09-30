/**
 * A Binary Search Tree implementation that supports rotation operations.
 * This class extends BinarySearchTree_Placeholder and adds the ability to perform
 * left and right rotations on nodes within the tree.
 *
 * @param <T> The type of elements stored in the tree, must implement Comparable
 */
public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {
    
    /**
     * Default constructor that creates an empty BST with rotation capability
     */
    public BSTRotation() {
        super();
    }

    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a right
     * child of the provided parent, this method will perform a left rotation.
     * When the provided nodes are not related in one of these ways, this
     * method will either throw a NullPointerException: when either reference is
     * null, or otherwise will throw an IllegalArgumentException.
     *
     * @param child is the node being rotated from child to parent position 
     * @param parent is the node being rotated from parent to child position
     * @throws NullPointerException when either passed argument is null
     * @throws IllegalArgumentException when the provided child and parent
     *     nodes are not initially (pre-rotation) related that way
     */
    
    protected void rotate(BinaryTreeNode<T> child, BinaryTreeNode<T> parent) 
        throws NullPointerException, IllegalArgumentException {
        
        if (child == null || parent == null) {
            throw new NullPointerException("Child and parent nodes must not be null.");
        }

        if (child.parent() != parent) {
            throw new IllegalArgumentException("The provided nodes are not directly connected as parent and child.");
        }

        // Get grandparent
        BinaryTreeNode<T> grandparent = parent.parent();
        boolean isParentLeftChild = (grandparent != null && grandparent.childLeft() == parent);

        // Left Rotation (Right Child Case)
        if (parent.childRight() == child) {  
            BinaryTreeNode<T> childLeft = child.childLeft();
            parent.setChildRight(childLeft);
            if (childLeft != null) childLeft.setParent(parent);
            child.setChildLeft(parent);
        } 
        // Right Rotation (Left Child Case)
        else if (parent.childLeft() == child) {  
            BinaryTreeNode<T> childRight = child.childRight();
            parent.setChildLeft(childRight);
            if (childRight != null) childRight.setParent(parent);
            child.setChildRight(parent);
        } 
        // Invalid rotation case
        else {
            throw new IllegalArgumentException("The provided nodes are not directly connected as parent and child.");
        }

        // Update the grandparent's reference
        child.setParent(grandparent);
        parent.setParent(child);

        if (grandparent == null) {
            root = child;  // New root
        } else if (isParentLeftChild) {
            grandparent.setChildLeft(child);
        } else {
            grandparent.setChildRight(child);
        }
    }

    //Test Methods

    /**
     * Test 1: Basic right rotation
     */
    public boolean test1() {
        BinaryTreeNode<T> parent = new BinaryTreeNode<>((T) Integer.valueOf(30));
        BinaryTreeNode<T> child = new BinaryTreeNode<>((T) Integer.valueOf(20));
        root = parent;

        parent.setChildLeft(child);
        child.setParent(parent);  // Set parent-child relationship

        rotate(child, parent);

        return root == child &&
               child.childRight() == parent &&
               parent.childLeft() == null;
    }

    /**
     * Test 2: Basic left rotation
     */
    public boolean test2() {
        BinaryTreeNode<T> parent = new BinaryTreeNode<>((T) Integer.valueOf(20));
        BinaryTreeNode<T> child = new BinaryTreeNode<>((T) Integer.valueOf(30));
        root = parent;

        parent.setChildRight(child);
        child.setParent(parent);  // Set parent-child relationship

        rotate(child, parent);

        return root == child &&
               child.childLeft() == parent &&
               parent.childRight() == null;
    }

    /**
     * Test 3: Complex right rotation
     */
    public boolean test3() {
        boolean allTestsPassed = true;

        // CASE 1: 0 shared children (Only Parent and Child)
        BinaryTreeNode<T> parent0 = new BinaryTreeNode<>((T) Integer.valueOf(30));
        BinaryTreeNode<T> child0 = new BinaryTreeNode<>((T) Integer.valueOf(20));

        root = parent0;
        parent0.setChildLeft(child0);
        child0.setParent(parent0);

        System.out.println("\n=== CASE 1: Before Rotation (Level-Order) ===");
        System.out.println(toLevelOrderString(root));

        rotate(child0, parent0);

        System.out.println("=== CASE 1: After Rotation (Level-Order) ===");
        System.out.println(toLevelOrderString(root));

        if (!(root == child0 && child0.childRight() == parent0 && parent0.childLeft() == null)) {
            System.out.println("Test 3 CASE 1 FAILED!");
            allTestsPassed = false;
        }

        // CASE 2: 1 shared child (Child has one extra right child) 
        BinaryTreeNode<T> parent1 = new BinaryTreeNode<>((T) Integer.valueOf(30));
        BinaryTreeNode<T> child1 = new BinaryTreeNode<>((T) Integer.valueOf(20));
        BinaryTreeNode<T> grandchild1 = new BinaryTreeNode<>((T) Integer.valueOf(25));

        root = parent1;
        parent1.setChildLeft(child1);
        child1.setParent(parent1);
        child1.setChildRight(grandchild1);
        grandchild1.setParent(child1);

        System.out.println("\n=== CASE 2: Before Rotation (Level-Order) ===");
        System.out.println(toLevelOrderString(root));

        rotate(child1, parent1);

        System.out.println("=== CASE 2: After Rotation (Level-Order) ===");
        System.out.println(toLevelOrderString(root));

        if (!(root == child1 && child1.childRight() == parent1 && parent1.childLeft() == grandchild1)) {
            System.out.println("Test 3 CASE 2 FAILED!");
            allTestsPassed = false;
        }

        return allTestsPassed;
    }

    /**
     * Main method to run all test cases
     */
    public static void main(String[] args) {
        BSTRotation<Integer> bst = new BSTRotation<>();
        
        System.out.println("Test 1 (Basic right rotation): " + 
                          (bst.test1() ? "PASSED" : "FAILED"));
        System.out.println("Test 2 (Basic left rotation): " + 
                          (bst.test2() ? "PASSED" : "FAILED"));
        System.out.println("Test 3 (Complex rotation): " + 
                          (bst.test3() ? "PASSED" : "FAILED"));
    }

    /**
     * Returns a level-order traversal as a string.
     */
    private String toLevelOrderString(BinaryTreeNode<T> root) {
        if (root == null) return "[]";

        int height = getHeight(root);
        StringBuilder sb = new StringBuilder("[");

        for (int i = 1; i <= height; i++) {
            appendLevel(root, i, sb);
        }

        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns the height of the tree.
     */
    private int getHeight(BinaryTreeNode<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(getHeight(node.childLeft()), getHeight(node.childRight()));
    }

    /**
     * Appends all nodes at a given level to the StringBuilder.
     */
    private void appendLevel(BinaryTreeNode<T> node, int level, StringBuilder sb) {
        if (node == null) return;
        if (level == 1) {
            sb.append(node.getData()).append(", ");
        } else {
            appendLevel(node.childLeft(), level - 1, sb);
            appendLevel(node.childRight(), level - 1, sb);
        }
    }
}

