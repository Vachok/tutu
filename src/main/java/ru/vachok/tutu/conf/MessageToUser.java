package ru.vachok.tutu.conf;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.vachok.tutu.logimpl.LocalMessenger;


public interface MessageToUser {
    
    String LOCAL_CONSOLE = "LocalMessenger";
    
    @NotNull @Contract(pure = true) static MessageToUser getInstance() {
        return new LocalMessenger();
    }
    
    void error(String headerMsg, String titleMsg, String bodyMsg);
    
    void info(String bodyMsg);
}
