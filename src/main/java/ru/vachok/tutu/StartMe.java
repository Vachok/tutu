package ru.vachok.tutu;


import ru.vachok.tutu.conf.InformationFactory;
import ru.vachok.tutu.conf.MessageToUser;
import ru.vachok.tutu.parser.SiteParser;


@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public class StartMe {
    
    
    public static void main(String[] args) {
        Thread.currentThread().setName(StartMe.class.getSimpleName());
        InformationFactory backEngine = new SiteParser();
        MessageToUser.getInstance().info(backEngine.getInfo());
        Runtime.getRuntime().runFinalization();
        System.exit(0);
    }
    
}
