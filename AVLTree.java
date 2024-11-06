import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/*This class was implemented in class*/
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

    /*This class was implemented in class*/

    // Inorder traversal to print the tree
    public void inorderTraversal() {
        inorderTraversalRecursive(root);
        System.out.println(); // New line after traversal
    }

    // Recursive inorder traversal
    private void inorderTraversalRecursive(Node node) {
        if (node != null) {
            inorderTraversalRecursive(node.left);
            System.out.println(node.data + " ");
            inorderTraversalRecursive(node.right);
        }
    }
    
    //Recursive search of words starting with the same prefix
    public void searchForPrefix(String prefix){
        searchRecPrefix(this.root, prefix);
    }

    public void searchRecPrefix(Node root, String prefix){ 
        if(root == null){
            return;
        }

        //Printout words that start with the prefix
        if(root.data.indexOf(prefix) == 0){ //source: https://javarevisited.blogspot.com/2016/10/how-to-check-if-string-contains-another-substring-in-java-indexof-example.html#
            System.out.println(root.data);
        }
        searchRecPrefix(root.left, prefix); //Recursive calls
        searchRecPrefix(root.right, prefix);
    }
    /////////////////////////////////////////////////////////////

    //Recursive search of words with Edit distance smaller than 2
    public void search(String prefix){
        searchRec(this.root, prefix);
    }

    public void searchRec(Node root, String prefix){ 
        if(root == null){
            return;
        }

        //Printout words that have edit distance at most 2
        if((levenshteinDist(root.data.length(), prefix.length(), root.data, prefix) > 0) && (levenshteinDist(root.data.length(), prefix.length(), root.data, prefix) < 2)){
            System.out.println(root.data); 
        }

        searchRec(root.left, prefix);
        searchRec(root.right, prefix);
    }
    /////////////////////////////////////////////////////////////

    public static int levenshteinDist(int i, int j, String s1, String s2){ //https://www.youtube.com/watch?v=Ay9V69E18Awx, https://www.youtube.com/watch?v=fJaKO8FbDdo

        if(i == 0){ //if one string is exhausted, take the other one
            return j; 
        }

        if(j == 0){ //if one string is exhausted, take the other one
            return i; 
        }

        if(s1.charAt(i - 1) == s2.charAt(j - 1)){ //strings are the same
            return levenshteinDist(i - 1, j - 1, s1, s2);
        }
        
        return 1 + Math.min( //return minimum of operations
        //Insert
        levenshteinDist(i-1, j, s1, s2),
        Math.min(
        //Delete
        levenshteinDist(i, j-1, s1, s2),
        //Replace
        levenshteinDist(i - 1, j - 1, s1, s2)));

    }

    // Main method to demonstrate AVL Tree usage
    public static void main(String[] args) {
        AVLTree avlTree = new AVLTree(); //build new AVL tree
        Scanner scan = new Scanner(System.in); //scanner for keyboard input

        try{
            BufferedReader reader = new BufferedReader(new FileReader("midfile.txt")); //load the file
            String word;
        while((word = reader.readLine()) != null){
            avlTree.insert(word);
        }
        
        System.out.println("To get Prefix-based suggestions, type in prefix: ");
        String strPrefix = scan.nextLine().toLowerCase(); //make the whole string lower case

        System.out.println("Words suggested based on prefix: ");
        avlTree.searchForPrefix(strPrefix); //print suggested words based on prefix

        System.out.println("Misspelled word suggestions: ");
        avlTree.search(strPrefix); //print words based on edit distance

        }catch(FileNotFoundException e){
            System.out.println("File not found");
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}