package edu.smith.cs.csc212.p6;

import java.util.Iterator;

import edu.smith.cs.csc212.p6.errors.BadIndexError;
import edu.smith.cs.csc212.p6.errors.EmptyListError;

public class DoublyLinkedList<T> implements P6List<T>,Iterable<T> {
	private Node<T> start;
	private Node<T> end;

	/**
	 * A doubly-linked list starts empty.
	 */
	public DoublyLinkedList() {
		this.start = null;
		this.end = null;
	}

	@Override
	//O(1)
	public T removeFront() {
		checkNotEmpty();

		T val = this.start.value;

		// if there is only one elements in the doublelinked list
		// remove it and set both start and end to null
		if (!isEmpty() && getFront().equals(getBack())) { //O(1)
			this.start = null;
			this.end = null;
		}
		// otherwise, set the before pointer of second node to null
		// and reset the start node.
		else {
			Node<T> second = this.start.after;
			second.before = null;
			this.start = second;
		}
		return val;

	}

	@Override
	//O(1)
	public T removeBack() {
		checkNotEmpty();
		// Keep a record of the removed value
		T val = this.end.value;

		// instead of calling size(), which takes O(n)
		// this statement only takes O(1) steps
		if (!isEmpty() && getFront().equals(getBack())) {
			this.start = null;
			this.end = null;
		} else {
			Node<T> beforeEnd = this.end.before;
			beforeEnd.after = null;
			this.end = beforeEnd;
		}
		return val;
	}

	@Override
	//O(N+N)
	public T removeIndex(int index) {
		checkNotEmpty();
		// Get the node we have to remove
		Node<T> removed = getNode(index); //O(N)
		T removedvalue = getIndex(index); //O(N)
		// compare whether the removed node is the start/end of the list
		if (removed == start) {
			removeFront();
		} else if (removed == end) {
			removeBack();
			// otherwise, change the pointer of prev/aft node nearby the removed node
		} else {
			Node<T> prev = removed.before;
			Node<T> aft = removed.after;
			prev.after = aft;
			aft.before = prev;
		}

		return removedvalue;
	}

	@Override
	//O(N)
	public void addFront(T item) {
		Node<T> newnode = new Node<T>(item);

		if (this.size() == 0) { // N
			this.start = newnode;
			this.end = newnode;
		} else {
			this.start.before = newnode;
			newnode.after = this.start;
			this.start = newnode;
		}
	}

	@Override
	//O(N)
	public void addBack(T item) {
		Node<T> newnode = new Node<T>(item);

		if (this.size() == 0) { // N
			this.start = newnode;
			this.end = newnode;
		} else {
			this.end.after = newnode;
			newnode.before = this.end;
			this.end = newnode;
		}
	}

	@Override // O(N)?
	public void addIndex(T item, int index) {
		while(index > this.size() || index < 0) {
			throw new BadIndexError();
		}
		if(index==0) {
			Node<T> newnode = new Node<T>(item);
			newnode.after=this.start;
			this.start.before=newnode;
			start = newnode;
		}
		else {
			Node<T> prevNode = this.getNode(index - 1); // N
			Node<T> newnode = new Node<T>(item);
			newnode.after = prevNode;
			newnode.before = prevNode.before;
			prevNode.before.after = newnode;
			prevNode.before = newnode;
		}
	}

	@Override // O(1)
	public T getFront() {
		checkNotEmpty();
		return this.start.value;

	}

	@Override // O(1)
	public T getBack() {
		checkNotEmpty();
		return this.end.value;
	}

	// O(N)
	public Node<T> getNode(int index) {
		checkNotEmpty();
		while(index > this.size() || index < 0) {
			throw new BadIndexError();
		}
		int i = 0;

		for (Node<T> current = start; current != null; current = current.after) {
			if (index == i) {
				return current;
			}
			i++;
		}
		throw new EmptyListError();
	}

	@Override // O(N)
	public T getIndex(int index) {
		while(index >= this.size() || index < 0) {
			throw new BadIndexError();
		}
		return this.getNode(index).value;
	}

	/*
	 * O(n)
	 */
	@Override
	public int size() {
		// keep track of count
		int count = 0;

		for (Node<T> current = start; current != null; current = current.after) {
			count++;
		}
		return count;
	}

	@Override
	// O(n)
	// The method call this.size() which need O(n) steps
	public boolean isEmpty() {
		if (this.size() == 0) {
			return true;
		}
		return false;
	}

	private void checkNotEmpty() {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
	}

	/**
	 * The node on any linked list should not be exposed. Static means we don't need
	 * a "this" of DoublyLinkedList to make a node.
	 * 
	 * @param <T>
	 *            the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes before me?
		 */
		public Node<T> before;
		/**
		 * What node comes after me?
		 */
		public Node<T> after;
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
		public Node(T value) {
			this.value = value;
			this.before = null;
			this.after = null;
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new Iter<>(this);
	}
	
	private static class Iter<T> implements Iterator<T> {

		Node <T> current;
		
		public Iter(DoublyLinkedList<T> list) {
			this.current = list.start;
		}
		
		@Override
		public boolean hasNext() {
			return current.after != null;
		}

		@Override
		public T next() {
			T found = current.value;
			current = current.after;
			return found;
		}
	}
}

