package ru.vachok.tutu.parser;


import ru.vachok.tutu.conf.BackEngine;
import ru.vachok.tutu.conf.InformationFactory;
import ru.vachok.tutu.conf.MessageToUser;
import ru.vachok.tutu.data.TrainsKeeper;

import java.util.*;


public class SiteParser implements InformationFactory {
    
    
    private MessageToUser messageToUser = MessageToUser.getInstance();
    
    private int numOfTrains = 2;
    
    private int stationCodeFrom;
    
    private int stationCodeTo;
    
    public SiteParser() {
        this.stationCodeFrom = 37805;
        this.stationCodeTo = 37505;
    }
    
    public SiteParser(int stationCodeFrom, int stationCodeTo) {
        this.stationCodeFrom = stationCodeFrom;
        this.stationCodeTo = stationCodeTo;
    }
    
    @Override
    public String getInfo() {
        BackEngine backEngine = new TrainsKeeper(stationCodeFrom, stationCodeTo);
        Map<Date, String> trains = backEngine.getComingTrains();
        String[] strings = new String[numOfTrains];
        Queue<String> stringQueue = new LinkedList<>();
        for (Map.Entry<Date, String> entry : trains.entrySet()) {
            stringQueue.add(entry.getKey() + " - " + entry.getValue());
        }
        for (int i = 0; i < numOfTrains; i++) {
            String add = stringQueue.remove();
            strings[i] = add;
        }
        
        return Arrays.toString(strings);
    }
    
    @Override
    public String getInfoAbout(Object aboutWhat) {
        try {
            this.numOfTrains = Integer.parseInt((String) aboutWhat);
        }
        catch (NumberFormatException | ClassCastException e) {
            messageToUser.error(SiteParser.class.getSimpleName(), e.getMessage(), " see line: 57 ***");
            this.numOfTrains = 2;
        }
        return getInfo();
    }
    
    @Override
    public void setClassOption(Object classOption) {
        this.numOfTrains = (int) classOption;
    }
    
    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("SiteParser{");
        sb.append("messageToUser=").append(messageToUser);
        sb.append('}');
        return sb.toString();
    }
    
}
