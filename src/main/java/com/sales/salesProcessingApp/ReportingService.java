package com.sales.salesProcessingApp;

/**
 * This is used for reporting to the system log.
 */
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sales.model.Sale;

public class ReportingService {

	public void generateReportLog(Map<String, List<Sale>> salesList) {
		
		System.out.println("*************** Sales Report *****************");
		System.out.println("|Product\t\t|Quantity\t\t|Value\t|");
		System.out.println("-------------------------------------------");
		for (Entry<String, List<Sale>> entry : salesList.entrySet()) {
			String productName = entry.getKey();
			List<Sale> list = entry.getValue();
			int totalProdQuantity = 0;
			double totalProdValue = 0;
			for (Sale sal : list) {
				totalProdQuantity += sal.getProductQuantity();
				double eachSaleValue = sal.getProductQuantity() * sal.getProductValue();

				totalProdValue = Math.round((totalProdValue + eachSaleValue) * 100D) / 100D;
			}
			System.out.println("|" + productName + "\t\t|" + totalProdQuantity + "\t\t|" + totalProdValue + "\t|");
		}

		System.out.println("-------------------------------------------");
		System.out.println("End\n\n");
	}
}
