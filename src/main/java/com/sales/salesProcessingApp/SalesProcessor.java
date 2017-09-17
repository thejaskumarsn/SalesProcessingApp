package com.sales.salesProcessingApp;

/**
 * This has the logic for processing the sales and storing it in-memory.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sales.model.Sale;

public class SalesProcessor {

	private Map<String, List<Sale>> salesList;

	public Map<String, List<Sale>> getSalesList() {
		return salesList;
	}

	public void setSalesList(Map<String, List<Sale>> salesList) {
		this.salesList = salesList;
	}

	public SalesProcessor() {
		salesList = new HashMap<String, List<Sale>>();
	}

	public void processTransaction(Sale sale) {

		String productName = sale.getProductName();
		List<Sale> productSaleList;
		if (salesList.get(productName) == null) {
			productSaleList = new ArrayList<Sale>();
			productSaleList.add(sale);
			salesList.put(productName, productSaleList);
		} else {
			productSaleList = salesList.get(productName);
			productSaleList.add(sale);
		}
	}

	public boolean adjustTransactions(String operator, String productName, String adjustPrice) {
		if (salesList.get(productName) != null) {
			List<Sale> productSaleList = salesList.get(productName);
			MessageParser parser = new MessageParser();
			double convertedPrice = Double.parseDouble(adjustPrice);
			
			parser.parsePrice(adjustPrice);
			for (Sale sale : productSaleList) {
				double newPrice = 0;
				if(operator.equalsIgnoreCase("Add")) {
					newPrice = Math.round((sale.getProductValue() + convertedPrice) * 100D) / 100D;
				} else if(operator.equalsIgnoreCase("Subtract")) {
					newPrice = Math.round((sale.getProductValue() - convertedPrice) * 100D) / 100D;
				} else if(operator.equalsIgnoreCase("Multiply")) {
					newPrice = Math.round((sale.getProductValue() *convertedPrice) * 100D) / 100D;
				} else {
					System.out.println("Invalid adjustment operator");
					return false;
				}
				
				sale.setProductValue(newPrice);
			}
			return true;
		} else {
			System.out.println("Product not found for adjustment");
			return false;
		}

	}
}
