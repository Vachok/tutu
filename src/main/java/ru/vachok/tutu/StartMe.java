package ru.vachok.tutu;


import ru.vachok.tutu.conf.InformationFactory;
import ru.vachok.tutu.conf.MessageToUser;


@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public class StartMe {
    
    
    public static void main(String[] args) {
        Thread.currentThread().setName(StartMe.class.getSimpleName());
        MessageToUser.getInstance().info(InformationFactory.getInstance().getInfo());
        Runtime.getRuntime().runFinalization();
        System.exit(0);
    }
    
}
