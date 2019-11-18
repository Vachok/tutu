package ru.vachok.tutu.logimpl;


import ru.vachok.tutu.conf.MessageToUser;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalTime;
import java.util.logging.FileHandler;
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
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler(String.format("%s.%d", headerMsg, LocalTime.now().toSecondOfDay()).toLowerCase());
            logger.addHandler(fileHandler);
        
        }
        catch (IOException e) {
            warn(LocalMessenger.class.getSimpleName(), e.getMessage(), " see line: 34 ***");
        }
        finally {
            logger.severe(MessageFormat.format("{0}: {1}", titleMsg, bodyMsg));
            logger.removeHandler(fileHandler);
        }
    }
    
    @Override public void info(String bodyMsg) {
        this.bodyMsg = bodyMsg;
        this.titleMsg = "";
        this.headerMsg = Thread.currentThread().getName();
        Logger logger = Logger.getLogger(headerMsg);
        logger.info(bodyMsg);
    }
    
    @Override
    public void error(String bodyMsg) {
        this.headerMsg = "Error";
        this.titleMsg = "";
        this.bodyMsg = bodyMsg;
        error(headerMsg, titleMsg, bodyMsg);
    }
    
    @Override
    public void warn(String headerMsg, String titleMsg, String bodyMsg) {
        this.headerMsg = headerMsg;
        this.titleMsg = titleMsg;
        this.bodyMsg = bodyMsg;
        Logger logger = Logger.getLogger(headerMsg);
        logger.warning(MessageFormat.format("{0}: {1}", titleMsg, bodyMsg));
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
