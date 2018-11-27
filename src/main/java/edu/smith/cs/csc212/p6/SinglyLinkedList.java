package edu.smith.cs.csc212.p6;

import java.util.Iterator;

import edu.smith.cs.csc212.p6.errors.BadIndexError;
import edu.smith.cs.csc212.p6.errors.EmptyListError;

public class SinglyLinkedList<T> implements P6List<T>, Iterable<T> {
	/**
	 * The start of this list. Node is defined at the bottom of this file.
	 */
	Node<T> start;

	@Override
	//O(1)
	public T removeFront() {
		checkNotEmpty();
		T before = start.value;
		start = start.next;
		return before;
	}

	@Override
	//O(N)
	public T removeBack() {
		checkNotEmpty();
		if (start.next == null) {
			T last = start.value;
			start = null;
			return last;
		} else {
			Node<T> current = start;
			for (current = start; current.next.next != null; current = current.next) {
				continue;
			}
			T last = current.next.value;
			current.next = null;
			return last;
		}
	}

	@Override
	//O(N)
	public T removeIndex(int index) {
		checkNotEmpty();
		if (index == 0) {
			T removed = start.value;
			Node<T> newstart = start.next;
			start = newstart;
			return removed;
		} else {
			T removed = start.value;
			Node<T> current = start;
			for (int i = 0; i < index - 1; i++) { //N
				current = current.next;
			}
			Node<T> after = current.next.next;
			removed = current.next.value;
			current.next = after;
			return removed;
		}
	}

	@Override
	//O(1)
	public void addFront(T item) {
		this.start = new Node<T>(item, start);
	}

	@Override
	//O(N)
	public void addBack(T item) {
		Node<T> newNode = new Node<T>(item, null);
		if (start == null) {
			start = newNode;
		} else {
			Node<T> last = start;
			while (last.next != null) {
				last = last.next;
			}
			last.next = newNode;
		}
	}

	@Override
	//O(N)
	/*
	 * Remember, it is to add a node with item into the singlylinked list
	 * The original one I wrote was just replace the value.
	 */
	public void addIndex(T item, int index) {
		while(index > this.size() || index < 0) {
			throw new BadIndexError();
		}
		if(index == 0) {
			Node<T> newnode = new Node<T>(item, start);
			start = newnode;
		}
		else {
			Node<T> current = start;
			for (int i = 0; i < index-1; i++) {
				current = current.next;
			}
			Node<T> newnode = new Node<T>(item, current.next);
			current.next = newnode;
		}
	}

	@Override
	//O(1)
	public T getFront() {
		checkNotEmpty();
		if(start==null) {
			return null;
		}
		else {
			return start.value;
		}
	}

	@Override
	//O(N)
	public T getBack() {
		checkNotEmpty();
		T last = start.value;
		for (Node<T> current = start; current != null; current = current.next) {
			last = current.value;
		}
		return last;
	}

	@Override
	//O(N)
	public T getIndex(int index) {
		checkNotEmpty();
		while(index>=this.size() || index < 0) {
			throw new BadIndexError();
		}
		T getvalue = start.value;
		Node<T> current = start;
		for (int i = 0; i < index; i++) {
			getvalue = current.next.value;
			current = current.next;
		}
		return getvalue;

	}

	@Override
	//O(N)
	public int size() {
		int count = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			count++;
		}
		return count;
	}

	@Override
	//O(1)
	public boolean isEmpty() {
		if (start == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Helper method to throw the right error for an empty state.
	 */
	private void checkNotEmpty() {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
	}

	/**
	 * The node on any linked list should not be exposed. Static means we don't need
	 * a "this" of SinglyLinkedList to make a node.
	 * 
	 * @param <T>
	 *            the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes after me?
		 */
		public Node<T> next;
		/**
		 * What value is stored in this node?
		 */
		public T value;

		/**
		 * Create a node with no friends.
		 * 
		 * @param value
		 *            - the value to put in it.
		 */
		public Node(T value, Node<T> next) {
			this.value = value;
			this.next = next;
		}
	}

	/**
	 * I'm providing this class so that SinglyLinkedList can be used in a for loop
	 * for {@linkplain ChunkyLinkedList}. This Iterator type is what java uses for
	 * {@code for (T x : list) { }} loops.
	 * 
	 * @author jfoley
	 *
	 * @param <T>
	 */
	private static class Iter<T> implements Iterator<T> {
		/**
		 * This is the value that walks through the list.
		 */
		Node<T> current;

		/**
		 * This constructor details where to start, given a list.
		 * 
		 * @param list
		 *            - the SinglyLinkedList to iterate or loop over.
		 */
		public Iter(SinglyLinkedList<T> list) {
			this.current = list.start;
		}

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			T found = current.value;
			current = current.next;
			return found;
		}
	}

	/**
	 * Implement iterator() so that {@code SinglyLinkedList} can be used in a for
	 * loop.
	 * 
	 * @return an object that understands "next()" and "hasNext()".
	 */
	public Iterator<T> iterator() {
		return new Iter<>(this);
	}
}
