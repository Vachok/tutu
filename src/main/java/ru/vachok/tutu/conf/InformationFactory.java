package ru.vachok.tutu.conf;


/**
 @since 16.11.2019 (10:52) */
public interface InformationFactory {
    
    
    String getInfo();
    
    void setClassOption(Object classOption);
    
    String getInfoAbout(Object aboutWhat);
    
}