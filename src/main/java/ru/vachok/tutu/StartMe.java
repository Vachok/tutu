package ru.vachok.tutu;


import ru.vachok.tutu.conf.AbstractForms;
import ru.vachok.tutu.conf.BackEngine;
import ru.vachok.tutu.conf.MessageToUser;
import ru.vachok.tutu.parser.SiteParser;


@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public class StartMe {
    
    
    public static void main(String[] args) {
        Thread.currentThread().setName(StartMe.class.getSimpleName());
        BackEngine backEngine = new SiteParser();
        MessageToUser.getInstance().info(AbstractForms.fromArray(backEngine.getComingTrains()));
        Runtime.getRuntime().runFinalization();
        System.exit(0);
    }
    
}
