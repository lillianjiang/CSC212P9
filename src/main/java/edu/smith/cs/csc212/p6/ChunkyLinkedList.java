package edu.smith.cs.csc212.p6;

import java.util.Iterator;

import edu.smith.cs.csc212.p6.errors.BadIndexError;
import edu.smith.cs.csc212.p6.errors.EmptyListError;

/**
 * This is a data structure that has an array inside each node of a Linked List.
 * Therefore, we only make new nodes when they are full. Some remove operations
 * may be easier if you allow "chunks" to be partially filled.
 * 
 * @author jfoley
 * @param <T>
 *            - the type of item stored in the list.
 */
public class ChunkyLinkedList<T> implements P6List<T> {
	private int chunkSize;
	private SinglyLinkedList<FixedSizeList<T>> chunks;

	public ChunkyLinkedList(int chunkSize) {
		this.chunkSize = chunkSize;
		chunks = new SinglyLinkedList<>();
		chunks.addBack(new FixedSizeList<T>(chunkSize));
	}

	//O(2n/chunkSize+chunkSize)n)
	@Override
	public T removeFront() {
		return removeIndex(0);
	}

	//O(2n/chunkSize+chunkSize)
	@Override
	public T removeBack() {
		if (this.isEmpty()) {
			throw new EmptyListError();
		} else {
			return removeIndex(this.size()-1);
		}
	}

	//O(2n/chunkSize+chunkSize)
	@Override
	public T removeIndex(int index) {
//		System.out.println("removeIndex: "+index);

		if (this.isEmpty()) {
			throw new EmptyListError();
		} else {
			//keep track of the chunk index(the index of singlylinkedlist)
			int chunkCount=0;
			
			int start = 0;
			for (FixedSizeList<T> chunk : this.chunks) { //O(n)
				int end = start + chunk.size();
				
				if (start <= index && index < end) {
					T deleted = chunk.removeIndex(index - start); //O(chunkSize)
					
					//if in current node, there is no chunk/element in it, we delete this node
					if(chunk.size() == 0) {
						this.chunks.removeIndex(chunkCount);
						chunkCount--;
					}
					return deleted;
				}
				start = end;
				chunkCount++;
			}
			throw new BadIndexError();
		}
	}

	
	@Override 
	public void addFront(T item) {
		this.addIndex(item, 0);
	}
//		if(this.chunks.getFront().size() == chunkSize) {
//			FixedSizeList<T> newchunk = new FixedSizeList<T>(chunkSize);
//			this.chunks.addFront(newchunk);
//			newchunk.addFront(item);
//		}
//		else {
//			this.chunks.getFront().addBack(item);
//		}
//	}

	
	@Override
	public void addBack(T item) {
		addIndex(item, size());
	}

	
	@Override
	public void addIndex(T item, int index) {
		//if there is no chunks in the chunky, we create a new node(chunks) and
		// put the first item into the new chunks
		if(this.isEmpty()) {
			FixedSizeList<T> newnode = new FixedSizeList<T>(chunkSize);
			newnode.addFront(item);
			//System.out.println(chunks.getFront().size());
			this.chunks.addFront(newnode);
			return;
		}
		//a counter for the index of chunks(Nodes) in the list
		int chunkNum = 0;
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			int end = start + chunk.size();
			if(start<=index && index <= end) {
				//if chunk is full
				if(chunk.size() == chunkSize) {
					//make a new FSL
					FixedSizeList<T> newchunk = new FixedSizeList<>(chunkSize);
					/*
					 * We need to move every element downward/rightward in order to
					 * create a space for the item we wanna add in.
					 * 
					 * the special case is if we want to add the item to the last position
					 * of the current chunk. Then we just create a new chunks(node) to put it into.
					 * 
					 * otherwise, we need to move all the elemts from the target postion to downward. and the last elemnt
					 * of the current chunk need to be moved to a new chunk's front
					 */
					if(index != end) {
						//remove the last elemts in current chunk to the newchunk's front
						newchunk.addFront(chunk.getBack());
						chunk.removeBack();
						//add the newchunk(node) after the current chunks
						chunks.addIndex(newchunk, chunkNum+1);
						
						//move every ele after the index downward
						chunk.addIndex(item, index - start);
					}
					else {
						newchunk.addFront(item);
						chunks.addIndex(newchunk, chunkNum+1);
					}
				}
				//if chunk is not full
				else {
					chunk.addIndex(item, index - start);
				}
				return;
			}
			//update the bound
			start = end;
			//chunks(node) index++
			chunkNum++;
		}
		
	}
		
	//O(1)
	@Override
	public T getFront() {
		return this.chunks.getFront().getFront();
		//return getIndex(0); 
		//0(n)
	}

	//O(1)
	@Override
	public T getBack() {
		return this.chunks.getBack().getBack();
		//return getIndex(size()); 
		//O(n)
	}

	//O(n)
	@Override
	public T getIndex(int index) {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
			int start = 0;
			for (FixedSizeList<T> chunk : this.chunks) { //O(n)
				// calculate bounds of this chunk.
				int end = start + chunk.size();
	
				// Check whether the index should be in this chunk:
				if (start <= index && index < end) {
					return chunk.getIndex(index - start); //O(1)
				}
	
				// update bounds of next chunk.
				start = end;
		}
			throw new BadIndexError();
	}

	//O(n)
	@Override
	public int size() {
		int total = 0;
//		Iterator<FixedSizeList<T>> iter = chunks.iterator();
//		while(iter.hasNext()) {
//			FixedSizeList<T> newsingle = iter.next();
//			total+=newsingle.size();
//		}
		for (FixedSizeList<T> chunk : this.chunks) {
			total += chunk.size();
		}
		return total;
	}

	//O(1)
	@Override
	public boolean isEmpty() {
		return this.chunks.isEmpty() || this.chunks.getFront().isEmpty();
	}
}
