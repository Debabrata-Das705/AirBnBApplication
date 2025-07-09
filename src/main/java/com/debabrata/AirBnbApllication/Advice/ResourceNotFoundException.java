package com.debabrata.AirBnbApllication.Advice;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String s){
        super(s);
    }
}
