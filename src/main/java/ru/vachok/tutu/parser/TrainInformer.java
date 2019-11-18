package ru.vachok.tutu.parser;


import ru.vachok.tutu.conf.BackEngine;
import ru.vachok.tutu.conf.InformationFactory;
import ru.vachok.tutu.conf.MessageToUser;
import ru.vachok.tutu.data.TrainsKeeper;

import java.util.*;


public class TrainInformer implements InformationFactory {
    
    
    private MessageToUser messageToUser = MessageToUser.getInstance();
    
    private int numOfTrains = 2;
    
    private int stationCodeFrom;
    
    private int stationCodeTo;
    
    public TrainInformer() {
        this.stationCodeFrom = 37805;
        this.stationCodeTo = 37505;
    }
    
    public TrainInformer(int stationCodeFrom, int stationCodeTo) {
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
            messageToUser.error(TrainInformer.class.getSimpleName(), e.getMessage(), " see line: 57 ***");
            this.numOfTrains = 2;
        }
        return getInfo();
    }
    
    @SuppressWarnings("ChainOfInstanceofChecks") @Override
    public void setClassOption(Object classOption) {
        if (classOption instanceof Integer) {
            this.numOfTrains = (int) classOption;
        }
        if (classOption instanceof int[]) {
            int[] stations = (int[]) classOption;
            this.stationCodeFrom = stations[0];
            this.stationCodeTo = stations[1];
        }
        
    }
    
    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("SiteParser{");
        sb.append("messageToUser=").append(messageToUser);
        sb.append('}');
        return sb.toString();
    }
    
}
