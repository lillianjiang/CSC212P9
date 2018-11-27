package edu.smith.cs.csc212.p6;

import org.junit.Test;

import edu.smith.cs.csc212.p6.errors.BadIndexError;
import edu.smith.cs.csc212.p6.errors.EmptyListError;
import edu.smith.cs.csc212.p6.errors.RanOutOfSpaceError;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;

public class GrowableListTest {
	private <T> P6List<T> makeEmptyList() {
		return new GrowableList<>();
	}
		
	@Test
	public void testEmpty() {
		P6List<String> data = makeEmptyList();
		Assert.assertEquals(0, data.size());
		Assert.assertEquals(true, data.isEmpty());
		data = new FixedSizeList<String>(32);
		Assert.assertEquals(0, data.size());
		Assert.assertEquals(true, data.isEmpty());
	}
	
	@Test(expected=EmptyListError.class)
	public void testRemoveFrontCrash() {
		P6List<String> data = makeEmptyList();
		data.removeFront();
	}
	
	@Test(expected=EmptyListError.class)
	public void testRemoveBackCrash() {
		P6List<String> data = makeEmptyList();
		data.removeBack();
	}
	
	@Test(expected=EmptyListError.class)
	public void testRemoveIndexCrash() {
		P6List<String> data = makeEmptyList();
		data.removeIndex(3);
	}

	@Test
	public void testAddToFront() {
		P6List<String> data = makeEmptyList();
		Assert.assertEquals(true, data.isEmpty());
		data.addFront("1");
		Assert.assertEquals(1, data.size());
		Assert.assertEquals("1", data.getIndex(0));
		Assert.assertEquals(false, data.isEmpty());
		data.addFront("0");
		Assert.assertEquals(2, data.size());
		Assert.assertEquals("0", data.getIndex(0));
		Assert.assertEquals("1", data.getIndex(1));
		Assert.assertEquals(false, data.isEmpty());
		data.addFront("-1");
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("-1", data.getIndex(0));
		Assert.assertEquals("0", data.getIndex(1));
		Assert.assertEquals("1", data.getIndex(2));
		Assert.assertEquals(false, data.isEmpty());
		data.addFront("-2");
		Assert.assertEquals("-2", data.getIndex(0));
		Assert.assertEquals("-1", data.getIndex(1));
		Assert.assertEquals("0", data.getIndex(2));
		Assert.assertEquals("1", data.getIndex(3));
		Assert.assertEquals(false, data.isEmpty());
	}
	
	@Test
	public void testAddToBack() {
		P6List<String> data = makeEmptyList();
		data.addBack("1");
		Assert.assertEquals(1, data.size());
		Assert.assertEquals("1", data.getIndex(0));
		data.addBack("0");
		Assert.assertEquals(2, data.size());
		Assert.assertEquals("0", data.getIndex(1));
		Assert.assertEquals("1", data.getIndex(0));
		data.addBack("-1");
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("-1", data.getIndex(2));
		Assert.assertEquals("0", data.getIndex(1));
		Assert.assertEquals("1", data.getIndex(0));
		data.addBack("-2");
		Assert.assertEquals("-2", data.getIndex(3));
		Assert.assertEquals("-1", data.getIndex(2));
		Assert.assertEquals("0", data.getIndex(1));
		Assert.assertEquals("1", data.getIndex(0));
	}
	
	/**
	 * Helper method to make a full list.
	 * @return
	 */
	public P6List<String> makeFullList() {
		P6List<String> data = makeEmptyList();
		data.addBack("a");
		data.addBack("b");
		data.addBack("c");
		data.addBack("d");
		return data;
	}
	
	@Test
	public void testAddBackFull() {
		P6List<Integer> items = makeEmptyList();
		for (int i=0; i<GrowableList.START_SIZE*5; i++) {
			items.addBack((i+1)*3);
			Assert.assertEquals(i+1, items.size());
			Assert.assertEquals((i+1)*3, (int) items.getBack()); 
		}
		for (int i=0; i<GrowableList.START_SIZE*5; i++) {
			Assert.assertEquals((i+1)*3, (int) items.getIndex(i)); 
		}
	}
	
	@Test
	public void testAddFrontFull() {
		P6List<Integer> items1 = makeEmptyList();
		for (int i=0; i<GrowableList.START_SIZE*5; i++) {
			items1.addBack((i+1)*3);
			Assert.assertEquals(i+1, items1.size());
			Assert.assertEquals((i+1)*3, (int) items1.getBack()); 
		}
		P6List<Integer> items2 = makeEmptyList();
		while(!items1.isEmpty()) {
			items2.addFront(items1.removeBack());
		}
		for (int i=0; i<GrowableList.START_SIZE*5; i++) {
			Assert.assertEquals((i+1)*3, (int) items2.getIndex(i)); 
		}
	}
	
	private void insertSorted(P6List<Integer> items, int num) {
		for (int i=0; i<items.size(); i++) {
			if (items.getIndex(i) >= num) {
				items.addIndex(num, i);
				return;
			}
		}
		items.addBack(num);
	}
	
