package com.example.crm.exceptions.handlers;

public class EmptyPageException extends RuntimeException {
    public EmptyPageException(){
        super("Record Page is empty");
    }

    public EmptyPageException(String message){
        super(message);
    }
}
