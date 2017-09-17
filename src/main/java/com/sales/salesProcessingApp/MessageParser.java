package com.sales.salesProcessingApp;


/**
 * This is for parsing each message type.
 * @author thejas
 *
 */
public class MessageParser {

    // Parsed product type
    private String productName;

    // Parsed product price
    private double productPrice;

    // Parsed product quantity
    private int productQuantity;

    // Parsed product operatorType e.g Add, Subtract
    private String operatorType;
    
    private boolean parseMessage;
    
    private String messageType;

    public boolean isParseMessage() {
		return parseMessage;
	}

	public void setParseMessage(boolean parseMessage) {
		this.parseMessage = parseMessage;
	}
	
	 public MessageParser() {
		 
	 }

	// Constructor on initialisation parses the
    // product details.
    public MessageParser(String message) {
        this.productName = "";
        this.productPrice = 0.0;
        this.productQuantity = 0;
        this.operatorType = "";
        parseMessage = parseMessage(message);
    }

    // Validates the message and identifies the message type to get it
    // parsed properly to obtain product details.
    // @return[Boolean] false on wrong message else returns true
    private boolean parseMessage(String message) {
        if (message == null || message.isEmpty()) {
            return false;
        }
        String[] messageArray = message.trim().split("\\s+");
        String firstWord = messageArray[0];
        if (firstWord.matches("Add|Subtract|Multiply")) {
        		messageType = "Message3";
            return parseMessageType3(messageArray);
        } else if (firstWord.matches("^\\d+")) {
        		messageType = "Message2";
            return parseMessageType2(messageArray);
        } else if (messageArray.length == 3 && messageArray[1].contains("at")) {
        		messageType = "Message1";
            return parseMessageType1(messageArray);
        } else {
            System.out.println("Wrong sales notice");
        }
        return true;
    }

    public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	// Parse message type 1
    private boolean parseMessageType1(String[] messageArray) {
        if(messageArray.length > 3 || messageArray.length < 3) return false;
        productName = messageArray[0];
        productPrice = parsePrice(messageArray[2]);
        productQuantity = 1; //Will always be 1
        return true;
    }

    // Parse message type 2
    private boolean parseMessageType2(String[] messageArray) {
        if(messageArray.length > 7 || messageArray.length < 7) return false;
        productName = messageArray[3];
        productPrice = parsePrice(messageArray[5]);
        productQuantity = Integer.parseInt(messageArray[0]);
        return true;
    }

    // Parse message type 3
    private boolean parseMessageType3(String[] messageArray) {
        if(messageArray.length > 3 || messageArray.length < 3) return false;
        operatorType = messageArray[0];
        productName = messageArray[2];
        productQuantity = 0;
        productPrice = parsePrice(messageArray[1]);
        return true;
    }


    // Parse the price and get only the value
    // @return[double] e.g "20p" will become 0.20
    public double parsePrice(String rawPrice) {
        double price = Double.parseDouble(rawPrice.replaceAll("£|p", ""));
        if (!rawPrice.contains(".")) {
            price = Double.valueOf(Double.valueOf(price) / Double.valueOf("100"));
        }
        return price;
    }

    // Get the product type
    public String getproductName() {
        return productName;
    }

    // Get the product price
    public double getProductPrice() {
        return productPrice;
    }

    // Get the operator type
    public String getOperatorType() {
        return operatorType;
    }

    // Get the product quantity
    public int getProductQuantity() {
        return productQuantity;
    }


}