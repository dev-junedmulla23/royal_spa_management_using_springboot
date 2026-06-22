package com.example.crm.exceptions.handlers;

public class EmptyListException extends RuntimeException {
    public EmptyListException(){
        super("Record list is empty");
    }

    public EmptyListException(String message){
        super(message);
    }
}
