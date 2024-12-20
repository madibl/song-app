// == CS400 Spring 2024 File Header Information ==
// Name: Madison Lin
// Email: mblin@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader:
import java.util.LinkedList;
import java.util.Stack;

/**
 * Binary Search Tree implementation with a Node inner class for representing
 * the nodes of the tree. We will turn this Binary Search Tree into a
 * self-balancing tree as part of project 1 by modifying its insert
 * functionality. In week 0 of project 1, we will start this process by
 * implementing tree rotations.
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollectionInterface<T> {

	/**
	 * This class represents a node holding a single value within a binary tree.
	 */
	protected static class Node<T> {
		public T data;

		// up stores a reference to the node's parent
		public Node<T> up;
		// The down array stores references to the node's children:
		// - down[0] is the left child reference of the node,
		// - down[1] is the right child reference of the node.
		// The @SupressWarning("unchecked") annotation is use to supress an unchecked
		// cast warning. Java only allows us to instantiate arrays without generic
		// type parameters, so we use this cast here to avoid future casts of the
		// node type's data field.
		@SuppressWarnings("unchecked")
		public Node<T>[] down = (Node<T>[]) new Node[2];

		public Node(T data) {
			this.data = data;
		}

		/**
		 * @return true when this node has a parent and is the right child of that
		 *         parent, otherwise return false
		 */
		public boolean isRightChild() {
			return this.up != null && this.up.down[1] == this;
		}

	}

	protected Node<T> root; // reference to root node of tree, null when empty
	protected int size = 0; // the number of values in the tree

	/**
	 * Inserts a new data value into the tree. This tree will not hold null
	 * references, nor duplicate data values.
	 * 
	 * @param data to be added into this binary search tree
	 * @return true if the value was inserted, false if is was in the tree already
	 * @throws NullPointerException when the provided data argument is null
	 */
	public boolean insert(T data) throws NullPointerException {
		if (data == null)
			throw new NullPointerException("Cannot insert data value null into the tree.");
		return this.insertHelper(new Node<>(data));
	}

	/**
	 * Performs a naive insertion into a binary search tree: adding the new node in
	 * a leaf position within the tree. After this insertion, no attempt is made to
	 * restructure or balance the tree.
	 * 
	 * @param node the new node to be inserted
	 * @return true if the value was inserted, false if is was in the tree already
	 * @throws NullPointerException when the provided node is null
	 */
	protected boolean insertHelper(Node<T> newNode) throws NullPointerException {
		if (newNode == null)
			throw new NullPointerException("new node cannot be null");

		if (this.root == null) {
			// add first node to an empty tree
			root = newNode;
			size++;
			return true;
		} else {
			// insert into subtree
			Node<T> current = this.root;
			while (true) {
				int compare = newNode.data.compareTo(current.data);
				if (compare == 0) {
					return false;
				} else if (compare < 0) {
					// insert in left subtree
					if (current.down[0] == null) {
						// empty space to insert into
						current.down[0] = newNode;
						newNode.up = current;
						this.size++;
						return true;
					} else {
						// no empty space, keep moving down the tree
						current = current.down[0];
					}
				} else {
					// insert in right subtree
					if (current.down[1] == null) {
						// empty space to insert into
						current.down[1] = newNode;
						newNode.up = current;
						this.size++;
						return true;
					} else {
						// no empty space, keep moving down the tree
						current = current.down[1];
					}
				}
			}
		}
	}

	/**
	 * Performs the rotation operation on the provided nodes within this tree. When
	 * the provided child is a left child of the provided parent, this method will
	 * perform a right rotation. When the provided child is a right child of the
	 * provided parent, this method will perform a left rotation. When the provided
	 * nodes are not related in one of these ways, this method will throw an
	 * IllegalArgumentException.
	 * 
	 * @param child  is the node being rotated from child to parent position
	 *               (between these two node arguments)
	 * @param parent is the node being rotated from parent to child position
	 *               (between these two node arguments)
	 * @throws IllegalArgumentException when the provided child and parent node
	 *                                  references are not initially (pre-rotation)
	 *                                  related that way
	 */
	protected void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {
		// throw exception if it either child or parent are null
		if (parent == null) {
			throw new NullPointerException("parent is null");
		}
		if (child == null) {
			throw new NullPointerException("child is null");
		}
		if (child.up != parent) {
			throw new IllegalArgumentException("child is not a child of parent");
		}

		// if is at the root, set child to root
		if (parent.up == null) {
			this.root = child;
			child.up = null;
		}
		// if it is in the middle or end
		else if (parent.up.down[0] == parent) {
				parent.up.down[0] = child;
				child.up = parent.up;

		} else {
			// if parent is right of its parent, set child to that right
			parent.up.down[1] = child;
			child.up = parent.up;

		}

		// for right rotation - if child is left of parent
		if (parent.down[0] == child) {
			// move right of child to left of parent
			parent.down[0] = child.down[1];
			if (child.down[1] != null) {
				child.down[1].up = parent;
			}
			// move parent to right of child
			parent.up = child;
			child.down[1] = parent;
		}
		// for left rotation
		else {
			// move left of child to right of parent
			parent.down[1] = child.down[0];
			if (child.down[0] != null) {
				child.down[0].up = parent;
			}
			// move parent to right of child
			parent.up = child;
			child.down[0] = parent;

		}

	}

	/**
	 * Get the size of the tree (its number of nodes).
	 * 
	 * @return the number of nodes in the tree
	 */
	public int size() {
		return size;
	}

	/**
	 * Method to check if the tree is empty (does not contain any node).
	 * 
	 * @return true of this.size() returns 0, false if this.size() != 0
	 */
	public boolean isEmpty() {
		return this.size() == 0;
	}

	/**
	 * Checks whether the tree contains the value *data*.
	 * 
	 * @param data a comparable for the data value to check for
	 * @return true if *data* is in the tree, false if it is not in the tree
	 */
	public boolean contains(Comparable<T> data) {
		// null references will not be stored within this tree
		if (data == null) {
			throw new NullPointerException("This tree cannot store null references.");
		} else {
			Node<T> nodeWithData = this.findNode(data);
			// return false if the node is null, true otherwise
			return (nodeWithData != null);
		}
	}

	/**
	 * Removes all keys from the tree.
	 */
	public void clear() {
		this.root = null;
		this.size = 0;
	}

	/**
	 * Helper method that will return the node in the tree that contains a specific
	 * key. Returns null if there is no node that contains the key.
	 * 
	 * @param data the data value for which we want to find the node that contains
	 *             it
	 * @return the node that contains the data value or null if there is no such
	 *         node
	 */
	protected Node<T> findNode(Comparable<T> data) {
		Node<T> current = this.root;
		while (current != null) {
			int compare = data.compareTo(current.data);
			if (compare == 0) {
				// we found our value
				return current;
			} else if (compare < 0) {
				if (current.down[0] == null) {
					// we have hit a null node and did not find our node
					return null;
				}
				// keep looking in the left subtree
				current = current.down[0];
			} else {
				if (current.down[1] == null) {
					// we have hit a null node and did not find our node
					return null;
				}
				// keep looking in the right subtree
				current = current.down[1];
			}
		}
		return null;
	}

	/**
	 * This method performs an inorder traversal of the tree. The string
	 * representations of each data value within this tree are assembled into a
	 * comma separated string within brackets (similar to many implementations of
	 * java.util.Collection, like java.util.ArrayList, LinkedList, etc).
	 * 
	 * @return string containing the ordered values of this tree (in-order
	 *         traversal)
	 */
	public String toInOrderString() {
		// generate a string of all values of the tree in (ordered) in-order
		// traversal sequence
		StringBuffer sb = new StringBuffer();
		sb.append("[ ");
		int nodesVisited = 0;
		if (this.root != null) {
			Stack<Node<T>> nodeStack = new Stack<>();
			Node<T> current = this.root;
			while (!nodeStack.isEmpty() || current != null) {
				if (current == null) {
					Node<T> popped = nodeStack.pop();
					if (++nodesVisited > this.size()) {
						throw new RuntimeException(
								"visited more nodes during traversal than there are keys in the tree; make sure there is no loop in the tree structure");
					}
					sb.append(popped.data.toString());
					if (!nodeStack.isEmpty() || popped.down[1] != null)
						sb.append(", ");
					current = popped.down[1];
				} else {
					nodeStack.add(current);
					current = current.down[0];
				}
			}
		}
		sb.append(" ]");
		return sb.toString();
	}

	/**
	 * This method performs a level order traversal of the tree. The string
	 * representations of each data value within this tree are assembled into a
	 * comma separated string within brackets (similar to many implementations of
	 * java.util.Collection). This method will be helpful as a helper for the
	 * debugging and testing of your rotation implementation.
	 * 
	 * @return string containing the values of this tree in level order
	 */
	public String toLevelOrderString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[ ");
		int nodesVisited = 0;
		if (this.root != null) {
			LinkedList<Node<T>> q = new LinkedList<>();
			q.add(this.root);
			while (!q.isEmpty()) {
				Node<T> next = q.removeFirst();
				if (++nodesVisited > this.size()) {
					throw new RuntimeException(
							"visited more nodes during traversal than there are keys in the tree; make sure there is no loop in the tree structure");
				}
				if (next.down[0] != null)
					q.add(next.down[0]);
				if (next.down[1] != null)
					q.add(next.down[1]);
				sb.append(next.data.toString());
				if (!q.isEmpty())
					sb.append(", ");
			}
		}
		sb.append(" ]");
		return sb.toString();
	}

	public String toString() {
		return "level order: " + this.toLevelOrderString() + "\nin order: " + this.toInOrderString();
	}

	// Implement at least 3 tests using the methods below. You can
	// use your notes from lecture for ideas of rotation examples to test with.
	// Make sure to include rotations at the root of a tree in your test cases.
	// Give each of the methods a meaningful header comment that describes what is
	// being
	// tested and make sure your tests have inline comments that help with reading
	// your test code.
	// If you'd like to add additional tests, then name those methods similar to the
	// ones given below.
	// Eg: public static boolean test4() {}
	// Do not change the method name or return type of the existing tests.
	// You can run your tests through the static main method of this class.

	/**
	 * This method tests if the rotate method correctly rotates at the ROOT.
	 * 
	 * @return true if it correctly rotates the tree and orders the rest of the tree
	 *         properly
	 */
	public static boolean test1() {
		// create BST
		boolean testLeftRotation = false;
		boolean testRightRotation = false;

		{
			// test left rotation
			BinarySearchTree<Integer> testBST = new BinarySearchTree<Integer>();
			testBST.insert(5);
			testBST.insert(3);
			testBST.insert(9);
			testBST.insert(7);
			testBST.insert(11);
			testBST.insert(12);
			// rotate it
			testBST.rotate(testBST.findNode(9), testBST.findNode(5));

			// compare strings
			String actual = testBST.toLevelOrderString();
			String expected = "[ 9, 5, 11, 3, 7, 12 ]";

			System.out.println(actual);
			if (actual.equals(expected)) {
				testLeftRotation = true;
			}
		}

		{
			// test right rotation
			BinarySearchTree<Integer> testBST = new BinarySearchTree<Integer>();

			testBST.insert(5);
			testBST.insert(3);
			testBST.insert(9);
			testBST.insert(1);
			testBST.insert(4);
			testBST.insert(11);

			// rotate it
			testBST.rotate(testBST.findNode(3), testBST.findNode(5));

			// compare strings
			String actual = testBST.toLevelOrderString();
			String expected = "[ 3, 1, 5, 4, 9, 11 ]";

			System.out.println(actual);
			if (actual.equals(expected)) {
				testRightRotation = true;
			}
		}
		return testLeftRotation && testRightRotation;
	}

	/**
	 * This method tests it the rotate method correctly rotates in the middle at the
	 * tree, not at the root or the leaf.
	 * 
	 * @return true if it correctly rotates the tree and orders the rest of the tree
	 *         properly
	 */
	public static boolean test2() {
		boolean testLeftRotation = false;
		boolean testRightRotation = false;
		boolean testLeftRotation2 = false;
		{
			// test left rotation
			BinarySearchTree<Integer> testBST = new BinarySearchTree<Integer>();

			testBST.insert(5);
			testBST.insert(3);
			testBST.insert(9);
			testBST.insert(7);
			testBST.insert(11);
			testBST.insert(12);

			// System.out.println(testBST.toLevelOrderString());
			// rotate it
			testBST.rotate(testBST.findNode(11), testBST.findNode(9));

			// compare strings
			String actual = testBST.toLevelOrderString();
			String expected = "[ 5, 3, 11, 9, 12, 7 ]";

			System.out.println(actual.equals(expected));
			if (actual.equals(expected)) {
				testLeftRotation = true;
			}
		}

		{
			// test left rotation again
			BinarySearchTree<Integer> testBST = new BinarySearchTree<Integer>();

			testBST.insert(4);
			testBST.insert(2);
			testBST.insert(1);
			testBST.insert(3);
			testBST.insert(6);
			testBST.insert(5);
			testBST.insert(7);

			// System.out.println(testBST.toLevelOrderString());
			// rotate it
			testBST.rotate(testBST.findNode(3), testBST.findNode(2));

			// compare strings
			String actual = testBST.toLevelOrderString();
			String expected = "[ 4, 3, 6, 2, 5, 7, 1 ]";

			System.out.println(actual.equals(expected));
			System.out.println(actual);

			if (actual.equals(expected)) {
				testLeftRotation2 = true;
			}
		}

		{
			// test right rotation
			BinarySearchTree<Integer> testBST = new BinarySearchTree<Integer>();

			testBST.insert(5);
			testBST.insert(3);
			testBST.insert(9);
			testBST.insert(1);
			testBST.insert(4);
			testBST.insert(11);
			testBST.insert(2);

			// rotate it
			testBST.rotate(testBST.findNode(1), testBST.findNode(3));

			// compare strings
			String actual = testBST.toLevelOrderString();
			String expected = "[ 5, 1, 9, 3, 11, 2, 4 ]";

			System.out.println(actual);
			if (actual.equals(expected)) {
				testRightRotation = true;
			}
		}
		return testLeftRotation && testRightRotation && testLeftRotation2;
	}

	/**
	 * This method tests if the rotate method correctly rotates at the end of the
	 * tree, with a leaf node.
	 * 
	 * @return true if it correctly rotates the tree and orders the rest of the tree
	 *         properly
	 */
	public static boolean test3() {

		boolean testLeftRotation = false;
		boolean testRightRotation = false;
		{
			// test left rotation
			BinarySearchTree<Integer> testBST = new BinarySearchTree<Integer>();

			testBST.insert(5);
			testBST.insert(3);
			testBST.insert(9);
			testBST.insert(7);
			testBST.insert(11);
			testBST.insert(12);
			testBST.insert(10);

			// System.out.println(testBST.toLevelOrderString());
			// rotate it
			testBST.rotate(testBST.findNode(12), testBST.findNode(11));

			// compare strings
			String actual = testBST.toLevelOrderString();
			String expected = "[ 5, 3, 9, 7, 12, 11, 10 ]";

			System.out.println(actual);
			if (actual.equals(expected)) {
				testLeftRotation = true;
			}
		}

		{
			// test right rotation
			BinarySearchTree<Integer> testBST = new BinarySearchTree<Integer>();

			testBST.insert(5);
			testBST.insert(3);
			testBST.insert(9);
			testBST.insert(1);
			testBST.insert(4);
			testBST.insert(11);

			// rotate it
			testBST.rotate(testBST.findNode(1), testBST.findNode(3));

			// compare strings
			String actual = testBST.toLevelOrderString();
			String expected = "[ 5, 1, 9, 3, 11, 4 ]";

			System.out.println(actual);
			if (actual.equals(expected)) {
				testRightRotation = true;
			}
		}
		return testLeftRotation && testRightRotation;
	}

	// other possible tests: if no right or left child,

	/**
	 * Main method to run tests. If you'd like to add additional test methods, add a
	 * line for each of them.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Test 1 passed: " + test1());
		System.out.println("Test 2 passed: " + test2());
		System.out.println("Test 3 passed: " + test3());
	}

}
