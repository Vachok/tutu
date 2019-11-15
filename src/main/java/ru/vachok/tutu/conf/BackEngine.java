package ru.vachok.tutu.conf;


import java.util.Date;
import java.util.List;


@FunctionalInterface public interface BackEngine {
    
    List<Date> getComingTrains();
    
}