	@Test
	public void testAddIndexFull() {
		P6List<Integer> items1 = makeEmptyList();
		for (int i=0; i<GrowableList.START_SIZE*5; i++) {
			items1.addBack((i+1)*3);
			Assert.assertEquals(i+1, items1.size());
			Assert.assertEquals((i+1)*3, (int) items1.getBack()); 
		}
		
		Random rand = new Random(13);
		P6List<Integer> items2 = makeEmptyList();
		while(!items1.isEmpty()) {
			int value = items1.removeIndex(rand.nextInt(items1.size()));
			insertSorted(items2, value);
		}
		
		for (int i=0; i<GrowableList.START_SIZE*5; i++) {
			Assert.assertEquals((i+1)*3, (int) items2.getIndex(i)); 
		}
	}
	
	@Test
	public void testRemoveFront() {
		P6List<String> data = makeFullList();
		Assert.assertEquals(4, data.size());
		Assert.assertEquals("a", data.removeFront());
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("b", data.removeFront());
		Assert.assertEquals(2, data.size());
		Assert.assertEquals("c", data.removeFront());
		Assert.assertEquals(1, data.size());
		Assert.assertEquals("d", data.removeFront());
		Assert.assertEquals(0, data.size());
	}
	
	@Test
	public void testRemoveBack() {
		P6List<String> data = makeFullList();
		Assert.assertEquals(4, data.size());
		Assert.assertEquals("d", data.removeBack());
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("c", data.removeBack());
		Assert.assertEquals(2, data.size());
		Assert.assertEquals("b", data.removeBack());
		Assert.assertEquals(1, data.size());
		Assert.assertEquals("a", data.removeBack());
		Assert.assertEquals(0, data.size());
	}
	
	@Test
	public void testRemoveIndex() {
		P6List<String> data = makeFullList();
		Assert.assertEquals(4, data.size());
		for(int i=0;i<data.size(); i++)
			System.out.print(data.getIndex(i));
		Assert.assertEquals("c", data.removeIndex(2));
		Assert.assertEquals(3, data.size());
		for(int i=0;i<data.size(); i++)
			System.out.print(data.getIndex(i));
		Assert.assertEquals("d", data.removeIndex(2));
		Assert.assertEquals(2, data.size());
		Assert.assertEquals("b", data.removeIndex(1));
		Assert.assertEquals(1, data.size());
		Assert.assertEquals("a", data.removeIndex(0));
		Assert.assertEquals(0, data.size());
	}
	
	@Test
	public void testAddIndexFront() {
		P6List<String> data = makeEmptyList();
		data.addBack("A");
		Assert.assertEquals(1, data.size());
		Assert.assertEquals("A", data.getFront());
		data.addIndex("B", 0);
		Assert.assertEquals(2, data.size());
		Assert.assertEquals("B", data.getFront());
		Assert.assertEquals("A", data.getBack());
	}
	
	@Test
	public void testAddIndexBack() {
		P6List<String> data = makeEmptyList();
		data.addBack("A");
		Assert.assertEquals(1, data.size());
		Assert.assertEquals("A", data.getFront());
		data.addIndex("B", 1);
		Assert.assertEquals(2, data.size());
		Assert.assertEquals("A", data.getFront());
		Assert.assertEquals("B", data.getBack());
	}
	
	@Test
	public void testAddIndexCenter() {
		P6List<String> data = makeEmptyList();
		data.addBack("A");
		data.addBack("C");
		data.addBack("D");
		data.addBack("E");
		Assert.assertEquals(4, data.size());
		
		data.addIndex("B", 1);
		Assert.assertEquals(5, data.size());
		Assert.assertEquals("B", data.getIndex(1));
	}
	
	@Test
	public void testGetFront() {
		P6List<String> data = makeFullList();
		assertEquals("a", data.getFront());
	}
	
	@Test
	public void testGetBack() {
		P6List<String> data = makeFullList();
		assertEquals("d", data.getBack());
	}
	
	@Test(expected=EmptyListError.class)
	public void testGetFrontCrash() {
		P6List<String> data = makeEmptyList();
		data.getFront();
	}

	@Test(expected=EmptyListError.class)
	public void testGetBackCrash() {
		P6List<String> data = makeEmptyList();
		data.getBack();
		
	}
	
	@Test(expected=BadIndexError.class)
	public void testGetIndexLow() {
		P6List<String> data = makeFullList();
		data.getIndex(-2);
	}
	
	@Test(expected=BadIndexError.class)
	public void testGetIndexHigh() {
		P6List<String> data = makeFullList();
		data.getIndex(data.size());
	}
	
	@Test(expected=BadIndexError.class)
	public void testGetIndexHighEasy() {
		P6List<String> data = makeFullList();
		data.getIndex(data.size()*2);
	}
	
	@Test(expected=BadIndexError.class)
	public void testAddIndexHighEasy() {
		P6List<String> data = makeFullList();
		data.addIndex("the", data.size()*2);
	}
	
	@Test(expected=BadIndexError.class)
	public void testAddIndexHigh() {
		P6List<String> data = makeFullList();
		data.addIndex("the", data.size()+1);
	}
	
	@Test(expected=BadIndexError.class)
	public void testAddIndexLow() {
		P6List<String> data = makeFullList();
		data.addIndex("the", -1);
	}
	
	@Test
	public void testQueue() {
		P6List<Integer> data = makeEmptyList();
		
		for (int trial=0; trial<4; trial++) {
			for (int i=0; i<20; i++) {
				data.addBack(i);
			}
			for (int i=0; i<20; i++) {
				//System.err.println("Assertion-Debug: "+trial+", value: "+i);
				Assert.assertEquals(i, (int) data.removeFront());
			}
		}
	}
}
