package ru.vachok.tutu.excepthandlers;


import java.text.MessageFormat;


public class TODOException extends RuntimeException {
    
    private String noteToDo;
    
    public TODOException(String noteToDo) {
        this.noteToDo = noteToDo;
    }
    
    @Override public String getMessage() {
        return MessageFormat.format("{0} {1}",noteToDo, this.getLocalizedMessage());
    }
}
