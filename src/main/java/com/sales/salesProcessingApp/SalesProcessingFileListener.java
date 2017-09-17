package com.sales.salesProcessingApp;

/**
 * Main class which acts as an API for listening to sales notification as file.
 * The location where the consumer(s) place the file should be captured in the code. 
 * The variable 'filePath' should be updated to the path required.
 */

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sales.model.Sale;

public class SalesProcessingFileListener {
	static String filePath = "/Users/thejas/Developer/test";

	public static void main(String[] args) {
		try {
			int salesCounter = 0;

			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get(filePath);
			dir.register(watcher, ENTRY_CREATE);
			SalesProcessor processor = new SalesProcessor();
			System.out.println("Waiting for sales notification");

			while (true) {
				WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException ex) {
					return;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();

					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();

					// MessageParser helps to parse the incoming messages and obtain product sale
					// information.
					MessageParser messageParser;
					List<String> fileContentList = new ArrayList<>();

					try (Stream<String> stream = Files.lines(dir.resolve(fileName.getFileName()))) {

						fileContentList = stream.collect(Collectors.toList());

					} catch (IOException e) {
						e.printStackTrace();
					}
					Files.delete(dir.resolve(fileName.getFileName()));

					for (String s : fileContentList) {
						salesCounter++;

						// Process the given message
						messageParser = new MessageParser(s);

						// Get the product type e.g 'apple'
						String productName = messageParser.getproductName();
						double productValue = messageParser.getProductPrice();
						int productQuantity = messageParser.getProductQuantity();

						// Check if product type is empty return false and do nothing.
						if (messageParser.isParseMessage()) {
							if (messageParser.getMessageType().equals("Message3")) {

								processor.adjustTransactions(messageParser.getOperatorType(),
										productName.substring(0, productName.length() - 1),
										String.valueOf(messageParser.getProductPrice()));
							} else if (messageParser.getMessageType().equals("Message2")) {

								Sale sale = new Sale();
								sale.setProductName(productName.substring(0, productName.length() - 1));
								sale.setProductValue(productValue);
								sale.setProductQuantity(productQuantity);
								processor.processTransaction(sale);

							} else {
								Sale sale = new Sale();
								sale.setProductName(productName);
								sale.setProductValue(productValue);
								sale.setProductQuantity(productQuantity);
								processor.processTransaction(sale);

							}

							if (Integer.valueOf(salesCounter) == 49) {

								System.out.println(
										"Application reached 50 messages and cannot process further. The following are the adjustment records made;\n");
								ReportingService ser = new ReportingService();
								ser.generateReportLog(processor.getSalesList());
								System.exit(1);
							}
							// if((salesCounter % 10) == 0 && salesCounter != 0) {
							if ((salesCounter % 10) == 0 && salesCounter != 0) {
								ReportingService ser = new ReportingService();
								System.out.println("10 sales notification");
								ser.generateReportLog(processor.getSalesList());
							}
						}
					}

				}

				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		} catch (IOException ex) {
			System.err.println(ex);
		}

	}

}