package com.xalpol12.auctionportal.exception;

public class BidOutOfTimeException extends RuntimeException {
    public BidOutOfTimeException(String message) {
        super(message);
    }
}
