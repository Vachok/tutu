package ru.vachok.tutu.logimpl;


import ru.vachok.tutu.conf.MessageToUser;
import ru.vachok.tutu.excepthandlers.TODOException;

import java.text.MessageFormat;
import java.util.logging.Logger;


public class LocalMessenger implements MessageToUser {
    
    
    private String headerMsg;
    
    private String titleMsg;
    
    private String bodyMsg;
    
    @Override public void error(String headerMsg, String titleMsg, String bodyMsg) {
        this.headerMsg = headerMsg;
        this.titleMsg = titleMsg;
        this.bodyMsg = bodyMsg;
        
        Logger logger = Logger.getLogger(headerMsg);
        logger.severe(MessageFormat.format("{0}: {1}", titleMsg, bodyMsg));
    }
    
    @Override public void info(String bodyMsg) {
        this.bodyMsg = bodyMsg;
        this.titleMsg = "";
        this.headerMsg = Thread.currentThread().getName();
        Logger logger = Logger.getLogger(headerMsg);
        logger.info(bodyMsg);
    }
    
    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("LocalMessenger{");
        sb.append("titleMsg='").append(titleMsg).append('\'');
        sb.append(", headerMsg='").append(headerMsg).append('\'');
        sb.append(", bodyMsg='").append(bodyMsg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
