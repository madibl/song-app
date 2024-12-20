import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

public class IterableRedBlackTree<T extends Comparable<T>> extends RedBlackTree<T>
		implements IterableSortedCollection<T> {

	private Comparable<T> start = (T) -> -1;

	public void setIterationStartPoint(Comparable<T> startPoint) {
		if (startPoint != null) {
			start = startPoint;
		}
			
		else {
			start = (T) -> -1;
		}

	}

	public Iterator<T> iterator() {
		return new RBTIterator<T>(root, start);
	}

	private static class RBTIterator<R> implements Iterator<R> {

		private Comparable<R> startIter;
		private Stack<Node<R>> stack;

		public RBTIterator(Node<R> root, Comparable<R> startPoint) {
			// store the specified start point in a private instance field within the
			// iterator.
			this.startIter = startPoint;
			// create and store an empty Stack of Node<T> references within private instance
			// field.
			this.stack = new Stack<Node<R>>();

			// call buildStackHelper and pass in root
			buildStackHelper(root);
		}

		/**
		 * find next value stored in a tree (or subtree) that is bigger than or equal to
		 * the specified start point and build up the stack of ancestor nodes that are
		 * larger than or equal to the start point, so that those nodes' data can be
		 * visited in the future
		 * 
		 * @param node
		 */
		private void buildStackHelper(Node<R> node) {
			// base case
			if (node == null) {
				return;
			}
			// recursive case
			if (startIter.compareTo(node.data) > 0) {
				buildStackHelper(node.down[1]);
			}
			// recursive case
			if (startIter.compareTo(node.data) <= 0) {
				stack.add(node);
				buildStackHelper(node.down[0]);
			}
		}

		public boolean hasNext() {
			return !stack.isEmpty();
		}

		public R next() {
			if (stack.isEmpty()) {
				throw new NoSuchElementException("Stack is Empty.");
			}
			Node<R> node = stack.pop();
			buildStackHelper(node.down[1]);
			return node.data;
		}
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
	@Override
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
//				if (compare == 0) {
//					return false;
//				} 
				if (compare <= 0) {
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
	 * Tests trees containing different kinds of Comparable data, such as strings vs
	 * integers
	 */
	@Test
	public void testDifferentTrees() {
		// tree of Integer Objects - test if compared and ordered as expected
		{
			IterableRedBlackTree<Integer> integerTree = new IterableRedBlackTree<Integer>();
			integerTree.insert(2);
			integerTree.insert(4);
			integerTree.insert(6);
			integerTree.insert(9);
			integerTree.insert(1);
			integerTree.insert(5);

			Iterator<Integer> iter = integerTree.iterator();
			ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(new Integer[] { 1, 2, 4, 5, 6, 9 }));
			ArrayList<Integer> result = new ArrayList<Integer>();
			while (iter.hasNext()) {
				result.add(iter.next());
			}
			Assertions.assertEquals(expected, result);

		}
		// tree of String objects - test if compared and ordered as expected
		{
			IterableRedBlackTree<String> stringTree = new IterableRedBlackTree<String>();
			stringTree.insert("apple");
			stringTree.insert("orange");
			stringTree.insert("banana");
			stringTree.insert("strawberry");
			stringTree.insert("pineapple");
			stringTree.insert("blueberry");

			Iterator<String> iter = stringTree.iterator();
			ArrayList<String> expected = new ArrayList<>(Arrays
					.asList(new String[] { "apple", "banana", "blueberry", "orange", "pineapple", "strawberry" }));
			ArrayList<String> result = new ArrayList<String>();
			while (iter.hasNext()) {
				result.add(iter.next());
			}
			Assertions.assertIterableEquals(expected, result);

		}
	}

	/**
	 * Tests some trees containing duplicate values
	 */
	@Test
	public void testDuplicate() {
		{
			IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
			tree.insert(1);
			tree.insert(1);
			tree.insert(4);
			tree.insert(4);
			tree.insert(5);
			tree.insert(5);

			Assertions.assertEquals(6, tree.size());

			Iterator<Integer> iter = tree.iterator();
			ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(new Integer[] { 1, 1, 4, 4, 5, 5 }));
			ArrayList<Integer> result = new ArrayList<Integer>();
			while (iter.hasNext()) {
				result.add(iter.next());
			}
			Assertions.assertEquals(expected, result);

		}
		
		{
			IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
			tree.insert(1);
			tree.insert(1);
			tree.insert(1);
			tree.insert(1);
			tree.insert(1);
			tree.insert(1);

			Assertions.assertEquals(6, tree.size());

			Iterator<Integer> iter = tree.iterator();
			ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(new Integer[] { 1, 1, 1, 1, 1, 1 }));
			ArrayList<Integer> result = new ArrayList<Integer>();
			while (iter.hasNext()) {
				result.add(iter.next());
			}
			Assertions.assertEquals(expected, result);

		}
		
		{
			IterableRedBlackTree<Character> tree = new IterableRedBlackTree<>();
			tree.insert('a');
			tree.insert('a');
			tree.insert('a');
			tree.insert('b');
			tree.insert('c');
			tree.insert('b');

			Assertions.assertEquals(6, tree.size());

			Iterator<Character> iter = tree.iterator();
			ArrayList<Character> expected = new ArrayList<>(Arrays.asList(new Character[] { 'a', 'a', 'a', 'b', 'b', 'c' }));
			ArrayList<Character> result = new ArrayList<Character>();
			while (iter.hasNext()) {
				result.add(iter.next());
			}
			Assertions.assertEquals(expected, result);

		}
	}
	
	/**
	 * Tests some trees containing non-duplicate values
	 */
	@Test
	public void testNonDuplicate() {
		{
			IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
			tree.insert(2);
			tree.insert(4);
			tree.insert(6);
			tree.insert(9);
			tree.insert(1);
			tree.insert(5);

			Assertions.assertEquals(6, tree.size());

			Iterator<Integer> iter = tree.iterator();
			ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(new Integer[] { 1, 2, 4, 5, 6, 9 }));
			ArrayList<Integer> result = new ArrayList<Integer>();
			
			while (iter.hasNext()) {
				result.add(iter.next());
			}
			Assertions.assertEquals(expected, result);

		}
		
		{
			IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
			tree.insert(2);
			tree.insert(4);
			tree.insert(6);
			tree.insert(9);
			tree.insert(1);
			tree.insert(2);

			Assertions.assertEquals(6, tree.size());

			Iterator<Integer> iter = tree.iterator();
			ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(new Integer[] { 1, 2, 2, 4, 6, 9 }));
			ArrayList<Integer> result = new ArrayList<Integer>();
			
			while (iter.hasNext()) {
				result.add(iter.next());
			}
			Assertions.assertEquals(expected, result);

		}
		
		{
			IterableRedBlackTree<Character> tree = new IterableRedBlackTree<>();
			tree.insert('z');
			tree.insert('y');
			tree.insert('a');
			tree.insert('b');
			tree.insert('c');
			tree.insert('d');

			Assertions.assertEquals(6, tree.size());

			Iterator<Character> iter = tree.iterator();
			ArrayList<Character> expected = new ArrayList<>(Arrays.asList(new Character[] { 'a', 'b', 'c', 'd', 'y', 'z' }));
			ArrayList<Character> result = new ArrayList<Character>();
			while (iter.hasNext()) {
				result.add(iter.next());
			}
			Assertions.assertEquals(expected, result);

		}
	}

	/**
	 * Tests iterator created with a specified starting point
	 */
	@Test
	public void testIteratorsWithStartingPoint() {
		// With specified starting point
		{
			IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
			tree.insert(2);
			tree.insert(4);
			tree.insert(6);
			tree.insert(9);
			tree.insert(1);
			tree.insert(5);

			tree.setIterationStartPoint(6);
			Iterator<Integer> iter = tree.iterator();
			
			ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(new Integer[] { 6, 9 }));
			ArrayList<Integer> result = new ArrayList<Integer>();
			while (iter.hasNext()) {
				result.add(iter.next());
			}
			Assertions.assertEquals(expected, result);
		}
		// With specified starting point not within the tree - should get the next value
		// after it
		{
			IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
			tree.insert(2);
			tree.insert(4);
			tree.insert(6);
			tree.insert(9);
			tree.insert(1);
			tree.insert(5);

			tree.setIterationStartPoint(3);
			Iterator<Integer> iter = tree.iterator();
			
			ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(new Integer[] { 4, 5, 6, 9 }));
			ArrayList<Integer> result = new ArrayList<Integer>();
			while (iter.hasNext()) {
				result.add(iter.next());
			}
			Assertions.assertEquals(expected, result);
		}
		// Starting point higher than all values
		{
			IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
			tree.insert(2);
			tree.insert(4);
			tree.insert(6);
			tree.insert(9);
			tree.insert(1);
			tree.insert(5);

			tree.setIterationStartPoint(10);
			Iterator<Integer> iter = tree.iterator();
			
			ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(new Integer[] { }));
			ArrayList<Integer> result = new ArrayList<Integer>();
			while (iter.hasNext()) {
				result.add(iter.next());
			}
			Assertions.assertEquals(expected, result);
		}
		
		{
			IterableRedBlackTree<String> tree = new IterableRedBlackTree<>();
			tree.insert("Barry");
			tree.insert("Anna");
			tree.insert("Charlie");
			tree.insert("Christoper");
			tree.insert("Samantha");
			tree.insert("Eric");

			tree.setIterationStartPoint("Edgar");
			Iterator<String> iter = tree.iterator();
			
			ArrayList<String> expected = new ArrayList<>(Arrays.asList(new String[] { "Eric", "Samantha" }));
			ArrayList<String> result = new ArrayList<String>();
			while (iter.hasNext()) {
				result.add(iter.next());
			}
			Assertions.assertEquals(expected, result);
		}
		
		
		
	}
	
	/**
	 * Tests iterator created without a specified starting point
	 */
	@Test
	public void testIteratorsWithoutStartingPoint() {


		// Without specified starting point - iterator should step through every element
		// in tree
		{
			IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();
			tree.insert(2);
			tree.insert(4);
			tree.insert(6);
			tree.insert(9);
			tree.insert(1);
			tree.insert(5);

			Assertions.assertEquals(6, tree.size());

			Iterator<Integer> iter = tree.iterator();
			ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(new Integer[] { 1, 2, 4, 5, 6, 9 }));
			ArrayList<Integer> result = new ArrayList<Integer>();
			while (iter.hasNext()) {
				result.add(iter.next());
			}
			Assertions.assertIterableEquals(expected, result);
		}
		
		{
			{
				IterableRedBlackTree<Character> tree = new IterableRedBlackTree<>();
				tree.insert('a');
				tree.insert('t');
				tree.insert('a');
				tree.insert('b');
				tree.insert('c');
				tree.insert('b');

				Assertions.assertEquals(6, tree.size());

				Iterator<Character> iter = tree.iterator();
				ArrayList<Character> expected = new ArrayList<>(Arrays.asList(new Character[] { 'a', 'a', 'b', 'b', 'c', 't' }));
				ArrayList<Character> result = new ArrayList<Character>();
				while (iter.hasNext()) {
					result.add(iter.next());
				}
				Assertions.assertEquals(expected, result);

			}
		}

	}

}
