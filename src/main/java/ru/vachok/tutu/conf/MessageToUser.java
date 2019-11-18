package ru.vachok.tutu.conf;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.vachok.tutu.logimpl.LocalMessenger;


public interface MessageToUser {
    
    
    @Contract(pure = true)
    static @NotNull MessageToUser getInstance() {
        return new LocalMessenger();
    }
    
    void error(String headerMsg, String titleMsg, String bodyMsg);
    
    void info(String bodyMsg);
    
    void error(String message);
    
    void warn(String headerMsg, String titleMsg, String bodyMsg);
}
