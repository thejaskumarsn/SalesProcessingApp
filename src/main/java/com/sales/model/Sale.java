package com.sales.model;

/**
 * This is the model class for Sale notification.
 * @author thejas
 *
 */
public class Sale {

	private String productName;
	
	private double productValue;
	
	private int productQuantity;
	
	

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductValue() {
		return productValue;
	}

	public void setProductValue(double productValue2) {
		this.productValue = productValue2;
	}
}
