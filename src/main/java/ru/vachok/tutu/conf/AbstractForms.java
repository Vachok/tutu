package ru.vachok.tutu.conf;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;


@SuppressWarnings("All") public abstract class AbstractForms {
    
    
    @NotNull public static String fromArray(@NotNull Exception e) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(e.getClass().getSimpleName()).append("\n");
        stringBuilder.append(e.getMessage());
        for (StackTraceElement traceElement : e.getStackTrace()) {
            stringBuilder.append(traceElement.toString());
        }
        if(e.getSuppressed()!=null){
            for (Throwable throwable : e.getSuppressed()) {
                for (StackTraceElement traceElement : throwable.getStackTrace()) {
                    stringBuilder.append(traceElement.toString()).append("\n");
                }
            }
        }
        
        return stringBuilder.toString();
    }
    
    @NotNull public static String networkerTrace(@NotNull StackTraceElement[] traceElements) {
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement traceElement:traceElements){
            String elString = traceElement.toString();
            if(traceElement.toString().contains("ru.vachok")) {
                stringBuilder.append(elString).append("\n");
            }
        }
        return stringBuilder.toString();
    }
    
    @NotNull public static String fromArray(@NotNull List<?> trains) {
        StringBuilder stringBuilder = new StringBuilder();
        for(Object o:trains){
            stringBuilder.append(o.toString()).append("\n");
        }
        return stringBuilder.toString();
    }
    
    @NotNull
    public static String fromArray(@NotNull Stream<Map.Entry<?, ?>> limit) {
        StringBuilder stringBuilder = new StringBuilder();
        limit.forEach(entry->{
            stringBuilder.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
        });
        return stringBuilder.toString();
    }
    
    public static String fromArray(Map<?, ?> trains) {
        StringBuilder stringBuilder = new StringBuilder();
        Set<?> keys = trains.keySet();
        keys.forEach(key->{
            stringBuilder.append(key).append(" - ").append(trains.get(key)).append("\n");
        });
        return stringBuilder.toString();
    }
}
