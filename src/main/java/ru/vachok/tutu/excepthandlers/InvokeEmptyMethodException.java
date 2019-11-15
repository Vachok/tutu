package ru.vachok.tutu.excepthandlers;


import java.text.MessageFormat;


public class InvokeEmptyMethodException extends RuntimeException {
    
    
    private String location;
    
    public InvokeEmptyMethodException(String location) {
        this.location = location;
    }
    
    @Override public String getMessage() {
        return MessageFormat.format("{0}\n{1}", location, this.getStackTrace());
    }
}
