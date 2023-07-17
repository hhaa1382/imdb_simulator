package com.example.imdp.Exeptions;

public class InvalidInput extends RuntimeException{
    public InvalidInput() {
        super("Error");
    }

    public InvalidInput(String message) {
        super("Error : "+message);
    }
}
