package ru.vachok.tutu.conf;


public abstract class AbstractForms {
    
    
    public static String fromArray(Exception e) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(e.getClass().getSimpleName()).append("\n");
        stringBuilder.append(e.getMessage());
        for (StackTraceElement traceElement : e.getStackTrace()) {
            stringBuilder.append(traceElement.toString());
        }
        if(e.getSuppressed()!=null){
            for (Throwable throwable : e.getSuppressed()) {
                for (StackTraceElement traceElement : throwable.getStackTrace()) {
                    stringBuilder.append(traceElement.toString());
                }
            }
        }
        
        return stringBuilder.toString();
    }
}
