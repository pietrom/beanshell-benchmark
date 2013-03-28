package org.amicofragile.benchmarks.beanshell;

import java.util.Date;

public class Item {
	private final int number;
	private Date modificationDate;
	
	public Item(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
}
