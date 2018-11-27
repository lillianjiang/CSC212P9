package edu.smith.cs.csc212.p6;

import edu.smith.cs.csc212.p6.errors.BadIndexError;
import edu.smith.cs.csc212.p6.errors.EmptyListError;

public class GrowableList<T> implements P6List<T> {
	public static final int START_SIZE = 4;
	private Object[] array;
	private int fill;

	public GrowableList() {
		this.array = new Object[START_SIZE];
		this.fill = 0;
	}

	@Override
	public T removeFront() {
		return removeIndex(0);
	}

	@Override
	public T removeBack() {
		return removeIndex(fill - 1);
	}

	@Override
	public T removeIndex(int index) {
		if (this.fill == 0) {
			throw new EmptyListError();
		}
		T removed = this.getIndex(index);
		for (int i = index; i < this.fill- 1 ; i++) {
			this.array[i] = this.array[i + 1];
		}
		this.array[this.fill-1] = null;
		fill--;
		return removed;
	}

	@Override
	public void addFront(T item) {
		addIndex(item, 0);
	}

	@Override
	public void addBack(T item) {
		addIndex(item, fill);
	}

	@Override
	public void addIndex(T item, int index) {
		if(index>this.array.length || index < 0)
		{
			throw new BadIndexError();
		}
		else {
			if (fill >= this.array.length) {
			int newsize = this.array.length * 2;
			Object[] newarray = new Object[newsize];
			for (int i = index; i < this.array.length; i++) {
				newarray[i + 1] = array[i];
			}
			for (int i = 0; i < index; i++) {
				newarray[i] = array[i];
			}
			this.array = newarray;
			newarray[index] = item;
			fill++;
		} else {
			for (int i = fill; i > index; i--) {
				array[i] = array[i - 1];
			}
			array[index] = item;
			fill++;
		}
		}
	}

	@Override
	public T getFront() {
		if(this.fill == 0) {
			throw new EmptyListError();
		}
		else {
			return this.getIndex(0);
		}
		
	}

	@Override
	public T getBack() {
		if(this.fill == 0) {
			throw new EmptyListError();
		}
		else {
			return this.getIndex(this.fill - 1);
		}
	}

	/**
	 * Do not allow unchecked warnings in any other method. Keep the "guessing" the
	 * objects are actually a T here. Do that by calling this method instead of
	 * using the array directly.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getIndex(int index) {
		if(index >= this.array.length || index < 0) {
			throw new BadIndexError();
		}
		else {
			return (T) this.array[index];
		}
	}

	@Override
	public int size() {
		return fill;
	}

	@Override
	public boolean isEmpty() {
		return fill == 0;
	}

}
