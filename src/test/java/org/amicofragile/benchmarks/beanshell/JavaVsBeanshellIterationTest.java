package org.amicofragile.benchmarks.beanshell;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import bsh.EvalError;
import bsh.Interpreter;

public class JavaVsBeanshellIterationTest {
	private static final int NANOS = 1000000000;
	private static final int ITEMS_COUNT = 1000;
	private static final int REPETITION_COUNT = 100;
	private List<Item> items;
	private Interpreter interpreter;
	
	@Before
	public void initInterpreter() {
		interpreter = new Interpreter(new InputStreamReader(getClass().getResourceAsStream("/iterate-and-set.bsh")),
				System.out, System.err, false);
	}
	
	@Before
	public void initFixtures() {
		items = new ArrayList<Item>(ITEMS_COUNT);
		for(int i = 0; i < ITEMS_COUNT; i++) {
			items.add(new Item(i));
		}
	}
	
	@Test
	public void iterateAndSetCurrentDateOnMultiplesOfThree() throws Exception {
		double start = System.nanoTime();
		for(int i = 0; i < REPETITION_COUNT; i++) {
			iterateAndSet(items);
		}
		double stop = System.nanoTime();
		double averageTime = (stop - start) / REPETITION_COUNT;
		System.out.println("Average time for Java code: " + averageTime / NANOS);
		double startBsh = System.nanoTime();
		items = iterateAndSetUsingBsh(items);
		double stopBsh = System.nanoTime();
		double bshAverageTime = (stopBsh - startBsh) / REPETITION_COUNT;
		System.out.println("Average time for BSH: " + bshAverageTime / NANOS);
		assertTrue(averageTime < bshAverageTime);
		
		double bshOnJavaRatio = bshAverageTime / averageTime;
		System.out.println(bshOnJavaRatio);
		assertTrue(bshOnJavaRatio > 10);
	}

	private void iterateAndSet(List<Item> items) {
		Date now = new Date();
		for (Item item : items) {
			if(item.getNumber() % 3 == 0) {
				item.setModificationDate(now);
			}
		}
	}
	
	private List<Item> iterateAndSetUsingBsh(List<Item> items) throws EvalError, FileNotFoundException, IOException {
		interpreter.set("items", items);
		interpreter.run();
		List<Item> modified = (List<Item>) interpreter.get("items");
		return modified;
	}
}
