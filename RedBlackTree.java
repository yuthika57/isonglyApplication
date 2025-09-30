import java.util.LinkedList;
import java.util.Queue;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {

    public RedBlackTree() {
        super();
    }
    
    @Override
    public void insert(T data) {
        if (data == null) {
            throw new NullPointerException("Cannot insert null into RedBlackTree");
        }

        RBTNode<T> newNode = new RBTNode<>(data);
        newNode.isRed = true;

        if (root == null) {
            root = newNode;
            newNode.isRed = false;
            return;
        }

        insertHelper(newNode, (RBTNode<T>) root);
        ensureRedProperty(newNode);
        ((RBTNode<T>) root).isRed = false;
    }

    protected void ensureRedProperty(RBTNode<T> node) {
        if (node.parent() == null) {
            node.isRed = false;
            return;
        }

        if (!node.parent().isRed()) {
            return;
        }

        RBTNode<T> parent = (RBTNode<T>) node.parent();
        RBTNode<T> grandparent = (RBTNode<T>) parent.parent();

        if (grandparent == null) {
            return;
        }

        boolean isParentLeft = (grandparent.childLeft() == parent);
        RBTNode<T> uncle = isParentLeft ? (RBTNode<T>) grandparent.childRight() : (RBTNode<T>) grandparent.childLeft();

        if (uncle != null && uncle.isRed()) {
            parent.flipColor();
            uncle.flipColor();
            grandparent.flipColor();
            ensureRedProperty(grandparent);
            return;
        }

        if (isParentLeft && parent.childRight() == node) { 
            rotate(node, parent);
            node = parent;
            parent = (RBTNode<T>) node.parent();
        } else if (!isParentLeft && parent.childLeft() == node) {
            rotate(node, parent);
            node = parent;
            parent = (RBTNode<T>) node.parent();
        }

        if (isParentLeft) {
            rotate(parent, grandparent);
        } else {
            rotate(parent, grandparent);
        }

        parent.flipColor();
        if (grandparent != null) {
            grandparent.flipColor();
        }

        ((RBTNode<T>) root).isRed = false;
    }

    public String levelOrderTraversal() {
        if (root == null) return "[]";

        StringBuilder sb = new StringBuilder("[");
        Queue<RBTNode<T>> queue = new LinkedList<>();
        queue.add((RBTNode<T>) root);

        while (!queue.isEmpty()) {
            RBTNode<T> current = queue.poll();
            sb.append(current.data).append(current.isRed ? "(r)" : "(b)").append(", ");

            if (current.childLeft() != null) queue.add(current.childLeft());
            if (current.childRight() != null) queue.add(current.childRight());
        }

        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        sb.append("]");

        return sb.toString();
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public static class RedBlackTreeTests {
        private RedBlackTree<String> tree;

        @BeforeAll
        public void setup() {
            tree = new RedBlackTree<>();
            tree.insert("M");
            tree.insert("G");
            tree.insert("U");
            tree.insert("E");
            tree.insert("R");
            tree.insert("X");
        }

        @Test
        public void testInsertF() {
            tree.insert("F");
            String expectedOrder = "[ M(b), F(b), U(b), E(r), G(r), R(r), X(r) ]";
            assertEquals(expectedOrder, tree.root.toLevelOrderString(),
                    "Level-order traversal mismatch after inserting F");
            assertTrue(isValidRedBlackTree(tree), "Tree violates Red-Black properties after insertion");
        }

        @Test
        public void testInsertV() {
            tree.insert("V");
            String expectedOrder = "[ M(b), F(b), U(r), E(r), G(r), R(b), X(b), V(r) ]";
            assertEquals(expectedOrder, tree.root.toLevelOrderString(),
                    "Level-order traversal mismatch after inserting V");
            assertTrue(isValidRedBlackTree(tree), "Tree violates Red-Black properties after insertion");
        }

        @Test
        public void testInsertZ() {
            tree.insert("Z");
            String expectedOrder = "[ M(b), F(b), U(r), E(r), G(r), R(b), X(b), V(r), Z(r) ]";
            assertEquals(expectedOrder, tree.root.toLevelOrderString(),
                    "Level-order traversal mismatch after inserting Z");
            assertTrue(isValidRedBlackTree(tree), "Tree violates Red-Black properties after insertion");
        }

        private boolean isValidRedBlackTree(RedBlackTree<String> tree) {
            return validateRedProperty((RBTNode<String>) tree.root) && validateBlackHeight((RBTNode<String>) tree.root) != -1;
        }

        private boolean validateRedProperty(RBTNode<String> node) {
            if (node == null) return true;
            if (node.isRed && node.parent() != null && node.parent().isRed) return false;
            return validateRedProperty(node.childLeft()) && validateRedProperty(node.childRight());
        }

        private int validateBlackHeight(RBTNode<String> node) {
            if (node == null) return 1;
            int leftHeight = validateBlackHeight(node.childLeft());
            int rightHeight = validateBlackHeight(node.childRight());
            if (leftHeight == -1 || rightHeight == -1 || leftHeight != rightHeight) return -1;
            return leftHeight + (node.isRed ? 0 : 1);
        }
    }
}
