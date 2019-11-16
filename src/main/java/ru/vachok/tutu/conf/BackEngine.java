package ru.vachok.tutu.conf;


import java.util.Date;
import java.util.Map;


@FunctionalInterface public interface BackEngine {
    
    
    Map<Date, String> getComingTrains();
    
}
