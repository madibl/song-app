// == CS400 Spring 2024 File Header Information ==
// Name: Madison Lin
// Email: mblin@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader:
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * This is a class that keeps a RedBlackTree.
 */
public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T>{
   /**
    * This class creates nodes for the RBT trees
    */
    protected static class RBTNode<T> extends Node<T> {
        public boolean isBlack = false;
        public RBTNode(T data) { super(data); }
        public RBTNode<T> getUp() { return (RBTNode<T>)this.up; }
        public RBTNode<T> getDownLeft() { return (RBTNode<T>)this.down[0]; }
        public RBTNode<T> getDownRight() { return (RBTNode<T>)this.down[1]; }
    }

    protected void enforceRBTreePropertiesAfterInsert(RBTNode<T> node) {
    	
    	if (node == null) {
    		// System.out.println("Null node");
    		return;
    	}
    	// base case, node is root -> color black, return
    	if (node.getUp() == null) {
    		// System.out.println("Root node");
    		node.isBlack = true;
    		return;
    	}
    	// base case, parent is black -> return
    	if (node.getUp().isBlack == true) {
    		// System.out.println("Black parent");
    		return;
    	}
    	
    	// if parent is left child then aunt is right child of grandparent - check if aunt is red
    	if (node.getUp() == node.getUp().getUp().getDownLeft()) {
    		RBTNode<T> aunt = node.getUp().getUp().getDownRight();
        	// recursive case 1 - red aunts handling
    		if (aunt != null && !aunt.isBlack) {
    			// System.out.println("Red Aunt");
	    		// swap colors between grand and parent
	    		node.getUp().isBlack = true;
	    		aunt.isBlack = true;
	    		node.getUp().getUp().isBlack = false;
	    		
	    		enforceRBTreePropertiesAfterInsert(node.getUp().getUp());
    		}
    		
        	// recursive case 2, 2.5, and 3 - black aunts handling
    		// if aunt is black
    		else {
        		// handle if zig case
        		if (node == node.getUp().getDownRight()) {
        			// rotate child and parent
        			this.rotate(node, node.getUp());
        			// rotate and swap colors between grand and parent (parent is now the node)
        			node = node.getDownLeft();
        		}
	        		// rotate and swap colors between grand and parent
        		node.getUp().isBlack = !node.getUp().isBlack;
        		node.getUp().getUp().isBlack = !node.getUp().getUp().isBlack;
	        	this.rotate(node.getUp(), node.getUp().getUp());

        	}
    	}

    	// now for if parent is right child and aunt is left child of grandparent
    	else {
    		RBTNode<T> aunt = node.getUp().getUp().getDownLeft();
        	// recursive case 1 - red aunts handling
    		if (aunt != null && !aunt.isBlack) {
    			// System.out.println("Red Aunt");
	    		// swap colors between grand and parent
	    		node.getUp().isBlack = true;
	    		aunt.isBlack = true;
	    		node.getUp().getUp().isBlack = false;
	    		
	    		enforceRBTreePropertiesAfterInsert(node.getUp().getUp());
    		}
    		
        	// recursive case 2, 2.5, and 3 - black aunts handling
    		// if aunt is black
    		else {
        		// handle if zig case
        		if (node == node.getUp().getDownLeft()) {
        			// rotate child and parent
        			this.rotate(node, node.getUp());
        			// rotate and swap colors between grand and parent (parent is now the node)
        			node = node.getDownRight();
        			
        			
        		} 
	        	// rotate and swap colors between grand and parent
        		node.getUp().isBlack = !node.getUp().isBlack;
        		node.getUp().getUp().isBlack = !node.getUp().getUp().isBlack;
	        	this.rotate(node.getUp(), node.getUp().getUp());
        	}
    	}	
    		
    }
    
    /**
     * This method inserts a RBT node into this RBT tree.
     * 
     * @param data - the data of the node
     */
    @Override
    public boolean insert(T data) throws NullPointerException {
        if (data == null)
            throw new NullPointerException("Cannot insert data value null into the tree.");
        RBTNode<T> node = new RBTNode<T>(data);
        boolean toReturn = insertHelper(node);
        enforceRBTreePropertiesAfterInsert(node);
        RBTNode<T> root = (RBTNode<T>) this.root;
        root.isBlack = true;
    
        return toReturn;
    }

    // JUNIT TESTS

    // test 1 - red aunts (case 1)
    /**
     * This method tests inserting into a RBT when it has a red aunt (case 1)
     */
    @Test
    public void testRedAunts(){

        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
    	tree.insert(5);
    	tree.insert(2);
    	tree.insert(9);
    	tree.insert(1);
    	tree.insert(3);
    	tree.insert(6);
    	tree.insert(10);
        tree.insert(7);


    	
    	String actual = tree.toLevelOrderString();
    	String expected = "[ 5, 2, 9, 1, 3, 6, 10, 7 ]";



        RBTNode<Integer> inserted = (RBTNode<Integer>) tree.findNode(7);
        RBTNode<Integer> parent = inserted.getUp();
        RBTNode<Integer> grandparent = inserted.getUp().getUp();
        RBTNode<Integer> aunt = grandparent.getDownRight();

        Assertions.assertEquals(expected, actual);
        Assertions.assertFalse(inserted.isBlack);
        Assertions.assertFalse(grandparent.isBlack);
        Assertions.assertTrue(parent.isBlack);
        Assertions.assertTrue(aunt.isBlack);
        


    }
    
    // test 1 - red aunts (case 1) - SIMPLE
    /**
     * This method tests inserting into a SIMPLE RBT when it has a red aunt (case 1)
     */
    @Test
    public void testRedAuntsSimple(){

        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
    	tree.insert(5);
    	tree.insert(2);
    	tree.insert(9);
    	tree.insert(1);


    	
    	String actual = tree.toLevelOrderString();
    	String expected = "[ 5, 2, 9, 1 ]";



        RBTNode<Integer> inserted = (RBTNode<Integer>) tree.findNode(1);
        RBTNode<Integer> parent = inserted.getUp();
        RBTNode<Integer> grandparent = parent.getUp();
        RBTNode<Integer> aunt = grandparent.getDownRight();

        System.out.println();
        Assertions.assertEquals(expected, actual);
        Assertions.assertFalse(inserted.isBlack);
        Assertions.assertTrue(grandparent.isBlack);
        Assertions.assertTrue(parent.isBlack);
        Assertions.assertTrue(aunt.isBlack);
        


    }

    // test 2 - black aunts (case 2/2.5)
    /**
     * This method tests inserting into a RBT when the aunt is black
     */
    @Test
    public void testBlackAunts() {
        {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
    	tree.insert(4);
    	tree.insert(2);
    	tree.insert(7); 
    	tree.insert(5); // red aunt
    	tree.insert(6); // zag null
        tree.insert(9); // red aunt
    	tree.insert(10); // null aunt
        tree.insert(11); // red aunt -> black aunt


    	
    	String actual = tree.toLevelOrderString();
    	String expected = "[ 6, 4, 9, 2, 5, 7, 10, 11 ]";



        RBTNode<Integer> inserted = (RBTNode<Integer>) tree.findNode(11);
        RBTNode<Integer> parent = inserted.getUp();
        RBTNode<Integer> grandparent = inserted.getUp().getUp();
        RBTNode<Integer> aunt = grandparent.getDownRight();


//        Assertions.assertFalse(inserted.isBlack);
//        Assertions.assertFalse(grandparent.isBlack);
//        Assertions.assertTrue(parent.isBlack);
//        Assertions.assertTrue(aunt.isBlack);
        Assertions.assertEquals(expected, actual);
        }


    }
    // test 3 - null aunts
    /**
     * This method tests inserting into a BST when there is a null aunt
     */
    @Test
    public void testNullAunts() {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
    	tree.insert(2);
    	tree.insert(1);
    	tree.insert(4);
    	tree.insert(5);
    	tree.insert(6);

    	
    	String actual = tree.toLevelOrderString();
    	String expected = "[ 2, 1, 5, 4, 6 ]";



        RBTNode<Integer> inserted = (RBTNode<Integer>) tree.findNode(6);
        RBTNode<Integer> parent = inserted.getUp();
        RBTNode<Integer> grandparent = inserted.getUp().getUp();
        RBTNode<Integer> aunt = grandparent.getDownRight();
        RBTNode<Integer> sibling = parent.getDownLeft();

        Assertions.assertFalse(inserted.isBlack);
        Assertions.assertFalse(sibling.isBlack);
        Assertions.assertTrue(grandparent.isBlack);
        Assertions.assertTrue(parent.isBlack);
        Assertions.assertTrue(aunt.isBlack);
        Assertions.assertEquals(expected, actual);

    }
}
