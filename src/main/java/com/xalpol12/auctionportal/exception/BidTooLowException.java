package com.xalpol12.auctionportal.exception;

public class BidTooLowException extends RuntimeException {

    public BidTooLowException(String message) {
        super(message);
    }
}
