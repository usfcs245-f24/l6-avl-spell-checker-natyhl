public class AVLTree{
    // Node class for the AVL Tree
    private class Node {
        String data;
        Node left;
        Node right;
        int height;

        Node(String data) {
            this.data = data;
            this.height = 1;
        }
    }

    // Root of the AVL Tree
    private Node root;

    // Get the height of a node
    private int height(Node node) {
        // If node is null, return 0 otherwise return the height of the node 
        if(node == null){
            return 0;
        }
        //return height(node);
        return node.height;
    }

    // Update height of a node
    private void updateHeight(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    // Get balance factor of a node
    private int getBalanceFactor(Node node) {
        // If node is null, return 0
        // else return the height diff between L and R nodes 
        if(node == null){
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    // Right rotation
    /* Before Rotation:
     *        30 = y
     *       /
     *     20 = x
     *    /  \
     *   10   25 = T2
     * 
     * After Rotation:
     *     20
     *    /  \
     *   10   30
     *       /
     *      25
     */

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // Left rotation
    /* Before Rotation:
     *    20 = x
     *   /  \
     *  10  30 = y
     *     /  \
     *    T2  40
     * 
     * After Rotation:
     *     30 = y
     *    /  \
     *   20=x 40
     *  / \
     * 10  T2
     */
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // Insert a value into the AVL tree
    public void insert(String s) {
        root = insertRecursive(root, s);
    }

    // Recursive insert method
    private Node insertRecursive(Node node, String s) {
        // 1. Perform standard BST insertion
        if (node == null) {
            return new Node(s);
        }

        if (s.compareTo(node.data) < 0) {
            node.left = insertRecursive(node.left, s);
        } else if (s.compareTo(node.data) > 0) {
            node.right = insertRecursive(node.right, s);
        } else {
            // Duplicate values are not allowed
            return node;
        }

        // 2. Update height of current node
        updateHeight(node);

        // 3. Get the balance factor
        int balance = getBalanceFactor(node);

        // 4. Perform rotations if needed (4 cases)

        // Left Left Case
        /* Before Rotation:
         *        30           
         *       /  \          
         *     20   40         
         *    /              
         *   10              
         * 
         * After Right Rotation:
         *       20
         *      /  \
         *    10   30
         *         / \
         *       25  40
        */
        if (balance > 1 && s.compareTo(node.left.data) < 0) {
            return rotateLeft(node);
        }

        // Right Right Case
        /* Before Rotation:
         *     20
         *    /  \
         *   10  30
         *        \
         *        40
         * 
         * After Left Rotation:
         *       30
         *      /  \
         *    20   40
         *   /  \
         * 10   25
         */

        if (balance < -1 && s.compareTo(node.right.data) > 0) {
            return rotateLeft(node);
        }

        // Left Right Case
        /* Before Rotation:
         *        30
         *       /  \
         *     10   40
         *      \
         *      20
         * 
         * After Double Rotation (Left then Right):
         *       20
         *      /  \
         *    10   30
         *         / \
         *       25  40
         */
        if (balance > 1 && s.compareTo(node.left.data) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
            
        }

        // Right Left Case
        /*
         * Before Rotation:
         *     20
         *    /  \
         *   10  40
         *       /
         *      30
         * 
         * After Double Rotation (Right then Left):
         *       30
         *      /  \
         *    20   40
         *   /  \
         * 10   25
         */
        if (balance < -1 && s.compareTo(node.right.data) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // Inorder traversal to print the tree
    public void inorderTraversal() {
        inorderTraversalRecursive(root);
        System.out.println(); // New line after traversal
    }

    // Recursive inorder traversal
    private void inorderTraversalRecursive(Node node) {
        if (node != null) {
            inorderTraversalRecursive(node.left);
            System.out.print(node.data + " ");
            inorderTraversalRecursive(node.right);
        }
    }

    // Main method to demonstrate AVL Tree usage
    public static void main(String[] args) {
        AVLTree avlTree = new AVLTree();

        // Insert some values
        String[] values = {"apple", "banana", "lemon", "kiwi", "avocado"};
        for (String s : values) {
            avlTree.insert(s);
        }

        // Print inorder traversal
        System.out.println("Inorder traversal:");
        avlTree.inorderTraversal();
    }
}