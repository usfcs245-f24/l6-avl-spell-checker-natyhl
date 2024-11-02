import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

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
            return rotateRight(node);
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

    public Node search(String prefix){
        return searchRec(this.root, prefix);
    }

    public Node searchRec(Node root, String prefix){
        if(root == null){
            return null;
        }

        if(root.data.indexOf(prefix) == 0){ //source: https://javarevisited.blogspot.com/2016/10/how-to-check-if-string-contains-another-substring-in-java-indexof-example.html#
            System.out.println(root.data);
        }

        if (root.data.compareTo(prefix) > 0) { //root is bigger than data //search in the left subtree
            return searchRec(root.left, prefix);
        } else if (root.data.compareTo(prefix) < 0) { //search in the right subtree
            return searchRec(root.right, prefix);
        } else { //root is the data
            return root; //word found
        }
    }

    // In-order traversal
    // void inOrderSearch(String word) {
    //     inOrderRecSearch(root, word);
    // }

    // void inOrderRecSearch(Node root, String word) {
    //     if (root != null) {
    //         inOrderRecSearch(root.left, word);
    //         if((root.data.length() == word.length()) || (root.data.length() == word.length() + 1) || (root.data.length() == word.length() - 1)){ //satisfies condition
    //             String w = root.data.toLowerCase();
    //             if(w.charAt(0) == word.charAt(0) && w.charAt(1) == word.charAt(1)){ //first two letters are the same
    //                 System.out.print(root.data + " "+"\n");
    //             }
    //         inOrderRecSearch(root.right, word);
    //         }
    //     }
    // }

    // Main method to demonstrate AVL Tree usage
    public static void main(String[] args) {
        AVLTree avlTree = new AVLTree(); //build new AVL tree
        Scanner scan = new Scanner(System.in); //scanner for keyboard input

        try{
            BufferedReader reader = new BufferedReader(new FileReader("smalllist.txt")); //load the file
            String word;
        while((word = reader.readLine()) != null){
            avlTree.insert(word);
        }
        
        System.out.println("To get Prefix-based suggestions, type in prefix: ");
        String strPrefix = scan.nextLine().toLowerCase(); //make the whole string lower case

        avlTree.search(strPrefix); //print suggested words based on prefix

        }catch(FileNotFoundException e){
            System.out.println("File not found");
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

    }
}